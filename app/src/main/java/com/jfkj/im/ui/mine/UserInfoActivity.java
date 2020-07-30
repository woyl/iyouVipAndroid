package com.jfkj.im.ui.mine;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.jfkj.im.Bean.UserDetailBean;
import com.jfkj.im.R;
import com.jfkj.im.citypick.CityBean;
import com.jfkj.im.citypick.DistrictBean;
import com.jfkj.im.citypick.JDCityConfig;
import com.jfkj.im.citypick.JDCityPicker;
import com.jfkj.im.citypick.OnCityItemClickListener;
import com.jfkj.im.citypick.ProvinceBean;
import com.jfkj.im.data.DataProvide;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.MessageEvent;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.mine.EditUserInfoPresenter;
import com.jfkj.im.mvp.mine.EditUserInfoView;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.CityDialog;
import com.jfkj.im.ui.dialog.PickDialog;
import com.jfkj.im.utils.DataFormatUtils;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * <pre>
 * Description:  编辑个人信息
 * @author :   ys
 * @date :         2019/11/21
 * </pre>
 */
public class UserInfoActivity extends BaseActivity<EditUserInfoPresenter> implements EditUserInfoView {
    private final static int INFO_CHANGE_NICKNAME = 20001;//昵称
    private final static int INFO_CHANGE_DESCRIBE = 20002;//描述
    private final static int INFO_CHANGE_BIRTHDAY = 20003;//生日
    private final static int INFO_CHANGE_HEIGHT = 20004;//身高
    private final static int INFO_CHANGE_WEIGHT = 20005;//体重
    private final static int INFO_CHANGE_EDUCATION = 20006;//学历
    private final static int INFO_CHANGE_HOME = 20007;//家乡
    private final static int INFO_CHANGE_HEADER = 20008;//头像
    private final static int INFO_CHANGE_STAR = 20009;//星座
    private final static int INFO_SCHOOL_STAR = 20010;//院校
    @BindView(R.id.iv_title_back)
    AppCompatImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    AppCompatTextView mTvTitle;
    @BindView(R.id.tv_title_right)
    AppCompatTextView mTvTitleRight;
    @BindView(R.id.iv_title_right)
    AppCompatImageView mIvTitleRight;
    @BindView(R.id.iv_user_head)
    ImageView mIvUserHead;
    @BindView(R.id.layout_image)
    LinearLayout mLayoutImage;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.layout_nickname)
    LinearLayout mLayoutNickname;
    @BindView(R.id.tv_describe)
    TextView mTvDescribe;
    @BindView(R.id.layout_describe)
    LinearLayout mLayoutDescribe;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.layout_sex)
    LinearLayout mLayoutSex;
    @BindView(R.id.tv_birthday)
    TextView mTvBirthday;
    @BindView(R.id.layout_birthday)
    LinearLayout mLayoutBirthday;
    @BindView(R.id.tv_star)
    TextView mTvStar;
    @BindView(R.id.layout_star)
    LinearLayout mLayoutStar;
    @BindView(R.id.tv_height)
    TextView mTvHeight;
    @BindView(R.id.layout_height)
    LinearLayout mLayoutHeight;
    @BindView(R.id.tv_weight)
    TextView mTvWeight;
    @BindView(R.id.layout_weight)
    LinearLayout mLayoutWeight;
    @BindView(R.id.tv_home)
    TextView mTvHome;
    @BindView(R.id.layout_home)
    LinearLayout mLayoutHome;
    @BindView(R.id.tv_education)
    TextView mTvEducation;
    @BindView(R.id.layout_education)
    LinearLayout mLayoutEducation;
    @BindView(R.id.tv_school)   //院校
    TextView tv_school;

    @BindView(R.id.tv_industry)
    TextView tv_industry;
    //职业
    @BindView(R.id.tv_post)
    TextView tv_post;

    @BindView(R.id.tv_smoing)
    TextView tv_smoing;

    @BindView(R.id.tv_wine)
    TextView tv_wine;

    @BindView(R.id.tv_like_cuisine)
    TextView tv_like_cuisine;

    private PickDialog weightDialog = null;
    private TimePickerView mTimePickerView;
    private OptionsPickerView mOptionsPickerView;
    private String occupation;
    private String industry;
    private  String somking;
    private String drinkwine;
    private String likeCuisine;
    private String height;
    private String weight;
    private String education;
    private String birthday;
    private CityDialog mCityDialog = null;
    public JDCityConfig.ShowType mWheelType = JDCityConfig.ShowType.PRO_CITY;
    private JDCityConfig jdCityConfig = new JDCityConfig.Builder().build();
    private JDCityPicker cityPicker;
    String proData = null;
    String cityData = null;
    private String hometown;
    private String xingzuo = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info_setting;
    }

    @Override
    public EditUserInfoPresenter createPresenter() {
        return new EditUserInfoPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        updatainfo();
    }


    //拆开礼物动画
    public void UnwrappingGiftAnimation(){
        SVGAImageView imageView = new SVGAImageView(this);


    }

    public void updatainfo() {
        IntentFilter intentFilter = new IntentFilter("getuserdetail");
        registerReceiver(broadcastReceiver, intentFilter);
        mTvTitle.setText(UserInfoManger.getNickName());
        Glide.with(this).load(UserInfoManger.getMineUserHeadUrl()).transform(new CircleCrop()).into(mIvUserHead);
        mTvBirthday.setText(UserInfoManger.getUserBirthday());
        if (UserInfoManger.getNickName() != null && !"".equals(UserInfoManger.getNickName())) {
            mTvNickname.setText(UserInfoManger.getNickName());
            mTvNickname.setTextColor(getResources().getColor(R.color.color_999999));
        } else {
            mTvNickname.setText("未填写");
            mTvNickname.setTextColor(getResources().getColor(R.color.color_ff4d6c));
        }
        if (UserInfoManger.getUserWeight() != null && !"0".equals(UserInfoManger.getUserWeight())) {
            mTvWeight.setText(UserInfoManger.getUserWeight() + "kg");
            mTvWeight.setTextColor(getResources().getColor(R.color.color_999999));
        } else {
            mTvWeight.setText("未选择");
            mTvWeight.setTextColor(getResources().getColor(R.color.color_ff4d6c));
        }

        if (UserInfoManger.getUserHeight() != null && !"0".equals(UserInfoManger.getUserHeight())) {
            mTvHeight.setText(UserInfoManger.getUserHeight() + "cm");
            mTvHeight.setTextColor(getResources().getColor(R.color.color_999999));
        } else {
            mTvHeight.setText("未选择");
            mTvHeight.setTextColor(getResources().getColor(R.color.color_ff4d6c));
        }

        if (UserInfoManger.getUserEducation() != null && !"".equals(UserInfoManger.getUserEducation())) {
            mTvEducation.setText(UserInfoManger.getUserEducation());
            mTvEducation.setTextColor(getResources().getColor(R.color.color_999999));
        } else {
            mTvEducation.setText("未选择");
            mTvEducation.setTextColor(getResources().getColor(R.color.color_ff4d6c));
        }


        //1
        if (UserInfoManger.getDescribe() != null && !"".equals(UserInfoManger.getDescribe())) {
            mTvDescribe.setText(UserInfoManger.getDescribe());
            mTvDescribe.setTextColor(getResources().getColor(R.color.color_999999));
        } else {
            mTvDescribe.setText("未填写");
            mTvDescribe.setTextColor(getResources().getColor(R.color.color_ff4d6c));
        }

        //家乡
        if (UserInfoManger.getHometown() != null && !"".equals(UserInfoManger.getHometown())) {
            mTvHome.setText(UserInfoManger.getHometown());
            mTvHome.setTextColor(getResources().getColor(R.color.color_999999));
        } else {
            mTvHome.setText("未选择");
            mTvHome.setTextColor(getResources().getColor(R.color.color_ff4d6c));
        }

        //院校
        if(UserInfoManger.getSchool()!=null && !"".equals(UserInfoManger.getSchool())){
            tv_school.setText(UserInfoManger.getSchool());
            tv_school.setTextColor(getResources().getColor(R.color.color_999999));
        }else{
            tv_school.setText("未填写");
            tv_school.setTextColor(getResources().getColor(R.color.color_ff4d6c));
        }

        //行业
        if(UserInfoManger.getindustry()!=null &&  !"".equals(UserInfoManger.getindustry())){
            tv_industry.setText(UserInfoManger.getindustry());
            tv_industry.setTextColor(getResources().getColor(R.color.color_999999));
        }else{
            tv_industry.setText("未选择");
            tv_industry.setTextColor(getResources().getColor(R.color.color_ff4d6c));
        }


        //职业
        if(UserInfoManger.getOccupation()!=null && !"".equals(UserInfoManger.getOccupation())){
            tv_post.setText(UserInfoManger.getOccupation());
            tv_post.setTextColor(getResources().getColor(R.color.color_999999));
        }else{
            tv_post.setText("未选择");
            tv_post.setTextColor(getResources().getColor(R.color.color_ff4d6c));
        }


        if(UserInfoManger.getSmoking()!=null && !"".equals(UserInfoManger.getSmoking())){
            tv_smoing.setText(UserInfoManger.getSmoking());
            tv_smoing.setTextColor(getResources().getColor(R.color.color_999999));
        }else{
            tv_smoing.setText("未选择");
            tv_smoing.setTextColor(getResources().getColor(R.color.color_ff4d6c));
        }


        if(UserInfoManger.getDrinkwine()!=null && !"".equals(UserInfoManger.getDrinkwine())){
            tv_wine.setText(UserInfoManger.getDrinkwine());
            tv_wine.setTextColor(getResources().getColor(R.color.color_999999));
        }else{
            tv_wine.setText("未选择");
            tv_wine.setTextColor(getResources().getColor(R.color.color_ff4d6c));
        }



        if(UserInfoManger.getLikeCuisine()!=null && !"".equals(UserInfoManger.getLikeCuisine())){
            tv_like_cuisine.setText(UserInfoManger.getLikeCuisine());
            tv_like_cuisine.setTextColor(getResources().getColor(R.color.color_999999));
        }else{
            tv_like_cuisine.setText("未选择");
            tv_like_cuisine.setTextColor(getResources().getColor(R.color.color_ff4d6c));
        }




        mTvStar.setText(UserInfoManger.getConstellation());
//        Map<String,String> map=new HashMap<>();
//        map.put(Utils.OSNAME, Utils.ANDROID);
//        map.put(Utils.CHANNEL, Utils.ANDROID);
//        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
//        map.put(Utils.USERID,Utils.APPID);
//        OkHttpUtils.post()
//                .tag(mActivity)
//                .url(ApiStores.base_url+"/home/getUsertail")
//                .addHeader(Utils.TOKEN, SPUtils.getInstance(mActivity).getString(Utils.TOKEN))
//                .params(map)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        try {
//                            JSONObject jsonObject=new JSONObject(response);
//                            if(jsonObject.getString("code").equals("200")){
//                                UserDetailBean detailBean= JSON.parseObject(response,UserDetailBean.class);
//                                mTvStar.setText(detailBean.getData().getConstellation());
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });

        if ("1".equals(UserInfoManger.getGender())) {
            mTvSex.setText("男");
        } else {
            mTvSex.setText("女");
        }

        mCityDialog = new CityDialog(this);
        cityPicker = new JDCityPicker();
        cityPicker.init(this);
        cityPicker.setConfig(jdCityConfig);

    }

    @OnClick({R.id.layout_post,R.id.layout_industry,R.id.layout_wine,R.id.layout_cuisine,R.id.layout_smoking,R.id.layout_school,R.id.iv_title_back, R.id.layout_image, R.id.layout_education, R.id.layout_nickname, R.id.layout_describe, R.id.layout_sex, R.id.layout_birthday, R.id.layout_star, R.id.layout_height, R.id.layout_weight, R.id.layout_home})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {

            case R.id.layout_school:
                bundle.putString("TAG", "school");
                JumpUtil.startForResult(this, CommChangeActivity.class, INFO_SCHOOL_STAR, bundle);
                break;
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.layout_image:
                //todo change image

                JumpUtil.startForResult(this, PersonalPhotoActivity.class, INFO_CHANGE_HEADER, bundle);
                break;
            case R.id.layout_nickname:
                bundle.putString("TAG", "nickName");
                JumpUtil.startForResult(this, CommChangeActivity.class, INFO_CHANGE_NICKNAME, bundle);
                break;
            case R.id.layout_describe:
                bundle.putString("TAG", "describe");
                JumpUtil.startForResult(this, CommChangeActivity.class, INFO_CHANGE_DESCRIBE, bundle);
                break;
            case R.id.layout_sex:
                break;
            case R.id.layout_birthday:
                // 日期选择
                showTimePick();
                mTimePickerView.show();
                break;
//            case R.id.layout_star:
//                //星座
//                JumpUtil.startForResult(this,SetHomeActivity.class,INFO_CHANGE_STAR,bundle);
//                break;
            case R.id.layout_post:
                weightDialog = new PickDialog(this, R.style.dialogstyle, "选择职位", DataProvide.getOccupation(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        ToastUtils.showShortToast("选择了" + pickStr);
                        Map<String, String> map = new HashMap<>();
                        map.put("occupation", pickStr);
                        occupation = pickStr;
                        mvpPresenter.editInfo(map, "USER_OCCUPATION");
                    }
                });
                weightDialog.show();
                break;
            case R.id.layout_industry:
                weightDialog = new PickDialog(this, R.style.dialogstyle, "选择行业", DataProvide.getIndustry(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        ToastUtils.showShortToast("选择了" + pickStr);
                        Map<String, String> map = new HashMap<>();
                        map.put("industry", pickStr);
                        industry = pickStr;
                        mvpPresenter.editInfo(map, "USER_INDUSTRY");
                    }
                });
                weightDialog.show();
                break;

            case R.id.layout_smoking:
                weightDialog = new PickDialog(this, R.style.dialogstyle, "抽烟习惯", DataProvide.getSmokingList(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        ToastUtils.showShortToast("选择了" + pickStr);
                        Map<String, String> map = new HashMap<>();
                        map.put("smoking", pickStr);
                        somking = pickStr;
                        mvpPresenter.editInfo(map, "USER_SOMKING");
                    }
                });
                weightDialog.show();
                break;

            case R.id.layout_wine:
                weightDialog = new PickDialog(this, R.style.dialogstyle, "喝酒习惯", DataProvide.getWinkList(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        ToastUtils.showShortToast("选择了" + pickStr);
                        Map<String, String> map = new HashMap<>();
                        map.put("drinkwine", pickStr);
                        drinkwine = pickStr;
                        mvpPresenter.editInfo(map, "USER_DRINKWINE");
                    }
                });
                weightDialog.show();

                break;

            case R.id.layout_cuisine:
                //喜欢菜系

                weightDialog = new PickDialog(this, R.style.dialogstyle, "喜欢菜系", DataProvide.getLikeCuisine(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        ToastUtils.showShortToast("选择了" + pickStr);
                        Map<String, String> map = new HashMap<>();
                        map.put("likeCuisine", pickStr);
                        likeCuisine = pickStr;
                        mvpPresenter.editInfo(map, "USER_LIKECUISINE");
                    }
                });
                weightDialog.show();
                break;

            case R.id.layout_height:
                weightDialog = new PickDialog(this, R.style.dialogstyle, "选择身高", DataProvide.getHeightList(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        ToastUtils.showShortToast("选择了" + pickStr);
                        Map<String, String> map = new HashMap<>();
                        map.put("Height", pickStr);
                        height = pickStr;
                        mvpPresenter.editInfo(map, "USER_HEIGHT");

                    }
                });
                weightDialog.show();
                break;
            case R.id.layout_weight:
                weightDialog = new PickDialog(this, R.style.dialogstyle, "选择体重", DataProvide.getWeightList(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        //todo 提交参数
                        ToastUtils.showShortToast("选择了" + pickStr);
                        weight = pickStr;
                        Map<String, String> map = new HashMap<>();
                        map.put("weight", pickStr);
                        mvpPresenter.editInfo(map, "USER_WEIGHT");
                    }
                });
                weightDialog.show();
                break;
            case R.id.layout_home:
                mWheelType = JDCityConfig.ShowType.PRO_CITY;
                jdCityConfig.setShowType(mWheelType);
                cityPicker.showCityPicker();

                cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
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
                        Map<String, String> map = new HashMap<>();
                        map.put("hometown", hometown);
                        mvpPresenter.editInfo(map, "HOMETOWN");
