package com.jfkj.im.TIM.modules.chat.layout.message.holder;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.component.AudioPlayer;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.utils.ScreenUtil;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.TIM.utils.TUIKitLog;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMSoundElem;

import java.io.File;

public class MessageAudioHolder extends MessageContentHolder {
    private static final int AUDIO_MIN_WIDTH = ScreenUtil.getPxByDp(60);
    private static final int AUDIO_MAX_WIDTH = ScreenUtil.getPxByDp(250);
    private static final int UNREAD = 0;
    private static final int READ = 1;
    private TextView audioTimeText;
    private ImageView audioPlayImage;
    private LinearLayout audioContentView;
      RelativeLayout audio_content_rl;
    public MessageAudioHolder(View itemView) {
        super(itemView);
    }

    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_audio;
    }
    @Override
    public void initVariableViews() {
        audioTimeText = rootView.findViewById(R.id.audio_time_tv);
        audioPlayImage = rootView.findViewById(R.id.audio_play_iv);
        audioContentView = rootView.findViewById(R.id.audio_content_ll);
        audio_content_rl = rootView.findViewById(R.id.audio_content_rl);
    }

    @Override
    public void layoutVariableViews(final MessageInfo msg, final int position) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        if (msg.isSelf()) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.rightMargin = 24;
            audioPlayImage.setImageResource(R.drawable.voice_msg_playing_3);
            audioContentView.removeView(audioPlayImage);
            audioContentView.addView(audioPlayImage);
            unreadAudioText.setVisibility(View.GONE);
            audio_content_rl.setBackgroundResource(R.drawable.shape_chat_main);
            audioTimeText.setTextColor(Color.parseColor("#ffffff"));
        } else {
            audioTimeText.setTextColor(Color.parseColor("#000000"));
             audio_content_rl.setBackgroundResource(R.drawable.shape_chat_visitor);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.leftMargin = 24;
            // TODO 图标不对
            audioPlayImage.setImageResource(R.drawable.chatting_dialog_voice_gray);
            audioContentView.removeView(audioPlayImage);
            audioContentView.addView(audioPlayImage, 0);
            if (msg.getCustomInt() == UNREAD) {
                LinearLayout.LayoutParams unreadParams = (LinearLayout.LayoutParams) isReadText.getLayoutParams();
                unreadParams.gravity = Gravity.CENTER_VERTICAL;
                unreadParams.leftMargin =1;
                unreadAudioText.setVisibility(View.VISIBLE);
                unreadAudioText.setLayoutParams(unreadParams);
            } else {
                unreadAudioText.setVisibility(View.GONE);
            }
        }
        audioContentView.setLayoutParams(params);

        TIMElem elem = msg.getElement();
        if (!(elem instanceof TIMSoundElem)) {
            return;
        }
        final TIMSoundElem soundElem = (TIMSoundElem) elem;
        int duration = (int) soundElem.getDuration();
        if (duration == 0) {
            duration = 1;
        }
        if (TextUtils.isEmpty(msg.getDataPath())) {
            getSound(msg, soundElem);
        }
        ViewGroup.LayoutParams audioParams = msgContentFrame.getLayoutParams();
        audioParams.width = AUDIO_MIN_WIDTH + ScreenUtil.getPxByDp(duration * 6);

        if (audioParams.width > AUDIO_MAX_WIDTH) {
            audioParams.width = AUDIO_MAX_WIDTH;
        }
        if(!msg.isSelf()){//这个是我增加的
            audioParams.width=ViewGroup.LayoutParams.WRAP_CONTENT;
            audioParams.width = AUDIO_MIN_WIDTH + ScreenUtil.getPxByDp(duration * 6);


        }

        msgContentFrame.setLayoutParams(audioParams);
        audioTimeText.setText(duration + "''");
        msgContentFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AudioPlayer.getInstance().isPlaying()) {
                    AudioPlayer.getInstance().stopPlay();
                    return;
                }
                if (TextUtils.isEmpty(msg.getDataPath())) {
                    ToastUtil.toastLongMessage("语音文件还未下载完成");
                    return;
                }
                if(msg.isSelf()){
                    audioPlayImage.setImageResource(R.drawable.play_voice_message);
                }else {
                    audioPlayImage.setImageResource(R.drawable.play_voice_unselfmessage);
                }

                final AnimationDrawable animationDrawable = (AnimationDrawable) audioPlayImage.getDrawable();
                animationDrawable.start();
                msg.setCustomInt(READ);

                unreadAudioText.setVisibility(View.GONE);
                AudioPlayer.getInstance().startPlay(msg.getDataPath(), new AudioPlayer.Callback() {
                    @Override
                    public void onCompletion(Boolean success) {
                        audioPlayImage.post(new Runnable() {
                            @Override
                            public void run() {
                                animationDrawable.stop();
                                if(msg.isSelf()){
                                    audioPlayImage.setImageResource(R.drawable.voice_msg_playing_3);
                                }else {
                                    audioPlayImage.setImageResource(R.drawable.play_voice_unselfmessage);
                                }

                            }
                        });
                    }
                });
            }
        });
    }

    private void getSound(final MessageInfo msgInfo, TIMSoundElem soundElemEle) {
        final String path = TUIKitConstants.RECORD_DOWNLOAD_DIR + soundElemEle.getUuid();
        File file = new File(path);
        if (!file.exists()) {
            soundElemEle.getSoundToFile(path, new TIMCallBack() {
                @Override
                public void onError(int code, String desc) {
                    TUIKitLog.e("getSoundToFile failed code = ", code + ", info = " + desc);
                }

                @Override
                public void onSuccess() {
                    msgInfo.setDataPath(path);
                }
            });
        } else {
            msgInfo.setDataPath(path);
        }
    }

}
