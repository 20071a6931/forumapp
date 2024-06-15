package com.example.forumapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forumapp.Model.Clicklistener;
import com.example.forumapp.Model.Forum;
import com.example.forumapp.R;
import com.example.forumapp.RepliesActivity;
import com.example.forumapp.Util.Util;
import com.example.forumapp.Webservices.JSONParse;
import com.example.forumapp.Webservices.RestAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.myview> {
    List<Forum> Stafflist = new ArrayList<>();
    Context context;
    View v;
    Clicklistener clicklistener;

    public FavouriteAdapter(List<Forum> Stafflist, Context context, Clicklistener clicklistener) {
        this.Stafflist = Stafflist;
        this.context = context;
        this.clicklistener = clicklistener;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        // set the view's size, margins, paddings and layout parameters5
        myview vh = new myview(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {
        Forum student = Stafflist.get(position);
        holder.name.setText("Created by: " + student.getName());
        holder.question.setText(student.getQuestion().trim());
        holder.dt.setText(student.getDt());
        holder.more.setText("Remove from Favorites");
        holder.more.setVisibility(View.GONE);
        holder.forumname.setText(student.getForumname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RepliesActivity.class);
                intent.putExtra("forum", Stafflist.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RemoveFavourite().execute(student.getFavid());
            }
        });

    }

    @Override
    public int getItemCount() {
        return Stafflist.size();
    }

    public class myview extends RecyclerView.ViewHolder {
        TextView name, question, forumname, dt, more;
        ImageView img;

        public myview(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_username);
            question = itemView.findViewById(R.id.item_question);
            forumname = itemView.findViewById(R.id.item_forum);
            dt = itemView.findViewById(R.id.item_dt);
            more = itemView.findViewById(R.id.item_viewreplies);
            img = itemView.findViewById(R.id.item_delete);

        }
    }

    public class RemoveFavourite extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();
            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.RemoveFavourite(strings[0]);
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
                    Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                    clicklistener.onClick("true");
                } else {
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
