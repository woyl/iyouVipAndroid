package com.jfkj.im.model;

import com.jfkj.im.ArticleDynamicBean;
import com.jfkj.im.Bean.PraiseIdBean;
import com.jfkj.im.entity.AddFriend;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.entity.HomeRecommend;
import com.jfkj.im.entity.ResponseFileUrl;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.retrofit.ApiUploadClient;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.Utils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/25
 * </pre>
 */
public class HomeModel {



    public Observable<BaseResponse> toUserReport(Map<String,String> maps) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .toUserReport(maps);
    }



    public Observable<AddFriend> applyAddFriend(String osName, String appVersion, String channel, String userId, String giftId, String size, String content) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .applyAddFriend(osName, appVersion, channel, userId, giftId, size, content);
    }

    public Observable<HomeRecommend> homeRecommend( String pageNo, String pageSize, String gender) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .homeRecommend( pageNo, pageSize, gender);
    }

    public Observable<BaseResponse> blackList(String userId, String type) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .toUserBlack(userId, type, Utils.ANDROID,Utils.ANDROID,Utils.getVersionCode()+"",Utils.ANDROID,Utils.ANDROID, AppUtils.getReqTime());
    }

    public Observable<ResponseFileUrl> uploadSingleFile(String fileType, String pathType, MultipartBody.Part file) {
        return ApiUploadClient.getmRetrofit().create(ApiStores.class)
                .uploadSingleFile(fileType, pathType, file);
    }
//      //  HomeRecommend
//    public Observable<ResponseBody> homePeople(String pageNo, String pageSize, String gender) {
//        return ApiClient.getmRetrofit().create(ApiStores.class)
//                .homeNewPeople(pageNo, pageSize, gender);
//    }

    public Observable<CircleBean> getUserCircleArticle(String longitude, String latitude, String userId) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .getUserCircleArticle(longitude, latitude, userId);
    }

    public Observable<BaseResponse> setComment(String userId, String articleId, String content, String type, String commentId) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .setComment(userId, articleId, content, type, commentId);
    }

    public Observable<PraiseIdBean> getUserPraise(Map<String,String> map) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .getUserPraise(map)
                .throttleFirst(2000, TimeUnit.SECONDS);
    }

    public Observable<ArticleDynamicBean> getArticleDynamic(String articleId, String ruserid){
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .getArticleDynamic(articleId,ruserid);
    }
}
