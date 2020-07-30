package com.jfkj.im.Bean;

import com.jfkj.im.entity.CommentBean;

import java.util.List;

public class Test {


    /**
     * code : 200
     * message : 查询成功
     * data : [{"commenCount":0,"Head":"http://file.vipiyou.com/static/VipHead.png","images":"http://file.vipiyou.com/friend/picture/20200507/89cb47d73720076e_l.jpg","commenlist":[],"articleId":"135582495449153536","praisecount":"0","NickName":"13677777777","content":"343243","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"北京市","videoTime":0,"cuserid":"132995616497336320","adddate":"35分钟前"},{"commenCount":3,"Head":"http://file.vipiyou.com/static/VipHead.png","images":"http://file.vipiyou.com/friend/picture/20200507/1c23458712452cc6_l.jpg,http://file.vipiyou.com/friend/picture/20200507/1199e40d28984e9f_l.jpg","commenlist":[{"commentNickName":"女性01","commentRNickName":"","articleId":"135581963099701248","ruserId":"0","userId":"132256176019734528","addDate":"2020-05-07 18:29:22","content":"vv尺寸"},{"commentNickName":"13677777777","commentRNickName":"女性01","articleId":"135581963099701248","ruserId":"132256176019734528","userId":"132995616497336320","addDate":"2020-05-07 18:29:28","content":"地方规范的广泛地"},{"commentNickName":"女性01","commentRNickName":"13677777777","articleId":"135581963099701248","ruserId":"132995616497336320","userId":"132256176019734528","addDate":"2020-05-07 18:31:30","content":"改成"}],"articleId":"135581963099701248","praisecount":"0","NickName":"13677777777","content":"5任3543543","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"北京市","videoTime":0,"cuserid":"132995616497336320","adddate":"38分钟前"},{"commenCount":0,"Head":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/dcffa2467071bbb9_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200507/2d64ba2636e22f1f_l.jpg","commenlist":[],"articleId":"135580778145906688","praisecount":"0","NickName":"女性01","content":"接电话还打不打","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"深圳市","videoTime":0,"cuserid":"132256176019734528","adddate":"42分钟前"},{"commenCount":0,"Head":"http://file.vipiyou.com/static/VipHead.png","images":"http://file.vipiyou.com/friend/picture/20200507/f0de36e14ff6db22_l.jpg","commenlist":[],"articleId":"135567008501727232","praisecount":"1","NickName":"家里凤弟","content":"不不不","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"深圳市","videoTime":0,"cuserid":"132295376429514752","adddate":"1小时前"},{"commenCount":0,"Head":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/435fa63b052a67f6_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200507/2399e7bffe3010b7_l.jpg,http://file.vipiyou.com/friend/picture/20200507/2a3f3a53f2c5088a_l.jpg","commenlist":[],"articleId":"135565256230567936","praisecount":"2","NickName":"刘昊然","content":"共同","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"深圳市","videoTime":0,"cuserid":"132311822626848768","adddate":"1小时前"},{"commenCount":0,"Head":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/4b17267f00c07893_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200507/572d751e44833964_l.jpg","commenlist":[],"articleId":"135563435537727488","praisecount":"1","NickName":"社会凤姐","content":"测试","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"成都市","videoTime":0,"cuserid":"132263954654298112","adddate":"1小时前"},{"commenCount":2,"Head":"http://file.vipiyou.com/static/VipHead.png","images":"http://file.vipiyou.com/friend/picture/20200507/faf43e23cb1cbe66_l.jpg","commenlist":[{"commentNickName":"135800000000","commentRNickName":"","articleId":"135561912254595072","ruserId":"0","userId":"132919026581110784","addDate":"2020-05-07 17:13:48","content":"解开"},{"commentNickName":"135800000000","commentRNickName":"","articleId":"135561912254595072","ruserId":"0","userId":"132919026581110784","addDate":"2020-05-07 17:13:56","content":"空空荡荡看开点"}],"articleId":"135561912254595072","praisecount":"0","NickName":"13677777777","content":"4554","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"北京市","videoTime":0,"cuserid":"132995616497336320","adddate":"1小时前"},{"commenCount":3,"Head":"http://iyfile.tiantiancaidian.com//verify/picture/20200430/c1c31e97dce6fb6a_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200507/9ef27c0597f2995d_l.jpg","commenlist":[{"commentNickName":"13677777777","commentRNickName":"","articleId":"135559078620561408","ruserId":"0","userId":"132995616497336320","addDate":"2020-05-07 16:58:22","content":"地方都是"},{"commentNickName":"135800000000","commentRNickName":"13677777777","articleId":"135559078620561408","ruserId":"132995616497336320","userId":"132919026581110784","addDate":"2020-05-07 16:58:28","content":"滚滚滚"},{"commentNickName":"135800000000","commentRNickName":"13677777777","articleId":"135559078620561408","ruserId":"132995616497336320","userId":"132919026581110784","addDate":"2020-05-07 16:58:34","content":"滚滚滚2"}],"articleId":"135559078620561408","praisecount":"0","NickName":"135800000000","content":"嘘嘘嘘u下","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"深圳市","videoTime":0,"cuserid":"132919026581110784","adddate":"2小时前"},{"commenCount":7,"Head":"http://file.vipiyou.com/static/VipHead.png","images":"http://file.vipiyou.com/friend/picture/20200507/4d08ab4b1d215dca_l.jpg","commenlist":[{"commentNickName":"135800000000","commentRNickName":"","articleId":"135557699227222016","ruserId":"0","userId":"132919026581110784","addDate":"2020-05-07 16:52:47","content":"FFF团"},{"commentNickName":"13677777777","commentRNickName":"135800000000","articleId":"135557699227222016","ruserId":"132919026581110784","userId":"132995616497336320","addDate":"2020-05-07 16:52:53","content":"幅度萨芬"},{"commentNickName":"13677777777","commentRNickName":"135800000000","articleId":"135557699227222016","ruserId":"132919026581110784","userId":"132995616497336320","addDate":"2020-05-07 16:52:59","content":"范德萨犯得是"},{"commentNickName":"13677777777","commentRNickName":"135800000000","articleId":"135557699227222016","ruserId":"132919026581110784","userId":"132995616497336320","addDate":"2020-05-07 16:53:03","content":"士大夫但是都是"},{"commentNickName":"13677777777","commentRNickName":"135800000000","articleId":"135557699227222016","ruserId":"132919026581110784","userId":"132995616497336320","addDate":"2020-05-07 16:53:09","content":"大师傅似的都是"},{"commentNickName":"13677777777","commentRNickName":"135800000000","articleId":"135557699227222016","ruserId":"132919026581110784","userId":"132995616497336320","addDate":"2020-05-07 16:53:12","content":"倒十分都是"},{"commentNickName":"135800000000","commentRNickName":"","articleId":"135557699227222016","ruserId":"0","userId":"132919026581110784","addDate":"2020-05-07 17:09:10","content":"硝化细菌"}],"articleId":"135557699227222016","praisecount":"0","NickName":"13677777777","content":"11111","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"北京市","videoTime":0,"cuserid":"132995616497336320","adddate":"2小时前"},{"commenCount":9,"Head":"http://file.vipiyou.com/static/VipHead.png","images":"http://file.vipiyou.com/friend/picture/20200507/4e08c71db5123642_l.jpg","commenlist":[{"commentNickName":"135800000000","commentRNickName":"","articleId":"135552288914800640","ruserId":"0","userId":"132919026581110784","addDate":"2020-05-07 16:31:17","content":"营业厅"},{"commentNickName":"13677777777","commentRNickName":"135800000000","articleId":"135552288914800640","ruserId":"132919026581110784","userId":"132995616497336320","addDate":"2020-05-07 16:31:26","content":"反对发射点"},{"commentNickName":"13677777777","commentRNickName":"135800000000","articleId":"135552288914800640","ruserId":"132919026581110784","userId":"132995616497336320","addDate":"2020-05-07 16:31:32","content":"大师傅但是倒十分都是"},{"commentNickName":"13677777777","commentRNickName":"135800000000","articleId":"135552288914800640","ruserId":"132919026581110784","userId":"132995616497336320","addDate":"2020-05-07 16:31:37","content":"撒旦发射点都是"},{"commentNickName":"13677777777","commentRNickName":"135800000000","articleId":"135552288914800640","ruserId":"132919026581110784","userId":"132995616497336320","addDate":"2020-05-07 16:31:41","content":"士大夫但是稍等"},{"commentNickName":"13677777777","commentRNickName":"135800000000","articleId":"135552288914800640","ruserId":"132919026581110784","userId":"132995616497336320","addDate":"2020-05-07 16:31:47","content":"发士大夫是"},{"commentNickName":"13677777777","commentRNickName":"135800000000","articleId":"135552288914800640","ruserId":"132919026581110784","userId":"132995616497336320","addDate":"2020-05-07 16:31:50","content":"撒旦发射点都是"},{"commentNickName":"13677777777","commentRNickName":"135800000000","articleId":"135552288914800640","ruserId":"132919026581110784","userId":"132995616497336320","addDate":"2020-05-07 16:51:54","content":"v程序的"},{"commentNickName":"13677777777","commentRNickName":"13677777777","articleId":"135552288914800640","ruserId":"132995616497336320","userId":"132995616497336320","addDate":"2020-05-07 16:52:05","content":"的说法第三方"}],"articleId":"135552288914800640","praisecount":"0","NickName":"13677777777","content":"111","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"北京市","videoTime":0,"cuserid":"132995616497336320","adddate":"2小时前"},{"commenCount":0,"Head":"http://file.vipiyou.com/static/VipHead.png","images":"http://file.vipiyou.com/friend/picture/20200507/e30cb66d4671a8b6_l.jpg","commenlist":[],"articleId":"135550924268634112","praisecount":"0","NickName":"男士","content":"安宫丸","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"深圳市","videoTime":0,"cuserid":"135448771608838144","adddate":"2小时前"},{"commenCount":0,"Head":"http://file.vipiyou.com/user/picture/20200507/c43fcfd92dfebee1_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200507/943f759e12064d05_l.jpg","commenlist":[],"articleId":"135550633473343488","praisecount":"0","NickName":"小月月","content":"防范风险","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"深圳市","videoTime":0,"cuserid":"135502175852953600","adddate":"2小时前"},{"commenCount":0,"Head":"http://file.vipiyou.com/user/picture/20200507/c43fcfd92dfebee1_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200507/14f2223dc3f4b497_l.jpg","commenlist":[],"articleId":"135550343118454784","praisecount":"0","NickName":"小月月","content":"天天过分","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"深圳市","videoTime":0,"cuserid":"135502175852953600","adddate":"2小时前"},{"commenCount":0,"Head":"http://file.vipiyou.com/static/VipHead.png","images":"http://file.vipiyou.com/friend/picture/20200507/7656f43238e6cca2_l.jpg","commenlist":[],"articleId":"135550147328344064","praisecount":"0","NickName":"男士","content":"路路通","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"深圳市","videoTime":0,"cuserid":"135448771608838144","adddate":"2小时前"},{"commenCount":0,"Head":"http://file.vipiyou.com/static/VipHead.png","images":"http://iyfile.tiantiancaidian.com//friend/picture/20200507/7a8307082efa33d2_l.jpg","commenlist":[],"articleId":"135549996954157056","praisecount":"0","NickName":"男性06","content":"很多变得棒棒哒","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"深圳市","videoTime":0,"cuserid":"132976135708475392","adddate":"2小时前"},{"commenCount":0,"Head":"http://file.vipiyou.com/static/VipHead.png","images":"http://file.vipiyou.com/friend/picture/20200507/ed1f78d7fc8c4bcc_l.jpg","commenlist":[],"articleId":"135549456232873984","praisecount":"0","NickName":"家里凤弟","content":"顾","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"深圳市","videoTime":0,"cuserid":"132295376429514752","adddate":"2小时前"},{"commenCount":3,"Head":"http://file.vipiyou.com/static/VipHead.png","images":"http://file.vipiyou.com/friend/picture/20200507/e61b5b907046cb19_l.jpg","commenlist":[{"commentNickName":"135800000000","commentRNickName":"","articleId":"135549024261505024","ruserId":"0","userId":"132919026581110784","addDate":"2020-05-07 16:18:31","content":"锦绣香江就行"},{"commentNickName":"13677777777","commentRNickName":"135800000000","articleId":"135549024261505024","ruserId":"132919026581110784","userId":"132995616497336320","addDate":"2020-05-07 16:18:40","content":"51516316"},{"commentNickName":"13677777777","commentRNickName":"135800000000","articleId":"135549024261505024","ruserId":"132919026581110784","userId":"132995616497336320","addDate":"2020-05-07 16:25:08","content":"范德萨犯得"}],"articleId":"135549024261505024","praisecount":"0","NickName":"13677777777","content":"54654","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"北京市","videoTime":0,"cuserid":"132995616497336320","adddate":"2小时前"},{"commenCount":0,"Head":"http://file.vipiyou.com/user/picture/20200504/2cda2dda3250995f_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200507/6b46d379d98c816b_l.jpg","commenlist":[],"articleId":"135548968611479552","praisecount":"0","NickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","content":"咯哦哦","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"成都市","videoTime":0,"cuserid":"134359032256397312","adddate":"2小时前"},{"commenCount":1,"Head":"http://file.vipiyou.com/user/picture/20200504/dd2fc782ad269690_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200507/9c8e18e0c999be5b_l.jpg,http://file.vipiyou.com/friend/picture/20200507/e16d4d9fd8d0843b_l.jpg,http://file.vipiyou.com/friend/picture/20200507/e90fd490e184f101_l.jpg,http://file.vipiyou.com/friend/picture/20200507/d17f70cb927d9405_l.jpg,http://file.vipiyou.com/friend/picture/20200507/d4b1427d9dfebfa1_l.jpg,http://file.vipiyou.com/friend/picture/20200507/cafd01c454065c88_l.jpg,http://file.vipiyou.com/friend/picture/20200507/d9c8a3e2fce6948e_l.jpg,http://file.vipiyou.com/friend/picture/20200507/c763456e5b4cec45_l.jpg,http://file.vipiyou.com/friend/picture/20200507/c2ec8d3028898957_l.jpg","commenlist":[{"commentNickName":"13677777777","commentRNickName":"","articleId":"135541718631186432","ruserId":"0","userId":"132995616497336320","addDate":"2020-05-07 16:17:23","content":"范德萨范德萨发"}],"articleId":"135541718631186432","praisecount":"2","NickName":"仙女菲","content":"XXXX","videoPath":"","videoImage":"","isPraise":0,"isMore":"0","location":"深圳市","videoTime":0,"cuserid":"134417563043102720","adddate":"3小时前"},{"commenCount":0,"Head":"http://file.vipiyou.com/user/picture/20200507/9af26ac6c1d6ac13_l.jpg","images":"","commenlist":[],"articleId":"135505933030785024","praisecount":"2","NickName":"女测11","content":"测试","videoPath":"http://iyfile.tiantiancaidian.com//friend/video/20200507/e9016cb282420fba.mp4","videoImage":"http://iyfile.tiantiancaidian.com//friend/video/20200507/e9016cb282420fba.jpg","isPraise":0,"isMore":"0","location":"成都市","videoTime":0,"cuserid":"135476366635302912","adddate":"5小时前"}]
     */

    private String code;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * commenCount : 0
         * Head : http://file.vipiyou.com/static/VipHead.png
         * images : http://file.vipiyou.com/friend/picture/20200507/89cb47d73720076e_l.jpg
         * commenlist : []
         * articleId : 135582495449153536
         * praisecount : 0
         * NickName : 13677777777
         * content : 343243
         * videoPath :
         * videoImage :
         * isPraise : 0
         * isMore : 0
         * location : 北京市
         * videoTime : 0
         * cuserid : 132995616497336320
         * adddate : 35分钟前
         */

        private int commenCount;
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

        private List<CommentBean> commenlist;

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

        public int getIsPraise() {
            return isPraise;
        }

        public void setIsPraise(int isPraise) {
            this.isPraise = isPraise;
        }

        public String getIsMore() {
            return isMore;
        }

        public void setIsMore(String isMore) {
            this.isMore = isMore;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getVideoTime() {
            return videoTime;
        }

        public void setVideoTime(int videoTime) {
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
}
