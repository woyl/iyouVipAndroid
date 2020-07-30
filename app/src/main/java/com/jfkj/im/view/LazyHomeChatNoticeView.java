package com.jfkj.im.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jfkj.im.R;
import com.jfkj.im.interfaces.ResponListeners;
import com.jfkj.im.utils.DisplayUtil;

import java.util.List;

public class LazyHomeChatNoticeView extends ViewFlipper implements View.OnClickListener {

    private Context mContext;
    private List<String> list;
    private List<String> imgUrl;
    private ResponListeners<Integer,String> responListener2;

    public void setResponListener2(ResponListeners<Integer,String> responListener2){
        this.responListener2 = responListener2;
    }

    public LazyHomeChatNoticeView(Context context) {
        super(context);
    }

    public LazyHomeChatNoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        //设置时间
        setFlipInterval(3000);
        //边距
        setPadding(DisplayUtil.dip2px(context,2),DisplayUtil.dip2px(context,2),DisplayUtil.dip2px(context,2),DisplayUtil.dip2px(context,2));
        //设置动画
        setInAnimation(context, R.anim.notice_in);
        setOutAnimation(context,R.anim.notice_out);
    }

    /**
     * 设置数据
     *
     * */
    public void addNotice(List<String> list,List<String> urls){
        this.list = list;
        imgUrl = urls;
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < list.size();i++){
            String notice = list.get(i);
            TextView textView = new TextView(mContext);
            textView.setSingleLine();
            textView.setText(notice);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            textView.setGravity(Gravity.CENTER);
            textView.setTag(i);
            linearLayout.setTag(i);
            ImageView imageView = new ImageView(mContext);
            layoutParams.height = DisplayUtil.dip2px(mContext,22);
            layoutParams.width = DisplayUtil.dip2px(mContext,22);
            imageView.setLayoutParams(layoutParams);
//            Glide.with(mContext).load(urls.get(i)).apply(RequestOptions.bitmapTransform(new RoundedCorners(11))).into(imageView);

            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.addView(imageView);
            linearLayout.addView(textView);
            linearLayout.setOnClickListener(this);
            removeAllViews();
            addView(linearLayout);
        }
    }


    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        String notice = list.get(position);
        if (responListener2 != null){
            responListener2.Rsps(position,notice);
        }
    }
}
