package com.jfkj.im.data;

import com.jfkj.im.Bean.VipData;
import com.jfkj.im.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/4
 * </pre>
 */
public class DataProvide {
    private static int[] mDrawables = {R.drawable.vip_3, R.drawable.vip_14, R.drawable.vip_15, R.drawable.vip_20
            , R.drawable.vip_40, R.drawable.vip_50, R.drawable.vip_60,R.drawable.vip_60};

    private static int[] drawableDark={R.drawable.member_icon_vip3_dark,R.drawable.member_icon_vip6_dark,R.drawable.member_icon_vip14_dark,
            R.drawable.member_icon_vip15_dark,R.drawable.member_icon_vip20_dark,R.drawable.member_icon_vip50_dark,R.drawable.member_icon_vip60_dark,R.drawable.member_icon_vip100_dark};

    private static int[] drawableLight={R.drawable.member_icon_vip3_light,R.drawable.member_icon_vip6_light,R.drawable.member_icon_vip14_light,R.drawable.member_icon_vip15_light,
            R.drawable.member_icon_vip20_light,R.drawable.member_icon_vip50_light,R.drawable.member_icon_vip60_light,R.drawable.member_icon_vip100_light};

    private static String[] vipLevel = {"VIP3", "VIP6", "VIP14", "VIP15", "VIP20", "VIP50","VIP70", "VIP100"};
    private static String[] vipPermission = {"创建俱乐部", "自定义冒险游戏", "视频聊天", "不限好友人数", "隐藏在线状态", "隐藏地理位置", "隐藏俱乐部成员","开启超级俱乐部"};

    /**
     * 体重
     *
     * @return
     */
    public static List<String> getWeightList() {
        List<String> weightlist = new ArrayList<>();
        for (int i = 35; i <= 100; i++) {
            weightlist.add(i + "");
        }
        return weightlist;
    }

    /**
     * 身高
     *
     * @return
     */
    public static List<String> getHeightList() {
        List<String> heightList = new ArrayList<>();
        for (int i = 150; i <= 210; i++) {
            heightList.add(i + "");
        }
        return heightList;
    }

    /**
     * 学历
     *
     * @return
     */
    public static List<String> getEducationList() {
        List<String> educationList = new ArrayList<>();
        educationList.add("初中");
        educationList.add("高中");
        educationList.add("大专");
        educationList.add("本科");
        educationList.add("硕士及以上");
        return educationList;
    }


    public  static  List<VipData> getVipData(){
        List<VipData> list = new ArrayList<>();
        for (int i = 0;i<mDrawables.length;i++){
            VipData vipData = new VipData();
            vipData.setDrawable(mDrawables[i]);
            vipData.setVipLevel(vipLevel[i]);
            vipData.setVipPermission(vipPermission[i]);
            vipData.setmDrawableDark(drawableDark[i]);
            vipData.setmDrawableLight(drawableLight[i]);
            list.add(vipData);
        }
        return list;
    }

    public static List<String> getOccupation(){
        List<String> list = new ArrayList<>();
        list.add("高管");
        list.add("职业经理");
        list.add("市场");
        list.add("产品");
        list.add("运营");
        list.add("销售");
        list.add("技术");
        list.add("商务");
        list.add("财务");
        list.add("设计");
        list.add("策划");
        list.add("空乘");
        list.add("公务员");
        list.add("科研人员");
        list.add("作家");
        list.add("律师");
        list.add("医生");
        list.add("自由职业者");


        return list;
    }

    public static List<String> getBlank(){

        List<String> list = new ArrayList<>();
        list.add("中国农业银行");
        list.add("中国建设银行");
        list.add("中国邮政储蓄银行");
        list.add("中国银行");
        list.add("中国招商银行");
        list.add("中国交通银行");

        return list;
    }

    /**
     * 行业
     */
    public static List<String> getIndustry(){
        List<String> list = new ArrayList<>();

        list.add("互联网及通讯行业");
        list.add("文化艺术行业");
        list.add("广告行业");
        list.add("影视娱乐行业");
        list.add("金融行业");
        list.add("医药行业");
        list.add("健康健身行业");
        list.add("制造业");
        list.add("媒体公关行业");
        list.add("贸易零售业");
        list.add("教育科研业");
        list.add("房地产建筑行业");
        list.add("服务业");
        list.add("政府机关");



        return list;
    }
    /**
     * 抽烟
     */
    public static List<String> getSmokingList(){
        List<String> list = new ArrayList<>();
        list.add("从不抽烟");
        list.add("偶尔抽烟");
        list.add("经常抽烟");
        return list;
    }

