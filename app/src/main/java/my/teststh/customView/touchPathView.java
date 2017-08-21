package my.teststh.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by jingwenpeng on 2017/8/21.
 */

public class touchPathView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean isDrawing;
    private Paint mPaint;
    private int x, y;
    private Path path;
    private boolean isClear = true;

    public touchPathView(Context context) {
        super(context);
        init();
    }

    public touchPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public touchPathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        path = new Path();
        path.moveTo(0, 200);
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        while (isDrawing) {
            draw();
        }
        long end = System.currentTimeMillis();
        if ((end - start) < 100) {
            try {
                Thread.sleep(100 - (end - start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (!isClear) {
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                mCanvas.drawPaint(mPaint);
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

                mCanvas.drawColor(Color.WHITE);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(Color.BLUE);
                mPaint.setStrokeWidth(10);
                mCanvas.drawPath(path, mPaint);
            } else {
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                mCanvas.drawPaint(mPaint);
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                mCanvas.drawColor(Color.WHITE);
                path.moveTo(0, 200);
            }
        } catch (Exception e) {

        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isClear = false;
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                isClear = true;
                break;
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
    }

}
