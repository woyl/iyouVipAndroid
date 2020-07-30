package com.jfkj.im.model;

import android.util.Log;

import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.Bean.PraiseIdBean;
import com.jfkj.im.entity.AccountBalance;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.ChargeRecordBean;
import com.jfkj.im.entity.MineInfo;
import com.jfkj.im.entity.MyGiftBean;
import com.jfkj.im.entity.ResponseFileUrl;
import com.jfkj.im.entity.Setup;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.retrofit.ApiUploadClient;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.videocompression.SiliCompressor;
import com.lzy.okgo.utils.OkLogger;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/6
 * </pre>
 */
public class MineModel {
    public Observable<Setup> setup(Map<String,String> maps){
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .setUp(maps);
    }


    public Observable<BaseResponse> updateMyPassword(Map<String,String> map) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .updateMyPassword(map);
    }

    public Observable<BaseResponse> editUserInfo(Map<String,String> map){
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .editorInfo(map);
    }



    public Observable<ResponseFileUrl> uploadSingleFile(Map<String,String> map, MultipartBody.Part file){
            return ApiUploadClient.getUpRetrofit().create(ApiStores.class)
                    .upload(map, file) ;

    }






    public Observable<MyGiftBean> myGift(Map<String,String> map){
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .myGift(map);
    }


    public Observable<BaseResponse> uploadVideo(String videoUrl){
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .uploadVideo(videoUrl);
    }

    public Observable<BaseResponse> deleteDynamic(String articleId){
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .deleteDynamic(articleId);

    }

    public Observable<BaseResponse> updatePasswordVerification(Map<String,String> map){
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .updatePasswordVerification(map);
    }

    public Observable<BaseResponse> updateMobileVerification(Map mobile){
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .updateMobileVerification(mobile);
    }

    public Observable<BaseResponse> getPhoneCode(Map<String,String> map){
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .getPhoneCode(map);
    }

    public Observable<ResponseBody> getStaticCode(Map<String,String> map){
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .getStaticCode(map);
    }

    public Observable<BaseResponse> updateMobile(Map<String,String> map) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .updateMobile(map);
    }

//    public Observable<ResponseBody> exchangeList(Map<String,String> map){
//        return ApiClient.getmRetrofit().create(ApiStores.class)
//                .exchangeList(map);
//    }

    public Observable<BaseResponse> exchangeDiamond(Map<String,String> map){
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .exchangeDiamond(map);
    }

    public Observable<PraiseIdBean> getUserPraise(String articleId, String type, String praiseId) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .getUserPraise(articleId, type, praiseId)
                .throttleFirst(2, TimeUnit.SECONDS);
    }

    public Observable<BaseResponse> setComment(String userId, String articleId, String content, String type, String commentId) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .setComment(userId, articleId, content, type, commentId);
    }
}
