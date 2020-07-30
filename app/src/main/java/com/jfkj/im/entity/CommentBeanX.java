package com.jfkj.im.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.jfkj.im.Bean.ChildCommentList;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/18
 * </pre>
 */
public class CommentBeanX   {

    private String code;

    private  String message;

    private List<CommentBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CommentBean> getData() {
        return data;
    }

    public void setData(List<CommentBean> data) {
        this.data = data;
    }
}
