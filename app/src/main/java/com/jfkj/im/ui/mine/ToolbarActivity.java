package com.jfkj.im.ui.mine;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/28
 * </pre>
 */
public class ToolbarActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private ImageView ivPhoto;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_toolbar;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,true);
//        MenuItem menuSetting = mToolbar.getMenu().findItem(R.id.menu_setting);
        ivPhoto = findViewById(R.id.iv_photo);
//        RequestOptions options = bitmapTransform(new CenterCropRoundCornerTransform(10));
        Glide.with(this)
                .load(R.drawable.pic_bg_user)
//                .apply(options)
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) //缓存转换后的资源
                .transform(new CenterCrop(),new RoundedCorners(10))
                .into(ivPhoto);
    }
}
