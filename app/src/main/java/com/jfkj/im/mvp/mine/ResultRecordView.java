package com.jfkj.im.mvp.mine;

import com.jfkj.im.entity.ResultRecordBean;
import com.jfkj.im.mvp.BaseView;

public interface ResultRecordView extends BaseView {

    /**
     * 显示历史记录
     */
    void showMentRecord(ResultRecordBean bean);
}
