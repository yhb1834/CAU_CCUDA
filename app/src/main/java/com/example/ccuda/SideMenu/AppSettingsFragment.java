package com.example.ccuda.SideMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.R;
import com.example.ccuda.data.PeopleItem;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.ChatRequest;
import com.example.ccuda.db.MypageRequest;
import com.example.ccuda.login_ui.LoginActivity;
import com.kakao.network.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

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
                    SaveSharedPreference.setPrefUserEmail(context,"");
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
                Response.Listener<String> responsListener = new Response.Listener<String>() {
                    @Override
                       public void onResponse(String response) {
                           try {
                               JSONObject jsonObject = new JSONObject(response);
                               Boolean success = jsonObject.getBoolean("success");
                               if(success){
                                   Toast.makeText(context,"회원탈퇴",Toast.LENGTH_SHORT).show();
                                   if(SaveSharedPreference.getPassword(context).equals("")){
                                       kakaosignout();
                                   }
                                   else{
                                       startActivity(new Intent(context,LoginActivity.class));
                                       getActivity().finish();
                                   }
                               }else {
                                   String check = jsonObject.getString("check");

                                   // TODO: 탈퇴불가 이후 동작
                                   if(check.equals("report")){
                                       Log.d("exit",": "+jsonObject.getString("reportdate")+" 자로 신고된 건 처리중 탈퇴 불가");
                                       //
                                   }
                                   else if(check.equals("deal")){
                                       Log.d("exit",": 거래중 탈퇴 불가");
                                       //
                                   }
                               }
                           } catch (Exception e) {
                               e.printStackTrace();
                           }
                       }
                   };
                   MypageRequest mypageRequest = new MypageRequest("exit", SaveSharedPreference.getId(context),"",responsListener);
                   RequestQueue queue = Volley.newRequestQueue(context);
                   queue.add(mypageRequest);

            }

        });

        return view;
    }

    private void kakaosignout(){
        UserManagement.getInstance()
                .requestUnlink(new UnLinkResponseCallback() {
                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Toast.makeText(context, "에러: "+errorResult+"로그인 세션이 닫혔습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public  void onFailure(ErrorResult errorResult) {
                        int error = errorResult.getErrorCode();
                        if(error == ApiErrorCode.CLIENT_ERROR_CODE) {
                            Toast.makeText(context, "에러: "+error+"\n네트워크 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "에러: "+error+"\n회원 탈퇴에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onSuccess(Long result) {
                        Toast.makeText(context, "성공: "+result+"\n회원 탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
    }

}