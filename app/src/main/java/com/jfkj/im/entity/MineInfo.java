package com.jfkj.im.entity;

import android.os.Parcel;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/16
 * </pre>
 */
public class MineInfo extends BaseResponse {

    /**
     * data :
     * {"jewel":7.0001495E7,"goldCoin":19,"circleArticle":
     * [{"commenCount":3,"Head":"http://iyoufile.198ty.com//1/311104/311104_1575885829224.png",
     * "images":"http://iyoufile.198ty.com/friend/picture/20191225/e7294dbcab41d7f4_s.jpg","commenlist":[{"commentNickName":"789","commentRNickName":"789","articleId":"87012985960267776","ruserId":"81217675350638592","userId":"81217675350638592","addDate":"2019-12-26T20:07:32","content":"ghhh？？？？？？"},{"commentNickName":"789","commentRNickName":"789","articleId":"87012985960267776","ruserId":"81217675350638592","userId":"81217675350638592","addDate":"2019-12-26T19:59:16","content":"？？？"},{"commentNickName":"789","commentRNickName":"789","articleId":"87012985960267776","ruserId":"81217675350638592","userId":"81217675350638592","addDate":"2019-12-26T16:46:54","content":"京津冀"}],"articleId":"87012985960267776","praisecount":"2","NickName":"789","content":"仿佛个","videoPath":"","videoImage":"","isPraise":1,"praiseId":"87361654341369856","location":"四川省成都市武侯区交子大道","videoTime":0,"cuserid":"81217675350638592","adddate":"2019-12-25T17:53:19"},{"commenCount":0,"Head":"http://iyoufile.198ty.com//1/311104/311104_1575885829224.png","images":"http://iyoufile.198ty.com/friend/video/20191225/a6e1bf884cd25df0.mp4","commenlist":[],"articleId":"86887474403606528","praisecount":"2","NickName":"789","content":"hhhhhh","videoPath":"","videoImage":"","isPraise":1,"praiseId":"87366338275835904","location":"广东深圳宝安区","videoTime":"","cuserid":"81217675350638592","adddate":"2019-12-25T09:34:35"},{"commenCount":0,"Head":"http://iyoufile.198ty.com/user/picture/20191225/dd2483b8812a3f3b_s.jpg","images":"","commenlist":[],"articleId":"86661126020005888","praisecount":"2","NickName":"维托-唐-科里昂","content":"测试打铁","videoPath":"","videoImage":"","isPraise":0,"location":"上海市","videoTime":"","cuserid":"76751353593987072","adddate":"2019-12-24T18:35:09"},{"commenCount":0,"Head":"175","images":"72050864205987840","commenlist":[],"articleId":"79765968972283904","praisecount":"0","NickName":"嘎嘎嘎","content":"10000","videoPath":"","videoImage":"","isPraise":0,"location":"10","videoTime":"","cuserid":"78610126684160000","adddate":"2019-12-05T17:57:57"},{"commenCount":0,"Head":"175","images":"72050864205987840","commenlist":[],"articleId":"78612202864640000","praisecount":"2","NickName":"嘎嘎嘎","content":"测试朋友圈3","videoPath":"","videoImage":"","isPraise":0,"location":"深圳","videoTime":"","cuserid":"78610126684160000","adddate":"2019-12-02T13:33:14"},{"commenCount":0,"Head":"175","images":"72050864205987840","commenlist":[],"articleId":"78611874345779200","praisecount":"2","NickName":"嘎嘎嘎","content":"测试朋友圈2","videoPath":"","videoImage":"","isPraise":0,"location":"深圳","videoTime":"","cuserid":"78610126684160000","adddate":"2019-12-02T13:31:56"},{"commenCount":7,"Head":"175","images":"72050864205987840","commenlist":[{"commentNickName":"789","commentRNickName":"测试","articleId":"78611021551501312","ruserId":"78610126684160000","userId":"81217675350638592","addDate":"2019-12-26T20:34:40","content":"哈哈哈哈积极"},{"commentNickName":"789","commentRNickName":"789","articleId":"78611021551501312","ruserId":"81217675350638592","userId":"81217675350638592","addDate":"2019-12-26T20:31:15","content":"哼哼唧唧"},{"commentNickName":"测试","commentRNickName":"测试","articleId":"78611021551501312","ruserId":"78610126684160000","userId":"78610126684160000","addDate":"2019-12-02T16:27:19","content":"测试评论3"},{"commentNickName":"测试","commentRNickName":"测试","articleId":"78611021551501312","ruserId":"78610126684160000","userId":"78610126684160000","addDate":"2019-12-02T16:07:01","content":"测试评论2"},{"commentNickName":"测试","commentRNickName":"嘎嘎嘎","articleId":"78611021551501312","ruserId":"76498878899290110","userId":"78610126684160000","addDate":"2019-12-02T16:06:37","content":"测试评论2"},{"commentNickName":"测试","commentRNickName":"嘎嘎嘎","articleId":"78611021551501312","ruserId":"76498878899290110","userId":"78610126684160000","addDate":"2019-12-02T15:47:30","content":"测试评论"},{"commentNickName":"测试","commentRNickName":"测试号_13700000046","articleId":"78611021551501312","ruserId":"76751252536426496","userId":"78610126684160000","addDate":"2019-12-02T15:45:32","content":"测试评论"}],"articleId":"78611021551501312","praisecount":"2","NickName":"嘎嘎嘎","content":"测试朋友圈","videoPath":"","videoImage":"","isPraise":1,"praiseId":"78646716454273024","location":"深圳","videoTime":"","cuserid":"78610126684160000","adddate":"2019-12-02T13:28:32"}]}
     */

