package com.jfkj.im.model;

import com.jfkj.im.Bean.MyDynamicMessage;
import com.jfkj.im.Bean.PraiseIdBean;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.entity.RankListBean;
import com.jfkj.im.entity.ResponseFileUrl;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiFileClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.retrofit.ApiUploadClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/10
 * </pre>
 */
public class DiscoveryModel {

    public Observable<BaseResponse> publishDynamic(Map<String, String> map) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .publishDynamic(map);
    }

    public Observable<CircleBean> getFindDynamic(String pageNo, String pageSize) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .getFindDynamic(pageNo, pageSize);
    }

    public Observable<ResponseFileUrl> uploadSingleFile(String fileType, String pathType, MultipartBody.Part file) {
        return ApiUploadClient.getmRetrofit().create(ApiStores.class)
                .uploadSingleFile(fileType, pathType, file)
                .throttleFirst(2, TimeUnit.SECONDS);
    }

    public Observable<ResponseFileUrl> uploadFiles(Map<String, String> map, List<MultipartBody.Part> partList) {
        return ApiUploadClient.getmRetrofit().create(ApiStores.class)
                .uploadFiles(map, partList)
                .throttleFirst(2, TimeUnit.SECONDS);
    }

    public Observable<List<RankListBean>> getRankList(String type) {
        return ApiFileClient.getmRetrofit().create(ApiStores.class)
                .rankList(type);
    }

    public Observable<PraiseIdBean> getUserPraise(String articleId, String type, String praiseId) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .getUserPraise(articleId, type, praiseId)
                .throttleFirst(2, TimeUnit.SECONDS);
    }

    public Observable<MyDynamicMessage> getDynamicMessage(String pageNo, String pageSize) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .getFindDynamicMessage(pageNo, pageSize);
    }

    public Observable<BaseResponse> setComment(String userId, String articleId, String content, String type, String commentId) {
        return ApiClient.getmRetrofit().create(ApiStores.class)
                .setComment(userId, articleId, content, type, commentId);
    }

    public Observable<BaseResponse> setCommentHashMap(String userId, String articleId, String content, String type, String commentId) {
        Map<String, String> map = new HashMap<>();
        if (userId.trim().length() > 0) {
            map.put("userId", userId);
        }
        map.put("articleId", articleId);
        map.put("content", content);
        map.put("type", type);
        if (commentId.trim().length() > 0) {
            map.put("commentId", commentId);
        }

        return ApiClient.getmRetrofit().create(ApiStores.class)
                .setCommentHashMap(map);
    }
}
