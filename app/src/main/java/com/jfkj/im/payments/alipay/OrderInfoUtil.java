//package com.jfkj.im.payments.alipay;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.alipay.sdk.app.PayTask;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Random;
//
//public class OrderInfoUtil {
//
//    private static final int SDK_PAY_FLAG = 1;
//    private Activity context;
//    private ALiPayBuilder builder;
//
//    private OrderInfoUtil(ALiPayBuilder builder) {
//        this.builder = builder;
//    }
//
//    private Handler mHandler = new Handler() {
//        @SuppressLint("HandlerLeak")
//        public void handleMessage(Message msg) {
//            //            返回码	含义
////            9000	订单支付成功
////            8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
////            4000	订单支付失败
////            5000	重复请求
////            6001	用户中途取消
////            6002	网络连接出错
////            6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
////            其它	其它支付错误
//            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
//            switch (payResult.getResultStatus()) {
//                case "9000":
//                    Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
//                    break;
//                case "8000":
//                case "6004":
//                    Toast.makeText(context, "正在处理中", Toast.LENGTH_SHORT).show();
//                    break;
//                case "4000":
//                    Toast.makeText(context, "订单支付失败", Toast.LENGTH_SHORT).show();
//                    break;
//                case "5000":
//                    Toast.makeText(context, "重复请求", Toast.LENGTH_SHORT).show();
//                    break;
//                case "6001":
//                    Toast.makeText(context, "已取消支付", Toast.LENGTH_SHORT).show();
//                    break;
//                case "6002":
//                    Toast.makeText(context, "网络连接出错", Toast.LENGTH_SHORT).show();
//                    break;
//                default:
//                    Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    };
//
//
//    /**
//     * 签名发在客户端来做。
//     *
//     * @param context
//     */
//    public void toALiPay(final Activity context) {
//        this.context = context;
//        boolean rsa2 = (builder.getRsa2().length() > 0);
//        Map<String, String> params = buildOrderParamMap(rsa2);
//        String orderParam = buildOrderParam(params);
//        String privateKey = rsa2 ? builder.getRsa2() : builder.getRsa();
//        String sign = getSign(params, privateKey, rsa2);
//        final String orderInfo = orderParam + "&" + sign;
//        Log.e("msg", "toALiPay: " + orderInfo);
//        Runnable payRunnable = new Runnable() {
//
//            @Override
//            public void run() {
//                PayTask alipay = new PayTask(context);
//                Map<String, String> result = alipay.payV2
//                        (orderInfo, true);
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
//            }
//        };
//
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
//    }
//
//    /**
//     * 签名在服务端来做
//     *
//     * @param context
//     * @param orderInfo
//     */
//    public void toALiPay(final Activity context, final String orderInfo) {
//        this.context = context;
//        Runnable payRunnable = new Runnable() {
//
//            @Override
//            public void run() {
//                PayTask alipay = new PayTask(context);
//                Map<String, String> result = alipay.payV2
//                        (orderInfo, true);
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
//            }
//        };
//
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
//    }
//
//
//    /**
//     * 构造支付订单参数列表
//     */
//    public Map<String, String> buildOrderParamMap( boolean rsa2) {
//        Map<String, String> keyValues = new HashMap<String, String>();
//
//        keyValues.put("app_id", builder.appid);
//
//        keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"" + builder.money + "\",\"subject\":\"" + builder.title + "\",\"out_trade_no\":\"" + builder.orderTradeId + "\"}");
//
//        keyValues.put("charset", "utf-8");
//
//        keyValues.put("method", "alipay.trade.app.pay");
//        //回调接口
//        keyValues.put("notify_url", builder.getNotifyUrl());
//
//        keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");
//
////		keyValues.put("timestamp", "2016-07-29 16:55:53");
//        keyValues.put("timestamp", getOutTradeNo());
//
//        keyValues.put("version", "1.0");
//
//
//        return keyValues;
//    }
//
//
//    /**
//     * 构造支付订单参数信息
//     *
//     * @param map 支付订单参数
//     * @return
//     */
//    public static String buildOrderParam(Map<String, String> map) {
//        List<String> keys = new ArrayList<String>(map.keySet());
//
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < keys.size() - 1; i++) {
//            String key = keys.get(i);
//            String value = map.get(key);
//            sb.append(buildKeyValue(key, value, true));
//            sb.append("&");
//        }
//
//        String tailKey = keys.get(keys.size() - 1);
//        String tailValue = map.get(tailKey);
//        sb.append(buildKeyValue(tailKey, tailValue, true));
//
//        return sb.toString();
//    }
//
//    /**
//     * 对支付参数信息进行签名
//     *
//     * @param map 待签名授权信息
//     * @return
//     */
//    public static String getSign(Map<String, String> map, String rsaKey, boolean rsa2) {
//        List<String> keys = new ArrayList<String>(map.keySet());
//        // key排序
//        Collections.sort(keys);
//
//        StringBuilder authInfo = new StringBuilder();
//        for (int i = 0; i < keys.size() - 1; i++) {
//            String key = keys.get(i);
//            String value = map.get(key);
//            authInfo.append(buildKeyValue(key, value, false));
//            authInfo.append("&");
//        }
//
//        String tailKey = keys.get(keys.size() - 1);
//        String tailValue = map.get(tailKey);
//        authInfo.append(buildKeyValue(tailKey, tailValue, false));
//
//        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, rsa2);
//        String encodedSign = "";
//
//        try {
//            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return "sign=" + encodedSign;
//    }
//
//    /**
//     * 拼接键值对
//     *
//     * @param key
//     * @param value
//     * @param isEncode
//     * @return
//     */
//    private static String buildKeyValue(String key, String value, boolean isEncode) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(key);
//        sb.append("=");
//        if (isEncode) {
//            try {
//                sb.append(URLEncoder.encode(value, "UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                sb.append(value);
//            }
//        } else {
//            sb.append(value);
//        }
//        return sb.toString();
//    }
//
//
//    /**
//     * 要求外部订单号必须唯一。
//     *
//     * @return
//     */
//    private static String getOutTradeNo() {
//        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
//        Date date = new Date();
//        String key = format.format(date);
//
//        Random r = new Random();
//        key = key + r.nextInt();
//        key = key.substring(0, 15);
//        return key;
//    }
//
//
//    public static class ALiPayBuilder {
//        private String rsa2 = "";
//        private String rsa = "";
//        private String appid;
//        private String money;
//        private String title;
//        private String notifyUrl;
//        private String orderTradeId;
//
//        public OrderInfoUtil build() {
//            return new OrderInfoUtil(this);
//        }
//
//        public String getOrderTradeId() {
//            return orderTradeId;
//        }
//
//        public ALiPayBuilder setOrderTradeId(String orderTradeId) {
//            this.orderTradeId = orderTradeId;
//            return this;
//        }
//
//        public String getRsa2() {
//            return rsa2;
//        }
//
//        public ALiPayBuilder setRsa2(String rsa2) {
//            this.rsa2 = rsa2;
//            return this;
//        }
//
//        public String getRsa() {
//            return rsa;
//        }
//
//        public ALiPayBuilder setRsa(String rsa) {
//            this.rsa = rsa;
//            return this;
//        }
//
//        public String getAppid() {
//            return appid;
//        }
//
//        public ALiPayBuilder setAppid(String appid) {
//            this.appid = appid;
//            return this;
//        }
//
//        public String getMoney() {
//            return money;
//        }
//
//        public ALiPayBuilder setMoney(String money) {
//            this.money = money;
//            return this;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public ALiPayBuilder setTitle(String title) {
//            this.title = title;
//            return this;
//        }
//
//        public String getNotifyUrl() {
//            return notifyUrl;
//        }
//
//        public ALiPayBuilder setNotifyUrl(String notifyUrl) {
//            this.notifyUrl = notifyUrl;
//            return this;
//        }
//    }
//
//}
