package com.jfkj.im.utils.trtc.trtc.widget.bgm;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.jfkj.im.R;
import com.jfkj.im.utils.trtc.trtc.sdkadapter.bgm.TRTCBgmManager;
import com.jfkj.im.utils.trtc.trtc.utils.FileUtils;
import com.jfkj.im.utils.trtc.trtc.widget.BaseSettingFragment;
import com.jfkj.im.utils.trtc.trtc.widget.settingitem.BaseSettingItem;
import com.jfkj.im.utils.trtc.trtc.widget.settingitem.SeekBarSettingItem;
import com.jfkj.im.utils.trtc.trtc.widget.settingitem.SelectionSettingItem;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * BGM设置页
 *
 * @author guanyifeng
 */
public class BgmSettingFragment extends BaseSettingFragment implements View.OnClickListener {
    public static final  int                   STATUS_IDLE            = 0;
    public static final  int                   STATUS_PAUSE           = 1;
    public static final  int                   STATUS_RESUME          = 2;
    // 对应 SDK 的混响列表（TRTCCloudDef中定义）
    private static final List<String> REVERB_LIST            = Arrays.asList("关闭混响", "KTV", "小房间", "大会堂", "低沉", "洪亮", "金属声", "磁性");
    private static final List<Integer> REVERB_TYPE_ARR        = Arrays.asList(TRTCCloudDef.TRTC_REVERB_TYPE_0,
            TRTCCloudDef.TRTC_REVERB_TYPE_1, TRTCCloudDef.TRTC_REVERB_TYPE_2, TRTCCloudDef.TRTC_REVERB_TYPE_3,
            TRTCCloudDef.TRTC_REVERB_TYPE_4, TRTCCloudDef.TRTC_REVERB_TYPE_5, TRTCCloudDef.TRTC_REVERB_TYPE_6, TRTCCloudDef.TRTC_REVERB_TYPE_7);
    // 对应 SDK 的变声列表（TRTCCloudDef中定义）
    private static final List<String> VOICE_CHANGER_LIST     = Arrays.asList("关闭变声", "熊孩子", "萝莉", "大叔", "重金属", "感冒", "外国人", "困兽", "死肥仔", "强电流", "重机械", "空灵");
    private static final List<Integer> VOICE_CHANGER_TYPE_ARR = Arrays.asList(TRTCCloudDef.TRTC_VOICE_CHANGER_TYPE_0,
            TRTCCloudDef.TRTC_VOICE_CHANGER_TYPE_1, TRTCCloudDef.TRTC_VOICE_CHANGER_TYPE_2, TRTCCloudDef.TRTC_VOICE_CHANGER_TYPE_3,
            TRTCCloudDef.TRTC_VOICE_CHANGER_TYPE_4, TRTCCloudDef.TRTC_VOICE_CHANGER_TYPE_5, TRTCCloudDef.TRTC_VOICE_CHANGER_TYPE_6,
            TRTCCloudDef.TRTC_VOICE_CHANGER_TYPE_7, TRTCCloudDef.TRTC_VOICE_CHANGER_TYPE_8, TRTCCloudDef.TRTC_VOICE_CHANGER_TYPE_9,
            TRTCCloudDef.TRTC_VOICE_CHANGER_TYPE_10, TRTCCloudDef.TRTC_VOICE_CHANGER_TYPE_11);
    private static final String LOCAL_BGM_FILE_NAME    = "buzuibuhui.mp3";
    private static final String LOCAL_BGM_PATH         = "/sdcard/testMusic/" + LOCAL_BGM_FILE_NAME;
    private LinearLayout mContentItem;
    private List<BaseSettingItem> mSettingItemList;
    private TextView mTitle;
    private Button mStartBtn;
    private Button mEndBtn;
    private ProgressBar mItemPb;
    private              int                   mPlayNextStatus        = STATUS_IDLE;
    private              int                   mBgmVol                = 50;
    private              int                   mMicVol                = 50;
    private              int                   mSelectedReverb;
    private              int                   mSelectedVoiceChange;
    private TRTCBgmManager mTRTCBgmManager;

