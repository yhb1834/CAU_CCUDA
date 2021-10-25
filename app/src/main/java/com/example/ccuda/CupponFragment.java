package com.example.ccuda;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.db.PostRequest;
import com.example.ccuda.data.UserData;

import org.json.JSONObject;

public class CupponFragment extends Fragment {
    private Context context;
    UserData userData;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        context = container.getContext();
        userData = UserData.getInstance();
        return inflater.inflate(R.layout.cuppon, container, false);
    }

}
