package com.mark.knowledge.ninegrid.bmp;

import android.graphics.Rect;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark
 * @Date on 2019/4/19
 **/
public class LineHelper {



    public static boolean checkCounts(int count){
        int v =0;int h =0;
        boolean isTrue = false;
        while (!isTrue){
            v++;
            if (Math.pow(v,2)<count&&count< Math.pow(v+1,2)){
                h = v;
                isTrue = true;
            }else{
                if (Math.pow(v,2)==count){
                    h =v;
                    isTrue = true;
                }else if (count== Math.pow(v+1,2)){
                    v++;
                    h = v;
                    isTrue = true;
                }else if (Math.pow(v,2)>count){
                    h = v-1;
                    isTrue = true;
                }
            }

        }
        for (int i = 0; i <v ; i++) {
            Line line = new Line();
            if (i==0){
                int d = v*h -count;
                line.setC(d==0?v:d);
                line.setV(v-d);
            }else{
                line.setV(v);
                line.setC(v);
            }
        }


      //  LogUtils.e("LineHelper"+"count=="+count+"  v=="+v+"  h=="+h);
        return true;

    }
}
