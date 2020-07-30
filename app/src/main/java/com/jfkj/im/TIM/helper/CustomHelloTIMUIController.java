package com.jfkj.im.TIM.helper;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.Receivegifbean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.bean.Gifbean;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.Giftdialog;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMMessageLocator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


////0礼物   1 性格测试  2 红包  3冒险游戏
public class CustomHelloTIMUIController {

    //svga 特效礼物ID
    static String ids = "93895334044237824,93895470157791232,93895587132735488,93895681303248896,93895788715180032,93895903374868480,93896009918578688,120364533444640768";

    static TIMConversation conversation;
    private static final String TAG = CustomHelloTIMUIController.class.getSimpleName();
    static Giftdialog giftdialog;
    static public onclickreceiveGif receiveGif;

    public static void setReceiveGif(onclickreceiveGif gif) {
        receiveGif = gif;
    }

    static TextView getTv_message;
    static ConstraintLayout constraintlayout_gif;

    public static void onDraw(Context context, ICustomMessageViewGroup parent, final CustomMessage data, String resultdata, boolean isself, MessageInfo messageInfo) {
        giftdialog = new Giftdialog(context);

        try {

            JSONObject jsonObject = new JSONObject(resultdata);
            conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, messageInfo.getFromUser());
            if (jsonObject.getString("type").equals("0")) {//这是礼物的自定义数据
                Gifbean gifbean = JSON.parseObject(resultdata, Gifbean.class);

                View view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.test_custom_message_layout1, null, false);
                @SuppressLint("CutPasteId") TextView tv_message = view.findViewById(R.id.tv_message);
                getTv_message = view.findViewById(R.id.tv_message);
                TextView tv_title = view.findViewById(R.id.tv_title);
                if (isself) {
                    tv_title.setText("赠送对方一份礼物");
                } else {
                    tv_title.setText("赠送你一份礼物");
                }



                if (messageInfo.isSelf()) {
                    tv_message.setText("礼物未拆开");
                    tv_message.setTextColor(ContextCompat.getColor(context,R.color.colorRadioText));
                    if(SPUtils.getInstance(App.getAppContext()).getBoolean(Utils.APPID+gifbean.getBody().getOrderId(),false)){
                        tv_message.setText("礼物已拆开");
                        tv_message.setTextColor(ContextCompat.getColor(context,R.color.colorRadioText));
                    }
                } else {
                    tv_message.setText("点击拆开礼物");
                    tv_message.setTextColor(ContextCompat.getColor(context,R.color.cFF0D52));
                }

                parent.addMessageContentView(view);
                constraintlayout_gif = view.findViewById(R.id.constraintlayout_gif);

                constraintlayout_gif.setVisibility(View.VISIBLE);
                if (!isself) {
//                    view.setBackgroundResource(R.drawable.shape_chat_visitor);
                    if (messageInfo.getTIMMessage().getCustomInt() == 0) {
                        constraintlayout_gif.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!gifbean.isReceive()) {

                                    Map<String, String> map = new HashMap<>();
                                    map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
                                    map.put(Utils.OSNAME, Utils.ANDROID);
                                    map.put(Utils.CHANNEL, Utils.ANDROID);
                                    map.put(Utils.DEVICENAME, Utils.getdeviceName());
                                    map.put(Utils.DEVICEID, Utils.ANDROID);
                                    map.put(Utils.REQTIME, AppUtils.getReqTime());
                                    map.put(Utils.USERID, messageInfo.getFromUser());
                                    map.put(Utils.ORDERID, gifbean.getBody().getOrderId());
                                    map.put(Utils.TRADEORDERNO, gifbean.getBody().getTradeOrderNo());
                                    map.put(Utils.SIGN,AppUtils.getReqTime());
                                    OkHttpUtils.post()
                                            .tag(App.getAppContext())
                                            .url(ApiStores.base_url + "/friend/receiveFriendGift")
                                            .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                                            .addHeader(Utils.SIGN,  MD5Utils.getMD5String(Utils.KEY+AppUtils.getReqTime()))
                                            .params(map)
                                            .build()
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onError(Call call, Exception e, int id) {

                                                }

                                                @SuppressLint("SetTextI18n")
                                                @Override
                                                public void onResponse(String response, int id) {
                                                    try {
                                                        JSONObject jsonObjects = new JSONObject(response);
                                                        if (jsonObjects.getString("code").equals("200")) {
                                                            Receivegifbean receivegifbean = JSON.parseObject(response, Receivegifbean.class);
                                                            gifbean.setReceive(true);
                                                            giftdialog.show();


                                                            //判断显示礼物方式
                                                            if(ids.contains(gifbean.getBody().getGiftId())){
                                                                giftdialog.setGiftId(gifbean.getBody().getGiftId());
                                                            }else{
                                                                giftdialog.setUrl(gifbean.getBody().getGifimgurl());
                                                            }

                                                            gifbean.setSendType("1");



                                                            double num = Double.parseDouble(receivegifbean.getData().getMoney());
                                                            tv_message.setText("已拆开(金币+" + (int) num + ")");
                                                            gifbean.setMessage("已拆开(金币+" + (int) num + ")");
                                                            tv_message.setTextColor(ContextCompat.getColor(context,R.color.colorRadioText));
                                                            TIMMessage timMessage = messageInfo.getTIMMessage();
                                                            view.setAlpha(0.5f);
                                                            timMessage.setCustomInt(1);
                                                            timMessage.setCustomStr("已拆开(金币+" + (int) num + ")");
                                                            MessageInfo info = MessageInfoUtil.buildgifMessage(gifbean.getBody().getGiftId(),
                                                                    gifbean.getBody().getGifimgurl(), gifbean.getBody().getOrderId(),
                                                                    gifbean.getBody().getTradeOrderNo(), 4 + "", 1 + "", 1 + "", messageInfo.getTIMMessage().getSeq() + "", messageInfo.getTIMMessage().getRand() + "", messageInfo.getTIMMessage().getMessageLocator().getTimestamp() + "");
                                                            //       info.setFromUser(messageInfo.getFromUser());
                                                            conversation.sendMessage(info.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                                                                @Override
                                                                public void onError(final int code, final String desc) {


                                                                }

                                                                @Override
                                                                public void onSuccess(TIMMessage timMessage) {

                                                                }
                                                            });

                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                    } else {
                        getTv_message.setText(messageInfo.getTIMMessage().getCustomStr());
                        getTv_message.setTextColor(ContextCompat.getColor(context,R.color.colorRadioText));
                    }
                } else {
                    //这里显示对方接收礼物的情况
                    if (messageInfo.getCustomInt() == 1) {
                        getTv_message.setText("礼物已拆开");
                        getTv_message.setTextColor(ContextCompat.getColor(context,R.color.colorRadioText));
                    }
                }
            }

            if (jsonObject.getString("type").equals("4")) {
                List<TIMMessageLocator> locaors = new ArrayList<>();

                TIMMessageLocator locator = messageInfo.getTIMMessage().getMessageLocator();

                locaors.add(locator.setSeq(jsonObject.getJSONObject("body").getLong("seq")));
                locaors.add(locator.setRand(jsonObject.getJSONObject("body").getLong("rand")));
                locaors.add(locator.setTimestamp(jsonObject.getJSONObject("body").getLong("timestamp")));
                String orderId = jsonObject.getJSONObject("body").getString("orderId");

                locaors.add(locator.setSelf(true));
                constraintlayout_gif.setVisibility(View.VISIBLE);
                conversation.findMessages(locaors, new TIMValueCallBack<List<TIMMessage>>() {
                    @Override
                    public void onError(int code, String desc) {

                    }

                    @Override
                    public void onSuccess(List<TIMMessage> timMessages) {
                        if (timMessages.size() > 0) {
                            timMessages.get(0).setCustomInt(1);
                            timMessages.get(0).setCustomStr("对方拆开了您的礼物");
                            boolean remove = messageInfo.getTIMMessage().remove();


                        }

                        //  TIMFriend friend = TIMFriendshipManager.getInstance().queryFriend(messageInfo.getFromUser());
                        //   Intent intent = new Intent(App.getAppContext(), ChatActivity.class);
                        //  ChatInfo chatInfo = new ChatInfo();
                        //  chatInfo.setChatName("friend.getTimUserProfile().getNickName()");

                        //  intent.putExtra(Constants.CHAT_INFO, chatInfo);
                        //  chatInfo.setType(TIMConversationType.C2C);
                        //  chatInfo.setId(messageInfo.getFromUser()+"");
                        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        //   App.getAppContext().startActivity(intent);


                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface onclickreceiveGif {
        public void receivefig(MessageInfo messageInfo);
    }
}
