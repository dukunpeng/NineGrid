package com.mark.knowledge.ninegrid.bmp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mark.knowledge.ninegrid.R;
import com.mark.knowledge.ninegrid.apps.App;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark
 * @Date on 2019/4/19
 **/
public class Pane implements IBmp {

    private List<Bitmap> originalBitmaps;

    private List<Line> lines;

    private Bitmap mergedBitmap;
    private Bitmap mBufferBitmap;
    private Bitmap[] sBitmaps;

    private int c;
    private int v;
    private int h;
    private int p;
    private int w;
    private int sw;

    private Canvas mBufferCanvas;

    public Pane(int w, int p) {
        this.p = p;
        this.w = w;

    }


    public Pane addBitmap(Bitmap bitmap) {
        if (originalBitmaps == null) {
            originalBitmaps = new ArrayList<>();
        }
        originalBitmaps.add(bitmap);
        return this;
    }

    @Override
    public Bitmap mergedBitmap() {
        return mergedBitmap;
    }

    @Override
    public void merge() {
        init();
        // 创建空得背景bitmap
        // 生成画布图像
        mergedBitmap = Bitmap.createBitmap(w,
                w, Bitmap.Config.RGB_565);
        //填充颜色
        mergedBitmap.eraseColor(Color.parseColor("#d5d5d5"));

        final MyCanvas canvas = new MyCanvas(mergedBitmap);
        draw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {

        if (mBufferBitmap!=null){
            canvas.drawBitmap(mBufferBitmap,0,0,null);
        }

    }

    private void init() {
        int count = originalBitmaps.size();
        int v = 0;
        int h = 0;
        boolean isTrue = false;
        while (!isTrue) {
            v++;
            h = count / v;
            if (h <= v) {
                isTrue = true;

                if (h < v && v * h < count) {
                    h += 1;
                } else if (h == v && v * h < count && (v + 1) * h >= count) {
                    v += 1;
                }
            }
        }
        lines = new ArrayList<>();
        for (int i = 0; i < h; i++) {
            Line line = new Line();
            if (i == 0) {
                int d = v * h - count;
                line.setV(d == 0 ? v : v - d);
                line.setC(v);
            } else {
                line.setV(v);
                line.setC(v);
            }
            lines.add(line);
        }


        this.c = count;
        this.v = v;
        this.h = h;
        this.sw = (w - (p * (v + 1))) / v;
       // LogUtils.e("LineHelper" + "count==" + count + "  v==" + v + "  h==" + h + "   sw==" + sw);
        sBitmaps = new Bitmap[originalBitmaps.size()];

        int resId = R.mipmap.test;
        Bitmap b = null;
        for (int i = 0; i < originalBitmaps.size(); i++) {
            if (originalBitmaps.get(i) == null) {
                if (b == null) {
                    b = BitmapFactory.decodeResource(App.instance.getResources(), resId);
                }

                sBitmaps[i] = Bitmap.createScaledBitmap(b, w, w, false);
            } else {

                sBitmaps[i] = cutBitmap(createMatrixBitmap(originalBitmaps.get(i), w), w);
            }
        }
        if (mBufferBitmap == null) {
            mBufferBitmap = Bitmap.createBitmap(w,
                    w, Bitmap.Config.RGB_565);
            mBufferBitmap.eraseColor(Color.parseColor("#d5d5d5"));
            mBufferCanvas = new Canvas(mBufferBitmap);
        }
        Rect srcRect = new Rect(0, 0, w, w);
        int c = 0;
        for (int i = 0; i < lines.size(); i++) {
            final Line line = lines.get(i);
            int top = 0;
            if (h == v) {
                top = sw * i + p * (i + 1);
            } else {
                top = (w - h * sw - (h - 1) * p) / 2 + sw * i + p * i;
            }

            int bottom = top + sw;
            for (int j = 0; j < line.getV(); j++) {
                int left = 0, right = 0;
                if (line.getC() == line.getV()) {
                    left = sw * j + p * (j + 1);
                } else {
                    left = (w - line.getV() * sw - (line.getV() - 1) * p) / 2 + sw * j + p * j;
                }
                right = left + sw;
                Rect dstRect = new Rect(left, top, right, bottom);
                mBufferCanvas.drawBitmap(sBitmaps[c++], srcRect, dstRect, null);
            }
        }
    }

    private class MyCanvas extends Canvas {
        private Paint paint;
        private int count;

        public MyCanvas(Bitmap bitmap) {
            super(bitmap);
            init();
        }

        public void init() {
            count = 0;
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setTextSize(12f);
        }

        @Override
        public void drawBitmap(@NonNull Bitmap bitmap, @Nullable Rect src, @NonNull Rect dst, @Nullable Paint paint) {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            int width = dst.right - dst.left;
            if (width > w) {
                dst.left += (width - w) / 2;
            }
            if (width > h) {
                dst.top += (width - h) / 2;
            }
            super.drawBitmap(bitmap, src, dst, paint);
            //添加水印
//            count++;
//            drawText("第"+count+"张图\n"+"宽度："+(dst.right-dst.left)+"\n高度："+(dst.bottom-dst.top),dst.left+2,dst.centerY(),this.paint);
            /*if (bitmap!=null&&!bitmap.isRecycled()){
                bitmap.recycle();
            }*/
        }
    }

    private Bitmap cutBitmap(Bitmap bitmap, int width) {
        final int bWidth = bitmap.getWidth();
        final int bHeight = bitmap.getHeight();
        if (bHeight < width && bWidth < width) {
            return bitmap;
        }
        int x, y;
        int newW = 0, newH = 0;
        //横向
        if (bWidth > bHeight) {
            //完全够截取
            if (bHeight >= width) {
                x = (bWidth - width) / 2;
                y = (bHeight - width) / 2;
                newW = width;
                newH = width;

            } else {
                x = (bWidth - width) / 2;
                y = 0;
                newW = width;
                newH = bHeight;
            }

        } else {
            //完全够截取
            if (bWidth >= width) {
                x = 0;
                y = (bHeight - width) / 2;
                newW = width;
                newH = width;
            } else {
                x = 0;
                y = (bHeight - width) / 2;
                newW = bWidth;
                newH = width;
            }
        }
        return Bitmap.createBitmap(bitmap, x, y, newW, newH);
    }

    /**
     * 按规则缩放
     *
     * @param bitmap
     * @param width
     * @return
     */
    private Bitmap createMatrixBitmap(Bitmap bitmap, int width) {
        final int bWidth = bitmap.getWidth();
        final int bHeight = bitmap.getHeight();
       /* if (bHeight<width&&bWidth<width){
            if (bHeight<width){

            }

            return bitmap;
        }*/
        Matrix matrix = new Matrix();
        //横向
        if (bWidth > bHeight) {
            float scale = (float) width / (float) bHeight;
            matrix.postScale(scale, scale);

        } else {
            float scale = (float) width / (float) bWidth;
            matrix.postScale(scale, scale);
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bWidth, bHeight,
                matrix, true);
    }
}
