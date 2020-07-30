package com.jfkj.im.Bean;

public class BaseBean<T> {

    /**
     * code : 200
            * message : 操作成功
     * data : {"giftId":"93891594675421184","money":"20","orderId":"121848213484470272","msgId":"2020033020560463dd3194596e4c649dac8000b9926586","tradeOrderNo":"JP10120200330121848213643853824","userId":"119893769985327104","giftSize":1,"content":"我是测试聊天1，很高兴遇见你，交个朋友吧"}
     */

    private String code;
    private String message;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
