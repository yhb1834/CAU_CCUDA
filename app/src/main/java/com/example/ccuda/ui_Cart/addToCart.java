package com.example.ccuda.ui_Cart;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.CartRequest;
import com.example.ccuda.ui_Home.Adapter;
import com.example.ccuda.ui_Home.HomeActivity;
import com.example.ccuda.ui_Home.UploadCoupon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.select.Elements;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class addToCart extends CartFragment {
    SearchView searchView;
    RecyclerView recyclerView;
    TextView textView;
    ArrayList<ItemData> cuItem = HomeActivity.cuItem;
    ArrayList<ItemData> gs25Item = HomeActivity.gs25Item;
    ArrayList<ItemData> sevenItem = HomeActivity.sevenItem;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference itemRef;

    CartItemAdapter adapter;
    ArrayList<CartItemModel> itemList=new ArrayList<>();
    ArrayList<CartItemModel> cartList=new ArrayList<>();
    Adapter adapterCart=new Adapter();
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView[] imageViews=new ImageView[5];
    int i;
    TextView emptyCartText;
    Button viewAll;
    ArrayList<ItemParccelable> sendToFramgent=new ArrayList<>();
    //ArrayList<> cartItemImgs=new ArrayList<>(); // 장바구니에 추가된 사진 list


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //System.out.println("onCreate");
        fillData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_cartlist, container, false);
        searchView=view.findViewById(R.id.searchView);
        recyclerView=view.findViewById(R.id.recyclerView);
        //System.out.println("onCreateView");
        setUpRecyclerView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });


        load_MyCartList();
        //System.out.println("cart item: "+cartList);
        emptyCartText=view.findViewById(R.id.emptyCartText);
        viewAll=view.findViewById(R.id.view_all);

        imageViews[0]=view.findViewById(R.id.image1);
        imageViews[1]=view.findViewById(R.id.image2);
        imageViews[2]=view.findViewById(R.id.image3);
        imageViews[3]=view.findViewById(R.id.image4);
        imageViews[4]=view.findViewById(R.id.image5);
        //ImageView[] imageViews=new ImageView[5];

        if(cartList.size()!=0){
            emptyCartText.setText("");
            emptyCartText.setTextSize(0);
            viewAll.setVisibility(View.VISIBLE);
        }
        if(cartList.size()==0){
            viewAll.setVisibility(View.INVISIBLE);
        }
        if(cartList.size()<=5){
            for (i=0;i<cartList.size();i++){
                CartItemModel item=cartList.get(i);
                Glide.with(getActivity()).load(cartList.get(i).getImageUrl()).into(imageViews[i]);
                imageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click_cart_item(getContext(),item);
                    }
                });
            }
        }else {
            for (i=0;i<5;i++){
                CartItemModel item=cartList.get(i);
                Glide.with(getActivity()).load(cartList.get(i).getImageUrl()).into(imageViews[i]);
                imageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click_cart_item(getContext(),item);

                    }
                });
            }
        }

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result=new Bundle();
                result.putParcelableArrayList("itemlist", sendToFramgent);
                //result.put
                getParentFragmentManager().setFragmentResult("requestKey", result);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AllCartFragment fragment=new AllCartFragment();
                fragment.setArguments(result);
                transaction.replace(R.id.innerLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }

    public void fillData() {
        //itemList = new ArrayList<>(); //샘플테이터
        for(int i=0;i<cuItem.size();i++){
            itemList.add(new CartItemModel(cuItem.get(i).getImage(), cuItem.get(i).getItemname(), cuItem.get(i).getStorename(), cuItem.get(i).getItemid()));

        }
    }

    public ArrayList<CartItemModel> getResult(){
        ArrayList<CartItemModel> itemList=new ArrayList<>();
        for(int i=0;i<cuItem.size();i++){
            itemList.add(new CartItemModel(cuItem.get(i).getImage(), cuItem.get(i).getItemname(), cuItem.get(i).getStorename(), cuItem.get(i).getItemid()));
            if(i!=cuItem.size()-1){
                //sb.append("\n");
            }
        }
        return itemList;
    }


    public ArrayList<CartItemModel> search(String query){
        ArrayList<CartItemModel> itemList=new ArrayList<>();
        for(int i=0;i<cuItem.size();i++){
            String item=cuItem.get(i).getItemname();
            if (item.toLowerCase().contains(query.toLowerCase())){
                itemList.add(new CartItemModel(cuItem.get(i).getImage(), cuItem.get(i).getItemname(), cuItem.get(i).getStorename(), cuItem.get(i).getItemid()));
            }
        }
        return itemList;
    }




    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        //itemList = new ArrayList<>(); //샘플테이터
        //fillData();
        adapter = new CartItemAdapter(itemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL); //밑줄
        recyclerView.addItemDecoration(dividerItemDecoration);


        //itemList.add(new CartItemModel(R.drawable.add, "One", "Ten"));
        //itemList.add(new CartItemModel(R.drawable.add, "Two", "Eleven"));
        //itemList.add(new CartItemModel(R.drawable.add, "Three", "Twelve"));
        //itemList.add(new CartItemModel(R.drawable.add, "Four", "Thirteen"));
        //itemList.add(new CartItemModel(R.drawable.add, "Five", "Fourteen"));
        //itemList.add(new CartItemModel(R.drawable.add, "Six", "Fifteen"));
        //itemList.add(new CartItemModel(R.drawable.add, "Seven", "Sixteen"));
        //itemList.add(new CartItemModel(R.drawable.add, "Eight", "Seventeen"));
        //itemList.add(new CartItemModel(R.drawable.add, "Nine", "Eighteen"));


        //데이터셋변경시
        //adapter.dataSetChanged(exampleList);

        //어댑터의 리스너 호출
        adapter.setOnClickListener(this::onItemClicked);
    }





    /****************************************************
     onCreateOptionsMenu SearchView  기능구현
     ***************************************************/


    /****************************************************
     리사이클러뷰 클릭이벤트 인터페이스 구현
     ***************************************************/

    public void onItemClicked(int position) {
        //Toast.makeText(getContext(), "" +position, Toast.LENGTH_SHORT).show();
        final CartItemModel cartItemModel = itemList.get(position);
        String storename = cartItemModel.getText2();
        int item_id = cartItemModel.getItemid();

        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        // 성공
                        Log.d("success","query success");
                        Toast.makeText(getActivity(),"장바구니 추가", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // 실패
                        Log.d("success","already in cart");
                        Toast.makeText(getActivity(),"같은 상품이 이미 장바구니에 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        CartRequest cartRequest = new CartRequest("addTocart", SaveSharedPreference.getId(getActivity()), item_id,storename, responsListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(cartRequest);
    }


    private void load_MyCartList(){
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
                                    cartList.add(new CartItemModel(cuItem.get(j).getImage(),cuItem.get(j).getItemname(),storename,cuItem.get(j).getItemid()));
                                    ItemParccelable item=new ItemParccelable();
                                    item.setProdName(cuItem.get(j).getItemname());
                                    item.setConvName(storename);
                                    item.setImgUrl(cuItem.get(j).getImage());
                                    sendToFramgent.add(item);
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
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                for(CartItemModel a : cartList){
                   // System.out.println(a.getImageUrl());
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







}
