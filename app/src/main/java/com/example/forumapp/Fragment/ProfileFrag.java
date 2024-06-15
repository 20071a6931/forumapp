package com.example.forumapp.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.forumapp.R;
import com.example.forumapp.Util.Util;
import com.example.forumapp.Webservices.JSONParse;
import com.example.forumapp.Webservices.RestAPI;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFrag extends Fragment {
    Toolbar toolbar;
    LinearLayout layout;
    TextInputEditText name, email, contact;
    Button register_btn;
    ProgressBar pb;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_profile,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        register_btn = view.findViewById(R.id.pregisteration_btn);
        name = view.findViewById(R.id.pname_edt);
        email = view.findViewById(R.id.pemail_edt);
        contact = view.findViewById(R.id.pcontact_edt);
        layout = view.findViewById(R.id.playout_reg);
        pb=view.findViewById(R.id.ppb_profile);
        getProfile();
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
                } else {

                    update();
                }

            }
        });
    }

    private void getProfile() {
        new Profile().execute(Util.getSP(getContext()));
    }

    public class Profile extends AsyncTask<String, String, String> {
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
                JSONObject json = restAPI.UgetProfile(strings[0]);
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
                    String nametv = jsonObject1.optString("data1");
                    String emailtv = jsonObject1.optString("data2");
                    String contacttv = jsonObject1.optString("data3");



                    name.setText(nametv);
                    email.setText(emailtv);
                    contact.setText(contacttv);


                    pb.setVisibility(View.GONE);
                } else {
                    Snackbar.make(layout, "Failed", Snackbar.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void update() {
        String n = name.getText().toString();
        String e = email.getText().toString();
        String h = contact.getText().toString();


        new UpdateProfile().execute(Util.getSP(getContext()), n, e, h);
    }


    public class UpdateProfile extends AsyncTask<String, String, String> {
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
                JSONObject json = restAPI.UUpdateprofile(strings[0], strings[1], strings[2], strings[3]);
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
                String res = jsonObject.getString("status");
                if (res.compareTo("true") == 0) {
                    pb.setVisibility(View.GONE);
                    Snackbar.make(layout, "Profile Updated", Snackbar.LENGTH_SHORT);
                    getProfile();
                } else if (res.compareTo("already") == 0) {
                    pb.setVisibility(View.GONE);
                    Snackbar.make(layout, "Profile Already Exits", Snackbar.LENGTH_SHORT);
                } else {
                    pb.setVisibility(View.GONE);
                    Snackbar.make(layout, "Failed!", Snackbar.LENGTH_SHORT);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
    }
}