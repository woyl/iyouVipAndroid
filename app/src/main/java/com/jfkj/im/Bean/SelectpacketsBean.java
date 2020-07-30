package com.jfkj.im.Bean;

public class SelectpacketsBean {
    String packets;
    boolean flag;

    public SelectpacketsBean(String packets, boolean flag) {
        this.packets = packets;
        this.flag = flag;
    }

    public String getPackets() {
        return packets;
    }

    public void setPackets(String packets) {
        this.packets = packets;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
