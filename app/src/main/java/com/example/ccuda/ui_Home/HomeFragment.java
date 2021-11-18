package com.example.ccuda.ui_Home;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.R;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.ui_Home.UploadCoupon;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.BitmapConverter;
import com.example.ccuda.db.CouponpageRequest;
import com.example.ccuda.db.PostRequest;
import com.example.ccuda.ui_Home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {
    public ArrayList<ItemData> cuItem = new ArrayList<>();
    public ArrayList<ItemData> gs25Item = new ArrayList<>();
    public ArrayList<ItemData> sevenItem = new ArrayList<>();
    Adapter adapter=new Adapter();
    ArrayList<CouponData> CouponArrayList = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference itemRef;
    ListView listView;

    SwipeRefreshLayout mSwipeRefreshLayout;//새로고침

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
    HomeActivity activity;
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        activity=(HomeActivity)getActivity();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        activity=null;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment1_home, container, false);
        listView=(ListView) v.findViewById(R.id.listView);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);//새로고침
        mSwipeRefreshLayout.setOnRefreshListener(this);

        load_item();
        load_couponlist(getActivity());

        FloatingActionButton addCuppon= (FloatingActionButton) v.findViewById(R.id.add_article);
        addCuppon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, new UploadCoupon()).commit();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.innerLayout, new UploadCoupon());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private ProductItemFragment productItemFragment = new ProductItemFragment();

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productData data = (productData) parent.getItemAtPosition(position);

                Bundle extras = new Bundle();
                /*extras.putString("photo", data.getImageurl());
                extras.putString("productname", data.getItem_name());
                extras.putString("store", data.getStorename());
                extras.putInt("price", data.getPrice());
                extras.putString("seller", data.getSeller_name());*/

                extras.putString("photo", data.getPhoto());
                extras.putString("productname", data.getProductName());
                extras.putString("store", data.getConvenientStore());
                extras.putInt("price", data.getPrice());
                extras.putString("seller", data.getSellerID());

                productItemFragment.setArguments(extras);

                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.innerLayout, productItemFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return v;
    }


    // 판매 쿠폰 리스트
    protected void load_couponlist(Context context){
        CouponArrayList.clear();
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("couponlist");
                    int length = jsonArray.length();

                    for(int i=0; i<length; i++){
                        CouponData couponData = new CouponData();
                        JSONObject object = jsonArray.getJSONObject(i);

                        couponData.setCoupon_id(Integer.parseInt(object.getString("coupon_id")));
                        couponData.setPrice(Integer.parseInt(object.getString("price")));       //쿠폰 가격
                        couponData.setExpiration_date(object.getString("expiration_date")); // 쿠폰 유효기간 "Y-m-d" 형식
                        couponData.setContent(object.getString("content")); // 글 내용
                        String storename = object.getString("storename");
                        storename = storename.toLowerCase();
                        couponData.setStorename(storename);
                        couponData.setPlustype(object.getString("category"));
                        couponData.setCouponimage( BitmapConverter.StringToBitmap(object.getString("original"))); // 쿠폰 이미지 (!= 상품 이미지)
                        couponData.setSeller_id(Long.parseLong(object.getString("seller_id"))); // 판매자 확인용 id
                        couponData.setPost_date(object.getString("post_date")); // "Y-m-d H:i:s" 형식
                        boolean isdeal;
                        if(object.getString("isdeal")=="1") // 거래 완료 여부
                            couponData.setIsdeal(true);
                        else
                            couponData.setIsdeal(false);
                        couponData.setSeller_name(object.getString("seller_nicname")); // 판매자 닉네임
                        couponData.setSeller_score(object.getString("seller_score")); // 판매자 평점

                        int item_id = Integer.parseInt(object.getString("item_id"));
                        if(storename == "cu"){
                            for(int j=0; j<cuItem.size(); j++ ){
                                if(cuItem.get(j).getItemid()==item_id){
                                    couponData.setItem_name(cuItem.get(j).getItemname());
                                    couponData.setCategory(cuItem.get(j).getCategory());
                                    couponData.setImage(cuItem.get(j).getImage());
                                    break;
                                }
                            }
                        }else if(storename == "gs25"){
                            for(int j=0; j<gs25Item.size(); j++ ){
                                if(gs25Item.get(j).getItemid()==item_id){
                                    couponData.setItem_name(gs25Item.get(j).getItemname());
                                    couponData.setCategory(gs25Item.get(j).getCategory());
                                    couponData.setPlustype(gs25Item.get(j).getPlustype());
                                    couponData.setImage(gs25Item.get(j).getImage());
                                    break;
                                }
                            }
                        }else {
                            for(int j=0; j<sevenItem.size(); j++ ){
                                if(sevenItem.get(j).getItemid()==item_id){
                                    couponData.setItem_name(sevenItem.get(j).getItemname());
                                    couponData.setCategory(sevenItem.get(j).getCategory());
                                    couponData.setPlustype(sevenItem.get(j).getPlustype());
                                    couponData.setImage(sevenItem.get(j).getImage());
                                    break;
                                }
                            }
                        }
                        CouponArrayList.add(couponData);
                        //adapter.addItem(couponData.getItem_name(), R.drawable.add, couponData.getStorename());
                    }
                    System.out.println("coupon list: "+CouponArrayList);
                    // TODO: Need adapter for couponArrayList(=판매글 리스트)


                    for(CouponData a:CouponArrayList){
                        adapter.addItem(a.getItem_name(), a.getImageurl(), a.getStorename());
                        System.out.println("itemname: "+a.getItem_name());
                    }

                    //adapter.addItem("물건1", R.drawable.add, "gs");
                    //adapter.addItem("물건2", R.drawable.add, "gs");
                    //adapter.addItem("물건3", R.drawable.add, "gs");
                    //adapter.addItem("물건4", R.drawable.add, "gs");
                    //adapter.addItem("물건5", R.drawable.add, "gs");
                    listView.setAdapter(adapter);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        CouponpageRequest couponpageRequest = new CouponpageRequest("couponlist", responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(couponpageRequest);
    }

    // 게시글 삭제
    protected void deletepost(Context context, int coupon_id){
        // 작성자: seller_id, 삭제할 쿠폰: coupon_id
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success=="success"){
                        // 포스팅 성공
                        Log.d("success","delete success");
                        int i;
                        for(i=0; i<CouponArrayList.size(); i++){
                            if(CouponArrayList.get(i).getCoupon_id() == coupon_id){
                                CouponArrayList.remove(i);
                                Toast.makeText(context,"해당 판매글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        PostRequest postRequest = new PostRequest("deletepost",SaveSharedPreference.getId(context),"", "",0, 0, "", "", "", coupon_id, responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
    public void load_item(){
        firebaseDatabase= FirebaseDatabase.getInstance();
        itemRef= firebaseDatabase.getReference();

        itemRef.child("item").child("cu").addChildEventListener(new ChildEventListener() {
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

        itemRef.child("item").child("gs25").addChildEventListener(new ChildEventListener() {
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

        itemRef.child("item").child("seven").addChildEventListener(new ChildEventListener() {
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

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //adapter.init();
                //adapter.notifyDataSetChanged();
                load_item();
                load_couponlist(getActivity());
                listView.setAdapter(adapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}