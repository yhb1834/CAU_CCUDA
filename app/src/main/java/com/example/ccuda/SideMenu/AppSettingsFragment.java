package com.example.ccuda.SideMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ccuda.R;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.login_ui.LoginActivity;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.response.model.User;

import java.util.ConcurrentModificationException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppSettingsFragment extends Fragment {
    Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AppSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppSettingsFragment newInstance(String param1, String param2) {
        AppSettingsFragment fragment = new AppSettingsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment2_app_settings, container, false);
        context = getActivity();
        Button logout = view.findViewById(R.id.logout);
        Button exit = view.findViewById(R.id.exit);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SaveSharedPreference.getPassword(context).equals("")){
                    Toast.makeText(context, "로그아웃", Toast.LENGTH_SHORT).show();
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            startActivity(new Intent(context,LoginActivity.class));
                            getActivity().finish();
                        }
                    });
                }else{
                    SaveSharedPreference.clearSession(context);
                    Toast.makeText(context, "로그아웃", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context,LoginActivity.class));
                    getActivity().finish();
                }

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SaveSharedPreference.getPassword(context).equals("")){
                    //
                }
            }
        });

        return view;
    }

}