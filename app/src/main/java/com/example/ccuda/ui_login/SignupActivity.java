// https://velog.io/@xyunkyung/안드로이드-회원가입-로그인-기능-구현하기-4

package com.example.ccuda.ui_login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.R;
import com.example.ccuda.db.SignupRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private EditText email, password, name, passwordconfirmed;
    private Button signup_button;
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email=findViewById(R.id.EmailAddress);
        password=findViewById(R.id.Password);
        name=findViewById(R.id.UserName);
        passwordconfirmed=findViewById(R.id.PasswordConfirm);

        signup_button=findViewById(R.id.signup);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String UserEmail=email.getText().toString();
                final String UserPwd=password.getText().toString();
                final String UserName=name.getText().toString();
                final String PassCk=passwordconfirmed.getText().toString();
                //공백제거
                UserEmail.replace(" ",""); UserPwd.replace(" ",""); UserName.replace(" ","");

                //System.out.println(UserEmail);
                //System.out.println(UserPwd);
                //System.out.println(UserName);
                //System.out.println(PassCk);

                PasswordEncryption passwordEncryption = new PasswordEncryption();
                Pattern emailPattern = Patterns.EMAIL_ADDRESS;
                // 입력 유효성 검토
                if (!emailPattern.matcher((UserEmail)).matches()){
                    Toast.makeText(SignupActivity.this, "Email형식으로 입력하세요", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    return;
                }
                if(UserEmail.equals("")||UserPwd.equals("")||UserName.equals("")){
                    Toast.makeText(getApplicationContext(), "올바른 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!UserPwd.equals(PassCk)){
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }

                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success=jsonObject.getBoolean("success");
                            if(success){
                                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "해당 계정은 이미 쁠원회원입니다.", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };

                SignupRequest signupRequest=new SignupRequest(UserEmail, passwordEncryption.encrypt(UserPwd), UserName, responseListener);
                RequestQueue queue= Volley.newRequestQueue(SignupActivity.this);
                queue.add(signupRequest);

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);


            }
        });
    }

}
