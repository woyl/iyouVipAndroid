package com.jfkj.im.Bean;

public class TipsStartActivity {

    public TipsStartActivity(int type, String id) {
        this.type = type;
        this.id = id;
    }

    /**
     *  跳转类型
     */
    private  int type ;


    /**
     *  用户ID，俱乐部ID。。。。。
     */
    private String id;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
