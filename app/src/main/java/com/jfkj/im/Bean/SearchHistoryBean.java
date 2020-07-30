package com.jfkj.im.Bean;

import java.util.List;

public class SearchHistoryBean {


    private List<DataBean> dataBean;

    public List<DataBean> getDataBean() {
        return dataBean;
    }

    public void setDataBean(List<DataBean> dataBean) {
        this.dataBean = dataBean;
    }

    public static class DataBean {

        public DataBean(String address) {
            this.address = address;
        }

        private  String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
