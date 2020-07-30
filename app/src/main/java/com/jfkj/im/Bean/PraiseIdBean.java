package com.jfkj.im.Bean;

import android.os.Parcel;

import com.jfkj.im.entity.BaseResponse;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/13
 * </pre>
 */
public class PraiseIdBean extends BaseResponse {

    /**
     * data : {"praiseid":"117836971203756032"}
     */

    private DataBean data;

    protected PraiseIdBean(Parcel in) {
        super(in);
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * praiseid : 117836971203756032
         */

        private String praiseid;

        public String getPraiseid() {
            return praiseid;
        }

        public void setPraiseid(String praiseid) {
            this.praiseid = praiseid;
        }
    }
}
