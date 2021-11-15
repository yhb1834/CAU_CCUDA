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
                itemList.add(new CartItemModel(R.drawable.add, itemData.getItemname(), itemData.getStorename()));
                System.out.println("haha1: "+itemData);
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
                itemList.add(new CartItemModel(R.drawable.add, itemData.getItemname(), itemData.getStorename()));
                System.out.println("haha2: "+itemData);
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
                itemList.add(new CartItemModel(R.drawable.add, itemData.getItemname(), itemData.getStorename()));
                System.out.println("haha3: "+itemData);
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
            itemList.add(new CartItemModel(R.drawable.add, cuItem.get(i).getItemname(), cuItem.get(i).getStorename()));

        }
    }

    public ArrayList<CartItemModel> getResult(){
        ArrayList<CartItemModel> itemList=new ArrayList<>();
        for(int i=0;i<cuItem.size();i++){
            itemList.add(new CartItemModel(R.drawable.add, cuItem.get(i).getItemname(), cuItem.get(i).getStorename()));
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
                itemList.add(new CartItemModel(R.drawable.add, cuItem.get(i).getItemname(), cuItem.get(i).getStorename()));
            }
        }
        return itemList;
    }




    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        itemList = new ArrayList<>(); //샘플테이터
        load_item();
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
