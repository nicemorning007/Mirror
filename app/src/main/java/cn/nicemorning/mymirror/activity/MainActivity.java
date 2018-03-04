package cn.nicemorning.mymirror.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import cn.nicemorning.mymirror.R;
import cn.nicemorning.mymirror.view.DrawView;
import cn.nicemorning.mymirror.view.FunctionView;
import cn.nicemorning.mymirror.view.PictureView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mSurface = (SurfaceView) findViewById(R.id.surface);
        mPicture = (PictureView) findViewById(R.id.picture);
        mFunction = (FunctionView) findViewById(R.id.function);
        mMinus = (ImageView) findViewById(R.id.minus);
        mSeekbar = (SeekBar) findViewById(R.id.seekbar);
        mAdd = (ImageView) findViewById(R.id.add);
        mBottomBar = (LinearLayout) findViewById(R.id.bottom_bar);
        mDrawGlasses = (DrawView) findViewById(R.id.draw_glasses);
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
                    mCamera = Camera.open();
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

}
