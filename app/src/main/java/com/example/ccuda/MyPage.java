package com.example.ccuda;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ccuda.data.SaveSharedPreference;

public class MyPage extends Fragment {
    private TextView mypagetv;
    private ImageView mypageiv;
    Context context;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mypage, container, false);
        context = getActivity();
        mypagetv = view.findViewById(R.id.mypagetv);
        mypageiv = view.findViewById(R.id.mypageiv);

        /*set userinfo*/
        String email;
        if(SaveSharedPreference.getEmail(context).length() == 0)
            email = "kakao login user";
        else
            email = SaveSharedPreference.getEmail(context);
        mypagetv.setText(SaveSharedPreference.getNicname(context)+"\n"+email);
        Glide.with(this).load(SaveSharedPreference.getProfileimage(context)).into(mypageiv);


        return view;
    }
}
