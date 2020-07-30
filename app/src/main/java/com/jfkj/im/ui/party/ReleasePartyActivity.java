package com.jfkj.im.ui.party;

import butterknife.BindView;
import butterknife.OnClick;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.party.ReleasePartyPresenter;
import com.jfkj.im.mvp.party.ReleasePartyView;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.ui.dialog.SelecTimeDialog;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ReleasePartyActivity extends BaseActivity<ReleasePartyPresenter> implements ReleasePartyView {

    @BindView(R.id.iv_title_back)
    ImageView img_title_back;

    @BindView(R.id.tv_num)
    TextView tv_num;

    @BindView(R.id.ed_party_title)
    EditText edPartyTitle;

    @BindView(R.id.tv_party_time)
    TextView tv_party_time;

    @BindView(R.id.tv_place)
    TextView tvPlace;

    @BindView(R.id.sw_welfare)
    Switch aSwitch;

    @BindView(R.id.ed_introduce)
    EditText edIntroduce;

    @BindView(R.id.tv_money)
    TextView tvMoney;

    @BindView(R.id.tv_submit)
    TextView tv_submit;



    private String money = "" ;


    ReleasePartyPresenter releasePartyPresenter;


    int num = 2;
    private BottomSheetDialog dialog;
    private TimePickerView mTimePickerView;
    private SimpleDateFormat format;
    private CommonDialog topUpDialog;
    private TextView tvTotalMoney;
    private SelecTimeDialog timeDialog;
    private SimpleDateFormat format1;
    private String partyTime;
    private String strdate;
   // private SimpleDateFormat format3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        releasePartyPresenter = new ReleasePartyPresenter(this);
        initDate();
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       // format3 = new SimpleDateFormat("yyyy年 MM月 dd日 HH:mm");
    }

    private void initDate() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_party;
    }

    @Override
    public ReleasePartyPresenter createPresenter() {
        return releasePartyPresenter;
    }

    private void showButtomDialog(){
        dialog = new BottomSheetDialog(this,R.style.BottomSheetDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_enter_for,null);
        dialog.setContentView(view);
        dialog.show();

        tvTotalMoney = view.findViewById(R.id.tv_total_money);


        LinearLayout ll1 =  view.findViewById(R.id.ll1);
        LinearLayout ll2 =  view.findViewById(R.id.ll2);
        LinearLayout ll3 =   view.findViewById(R.id.ll3);
        LinearLayout ll4 =    view.findViewById(R.id.ll4);


        TextView tv1 =  view.findViewById(R.id.tv1);
        TextView tv2 =  view.findViewById(R.id.tv2);
        TextView tv3 =  view.findViewById(R.id.tv3);
        TextView tv4 = view.findViewById(R.id.tv4);

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setTextColor(android.graphics.Color.parseColor("#ff060606"));
                tv2.setTextColor(android.graphics.Color.parseColor("#ff060606"));
                tv3.setTextColor(android.graphics.Color.parseColor("#ff060606"));
                tv4.setTextColor(android.graphics.Color.parseColor("#ff060606"));

                ll1.setBackgroundResource(R.drawable.shap_party_select_bg);
                ll2.setBackgroundResource(R.drawable.shape_tv_money_num);
                ll3.setBackgroundResource(R.drawable.shape_tv_money_num);
                ll4.setBackgroundResource(R.drawable.shape_tv_money_num);
                tvTotalMoney.setText(num * 50000 + "");
                money = "50000";
            }
        });
        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setTextColor(android.graphics.Color.parseColor("#ff060606"));
                tv2.setTextColor(android.graphics.Color.parseColor("#ff060606"));
                tv3.setTextColor(android.graphics.Color.parseColor("#ff060606"));
                tv4.setTextColor(android.graphics.Color.parseColor("#ff060606"));

                ll1.setBackgroundResource(R.drawable.shape_tv_money_num);
                ll2.setBackgroundResource(R.drawable.shap_party_select_bg);
                ll3.setBackgroundResource(R.drawable.shape_tv_money_num);
                ll4.setBackgroundResource(R.drawable.shape_tv_money_num);
                money = "100000";
                tvTotalMoney.setText(num * 100000  + "");
            }
        });
        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setTextColor(android.graphics.Color.parseColor("#ff060606"));
                tv2.setTextColor(android.graphics.Color.parseColor("#ff060606"));
                tv3.setTextColor(android.graphics.Color.parseColor("#ff060606"));
                tv4.setTextColor(android.graphics.Color.parseColor("#ff060606"));

                ll1.setBackgroundResource(R.drawable.shape_tv_money_num);
                ll2.setBackgroundResource(R.drawable.shape_tv_money_num);
                ll3.setBackgroundResource(R.drawable.shap_party_select_bg);
                ll4.setBackgroundResource(R.drawable.shape_tv_money_num);
                money = "200000";
                tvTotalMoney.setText(num * 200000 + "");
            }
        });
        ll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setTextColor(android.graphics.Color.parseColor("#ff060606"));
                tv2.setTextColor(android.graphics.Color.parseColor("#ff060606"));
                tv3.setTextColor(android.graphics.Color.parseColor("#ff060606"));
                tv4.setTextColor(android.graphics.Color.parseColor("#ff060606"));

                ll1.setBackgroundResource(R.drawable.shape_tv_money_num);
                ll2.setBackgroundResource(R.drawable.shape_tv_money_num);
                ll3.setBackgroundResource(R.drawable.shape_tv_money_num);
                ll4.setBackgroundResource(R.drawable.shap_party_select_bg);
                money = "500000";
                tvTotalMoney.setText( num * 500000 + "" );
            }
        });


        view.findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!"".equals(money)){
                    int moneya  = Integer.parseInt(money)  ;

                    tvMoney.setTextColor(ContextCompat.getColor(mActivity,R.color.white));
                    tvMoney.setText("x"+moneya + "/人");

                    dialog.dismiss();
                }
            }
        });
    }




    private void showTimePick() {
            mTimePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    String datetime = format.format(date);
//                    ToastUtils.showShortToast(datetime);
                    tv_party_time.setText(datetime);
                }
            }) .setLayoutRes(R.layout.picker_view_party, new CustomListener() {
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
                .setType(new boolean[]{false, true, true, true, true, false})
                .isDialog(true)
                .build();

        Dialog mDialog = mTimePickerView.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER);

            params.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
            params.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
            mTimePickerView.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }


    @OnClick({R.id.tl_select_time,R.id.tv_submit,R.id.rl_money_layout,R.id.rl_select_address,R.id.iv_title_back,R.id.img_remove,R.id.img_add})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tl_select_time:
                format1 = new SimpleDateFormat("yyyy MM月 dd日 HH:mm");

                //showTimePick();
                //  mTimePickerView.show();

                timeDialog = new SelecTimeDialog(this, R.style.dialogstyle, new SelecTimeDialog.CustomizeDialogListener() {
                    @Override
                    public void onClick(View view,String strDate) {
                        switch (view.getId()){
                            case R.id.cancel:
                                //取消
                                timeDialog.dismiss();
                                break;
                            case R.id.tv_submit:
                                timeDialog.dismiss();

                                strdate = strDate;
                                //4月 29日 22:30下午
                                //

                                try {

                                    Calendar date = Calendar.getInstance();
                                    int yyyy = date.get(Calendar.YEAR);


                                    //去掉最后两个字符
                                    Date parse = format1.parse(yyyy+ " " + strDate.substring(0, strDate.length() - 2));


                                    long time = parse.getTime();

                                    if(strDate.contains("上午")){

                                    }else{
                                        //下午  时间戳+12小时

                                        Integer  count = 12*60*60*1000;
                                        time+= count;
                                    }

                                    Date date1 = new Date(time);
                                    partyTime = format.format(date1);
                                    tv_party_time.setTextColor(ContextCompat.getColor(mActivity,R.color.white));
                                    tv_party_time.setText(yyyy  + "年 "+ strdate);

                                }catch (Exception e){

                                }

                                OkLogger.e(strDate);

                                timeDialog.dismiss();
                                //确认

                                break;
                        }
                    }
                });
                timeDialog.show();

                break;
            case R.id.tv_submit:
                if(tvPlace.getText().toString().trim().equals("请选择")){
                    toastShow("请选择聚会地点");

                }else{
                    Map<String,String> maps = new HashMap<>();
                    maps.put(Utils.APPVERSION, Utils.getVersionCode() + "");
                    maps.put(Utils.OSNAME, Utils.ANDROID);
                    maps.put(Utils.CHANNEL, Utils.ANDROID);
                    maps.put(Utils.DEVICENAME, Utils.getdeviceName());
                    maps.put(Utils.DEVICEID, Utils.ANDROID);
                    maps.put(Utils.REQTIME, AppUtils.getReqTime());
                    maps.put("partyTitle",edPartyTitle.getText().toString());
                    maps.put("partyTime",partyTime);
                    maps.put("place",tvPlace.getText().toString());
                    maps.put("stringDate",strdate);

                    if(aSwitch.isChecked()){
                        maps.put("welfare","1");
                    }else{
                        maps.put("welfare","0");
                    }
                    maps.put("maxNumber",tv_num.getText().toString());
                    maps.put("diamonds",money);
                    maps.put("introduce",edIntroduce.getText().toString().trim());

                    releasePartyPresenter.addParty(maps);
                }
                break;
            case R.id.rl_money_layout:
                showButtomDialog();
                break;

            case R.id.img_add:
                if(num<99){
                    num ++ ;
                    tv_num.setText(num + "");
                }
                break;

            case R.id.img_remove:
                if(num>2){
                    num--;
                    tv_num.setText(num + "");
                }
                break;

            case R.id.iv_title_back:
                finish();
                break;

            case R.id.rl_select_address:

                Intent  intent = new Intent(this,PartyAddressActivity.class);
                startActivityForResult(intent,0x10);

                break;
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){
            case 0x01:
                String address = data.getStringExtra("address");
                if(!"".equals(address)){
                    tvPlace.setText(address);
                    tvPlace.setTextColor(this.getResources().getColor(R.color.white));
                    tv_submit.setAlpha(1);
                }
                break;
        }
    }

    @Override
    public void playSuccess(BaseBean signInBean) {
        if(signInBean.getCode().equals("200")){
            toastShow("提交成功,审核通过后立即生效");
            finish();
        }else if(signInBean.getCode().equals("50005")){
            showExitDialog();
        }else{
            ToastUtils.showShortToast(signInBean.getMessage());
        }
    }



    //余额不足
    private void showExitDialog() {
        topUpDialog = new CommonDialog(this, R.style.dialogstyle, new CommonDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_cancel:
                        topUpDialog.dismiss();
                        break;
                    case R.id.tv_confirm:
                        topUpDialog.dismiss();

                        //startActivity(new Intent(ReleasePartyActivity.this, AccountActivity.class));

                        ChargeDialog mChargeDialog = new ChargeDialog(mActivity, mActivity);
                        mChargeDialog.show();

                        break;
                }
            }
        });
        topUpDialog.setTitleText("温馨提示").setContentText("您的余额不足，请及时充值！").setYesBtn("立即充值").show();
    }



    @Override
    public void playError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
