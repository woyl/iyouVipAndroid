package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.RedPacket.RedPacketPresenter;
import com.jfkj.im.mvp.RedPacket.RedPacketView;
import com.jfkj.im.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class RedPacketActivity extends BaseActivity<RedPacketPresenter> implements View.OnClickListener, TextWatcher , RedPacketView {
    @BindView(R.id.et_number)
    EditText et_number;
    @BindView(R.id.et_red_number)
    EditText et_red_number;
    @BindView(R.id.et_hint)
    EditText et_hint;
    @BindView(R.id.tv_number_people)
    TextView tv_number_people;
    @BindView(R.id.send_red_btn)
    Button send_red_btn;
    @BindView(R.id.back_iv)
    ImageView back_iv;
    String personcount;
    RedPacketPresenter redPacketPresenter;
    Intent intent;
    @BindView(R.id.tv_rednumber)
    TextView tv_rednumber;
    String  receiveid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back_iv.setOnClickListener(this);
        send_red_btn.setOnClickListener(this);
        et_number.addTextChangedListener(this);
        et_red_number.addTextChangedListener(this);
        redPacketPresenter = new RedPacketPresenter(this);
        intent = getIntent();
        receiveid=intent.getStringExtra(Utils.RECEIVEID);
        if(receiveid.equals("0")){
            tv_number_people.setVisibility(View.GONE);
        }else {
            redPacketPresenter.RedPacket(receiveid);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_redpacket;
    }

    @Override
    public RedPacketPresenter createPresenter() {
        return redPacketPresenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_red_btn:
                if (Utils.netWork()) {
                    if(receiveid.equals("0")){
                        if (et_hint.getText().toString().trim().length() > 0) {
                            redPacketPresenter.sendSquareRedPacket(et_number.getText().toString().trim(),et_red_number.getText().toString().trim(),et_hint.getText().toString().trim());
                        } else {
                            redPacketPresenter.sendSquareRedPacket(et_number.getText().toString().trim(),et_red_number.getText().toString().trim(),"恭喜发财，大吉大利!");
                        }
                    }else {
                        if (et_hint.getText().toString().trim().length() > 0) {
                            redPacketPresenter.sendRedPacked(receiveid,et_number.getText().toString().trim(),et_red_number.getText().toString().trim(),et_hint.getText().toString().trim());
                        } else {
                            redPacketPresenter.sendRedPacked(receiveid,et_number.getText().toString().trim(),et_red_number.getText().toString().trim(),"恭喜发财，大吉大利!");
                        }
                    }
                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.back_iv:
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(et_number.getText().toString().trim()) || TextUtils.isEmpty(et_red_number.getText().toString().trim()) ) {
                send_red_btn.setEnabled(false);
                send_red_btn.setAlpha(0.5f);
                }else {
                send_red_btn.setEnabled(true);
                send_red_btn.setAlpha(1.0f);
                }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void RedPacketSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").trim().equals("200")) {
                personcount = jsonObject.getJSONObject("data").getInt("PersonCount") + "";
                tv_number_people.setText("(本群共" + personcount + "人)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendRedPacked(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            if(jsonObject.getString("code").equals("200")){
                intent.putExtra("result",string);
                setResult(RESULT_OK, intent);
                finish();
            }else {
                tv_rednumber.setText(jsonObject.getString("message"));
                tv_number_people.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendSquareRedPacketSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            if(jsonObject.getString("code").equals("200")){
                intent.putExtra("result",string);
                setResult(RESULT_OK, intent);
                finish();
            }else{
                tv_rednumber.setText(jsonObject.getString("message"));
                tv_number_people.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
