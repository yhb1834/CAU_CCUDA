package com.example.ccuda.ui_Recipe;

import android.content.Context;
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
import android.widget.Toast;

import com.example.ccuda.R;
import com.example.ccuda.data.RecipeDTO;
import com.example.ccuda.data.RecipeItem;
import com.example.ccuda.data.SaveSharedPreference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RecipeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private ArrayList<RecipeItem> RecipeItems;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    SwipeRefreshLayout mSwipeRefreshLayout;//새로고침

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference recipeRef;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment1_recipe, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecipeRecyclerView);
        context = getActivity();

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh2);//새로고침
        mSwipeRefreshLayout.setOnRefreshListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        recipeRef = firebaseDatabase.getReference().child("Recipe");

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

        mRecipeAdapter.setRecipeList(RecipeItems);
        mRecyclerView.setAdapter(mRecipeAdapter);

        load_Recipelist();

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

        //좋아요 클릭
        mRecipeAdapter.setOnLikeClickEventListener(new RecipeAdapter.OnLikeClickEventListener() {
            @Override
            public void onLikeClick(View view, int position) {
                final RecipeItem item = RecipeItems.get(position);

                likeClicked(recipeRef,item.getImageurl().get(0),context);
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
        recipeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RecipeItems.clear();
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    //새 데이터(값 : ChatData객체) 가져오기
                    RecipeDTO recipeDTO = ds.getValue(RecipeDTO.class);
                    ArrayList<String> imageurl = getimageurl(recipeDTO);
                    String[] itemname = getitemname(recipeDTO.getItemname());
                    RecipeItem recipeItem = new RecipeItem(imageurl.get(0),recipeDTO.getLike(),recipeDTO.getTitle(),itemname,imageurl,recipeDTO.getContent(),recipeDTO.getLikes());
                    RecipeItems.add(recipeItem);
                }

                mRecipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private String[] getitemname(String itemnamelist){
        String[] itemname;
        itemname = itemnamelist.split(", ");
        return itemname;
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

    //좋아요 데이터베이스 트랜잭션 저장
    public void likeClicked(DatabaseReference recipeRef, String data, Context context) {
        recipeRef.orderByChild("image1").equalTo(data).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    resetlike(recipeRef, key, context);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void resetlike(DatabaseReference recipeRef, String key, Context context){
        recipeRef.child(key).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                RecipeDTO recipeDTO = mutableData.getValue(RecipeDTO.class);
                String me = Long.toString(SaveSharedPreference.getId(context));
                if (recipeDTO == null) {
                    return Transaction.success(mutableData);
                }

                //좋아요 누른 사람을 확인
                if (recipeDTO.getLikes().containsKey(me))
                {
                    //좋아요 취소
                    recipeDTO.setLike(recipeDTO.getLike() - 1);
                    recipeDTO.getLikes().remove(me);
                } else {
                    // 좋아요
                    recipeDTO.setLike(recipeDTO.getLike() + 1);
                    recipeDTO.getLikes().put(me, true);
                }

                // Set value and report transaction success
                mutableData.setValue(recipeDTO);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed,
                                   DataSnapshot currentData) {
                // Transaction completed
            }
        });
    }


    /*삭제버튼 생성후 삽입*/
    // 이미지 storage삭제
    public void onDeleteImage(String fileName)
    {
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        StorageReference desertRef = firebaseStorage.getReference("recipeImages/"+ fileName);
        desertRef.delete();
    }

    public void getfirebasekey(String data){
        firebaseDatabase.getReference().child("Recipe").orderByChild("image1").equalTo(data).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    deleteRecipe(key);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // 게시글 삭제
    public void deleteRecipe(String key){
        firebaseDatabase.getReference().child("Recipe").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "삭제 성공", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("error: "+e.getMessage());
                Toast.makeText(getActivity(), "삭제 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}