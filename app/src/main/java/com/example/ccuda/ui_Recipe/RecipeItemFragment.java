package com.example.ccuda.ui_Recipe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ccuda.R;
import com.example.ccuda.data.RecipeItem;

public class RecipeItemFragment extends Fragment implements OnBackPressedListener{

    private TextView like11, recipeTitle11;
    private ImageView recipeImage11;
    private RecipeItem item;

    public RecipeItemFragment() {
        // Required empty public constructor
    }
/*
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    // TODO: Rename and change types and number of parameters
    public static RecipeItemFragment newInstance(String param1, String param2) {
        RecipeItemFragment fragment = new RecipeItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment1_recipe_item, container, false);
        item = getArguments().getParcelable(Global.KEY_DATA);
        if ( item != null) {
            int isImage = item.getImage();
            String islike = item.getLike();
            String isTitle = item.getTitle();

            recipeImage11 = (ImageView) v.findViewById(R.id.recipeImage2);
            like11 = (TextView) v.findViewById(R.id.likenumber2);
            recipeTitle11 = (TextView) v.findViewById(R.id.recipetitle);

            recipeImage11.setImageResource(R.drawable.person);
            like11.setText(islike);
            recipeTitle11.setText(isTitle);
        }
        return v;
    }

    @Override
    public void onBackPressed() {
        getActivity().finish();
    }
}