    private TRTCCloud.BGMNotify mBGMNotify = new TRTCCloud.BGMNotify() {
        @Override
        public void onBGMStart(int errCode) {
        }

        @Override
        public void onBGMProgress(final long progress, final long duration) {
            if (mItemPb != null) {
                mItemPb.post(new Runnable() {
                    @Override
                    public void run() {
                        mItemPb.setProgress((int) (progress / (float) duration * 100));
                    }
                });
            }
        }

        @Override
        public void onBGMComplete(int err) {
            if (mStartBtn != null) {
                mStartBtn.setBackgroundResource(R.drawable.trtc_ic_play_start);
            }
            if (mItemPb != null) {
                mItemPb.post(new Runnable() {
                    @Override
                    public void run() {
                        mItemPb.setProgress(0);
                    }
                });
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 拷贝mp3文件到sdcard
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                File file = new File(LOCAL_BGM_PATH);
                if (file.exists()) {
                    return;
                }
                FileUtils.copyFilesFromAssets(BgmSettingFragment.this.getActivity(),
                        LOCAL_BGM_FILE_NAME,
                        LOCAL_BGM_PATH);
            }
        });
    }

    public TRTCCloud.BGMNotify getBGMNotify() {
        return mBGMNotify;
    }

    public void setTRTCBgmManager(TRTCBgmManager trtcBgmManager) {
        mTRTCBgmManager = trtcBgmManager;
    }

    @Override
    protected void initView(View itemView) {
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mStartBtn = (Button) itemView.findViewById(R.id.btn_start);
        mStartBtn.setOnClickListener(this);
        mEndBtn = (Button) itemView.findViewById(R.id.btn_end);
        mEndBtn.setOnClickListener(this);
        mItemPb = (ProgressBar) itemView.findViewById(R.id.pb_item);
        mContentItem = (LinearLayout) itemView.findViewById(R.id.item_content);
        mSettingItemList = new ArrayList<>();

        BaseSettingItem.ItemText itemText =
                new BaseSettingItem.ItemText("BGM音量", "");
        mSettingItemList.add(new SeekBarSettingItem(getContext(), itemText, new SeekBarSettingItem.Listener() {
            @Override
            public void onSeekBarChange(int progress, boolean fromUser) {
                mBgmVol = progress;
                if (mTRTCBgmManager != null) {
                    mTRTCBgmManager.setBGMVolume(mBgmVol);
                }
            }
        }).setProgress(mBgmVol));

        itemText =
                new BaseSettingItem.ItemText("MIC音量", "");
        mSettingItemList.add(new SeekBarSettingItem(getContext(), itemText, new SeekBarSettingItem.Listener() {
            @Override
            public void onSeekBarChange(int progress, boolean fromUser) {
                mMicVol = progress;
                if (mTRTCBgmManager != null) {
                    mTRTCBgmManager.setMicVolumeOnMixing(mMicVol);
                }
            }
        }).setProgress(mMicVol));

        itemText =
                new BaseSettingItem.ItemText("混响设置", REVERB_LIST);
        mSettingItemList.add(new SelectionSettingItem(getContext(), itemText, new SelectionSettingItem.Listener() {
            @Override
            public void onItemSelected(int position, String text) {
                mSelectedReverb = position;
                if (mTRTCBgmManager != null) {
                    mTRTCBgmManager.setReverbType(REVERB_TYPE_ARR.get(position));
                }
            }
        }).setSelect(mSelectedReverb));

        itemText =
                new BaseSettingItem.ItemText("变音设置", VOICE_CHANGER_LIST);
        mSettingItemList.add(new SelectionSettingItem(getContext(), itemText, new SelectionSettingItem.Listener() {
            @Override
            public void onItemSelected(int position, String text) {
                mSelectedVoiceChange = position;
                if (mTRTCBgmManager != null) {
                    mTRTCBgmManager.setVoiceChangerType(VOICE_CHANGER_TYPE_ARR.get(position));
                }
            }
        }).setSelect(mSelectedVoiceChange));

        // 将这些view添加到对应的容器中
        for (BaseSettingItem item : mSettingItemList) {
            View view = item.getView();
            view.setPadding(0, SizeUtils.dp2px(5), 0, 0);
            mContentItem.addView(view);
        }
        updateStartBtnIcon();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.trtc_fragment_bgm;

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_start) {
            if (mTRTCBgmManager == null) {
                return;
            }
            switch (mPlayNextStatus) {
                case STATUS_IDLE:
                    mTRTCBgmManager.playBGM(LOCAL_BGM_PATH, 1, mBgmVol, mMicVol, mBGMNotify);
                    mPlayNextStatus = STATUS_PAUSE;
                    break;
                case STATUS_PAUSE:
                    mTRTCBgmManager.pauseBGM();
                    mPlayNextStatus = STATUS_RESUME;
                    break;
                case STATUS_RESUME:
                    mTRTCBgmManager.resumeBGM();
                    mPlayNextStatus = STATUS_PAUSE;
                    break;
                default:
                    break;
            }
            updateStartBtnIcon();
        } else if (id == R.id.btn_end) {
            if (mTRTCBgmManager == null) {
                return;
            }
            mTRTCBgmManager.stopBGM();
            mStartBtn.setBackgroundResource(R.drawable.trtc_ic_play_start);
            mItemPb.setProgress(0);
            mPlayNextStatus = STATUS_IDLE;
        }
    }

    private void updateStartBtnIcon() {
        switch (mPlayNextStatus) {
            case STATUS_IDLE:
                mStartBtn.setBackgroundResource(R.drawable.trtc_ic_play_start);
                break;
            case STATUS_PAUSE:
                mStartBtn.setBackgroundResource(R.drawable.trtc_ic_play_pause);
                break;
            case STATUS_RESUME:
                mStartBtn.setBackgroundResource(R.drawable.trtc_ic_play_start);
                break;
            default:
                break;
        }
    }
}
