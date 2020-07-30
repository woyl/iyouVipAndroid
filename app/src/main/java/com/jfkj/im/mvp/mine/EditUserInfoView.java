package com.jfkj.im.mvp.mine;

import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.mvp.BaseView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/13
 * </pre>
 */
public interface EditUserInfoView extends BaseView {
    void showEditSuccess(BaseResponse baseResponse, String type);
}
