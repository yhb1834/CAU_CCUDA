package com.example.ccuda.ui_Recipe;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RecipeItemFragment extends Fragment { //implements OnBackPressedListener{

    private TextView like11, recipeTitle11, recipecontent;
    private ImageView recipeImage11;
    private RecipeItem item;

    FirebaseDatabase firebaseDatabase;
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
            String content = item.getContent();

            //파이어베이스 데이터베이스 생성
            firebaseDatabase = FirebaseDatabase.getInstance();

            recipeImage11 = (ImageView) v.findViewById(R.id.recipeImage2);
            like11 = (TextView) v.findViewById(R.id.likenumber2);
            recipeTitle11 = (TextView) v.findViewById(R.id.recipetitle);
            recipecontent = v.findViewById(R.id.recipecontent);

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
        return v;
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

    //@Override
    /*public void onBackPressed() {
        getActivity().finish();
    }*/
}