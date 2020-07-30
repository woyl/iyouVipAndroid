package com.jfkj.im.TIM.base;

import java.util.List;

public interface IUIKitCallsBack<T> {
    void onSuccess(List<T> data);

    void onError(String module, int errCode, String errMsg);
}
