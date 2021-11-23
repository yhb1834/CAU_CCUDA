package com.example.ccuda.ui_Recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.RecipeItem;
import com.example.ccuda.data.SaveSharedPreference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileReader;
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
        return new ViewHolder(view, onItemClickEventListener, onLikeClickEventListener);
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
    public interface OnLikeClickEventListener{
        void onLikeClick(View view, int position);
    }

    private OnItemClickEventListener onItemClickEventListener;
    private OnLikeClickEventListener onLikeClickEventListener;

    public void setOnItemClickListener(OnItemClickEventListener listener) {
        onItemClickEventListener = listener;
    }
    public void setOnLikeClickEventListener(OnLikeClickEventListener listener){
        onLikeClickEventListener = listener;
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    TextView like;
    TextView title;
    ImageButton likebutton;

    public ViewHolder(@NonNull View itemView, final RecipeAdapter.OnItemClickEventListener itemClickListener, final RecipeAdapter.OnLikeClickEventListener likeClickEventListener) {
        super(itemView);

        image = (ImageView) itemView.findViewById(R.id.recipeImage1);
        like = (TextView) itemView.findViewById(R.id.likenumber1);
        title = (TextView) itemView.findViewById(R.id.recipeTitle1);
        likebutton = (ImageButton) itemView.findViewById(R.id.likebutton);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(a_view, position);
                }
            }
        });

        likebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    likeClickEventListener.onLikeClick(view,position);
                }
            }
        });
    }


    void onBindRecipe(RecipeItem item){
        Glide.with(itemView.getContext()).load(item.getImage()).into(image);
        //image.setImageResource(item.getImage());
        like.setText(String.valueOf(item.getLike()));
        title.setText(item.getTitle());
        if (item.getLikes().containsKey(Long.toString(SaveSharedPreference.getId(itemView.getContext())))) {
            likebutton.setImageResource(R.drawable.favorite_full);
        }else {
            likebutton.setImageResource(R.drawable.favorite);
        }
    }


}