    /**
     * 喝酒
     */
    public static List<String> getWinkList(){
        List<String> list = new ArrayList<>();
        list.add("从不喝酒");
        list.add("偶尔喝酒");
        list.add("经常喝酒");

        return list;
    }


    /**
     * 菜系
     */
    public static List<String> getLikeCuisine(){
        List<String> list = new ArrayList<>();

        list.add("川菜");
        list.add("粤菜");
        list.add("湘菜");
        list.add("鲁菜");
        list.add("淮扬菜");
        list.add("日料");
        list.add("徽菜");
        list.add("闽菜");
        list.add("浙菜");
        list.add("法餐");
        list.add("韩餐");
        list.add("日餐");
        list.add("意大利餐");
        list.add("墨西哥餐");



        return  list;

    }


    /**
     * 月份
     */




    /**
     * 日期选择只显示当前时间之后的日期
     *
     * @return
     */
    public static List<String> getMonth(){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        List<String> strings = new ArrayList<>();
        for (int i = 0 ; i < 12 ; i++){
            if(i+1 >= month){
                strings.add(i+1+"月");
            }
        }
        return strings;
    }


    /**
     * 获取选中月有多少天
     * @param
     * @param month
     * @return
     */
    public static int getDays(int month) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);

        int days = 0;
        if (month != 2) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    days = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    days = 30;

            }
        } else {
            // 闰年
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
                days = 29;
            else
                days = 28;
        }

        return days;
    }


    /**
     *
     *
     * @param month 想查询那个月
     * @param
     * @return
     */
    public static List<String> getDay( int month){

        Calendar cal = Calendar.getInstance();
        List<String> list;
        int day = cal.get(Calendar.DAY_OF_MONTH);  //当前日
        int presentDay = getDays(month);  //当月有多少天
        int presentMonth = cal.get(Calendar.MONTH) + 1;

        //如果等于当前月份
        if(month == presentMonth){
           list  = new ArrayList<>();
            for (int i = 0 ; i < presentDay ; i++){
                if(i+1>=day){
                    list.add(i +1+"日");
                }
            }
        }else{
            list = new ArrayList<>();
            for (int i = 0 ; i < presentDay ; i++){
                    list.add(i+1+"日");
            }

        }

        return list;
    }




    /**
     * 获取小时分钟
     * @return
     */
    public static List<String> getHourMinute(){
        List<String> hourMinute = new ArrayList<>();
        hourMinute.add("0:00上午");hourMinute.add("0:30上午");hourMinute.add("1:00上午");hourMinute.add("1:30上午");hourMinute.add("2:00上午");hourMinute.add("2:30上午");
        hourMinute.add("3:00上午");hourMinute.add("3:30上午");
        hourMinute.add("4:00上午");hourMinute.add("4:30上午");
        hourMinute.add("5:00上午");hourMinute.add("5:30上午");
        hourMinute.add("6:00上午");
        hourMinute.add("6:30上午");hourMinute.add("7:00上午");
        hourMinute.add("7:30上午");
        hourMinute.add("8:00上午");hourMinute.add("8:30上午");
        hourMinute.add("9:00上午");
        hourMinute.add("9:30上午");hourMinute.add("10:00上午");
        hourMinute.add("10:30上午");
        hourMinute.add("11:00上午");
        hourMinute.add("11:30上午");hourMinute.add("0:00下午");
        hourMinute.add("0:30下午");
        hourMinute.add("1:00下午");
        hourMinute.add("1:30下午");
        hourMinute.add("2:00下午");hourMinute.add("2:30下午");
        hourMinute.add("3:00下午");
        hourMinute.add("3:30下午");
        hourMinute.add("4:00下午");hourMinute.add("4:30下午");
        hourMinute.add("5:00下午");
        hourMinute.add("5:30下午");
        hourMinute.add("6:00下午");
        hourMinute.add("6:30下午");hourMinute.add("7:00下午");
        hourMinute.add("7:30下午");
        hourMinute.add("8:00下午");
        hourMinute.add("8:30下午");
        hourMinute.add("9:00下午");
        hourMinute.add("9:30下午");hourMinute.add("10:00下午");
        hourMinute.add("10:30下午");hourMinute.add("11:00下午");
        hourMinute.add("11:30下午");



        return hourMinute;
    }

}
