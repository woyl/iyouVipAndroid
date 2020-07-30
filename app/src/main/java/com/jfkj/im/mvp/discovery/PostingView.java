package com.jfkj.im.mvp.discovery;

import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.mvp.BaseView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/10
 * </pre>
 */
public interface PostingView extends BaseView {

    void publishSuccess(BaseResponse o);
}
