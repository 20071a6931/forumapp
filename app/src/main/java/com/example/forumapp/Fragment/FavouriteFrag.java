package com.example.forumapp.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forumapp.Adapter.FavouriteAdapter;
import com.example.forumapp.Adapter.ForumAdapter;
import com.example.forumapp.Model.Clicklistener;
import com.example.forumapp.Model.Forum;
import com.example.forumapp.R;
import com.example.forumapp.Util.Util;
import com.example.forumapp.Webservices.JSONParse;
import com.example.forumapp.Webservices.RestAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFrag extends Fragment {

    String name, did;
    Forum entity;
    FavouriteAdapter adapter;
    RecyclerView rv;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = view.findViewById(R.id.rv_fav);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        new getFav().execute(Util.getSP(context));
    }

    public class getFav extends AsyncTask<String, String, String> implements Clicklistener {
        List<Forum> forumlist = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Util.ProgressDialogshow(context);
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();
            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.getfavourite(strings[0]);
                data = jp.parse(json);
            } catch (Exception e) {
                data = e.getMessage();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("fav", s);
            try {
                rv.setAdapter(null);
                JSONObject jsonObject = new JSONObject(s);
                JSONArray array = jsonObject.getJSONArray("Data");
                Log.d("Json", array.toString());
                float sum = 0;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObj = array.getJSONObject(i);
                    entity = new Forum();
                    entity.setFavid(jsonObj.getString("data0"));
                    entity.setFid(jsonObj.getString("data2"));
                    entity.setDid(jsonObj.getString("data4"));
                    entity.setUid(jsonObj.getString("data5"));
                    entity.setForumname(jsonObj.getString("data6"));
                    entity.setQuestion(jsonObj.getString("data7"));
                    entity.setDt(jsonObj.getString("data8"));
                    entity.setName(jsonObj.getString("data10"));
                    entity.setEmail(jsonObj.getString("data11"));

                    forumlist.add(entity);

                }
                adapter = new FavouriteAdapter(forumlist, context, this);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                Util.Progresscancel(context);
            } catch (JSONException e) {
                e.printStackTrace();
                Util.Progresscancel(context);
            }
            Util.Progresscancel(context);
        }

        @Override
        public void onClick(String value) {
            if (value.compareTo("true") == 0) {
                onResume();
            }
        }
    }

}
