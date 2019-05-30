package com.mark.knowledge.ninegrid.bmp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * @author Mark
 * @Date on 2019/4/19
 **/
public interface IBmp {

    Bitmap mergedBitmap();
    void merge();
    void draw(Canvas canvas);


}
