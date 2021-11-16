package com.example.ccuda.ui_Cart;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.ccuda.ui_Home.UploadCoupon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class addToCart extends Fragment {
    SearchView searchView;
    RecyclerView recyclerView;
    TextView textView;
    ArrayList<ItemData> cuItem = new ArrayList<>();
    ArrayList<ItemData> gs25Item = new ArrayList<>();
    ArrayList<ItemData> sevenItem = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference itemRef;

    CartItemAdapter adapter;
    ArrayList<CartItemModel> itemList=new ArrayList<>();


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

                //setUpRecyclerView();
                //adapter.
                return true;
            }
        });

        return view;
    }


    public void load_item(){
        firebaseDatabase= FirebaseDatabase.getInstance();
        itemRef= firebaseDatabase.getReference();

        itemRef.child("item").child("cu").addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ItemData itemData = dataSnapshot.getValue(ItemData.class);
                itemList.add(new CartItemModel(itemData.getImage(), itemData.getItemname(), itemData.getStorename(), itemData.getItemid()));
                //System.out.println("haha1: "+itemData);
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
                itemList.add(new CartItemModel(itemData.getImage(), itemData.getItemname(), itemData.getStorename(), itemData.getItemid()));
                //System.out.println("haha2: "+itemData);
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
                itemList.add(new CartItemModel(itemData.getImage(), itemData.getItemname(), itemData.getStorename(), itemData.getItemid()));
                //System.out.println("haha3: "+itemData);
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





    public void fillData() {
        //itemList = new ArrayList<>(); //샘플테이터
        load_item();
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

        itemList = new ArrayList<>(); //샘플테이터
        load_item();
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
        String storename = cartItemModel.getText1();
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




}
