package com.example.ccuda.ui_Recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ccuda.R;
import com.example.ccuda.data.RecipeItem;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<RecipeItem> mRecipe = null;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment1_recipe_simple_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindRecipe(mRecipe.get(position));
    }

    public void setRecipeList(ArrayList<RecipeItem> list){
        this.mRecipe = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mRecipe.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    TextView like;
    TextView title;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        image = (ImageView) itemView.findViewById(R.id.imageView2);
        like = (TextView) itemView.findViewById(R.id.like);
        title = (TextView) itemView.findViewById(R.id.RecipeName);
    }

    void onBindRecipe(RecipeItem item){
        image.setImageResource((item.getImage()));
        like.setInputType(item.getLike());
        title.setText(item.getTitle());
    }
}