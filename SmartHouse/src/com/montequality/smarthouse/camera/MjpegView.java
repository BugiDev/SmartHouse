package com.montequality.smarthouse.camera;

import java.io.IOException;

import com.montequality.smarthouse.R;
import com.montequality.smarthouse.tasks.MoveCameraTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MjpegView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "MjpegView";

    public final static int POSITION_UPPER_LEFT = 9;
    public final static int POSITION_UPPER_RIGHT = 3;
    public final static int POSITION_LOWER_LEFT = 12;
    public final static int POSITION_LOWER_RIGHT = 6;

    public final static int SIZE_STANDARD = 1;
    public final static int SIZE_BEST_FIT = 4;
    public final static int SIZE_FULLSCREEN = 8;

    private MjpegViewThread thread;
    private MjpegInputStream mIn = null;
    private boolean showFps = false;
    private boolean mRun = false;
    private boolean surfaceDone = false;
    private Paint overlayPaint;
    private int overlayTextColor;
    private int overlayBackgroundColor;
    private int ovlPos;
    private int dispWidth;
    private int dispHeight;
    private int displayMode;
    
    Rect arrowUpRect = new Rect();
    Rect arrowDownRect = new Rect();
    Rect arrowLeftRect = new Rect();
    Rect arrowRightRect = new Rect();
    
    String cameraHostURL;

    public class MjpegViewThread extends Thread {
        private SurfaceHolder mSurfaceHolder;
        private int frameCounter = 0;
        private long start;
        private Bitmap ovl;
        
        private Bitmap arrowUp;
        private Bitmap arrowDown;
        private Bitmap arrowLeft;
        private Bitmap arrowRight;
        

        public MjpegViewThread(SurfaceHolder surfaceHolder, Context context) {
            mSurfaceHolder = surfaceHolder;
        }

        private Rect destRect(int bmw, int bmh) {
            int tempx;
            int tempy;
            if (displayMode == MjpegView.SIZE_STANDARD) {
                tempx = (dispWidth / 2) - (bmw / 2);
                tempy = (dispHeight / 2) - (bmh / 2);
                return new Rect(tempx, tempy, bmw + tempx, bmh + tempy);
            }
            if (displayMode == MjpegView.SIZE_BEST_FIT) {
                float bmasp = (float) bmw / (float) bmh;
                bmw = dispWidth;
                bmh = (int) (dispWidth / bmasp);
                if (bmh > dispHeight) {
                    bmh = dispHeight;
                    bmw = (int) (dispHeight * bmasp);
                }
                tempx = (dispWidth / 2) - (bmw / 2);
                tempy = (dispHeight / 2) - (bmh / 2);
                return new Rect(tempx, tempy, bmw + tempx, bmh + tempy);
            }
            if (displayMode == MjpegView.SIZE_FULLSCREEN) {
                return new Rect(0, 0, dispWidth, dispHeight);
            }
            return null;
        }

        public void setSurfaceSize(int width, int height) {
            synchronized (mSurfaceHolder) {
                dispWidth = width;
                dispHeight = height;
            }
        }

        private Bitmap makeFpsOverlay(Paint p, String text) {
            Rect b = new Rect();
            p.getTextBounds(text, 0, text.length(), b);
            int bwidth = b.width() + 2;
            int bheight = b.height() + 2;
            Bitmap bm = Bitmap.createBitmap(bwidth, bheight, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bm);
            p.setColor(overlayBackgroundColor);
            c.drawRect(0, 0, bwidth, bheight, p);
            p.setColor(overlayTextColor);
            c.drawText(text, -b.left + 1, (bheight / 2) - ((p.ascent() + p.descent()) / 2) + 1, p);
            return bm;
        }
        
        private Bitmap makeArrowUpOverlay() {
          
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_up_camera);
            Bitmap mutableBitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
            Canvas c = new Canvas(mutableBitmap);
            Paint p = new Paint();
            c.drawRect(0, 0, bm.getWidth(), bm.getHeight(), p);
            arrowUpRect.left = mSurfaceHolder.getSurfaceFrame().width()/2 - bm.getWidth();
            arrowUpRect.top = 0;
            arrowUpRect.right = mSurfaceHolder.getSurfaceFrame().width()/2 + bm.getWidth();
            arrowUpRect.bottom = bm.getHeight();
            return bm;
        }
        
        private Bitmap makeArrowDownOverlay() {
            
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_down_camera);
            Bitmap mutableBitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
            Canvas c = new Canvas(mutableBitmap);
            Paint p = new Paint();
            c.drawRect(0, 0, bm.getWidth(), bm.getHeight(), p);
            arrowDownRect.left = mSurfaceHolder.getSurfaceFrame().width()/2 - bm.getWidth();
            arrowDownRect.top = mSurfaceHolder.getSurfaceFrame().height() - bm.getHeight();
            arrowDownRect.right = mSurfaceHolder.getSurfaceFrame().width()/2 + bm.getWidth();
            arrowDownRect.bottom = mSurfaceHolder.getSurfaceFrame().height();
            return bm;
        }
        
        private Bitmap makeArrowLeftOverlay() {
            
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_left_camera);
            Bitmap mutableBitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
            Canvas c = new Canvas(mutableBitmap);
            Paint p = new Paint();
            c.drawRect(0, 0, bm.getWidth(), bm.getHeight(), p);
            arrowLeftRect.left = 0;
            arrowLeftRect.top = mSurfaceHolder.getSurfaceFrame().height()/2 - bm.getHeight();
            arrowLeftRect.right = bm.getWidth();
            arrowLeftRect.bottom = mSurfaceHolder.getSurfaceFrame().height()/2 + bm.getHeight();
            return bm;
        }
        
        private Bitmap makeArrowRightOverlay() {
            
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_right_camera);
            Bitmap mutableBitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
            Canvas c = new Canvas(mutableBitmap);
            Paint p = new Paint();
            c.drawRect(0, 0, bm.getWidth(), bm.getHeight(), p);
            arrowRightRect.left = mSurfaceHolder.getSurfaceFrame().width() - bm.getWidth();
            arrowRightRect.top = mSurfaceHolder.getSurfaceFrame().height()/2 - bm.getHeight();
            arrowRightRect.right = mSurfaceHolder.getSurfaceFrame().width();
            arrowRightRect.bottom = mSurfaceHolder.getSurfaceFrame().height()/2 + bm.getHeight();
            return bm;
        }

        public void run() {
            start = System.currentTimeMillis();
            PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.DST_OVER);
            Bitmap bm;
            int width;
            int height;
            Rect destRect;
            Canvas c = null;
            Paint p = new Paint();
            String fps;
            while (mRun) {
                if (surfaceDone) {
                    try {
                        c = mSurfaceHolder.lockCanvas();
                        synchronized (mSurfaceHolder) {
                            try {
                                bm = mIn.readMjpegFrame();
                                destRect = destRect(bm.getWidth(), bm.getHeight());
                                c.drawColor(Color.BLACK);
                                c.drawBitmap(bm, null, destRect, p);
                                if (showFps) {
                                    p.setXfermode(mode);
                                    if (ovl != null) {
                                        height = ((ovlPos & 1) == 1) ? destRect.top : destRect.bottom - ovl.getHeight();
                                        width = ((ovlPos & 8) == 8) ? destRect.left : destRect.right - ovl.getWidth();
                                        c.drawBitmap(ovl, width, height, null);
                                        
                                        c.drawBitmap(arrowUp,mSurfaceHolder.getSurfaceFrame().width()/2 - arrowUp.getWidth()/2, 0, null);
                                        c.drawBitmap(arrowDown,mSurfaceHolder.getSurfaceFrame().width()/2 - arrowDown.getWidth()/2 , mSurfaceHolder.getSurfaceFrame().height()-arrowDown.getHeight(), null);
                                        c.drawBitmap(arrowLeft,0, mSurfaceHolder.getSurfaceFrame().height()/2-arrowLeft.getHeight()/2, null);
                                        c.drawBitmap(arrowRight,mSurfaceHolder.getSurfaceFrame().width() - arrowRight.getWidth(),mSurfaceHolder.getSurfaceFrame().height()/2-arrowRight.getHeight()/2, null);
                                    }
                                    p.setXfermode(null);
                                    frameCounter++;
                                    if ((System.currentTimeMillis() - start) >= 1000) {
                                        fps = String.valueOf(frameCounter) + " fps";
                                        frameCounter = 0;
                                        start = System.currentTimeMillis();
                                        ovl = makeFpsOverlay(overlayPaint, fps);
                                        
                                        arrowUp = makeArrowUpOverlay();
                                        arrowDown = makeArrowDownOverlay();
                                        arrowLeft = makeArrowLeftOverlay();
                                        arrowRight = makeArrowRightOverlay();
                                    }
                                }
                            } catch (IOException e) {
                                e.getStackTrace();
                                Log.d(TAG, "catch IOException hit in run", e);
                            }
                        }
                    } finally {
                        if (c != null) {
                            mSurfaceHolder.unlockCanvasAndPost(c);
                        }
                    }
                }
            }
        }
    }
    

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        


        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int x = (int) event.getX();
            int y = (int) event.getY();
            
            if(arrowUpRect.contains(x, y)){
                MoveCameraTask mcTask = new MoveCameraTask("up", cameraHostURL);
                mcTask.execute((Void)null);
            }else if(arrowDownRect.contains(x, y)){
                MoveCameraTask mcTask = new MoveCameraTask("down", cameraHostURL);
                mcTask.execute((Void)null);
            }else if(arrowLeftRect.contains(x, y)){
                MoveCameraTask mcTask = new MoveCameraTask("left", cameraHostURL);
                mcTask.execute((Void)null);
            }else if(arrowRightRect.contains(x, y)){
                MoveCameraTask mcTask = new MoveCameraTask("right", cameraHostURL);
                mcTask.execute((Void)null);
            }
            
            
          Log.d("touched", Integer.toString(x)+", "+ Integer.toString(y));
        }

        return true;
    }

    private void init(Context context) {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        thread = new MjpegViewThread(holder, context);
        setFocusable(true);
        overlayPaint = new Paint();
        overlayPaint.setTextAlign(Paint.Align.LEFT);
        overlayPaint.setTextSize(12);
        overlayPaint.setTypeface(Typeface.DEFAULT);
        overlayTextColor = Color.WHITE;
        overlayBackgroundColor = Color.RED;
        ovlPos = MjpegView.POSITION_LOWER_RIGHT;
        displayMode = MjpegView.SIZE_STANDARD;
        dispWidth = getWidth();
        dispHeight = getHeight();
    }

    public void startPlayback() {
        if (mIn != null) {
            mRun = true;
            thread.start();
        }
    }

    public void stopPlayback() {
        mRun = false;
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.getStackTrace();
                Log.d(TAG, "catch IOException hit in stopPlayback", e);
            }
        }
    }

    public MjpegView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void surfaceChanged(SurfaceHolder holder, int f, int w, int h) {
        thread.setSurfaceSize(w, h);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceDone = false;
        stopPlayback();
    }

    public MjpegView(Context context, String cameraHostURL) {
        super(context);
        this.cameraHostURL = cameraHostURL;
        init(context);
        
    }

    public void surfaceCreated(SurfaceHolder holder) {
        surfaceDone = true;
    }

    public void showFps(boolean b) {
        showFps = b;
    }

    public void setSource(MjpegInputStream source) {
        mIn = source;
        startPlayback();
    }

    public void setOverlayPaint(Paint p) {
        overlayPaint = p;
    }

    public void setOverlayTextColor(int c) {
        overlayTextColor = c;
    }

    public void setOverlayBackgroundColor(int c) {
        overlayBackgroundColor = c;
    }

    public void setOverlayPosition(int p) {
        ovlPos = p;
    }

    public void setDisplayMode(int s) {
        displayMode = s;
    }
}