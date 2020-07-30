package com.jfkj.im.Bean.chat;


public class MsgBody implements java.io.Serializable {

    private MsgType localMsgType;
    private String receive_head_url;
    private String cgameid = "";
    private boolean istestfinsh = false;
    private String charactertime;
    private String gameid;
    public String getGameid() {
        return gameid;
    }
    public void setGameid(String gameid) {
        this.gameid = gameid;
    }
    public String getCharactertime() {
        return charactertime;
    }
      private String chatid;

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public void setCharactertime(String charactertime) {
        this.charactertime = charactertime;
    }

    public boolean isIstestfinsh() {
        return istestfinsh;
    }

    public void setIstestfinsh(boolean istestfinsh) {
        this.istestfinsh = istestfinsh;
    }

    public String getCgameid() {
        return cgameid;
    }

    public void setCgameid(String cgameid) {
        this.cgameid = cgameid;
    }

    public String getReceive_head_url() {
        return receive_head_url;
    }

    public void setReceive_head_url(String receive_head_url) {
        this.receive_head_url = receive_head_url;
    }

    private boolean isRedReceivePacket = false;
    int count = 0;
    public boolean isAudioplay = false;

    public boolean isAudioplay() {
        return isAudioplay;
    }

    public void setAudioplay(boolean audioplay) {
        isAudioplay = audioplay;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private long sentTime;

    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public MsgType getLocalMsgType() {
        return localMsgType;
    }

    public void setLocalMsgType(MsgType localMsgType) {
        this.localMsgType = localMsgType;
    }

    public boolean isRedReceivePacket() {
        return isRedReceivePacket;
    }

    public void setRedReceivePacket(boolean redReceivePacket) {
        isRedReceivePacket = redReceivePacket;
    }

    private String message = "";


    public String getMessage() {
        return message;
    }

    private String redId = "";
    private boolean isreceive;

    public String getRedId() {
        return redId;
    }

    public void setRedId(String redId) {
        this.redId = redId;
    }

    public boolean isIsreceive() {
        return isreceive;
    }

    public void setIsreceive(boolean isreceive) {
        this.isreceive = isreceive;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    private long duration;
    //高度
    private int height;
    //宽度
    private int width;


    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


    //缩略图文件的本地路径
    private String thumbPath;
    //缩略图远程地址
    private String thumbUrl;
    //是否压缩(false:原图，true：压缩过)
    private boolean compress;


    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public boolean isCompress() {
        return compress;
    }

    public void setCompress(boolean compress) {
        this.compress = compress;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }


    private String orderId = "";
    private String tradeOrderNo = "";
    private String giftid = "";

    public String getOrderId() {
        return orderId;
    }

    public String getGiftid() {
        return giftid;
    }

    public void setGiftid(String giftid) {
        this.giftid = giftid;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeOrderNo() {
        return tradeOrderNo;
    }

    public void setTradeOrderNo(String tradeOrderNo) {
        this.tradeOrderNo = tradeOrderNo;
    }

    //文件后缀名
    private String ext;
    //文件名称,可以和文件名不同，仅用于界面展示
    private String displayName;
    //文件长度(字节)
    private long size;
    //本地文件保存路径
    private String localPath;
    //文件下载地址
    private String remoteUrl;
    //文件内容的MD5
    private String md5;
    //扩展信息，可以放置任意的数据内容，也可以去掉此属性
    private String extra;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }


}
