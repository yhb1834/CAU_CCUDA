package com.example.ccuda;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ccuda.data.UserData;

public class CupponFragment extends Fragment {
    private Context context;
    UserData userData;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        context = container.getContext();
        userData = UserData.getInstance();
        return inflater.inflate(R.layout.fragment_cuppon, container, false);
    }

}
