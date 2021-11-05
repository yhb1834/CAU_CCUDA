package com.example.ccuda.ui_login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.R;
import com.example.ccuda.db.RecoverRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class RecreatepasswordActivity extends AppCompatActivity {
    TextInputEditText newpwd;
    TextInputEditText checkpwd;
    Button recreateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recreatepassword);

        Intent intent = getIntent();
        String Email = intent.getExtras().getString("Email");

        newpwd = (TextInputEditText) findViewById(R.id.newpassword);
        checkpwd = (TextInputEditText) findViewById(R.id.checknewpassword);
        recreateButton = (Button) findViewById(R.id.btn_recreatepwd);

        recreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Password = newpwd.getText().toString();
                final String RePassword = checkpwd.getText().toString();

                if(Password.equals("")){
                    Toast.makeText(RecreatepasswordActivity.this, "비밀번호를 설정해주세요.", Toast.LENGTH_SHORT).show();
                    newpwd.requestFocus();
                    return;
                }
                else if(!Password.equals(RePassword)){
                    Toast.makeText(RecreatepasswordActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    newpwd.setText("");
                    checkpwd.setText("");
                    newpwd.requestFocus();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                Toast.makeText(getApplicationContext(), "비밀번호가 재설정되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RecreatepasswordActivity.this);
                                builder.setMessage("재설정 실패")
                                        .setNegativeButton("재시도",null)
                                        .create()
                                        .show();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                };
                RecoverRequest recoverRequest = new RecoverRequest("recreatepassword", Email, "", PasswordEncryption.encrypt(Password), responseListener);
                RequestQueue queue = Volley.newRequestQueue(RecreatepasswordActivity.this);
                queue.add(recoverRequest);
            }
        });

    }
}
