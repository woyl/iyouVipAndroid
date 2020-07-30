package com.jfkj.im.ui.activity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.utils.AppManager;

import butterknife.BindView;

public class ClubnoticeActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.club_et)
    EditText club_et;
    @BindView(R.id.next_tv)
    TextView next_tv;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;

    private  Bundle bundle;
    private String type,content;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_clubnotice;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        bundle = getIntent().getBundleExtra(TUIKitConstants.Selection.CONTENT);
        if (bundle != null) {
            type = bundle.getString("type");
        }
        if (TextUtils.equals(type,"group_notice")){
            String defaultString = bundle.getString(TUIKitConstants.Selection.INIT_CONTENT);
            if (!TextUtils.isEmpty(defaultString)){
                tv_number.setText(defaultString.length() + "/50");
                next_tv.setEnabled(true);
                next_tv.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_base_btn_white_20dp));
                next_tv.setText("确认");
            }else {
                next_tv.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_base_btn_white_20dp));
                next_tv.setAlpha(0.3f);
                next_tv.setText("下一步");
                next_tv.setEnabled(false);
            }
            club_et.setText(defaultString);
            if (defaultString != null) {
                club_et.setSelection(defaultString.length());
            }
        }

        setAndroidNativeLightStatusBar(this,false);
        setStaturBar(constraint_head);
        title_left_tv.setBackgroundResource(R.mipmap.iv_back_black);
        title_left_tv.setOnClickListener(this);
        next_tv.setOnClickListener(this);
        club_et.addTextChangedListener(this);
        next_tv.setEnabled(false);
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_tv:
                finish();
                break;
            case R.id.next_tv:
                if (TextUtils.equals(type,"group_notice")){
                    if (sOnResultReturnListener != null) {
                        sOnResultReturnListener.onReturn(club_et.getText().toString());
                    }
                    finish();
                }else {
                    Intent intent = new Intent(App.getAppContext(), SelectpacketsActivity.class);
                    intent.putExtra("name",bundle.getString("name"));
                    intent.putExtra("notice",club_et.getText().toString().trim());
                    intent.putExtra("group",false);
                    startActivity(intent);
                }
//                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        tv_number.setText(club_et.getText().toString().trim().length() + "/50");
        if (club_et.getText().toString().trim().length() == 0) {
            next_tv.setAlpha(0.3f);
            next_tv.setEnabled(false);
            next_tv.setAlpha(0.3f);
        } else {
            next_tv.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_tv_three_bg_gray_s));
            next_tv.setAlpha(1f);
            next_tv.setEnabled(true);
            next_tv.setAlpha(1f);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }



    /**group_set*/

    private static OnResultReturnListener sOnResultReturnListener;

    public static void startTextSelection(Context context, Bundle bundle, OnResultReturnListener listeners) {
        startSelection(context, bundle, listeners);
    }

    private static void startSelection(Context context, Bundle bundle, OnResultReturnListener listener) {
        Intent intent = new Intent(context, ClubnoticeActivity.class);
        intent.putExtra(TUIKitConstants.Selection.CONTENT, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        sOnResultReturnListener = listener;
    }

    public interface OnResultReturnListener {
        void onReturn(Object res);
    }

}
