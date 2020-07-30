package com.jfkj.im.Bean;

import java.util.List;

public class loadOfflineMessageBean {



    private String code;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {


        private int totalCount;
        private List<MsgArrayBean> msgArray;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<MsgArrayBean> getMsgArray() {
            return msgArray;
        }

        public void setMsgArray(List<MsgArrayBean> msgArray) {
            this.msgArray = msgArray;
        }

        public static class MsgArrayBean {
            /**
             * mode : 7
             * sendId : 76751335294238720
             * sendGroup : {"groupName":".222222","groupId":"89418003229310976"}
             * groupId : 89418003229310976
             * needAck : 1
             * sendType : 1
             * sendUser : {"head":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","uid":"76751335294238720","vipLevel":3,"gender":1,"nickName":"测试号_13700000093"}
             * msgId : 20200104135825b904781f3b8d44b08e782af35d8655aa
             * receiveId : 76743604952891392
             * sendTime : 1578117505539
             * body : {"contentType":1,"content":["用户ggege成功加入了俱乐部"]}
             * needPush : 1
             * toMsgId : 202001041758323a9d638a148c4b1d849baa38bef4851d
             */

            private int mode;
            private String sendId;
            private SendGroupBean sendGroup;
            private String groupId;
            private int needAck;
            private int sendType;
            private SendUserBean sendUser;
            private String msgId;
            private String receiveId;
            private String sendTime;
            private BodyBean body;
            private int needPush;
            private String toMsgId;

            public int getMode() {
                return mode;
            }

            public void setMode(int mode) {
                this.mode = mode;
            }

            public String getSendId() {
                return sendId;
            }

            public void setSendId(String sendId) {
                this.sendId = sendId;
            }

            public SendGroupBean getSendGroup() {
                return sendGroup;
            }

            public void setSendGroup(SendGroupBean sendGroup) {
                this.sendGroup = sendGroup;
            }

            public String getGroupId() {
                return groupId;
            }

            public void setGroupId(String groupId) {
                this.groupId = groupId;
            }

            public int getNeedAck() {
                return needAck;
            }

            public void setNeedAck(int needAck) {
                this.needAck = needAck;
            }

            public int getSendType() {
                return sendType;
            }

            public void setSendType(int sendType) {
                this.sendType = sendType;
            }

            public SendUserBean getSendUser() {
                return sendUser;
            }

            public void setSendUser(SendUserBean sendUser) {
                this.sendUser = sendUser;
            }

            public String getMsgId() {
                return msgId;
            }

            public void setMsgId(String msgId) {
                this.msgId = msgId;
            }

            public String getReceiveId() {
                return receiveId;
            }

            public void setReceiveId(String receiveId) {
                this.receiveId = receiveId;
            }

            public String getSendTime() {
                return sendTime;
            }

            public void setSendTime(String sendTime) {
                this.sendTime = sendTime;
            }

            public BodyBean getBody() {
                return body;
            }

            public void setBody(BodyBean body) {
                this.body = body;
            }

            public int getNeedPush() {
                return needPush;
            }

            public void setNeedPush(int needPush) {
                this.needPush = needPush;
            }

            public String getToMsgId() {
                return toMsgId;
            }

            public void setToMsgId(String toMsgId) {
                this.toMsgId = toMsgId;
            }

            public static class SendGroupBean {
                /**
                 * groupName : .222222
                 * groupId : 89418003229310976
                 */
                 private String groupHead;
                private String groupName;
                private String groupId;

                public String getGroupHead() {
                    return groupHead;
                }

                public void setGroupHead(String groupHead) {
                    this.groupHead = groupHead;
                }

                public String getGroupName() {
                    return groupName;
                }

                public void setGroupName(String groupName) {
                    this.groupName = groupName;
                }

                public String getGroupId() {
                    return groupId;
                }

                public void setGroupId(String groupId) {
                    this.groupId = groupId;
                }
            }

            public static class SendUserBean {
                /**
                 * head : http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png
                 * uid : 76751335294238720
                 * vipLevel : 3
                 * gender : 1
                 * nickName : 测试号_13700000093
                 */

                private String head;
                private String uid;
                private int vipLevel;
                private int gender;
                private String nickName;

                public String getHead() {
                    return head;
                }

                public void setHead(String head) {
                    this.head = head;
                }

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public int getVipLevel() {
                    return vipLevel;
                }

                public void setVipLevel(int vipLevel) {
                    this.vipLevel = vipLevel;
                }

                public int getGender() {
                    return gender;
                }

                public void setGender(int gender) {
                    this.gender = gender;
                }

                public String getNickName() {
                    return nickName;
                }

                public void setNickName(String nickName) {
                    this.nickName = nickName;
                }
            }

            public static class BodyBean {
                /**
                 * contentType : 1
                 * content : ["用户ggege成功加入了俱乐部"]
                 */

                private int contentType;
                private List<String> content;
                private  RedPacketBean redPacket;
                private MediaContentBean mediaContent;
                public int getContentType() {
                    return contentType;
                }
                public MediaContentBean getMediaContent() {
                    return mediaContent;
                }

                public void setMediaContent(MediaContentBean mediaContent) {
                    this.mediaContent = mediaContent;
                }
                public RedPacketBean getRedPacket() {
                    return redPacket;
                }

                public void setRedPacket(RedPacketBean redPacket) {
                    this.redPacket = redPacket;
                }
                private GiftBean gift;

                public GiftBean getGift() {
                    return gift;
                }

                public void setGift(GiftBean gift) {
                    this.gift = gift;
                }

                public void setContentType(int contentType) {
                    this.contentType = contentType;
                }

                public List<String> getContent() {
                    return content;
                }

                public void setContent(List<String> content) {
                    this.content = content;
                }
                public static class MediaContentBean {
                    /**
                     * mediaTime : 1
                     * mediaPath : http://iyoufile.198ty.com/chat/voice/20200229/06208ca6c795e4a3.mp3
                     * fileType : 1
                     */

                    private String mediaTime;
                    private String mediaPath;
                    private int fileType;

                    public String getMediaTime() {
                        return mediaTime;
                    }

                    public void setMediaTime(String mediaTime) {
                        this.mediaTime = mediaTime;
                    }

                    public String getMediaPath() {
                        return mediaPath;
                    }

                    public void setMediaPath(String mediaPath) {
                        this.mediaPath = mediaPath;
                    }

                    public int getFileType() {
                        return fileType;
                    }

                    public void setFileType(int fileType) {
                        this.fileType = fileType;
                    }
                }
                public static class RedPacketBean {
                    /**
                     * redType : 1
                     * redId : 94256759153229824
                     * sendWord : 哭T﹏T
                     */

                    private int redType;
                    private String redId;
                    private String sendWord;

                    public int getRedType() {
                        return redType;
                    }

                    public void setRedType(int redType) {
                        this.redType = redType;
                    }

                    public String getRedId() {
                        return redId;
                    }

                    public void setRedId(String redId) {
                        this.redId = redId;
                    }

                    public String getSendWord() {
                        return sendWord;
                    }

                    public void setSendWord(String sendWord) {
                        this.sendWord = sendWord;
                    }
                }

                public static class GiftBean {
                    private String giftId;
                    private String money;
                    private String orderId;
                    private String tradeOrderNo;
                    private int giftSize;

                    public String getGiftId() {
                        return giftId;
                    }

                    public void setGiftId(String giftId) {
                        this.giftId = giftId;
                    }

                    public String getMoney() {
                        return money;
                    }

                    public void setMoney(String money) {
                        this.money = money;
                    }

                    public String getOrderId() {
                        return orderId;
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

                    public int getGiftSize() {
                        return giftSize;
                    }

                    public void setGiftSize(int giftSize) {
                        this.giftSize = giftSize;
                    }
                }
            }

        }
    }
}
