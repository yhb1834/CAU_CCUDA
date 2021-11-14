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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ccuda.R;
import com.example.ccuda.data.RecipeItem;
import com.example.ccuda.ui_Home.HomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecipeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private ArrayList<RecipeItem> RecipeItems;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment1_recipe, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecipeRecyclerView);

        /* initiate adapter */
        mRecipeAdapter = new RecipeAdapter();

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mRecipeAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));

        /* adapt data */
        RecipeItems = new ArrayList<>();
        for(int i=1;i<=10;i++){
            RecipeItems.add(new RecipeItem(R.drawable.person,i,i+"조합 "));
        }

        mRecipeAdapter.setRecipeList(RecipeItems);


        mRecipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickEventListener(){
            private RecipeItemFragment fragmentRecipeItem = new RecipeItemFragment();

            @Override
            public void onItemClick(View a_view, int a_position) {
                final RecipeItem item = RecipeItems.get(a_position);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.innerLayout, fragmentRecipeItem);
                transaction.addToBackStack(null);
                transaction.commit();
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
}