package com.jfkj.im.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/20
 * </pre>
 */

public class CircleBean {


    /**
     * code : 200
     * message : 查询成功
     * data : [{"commenCount":0,"Head":"http://iyfile.tiantiancaidian.com//verify/picture/20200612/5e91b3edbf7aa80e_l.jpg","images":"http://iyfile.tiantiancaidian.com//verify/picture/20200612/5e91b3edbf7aa80e_l.jpg,http://iyfile.tiantiancaidian.com//verify/picture/20200612/5f47a07196a22452_l.jpg","commenlist":[],"articleId":"148586212020584448","praisecount":"0","NickName":"诺瓦克记录","content":"新人报道！","videoPath":"","picImages":[{"width":1512,"url":"http://iyfile.tiantiancaidian.com//verify/picture/20200612/5e91b3edbf7aa80e_l.jpg","height":2016},{"width":1512,"url":"http://iyfile.tiantiancaidian.com//verify/picture/20200612/5f47a07196a22452_l.jpg","height":2016}],"videoImage":"","isPraise":0,"isMore":"0","location":"成都市","videoTime":"","cuserid":"148584597603614720","videoImages":{"width":0,"url":"","height":0},"adddate":"22小时前"},{"commenCount":0,"Head":"http://iyfile.tiantiancaidian.com//user/picture/20200612/3d65b0e6af5d9ad4_l.jpg","images":"http://iyfile.tiantiancaidian.com//friend/picture/20200612/a160fcf0102468de_l.jpg","commenlist":[],"articleId":"148573287172145152","praisecount":"2","NickName":"测试男1","content":"好咯","videoPath":"","picImages":[{"width":828,"url":"http://iyfile.tiantiancaidian.com//friend/picture/20200612/a160fcf0102468de_l.jpg","height":1472}],"videoImage":"","isPraise":1,"isMore":"0","praiseId":"148905051254030336","location":"成都市","videoTime":0,"cuserid":"138422858853515264","videoImages":{"width":0,"url":"","height":0},"adddate":"23小时前"},{"commenCount":0,"Head":"http://file.vipiyou.com/user/picture/20200428/801003c77219bdc1_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200612/d8b5fb72815293b1_l.jpg,http://file.vipiyou.com/friend/picture/20200612/d3651a8ee80473f2_l.jpg,http://file.vipiyou.com/friend/picture/20200612/dd82adb64f3a4e5b_l.jpg,http://file.vipiyou.com/friend/picture/20200612/c035a422606b9c68_l.jpg","commenlist":[],"articleId":"148555481651216384","praisecount":"3","NickName":"额额额额额呃呃呃呃得得额额","content":"ggh","videoPath":"","picImages":[{"width":720,"url":"http://file.vipiyou.com/friend/picture/20200612/d8b5fb72815293b1_l.jpg","height":1440},{"width":480,"url":"http://file.vipiyou.com/friend/picture/20200612/d3651a8ee80473f2_l.jpg","height":800},{"width":600,"url":"http://file.vipiyou.com/friend/picture/20200612/dd82adb64f3a4e5b_l.jpg","height":901},{"width":1080,"url":"http://file.vipiyou.com/friend/picture/20200612/c035a422606b9c68_l.jpg","height":1920}],"videoImage":"","isPraise":1,"isMore":"0","praiseId":"148905059999154176","location":"成都市","videoTime":0,"cuserid":"132264384402685952","videoImages":{"width":0,"url":"","height":0},"adddate":"昨天"},{"commenCount":0,"Head":"http://file.vipiyou.com/user/picture/20200521/b4f2b1952da9fe1e_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200609/af779b453fb9732b_l.jpg","commenlist":[],"articleId":"147468081801854976","praisecount":"2","NickName":"dfdfdf","content":"提交了兔","videoPath":"","picImages":[{"width":1080,"url":"http://file.vipiyou.com/friend/picture/20200609/af779b453fb9732b_l.jpg","height":2248}],"videoImage":"","isPraise":1,"isMore":"0","praiseId":"147836181365784576","location":"成都市","videoTime":0,"cuserid":"132255289025101824","videoImages":{"width":0,"url":"","height":0},"adddate":"4天前"},{"commenCount":0,"Head":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/9c02b44d0651245b_l.jpg","images":"http://iyfile.tiantiancaidian.com//friend/picture/20200608/8dfddafdf73880fc_l.jpg","commenlist":[],"articleId":"147118211027042304","praisecount":"2","NickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","content":"123213213213","videoPath":"","picImages":[{"width":828,"url":"http://iyfile.tiantiancaidian.com//friend/picture/20200608/8dfddafdf73880fc_l.jpg","height":466}],"videoImage":"","isPraise":1,"isMore":"0","praiseId":"147836187334279168","location":"","videoTime":0,"cuserid":"132310047622561792","videoImages":{"width":0,"url":"","height":0},"adddate":"4天前"},{"commenCount":0,"Head":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/9c02b44d0651245b_l.jpg","images":"http://iyfile.tiantiancaidian.com//friend/picture/20200608/a4b2616481df5dfa_l.jpg","commenlist":[],"articleId":"147118160728948736","praisecount":"1","NickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","content":"23123","videoPath":"","picImages":[{"width":750,"url":"http://iyfile.tiantiancaidian.com//friend/picture/20200608/a4b2616481df5dfa_l.jpg","height":1260}],"videoImage":"","isPraise":1,"isMore":"0","praiseId":"147836196888903680","location":"","videoTime":0,"cuserid":"132310047622561792","videoImages":{"width":0,"url":"","height":0},"adddate":"4天前"},{"commenCount":0,"Head":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/9c02b44d0651245b_l.jpg","images":"http://iyfile.tiantiancaidian.com//friend/picture/20200608/98da0dc5738f959f_l.jpg","commenlist":[],"articleId":"147118048959135744","praisecount":"0","NickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","content":"123123123","videoPath":"","picImages":[{"width":828,"url":"http://iyfile.tiantiancaidian.com//friend/picture/20200608/98da0dc5738f959f_l.jpg","height":620}],"videoImage":"","isPraise":0,"isMore":"0","location":"","videoTime":0,"cuserid":"132310047622561792","videoImages":{"width":0,"url":"","height":0},"adddate":"4天前"},{"commenCount":0,"Head":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/9c02b44d0651245b_l.jpg","images":"http://iyfile.tiantiancaidian.com//friend/picture/20200608/675b0a43cafc7fab_l.jpg","commenlist":[],"articleId":"147116986067976192","praisecount":"0","NickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","content":"asdasdasdas","videoPath":"","picImages":[{"width":1080,"url":"http://iyfile.tiantiancaidian.com//friend/picture/20200608/675b0a43cafc7fab_l.jpg","height":400}],"videoImage":"","isPraise":0,"isMore":"0","location":"","videoTime":0,"cuserid":"132310047622561792","videoImages":{"width":0,"url":"","height":0},"adddate":"4天前"},{"commenCount":0,"Head":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/9c02b44d0651245b_l.jpg","images":"http://iyfile.tiantiancaidian.com//friend/picture/20200608/bbe1deb175ad1bc1_l.jpg","commenlist":[],"articleId":"147103983876571136","praisecount":"0","NickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","content":"12321312","videoPath":"","picImages":[{"width":414,"url":"http://iyfile.tiantiancaidian.com//friend/picture/20200608/bbe1deb175ad1bc1_l.jpg","height":3180}],"videoImage":"","isPraise":0,"isMore":"0","location":"","videoTime":0,"cuserid":"132310047622561792","videoImages":{"width":0,"url":"","height":0},"adddate":"5天前"},{"commenCount":12,"Head":"http://file.vipiyou.com/user/picture/20200602/121576a80d904331_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200604/cb65084de1f31394_l.jpg","commenlist":[{"commentNickName":"测试男1","commentHead":"http://iyfile.tiantiancaidian.com//user/picture/20200612/3d65b0e6af5d9ad4_l.jpg","commentRNickName":"","articleId":"145738520152113152","ruserId":"0","userId":"138422858853515264","addDate":"2020-06-04 19:07:44","content":"啊"},{"commentNickName":"社会凤姐","commentHead":"http://file.vipiyou.com/user/picture/20200602/121576a80d904331_l.jpg","commentRNickName":"测试男1","articleId":"145738520152113152","ruserId":"138422858853515264","userId":"132263954654298112","addDate":"2020-06-04 19:07:57","content":"啊路"},{"commentNickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","commentHead":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/9c02b44d0651245b_l.jpg","commentRNickName":"","articleId":"145738520152113152","ruserId":"0","userId":"132310047622561792","addDate":"2020-06-06 15:19:08","content":"123123"},{"commentNickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","commentHead":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/9c02b44d0651245b_l.jpg","commentRNickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","articleId":"145738520152113152","ruserId":"132310047622561792","userId":"132310047622561792","addDate":"2020-06-08 16:19:41","content":"12312312"},{"commentNickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","commentHead":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/9c02b44d0651245b_l.jpg","commentRNickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","articleId":"145738520152113152","ruserId":"132310047622561792","userId":"132310047622561792","addDate":"2020-06-08 16:39:38","content":"123123123123"},{"commentNickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","commentHead":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/9c02b44d0651245b_l.jpg","commentRNickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","articleId":"145738520152113152","ruserId":"132310047622561792","userId":"132310047622561792","addDate":"2020-06-08 17:12:25","content":"Assad\u2019s dads"},{"commentNickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","commentHead":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/9c02b44d0651245b_l.jpg","commentRNickName":"","articleId":"145738520152113152","ruserId":"0","userId":"132310047622561792","addDate":"2020-06-08 17:18:31","content":"12321321355sdasdasd"},{"commentNickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","commentHead":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/9c02b44d0651245b_l.jpg","commentRNickName":"","articleId":"145738520152113152","ruserId":"0","userId":"132310047622561792","addDate":"2020-06-08 17:18:56","content":"1231231"},{"commentNickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","commentHead":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/9c02b44d0651245b_l.jpg","commentRNickName":"","articleId":"145738520152113152","ruserId":"0","userId":"132310047622561792","addDate":"2020-06-08 17:20:02","content":"123123123123"},{"commentNickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","commentHead":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/9c02b44d0651245b_l.jpg","commentRNickName":"","articleId":"145738520152113152","ruserId":"0","userId":"132310047622561792","addDate":"2020-06-08 17:21:08","content":"12321312qq"},{"commentNickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","commentHead":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/9c02b44d0651245b_l.jpg","commentRNickName":"","articleId":"145738520152113152","ruserId":"0","userId":"132310047622561792","addDate":"2020-06-08 17:23:27","content":"1231231231"},{"commentNickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","commentHead":"http://iyfile.tiantiancaidian.com//verify/picture/20200428/9c02b44d0651245b_l.jpg","commentRNickName":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","articleId":"145738520152113152","ruserId":"132310047622561792","userId":"132310047622561792","addDate":"2020-06-08 17:36:37","content":"As31das231d231asd231as2e13qwe21qw5e46qw54eqw54eqw54e54a6d456ad123asd321as1das3d3as1d132asd21as2d13zx12cxz213c2xz1c1xzc312zx32ca321d2asdasdasdasd"}],"articleId":"145738520152113152","praisecount":"2","NickName":"社会凤姐","content":"啊","videoPath":"","picImages":[{"width":1080,"url":"http://file.vipiyou.com/friend/picture/20200604/cb65084de1f31394_l.jpg","height":2160}],"videoImage":"","isPraise":1,"isMore":"1","praiseId":"147865844259618816","location":"成都市","videoTime":0,"cuserid":"132263954654298112","videoImages":{"width":0,"url":"","height":0},"adddate":"8天前"},{"commenCount":0,"Head":"http://file.vipiyou.com/user/picture/20200428/801003c77219bdc1_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200604/b01b36cf212df921_l.jpg","commenlist":[],"articleId":"145737175139483648","praisecount":"1","NickName":"额额额额额呃呃呃呃得得额额","content":"ttt","videoPath":"","picImages":[{"width":720,"url":"http://file.vipiyou.com/friend/picture/20200604/b01b36cf212df921_l.jpg","height":1440}],"videoImage":"","isPraise":0,"isMore":"0","location":"成都市","videoTime":0,"cuserid":"132264384402685952","videoImages":{"width":0,"url":"","height":0},"adddate":"8天前"},{"commenCount":0,"Head":"http://file.vipiyou.com/user/picture/20200608/817444be8e23d941_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200604/ae7a1e2c97689c03_l.jpg","commenlist":[],"articleId":"145737010261393408","praisecount":"0","NickName":"测试女002","content":"啊","videoPath":"","picImages":[{"width":1440,"url":"http://file.vipiyou.com/friend/picture/20200604/ae7a1e2c97689c03_l.jpg","height":1280}],"videoImage":"","isPraise":0,"isMore":"0","location":"成都市","videoTime":0,"cuserid":"138813266884427776","videoImages":{"width":0,"url":"","height":0},"adddate":"8天前"},{"commenCount":0,"Head":"http://iyfile.tiantiancaidian.com//user/picture/20200612/3d65b0e6af5d9ad4_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200604/a4b6be2d227ad870_l.jpg","commenlist":[],"articleId":"145736643284959232","praisecount":"2","NickName":"测试男1","content":"阿狸","videoPath":"","picImages":[{"width":1080,"url":"http://file.vipiyou.com/friend/picture/20200604/a4b6be2d227ad870_l.jpg","height":2340}],"videoImage":"","isPraise":0,"isMore":"0","location":"成都市","videoTime":0,"cuserid":"138422858853515264","videoImages":{"width":0,"url":"","height":0},"adddate":"8天前"},{"commenCount":0,"Head":"http://file.vipiyou.com/user/picture/20200428/801003c77219bdc1_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200604/735895e1639121e5_l.jpg,http://file.vipiyou.com/friend/picture/20200604/775fdd5d073cd18a_l.jpg,http://file.vipiyou.com/friend/picture/20200604/840b5e3a903c8d6f_l.jpg,http://file.vipiyou.com/friend/picture/20200604/83fb31c5720c37cc_l.jpg,http://file.vipiyou.com/friend/picture/20200604/6bc60866060a93b6_l.jpg","commenlist":[],"articleId":"145702491017838592","praisecount":"1","NickName":"额额额额额呃呃呃呃得得额额","content":"55555","videoPath":"","picImages":[{"width":600,"url":"http://file.vipiyou.com/friend/picture/20200604/735895e1639121e5_l.jpg","height":901},{"width":480,"url":"http://file.vipiyou.com/friend/picture/20200604/775fdd5d073cd18a_l.jpg","height":800},{"width":1100,"url":"http://file.vipiyou.com/friend/picture/20200604/840b5e3a903c8d6f_l.jpg","height":1390},{"width":480,"url":"http://file.vipiyou.com/friend/picture/20200604/83fb31c5720c37cc_l.jpg","height":800},{"width":720,"url":"http://file.vipiyou.com/friend/picture/20200604/6bc60866060a93b6_l.jpg","height":1553}],"videoImage":"","isPraise":0,"isMore":"0","location":"成都市","videoTime":0,"cuserid":"132264384402685952","videoImages":{"width":0,"url":"","height":0},"adddate":"8天前"},{"commenCount":0,"Head":"http://file.vipiyou.com/user/picture/20200428/801003c77219bdc1_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200604/e57cf67a24524295_l.jpg,http://file.vipiyou.com/friend/picture/20200604/e203b9c4405d0d70_l.jpg,http://file.vipiyou.com/friend/picture/20200604/e14dc7c5f63260d2_l.jpg,http://file.vipiyou.com/friend/picture/20200604/b1bad5fa6f464209_l.jpg","commenlist":[],"articleId":"145702383668822016","praisecount":"1","NickName":"额额额额额呃呃呃呃得得额额","content":"44444","videoPath":"","picImages":[{"width":480,"url":"http://file.vipiyou.com/friend/picture/20200604/e57cf67a24524295_l.jpg","height":800},{"width":480,"url":"http://file.vipiyou.com/friend/picture/20200604/e203b9c4405d0d70_l.jpg","height":800},{"width":720,"url":"http://file.vipiyou.com/friend/picture/20200604/e14dc7c5f63260d2_l.jpg","height":1558},{"width":720,"url":"http://file.vipiyou.com/friend/picture/20200604/b1bad5fa6f464209_l.jpg","height":1560}],"videoImage":"","isPraise":0,"isMore":"0","location":"成都市","videoTime":0,"cuserid":"132264384402685952","videoImages":{"width":0,"url":"","height":0},"adddate":"8天前"},{"commenCount":0,"Head":"http://file.vipiyou.com/user/picture/20200428/801003c77219bdc1_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200604/7d831b8cafc7bff5_l.jpg,http://file.vipiyou.com/friend/picture/20200604/7be5980d1fffd62e_l.jpg,http://file.vipiyou.com/friend/picture/20200604/76021b257636b187_l.jpg","commenlist":[],"articleId":"145701491695550464","praisecount":"1","NickName":"额额额额额呃呃呃呃得得额额","content":"3333","videoPath":"","picImages":[{"width":600,"url":"http://file.vipiyou.com/friend/picture/20200604/7d831b8cafc7bff5_l.jpg","height":901},{"width":480,"url":"http://file.vipiyou.com/friend/picture/20200604/7be5980d1fffd62e_l.jpg","height":800},{"width":480,"url":"http://file.vipiyou.com/friend/picture/20200604/76021b257636b187_l.jpg","height":800}],"videoImage":"","isPraise":0,"isMore":"0","location":"成都市","videoTime":0,"cuserid":"132264384402685952","videoImages":{"width":0,"url":"","height":0},"adddate":"8天前"},{"commenCount":0,"Head":"http://file.vipiyou.com/user/picture/20200428/801003c77219bdc1_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200604/89eba3755952e10b_l.jpg,http://file.vipiyou.com/friend/picture/20200604/64c2e96a2ede804b_l.jpg","commenlist":[],"articleId":"145666197428502528","praisecount":"1","NickName":"额额额额额呃呃呃呃得得额额","content":"222222","videoPath":"","picImages":[{"width":1080,"url":"http://file.vipiyou.com/friend/picture/20200604/89eba3755952e10b_l.jpg","height":1920},{"width":720,"url":"http://file.vipiyou.com/friend/picture/20200604/64c2e96a2ede804b_l.jpg","height":1440}],"videoImage":"","isPraise":0,"isMore":"0","location":"成都市","videoTime":0,"cuserid":"132264384402685952","videoImages":{"width":0,"url":"","height":0},"adddate":"8天前"},{"commenCount":0,"Head":"http://iyfile.tiantiancaidian.com//verify/picture/20200601/0038eab02cc906af_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200604/2ac34e22dcdc1e83_l.jpg","commenlist":[],"articleId":"145626142005657600","praisecount":"1","NickName":"测试女020","content":"哭","videoPath":"","picImages":[{"width":1080,"url":"http://file.vipiyou.com/friend/picture/20200604/2ac34e22dcdc1e83_l.jpg","height":2340}],"videoImage":"","isPraise":0,"isMore":"0","location":"成都市","videoTime":0,"cuserid":"144581687941136384","videoImages":{"width":0,"url":"","height":0},"adddate":"9天前"},{"commenCount":0,"Head":"http://iyfile.tiantiancaidian.com//user/picture/20200612/3d65b0e6af5d9ad4_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200604/18b4dd89ac4c5872_l.jpg","commenlist":[],"articleId":"145626081951612928","praisecount":"0","NickName":"测试男1","content":"啊","videoPath":"","picImages":[{"width":1440,"url":"http://file.vipiyou.com/friend/picture/20200604/18b4dd89ac4c5872_l.jpg","height":1280}],"videoImage":"","isPraise":0,"isMore":"0","location":"成都市","videoTime":0,"cuserid":"138422858853515264","videoImages":{"width":0,"url":"","height":0},"adddate":"9天前"},{"commenCount":0,"Head":"http://iyfile.tiantiancaidian.com//user/picture/20200612/3d65b0e6af5d9ad4_l.jpg","images":"http://file.vipiyou.com/friend/picture/20200604/b2a57cf4726afb4c_l.jpg","commenlist":[],"articleId":"145616777685106688","praisecount":"3","NickName":"测试男1","content":"阿鲁","videoPath":"","picImages":[{"width":1080,"url":"http://file.vipiyou.com/friend/picture/20200604/b2a57cf4726afb4c_l.jpg","height":2340}],"videoImage":"","isPraise":0,"isMore":"0","location":"成都市","videoTime":0,"cuserid":"138422858853515264","videoImages":{"width":0,"url":"","height":0},"adddate":"9天前"}]
     */

