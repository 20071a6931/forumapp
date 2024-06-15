package com.example.forumapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.forumapp.Webservices.JSONParse;
import com.example.forumapp.Webservices.RestAPI;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout layout;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextInputEditText name, email, height, contact, pass;
    String gender;
    Button register_btn;
    String bmi, bmr;

    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUi();
    }
    private void initUi() {
        toolbar = (Toolbar) findViewById(R.id.toolbaar);
        toolbar.setTitle("Registeration");
        setSupportActionBar(toolbar);

        register_btn = (Button) findViewById(R.id.registeration_btn);
        name = (TextInputEditText) findViewById(R.id.name_edt);
        email = (TextInputEditText) findViewById(R.id.remail_edt);
        contact = (TextInputEditText) findViewById(R.id.contact_edt);

        pass = (TextInputEditText) findViewById(R.id.rpass_edt);
        layout = (LinearLayout) findViewById(R.id.layout_reg);
        gender = "Male";
        pb = (ProgressBar) findViewById(R.id.pb_reg);


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                if (name.getText().toString().isEmpty()) {
                    pb.setVisibility(View.GONE);
                    Snackbar.make(layout, "Enter Name", Snackbar.LENGTH_SHORT).show();
                } else if (email.getText().toString().isEmpty()) {
                    pb.setVisibility(View.GONE);
                    Snackbar.make(layout, "Enter Email", Snackbar.LENGTH_SHORT).show();
                } else if (contact.getText().toString().isEmpty()) {
                    pb.setVisibility(View.GONE);
                    Snackbar.make(layout, "Enter Contact", Snackbar.LENGTH_SHORT).show();
                } else if (pass.getText().toString().isEmpty()) {
                    pb.setVisibility(View.GONE);
                    Snackbar.make(layout, "Enter Password", Snackbar.LENGTH_SHORT).show();
                } else {

                    new Register().execute(name.getText().toString(), email.getText().toString(), contact.getText().toString(), pass.getText().toString());
                }


            }
        });
    }

    private void calculatebmr(float height, float weight, float age, String gender) {
        if (gender.equals("Male")) {
            bmr = String.valueOf((88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)));
            Log.d("gender", gender);
            Log.d("bmr", bmr);
        } else if (gender.equals("Female")) {
            bmr = String.valueOf((447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)));
            Log.d("gender", gender);
            Log.d("bmr", bmr);
        } else {

        }

    }


    public class Register extends AsyncTask<String, String, String> {
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
                JSONObject json = restAPI.URegister(strings[0], strings[1], strings[2], strings[3]);
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
                if (response.compareTo("true") == 0) {
                    pb.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();

                } else if (response.compareTo("already") == 0) {
                    Snackbar.make(layout, " Already Registered", BaseTransientBottomBar.LENGTH_SHORT).show();
                } else {

                    pb.setVisibility(View.GONE);

                    Snackbar.make(layout, " Failed", BaseTransientBottomBar.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}