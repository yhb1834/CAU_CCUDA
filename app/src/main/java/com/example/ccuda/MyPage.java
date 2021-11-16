package com.example.ccuda;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.CartRequest;
import com.example.ccuda.ui_Cart.CartItemModel;
import com.example.ccuda.ui_Cart.addToCart;
import com.example.ccuda.ui_Home.Adapter;
import com.example.ccuda.ui_Home.UploadCoupon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyPage extends Fragment {
    private ArrayList<ItemData> cuItem = new ArrayList<>();
    private ArrayList<ItemData> gs25Item = new ArrayList<>();
    private ArrayList<ItemData> sevenItem = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference cartref;
    private TextView mypagetv;
    private ImageView mypageiv;
    Context context;

    Button upload;
    ListView cartList;
    Adapter adapter=new Adapter();
    Button addCart;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mypage, container, false);
        context = getActivity();
        mypagetv = view.findViewById(R.id.mypagetv);
        mypageiv = view.findViewById(R.id.mypageiv);
        upload=view.findViewById(R.id.mypage_coupon_upload);
        cartList=view.findViewById(R.id.cartList);

        /*set userinfo*/
        String email;
        if(SaveSharedPreference.getEmail(context).length() == 0)
            email = "kakao login user";
        else
            email = SaveSharedPreference.getEmail(context);
        mypagetv.setText(SaveSharedPreference.getNicname(context)+"\n"+email);
        Glide.with(this).load(SaveSharedPreference.getProfileimage(context)).into(mypageiv);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.innerLayout, new UploadCoupon());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        load_item();
        laod_MyCartList();
        addCart=view.findViewById(R.id.add_to_cart_button);
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.innerLayout, new addToCart());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    private void laod_MyCartList(){
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
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
                                    adapter.addItem(cuItem.get(j).getItemname(),cuItem.get(j).getImage(),storename);
                                }
                            }
                        }else if(storename.equals("gs25")){
                            for(int j=0; j<gs25Item.size(); j++){
                                if(Integer.toString(gs25Item.get(j).getItemid()).equals(item_id)){
                                    adapter.addItem(gs25Item.get(j).getItemname(),gs25Item.get(j).getImage(),storename);
                                }
                            }
                        }else {
                            for(int j=0; j<sevenItem.size(); j++){
                                if(Integer.toString(sevenItem.get(j).getItemid()).equals(item_id)){
                                    adapter.addItem(sevenItem.get(j).getItemname(),sevenItem.get(j).getImage(),storename);
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
                cartList.setAdapter(adapter);
            }
        };
        CartRequest cartRequest = new CartRequest("mycartlist", SaveSharedPreference.getId(context), 0,"",responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(cartRequest);
    }

    public void load_item(){
        firebaseDatabase= FirebaseDatabase.getInstance();
        cartref= firebaseDatabase.getReference();

        cartref.child("item").child("cu").addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ItemData itemData = dataSnapshot.getValue(ItemData.class);
                cuItem.add(itemData);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cartref.child("item").child("gs25").addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ItemData itemData = dataSnapshot.getValue(ItemData.class);
                gs25Item.add(itemData);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cartref.child("item").child("seven").addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ItemData itemData = dataSnapshot.getValue(ItemData.class);
                sevenItem.add(itemData);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
