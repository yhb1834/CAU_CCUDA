package com.example.ccuda.ui_Cart;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.R;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.BitmapConverter;
import com.example.ccuda.db.CartRequest;
import com.example.ccuda.ui_Home.HomeActivity;
import com.example.ccuda.ui_Home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    private Context context;
    ArrayList<CartItemModel> cartList=new ArrayList<>();
    ArrayList<ItemParccelable> sendToFramgent=new ArrayList<>();
    ArrayList<ItemData> cuItem = HomeActivity.cuItem;
    ArrayList<ItemData> gs25Item = HomeActivity.gs25Item;
    ArrayList<ItemData> sevenItem = HomeActivity.sevenItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        context = getContext();
        return inflater.inflate(R.layout.fragment1_cart, container, false);
    }



    // 장바구니 삭제 정보 db저장
    protected void removeFromcart(int item_id, String storename){
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    // 성공
                    Toast.makeText(context,"장바구니에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        CartRequest cartRequest = new CartRequest("removeFromcart", SaveSharedPreference.getId(context), item_id,storename, responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(cartRequest);
    }

    public void click_cart_item(Context context,CartItemModel item){
        Intent intent=new Intent(context,ItemPopUp.class);
        intent.putExtra("prodImage", item.getImageUrl());
        intent.putExtra("prodName", item.getText1());
        intent.putExtra("prodConv", item.getText2());
        intent.putExtra("prodId", item.getItemid());
        intent.putExtra("clickedWhere","MAIN_CART");
        //startActivity(intent);
        startForResult_main.launch(intent);
    }
    public void click_cart_item_in_all(Context context,CartItemModel item){
        Intent intent=new Intent(context,ItemPopUp.class);
        intent.putExtra("prodImage", item.getImageUrl());
        intent.putExtra("prodName", item.getText1());
        intent.putExtra("prodConv", item.getText2());
        intent.putExtra("prodId", item.getItemid());
        intent.putExtra("clickedWhere","ALL_CART");
        //startActivity(intent);
        startForResult_cart.launch(intent);
    }

    ActivityResultLauncher<Intent> startForResult_main=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==getActivity().RESULT_OK){
                }
            }
    );
    ActivityResultLauncher<Intent> startForResult_cart=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==getActivity().RESULT_OK){
                }
            }
    );


    public void load_MyCartList(){
        //cartList=new ArrayList<>();
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cartList.clear();
                try{
                    cartList.clear();
                    sendToFramgent.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("mycartlist");
                    int length = jsonArray.length();
                    for(int i=0; i<length; i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        String item_id = object.getString("item_id");
                        String storename = object.getString("storename");

                        if(storename.equals("cu")){
                            for(int j=0; j<cuItem.size(); j++){
                                if(Integer.toString(cuItem.get(j).getItemid()).equals(item_id)){
                                    cartList.add(new CartItemModel(cuItem.get(j).getImage(),cuItem.get(j).getItemname(),storename,cuItem.get(j).getItemid()));
                                    ItemParccelable item=new ItemParccelable();
                                    item.setProdName(cuItem.get(j).getItemname());
                                    item.setConvName(storename);
                                    item.setImgUrl(cuItem.get(j).getImage());
                                    sendToFramgent.add(item);
                                    break;
                                }

                            }
                        }else if(storename.equals("gs25")){
                            for(int j=0; j<gs25Item.size(); j++){
                                if(Integer.toString(gs25Item.get(j).getItemid()).equals(item_id)){
                                    cartList.add(new CartItemModel(gs25Item.get(j).getImage(),gs25Item.get(j).getItemname(),storename,gs25Item.get(j).getItemid()));
                                    ItemParccelable item=new ItemParccelable();
                                    item.setProdName(gs25Item.get(j).getItemname());
                                    item.setConvName(storename);
                                    item.setImgUrl(gs25Item.get(j).getImage());
                                    sendToFramgent.add(item);
                                    break;
                                }
                            }
                        }else {
                            for(int j=0; j<sevenItem.size(); j++){
                                if(Integer.toString(sevenItem.get(j).getItemid()).equals(item_id)){
                                    cartList.add(new CartItemModel(sevenItem.get(j).getImage(),sevenItem.get(j).getItemname(),storename,sevenItem.get(j).getItemid()));
                                    ItemParccelable item=new ItemParccelable();
                                    item.setProdName(sevenItem.get(j).getItemname());
                                    item.setConvName(storename);
                                    item.setImgUrl(sevenItem.get(j).getImage());
                                    sendToFramgent.add(item);
                                    break;
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


                //adapter.addItem("물건1", "", "gs");
                //adapter.addItem("물건2", "", "gs");
                //adapter.addItem("물건3", "", "gs");
                //adapter.addItem("물건4", "", "gs");
                //adapter.addItem("물건5", "", "gs");
                //cartList.setAdapter(adapterCart);
            }
        };
        CartRequest cartRequest = new CartRequest("mycartlist", SaveSharedPreference.getId(getActivity()), 0,"",responsListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(cartRequest);
    }
    static final String CHANNEL_ID="channelId";
    static final int notificationId=0;
    /*
    public void setNotification(String title, String content){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);

            Intent intent=new Intent(getContext(), HomeFragment.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent=PendingIntent.getActivity(getActivity(),0,intent,0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), String.valueOf(notificationChannel))
                    .setSmallIcon(R.drawable.coupon)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(getContext());
            notificationManagerCompat.notify(notificationId, builder.build());
        }

    }


     */
    public void setNotification(String title, String content) { //createNotificationChannel
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager=(NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel=
                    new NotificationChannel(
                            "alarm_channel_id",
                            "알람 테스트",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
            notificationChannel.setDescription("알람테스트");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent intent=new Intent(getContext(), HomeFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(getActivity(),0,intent,0);
        Notification notification=new NotificationCompat.Builder(getContext(),"alarm_channel_id")
                .setColor(Color.parseColor("#FFC107"))
                .setSmallIcon(R.drawable.coupon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        final  NotificationManager nm=(NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0,notification);

    }

}
