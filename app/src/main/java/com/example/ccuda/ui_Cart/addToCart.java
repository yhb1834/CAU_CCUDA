package com.example.ccuda.ui_Cart;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.ui_Home.Adapter;
import com.example.ccuda.ui_Home.UploadCoupon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class addToCart extends Fragment {
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<ItemData> cuItem = new ArrayList<>();
    ArrayList<ItemData> gs25Item = new ArrayList<>();
    ArrayList<ItemData> sevenItem = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference itemRef;

    CartItemAdapter adapter;
    List<CartItemModel> itemList;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_cartlist, container, false);
        searchView=view.findViewById(R.id.searchView);
        recyclerView=view.findViewById(R.id.recyclerView);
        //load_item();
        System.out.println("llll: "+cuItem.size());
        load_item();
        setUpRecyclerView();



        //textView=view.findViewById(R.id.searchTextView);
        //textView.setText(getResult());

        load_item();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //textView.setText(search(newText));
                //recyclerView=search(newText);
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return view;
    }


    public void fillData() {
        itemList = new ArrayList<>(); //샘플테이터
        //load_item();
        for(int i=0;i<cuItem.size();i++){
            itemList.add(new CartItemModel(R.drawable.add, cuItem.get(i).getItemname(), cuItem.get(i).getStorename()));
        }
       // System.out.println("llll: "+cuItem.size());
        //itemList.add(new CartItemModel(R.drawable.add, "One", "Ten"));
        //itemList.add(new CartItemModel(R.drawable.add, "Two", "Eleven"));
        //itemList.add(new CartItemModel(R.drawable.add, "Three", "Twelve"));
        //itemList.add(new CartItemModel(R.drawable.add, "Four", "Thirteen"));
        //itemList.add(new CartItemModel(R.drawable.add, "Five", "Fourteen"));
        //itemList.add(new CartItemModel(R.drawable.add, "Six", "Fifteen"));
        //itemList.add(new CartItemModel(R.drawable.add, "Seven", "Sixteen"));
        //itemList.add(new CartItemModel(R.drawable.add, "Eight", "Seventeen"));
        //itemList.add(new CartItemModel(R.drawable.add, "Nine", "Eighteen"));
    }

    public String getResult(){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<cuItem.size();i++){
            String item=cuItem.get(i).getItemname();
            sb.append(item);
            if(i!=cuItem.size()-1){
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public ArrayList<CartItemModel> search(String query){
        //StringBuilder sb=new StringBuilder();
        ArrayList<CartItemModel> itemList=new ArrayList<>();
        for(int i=0;i<cuItem.size();i++){
            String item=cuItem.get(i).getItemname();
            if (item.toLowerCase().contains(query.toLowerCase())){
                //sb.append(item);
                itemList.add(new CartItemModel(R.drawable.add, cuItem.get(i).getItemname(), cuItem.get(i).getStorename()));
            }
        }
        return itemList;
    }

    public void runThread(String convName, String URL){
        new Thread(new Runnable() {
            ArrayList<String> prodList=new ArrayList<>();
            ArrayList<String> prodPrice=new ArrayList<>();
            Handler mHandler=new Handler();
            Elements elements=new Elements();
            @Override
            public void run() {
                try {
                    if(convName == "cu"){
                        for(int i=0; i<cuItem.size(); i++){
                            prodList.add(cuItem.get(i).getItemname());
                            prodPrice.add(Integer.toString(cuItem.get(i).getItemprice2()));
                        }
                    }
                    else if(convName == "seven"){
                        for(int i=0; i<sevenItem.size(); i++){
                            prodList.add(sevenItem.get(i).getItemname());
                            prodPrice.add(Integer.toString(sevenItem.get(i).getItemprice2()));
                        }
                    }
                    if(convName == "gs25"){
                        for(int i=0; i<gs25Item.size(); i++){
                            prodList.add(gs25Item.get(i).getItemname());
                            prodPrice.add(Integer.toString(gs25Item.get(i).getItemprice2()));
                        }
                    }
                    //prodList = getProductName(convName, URL); //getProductList(convName, URL);
                    // prodPrice = getProductPrice(convName, URL);
                    Thread.sleep(1000);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {



                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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



    private void setUpRecyclerView() {
        //recyclerview
        //RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        //adapter
        itemList = new ArrayList<>(); //샘플테이터
        //load_item();
        fillData();
        adapter = new CartItemAdapter(itemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL); //밑줄
        recyclerView.addItemDecoration(dividerItemDecoration);

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
        Toast.makeText(getContext(), "" +position, Toast.LENGTH_SHORT).show();
    }




}