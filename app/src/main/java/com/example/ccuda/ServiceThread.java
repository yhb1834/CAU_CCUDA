package com.example.ccuda;

//import java.util.logging.Handler;
import android.os.Message;
import android.os.Handler;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.SideMenu.AdapterForUploadarticles;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.MypageRequest;
import com.example.ccuda.db.NotificationRequest;

import org.json.JSONArray;
import org.json.JSONObject;


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
        while(isRun){
            Response.Listener<String> responsListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if(success){
                            //새로올라온 품목 중 장바구니에 있는 경우
                            handler.sendEmptyMessage(0);
                        }
                        else{
                            //없는 경우
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            NotificationRequest notificationRequest = new NotificationRequest(SaveSharedPreference.getId(CartItemMessagingService.context),responsListener);
            RequestQueue queue = Volley.newRequestQueue(CartItemMessagingService.context);
            queue.add(notificationRequest);

            try{
                Thread.sleep(10000);
            }catch (Exception e){

            }
        }

    }
}
