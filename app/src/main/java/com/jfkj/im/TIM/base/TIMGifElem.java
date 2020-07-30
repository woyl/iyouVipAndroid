package com.jfkj.im.TIM.base;

import com.tencent.imsdk.TIMElem;

import com.tencent.imsdk.TIMElemType;


public class TIMGifElem extends TIMElem {

    String gifid;
    String gitImaurl;
    String hint;
    String Orderid;

    byte[] data;

    public TIMGifElem() {
        type = TIMElemType.File;
    }
    public byte[] getData() {
        return data == null ? "".getBytes() : data;
    }


    public void setData(byte[] data) {
        this.data = data;
    }


    public String getGifid() {
        return gifid;
    }

    public void setGifid(String gifid) {
        this.gifid = gifid;
    }

    public String getGitImaurl() {
        return gitImaurl;
    }

    public void setGitImaurl(String gitImaurl) {
        this.gitImaurl = gitImaurl;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getOrderid() {
        return Orderid;
    }

    public void setOrderid(String orderid) {
        Orderid = orderid;
    }
}
