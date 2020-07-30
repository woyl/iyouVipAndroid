package com.jfkj.im.retrofit;

public class Urls {


    /**
     * 环境切换  --
     * 1.域名
     * 2.腾讯appid 切换
     * 3.bugly 腾讯切换
     *
     * 正式服
     *
     * @TGS#3SM5K5KGH 广场
     * @TGS#aWK6K5KGZ Iyou圈
     * 测试服
     *
     * @TGS#aHAFI5KGO Iyou圈
     * @TGS#3TIG3IJGR 广场
     */


//    /** 广场  正式*/
//    public final static String square_chat = "@TGS#3SM5K5KGH";
//    /**Iyou圈 正式*/
//    public final static String i_you_circle = "@TGS#aWK6K5KGZ";
//    //正式服
//    public final static String BuglyAppID = "74e0d0aafa";
//    //正式api
//    public final static  String base_url = "http://api.vipiyou.com";
//    //文件上传
//    public final static String baseupload_url = "http://upload.vipiyou.com";
//    /*文件类域名*/
//    public final static String baseupload_url = "http://file.vipiyou.com";

    /**测试api*/
    public final static String square_chat = "@TGS#3TIG3IJGR";
    public final static String i_you_circle = "@TGS#aHAFI5KGO";
    public final static String BuglyAppID = "b9bb41fab5";
    public final static String base_url = "http://iyapi.tiantiancaidian.com";
//    public final static String base_url = "http://192.168.110.158:8081";

    public final static String baseupload_url = "http://iyupload.tiantiancaidian.com";

//    本地Api
//    String base_url = "http://192.168.110.158:8081";

    //文件类域名
    public final static String baseJsonUrl = "http://file.vipiyou.com";

    //获取国家地区代码
    public final static String nationalareaurl ="/static/NationalArea.json";

    //vip特权地址-
    public final static String vip_url = "http://iyfile.tiantiancaidian.com/static/iyou/vip.html";

}
