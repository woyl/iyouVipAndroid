package com.jfkj.im.ui.mine;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.citypick.CityBean;
import com.jfkj.im.citypick.DistrictBean;
import com.jfkj.im.citypick.JDCityConfig;
import com.jfkj.im.citypick.JDCityPicker;
import com.jfkj.im.citypick.OnCityItemClickListener;
import com.jfkj.im.citypick.ProvinceBean;
import com.jfkj.im.data.DataProvide;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.activity.MainActivity;
import com.jfkj.im.ui.dialog.CityDialog;
import com.jfkj.im.ui.dialog.PickDialog;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.widget.StatusBarHeightView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class InsertUserinfoActivity extends BaseActivity {
    //

    private static final int RESULTSCHOOL = 0X11;
    private static final int RESULDESC = 0X12;


    @BindView(R.id.tv_user_desc)
    TextView tvUserDesc;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_hometown)
    TextView tvHometown;
    @BindView(R.id.tv_education)
    TextView tvEducation;
    @BindView(R.id.tv_school)
    TextView tvSchool;
    @BindView(R.id.tv_industry)
    TextView tvIndustry;
    @BindView(R.id.tv_post)
    TextView tvPost;
    @BindView(R.id.tv_smoing)
    TextView tvSmoing;
    @BindView(R.id.tv_wine)
    TextView tvWine;
    @BindView(R.id.tv_like_cuisine)
    TextView tvLikeCuisine;
    private PickDialog weightDialog;

    public JDCityConfig.ShowType mWheelType = JDCityConfig.ShowType.PRO_CITY;
    private JDCityConfig jdCityConfig = new JDCityConfig.Builder().build();
    private JDCityPicker cityPicker;
    private CityDialog mCityDialog = null;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULDESC && data!=null){
            tvUserDesc.setText(data.getStringExtra("dataKey"));
            tvUserDesc.setTextColor(getResources().getColor(R.color.color_999999));
        }else if(requestCode == RESULTSCHOOL && data!=null){
            tvSchool.setText(data.getStringExtra("dataKey"));
            tvSchool.setTextColor(getResources().getColor(R.color.color_999999));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.tv_submit,R.id.tv_cancel,R.id.ll_desc,R.id.ll_height,R.id.ll_weight,R.id.ll_home,R.id.ll_education,R.id.ll_school,R.id.ll_industry,R.id.ll_post,R.id.layout_smoking,R.id.layout_wine,R.id.layout_cuisine})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_submit:

                examinePerfect();
                //提交
                break;
            case R.id.tv_cancel:
                //跳过

                Intent intentmain = new Intent(App.getAppContext(), MainActivity.class);
                intentmain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentmain);
                ApiClient.onDestroy();

                break;
            case R.id.ll_desc:
                //个人描述

                Intent intent  = new Intent(this,AddUserInfoActivity.class);
                intent.putExtra("type","desc");
                startActivityForResult(intent,RESULDESC);

                break;
            case  R.id.ll_height:
                weightDialog = new PickDialog(this, R.style.dialogstyle, "选择身高", DataProvide.getHeightList(), new PickDialog.onConfirmDialogListener() {

                    @Override
                    public void onStrPick(String pickStr) {
                        ToastUtils.showShortToast("选择了" + pickStr);
                        tvHint.setText(pickStr);
                        tvHint.setTextColor(getResources().getColor(R.color.color_999999));
                    }
                });
                weightDialog.show();
                break;

            case  R.id.ll_weight:
                weightDialog = new PickDialog(this, R.style.dialogstyle, "选择体重", DataProvide.getWeightList(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        //todo 提交参数
                        ToastUtils.showShortToast("选择了" + pickStr);
                        tvWeight.setText(pickStr);
                        tvWeight.setTextColor(getResources().getColor(R.color.color_999999));
                    }
                });
                weightDialog.show();

                break;

            case  R.id.ll_home:
                mWheelType = JDCityConfig.ShowType.PRO_CITY;
                jdCityConfig.setShowType(mWheelType);
                cityPicker.showCityPicker();

                cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {

                    private String hometown;
                    private String cityData;
                    private String proData;

                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        if (province != null) {
                            proData = "name:  " + province.getName() + "   id:  " + province.getId();
                            proData = province.getName();
                        }
                        if (city != null) {
                            cityData = "name:  " + city.getName() + "   id:  " + city.getId();
                            cityData = city.getName();
                        }
                        String districtData = null;
                        if (district != null) {
                            districtData = "name:  " + district.getName() + "   id:  " + district.getId();
                        }
                        if (mWheelType == JDCityConfig.ShowType.PRO_CITY_DIS) {
                            ToastUtils.showShortToast(proData + cityData);
                        } else {
                            ToastUtils.showShortToast(proData + cityData);
                        }
                        hometown = proData + "·" + cityData;

                        ToastUtils.showShortToast(hometown);

                        tvHometown.setText(hometown);
                        tvHometown.setTextColor(getResources().getColor(R.color.color_999999));
                        //                        UserInfoManger.saveHometown(proData + cityData);
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                break;

            case  R.id.ll_education:

                weightDialog = new PickDialog(this, R.style.dialogstyle, "选择学历", DataProvide.getEducationList(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        ToastUtils.showShortToast("选择了" + pickStr);
                        tvEducation.setText(pickStr);
                        tvEducation.setTextColor(getResources().getColor(R.color.color_999999));
                    }
                });
                weightDialog.show();

                break;

            case  R.id.ll_school:

                Intent intentSchool  = new Intent(this,AddUserInfoActivity.class);
                intentSchool.putExtra("type","school");
                startActivityForResult(intentSchool,RESULTSCHOOL);

                break;

            case  R.id.ll_industry:
                weightDialog = new PickDialog(this, R.style.dialogstyle, "选择行业", DataProvide.getIndustry(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        ToastUtils.showShortToast("选择了" + pickStr);
                        tvIndustry.setText(pickStr);
                        tvIndustry.setTextColor(getResources().getColor(R.color.color_999999));

                    }
                });
                weightDialog.show();

                break;
            case  R.id.ll_post:
                weightDialog = new PickDialog(this, R.style.dialogstyle, "选择职位", DataProvide.getOccupation(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        ToastUtils.showShortToast("选择了" + pickStr);
                        tvPost.setText(pickStr);
                        tvPost.setTextColor(getResources().getColor(R.color.color_999999));

                    }
                });
                weightDialog.show();
                break;
            case  R.id.layout_smoking:

                weightDialog = new PickDialog(this, R.style.dialogstyle, "抽烟习惯", DataProvide.getSmokingList(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                    tvSmoing.setText(pickStr);
                    tvSmoing.setTextColor(getResources().getColor(R.color.color_999999));

                    }
                });
                weightDialog.show();

                break;
            case  R.id.layout_wine:
                weightDialog = new PickDialog(this, R.style.dialogstyle, "喝酒习惯", DataProvide.getWinkList(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        tvWine.setText(pickStr);
                        tvWine.setTextColor(getResources().getColor(R.color.color_999999));

                    }
                });
                weightDialog.show();
                break;
            case  R.id.layout_cuisine:

                weightDialog = new PickDialog(this, R.style.dialogstyle, "喜欢菜系", DataProvide.getLikeCuisine(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        ToastUtils.showShortToast("选择了" + pickStr);
                        tvLikeCuisine.setText(pickStr);
                        tvLikeCuisine.setTextColor(getResources().getColor(R.color.color_999999));

                    }
                });
                weightDialog.show();
                break;

        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this, true);

        mCityDialog = new CityDialog(this);
        cityPicker = new JDCityPicker();
        cityPicker.init(this);
        cityPicker.setConfig(jdCityConfig);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_insert_userinfo;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }



    public void examinePerfect(){
        Map<String,String> map  = new HashMap<>();

        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());


        map.put("height","未选择".equals(tvHint.getText().toString().trim())?"":tvHint.getText().toString().trim());
        map.put("weight","未选择".equals(tvWeight.getText().toString().trim())?"":tvWeight.getText().toString().trim());
        map.put("educational","未选择".equals(tvEducation.getText().toString().trim())?"":tvEducation.getText().toString().trim());
        map.put("hometown","未选择".equals(tvHometown.getText().toString().trim())?"":tvHometown.getText().toString().trim());
        map.put("signature","未填写".equals(tvUserDesc.getText().toString().trim())?"":tvUserDesc.getText().toString().trim());
        map.put("school","未填写".equals(tvSchool.getText().toString().trim())?"":tvSchool.getText().toString().trim());
        map.put("industry","未选择".equals(tvIndustry.getText().toString().trim())?"":tvIndustry.getText().toString().trim());
        map.put("occupation","未选择".equals(tvPost.getText().toString().trim())?"":tvPost.getText().toString().trim());
        map.put("smoking","未选择".equals(tvSmoing.getText().toString().trim())?"":tvSmoing.getText().toString().trim());
        map.put("drinkwine","未选择".equals(tvWine.getText().toString().trim())?"":tvWine.getText().toString().trim());
        map.put("likeCuisine","未选择".equals(tvLikeCuisine.getText().toString().trim())?"":tvLikeCuisine.getText().toString().trim());


        Map<String, String> headmap = new HashMap<>();

        headmap.put(Utils.TOKEN, SPUtils.getInstance(mActivity).getString(Utils.TOKEN));
        headmap.put(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY+ AppUtils.getReqTime()));
        OkHttpUtils.post()
                .tag(mActivity)
                .url(ApiStores.base_url+"/user/examinePerfect")
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
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("code").equals("200")){
                                //设置信息成功
                                Intent intent = new Intent(App.getAppContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                ApiClient.onDestroy();
                            }else{
                                toastShow(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}
