
package com.example.forumapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.forumapp.Util.Util;
import com.example.forumapp.Webservices.JSONParse;
import com.example.forumapp.Webservices.RestAPI;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    Toolbar toolbar;
    ProgressBar pb;
    TextInputEditText username, pass;
    Button btn, register;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Util.getSP(getApplicationContext()).equals("")) {
            initui();
        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private void initui() {
        toolbar = findViewById(R.id.toolbaar);
        toolbar.setTitle("Login");
        register = findViewById(R.id.registerbtn);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        setSupportActionBar(toolbar);
        btn = (Button) findViewById(R.id.loginbtn);
        pb = (ProgressBar) findViewById(R.id.pb_login);
        username = (TextInputEditText) findViewById(R.id.email_edt);
        pass = (TextInputEditText) findViewById(R.id.pass_edt);
        layout = (LinearLayout) findViewById(R.id.layout_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pb.setVisibility(View.VISIBLE);
                if (username.getText().toString().isEmpty()) {
                    Snackbar.make(layout, "Enter Username", Snackbar.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                } else if (pass.getText().toString().isEmpty()) {
                    Snackbar.make(layout, "Enter Password", Snackbar.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                } else {
                    login();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    private void login() {
        new Login().execute(username.getText().toString(), pass.getText().toString());

    }

    public class Login extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.ULogin(strings[0],strings[1]);
                data = jp.parse(json);
            } catch (Exception e) {
                data = e.getMessage();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("res", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String response = jsonObject.getString("status");
                if (response.compareTo("ok") == 0) {
                    JSONArray array = jsonObject.getJSONArray("Data");
                    JSONObject jsonObject1 = array.getJSONObject(0);
                    String uid = jsonObject1.optString("data0");
                    Log.d("uid", uid);
                    String name = jsonObject1.optString("data1");
                    Util.setSP(getApplicationContext(), uid);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    Snackbar.make(layout, "Failed", Snackbar.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}