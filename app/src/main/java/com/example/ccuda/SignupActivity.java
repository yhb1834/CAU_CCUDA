// https://velog.io/@xyunkyung/안드로이드-회원가입-로그인-기능-구현하기-4

package com.example.ccuda;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
            }
        });
    }

}
