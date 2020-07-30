package com.jfkj.im.utils.trtc.trtc.widget.feature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jfkj.im.R;
import com.jfkj.im.utils.trtc.trtc.sdkadapter.ConfigHelper;
import com.jfkj.im.utils.trtc.trtc.sdkadapter.feature.VideoConfig;
import com.jfkj.im.utils.trtc.trtc.utils.VideoUtils;
import com.jfkj.im.utils.trtc.trtc.widget.BaseSettingFragment;
import com.jfkj.im.utils.trtc.trtc.widget.settingitem.BaseSettingItem;
import com.jfkj.im.utils.trtc.trtc.widget.settingitem.CheckBoxSettingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 混流相关配置
 *
 * @author guanyifeng
 */
public class SteamSettingFragment extends BaseSettingFragment implements View.OnClickListener {
    private static final String TAG = SteamSettingFragment.class.getName();
    private LinearLayout mContentItem;
    private ImageView mQrImg;
    private Button mShare;
    private List<BaseSettingItem> mSettingItemList;
    private String mPlayUrl;
    private String mRoomId;
    private String mUserId;
    private CheckBoxSettingItem mMixItem;
    private VideoConfig mVideoConfig;

    @Override
    protected void initView(View itemView) {
        mContentItem = (LinearLayout) itemView.findViewById(R.id.item_content);
        mQrImg = (ImageView) itemView.findViewById(R.id.img_qr);
        mShare = (Button) itemView.findViewById(R.id.share);
        mShare.setOnClickListener(this);

        mSettingItemList = new ArrayList<>();
        mVideoConfig = ConfigHelper.getInstance().getVideoConfig();
        BaseSettingItem.ItemText itemText =
                new BaseSettingItem.ItemText("开启云端混流", "");
        mMixItem = new CheckBoxSettingItem(getContext(), itemText,
                new CheckBoxSettingItem.ClickListener() {
                    @Override
                    public void onClick() {
                        boolean enable = mVideoConfig.isEnableCloudMixture();
                        mVideoConfig.setEnableCloudMixture(!enable);
                        if (mTRTCRemoteUserManager != null) {
                            mTRTCRemoteUserManager.updateCloudMixtureParams();
                        }
                    }
                });
        mMixItem.setCheck(mVideoConfig.isEnableCloudMixture());
        mSettingItemList.add(mMixItem);

        // 将这些view添加到对应的容器中
        for (BaseSettingItem item : mSettingItemList) {
            View view = item.getView();
            view.setPadding(0, SizeUtils.dp2px(5), 0, 0);
            mContentItem.addView(view);
        }

        updateView();
    }

    public void setId(String roomId, String userId) {
        mRoomId = roomId;
        mUserId = userId;
        updateView();
    }

    private void updateView() {
        if (TextUtils.isEmpty(mRoomId) || TextUtils.isEmpty(mUserId)) {
            Log.e(TAG, "room id and user id can't be null!! ");
            return;
        }

        // 注意：该功能需要在控制台开启【旁路直播】功能，
        // 此功能是获取 CDN 直播地址，通过此功能，方便您能够在常见播放器中，播放音视频流。
        // 【*****】更多信息，您可以参考：https://cloud.tencent.com/document/product/647/16826
        // 拼接流id
        String streamId = "3891_" + VideoUtils.md5("" + mRoomId + "_" + mUserId + "_main");
        // 拼接旁路流地址
        final String playUrl = "http://3891.liveplay.myqcloud.com/live/" + streamId + ".flv";
        mPlayUrl = playUrl;

        if (mQrImg == null) {
            return;
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = VideoUtils.createQRCodeBitmap(playUrl, 400, 400);
                mQrImg.post(new Runnable() {
                    @Override
                    public void run() {
                        mQrImg.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.trtc_fragment_mix_setting;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.share) {
            if (TextUtils.isEmpty(mPlayUrl)) {
                ToastUtils.showShort("播放地址不能为空");
                return;
            }
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, mPlayUrl);
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, "分享"));
        }
    }
}
