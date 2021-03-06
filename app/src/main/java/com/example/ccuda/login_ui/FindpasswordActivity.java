package com.example.ccuda.login_ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.Config;
import com.example.ccuda.R;
import com.example.ccuda.db.RecoverRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.Random;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class FindpasswordActivity extends AppCompatActivity  {
    TextInputEditText nameInput;
    TextInputEditText emailInput;
    TextInputEditText auth_numberInput;
    Button findButton;
    Button authButton;
    String Email;
    String Name;
    int randomnum;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);

        nameInput = (TextInputEditText) findViewById(R.id.recreatePasswordName);
        emailInput = (TextInputEditText) findViewById(R.id.recreatePasswordEmail);
        auth_numberInput = (TextInputEditText) findViewById(R.id.recreatePasswordCheck);
        auth_numberInput.setVisibility(View.GONE);
        findButton = (Button) findViewById(R.id.btn_findpassword);
        authButton = (Button) findViewById(R.id.btn_check);
        authButton.setVisibility(View.GONE);

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = nameInput.getText().toString();
                Email = emailInput.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
                        if(Name.equals("")||Email.equals("")){
                            Toast.makeText(FindpasswordActivity.this,"????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(!emailPattern.matcher((Email)).matches()){
                            Toast.makeText(FindpasswordActivity.this, "Email???????????? ???????????????", Toast.LENGTH_SHORT).show();
                            emailInput.requestFocus();
                            return;
                        }

                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                new SendEmailAsyncTask().execute(null,null,null);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "???????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                RecoverRequest recoverRequest = new RecoverRequest("uservalidate",Email,Name,"",responseListener);
                RequestQueue queue = Volley.newRequestQueue(FindpasswordActivity.this);
                queue.add(recoverRequest);
                randomnum=Integer.parseInt(numberGen(1));

            }
        });

        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputnum = Integer.parseInt(auth_numberInput.getText().toString());
                if(auth_numberInput.getText().toString()==""){
                    Toast.makeText(FindpasswordActivity.this, "??????????????? ??????????????????", Toast.LENGTH_SHORT).show();
                }
                else if(randomnum != 0 && inputnum == randomnum){
                    Toast.makeText(FindpasswordActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
                    Intent recreatepassword = new Intent(FindpasswordActivity.this, RecreatepasswordActivity.class);
                    recreatepassword.putExtra("Email",Email);
                    FindpasswordActivity.this.startActivity(recreatepassword);
                    finish();
                }else{
                    Toast.makeText(FindpasswordActivity.this, "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public static String numberGen(int dupCd ) {

        Random rand = new Random();
        String numStr = ""; //????????? ????????? ??????

        for(int i=0;i<6;i++) {

            //0~9 ?????? ?????? ??????
            String ran = Integer.toString(rand.nextInt(10));

            if(dupCd==1) {
                //?????? ????????? numStr??? append
                numStr += ran;
            }else if(dupCd==2) {
                //????????? ???????????? ????????? ????????? ?????? ????????? ????????????
                if(!numStr.contains(ran)) {
                    //????????? ?????? ????????? numStr??? append
                    numStr += ran;
                }else {
                    //????????? ????????? ???????????? ????????? ?????? ????????????
                    i-=1;
                }
            }
        }
        return numStr;
    }

    class SendEmailAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GmailSender sender = new GmailSender(Config.VALIDATION_EMAIL_ADDRESS, Config.VALIDATION_EMAIL_PASSWORD);
            try{
                sender.sendMail("[CCUDA_PPLUSONE] ??????????????? ????????????????", "????????????: "+randomnum, Email);

            } catch (SendFailedException e) {
                Toast.makeText(getApplicationContext(), "????????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
            } catch (MessagingException e) {
                Toast.makeText(getApplicationContext(), "????????? ????????? ?????????????????????", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            Toast.makeText(FindpasswordActivity.this,"???????????? ??????????????????.",Toast.LENGTH_SHORT).show();
            auth_numberInput.setVisibility(View.VISIBLE);
            authButton.setVisibility(View.VISIBLE);
        }
    }

}
