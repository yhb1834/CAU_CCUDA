package com.example.ccuda;
//https://twinw.tistory.com/50
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.ccuda.ui_Home.HomeActivity;
import com.example.ccuda.ui_Home.HomeFragment;

public class CartItemMessagingService extends Service {
    NotificationManager notificationManager;
    Notification notification;
    ServiceThread thread;
    public static Context context;

    public CartItemMessagingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler=new myServiceHandler();
        thread=new ServiceThread(handler);
        thread.start();
        return  START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        thread.stopForever();
        thread=null;
        //super.onDestroy();
    }

    public void setNotification(String title, String content) { //createNotificationChannel
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager=(NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel=
                    new NotificationChannel(
                            "alarm_channel_id",
                            "알람 테스트",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
            notificationChannel.setDescription("알람테스트");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent intent=new Intent(getApplication(), HomeFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        Notification notification=new NotificationCompat.Builder(getApplication(),"alarm_channel_id")
                .setColor(Color.parseColor("#FFC107"))
                .setSmallIcon(R.drawable.coupon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        final  NotificationManager nm=(NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0,notification);

    }

    class myServiceHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            setNotification("장바구니에 넣어두신 물건이 들어왔어요!","확인해보세요");
        }
    }
}