//                        UserInfoManger.saveHometown(proData + cityData);
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                break;
            case R.id.layout_education:
                weightDialog = new PickDialog(this, R.style.dialogstyle, "选择学历", DataProvide.getEducationList(), new PickDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {
                        ToastUtils.showShortToast("选择了" + pickStr);
                        education = pickStr;
                        Map<String, String> map = new HashMap<>();
                        map.put("educational", pickStr);
                        mvpPresenter.editInfo(map, "USER_EDUCATION");
                    }
                });
                weightDialog.show();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INFO_CHANGE_NICKNAME) {
                mTvNickname.setText(UserInfoManger.getNickName());
            } else if (requestCode == INFO_CHANGE_DESCRIBE) {
                mTvDescribe.setText(UserInfoManger.getDescribe());
            } else if (requestCode == INFO_CHANGE_HEADER) {
                Glide.with(this).load(UserInfoManger.getMineUserHeadUrl()).transform(new CircleCrop()).into(mIvUserHead);
            } else if (requestCode == INFO_CHANGE_HOME) {
                mTvHome.setText(UserInfoManger.getHometown());
            } else if (requestCode == INFO_SCHOOL_STAR){
                tv_school.setText(UserInfoManger.getSchool());
                tv_school.setTextColor(getResources().getColor(R.color.color_999999));
            }
        }
    }

    private void showTimePick() {
        mTimePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {

                if(DataFormatUtils.checkAdult(date)){
                    birthday = Utils.getTime(date);
                    Map<String, String> map = new HashMap<>();
                    map.put("birthday", Utils.getTime(date));
                    mvpPresenter.editInfo(map, "USER_BIRTHDAY");
                    ToastUtils.showShortToast(Utils.getTime(date));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    xingzuo = Utils.date2Constellation(calendar);

                }else{
                    ToastUtils.showShortToast("年龄必须大于18岁");
                }



            }
        })
                .setLayoutRes(R.layout.layout_custom_pickerview_time, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        v.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mTimePickerView.dismiss();
                            }
                        });
                        v.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mTimePickerView.returnData();
                                mTimePickerView.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true)
                .build();

        Dialog mDialog = mTimePickerView.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER);

            params.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
            params.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
            mTimePickerView.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.CENTER);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }


    @Override
    public void showEditSuccess(BaseResponse baseResponse, String type) {

        Intent intent = new Intent("getuserdetail");
        sendBroadcast(intent);
        if ("USER_HEIGHT".equals(type)) {
            UserInfoManger.saveHeight(height);
            mTvHeight.setText(height + "cm");
            EventBus.getDefault().post(new MessageEvent(Utils.SET_USER_HEIGHT, ""));
        } else if ("USER_WEIGHT".equals(type)) {
            UserInfoManger.saveWeight(weight);
            mTvWeight.setText(weight + "kg");
            EventBus.getDefault().post(new MessageEvent(Utils.SET_USER_WEIGHT, ""));
        } else if ("USER_EDUCATION".equals(type)) {
            UserInfoManger.saveEducation(education);
            mTvEducation.setText(education);
            EventBus.getDefault().post(new MessageEvent(Utils.SET_USER_EDUCATION, ""));
        } else if ("USER_BIRTHDAY".equals(type)) {
            UserInfoManger.saveBirthday(birthday);
            UserInfoManger.saveConstellation(xingzuo);
            mTvBirthday.setText(UserInfoManger.getUserBirthday());
        } else if ("HOMETOWN".equals(type)) {
            UserInfoManger.saveHometown(hometown);
            mTvHome.setText(hometown);
            EventBus.getDefault().post(new MessageEvent(Utils.SET_USER_HOMETOWN, ""));
        }else if ("USER_LIKECUISINE".equals(type)){
            UserInfoManger.savaLikeCuisine(likeCuisine);
            tv_like_cuisine.setText(likeCuisine);
        }else if("USER_DRINKWINE".equals(type)){
            UserInfoManger.savaDrinkwine(drinkwine);
            tv_wine.setText(drinkwine);
        }else if ("USER_SOMKING".equals(type)){
            UserInfoManger.savaSmokingy(somking);
            tv_smoing.setText(somking);
        }else if("USER_INDUSTRY".equals(type)){
            UserInfoManger.savaindustry(industry);
            tv_industry.setText(industry);
        }else if("USER_OCCUPATION".equals(type)){
            UserInfoManger.savaOccupation(occupation);
            tv_post.setText(occupation);
        }

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "getuserdetail":
                    updatainfo();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
