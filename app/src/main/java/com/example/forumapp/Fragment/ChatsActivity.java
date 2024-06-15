package com.example.forumapp.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.forumapp.Adapter.ForumAdapter;
import com.example.forumapp.LoginActivity;
import com.example.forumapp.Model.Domain;
import com.example.forumapp.Model.Forum;
import com.example.forumapp.R;
import com.example.forumapp.Util.Util;
import com.example.forumapp.Webservices.JSONParse;
import com.example.forumapp.Webservices.RestAPI;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {
    Toolbar toolbar;
    String name, did;
    Forum entity;
    ForumAdapter adapter;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        name = getIntent().getStringExtra("name");
        did = getIntent().getStringExtra("did");
        toolbar = (Toolbar) findViewById(R.id.toolbaarchat);
        toolbar.setTitle(name + " Forum");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv = (RecyclerView) findViewById(R.id.rv_query);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatsActivity.this);
        rv.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new getQuestion().execute(did, Util.getSP(getApplicationContext()), name);
    }

    public class getQuestion extends AsyncTask<String, String, String> {
        List<Forum> forumlist = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Util.ProgressDialogshow(ChatsActivity.this);
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();
            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.getForum(strings[0], strings[1], strings[2]);
                data = jp.parse(json);
            } catch (Exception e) {
                data = e.getMessage();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("getchat", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray array = jsonObject.getJSONArray("Data");
                Log.d("Json", array.toString());
                float sum = 0;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObj = array.getJSONObject(i);
                    entity = new Forum();
                    entity.setFid(jsonObj.getString("data0"));
                    entity.setDid(jsonObj.getString("data1"));
                    entity.setUid(jsonObj.getString("data2"));
                    entity.setForumname(jsonObj.getString("data3"));
                    entity.setQuestion(jsonObj.getString("data4"));
                    entity.setDt(jsonObj.getString("data5"));
                    entity.setName(jsonObj.getString("data7"));
                    entity.setEmail(jsonObj.getString("data8"));

                    forumlist.add(entity);

                }
                adapter = new ForumAdapter(forumlist, getApplicationContext());
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                Util.Progresscancel(ChatsActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
                Util.Progresscancel(ChatsActivity.this);
            }
            Util.Progresscancel(ChatsActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}