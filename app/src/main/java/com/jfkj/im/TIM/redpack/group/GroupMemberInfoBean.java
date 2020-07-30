package com.jfkj.im.TIM.redpack.group;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupMemberInfoBean implements Parcelable {

    /**
     * {
     * "code": "200",... <string>
     * "message": "操作成功",... <string>
     * -"data": {...<object>
     * -"array": [...<array>
     * -{
     * "head": "http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png",头像 <string>
     * "vipLevel": "3",会员等级 <string>
     * "isOwner": 0,是否群主 <number>
     * "nickName": "测试号_13700000008",昵称 <string>
     * "groupId": "76747298909978624",俱乐部ID <string>
     * "sort": "1574832398325",分页字段 <string>
     * "userId": "76749484243025920",用户ID <string>
     * "memberId": "76799012220698624"群成员记录ID <string>
     * }
     * ]
     * }
     * }
     *
     * */
    /**
     * head : http://iyfile.tiantiancaidian.com/static/VipHead.png
     * vipLevel : 1
     * isOwner : 1
     * nickName : 我的
     * groupId : 114616609468973056
     * sort : 1583848816154
     * userId : 113051629334429696
     * memberId : 114616609586413568
     */

    private String head;
    private String vipLevel;
    private int isOwner;
    private String nickName;
    private String groupId;
    private String sort;
    private String userId;
    private String memberId;
    private int type = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GroupMemberInfoBean() {
    }

    protected GroupMemberInfoBean(Parcel in) {
        head = in.readString();
        vipLevel = in.readString();
        isOwner = in.readInt();
        nickName = in.readString();
        groupId = in.readString();
        sort = in.readString();
        userId = in.readString();
        memberId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(head);
        dest.writeString(vipLevel);
        dest.writeInt(isOwner);
        dest.writeString(nickName);
        dest.writeString(groupId);
        dest.writeString(sort);
        dest.writeString(userId);
        dest.writeString(memberId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupMemberInfoBean> CREATOR = new Creator<GroupMemberInfoBean>() {
        @Override
        public GroupMemberInfoBean createFromParcel(Parcel in) {
            return new GroupMemberInfoBean(in);
        }

        @Override
        public GroupMemberInfoBean[] newArray(int size) {
            return new GroupMemberInfoBean[size];
        }
    };

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
