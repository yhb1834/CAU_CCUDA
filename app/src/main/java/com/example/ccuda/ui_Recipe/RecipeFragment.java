package com.example.ccuda.ui_Recipe;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ccuda.R;
import com.example.ccuda.data.RecipeDTO;
import com.example.ccuda.data.RecipeItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecipeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private ArrayList<RecipeItem> RecipeItems;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    SwipeRefreshLayout mSwipeRefreshLayout;//새로고침
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference recipeRef;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment1_recipe, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecipeRecyclerView);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh2);//새로고침
        mSwipeRefreshLayout.setOnRefreshListener(this);

        /* initiate adapter */
        mRecipeAdapter = new RecipeAdapter();

        /* initiate recyclerview */
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(mLayoutManager);//new LinearLayoutManager(getActivity()));
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));

        /* adapt data */
        RecipeItems = new ArrayList<>();
        //for(int i=1;i<=10;i++){
        //    RecipeItems.add(new RecipeItem(R.drawable.person,i,i+"조합 "));
        //}
        load_Recipelist();
        mRecipeAdapter.setRecipeList(RecipeItems);

        //레시피 개별 페이지 클릭
        mRecipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickEventListener(){
            private RecipeItemFragment fragmentRecipeItem = new RecipeItemFragment();

            @Override
            public void onItemClick(View a_view, int a_position) {
                final RecipeItem item = RecipeItems.get(a_position);

                Bundle bundle = new Bundle();

                bundle.putParcelable(Global.KEY_DATA, item);
                fragmentRecipeItem.setArguments(bundle);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.innerLayout, fragmentRecipeItem);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        //맨 위로 플로팅 버
        FloatingActionButton fabup = (FloatingActionButton) view.findViewById(R.id.scrolltop2);
        fabup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(mRecipeAdapter.getItemCount()-1);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.add_recipe);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RecipeRegisterActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private FragmentManager getSupportFragmentManager() {
        return null;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //adapter.notifyDataSetChanged();
                //GettingPHP gPHP = new GettingPHP();
                //gPHP.execute(url_showPrescription);
                //listView.setAdapter(adapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    private void load_Recipelist(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        recipeRef = firebaseDatabase.getReference();

        recipeRef.child("Recipe").addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //새로 추가된 데이터(값 : ChatData객체) 가져오기
                RecipeDTO recipeDTO = dataSnapshot.getValue(RecipeDTO.class);
                ArrayList<String> imageurl = getimageurl(recipeDTO);
                RecipeItem recipeItem = new RecipeItem(imageurl.get(0),recipeDTO.getLike(),recipeDTO.getTitle(),recipeDTO.getItemname(),imageurl,recipeDTO.getContent());
                RecipeItems.add(recipeItem);

                mRecyclerView.setAdapter(mRecipeAdapter);
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

    private ArrayList<String> getimageurl(RecipeDTO recipeDTO){
        ArrayList<String> imageurl = new ArrayList<>();

        if(recipeDTO.getImage1() != null){
            imageurl.add(recipeDTO.getImage1());
            if(recipeDTO.getImage2() != null){
                imageurl.add(recipeDTO.getImage2());
                if(recipeDTO.getImage3() != null){
                    imageurl.add(recipeDTO.getImage3());
                    if(recipeDTO.getImage4() != null){
                        imageurl.add(recipeDTO.getImage4());
                        if(recipeDTO.getImage5() != null){
                            imageurl.add(recipeDTO.getImage5());
                            if(recipeDTO.getImage6() != null){
                                imageurl.add(recipeDTO.getImage6());
                                if(recipeDTO.getImage7() != null){
                                    imageurl.add(recipeDTO.getImage7());
                                    if(recipeDTO.getImage8() != null){
                                        imageurl.add(recipeDTO.getImage8());
                                        if(recipeDTO.getImage9() != null){
                                            imageurl.add(recipeDTO.getImage9());
                                            if(recipeDTO.getImage10() != null){
                                                imageurl.add(recipeDTO.getImage10());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return imageurl;

    }
}