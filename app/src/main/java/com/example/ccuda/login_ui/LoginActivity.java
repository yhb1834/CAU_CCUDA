package com.example.ccuda.login_ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.R;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.LoginRequest;
import com.example.ccuda.ui_Home.HomeActivity;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG_PLUSONE_LOGIN = "plusonelogin";
    private static final String TAG_KAKAO_LOGIN = "kakaologin";

    // 로그인 세션
    private SessionCallback sessionCallback = new SessionCallback();
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login=findViewById(R.id.loginbutton);
        TextView signup=(TextView) findViewById(R.id.signup);
        TextView findepwd =(TextView) findViewById(R.id.findpassword);
        //Button toHome=findViewById(R.id.tohome);
        EditText Email = findViewById(R.id.EmailAddress);
        EditText Password = findViewById(R.id.Password);

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);
        session.checkAndImplicitOpen(); // 자동 로그인

        // 쁠원 자체 회원 로그인 세션
        if(SaveSharedPreference.getEmail(this).length() != 0){
            getUserInfo(TAG_PLUSONE_LOGIN,0,SaveSharedPreference.getEmail(this),SaveSharedPreference.getPassword(this),LoginActivity.this);
            toMainActivity();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInfo(TAG_PLUSONE_LOGIN,0,Email.getText().toString(), PasswordEncryption.encrypt(Password.getText().toString()),LoginActivity.this);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
/*
        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

 */
        findepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindpasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    public void toMainActivity(){
        final Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0); // 전환효과 제거
        this.finish();
    }

    // 로그인
    private void getUserInfo(String option, long id, String email, String password,Context context){
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        Log.d("success","query success");
                        long p_id = Long.parseLong(jsonObject.getString("id"));
                        String p_email = jsonObject.getString("email");
                        String p_nicname = jsonObject.getString("nicname");
                        double p_score = Double.parseDouble(jsonObject.getString("score"));
                        String imageurl = jsonObject.getString("profile");
                        String filename = jsonObject.getString("filename");
                        if(imageurl.equals("null")){
                            imageurl = null;
                        }else{
                            SaveSharedPreference.setPrefUserProfilefile(LoginActivity.this, filename);
                        }
                        if(SaveSharedPreference.getEmail(context).length() == 0){
                            SaveSharedPreference.setSession(context, p_id, p_email, password,p_nicname, p_score,imageurl);
                            if(option == TAG_PLUSONE_LOGIN)
                                toMainActivity();
                        }

                    }
                    else{
                        Log.d("success","query fail");
                        Toast.makeText(context,"회원 정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        LoginRequest loginRequest = new LoginRequest(option, id, email,password, responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(loginRequest);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private  class  SessionCallback implements ISessionCallback {

        // 로그인 성공 상태
        @Override
        public void onSessionOpened() {
            requestMe();
            toMainActivity();
        }

        // 로그인 실패 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe() {
            UserManagement.getInstance()
                    .me(new MeV2ResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                        }

                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                        }

                        @Override
                        public void onSuccess(MeV2Response result) {
                            Log.i("KAKAO_API", "사용자 아이디: " + result.getId());

                            UserAccount kakaoAccount = result.getKakaoAccount();
                            if (kakaoAccount != null) {
                                // 이메일
                                String email = kakaoAccount.getEmail();

                                if (email != null) {
                                    Log.i("KAKAO_API", "email: " + email);
                                } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.FALSE.TRUE) {
                                    // 동의 요청 후 이메일 획득 가능
                                    // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.
                                } else {
                                    // 이메일 획득 불가
                                }
                                // 프로필
                                Profile profile = kakaoAccount.getProfile();

                                if (profile != null) {
                                    Log.d("KAKAO_API", "nickname: " + profile.getNickname());
                                    Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
                                    Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());

                                    long p_id = result.getId();
                                    String p_email = kakaoAccount.getEmail();
                                    if (p_email == null)    p_email="";
                                    getUserInfo(TAG_KAKAO_LOGIN,p_id,p_email,"", LoginActivity.this);

                                } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                                    // 동의 요청 후 프로필 정보 획득 가능

                                } else {
                                    // 프로필 획득 불가
                                }
                            }
                        }
                    });
        }

    }

}

