package com.jfkj.im.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/18
 * </pre>
 */
public class InfoBean extends BaseResponse implements Parcelable {
    private List<String> mStringList;
    protected InfoBean(Parcel in) {
        super(in);
    }
}
