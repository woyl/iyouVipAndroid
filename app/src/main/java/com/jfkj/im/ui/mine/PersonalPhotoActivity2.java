package com.jfkj.im.ui.mine;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jfkj.im.BuildConfig;
import com.jfkj.im.R;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.MessageEvent;
import com.jfkj.im.entity.ResponseFileUrl;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.mine.EditUserInfoPresenter;
import com.jfkj.im.mvp.mine.EditUserInfoView;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.retrofit.ApiUploadClient;
import com.jfkj.im.utils.PermissionsCheckUtils;
import com.jfkj.im.utils.StatusBarUtil;
import com.jfkj.im.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

/**
 * <pre>
 * Description: 个人头像页
 * @author :   ys
 * @date :         2019/12/3
 * </pre>
 */
public class PersonalPhotoActivity2 extends BaseActivity<EditUserInfoPresenter> implements EditUserInfoView, View.OnClickListener {
    public static final int PERMISSION_EXTERNAL_STORAGE = 0xf1;
    public static final int PERMISSION_GALLERY_CODE = 0xf2;
    public final static int CROP_REQUEST_CODE = 0xf3;
    // 拍照回传码
    public final static int CAMERA_REQUEST_CODE = 0xf1;
    // 相册选择回传吗
    public final static int GALLERY_REQUEST_CODE = 0xf2;
    @BindView(R.id.iv_title_back)
    AppCompatImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    AppCompatTextView mTvTitle;
    @BindView(R.id.iv_title_right)
    AppCompatImageView mIvTitleRight;
    @BindView(R.id.iv_user_photo)
    ImageView mIvUserPhoto;
    private BottomSheetDialog mDialog;
    private Uri mPhotoUri = null;
    private Uri mCutUri = null;
    private String path;
    private File photoFile;
    private String headUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_photo;
    }

    @Override
    public EditUserInfoPresenter createPresenter() {
        return new EditUserInfoPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        StatusBarUtil.setStatusBarDarkTheme(this,false);
        mTvTitle.setText("个人头像");
        Glide.with(this).load(UserInfoManger.getMineUserHeadUrl()).into(mIvUserPhoto);
    }

    @OnClick({R.id.iv_title_back, R.id.iv_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.iv_title_right:
                showPhotoDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 底部弹窗选择dialog
     */
    private void showPhotoDialog() {
        mDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_photo, null);
        mDialog.setContentView(view);
        mDialog.show();
        view.findViewById(R.id.tv_take_photo).setOnClickListener(this);
        view.findViewById(R.id.tv_from_photo).setOnClickListener(this);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //拍照
            case R.id.tv_take_photo:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PERMISSION_DENIED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_EXTERNAL_STORAGE);
                    }
                } else {
                    takePhoto();
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                break;
            //选择相册
            case R.id.tv_from_photo:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_GALLERY_CODE);
                    } else {
                        choosePhoto();
                    }
                } else {
                    choosePhoto();
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                break;
            case R.id.tv_cancel:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                break;
        }
    }

    /**
     * 相册选择
     */
    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), GALLERY_REQUEST_CODE);
    }

    /**
     * 拍照
     */
    private void takePhoto() {

        //跳转到系统的拍照界面
        Intent intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String tempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
        Log.d("@@@", "tempPhotoPath===" + tempPhotoPath);
        photoFile = new File(tempPhotoPath);
        if (!photoFile.exists()) {
            photoFile.mkdirs();
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            mPhotoUri = FileProvider.getUriForFile(PersonalPhotoActivity2.this, BuildConfig.APPLICATION_ID + ".fileProvider", photoFile);
        } else {
            mPhotoUri = Uri.fromFile(photoFile);
        }

        //下面这句指定调用相机拍照后的照片存储的路径
        intentTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        startActivityForResult(intentTakePhoto, CAMERA_REQUEST_CODE);
        Log.d("@@@", "mPhotoUri====:" + mPhotoUri);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    path = photoFile.getAbsolutePath();
                    try {
                        FileInputStream stream = new FileInputStream(photoFile);

                        Bitmap bitmap = BitmapFactory.decodeStream(stream);
                        mIvUserPhoto.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
//                    uploadImage(path);

                } else {
                    mDialog.dismiss();
                    mPhotoUri = null;
                }

                break;
            case GALLERY_REQUEST_CODE:
                //相册
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
//                        uploadImage(uri);
                        Glide.with(this).load(uri).into(mIvUserPhoto);
                    }
                }

                break;
            //调用剪裁后返回
            case CROP_REQUEST_CODE:

                try {
                    //获取裁剪后的图片，并显示出来
                    Bitmap bitmap = BitmapFactory.decodeStream(
                            getContentResolver().openInputStream(mCutUri));
                    //                    if (mListener != null) {
                    //                        mListener.onTakePhoto(bitmap);
                    //                    }

                    //                    deleteUri(getContext(), mCutUri);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                }
                break;

        }
    }

    private Bitmap getBitmapUri(Uri uri) {
        InputStream input = null;
        try {
            input = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //这一段代码是不加载文件到内存中也得到bitmap的真是宽高，主要是设置inJustDecodeBounds为true
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;//不加载到内存
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;

        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        try {
            input = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return compressImage(bitmap);//再进行质量压缩
    }

    public Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            if (options<=0)
                break;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private void uploadImage(String path) {
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        ApiUploadClient.getmRetrofit().create(ApiStores.class)
                .uploadSingleFile("1", "1", part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new HttpErrorMsgObserver<ResponseFileUrl>() {
                    @Override
                    public void onNext(ResponseFileUrl responseFileUrl) {
                        if (responseFileUrl.isSuccess()) {
                            Map<String, String> map = new HashMap<>();
                            headUrl = responseFileUrl.getData().getFileUrl();
                            map.put("portrait", headUrl);
                            mvpPresenter.editInfo(map, "USER_HEADER");
                        }
                    }
                });
    }

    private void cropPhoto(Uri photoUri) {
        if (photoUri == null) {
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_EXTERNAL_STORAGE:
                if (PermissionsCheckUtils.hasAllPermissionsGranted(grantResults)) {
                    takePhoto();
                } else {
                    mDialog.dismiss();
                }
                break;
            case PERMISSION_GALLERY_CODE:
                if (PermissionsCheckUtils.hasAllPermissionsGranted(grantResults)) {
                    choosePhoto();
                } else {
                    mDialog.dismiss();
                }
                break;
        }
    }

    @Override
    public void showEditSuccess(BaseResponse baseResponse, String type) {
        Glide.with(this).load(headUrl).into(mIvUserPhoto);
        UserInfoManger.saveMineUserHeadState(headUrl);
        setResult(RESULT_OK);
        EventBus.getDefault().post(new MessageEvent(Utils.SET_USER_HEADER,""));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
