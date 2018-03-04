package cn.nicemorning.mymirror.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.List;

import cn.nicemorning.mymirror.R;
import cn.nicemorning.mymirror.view.DrawView;
import cn.nicemorning.mymirror.view.FunctionView;
import cn.nicemorning.mymirror.view.PictureView;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback,
        SeekBar.OnSeekBarChangeListener, View.OnTouchListener, View.OnClickListener,
        FunctionView.OnFunctionViewItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SurfaceHolder holder;
    private SurfaceView mSurface;
    private PictureView mPicture;
    private FunctionView mFunction;
    private ImageView mMinus;
    private SeekBar mSeekbar;
    private ImageView mAdd;
    private LinearLayout mBottomBar;
    private DrawView mDrawGlasses;
    private boolean haveCamera;
    private int mCurrentCamIndex;
    private int ROTATE;
    private int minFocus;
    private int maxFocus;
    private int everyFocus;
    private int nowFocus;
    private Camera camera;
    private int frame_index;
    private int[] frame_index_ID;
    private static final int PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setViews();
        frame_index = 0;
        frame_index_ID = new int[]{R.mipmap.mag_0001, R.mipmap.mag_0003, R.mipmap.mag_0005,
                R.mipmap.mag_0006, R.mipmap.mag_0007, R.mipmap.mag_0008, R.mipmap.mag_0009,
                R.mipmap.mag_0011, R.mipmap.mag_0012, R.mipmap.mag_0014};
    }

    private void initView() {
        mSurface = findViewById(R.id.surface);
        mPicture = findViewById(R.id.picture);
        mFunction = findViewById(R.id.function);
        mMinus = findViewById(R.id.minus);
        mSeekbar = findViewById(R.id.seekbar);
        mAdd = findViewById(R.id.add);
        mBottomBar = findViewById(R.id.bottom_bar);
        mDrawGlasses = findViewById(R.id.draw_glasses);
    }

    private boolean checkCameraHardware() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private Camera openFrontFacingCameraGingerbread() {
        int cameraCount;
        Camera mCamera = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    Log.d(TAG, "注意!!!注意!!!注意!!!4");
                    mCamera = Camera.open();
                    Log.d(TAG, "注意!!!注意!!!注意!!!5");
                    mCurrentCamIndex = camIdx;
                } catch (RuntimeException e) {
                    Log.d(TAG, "相机打开失败" + e.getLocalizedMessage());
                }
            }
        }
        return mCamera;
    }

    private void setCamerDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degress = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degress = 0;
                break;
            case Surface.ROTATION_90:
                degress = 90;
                break;
            case Surface.ROTATION_180:
                degress = 180;
                break;
            case Surface.ROTATION_270:
                degress = 270;
                break;
            default:
                break;
        }
        int result = 0;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degress) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degress + 360) % 360;
        }
        ROTATE = result + 180;
        camera.setDisplayOrientation(result);
    }

    private void setCamera() {
        if (checkCameraHardware()) {
            camera = openFrontFacingCameraGingerbread();
            camera.setErrorCallback(new Camera.ErrorCallback() {
                @Override
                public void onError(int error, Camera camera) {
                    switch (error) {
                        case Camera.CAMERA_ERROR_SERVER_DIED:
                            Log.d("cwxin", "to do something");
                            break;
                        case Camera.CAMERA_ERROR_UNKNOWN:
                            Log.d("cwxin", "to do something");
                            break;
                    }
                }
            });
            setCamerDisplayOrientation(this, mCurrentCamIndex, camera);
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPictureFormat(ImageFormat.JPEG);
            List<String> list = parameters.getSupportedFocusModes();
            for (String string : list) {
                Log.d(TAG, "支持的对焦模式" + string);
            }
            List<Camera.Size> pictureList = parameters.getSupportedPictureSizes();
            List<Camera.Size> previewList = parameters.getSupportedPreviewSizes();
            parameters.setPictureSize(pictureList.get(0).width, pictureList.get(0).height);
            parameters.setPreviewSize(pictureList.get(0).width, pictureList.get(0).height);
            minFocus = parameters.getZoom();
            maxFocus = parameters.getMaxZoom();
            everyFocus = 1;
            nowFocus = minFocus;
            mSeekbar.setMax(maxFocus);
            Log.d(TAG, "当前镜头距离" + minFocus + "\t\t获取最大距离" + maxFocus);
            camera.setParameters(parameters);
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("surfaceCreated", "绘制开始");
        try {
            setCamera();
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            camera.release();
            camera = null;
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("surfaceChanged", "绘制改变");
        try {
            camera.stopPreview();
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("surfaceDestroyed", "绘制结束");
        toRelease();
    }

    private void toRelease() {
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private void setViews() {
        holder = mSurface.getHolder();
        holder.addCallback(this);
        mAdd.setOnTouchListener(this);
        mMinus.setOnTouchListener(this);
        mSeekbar.setOnSeekBarChangeListener(this);
        mFunction.setOnFunctionViewItemClickListener(this);
    }

    private void setZoomValues(int want) {
        Camera.Parameters parameters = camera.getParameters();
        mSeekbar.setProgress(want);
        parameters.setZoom(want);
        camera.setParameters(parameters);
    }

    private int getZoomValues() {
        return camera.getParameters().getZoom();
    }

    private void addZoomValues() {
        if (nowFocus > maxFocus) {
            Log.d(TAG, "超出最大焦距");
        } else if (nowFocus == maxFocus) {
        } else {
            setZoomValues(getZoomValues() + everyFocus);
        }
    }

    private void minusZoomValues() {
        if (nowFocus < 0) {
            Log.d(TAG, "小于最大焦距");
        } else if (nowFocus == 0) {
        } else {
            setZoomValues(getZoomValues() - everyFocus);
        }
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Camera.Parameters parameters = camera.getParameters();
        nowFocus = progress;
        parameters.setZoom(progress);
        camera.setParameters(parameters);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.add:
                addZoomValues();
                break;
            case R.id.minus:
                minusZoomValues();
                break;
            case R.id.picture:
                //添加手势识别
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void hint() {
        startActivity(new Intent(this, HintActivity.class));
    }

    @Override
    public void choose() {
        Intent intent = new Intent(this, PhotoFrameActivity.class);
        startActivityForResult(intent, PHOTO);
        Toast.makeText(this, "选择！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void down() {

    }

    @Override
    public void up() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Return:" + resultCode + "\t\t Request:" + requestCode);
        if (resultCode == RESULT_OK && requestCode == PHOTO) {
            int position = data.getIntExtra("POSITION", 0);
            frame_index = position;
            Log.d(TAG, "Mirror frame:" + position);
        }
    }
}
