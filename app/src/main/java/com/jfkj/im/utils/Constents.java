package com.jfkj.im.utils;

import com.jfkj.im.view.TRTCVideoViewLayout;

public class Constents {

    /**
     * 1对1语音通话
     */
    public final static String ONE_TO_ONE_AUDIO_CALL = "1";
    /**
     * 1对多语音通话
     */
    public final static String ONE_TO_MULTIPE_AUDIO_CALL = "2";
    /**
     * 1对1视频通话
     */
    public final static String ONE_TO_ONE_VIDEO_CALL = "3";

    /**
     * 1对多视频通话
     */
    public final static String ONE_TO_MULTIPE_VIDEO_CALL = "4";

    /**
     * 实时语音通话消息描述内容
     */
    public final static String AUDIO_CALL_MESSAGE_DESC = "AUDIO_CALL_MESSAGE_DESC";
    /**
     * 实时视频通话消息描述内容
     */
    public final static String VIDEO_CALL_MESSAGE_DESC = "VIDEO_CALL_MESSAGE_DESC";

    /**
     * 实时语音通话消息拒接
     */
    public final static String AUDIO_CALL_MESSAGE_DECLINE_DESC = "AUDIO_CALL_MESSAGE_DECLINE_DESC";
    /**
     * 实时视频通话消息拒接
     */
    public final static String VIDEO_CALL_MESSAGE_DECLINE_DESC = "VIDEO_CALL_MESSAGE_DECLINE_DESC";

    /**
     * 悬浮窗与TRTCVideoActivity共享的视频View
     */
    public static TRTCVideoViewLayout mVideoViewLayout;

    /**
     * 悬浮窗是否开启
     */
    public static boolean isShowFloatWindow = false;

    /**
     * 语音通话开始计时时间（悬浮窗要显示时间在这里记录开始值）
     */
    public static long audioCallStartTime;

}
