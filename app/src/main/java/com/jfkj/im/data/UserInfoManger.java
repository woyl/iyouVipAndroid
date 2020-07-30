package com.jfkj.im.data;

import android.text.TextUtils;

import com.jfkj.im.App;
import com.jfkj.im.TIM.component.photoview.Util;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;

import static com.jfkj.im.utils.Utils.SP_KEY_USER_UPGRADE_MONEY;

/**
 * <pre>
 * Description:  用户信息保存管理类
 * @author :   ys
 * @date :         2019/12/13
 * </pre>
 */
public class UserInfoManger {

    private static SPUtils mUtils = SPUtils.getInstance(App.getAppContext());


    /**
     * 是否已登录
     *
     * @return
     */
    public static boolean isLogin() {
        if (!TextUtils.isEmpty(getUserId())) {
            return true;
        }

        return false;
    }


    public static void savePhoneNumber(String phone) {
        mUtils.put(Utils.SP_KEY_PHONE_NUM, phone);
    }

    /**
     * RegistrationID
     * @param registrationID
     */
    public static void saveRegistrationID(String registrationID) {
        mUtils.put(Utils.SP_KEY_REGISTRATION_ID, registrationID);
    }

    public static String getRegistrationID(){
        return mUtils.getString(Utils.SP_KEY_REGISTRATION_ID, "");
    }

    /**
     * nickName
     * @param nickName
     */
    public static void saveNickName(String nickName) {
        mUtils.put(Utils.SP_KEY_NICK_NAME, nickName);
    }

    public static String getNickName(){
        return mUtils.getString(Utils.SP_KEY_NICK_NAME, "");
    }
    /**
     * 描述get
     */
    public static void saveDescribe(String describe) {
        mUtils.put(Utils.SP_KEY_USER_DESCRIBE, describe);
    }

    public static String getDescribe(){
        return mUtils.getString(Utils.SP_KEY_USER_DESCRIBE, "");
    }

    /**
     * 家乡
     */
    public static void saveHometown(String hometown) {
        mUtils.put(Utils.SP_KEY_USER_HOMETOWN, hometown);
    }

    public static String getHometown(){
        return mUtils.getString(Utils.SP_KEY_USER_HOMETOWN, "");
    }

    /**
     * 星座
     */
    public static void saveConstellation(String constellation) {
        mUtils.put(Utils.SP_KEY_USER_CONSTELLATION, constellation);
    }

    public static String getConstellation(){
        return mUtils.getString(Utils.SP_KEY_USER_CONSTELLATION, "");
    }

    /**
     * 真实name
     * @param trueName
     */

    public static void saveTrueName(String trueName) {
        mUtils.put(Utils.SP_KEY_TRUE_NAME, trueName);
    }

    public static void saveIdno(String idno) {
        mUtils.put(Utils.SP_KEY_IDNO ,idno);
    }

    public static void saveSummary(String summary) {
        mUtils.put(Utils.SP_KEY_SUMMARY, summary);
    }

    /**
     * token
     * @param token
     */
    public static void saveToken(String token) {
        mUtils.put(Utils.SP_KEY_USER_TOKEN, token);
    }



    public static String getToken(){
        return mUtils.getString(Utils.SP_KEY_USER_TOKEN, "");
    }

    /**
     * 是否获得位置权限
     */
    public static void saveIsGranted(boolean b){
        mUtils.put(Utils.SP_KEY_USER_HAS_GRANTED,b);
    }

    public static boolean getIsGranted(){
        return mUtils.getBoolean(Utils.SP_KEY_USER_HAS_GRANTED, false);
    }

    /**
     * 性别
     * @param gender
     */
    public static void saveGender(String gender) {
        mUtils.put(Utils.SP_KEY_USER_GENDER, gender);
    }

    public static String getGender(){
        return mUtils.getString(Utils.SP_KEY_USER_GENDER, "");
    }

    /**
     * Age
     */
    public static void saveAge(String age) {
        mUtils.put(Utils.SP_KEY_USER_AGE, age);
    }

    public static String getAge(){
        return mUtils.getString(Utils.SP_KEY_USER_AGE, "");
    }


    /**
     * Smoking
     */
    public static void savaSmokingy(String industry){
        mUtils.put(Utils.SP_KEY_USER_SMOKING,industry);
    }

    public static String getSmoking(){
        return mUtils.getString(Utils.SP_KEY_USER_SMOKING);
    }

    /**
     * Drinkwine
     */
    public static void savaDrinkwine(String industry){
        mUtils.put(Utils.SP_KEY_USER_DRINKWINK,industry);
    }

    public static String getDrinkwine(){
        return mUtils.getString(Utils.SP_KEY_USER_DRINKWINK);
    }


