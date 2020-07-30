package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.ui.activity.MainActivity;

import java.io.IOException;
import java.lang.reflect.Method;

import butterknife.BindView;

public class PreviewDialog extends Dialog implements View.OnClickListener, SurfaceHolder.Callback {
    private static final String TAG = PreviewDialog.class.getSimpleName();
    private static final int MEDIA_TYPE_IMAGE = 1;
    private SurfaceView surfaceSv;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    // 0表示后置，1表示前置
    private int cameraPosition = 1;
    @BindView(R.id.trtc_iv_camera)
    TextView trtc_iv_camera;
    @BindView(R.id.call_btn_refuse_red_iv)
    TextView call_btn_refuse_red_iv;
    Context context;
    OnClick onClick;
    public PreviewDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
        this.context=context;
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkCameraHardware(App.getAppContext()) && (mCamera == null)) {
            // 打开camera
            mCamera = getCamera();
            if (mHolder != null) {
                setStartPreview(mCamera, mHolder);
            }
        }
        setContentView(R.layout.dialog_preview);
        trtc_iv_camera=findViewById(R.id.trtc_iv_camera);
        call_btn_refuse_red_iv=findViewById(R.id.call_btn_refuse_red_iv);
        surfaceSv=findViewById(R.id.surface_view);
        trtc_iv_camera.setOnClickListener(this);
        trtc_iv_camera.setOnClickListener(this);
        call_btn_refuse_red_iv.setOnClickListener(this);
         mHolder = surfaceSv.getHolder();
         mHolder.addCallback(this);
    }
   public interface  OnClick{
        public void onclickcancel();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
                dismiss();
                onClick.onclickcancel();

                break;
        }
    }

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
    public void dismiss() {
        super.dismiss();
        releaseCamera();
    }
    public boolean showSystemDialog() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(context)) {
                ToastUtil.toastLongMessage("请打开设置“允许显示在其他应用的上层”选项");
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return false;
            } else {
                // Android6.0以上
                if (!isShowing()) {
                    super.show();
                    return true;
                }
            }
        } else {
            // Android6.0以下，不用动态声明权限
            if (!isShowing()) {
               show();
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();// 停掉原来摄像头的预览
            mCamera.release();
            mCamera = null;
        }

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setStartPreview(mCamera, mHolder);
    }
    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);

    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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
        releaseCamera();
        holder = null;
        surfaceSv = null;
    }
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
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
}
