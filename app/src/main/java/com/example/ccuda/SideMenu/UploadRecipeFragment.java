package com.example.ccuda.SideMenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.ccuda.ui_Recipe.Global;
import com.example.ccuda.ui_Recipe.RecipeAdapter;
import com.example.ccuda.ui_Recipe.RecipeFragment;
import com.example.ccuda.ui_Recipe.RecipeItemFragment;
import com.example.ccuda.ui_Recipe.RecipeRegisterActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class UploadRecipeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private ArrayList<RecipeItem> RecipeItems;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference recipeRef;
    Context context;
    SwipeRefreshLayout mSwipeRefreshLayout;//새로고침


    public UploadRecipeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_recipe2, container, false);
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

        mRecyclerView.setLayoutManager(mLayoutManager);

        /* adapt data */
        RecipeItems = new ArrayList<>();

        mRecipeAdapter.setRecipeList(RecipeItems);
        mRecyclerView.setAdapter(mRecipeAdapter);

        load_MyRecipelist();

        //레시피 개별 페이지 클릭
        mRecipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickEventListener() {
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

        mRecipeAdapter.setOnItemLongClickListener(new RecipeAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                final RecipeItem item = RecipeItems.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("레시피 삭제");
                builder.setMessage("레시피를 삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        for(int i=0; i<item.getFilename().size(); i++){
                            onDeleteImage(item.getFilename().get(i));
                        }
                        getfirebasekey(item.getImage());
                    }
                });
                builder.setNegativeButton("취소",null);
                builder.show();
            }
        });

        //좋아요 클릭
        mRecipeAdapter.setOnLikeClickEventListener(new RecipeAdapter.OnLikeClickEventListener() {
            @Override
            public void onLikeClick(View view, int position) {
                final RecipeItem item = RecipeItems.get(position);
                new RecipeFragment().likeClicked(recipeRef, item.getImageurl().get(0), context);
            }
        });

        FloatingActionButton fabup = (FloatingActionButton) view.findViewById(R.id.scrolltop2);
        fabup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(mRecipeAdapter.getItemCount()-1);
            }
        });

        return view;
    }


    private void load_MyRecipelist() {
        recipeRef.orderByChild("writer_id").equalTo(Long.toString(SaveSharedPreference.getId(context))).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RecipeItems.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //새 데이터(값 : ChatData객체) 가져오기
                    RecipeDTO recipeDTO = ds.getValue(RecipeDTO.class);
                    RecipeFragment recipeFragment = new RecipeFragment();
                    ArrayList<String> imageurl = recipeFragment.getimageurl(recipeDTO);
                    String[] itemname = recipeFragment.getitemname(recipeDTO.getItemname());
                    ArrayList<String> filename = recipeFragment.getfilename(recipeDTO);
                    RecipeItem recipeItem = new RecipeItem(imageurl.get(0), recipeDTO.getLike(), recipeDTO.getTitle(), itemname, imageurl, filename, recipeDTO.getContent(), recipeDTO.getLikes());
                    RecipeItems.add(recipeItem);
                }

                mRecipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

    // 이미지 storage삭제
    private void onDeleteImage(String fileName)
    {
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        StorageReference desertRef = firebaseStorage.getReference("recipeImages/"+ fileName);
        desertRef.delete();
    }

    private void getfirebasekey(String data){
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
    private void deleteRecipe(String key){
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