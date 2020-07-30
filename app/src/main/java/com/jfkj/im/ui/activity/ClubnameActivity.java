package com.jfkj.im.ui.activity;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.group.info.GroupInfoPresenter;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.CheckGroupName.CheckGroupNamePresenter;
import com.jfkj.im.mvp.CheckGroupName.CheckGroupNameView;
import com.jfkj.im.mvp.createGroup.CreateGroupPresenter;
import com.jfkj.im.mvp.createGroup.CreateGroupView;
import com.jfkj.im.utils.AppManager;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMValueCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class ClubnameActivity extends BaseActivity<CheckGroupNamePresenter> implements TextWatcher, View.OnClickListener, CheckGroupNameView {
    @BindView(R.id.club_et)
    EditText club_et;
    @BindView(R.id.clear_et_iv)
    ImageView clear_et_iv;
    @BindView(R.id.next_tv)
    TextView next_tv;
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;

    CheckGroupNamePresenter checkGroupNamePresenter;

    private String type,content;
    private static GroupInfoPresenter groupInfoPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_clubname;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        Bundle bundle = getIntent().getBundleExtra(TUIKitConstants.Selection.CONTENT);
        if (bundle != null) {
            type = bundle.getString("type");
        }
        if (TextUtils.equals(type,"group_set")){
            String defaultString = bundle.getString(TUIKitConstants.Selection.INIT_CONTENT);
            if (!TextUtils.isEmpty(defaultString)){
                next_tv.setEnabled(true);
                next_tv.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_tv_three_bg_gray_s));
                clear_et_iv.setVisibility(View.VISIBLE);
            }else {
                next_tv.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_tv_three_bg_gray));
                next_tv.setEnabled(false);
                clear_et_iv.setVisibility(View.GONE);
            }
            club_et.setText(defaultString);
            if (defaultString != null) {
                club_et.setSelection(defaultString.length());
            }
        }else {
            groupInfoPresenter = null;
        }
        setAndroidNativeLightStatusBar(this,false);
        setStaturBar(constraint_head);
        club_et.addTextChangedListener(this);
        clear_et_iv.setVisibility(View.GONE);
        title_left_tv.setOnClickListener(this);
        clear_et_iv.setOnClickListener(this);
        next_tv.setOnClickListener(this);
        checkGroupNamePresenter = new CheckGroupNamePresenter(this);
        title_left_tv.setBackgroundResource(R.mipmap.iv_back_black);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_et_iv:
                club_et.setText("");
                break;
            case R.id.next_tv:
                if(Utils.netWork()){
                    if (TextUtils.isEmpty(club_et.getText().toString().trim())) {
                        toastShow("请输入俱乐部名称");
                    } else {
                        checkGroupNamePresenter.CheckGroupName(club_et.getText().toString().trim());
                    }
                }else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.title_left_tv:
                finish();
                break;
        }
    }

    @Override
    public CheckGroupNamePresenter createPresenter() {
        return checkGroupNamePresenter;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (club_et.getText().toString().trim().length() > 0) {
//            next_tv.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_tv_three_bg_gray_s));
            next_tv.setAlpha(1f);
            next_tv.setEnabled(true);
            clear_et_iv.setVisibility(View.VISIBLE);
        } else {
            next_tv.setAlpha(0.3f);
//            next_tv.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_tv_three_bg_gray));
            next_tv.setEnabled(false);
            clear_et_iv.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void CheckGroupNameSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            String code = jsonObject.getString("code");
            String message = jsonObject.getString("message");
            if(code.equals("200")) {
                if (TextUtils.equals(type, "group_set")) {
                    if (groupInfoPresenter != null){
                        groupInfoPresenter.modifyGroupName(club_et.getText().toString(), new TIMValueCallBack<Object>() {
                            @Override
                            public void onError(int code, String desc) {
                                toastShow(desc);
                            }

                            @Override
                            public void onSuccess(Object o) {
                                if (sOnResultReturnListener != null) {
                                    sOnResultReturnListener.onReturn(club_et.getText().toString());
                                }
                                finish();
                            }
                        });
                    }
                } else {
                    Intent intent = new Intent(App.getAppContext(), ClubnoticeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "club_name");
                    bundle.putString("name", club_et.getText().toString().trim());
                    intent.putExtra(TUIKitConstants.Selection.CONTENT, bundle);
                    startActivity(intent);
                }

            }else if (code.equals("70015")){
                toastShow(message);
            }
//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**group_set*/

    private static OnResultReturnListener sOnResultReturnListener;

    public static void startTextSelection(Context context, Bundle bundle, GroupInfoPresenter mPresenter, OnResultReturnListener listeners) {
//        bundle.putInt(TUIKitConstants.Selection.TYPE, TUIKitConstants.Selection.TYPE_TEXT);
        startSelection(context, bundle, listeners,mPresenter);
    }

    private static void startSelection(Context context, Bundle bundle, OnResultReturnListener listener,GroupInfoPresenter mPresenter) {
        groupInfoPresenter = mPresenter;
        Intent intent = new Intent(context, ClubnameActivity.class);
        intent.putExtra(TUIKitConstants.Selection.CONTENT, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        sOnResultReturnListener = listener;
    }

    public interface OnResultReturnListener {
        void onReturn(Object res);
    }


    @Override
    public void CheckGroupNamefail(String s) {
        toastShow(R.string.nonetwork);
    }
}