    private String code;
    private String message;
    private List<DataBean> data;


    public boolean isSuccess(){
        return "200".equals(code);
    }


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
         * Head : http://iyfile.tiantiancaidian.com//verify/picture/20200612/5e91b3edbf7aa80e_l.jpg
         * images : http://iyfile.tiantiancaidian.com//verify/picture/20200612/5e91b3edbf7aa80e_l.jpg,http://iyfile.tiantiancaidian.com//verify/picture/20200612/5f47a07196a22452_l.jpg
         * commenlist : []
         * articleId : 148586212020584448
         * praisecount : 0
         * NickName : 诺瓦克记录
         * content : 新人报道！
         * videoPath :
         * picImages : [{"width":1512,"url":"http://iyfile.tiantiancaidian.com//verify/picture/20200612/5e91b3edbf7aa80e_l.jpg","height":2016},{"width":1512,"url":"http://iyfile.tiantiancaidian.com//verify/picture/20200612/5f47a07196a22452_l.jpg","height":2016}]
         * videoImage :
         * isPraise : 0
         * isMore : 0
         * location : 成都市
         * videoTime :
         * cuserid : 148584597603614720
         * videoImages : {"width":0,"url":"","height":0}
         * adddate : 22小时前
         * praiseId : 148905051254030336
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
        private String videoTime;
        private String cuserid;
        private VideoImagesBean videoImages;
        private String adddate;
        private String praiseId;
        private List<?> commenlist;
        private List<PicImagesBean> picImages;
        private String commenDiamonds;

        public String getCommenDiamonds() {
            return commenDiamonds;
        }

        public void setCommenDiamonds(String commenDiamonds) {
            this.commenDiamonds = commenDiamonds;
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

        public VideoImagesBean getVideoImages() {
            return videoImages;
        }

        public void setVideoImages(VideoImagesBean videoImages) {
            this.videoImages = videoImages;
        }

        public String getAdddate() {
            return adddate;
        }

        public void setAdddate(String adddate) {
            this.adddate = adddate;
        }

        public String getPraiseId() {
            return praiseId;
        }

        public void setPraiseId(String praiseId) {
            this.praiseId = praiseId;
        }

        public List<?> getCommenlist() {
            return commenlist;
        }

        public void setCommenlist(List<?> commenlist) {
            this.commenlist = commenlist;
        }

        public List<PicImagesBean> getPicImages() {
            return picImages;
        }

        public void setPicImages(List<PicImagesBean> picImages) {
            this.picImages = picImages;
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
