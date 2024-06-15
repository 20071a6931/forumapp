package com.example.forumapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forumapp.Adapter.ForumAdapter;
import com.example.forumapp.Adapter.RepliesAdapter;
import com.example.forumapp.Fragment.ChatsActivity;
import com.example.forumapp.Model.Clicklistener;
import com.example.forumapp.Model.Forum;
import com.example.forumapp.Model.Reply;
import com.example.forumapp.Util.Util;
import com.example.forumapp.Webservices.JSONParse;
import com.example.forumapp.Webservices.RestAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RepliesActivity extends AppCompatActivity {
    Forum entity;
    Reply entity1;
    TextView question, dt, name;
    Toolbar toolbar;
    EditText reply_edt;
    ImageView sendimg;
    RepliesAdapter adapter;
    RecyclerView rv;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replies);
        entity = (Forum) getIntent().getSerializableExtra("forum");
        question = (TextView) findViewById(R.id.reply_question);
        dt = (TextView) findViewById(R.id.reply_dt);
        name = (TextView) findViewById(R.id.reply_username);
        toolbar = (Toolbar) findViewById(R.id.replytoolbar);
        img = (ImageView) findViewById(R.id.addtofavourite);
        toolbar.setTitle("Replies");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reply_edt = (EditText) findViewById(R.id.edt_reply);
        sendimg = (ImageView) findViewById(R.id.sendimg);
        rv = (RecyclerView) findViewById(R.id.rv_reply);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RepliesActivity.this);
        rv.setLayoutManager(linearLayoutManager);
        img.setVisibility(View.VISIBLE);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddFavourite().execute(entity.getFid(), Util.getSP(getApplicationContext()));
            }
        });
        question.setText(entity.getQuestion());
        dt.setText(entity.getDt());
        name.setText(entity.getName());
        sendimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reply_edt.getText().toString().isEmpty()) {
                    Toast.makeText(RepliesActivity.this, "Enter Something", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current dateTime => " + c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("YYYY/MM/dd HH:mm");
                    String formattedDate = df.format(c.getTime());
                    System.out.println("Format dateTime => " + formattedDate);
                    new AddReply().execute(entity.getFid(), Util.getSP(getApplicationContext()), reply_edt.getText().toString(), formattedDate);
                }
            }
        });

    }

    public class AddFavourite extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Util.ProgressDialogshow(RepliesActivity.this);
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.AddFavourite(strings[0], strings[1]);
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
                    Util.Progresscancel(RepliesActivity.this);
                    finish();
                } else if (response.compareTo("already") == 0) {
                    Util.Progresscancel(RepliesActivity.this);
                    Toast.makeText(RepliesActivity.this, "Already Added to Favourite", Toast.LENGTH_SHORT).show();
                } else {
                    Util.Progresscancel(RepliesActivity.this);
                    Toast.makeText(RepliesActivity.this, "Failed!", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class AddReply extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Util.ProgressDialogshow(RepliesActivity.this);
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();
            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.AddReply(strings[0], strings[1], strings[2], strings[3]);
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
                    Util.Progresscancel(RepliesActivity.this);
                    reply_edt.getText().clear();
                    onResume();

                } else {
                    Util.Progresscancel(RepliesActivity.this);
                    Toast.makeText(RepliesActivity.this, "Failed!", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class getReply extends AsyncTask<String, String, String> implements Clicklistener {
        List<Reply> replyList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Util.ProgressDialogshow(RepliesActivity.this);
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();
            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.getReply(strings[0], strings[1]);
                data = jp.parse(json);
            } catch (Exception e) {
                data = e.getMessage();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("getreply", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray array = jsonObject.getJSONArray("Data");
                Log.d("Json", array.toString());
                float sum = 0;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObj = array.getJSONObject(i);
                    entity1 = new Reply();
                    entity1.setRid(jsonObj.getString("data0"));
                    entity1.setFid(jsonObj.getString("data1"));
                    entity1.setUid(jsonObj.getString("data2"));
                    entity1.setMessage(jsonObj.getString("data3"));
                    entity1.setDt(jsonObj.getString("data4"));
                    entity1.setUpvote(jsonObj.getString("data5"));
                    entity1.setDownvote(jsonObj.getString("data6"));
                    entity1.setName(jsonObj.getString("data8"));
                    entity1.setMyvote(jsonObj.getString("data7"));
                    replyList.add(entity1);
                }
                adapter = new RepliesAdapter(replyList, getApplicationContext(), this);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                Util.Progresscancel(RepliesActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
                Util.Progresscancel(RepliesActivity.this);
            }
            Util.Progresscancel(RepliesActivity.this);
        }

        @Override
        public void onClick(String value) {
            if (value.compareTo("true") == 0) {
                onResume();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new getReply().execute(entity.getFid(), Util.getSP(getApplicationContext()));
    }
}