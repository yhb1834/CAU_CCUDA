package com.example.ccuda;

//import java.util.logging.Handler;
import android.os.Message;
import android.os.Handler;


public class ServiceThread extends Thread{
    Handler handler;
    boolean isRun=true;

    public ServiceThread(Handler handler){
        this.handler=handler;
    }
    public void stopForever(){
        synchronized (this){
            this.isRun=false;
        }
    }
    public void run(){
        while (isRun){
            handler.sendEmptyMessage(0);
            try {
                Thread.sleep(10000);
            }catch (Exception e){ }
        }
    }
}
