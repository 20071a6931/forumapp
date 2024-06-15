package com.example.forumapp.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.forumapp.LoginActivity;
import com.example.forumapp.R;
import com.example.forumapp.Util.Util;
import com.example.forumapp.Webservices.JSONParse;
import com.example.forumapp.Webservices.RestAPI;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassFrag extends Fragment {

    TextInputEditText oldpass, newpass;
    Button cpbtn;
    LinearLayout layout;
    String uid;
    ProgressBar pb;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_cp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        oldpass = view.findViewById(R.id.cp_oldpassedt);
        newpass = view.findViewById(R.id.cp_newpassedt);
        cpbtn = view.findViewById(R.id.cp_btn);
        layout = view.findViewById(R.id.layout_cp);
        pb = view.findViewById(R.id.pb_cp);
        cpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (oldpass.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(layout, "Enter Old Password", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else if (newpass.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(layout, "Enter New Password", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    cp();
                }
            }

        });

    }

    private void cp() {
        new Cp().execute(Util.getSP(getContext()), oldpass.getText().toString(), newpass.getText().toString());
    }

    public class Cp extends AsyncTask<String, String, String> {

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
                JSONObject json = restAPI.UChangePassword(strings[0], strings[1], strings[2]);
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
                    Util.setSP(getContext(), "");

                    Snackbar snackbar = Snackbar.make(layout, "Password Changed ", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    pb.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(layout, "Failed! Try Again ", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

