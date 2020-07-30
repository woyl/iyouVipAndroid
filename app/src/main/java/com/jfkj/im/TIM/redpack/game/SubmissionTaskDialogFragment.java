package com.jfkj.im.TIM.redpack.game;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.RedPackageCustom;
import com.jfkj.im.TIM.redpack.RedPackageUtil;
import com.jfkj.im.TIM.redpack.SendRedPackageBean;
import com.jfkj.im.event.TaskRedPackageEvent;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.interfaces.ResponListeners;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.BaseDialogFragment;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.LoadingDialog;
import com.jfkj.im.ui.dialog.PublicComCancelDialog;
import com.jfkj.im.ui.dialog.TipsBaseDialogFragment;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.Filter;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;

import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_TWO;

public class SubmissionTaskDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private ResponListeners<Integer,String> responListener;
    ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();
    private String nickName;
    private String urlHead;

    private TIMConversation conversation;
    private ChatInfo chatInfo;
    private String taskRedpackId;
    private LoadingDialog loadingDialog;

    public SubmissionTaskDialogFragment(boolean isWidth, int ori) {
        super(isWidth, ori);
    }

    public void setResponListener(ResponListeners<Integer,String> responListener) {
        this.responListener = responListener;
    }

    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_subm_task,null);
        view.findViewById(R.id.tv_open_album).setOnClickListener(this);
        view.findViewById(R.id.tv_send_red_pack).setOnClickListener(this);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        if (getArguments() != null) {
            chatInfo = (ChatInfo) getArguments().getSerializable("chat");
        }
        if (getArguments() != null) {
            taskRedpackId = getArguments().getString("taskRedpackId");
        }
        conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, chatInfo.getId());
        getData();
        loadingDialog = new LoadingDialog(getActivity(),R.style.dialog);
    }

    private void getData() {
        TIMUserProfile timUserProfile =  TIMFriendshipManager.getInstance().querySelfProfile();
        if (timUserProfile == null){
            TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
                @Override
                public void onError(int code, String desc) {

                }

                @Override
                public void onSuccess(TIMUserProfile timUserProfile) {
                    nickName = timUserProfile.getNickName();
                    urlHead = timUserProfile.getFaceUrl();
                }
            });
        }else {
            nickName = timUserProfile.getNickName();
            urlHead = timUserProfile.getFaceUrl();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_open_album:
                responListener.Rsps(1,taskRedpackId);
                dismiss();
                break;
            case R.id.tv_send_red_pack:
//                responListener.Rsp(2);
                choiceSendRedpackage();
                break;

        }
    }

    private void choiceSendRedpackage() {
        TipsBaseDialogFragment dialogFragment = new TipsBaseDialogFragment(true,
                Gravity.CENTER,"发送1000钻石红包到群里，可以替代完成游戏任务","取消","发红包",false);
        dialogFragment.setResponListener(new ResponListener<Boolean>() {
            @Override
            public void Rsp(Boolean s) {
                if (s){
                    loadingDialog.show();
                    new RedPackageUtil().taskRedpackage(chatInfo.getId(), taskRedpackId, new TIMValueCallBack<SendRedPackageBean>() {
                        @Override
                        public void onError(int code, String desc) {
                            loadingDialog.dismiss();
                            dismiss();
                        }

                        @Override
                        public void onSuccess(SendRedPackageBean sendRedPackageBean) {
                            loadingDialog.dismiss();
                            EventBus.getDefault().post(new TaskRedPackageEvent(true));
                            dismiss();
                        }
                    }, new TIMValueCallBack<Boolean>() {
                        @Override
                        public void onError(int code, String desc) {
                            dismiss();
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            loadingDialog.dismiss();
                            dismiss();
                            if (aBoolean){
                                PublicComCancelDialog dialog = new PublicComCancelDialog(true, Gravity.CENTER, "温馨提示",
                                        "您的余额不足，请及时充值！", "立即充值",true);
                                dialog.setRsp(new ResponListener<Boolean>() {
                                    @Override
                                    public void Rsp(Boolean s) {
                                        if (s){
                                            ChargeDialog mChargeDialog = new ChargeDialog(Objects.requireNonNull(getContext()),getActivity());
                                            mChargeDialog.show();
                                        }
                                    }
                                });
                                if (getFragmentManager() != null) {
                                    dialog.show(getFragmentManager(), "");
                                }
                            }

                        }
                    });
                }
            }
        });
        if (getFragmentManager() != null) {
            dialogFragment.show(getFragmentManager(),"");
        }
    }

    /**
     * 从相册中选择视频
     */
    private void choiceVideo() {
//        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(i, 66);
//        Album.video(getActivity()) // Image selection.
//                .multipleChoice()
//                .camera(true)
//                .columnCount(3)
//                .selectCount(1)
//                .checkedList(mAlbumFiles)
//                .filterSize(longFilter) // Filter the file size.
//                .filterMimeType(stringFilter) // Filter file format.
//                .afterFilterVisibility(true) // Show the filtered files, but they are not available.
//                .onResult(new Action<ArrayList<AlbumFile>>() {
//                    @Override
//                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
//                        for (AlbumFile albumFile : result) {
//                            Log.e("-->", albumFile.getPath());
//                            onSaveRecord(new File(albumFile.getPath()));
//                        }
//                    }
//                })
//                .onCancel(new Action<String>() {
//                    @Override
//                    public void onAction(@NonNull String result) {
//
//                    }
//                }).start();


        PictureSelector.create(getActivity())
                .openCamera(PictureMimeType.ofImage())
                .forResult(PictureConfig.CHOOSE_REQUEST);

    }


    private void onSaveRecord(File file) {
        Map<String,String> map = new HashMap<>();
        map.put("fileType","4");
        map.put("pathType","5");
        map.put(Utils.TOKEN,SPUtils.getInstance(getActivity()).getString(Utils.TOKEN));
        map.put("userId", SPUtils.getInstance(getActivity()).getString(Utils.USERID));
        OkHttpUtils.post()
                .tag(getActivity())
                .url(ApiStores.baseupload_url+"/file/uploadFiles")
                .headers(map)
                .addFile("file",file.getName(),file)
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ToastUtils.showShortToast(jsonObject.getString("message"));
                            if (jsonObject.getString("code").equals("200")) {
                                String url_video = jsonObject.getJSONObject("data").getString("fileUrls");
                                TimeTaskUtils.subGameVideo(taskRedpackId, new TIMValueCallBack<Boolean>() {
                                    @Override
                                    public void onError(int code, String desc) {

                                    }

                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        sendMess(url_video);
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void sendMess(String url_video) {
        TIMMessage message = buildSubVedio(url_video);
        conversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int code, String desc) {
                Log.e("msg", ",,,,,,code,,,,,,,,," + code);
                Log.e("msg", ",,,,,,desc,,,,,,,,," + desc);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                Log.e("msg", ",,,,,,,,timMessage,,,,,,," + timMessage.getSender());
                dismiss();
            }
        });
    }

    private TIMMessage buildSubVedio(String url_video) {
        Gson gson = new Gson();
        CustomMessage message = new CustomMessage();
        RedPackageCustom redPackageCustom = new RedPackageCustom();
        redPackageCustom.setMsgType(READ_PACKAGE_MSG_TYPE_TWO);
        redPackageCustom.setSendId(TIMManager.getInstance().getLoginUser());
        redPackageCustom.setCusType(READ_PACKAGE_CUS_TYPE_THREE);
        redPackageCustom.setSendType(READ_PACKAGE_MSG_TYPE_TWO);
        redPackageCustom.setAvatarUrl(urlHead);
        redPackageCustom.setSendName(nickName);
        redPackageCustom.setTaskUrl(url_video);
        message.setPackageCustom(redPackageCustom);
        String data = gson.toJson(message);
        MessageInfo info = MessageInfoUtil.buildTaskCustomMessage(data);
        return info.getTIMMessage();
    }


    Filter<Long> longFilter = new Filter<Long>() {
        @Override
        public boolean filter(Long attributes) {

            return false;
        }
    };
    Filter<String> stringFilter = new Filter<String>() {
        @Override
        public boolean filter(String attributes) {

            return false;
        }
    };

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 66 && resultCode == RESULT_OK && null != data) {
//            Uri selectedVideo = data.getData();
//            String[] filePathColumn = {MediaStore.Video.Media.DATA};
//
//            Cursor cursor = getActivity().getContentResolver().query(selectedVideo,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String path = cursor.getString(columnIndex);
//            File file = new File(path);
//            onSaveRecord(file);
//            cursor.close();
//        }
//    }
}