    /**
     * LikeCuisine
     */

    /**
     * Drinkwine
     */
    public static void savaLikeCuisine(String industry){
        mUtils.put(Utils.SP_KEY_USER_LIKECUISINE,industry);
    }

    public static String getLikeCuisine(){
        return mUtils.getString(Utils.SP_KEY_USER_LIKECUISINE);
    }


    /**
     * occupation
     */

    public static void savaOccupation(String occupation){
        mUtils.put(Utils.SP_KEY_USER_OCCUPATION,occupation);
    }

    public static String getOccupation(){
        return mUtils.getString(Utils.SP_KEY_USER_OCCUPATION);
    }

    /**
     * industry
     */

    public static void savaindustry(String industry){
        mUtils.put(Utils.SP_KEY_USER_INDUSTRY,industry);
    }

    public static String getindustry(){
        return mUtils.getString(Utils.SP_KEY_USER_INDUSTRY);
    }


    /**
     * School
     */

    public static void savaSchool(String school){
        mUtils.put(Utils.SP_KEY_USER_SCHOOL,school);
    }

    public static String getSchool(){
        return mUtils.getString(Utils.SP_KEY_USER_SCHOOL);
    }

    /**
     * userId
     * @return
     */
    public static void saveUserId(String userId) {
        mUtils.put(Utils.SP_KEY_USER_ID, userId);
    }

    public static String getUserId() {
        return mUtils.getString(Utils.SP_KEY_USER_ID, "");
    }

    /**
     * balance
     * @return
     */
    public static void saveUserBalance(String balance) {
        mUtils.put(Utils.SP_KEY_USER_BALANCE, balance);
    }

    public static String getUserBanlance() {
        return mUtils.getString(Utils.SP_KEY_USER_BALANCE, "");
    }

    /**
     * saveUserCost  消费金额 就是送出礼物数量
     * @return
     */
    public static void saveUserCost(String cost) {
        mUtils.put(Utils.SP_KEY_USER_COST, cost);
    }

    public static String getUserCost() {
        return mUtils.getString(Utils.SP_KEY_USER_COST, "");
    }

    /**
     * gift  收到礼物
     * @return
     */
    public static void saveUserGift(String gift) {
        mUtils.put(Utils.SP_KEY_USER_GIFT, gift);
    }

    public static String getUserGift() {
        return mUtils.getString(Utils.SP_KEY_USER_GIFT, "");
    }

    /**
     * 累计获得的礼物
     */
    public static void savaAccumulatedGifts(String goldIncome ){
        mUtils.put(SP_KEY_USER_UPGRADE_MONEY,goldIncome);
    }

    public static String getAccumulatedGifts(){
        return mUtils.getString(SP_KEY_USER_UPGRADE_MONEY,"");
    }

    /**
     * saveUpgradeAmount 升级还需要多少充值
     * @return
     */
    public static void saveUpgradeAmount(String amount) {
        mUtils.put(Utils.SP_KEY_USER_UPGRADE_AMOUNT, amount);
    }

    public static String getUpgradeAmount() {
        return mUtils.getString(Utils.SP_KEY_USER_UPGRADE_AMOUNT, "");
    }

    /**
     * gold  金币
     * @return
     */
    public static void saveUserGold(String gold) {
        mUtils.put(Utils.SP_KEY_USER_GOLD, gold);
    }

    public static String getUserGold() {
        return mUtils.getString(Utils.SP_KEY_USER_GOLD, "");
    }


    public static void saveMineUserHeadState(String userHeadUrl){
        mUtils.put(Utils.SP_KEY_MINE_HEADER_STATE,userHeadUrl);
    }

    public static String getMineUserHeadState(){
        return mUtils.getString(Utils.SP_KEY_MINE_HEADER_STATE,"");
    }



    public static void savaMineUserHeadUrl(String userHeadUrl){
        mUtils.put(Utils.SP_KEY_MINE_HEADER_URL,userHeadUrl);
    }

    public static String getMineUserHeadUrl(){
        return mUtils.getString(Utils.SP_KEY_MINE_HEADER_URL,"");
    }

    /**
     * userHeadUrl
     * @return
     */
    public static void saveUserHeadUrl(String userHeadUrl) {
        mUtils.put(Utils.SP_KEY_HEADER_URL, userHeadUrl);
    }

    public static String getUserHeadUrl() {
        return mUtils.getString(Utils.SP_KEY_HEADER_URL, "");
    }


    public static void saveUserHeadState(String userHeadState) {
        mUtils.put(Utils.SP_KEY_HEADER_STATE, userHeadState);
    }

