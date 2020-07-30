package com.jfkj.im.TIM.modules.conversation.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.TIM.component.gatherimage.SynthesizedImageView;
import com.jfkj.im.TIM.redpack.game.GameListBean;
import com.jfkj.im.TIM.utils.ImageUtil;
import com.jfkj.im.TIM.utils.ScreenUtil;
import com.jfkj.im.utils.Utils;

import java.util.List;

/**
 * 会话列表头像View
 */
public class ConversationIconView extends RelativeLayout {

    private static final int icon_size = ScreenUtil.getPxByDp(50);
    private ImageView mIconView;


    public ConversationIconView(Context context) {
        super(context);
        init();
    }

    public ConversationIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConversationIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void invokeInformation(ConversationInfo conversationInfo, DynamicConversationIconView infoView) {
        infoView.setLayout(this);
        infoView.setMainViewId(R.id.profile_icon_group);
        infoView.parseInformation(conversationInfo);
    }

    private void init() {
        inflate(getContext(), R.layout.profile_icon_view, this);
        mIconView = findViewById(R.id.profile_icon);
        ((SynthesizedImageView) mIconView).defaultImage(0);


    }

    public void setProfileImageView(ImageView iconView) {
        mIconView = iconView;
        LayoutParams params = new LayoutParams(icon_size, icon_size);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(mIconView, params);

    }

    /**
     * 设置会话头像的url
     *
     * @param iconUrls 头像url,最多只取前9个
     */
    public void setIconUrls(List<Object> iconUrls) {
        if (mIconView instanceof SynthesizedImageView) {
            ((SynthesizedImageView) (mIconView)).displayImage(iconUrls).load();
        }
    }

    public void setConversation(ConversationInfo c) {
        if (mIconView instanceof SynthesizedImageView) {
            ((SynthesizedImageView) (mIconView)).setImageId(c.getId());
            if(c.getId().equals(Utils.SYSTEMID)){
                Glide.with(getContext()).load(R.mipmap.chatting_list_icon_system).into(mIconView);
            }else{
                setIconUrls(c.getIconUrlList());
            }
        }
    }

    public void setRadius(int radius) {
        if (mIconView instanceof SynthesizedImageView) {
            ((SynthesizedImageView) (mIconView)).setRadius(radius);
        }
    }

    public void setDefaultImageResId(int resId) {
        BitmapDrawable bd = (BitmapDrawable) getContext().getResources().getDrawable(resId);
        mIconView.setImageBitmap(bd.getBitmap());
    }

    public void setBitmapResId(int resId) {
        BitmapDrawable bd = (BitmapDrawable) getContext().getResources().getDrawable(resId);
        Bitmap bitmap = ImageUtil.toRoundBitmap(bd.getBitmap());
        mIconView.setImageBitmap(bitmap);
    }
}

