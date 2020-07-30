package com.jfkj.im.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.utils.ExecutorServiceUtils;
import com.jfkj.im.utils.Utils;

import java.io.IOException;
import java.lang.reflect.Method;

import butterknife.BindView;
//在这里有两种情况  第一是自己主动取消通话
//二是对方突然拒绝通话
public class PreviewActivity extends BaseActivity implements View.OnClickListener, SurfaceHolder.Callback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MEDIA_TYPE_IMAGE = 1;

    private SurfaceView surfaceSv;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    // 0表示后置，1表示前置
    private int cameraPosition = 1;
    @BindView(R.id.trtc_iv_camera)
    ImageView trtc_iv_camera;
    @BindView(R.id.call_btn_refuse_red_iv)
    ImageView call_btn_refuse_red_iv;
    Intent getIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trtc_iv_camera.setOnClickListener(this);
        call_btn_refuse_red_iv.setOnClickListener(this);
        findById();
        initData();
        getIntent = getIntent();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    /**
     * 初始化view
     */
    private void findById() {

        surfaceSv = this.findViewById(R.id.surface_view);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("agreeaccept");
        intentFilter.addAction("enterroom");
        intentFilter.addAction("refuse");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * 初始化相关data
     */
    private void initData() {
        // 获得句柄
        mHolder = surfaceSv.getHolder();
//        // 添加回调
        mHolder.addCallback(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.checkCameraHardware(this) && (mCamera == null)) {
            // 打开camera
            mCamera = getCamera();
            if (mHolder != null) {
                setStartPreview(mCamera, mHolder);
            }
        }
    }

    private Camera getCamera() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            camera = null;

        }
        return camera;
    }

    @Override
    public void onPause() {
        super.onPause();
/**
 * 记得释放camera，方便其他应用调用
 */
        releaseCamera();
    }

    @Override
    public void onDestroy() {
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        releaseCamera();
        super.onDestroy();
    }

    /**
     * 释放mCamera
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();// 停掉原来摄像头的预览
            mCamera.release();
            mCamera = null;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trtc_iv_camera:
                // 切换前后摄像头
                int cameraCount = 0;
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数

                for (int i = 0; i < cameraCount; i++) {
                    Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
                    if (cameraPosition == 1) {
                        // 现在是后置，变更为前置
                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                            /**
                             * 记得释放camera，方便其他应用调用
                             */
                            releaseCamera();
                            // 打开当前选中的摄像头
                            mCamera = Camera.open(i);
                            // 通过surfaceview显示取景画面
                            setStartPreview(mCamera, mHolder);
                            cameraPosition = 0;
                            break;
                        }
                    } else {
                        // 现在是前置， 变更为后置
                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                            /**
                             * 记得释放camera，方便其他应用调用
                             */
                            releaseCamera();
                            mCamera = Camera.open(i);
                            setStartPreview(mCamera, mHolder);
                            cameraPosition = 1;
                            break;
                        }
                    }
                }
                break;
            case R.id.call_btn_refuse_red_iv:
 //这里是自己突然取消了通话

                getIntent.putExtra("videocalltype","取消通话");
                getIntent.putExtra(Utils.RECEIVEID,getIntent.getStringExtra(Utils.RECEIVEID));
                PreviewActivity.this.setResult(RESULT_OK, getIntent);

                finish();
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setStartPreview(mCamera, mHolder);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        setStartPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 当surfaceview关闭时，关闭预览并释放资源
        /**
         * 记得释放camera，方便其他应用调用
         */
        releaseCamera();
        holder = null;
        surfaceSv = null;
    }


    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * 设置camera显示取景画面,并预览
     *
     * @param camera
     */
    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            setDispaly(parameters, mCamera);
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    // 控制图像的正确显示方向
    private void setDispaly(Camera.Parameters parameters, Camera camera) {
        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
            setDisplayOrientation(camera, 90);
        } else {
            parameters.setRotation(90);
        }
    }

    // 实现的图像的正确显示
    private void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod(
                    "setDisplayOrientation", new Class[]{int.class});
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[]{i});
            }
        } catch (Exception e) {
            Log.e("Came_e", "图像出错");
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "agreeaccept":
                    String vroomid = intent.getStringExtra(Utils.VROOMID);
                    String userid = intent.getStringExtra(Utils.USERID);
                    String receiveid = intent.getStringExtra(Utils.RECEIVEID);
                    String uid = intent.getStringExtra(Utils.UID);
                    String msgid = intent.getStringExtra(Utils.MSGID);
                    if (Utils.netWork()) {

                        Intent intent_activity = new Intent(mActivity, TRTCVideoRoomActivity.class);
                        intent_activity.putExtra(Utils.VROOMID, vroomid);
                        intent_activity.putExtra(Utils.USERID, userid);
                        intent_activity.putExtra(Utils.RECEIVEID,getIntent.getStringExtra(Utils.RECEIVEID));
                        startActivity(intent_activity);
                        finish();
                    }
                    break;
                case "enterroom":
                    Intent intent_enterroom = new Intent(mActivity, TRTCVideoRoomActivity.class);
                    intent_enterroom.putExtra(Utils.VROOMID, intent.getStringExtra(Utils.VROOMID));
                    intent_enterroom.putExtra(Utils.USERID, intent.getStringExtra(Utils.RECEIVEID));
                    intent_enterroom.putExtra(Utils.RECEIVEID,getIntent.getStringExtra(Utils.RECEIVEID));
                    startActivity(intent_enterroom);
                    break;
                //这里是对方拒绝通话  9-6的通话
                case "refuse":
                    getIntent.putExtra("videocalltype","拒绝通话");
                    getIntent.putExtra(Utils.CHAT_HEAD_URL,intent.getStringExtra(Utils.CHAT_HEAD_URL));
                    PreviewActivity.this.setResult(RESULT_OK, getIntent);
                    finish();
                    break;
            }
        }
    };

}