    public static String getUserHeadState() {
        return mUtils.getString(Utils.SP_KEY_HEADER_STATE, "");
    }



    /**
     *   user  video Url
     */
    public static void saveUserVideo(String videoUrl) {
        mUtils.put(Utils.SP_KEY_USER_VIDEO_URL, videoUrl);
    }

    public static String getUserVideoUrl() {
        return mUtils.getString(Utils.SP_KEY_USER_VIDEO_URL, "");
    }

    /**
     *   Longitude
     */
    public static void saveLongitude(String longitude) {
        mUtils.put(Utils.SP_KEY_USER_LONGITUDE, longitude);
    }

    public static String getLongitude() {
        String longitude = mUtils.getString(Utils.SP_KEY_USER_LONGITUDE, "0");

        return longitude.equals("") ? "114.024587":longitude;
    }
    public static String getcity() {
        String city = mUtils.getString(Utils.CITY, "");
        return city.equals("")?"未知城市":city;
    }
    public static void savecity(String city) {
        mUtils.put(Utils.CITY, city);
    }
    /**
     *   latitude
     */
    public static void saveLatitude(String latitude) {
        mUtils.put(Utils.SP_KEY_USER_LATITUDE, latitude);
    }

    public static String getLatitude() {
        String latitude = mUtils.getString(Utils.SP_KEY_USER_LATITUDE, "0");
        return latitude.equals("")?"22.531813":latitude;
    }

    /**
     * height
     * @return
     */
    public static void saveHeight(String height) {
        mUtils.put(Utils.SP_KEY_USER_HEIGHT, height);
    }

    public static String getUserHeight() {
        return mUtils.getString(Utils.SP_KEY_USER_HEIGHT, "");
    }


    /**
     * 黑卡标识
     */
    public static void savaVipCard(String VipCard){
        mUtils.put(Utils.SP_KEY_USER_VIPCARD,VipCard);
    }

    public static String getVipCard(){

        return  mUtils.getString(Utils.SP_KEY_USER_VIPCARD,"");
    }

    /**
     * weight
     * @return
     */
    public static void saveWeight(String weight) {
        mUtils.put(Utils.SP_KEY_USER_WEIGHT, weight);
    }

    public static String getUserWeight() {
        return mUtils.getString(Utils.SP_KEY_USER_WEIGHT, "");
    }

    /**
     * education
     * @return
     */
    public static void saveEducation(String education) {
        mUtils.put(Utils.SP_KEY_USER_EDUCATION, education);
    }

    public static String getUserEducation() {
        return mUtils.getString(Utils.SP_KEY_USER_EDUCATION, "");
    }

    /**
     * saveBirthday
     * @return
     */
    public static void saveBirthday(String birthday) {
        mUtils.put(Utils.SP_KEY_USER_BIRTHDAY, birthday);
    }

    public static String getUserBirthday() {
        return mUtils.getString(Utils.SP_KEY_USER_BIRTHDAY, "");
    }

    /**
     * saveVIPlevel
     * @return
     */
    public static void saveUserVipLevel(String vipLevel) {
        mUtils.put(Utils.SP_KEY_USER_VIP_LEVEL, vipLevel);
    }

    public static String getUserVipLevel() {
        return mUtils.getString(Utils.SP_KEY_USER_VIP_LEVEL, "");
    }

    /**
     *
     * */
    public static void saveTaskCount(String taskCount){
        mUtils.put(Utils.TASK_COUNT,taskCount);
    }

    public static String getTaskCount(){
        return mUtils.getString(Utils.TASK_COUNT, "");
    }




    public static String getUserInfo(){

        return   mUtils.getString(Utils.USERINFO, "");
    }

    public static void savaUserInfo(String userinfo){

        mUtils.put(Utils.USERINFO , userinfo);
    }

    public static String getPhoneNum() {
        return mUtils.getString(Utils.SP_KEY_PHONE_NUM, "");
    }

    public static String getName() {
        return mUtils.getString(Utils.SP_KEY_NICK_NAME, "");
    }


    public static int getFans() {
        return mUtils.getInt(Utils.SP_KEY_FANS, 0);
    }

    public static int getLikes() {
        return mUtils.getInt(Utils.SP_KEY_LIKES, 0);
    }

    public static int getFocus() {
        return mUtils.getInt(Utils.SP_KEY_FOCUS, 0);
    }

    public static void saveIfNeedShow(boolean b){
        mUtils.put(Utils.SP_KEY_USER_NOT_HINT,b);
    }




    /**
     * 是否需要显示  默认显示
     * @return
     */
    public static boolean getIfNeedShow(){
        return mUtils.getBoolean(Utils.SP_KEY_USER_NOT_HINT, true);
    }
}
