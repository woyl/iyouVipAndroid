package com.jfkj.im.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/9
 * </pre>
 */
public class GiftData extends BaseResponse {
    /**
     * data : {"array":[{"giftId":74947437982384130,"top":18,"price":59,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"棉花糖","state":1},{"giftId":74947437147717630,"top":20,"price":19,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"冰淇淋","state":1},{"giftId":74947439102263300,"top":15,"price":199,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"红酒","state":1},{"giftId":74947439416836100,"top":14,"price":399,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"口红","state":1},{"giftId":74947439894986750,"top":13,"price":399,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"香水","state":1},{"giftId":74947441006477310,"top":11,"price":399,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"比心","state":1},{"giftId":74947441950195710,"top":8,"price":520,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"小可爱","state":1},{"giftId":74947441635622910,"top":9,"price":520,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"仙女棒","state":1},{"giftId":74947442721947650,"top":6,"price":1999,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"生日蛋糕","state":1},{"giftId":74947440364748800,"top":12,"price":399,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"小风车","state":1},{"giftId":74947444181565440,"top":2,"price":9999,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"跑车","state":1},{"giftId":74947438322122750,"top":17,"price":59,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"荧光棒","state":1},{"giftId":74947442407374850,"top":7,"price":999,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"玫瑰花盒","state":1},{"giftId":74947443346898940,"top":4,"price":5999,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"钻戒","state":1},{"giftId":74947437651034110,"top":19,"price":59,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"棒棒糖","state":1},{"giftId":74947438791884800,"top":16,"price":199,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"甜甜圈","state":1},{"giftId":74947441316855800,"top":10,"price":520,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"一见倾心","state":1},{"giftId":74947443032326140,"top":5,"price":3999,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"告白气球","state":1},{"giftId":74947444504526850,"top":1,"price":12999,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"爱心火箭","state":1},{"giftId":74947436019449860,"top":21,"price":19,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"小星星","state":1},{"giftId":74947443841826820,"top":3,"price":9999,"pictureUrl":"http://iyoufile.198ty.com/icon/01.jpg","name":"皇冠","state":1}]}
     */

    private DataBean data;

    public GiftData() {
    }

    protected GiftData(Parcel in) {
        super(in);
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private List<ArrayBean> array;

        public List<ArrayBean> getArray() {
            return array;
        }

        public void setArray(List<ArrayBean> array) {
            this.array = array;
        }

        public static class ArrayBean implements Parcelable {
            /**
             * giftId : 74947437982384130
             * top : 18
             * price : 59
             * pictureUrl : http://iyoufile.198ty.com/icon/01.jpg
             * name : 棉花糖
             * state : 1
             */

            private String giftId;
            private int top;
            private Integer price;
            private String pictureUrl;
            private String name;
            private int state;

            private boolean isSelect;

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public ArrayBean() {
            }

            public ArrayBean(String giftId, int top, Integer price, String pictureUrl, String name, int state) {
                this.giftId = giftId;
                this.top = top;
                this.price = price;
                this.pictureUrl = pictureUrl;
                this.name = name;
                this.state = state;
            }

            public ArrayBean(String giftId, int top, Integer price, String pictureUrl, String name, int state, boolean isSelect) {
                this.giftId = giftId;
                this.top = top;
                this.price = price;
                this.pictureUrl = pictureUrl;
                this.name = name;
                this.state = state;
                this.isSelect = isSelect;
            }

            protected ArrayBean(Parcel in) {
                giftId = in.readString();
                top = in.readInt();
                price = in.readInt();
                pictureUrl = in.readString();
                name = in.readString();
                state = in.readInt();
            }

            public static final Creator<ArrayBean> CREATOR = new Creator<ArrayBean>() {
                @Override
                public ArrayBean createFromParcel(Parcel in) {
                    return new ArrayBean(in);
                }

                @Override
                public ArrayBean[] newArray(int size) {
                    return new ArrayBean[size];
                }
            };

            public String getGiftId() {
                return giftId;
            }

            public void setGiftId(String giftId) {
                this.giftId = giftId;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public Integer getPrice() {
                return price;
            }

            public void setPrice(Integer price) {
                this.price = price;
            }

            public String getPictureUrl() {
                return pictureUrl;
            }

            public void setPictureUrl(String pictureUrl) {
                this.pictureUrl = pictureUrl;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(giftId);
                dest.writeInt(top);
                dest.writeDouble(price);
                dest.writeString(pictureUrl);
                dest.writeString(name);
                dest.writeInt(state);
            }
        }
    }
}
