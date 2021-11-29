package com.example.ccuda.ui_Cart;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
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
    //ArrayList<ItemData> cuItem = HomeActivity.cuItem;
    //ArrayList<ItemData> gs25Item = HomeActivity.gs25Item;
    //ArrayList<ItemData> sevenItem = HomeActivity.sevenItem;


    CartItemAdapter adapter;
    ArrayList<CartItemModel> itemList=new ArrayList<>();
    //ArrayList<CartItemModel> cartList=new ArrayList<>();
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
    //ArrayList<ItemParccelable> sendToFramgent=new ArrayList<>();
    //ArrayList<> cartItemImgs=new ArrayList<>(); // 장바구니에 추가된 사진 list


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_cartlist, container, false);
        searchView=view.findViewById(R.id.searchView);
        recyclerView=view.findViewById(R.id.recyclerView);
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

        emptyCartText=view.findViewById(R.id.emptyCartText);
        viewAll=view.findViewById(R.id.view_all);

        imageViews[0]=view.findViewById(R.id.image1);
        imageViews[1]=view.findViewById(R.id.image2);
        imageViews[2]=view.findViewById(R.id.image3);
        imageViews[3]=view.findViewById(R.id.image4);
        imageViews[4]=view.findViewById(R.id.image5);


        if(cartList.size()!=0){
            emptyCartText.setText("");
            emptyCartText.setTextSize(0);
            viewAll.setVisibility(View.VISIBLE);
        }
        if(cartList.size()==0){
            viewAll.setVisibility(View.INVISIBLE);
        }
        resetImageview();

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result=new Bundle();
                result.putParcelableArrayList("itemlist", sendToFramgent);
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
        for(int i=0;i<gs25Item.size();i++){
            itemList.add(new CartItemModel(gs25Item.get(i).getImage(), gs25Item.get(i).getItemname(), gs25Item.get(i).getStorename(), gs25Item.get(i).getItemid()));

        }
        for(int i=0;i<sevenItem.size();i++){
            itemList.add(new CartItemModel(sevenItem.get(i).getImage(), sevenItem.get(i).getItemname(), sevenItem.get(i).getStorename(), sevenItem.get(i).getItemid()));

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

        adapter = new CartItemAdapter(itemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL); //밑줄
        recyclerView.addItemDecoration(dividerItemDecoration);


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
                        adapter.notifyDataSetChanged();

                        cartList.add(cartItemModel);
                        ItemParccelable item=new ItemParccelable();
                        item.setProdName(cartItemModel.getText1());
                        item.setConvName(cartItemModel.getText2());
                        item.setImgUrl(cartItemModel.getImageUrl());
                        sendToFramgent.add(item);
                        resetImageview();
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



    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume at addToCart");
        load_MyCartList();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(cartList.size()!=0){
                    emptyCartText.setText("");
                    emptyCartText.setTextSize(0);
                    viewAll.setVisibility(View.VISIBLE);
                }
                if(cartList.size()==0){
                    viewAll.setVisibility(View.INVISIBLE);
                }
                load_MyCartList();
                resetImageview();
            }
        }, 500);

        //resetImageview();
    }

    private void resetImageview(){
        for(int i=0;i<5;i++){
            imageViews[i].setImageResource(0);
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


        if(cartList.size()<=5){
            for (i=0;i<cartList.size();i++){
                CartItemModel item=cartList.get(cartList.size()-1-i);
                Glide.with(getContext()).load(cartList.get(cartList.size()-1-i).getImageUrl())
                        .into(imageViews[i]);
                imageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click_cart_item(getContext(),item);
                    }
                });
            }
        }else {
            for (i=0;i<5;i++){
                CartItemModel item=cartList.get(cartList.size()-1-i);
                Glide.with(getContext()).load(cartList.get(cartList.size()-1-i).getImageUrl())
                        .into(imageViews[i]);
                imageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { click_cart_item(getContext(),item); }
                });
            }
        }
    }





}
