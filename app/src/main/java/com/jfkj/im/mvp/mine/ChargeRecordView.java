package com.jfkj.im.mvp.mine;

import com.jfkj.im.entity.ChargeRecordBean;
import com.jfkj.im.mvp.BaseView;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/24
 * </pre>
 */
public interface ChargeRecordView extends BaseView {
    void showRecord(  ChargeRecordBean  bean);
}
