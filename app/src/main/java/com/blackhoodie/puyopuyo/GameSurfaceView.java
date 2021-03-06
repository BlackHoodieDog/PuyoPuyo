package com.blackhoodie.puyopuyo;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;

/**
 * 画面描画を担うクラス
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    /** スレッド */
    private Thread thread;

    /**
     * コンストラクタ
     * @param context
     */
    public GameSurfaceView(Context context){
        super(context);
        getHolder().addCallback(this);

        Game.createInstance();
        Game.getInstance().setView(this);
        Game.getInstance().setContext(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder){
        thread = new Thread(this);
        thread.start();

        Game.getInstance().initialize();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder){
        thread = null;
    }

    @Override
    public void run(){
        while(thread != null){
            try{
                Game.getInstance().run();
            }
            catch(InterruptedException exception){
                exception.printStackTrace();
            }

            render(getHolder());
        }
    }

    /**
     * ゲーム画面の描画を行う（GameクラスへCanvasを渡す）
     * @param surfaceHolder
     */
    private void render(SurfaceHolder surfaceHolder){
        Canvas canvas = surfaceHolder.lockCanvas();
        if(canvas == null){
            return;
        }

        Game.getInstance().render(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event){
        Game.getInstance().onTouchEvent(event);

        return true;
    }

}
