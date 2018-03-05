package cn.nicemorning.mymirror.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cn.nicemorning.mymirror.R;

/**
 * Created by Nicemorning on 04-Mar-18.
 */

public class DrawView extends View {
    private Canvas canvas;
    private Path path;
    private Paint paint;
    private float moveX, moveY;
    private Bitmap bitmapT;
    private Bitmap bitmap;
    private volatile boolean complete = false;
    private OnCaYiCaCompleteListener caYiCaCompleteListener;

    public void setCaYiCaCompleteListener(OnCaYiCaCompleteListener listener) {
        this.caYiCaCompleteListener = listener;
    }

    public DrawView(Context context) {
        this(context, null);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.glasses).copy(
                Bitmap.Config.ARGB_8888, true);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(100);
        path = new Path();
    }

    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        if (!complete) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawBitmap(bitmap, 0, 0, null);
            canvas.drawPath(path, paint);
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
        if (complete) {
            if (caYiCaCompleteListener != null) {
                caYiCaCompleteListener.complete();
                setEndValues();
            }
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        bitmapT = Bitmap.createScaledBitmap(bitmap, width, height, true);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        canvas = new Canvas(bitmapT);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    public interface OnCaYiCaCompleteListener {
        void complete();
    }

    public void setEndValues() {
        moveX = 0;
        moveY = 0;
        path.reset();
        complete = false;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int w = getWidth();
            int h = getHeight();
            float wipeArea = 0;
            float totalArea = w * h;
            Bitmap bitmap = bitmapT;
            int[] pixels = new int[w * h];
            bitmap.getPixels(pixels, 0, w, 0, 0, w, h);
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    int index = i + j * w;
                    if (pixels[index] == 0) {
                        wipeArea++;
                    }
                }
            }
            if (wipeArea > 0 && totalArea > 0) {
                int percent = (int) (wipeArea * 100 / totalArea);
                Log.d("DrawView", percent + " ");
                if (percent > 50) {
                    complete = true;
                    postInvalidate();
                }
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveX = x;
                moveY = y;
                path.moveTo(moveX, moveY);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) Math.abs(moveX - x);
                int dy = (int) Math.abs(moveY - y);
                if (dx > 1 || dy > 1) {
                    path.quadTo(x, y, (moveX + x) / 2, (moveY + y) / 2);
                }
                moveX = x;
                moveY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (!complete) {
                    new Thread(runnable).start();
                }
                break;

            default:
                break;
        }
        if (!complete) {
            invalidate();
        }
        return true;
    }

}
