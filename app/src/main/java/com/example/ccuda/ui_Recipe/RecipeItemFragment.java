package com.example.ccuda.ui_Recipe;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.method.CharacterPickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.RecipeDTO;
import com.example.ccuda.data.RecipeItem;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.ui_Home.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RecipeItemFragment extends Fragment { //implements OnBackPressedListener{

    private TextView like11, recipeTitle11, recipecontent;
    private ImageView recipeImage11;
    private RecipeItem item;
    private ImageButton like2;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference recipeRef;
    ImageButton share;
    int likecheck, likecheck2;      //개별페이지 like버튼 재설정위한 변수
    int islike;

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
            islike = item.getLike();
            String isTitle = item.getTitle();
            String content = item.getContent();

            //파이어베이스 데이터베이스 생성
            firebaseDatabase = FirebaseDatabase.getInstance();
            recipeRef = firebaseDatabase.getReference().child("Recipe");

            recipeImage11 = (ImageView) v.findViewById(R.id.recipeImage2);
            like11 = (TextView) v.findViewById(R.id.likenumber2);
            recipeTitle11 = (TextView) v.findViewById(R.id.recipetitle);
            recipecontent = v.findViewById(R.id.recipecontent);
            like2 = v.findViewById(R.id.like2);

            Glide.with(this).load(isImage).into(recipeImage11);
            //recipeImage11.setImageResource(isImage);
            like11.setText(String.valueOf(islike));
            recipeTitle11.setText(isTitle);
            recipecontent.setText(content);


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

        // like 버튼 setting
        if (item.getLikes().containsKey(Long.toString(SaveSharedPreference.getId(getActivity())))) {
            likecheck = 1; likecheck2 = 0;
        }else {
            likecheck =0; likecheck2 = 0;
        }
        resetlikebtn(likecheck);

        // 좋아요 누르기
        like2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeclick();
            }
        });
        return v;
    }

    //@Override
    /*public void onBackPressed() {
        getActivity().finish();
    }*/

    private void resetlikebtn(int likecheck){
        if(likecheck==1){
            like2.setImageResource(R.drawable.favorite_full);
        }else{
            like2.setImageResource(R.drawable.favorite);
        }
    }

    private void likeclick(){
        new RecipeFragment().likeClicked(recipeRef,item.getImageurl().get(0),getActivity());
        if(likecheck==0){
            //좋아요 안누른 경우
            if(likecheck2==0) {
                // 좋아요
                likecheck2 = 1;
                like11.setText(String.valueOf(islike + 1));
                resetlikebtn(1);
            }
            else {
                // 취소
                likecheck2 = 0;
                like11.setText(String.valueOf(islike));
                resetlikebtn(0);
            }
        }else{
            //좋아요 누른 경우
            if(likecheck2==0){
                // 취소
                likecheck2 = 1;
                like11.setText(String.valueOf(islike-1));
                resetlikebtn(0);
            }
            else{
                // 좋아요
                likecheck2 = 0;
                like11.setText(String.valueOf(islike));
                resetlikebtn(1);
            }
        }
    }

}