package com.jfkj.im.payments.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jfkj.im.entity.EventMessage;
import com.jfkj.im.payments.weixin.WXPay;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.greenrobot.eventbus.EventBus;

import static com.jfkj.im.utils.Utils.REFRUSH_USER_BALANCE;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {


    private static final String TAG = "WXPayEntryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_wxpay_call_back);

        if(WXPay.getInstance() != null) {
            WXPay.getInstance().getWXApi().handleIntent(getIntent(), this);
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if(WXPay.getInstance() != null) {
            WXPay.getInstance().getWXApi().handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            switch (resp.errCode) {
                case 0://支付成功

                    Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                    EventMessage eventMessage = new EventMessage();
                    eventMessage.setType(REFRUSH_USER_BALANCE);
                    eventMessage.setMessage("充值成功");
                    EventBus.getDefault().post(eventMessage);


                    Log.d(TAG, "onResp: resp.errCode = 0   支付成功");
                    break;
                case -1://错误，可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
                    Toast.makeText(this, "支付错误" + resp.errCode, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResp: resp.errCode = -1  支付错误");
                    break;
                case -2://用户取消，无需处理。发生场景：用户不支付了，点击取消，返回APP。
                    Log.d(TAG, "onResp: resp.errCode = -2  用户取消");
                    Toast.makeText(this, "用户取消" + resp.errCode, Toast.LENGTH_SHORT).show();
                    break;

            }


            Log.d(TAG, "onResp:  finish" );
            finish();//这里需要关闭该页面
        }
    }
}
