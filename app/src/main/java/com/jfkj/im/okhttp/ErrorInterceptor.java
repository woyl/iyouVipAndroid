package com.jfkj.im.okhttp;

import com.jfkj.im.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/23
 * </pre>
 */
public class ErrorInterceptor extends ResponseBodyInterceptor {
    private JSONObject jsonObject;

    @Override
    Response intercept(@NotNull Response response, String url, String body) throws IOException, JSONException {
        try {
            jsonObject = new JSONObject(body);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            String message = jsonObject.getString("message");
            if (Utils.STATUS_LOGIN_OUT_OF_DATA.equals(jsonObject.optString("code"))) {
                throw new LoginException(message);
            } else if (Utils.STATUS_SYSTEM_ERROR.equals(jsonObject.getString("code"))) {
                throw new OtherException(message);
            }
        }
        return response;
    }
    //    @Override
    //    Response intercept(@NotNull Response response, String url, String body) throws IOException {
    //        JSONObject jsonObject = (JSONObject)null;
    //
    //        try {
    //            jsonObject = new JSONObject(body);
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //
    //        if (jsonObject != null && jsonObject.optInt("code", -1) != 200 && jsonObject.has("msg")) {
    //
    //            return response;
    //        }else if (jsonObject != null && jsonObject.optInt("code", -1) != 10086){
    //            throw new MultiDeviceException(response.message());
    //
    //        }
    //            return response;
    //    }
}
