package com.jfkj.im.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.Userhead.UserheadPresenter;
import com.jfkj.im.mvp.Userhead.UserheadView;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppManager;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.LQRPhotoSelectUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import butterknife.BindView;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class UserheadActivity extends BaseActivity<UserheadPresenter> implements TextWatcher, View.OnClickListener, UserheadView {
    @BindView(R.id.add_user_iv)
    ImageView add_user_iv;
    @BindView(R.id.btn_enter)
    Button btn_enter;
    @BindView(R.id.et_write_name)
    EditText et_write_name;
    LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    UserheadPresenter presenter;
    Intent getIntent;
    String head_url ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(mActivity, false);
        getIntent = getIntent();
        et_write_name.addTextChangedListener(this);
        add_user_iv.setOnClickListener(this);
        btn_enter.setOnClickListener(this);
        presenter = new UserheadPresenter(this);
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                if (Utils.netWork()) {

                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), outputFile);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("picture", outputFile.getName().replace("com.jfkj.im", ""), requestBody);
                    presenter.getuploadImage("1", getIntent.getStringExtra(Utils.MOBILENO), body);
                } else {
                    toastShow(R.string.nonetwork);
                }

            }
        }, false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            System.exit(0);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_user_iv:
                PermissionGen.needPermission(UserheadActivity.this, LQRPhotoSelectUtils.REQ_SELECT_PHOTO, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
                break;
            case R.id.btn_enter:
                if (et_write_name.getText().toString().trim().length() == 0) {
                    toastShow("昵称不能为空");
                    return;
                }
                if (head_url.trim().length() == 0) {
                    toastShow("图片地址无效");
                    return;
                }
                if(Utils.netWork()){
                    Map<String, String> headmap = new HashMap<>();
                    Map<String, String> map = new HashMap<>();
                    map.put(Utils.OSNAME, Utils.ANDROID);
                    map.put(Utils.CHANNEL, Utils.CHANNEL);
                    map.put(Utils.APPVERSION, Utils.getVersionCode()+"");
                    map.put(Utils.HEAD, head_url);
                    map.put(Utils.NICKNAME,et_write_name.getText().toString().trim());
                    headmap.put(Utils.TOKEN, getIntent.getStringExtra(Utils.TOKEN));
                    headmap.put(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()));
                    if (Utils.netWork()) {
                        OkHttpUtils.post()
                                .tag(mActivity)
                                .url(ApiStores.base_url+"/user/register")
                                .headers(headmap)
                                .params(map)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {

                                    }
                                    @Override
                                    public void onResponse(String response, int id) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            toastShow(jsonObject.getString("message"));
                                            if (jsonObject.getString("code").equals("200")) {

                                                    toastShow(jsonObject.getString("message"));
                                                    if (jsonObject.getString("code").equals("200")) {

                                                        if (getIntent.getStringExtra(Utils.SEX).equals("2")) {//这是女性用户注册
                                                            Intent intent = new Intent(App.getAppContext(), UploadpicturesActivity.class);
                                                            SPUtils.getInstance(mActivity).put(Utils.TOKEN, getIntent.getStringExtra(Utils.TOKEN));
                                                            intent.putExtra(Utils.TOKEN, getIntent.getStringExtra(Utils.TOKEN));
                                                            startActivity(intent);
                                                        }
                                                        if (getIntent.getStringExtra(Utils.SEX).equals("1")) {//这是男性用户注册
                                                            SPUtils.getInstance(mActivity).put(Utils.TOKEN, getIntent.getStringExtra(Utils.TOKEN));
                                                            SPUtils.getInstance(mActivity).put(Utils.USERID, jsonObject.getJSONObject("data").getString("userId"));
                                                            SPUtils.getInstance(mActivity).put(Utils.SP_KEY_USER_TOKEN,getIntent.getStringExtra(Utils.TOKEN));
                                                            SPUtils.getInstance(mActivity).put(Utils.SP_KEY_USER_ID, jsonObject.getJSONObject("data").getString("userId"));
                                                            SPUtils.getInstance(mActivity).put(Utils.SEQROOM, jsonObject.getJSONObject("data").getString("seqroom"));
                                                            SPUtils.getInstance(mActivity).put(Utils.AVCHATROOMID, jsonObject.getJSONObject("data").getString("AVChatRoomId"));
                                                            Intent intent = new Intent(mActivity, MainActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            intent.putExtra("isSendJewel",jsonObject.getJSONObject("data").getInt("isNewUser"));
                                                            startActivity(intent);
                                                            ApiClient.onDestroy();
                                                        }
                                                        finish();

                                                    }

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    } else {
                        toastShow(R.string.nonetwork);
                    }


                }else{
                    toastShow(R.string.nonetwork);
                }

                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userhead;
    }

    @Override
    public UserheadPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        mLqrPhotoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        showDialog();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    public void showDialog() {
        //创建对话框创建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框显示小图标
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置标题
        builder.setTitle("权限申请");
        //设置正文
        builder.setMessage("在设置-应用-虎嗅-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");

        //添加确定按钮点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里用来跳到手机设置页，方便用户开启权限
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + UserheadActivity.this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        //添加取消按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //使用构建器创建出对话框对象
        AlertDialog dialog = builder.create();
        dialog.show();//显示对话框
    }

    @Override
    public void uploadImageSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                Glide.with(App.getAppContext()).load(jsonObject.getJSONObject("data").getString("imageUrl")).circleCrop().into(add_user_iv);
                head_url = jsonObject.getJSONObject("data").getString("imageUrl");
                UserInfoManger.saveUserHeadUrl(head_url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadImagefail(String s) {

    }

    @Override
    public void registSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();

            JSONObject jsonObject = new JSONObject(string);

            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {

                if (getIntent.getStringExtra(Utils.SEX).equals("2")) {//这是女性用户注册
                    Intent intent = new Intent(App.getAppContext(), UploadpicturesActivity.class);
                    intent.putExtra(Utils.PASSWORD, getIntent.getStringExtra(Utils.PASSWORD));
                    intent.putExtra(Utils.MOBILENO, getIntent.getStringExtra(Utils.MOBILENO));
                    intent.putExtra(Utils.Birthday, getIntent.getStringExtra(Utils.Birthday));
                    intent.putExtra(Utils.HEAD_URL, head_url);
                    intent.putExtra(Utils.SEX, getIntent.getStringExtra(Utils.SEX));
                    intent.putExtra(Utils.NICKNAME, et_write_name.getText().toString().trim());
                    SPUtils.getInstance(mActivity).put(Utils.TOKEN,  getIntent.getStringExtra(Utils.TOKEN));
                    intent.putExtra(Utils.TOKEN, getIntent.getStringExtra(Utils.TOKEN));
                    startActivity(intent);
                    ApiClient.onDestroy();
                }
                if (getIntent.getStringExtra(Utils.SEX).equals("1")) {//这是男性用户注册
                    SPUtils.getInstance(mActivity).put(Utils.TOKEN, getIntent.getStringExtra(Utils.TOKEN));
                    SPUtils.getInstance(mActivity).put(Utils.USERID, jsonObject.getJSONObject("data").getString("userId"));
                    SPUtils.getInstance(mActivity).put(Utils.SP_KEY_USER_TOKEN, getIntent.getStringExtra(Utils.TOKEN));
                    SPUtils.getInstance(mActivity).put(Utils.SP_KEY_USER_ID, jsonObject.getJSONObject("data").getString("userId"));
                    SPUtils.getInstance(mActivity).put(Utils.SEQROOM, jsonObject.getJSONObject("data").getString("seqroom"));
                    Intent intent = new Intent(mActivity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("isSendJewel",jsonObject.getJSONObject("data").getInt("isSendJewel"));

                    startActivity(intent);
                    ApiClient.onDestroy();
                }
                finish();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void registfail(String s) {

    }



    //注册成功之后 如果是男生直接进入主界面 如果是女生 上传图片
    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }
}
