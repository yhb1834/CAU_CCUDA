package com.example.ccuda.ui_Recipe;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.RecipeItem;

public class RecipeItemFragment extends Fragment { //implements OnBackPressedListener{

    private TextView like11, recipeTitle11;
    private ImageView recipeImage11;
    private RecipeItem item;

    ImageButton share;

    public RecipeItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment1_recipe_item, container, false);
        item = getArguments().getParcelable(Global.KEY_DATA);
        if ( item != null) {
            String isImage = item.getImage();
            int islike = item.getLike();
            String isTitle = item.getTitle();

            recipeImage11 = (ImageView) v.findViewById(R.id.recipeImage2);
            like11 = (TextView) v.findViewById(R.id.likenumber2);
            recipeTitle11 = (TextView) v.findViewById(R.id.recipetitle);

            Glide.with(this).load(isImage).into(recipeImage11);
            //recipeImage11.setImageResource(isImage);
            like11.setText(String.valueOf(islike));
            recipeTitle11.setText(isTitle);
        }

        //다른 앱 이용해서 공유하기
        share = v.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent msg = new Intent(Intent.ACTION_SEND);

                msg.addCategory(Intent.CATEGORY_DEFAULT);

                String isTitle = item.getTitle();

                msg.putExtra(Intent.EXTRA_SUBJECT, isTitle);
                msg.putExtra(Intent.EXTRA_TEXT, "내용");
                msg.putExtra(Intent.EXTRA_TITLE, isTitle); //앱 공유할 때 나오는 제

                msg.setType("text/plain");
                startActivity(Intent.createChooser(msg, "공유하기"));

            }
        });
        return v;
    }

    //@Override
    /*public void onBackPressed() {
        getActivity().finish();
    }*/
}