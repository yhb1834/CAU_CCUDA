package com.example.ccuda.SideMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ccuda.R;
import com.example.ccuda.data.RecipeDTO;
import com.example.ccuda.data.RecipeItem;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.ui_Recipe.Global;
import com.example.ccuda.ui_Recipe.RecipeAdapter;
import com.example.ccuda.ui_Recipe.RecipeFragment;
import com.example.ccuda.ui_Recipe.RecipeItemFragment;
import com.example.ccuda.ui_Recipe.RecipeRegisterActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UploadRecipeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private ArrayList<RecipeItem> RecipeItems;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference recipeRef;
    Context context;


    public UploadRecipeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_recipe, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecipeRecyclerView);
        context = getActivity();

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

        //좋아요 클릭
        mRecipeAdapter.setOnLikeClickEventListener(new RecipeAdapter.OnLikeClickEventListener() {
            @Override
            public void onLikeClick(View view, int position) {
                final RecipeItem item = RecipeItems.get(position);
                new RecipeFragment().likeClicked(recipeRef, item.getImageurl().get(0), context);
            }
        });


        //맨 위로 플로팅 버
        FloatingActionButton fabup = (FloatingActionButton) view.findViewById(R.id.scrolltop2);
        fabup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(mRecipeAdapter.getItemCount() - 1);
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
                    RecipeItem recipeItem = new RecipeItem(imageurl.get(0), recipeDTO.getLike(), recipeDTO.getTitle(), itemname, imageurl, recipeDTO.getContent(), recipeDTO.getLikes());
                    RecipeItems.add(recipeItem);
                }

                mRecipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}