package com.example.ccuda.ui_Recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.RecipeItem;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<ViewHolder> {
    public Context mContext;
    private ArrayList<RecipeItem> mRecipe = null;

    public RecipeAdapter() {

    }

    public RecipeAdapter(Context context, ArrayList<RecipeItem> list){
        this.mRecipe = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment1_recipe_simple_item_layout, parent, false);
        return new ViewHolder(view, onItemClickEventListener);
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


    public interface OnItemClickEventListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickEventListener onItemClickEventListener;

    public void setOnItemClickListener(OnItemClickEventListener listener) {
        onItemClickEventListener = listener;
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    TextView like;
    TextView title;

    public ViewHolder(@NonNull View itemView, final RecipeAdapter.OnItemClickEventListener itemClickListener) {
        super(itemView);

        image = (ImageView) itemView.findViewById(R.id.recipeImage1);
        like = (TextView) itemView.findViewById(R.id.likenumber1);
        title = (TextView) itemView.findViewById(R.id.recipeTitle1);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(a_view, position);
                }
            }
        });
    }


    void onBindRecipe(RecipeItem item){
        Glide.with(itemView.getContext()).load(item.getImage()).into(image);
        //image.setImageResource(item.getImage());
        like.setText(String.valueOf(item.getLike()));
        title.setText(item.getTitle());
    }

}