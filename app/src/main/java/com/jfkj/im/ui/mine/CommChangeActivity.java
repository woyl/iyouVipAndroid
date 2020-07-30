package com.jfkj.im.ui.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.TIM.component.photoview.Util;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.MessageEvent;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.mine.EditUserInfoPresenter;
import com.jfkj.im.mvp.mine.EditUserInfoView;
import com.jfkj.im.utils.KeyBoardUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 * Description:  修改信息页面（昵称 描述）
 * @author :   ys
 * @date :         2019/11/21
 * </pre>
 */
public class CommChangeActivity extends BaseActivity<EditUserInfoPresenter> implements EditUserInfoView {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.edit_input)
    EditText mEditInput;
    @BindView(R.id.iv_clear_input)
    ImageView mIvClearInput;
    @BindView(R.id.layout_input)
    LinearLayout mLayoutInput;
    @BindView(R.id.edit_input_content)
    EditText mEditInputContent;
    @BindView(R.id.tv_text_num)
    TextView mTvTextNum;
    @BindView(R.id.layout_input_content)
    ConstraintLayout mLayoutInputContent;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;
    @BindView(R.id.tv_nickname_hint)
    TextView mTvNicknameHint;


    @BindView(R.id.constraintlayout)
    ConstraintLayout cl_school;

    @BindView(R.id.edit_input_school)
    EditText edit_input_school;

    @BindView(R.id.tv_text_num2)
    TextView tv_text_num2;

    private String string;
    private String nickName = "";
    private String describe = "";
    private String school = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_common_change;
    }

    @Override
    public EditUserInfoPresenter createPresenter() {
        return new EditUserInfoPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            string = bundle.getString("TAG");
        }
        if ("nickName".equals(string)) {
            mTvName.setText("修改昵称");
            mLayoutInput.setVisibility(View.VISIBLE);
            mLayoutInputContent.setVisibility(View.GONE);
            mTvNicknameHint.setVisibility(View.GONE);
            cl_school.setVisibility(View.GONE);
        } else if ("describe".equals(string)) {
            mTvName.setText("修改个人描述");
            mTvNicknameHint.setVisibility(View.GONE);
            mLayoutInput.setVisibility(View.GONE);
            mLayoutInputContent.setVisibility(View.VISIBLE);
            mEditInputContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
            cl_school.setVisibility(View.GONE);
        }else if("school".equals(string)){
            mTvName.setText("修改院校");
            mTvNicknameHint.setVisibility(View.GONE);
            mLayoutInput.setVisibility(View.GONE);
            mLayoutInputContent.setVisibility(View.GONE);
            edit_input_school.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            cl_school.setVisibility(View.VISIBLE);
        }
        registerInput();
        mTvConfirm.setEnabled(false);

    }

    private void registerInput() {
        mEditInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mIvClearInput.setVisibility(View.VISIBLE);
                    mTvConfirm.setEnabled(true);
                    mTvConfirm.setAlpha(1);
                } else {
                    mIvClearInput.setVisibility(View.GONE);
                    mTvConfirm.setEnabled(false);
                    mTvConfirm.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edit_input_school.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() > 0 && s.length() <= 50) {
                    mTvConfirm.setEnabled(true);
                    mTvConfirm.setAlpha(1);
                    tv_text_num2.setText(s.length() + "/15");
                } else {
                    mTvConfirm.setEnabled(false);
                    mTvConfirm.setAlpha(0.5f);
                    mTvTextNum.setText("0/15");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mEditInputContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && s.length() <= 50) {
                    mTvConfirm.setEnabled(true);
                    mTvConfirm.setAlpha(1);
                    mTvTextNum.setText(s.length() + "/50");
                } else {
                    mTvConfirm.setEnabled(false);
                    mTvConfirm.setAlpha(0.5f);
                    mTvTextNum.setText("0/50");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.iv_back, R.id.iv_clear_input, R.id.tv_confirm,R.id.layout_input_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear_input:
                mEditInput.setText("");
                break;
            case R.id.layout_input_content:
                KeyBoardUtils.requestShowKeyBord(mEditInputContent);
                break;
            case R.id.tv_confirm:
                if (check()) {
                    Map<String, String> map = new TreeMap<>();
                    if ("nickName".equals(string)) {
                        map.put("userNickName", nickName);
                    } else if ("describe".equals(string)) {
                        map.put("signature", describe);
                    } else if("school".equals(string)){
                        map.put("school",school);
                    }
                    mvpPresenter.editInfo(map,string);
                }
                break;
            default:
                break;
        }
    }

    private boolean check() {
        if ("nickName".equals(string)) {
            nickName = mEditInput.getText().toString().trim();
            if ("".equals(nickName)) {
                ToastUtils.showShortToast("请输入昵称");
                return false;
            }
        } else if ("describe".equals(string)) {
            describe = mEditInputContent.getText().toString().trim();
            if ("".equals(describe)) {
                ToastUtils.showShortToast("请输入描述内容");
                return false;
            }
        }else if("school".equals(string)){
            school = edit_input_school.getText().toString().trim();
            if("".equals(school)){
                ToastUtils.showShortToast("请填写您得毕业院校");
                return  false;
            }
        }
        return true;
    }

    @Override
    public void showEditSuccess(BaseResponse response,String type) {
        setResult(RESULT_OK);
        if ("nickName".equals(type)) {
            UserInfoManger.saveNickName(nickName);
            EventBus.getDefault().post(new MessageEvent(Utils.SET_USER_NICKNAME,""));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK,nickName);

            TIMFriendshipManager.getInstance().modifySelfProfile(hashMap, new TIMCallBack() {
                @Override
                public void onError(int i, String s) {


                }
                @Override
                public void onSuccess() {

                }
            });

        } else if ("describe".equals(type)) {
            UserInfoManger.saveDescribe(describe);
            EventBus.getDefault().post(new MessageEvent(Utils.SET_USER_DESCRIBE,""));
        } else if("school".equals(type)){
            UserInfoManger.savaSchool(school);
//            EventBus.getDefault().post(new MessageEvent(Utils.SETUSER));
        }
        finish();
        ToastUtils.showShortToast(response.getMessage());
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