    private DataBean data;

    protected MineInfo(Parcel in) {
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
         * jewel : 7.0001495E7
         * goldCoin : 19.0
         * circleArticle : [{"commenCount":3,"Head":"http://iyoufile.198ty.com//1/311104/311104_1575885829224.png","images":"http://iyoufile.198ty.com/friend/picture/20191225/e7294dbcab41d7f4_s.jpg","commenlist":[{"commentNickName":"789","commentRNickName":"789","articleId":"87012985960267776","ruserId":"81217675350638592","userId":"81217675350638592","addDate":"2019-12-26T20:07:32","content":"ghhh？？？？？？"},{"commentNickName":"789","commentRNickName":"789","articleId":"87012985960267776","ruserId":"81217675350638592","userId":"81217675350638592","addDate":"2019-12-26T19:59:16","content":"？？？"},{"commentNickName":"789","commentRNickName":"789","articleId":"87012985960267776","ruserId":"81217675350638592","userId":"81217675350638592","addDate":"2019-12-26T16:46:54","content":"京津冀"}],"articleId":"87012985960267776","praisecount":"2","NickName":"789","content":"仿佛个","videoPath":"","videoImage":"","isPraise":1,"praiseId":"87361654341369856","location":"四川省成都市武侯区交子大道","videoTime":0,"cuserid":"81217675350638592","adddate":"2019-12-25T17:53:19"},{"commenCount":0,"Head":"http://iyoufile.198ty.com//1/311104/311104_1575885829224.png","images":"http://iyoufile.198ty.com/friend/video/20191225/a6e1bf884cd25df0.mp4","commenlist":[],"articleId":"86887474403606528","praisecount":"2","NickName":"789","content":"hhhhhh","videoPath":"","videoImage":"","isPraise":1,"praiseId":"87366338275835904","location":"广东深圳宝安区","videoTime":"","cuserid":"81217675350638592","adddate":"2019-12-25T09:34:35"},{"commenCount":0,"Head":"http://iyoufile.198ty.com/user/picture/20191225/dd2483b8812a3f3b_s.jpg","images":"","commenlist":[],"articleId":"86661126020005888","praisecount":"2","NickName":"维托-唐-科里昂","content":"测试打铁","videoPath":"","videoImage":"","isPraise":0,"location":"上海市","videoTime":"","cuserid":"76751353593987072","adddate":"2019-12-24T18:35:09"},{"commenCount":0,"Head":"175","images":"72050864205987840","commenlist":[],"articleId":"79765968972283904","praisecount":"0","NickName":"嘎嘎嘎","content":"10000","videoPath":"","videoImage":"","isPraise":0,"location":"10","videoTime":"","cuserid":"78610126684160000","adddate":"2019-12-05T17:57:57"},{"commenCount":0,"Head":"175","images":"72050864205987840","commenlist":[],"articleId":"78612202864640000","praisecount":"2","NickName":"嘎嘎嘎","content":"测试朋友圈3","videoPath":"","videoImage":"","isPraise":0,"location":"深圳","videoTime":"","cuserid":"78610126684160000","adddate":"2019-12-02T13:33:14"},{"commenCount":0,"Head":"175","images":"72050864205987840","commenlist":[],"articleId":"78611874345779200","praisecount":"2","NickName":"嘎嘎嘎","content":"测试朋友圈2","videoPath":"","videoImage":"","isPraise":0,"location":"深圳","videoTime":"","cuserid":"78610126684160000","adddate":"2019-12-02T13:31:56"},{"commenCount":7,"Head":"175","images":"72050864205987840","commenlist":[{"commentNickName":"789","commentRNickName":"测试","articleId":"78611021551501312","ruserId":"78610126684160000","userId":"81217675350638592","addDate":"2019-12-26T20:34:40","content":"哈哈哈哈积极"},{"commentNickName":"789","commentRNickName":"789","articleId":"78611021551501312","ruserId":"81217675350638592","userId":"81217675350638592","addDate":"2019-12-26T20:31:15","content":"哼哼唧唧"},{"commentNickName":"测试","commentRNickName":"测试","articleId":"78611021551501312","ruserId":"78610126684160000","userId":"78610126684160000","addDate":"2019-12-02T16:27:19","content":"测试评论3"},{"commentNickName":"测试","commentRNickName":"测试","articleId":"78611021551501312","ruserId":"78610126684160000","userId":"78610126684160000","addDate":"2019-12-02T16:07:01","content":"测试评论2"},{"commentNickName":"测试","commentRNickName":"嘎嘎嘎","articleId":"78611021551501312","ruserId":"76498878899290110","userId":"78610126684160000","addDate":"2019-12-02T16:06:37","content":"测试评论2"},{"commentNickName":"测试","commentRNickName":"嘎嘎嘎","articleId":"78611021551501312","ruserId":"76498878899290110","userId":"78610126684160000","addDate":"2019-12-02T15:47:30","content":"测试评论"},{"commentNickName":"测试","commentRNickName":"测试号_13700000046","articleId":"78611021551501312","ruserId":"76751252536426496","userId":"78610126684160000","addDate":"2019-12-02T15:45:32","content":"测试评论"}],"articleId":"78611021551501312","praisecount":"2","NickName":"嘎嘎嘎","content":"测试朋友圈","videoPath":"","videoImage":"","isPraise":1,"praiseId":"78646716454273024","location":"深圳","videoTime":"","cuserid":"78610126684160000","adddate":"2019-12-02T13:28:32"}]
         */

        private String jewel;
        private String goldCoin;
        private List<CircleArticleBean> circleArticle;

        public String getJewel() {
            return jewel;
        }

        public void setJewel(String jewel) {
            this.jewel = jewel;
        }

        public String getGoldCoin() {
            return goldCoin;
        }

        public void setGoldCoin(String goldCoin) {
            this.goldCoin = goldCoin;
        }

        public List<CircleArticleBean> getCircleArticle() {
            return circleArticle;
        }

        public void setCircleArticle(List<CircleArticleBean> circleArticle) {
            this.circleArticle = circleArticle;
        }

        public static class CircleArticleBean {
            /**
             * commenCount : 3
             * Head : http://iyoufile.198ty.com//1/311104/311104_1575885829224.png
             * images : http://iyoufile.198ty.com/friend/picture/20191225/e7294dbcab41d7f4_s.jpg
             * commenlist : [{"commentNickName":"789","commentRNickName":"789","articleId":"87012985960267776","ruserId":"81217675350638592","userId":"81217675350638592","addDate":"2019-12-26T20:07:32","content":"ghhh？？？？？？"},{"commentNickName":"789","commentRNickName":"789","articleId":"87012985960267776","ruserId":"81217675350638592","userId":"81217675350638592","addDate":"2019-12-26T19:59:16","content":"？？？"},{"commentNickName":"789","commentRNickName":"789","articleId":"87012985960267776","ruserId":"81217675350638592","userId":"81217675350638592","addDate":"2019-12-26T16:46:54","content":"京津冀"}]
             * articleId : 87012985960267776
             * praisecount : 2                 //点赞数
             * NickName : 789
             * content : 仿佛个
             * videoPath :
             * videoImage :
             private int commenCount;         //评论数
             private String Head;
             private String images;
             private String articleId;
             private String praisecount;
             private String NickName;
             private String content;
             private String videoPath;
             private String videoImage;
             private int isPraise;
             private String isMore;
             private String location;
             private int videoTime;
             private String cuserid;
             private String adddate;
             private String praiseId;
             private List<CommenlistBean> commenlist;
             * isPraise : 1
             * praiseId : 87361654341369856
             * location : 四川省成都市武侯区交子大道
             * videoTime : 0
             * cuserid : 81217675350638592
             * adddate : 2019-12-25T17:53:19
             */

            private int commenCount;
            private String Head;
            private String images="";
            private String articleId;
            private String praisecount;
            private String NickName;
            private String content;
            private String videoPath;
            private String videoImage;
            private String isMore;
            private String isPraise;  //是否点赞  1 是  0否
            private String praiseId;
            private String location;
            private String videoTime;
            private String cuserid;
            private String adddate;
            private List<CommentBean> commenlist;

            private VideoImagesBean videoImages;
            private List<PicImagesBean> picImages;

            private String commenDiamonds;

            public String getCommenDiamonds() {
                return commenDiamonds;
            }

            public void setCommenDiamonds(String commenDiamonds) {
                this.commenDiamonds = commenDiamonds;
            }

            public VideoImagesBean getVideoImages() {
                return videoImages;
            }

            public void setVideoImages(VideoImagesBean videoImages) {
                this.videoImages = videoImages;
            }

            public List<PicImagesBean> getPicImages() {
                return picImages;
            }

            public void setPicImages(List<PicImagesBean> picImages) {
                this.picImages = picImages;
            }

            public String getIsMore() {
                return isMore;
            }

            public void setIsMore(String isMore) {
                this.isMore = isMore;
            }

            public int getCommenCount() {
                return commenCount;
            }

            public void setCommenCount(int commenCount) {
                this.commenCount = commenCount;
            }

            public String getHead() {
                return Head;
            }

            public void setHead(String Head) {
                this.Head = Head;
            }

            public String getImages() {
                return images;
            }

            public void setImages(String images) {
                this.images = images;
            }

            public String getArticleId() {
                return articleId;
            }

            public void setArticleId(String articleId) {
                this.articleId = articleId;
            }

            public String getPraisecount() {
                return praisecount;
            }

            public void setPraisecount(String praisecount) {
                this.praisecount = praisecount;
            }

            public String getNickName() {
                return NickName;
            }

            public void setNickName(String NickName) {
                this.NickName = NickName;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getVideoPath() {
                return videoPath;
            }

            public void setVideoPath(String videoPath) {
                this.videoPath = videoPath;
            }

            public String getVideoImage() {
                return videoImage;
            }

            public void setVideoImage(String videoImage) {
                this.videoImage = videoImage;
            }

            public String getIsPraise() {
                return isPraise;
            }

            public void setIsPraise(String isPraise) {
                this.isPraise = isPraise;
            }

            public String getPraiseId() {
                return praiseId;
            }

            public void setPraiseId(String praiseId) {
                this.praiseId = praiseId;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getVideoTime() {
                return videoTime;
            }

            public void setVideoTime(String videoTime) {
                this.videoTime = videoTime;
            }

            public String getCuserid() {
                return cuserid;
            }

            public void setCuserid(String cuserid) {
                this.cuserid = cuserid;
            }

            public String getAdddate() {
                return adddate;
            }

            public void setAdddate(String adddate) {
                this.adddate = adddate;
            }

            public List<CommentBean> getCommenlist() {
                return commenlist;
            }

            public void setCommenlist(List<CommentBean> commenlist) {
                this.commenlist = commenlist;
            }

        }

        public static class VideoImagesBean {
            /**
             * width : 0
             * url :
             * height : 0
             */

            private int width;
            private String url;
            private int height;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }

        public static class PicImagesBean {
            /**
             * width : 1512
             * url : http://iyfile.tiantiancaidian.com//verify/picture/20200612/5e91b3edbf7aa80e_l.jpg
             * height : 2016
             */

            private int width;
            private String url;
            private int height;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }
    }
}
