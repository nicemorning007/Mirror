package cn.nicemorning.mymirror.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

import cn.nicemorning.mymirror.R;

/**
 * Created by Nicemorning on 04-Mar-18.
 */

public class PictureView extends android.support.v7.widget.AppCompatImageView {

    private int[] bitmap_ID_Array;
    private Canvas mCanvas;
    private int draw_Width;
    private int draw_Height;
    private Bitmap mBitmap;
    private int bitmap_index;

    public PictureView(Context context) {
        this(context, null);
    }

    public PictureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PictureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getTheWindowSize((Activity) context);
        init();
    }

    private void initBitmaps() {
        bitmap_ID_Array = new int[]{R.mipmap.mag_0001, R.mipmap.mag_0003, R.mipmap.mag_0005,
                R.mipmap.mag_0006, R.mipmap.mag_0007, R.mipmap.mag_0008, R.mipmap.mag_0009,
                R.mipmap.mag_0011, R.mipmap.mag_0012, R.mipmap.mag_0014};
    }

    private void init() {
        initBitmaps();
        bitmap_index = 0;
        mBitmap = Bitmap.createBitmap(draw_Width, draw_Height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.TRANSPARENT);
    }

    public void setPhotoFrame(int index) {
        bitmap_index = index;
        invalidate();
    }

    public int getPhotoFrame() {
        return bitmap_index;
    }

    private void getTheWindowSize(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        draw_Height = displayMetrics.heightPixels;
        draw_Width = displayMetrics.widthPixels;
        Log.d("屏幕宽度：", draw_Width + "\t\t屏幕高度：" + draw_Width);
    }

    private Bitmap getNewBitmap() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                bitmap_ID_Array[bitmap_index]).copy(Bitmap.Config.ARGB_8888, true);
        bitmap = Bitmap.createScaledBitmap(bitmap, draw_Width, draw_Height, true);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        @SuppressLint("DrawAllocation") Rect rect = new Rect(0, 0,
                this.getWidth(), this.getHeight());
        canvas.drawBitmap(getNewBitmap(), null, rect, null);
    }

}
