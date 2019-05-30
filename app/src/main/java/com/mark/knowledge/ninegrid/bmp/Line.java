package com.mark.knowledge.ninegrid.bmp;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mark
 * @Date on 2019/4/19
 **/
public class Line {

  private int v;
  private int c;

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }
  /* public static ThreadLocal<String> threadLocal = new ThreadLocal<String>(){

        @Override
        protected String initialValue() {
            return "sss";
        }
    };
    public static AtomicInteger atomicInteger = new AtomicInteger();
    public static int i = getI();
    public static int getI(){
       return atomicInteger.addAndGet(1);
    }
    public static void test(){
        for (int j = 0; j <10 ; j++) {
            LogUtils.e("iiii",i);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                threadLocal.set("name");
            }
        }).start();
        threadLocal.get();
    }
*/
}
