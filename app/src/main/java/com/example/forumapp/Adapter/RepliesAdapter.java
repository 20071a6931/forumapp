package com.example.forumapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forumapp.Model.Clicklistener;
import com.example.forumapp.Model.Reply;
import com.example.forumapp.R;
import com.example.forumapp.RepliesActivity;
import com.example.forumapp.Util.Util;
import com.example.forumapp.Webservices.JSONParse;
import com.example.forumapp.Webservices.RestAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.myview> {
    List<Reply> Stafflist = new ArrayList<>();
    Context context;
    View v;
    Clicklistener clicklistener;

    public RepliesAdapter(List<Reply> Stafflist, Context context, Clicklistener clicklistener) {
        this.Stafflist = Stafflist;
        this.context = context;
        this.clicklistener = clicklistener;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply, parent, false);
        // set the view's size, margins, paddings and layout parameters5
        myview vh = new myview(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {
        Reply student = Stafflist.get(position);
        holder.name.setText("Replied by: " + student.getName());
        holder.reply.setText(student.getMessage());
        holder.dt.setText(student.getDt());
        holder.downcount.setText(student.getDownvote());
        holder.upcount.setText(student.getUpvote());
        if (student.getMyvote().equals("Up")) {
            holder.upimg.setImageResource(R.drawable.upgreen);
        } else if (student.getMyvote().equals("Down")) {
            holder.downimg.setImageResource(R.drawable.downred);
        } else {

        }
        holder.upimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpVoteAndDownVote().execute(Util.getSP(context), student.getRid(), "up");
            }
        });
        holder.downimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpVoteAndDownVote().execute(Util.getSP(context), student.getRid(), "down");

            }
        });


    }

    @Override
    public int getItemCount() {
        return Stafflist.size();
    }

    public class myview extends RecyclerView.ViewHolder {
        TextView name, reply, dt, upcount, downcount, forumname;
        ImageView upimg, downimg;

        public myview(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_replyuser);
            reply = itemView.findViewById(R.id.item_reply);
            dt = itemView.findViewById(R.id.item_replydate);
            upcount = itemView.findViewById(R.id.item_upupvote);
            downcount = itemView.findViewById(R.id.item_downvote);
            upimg = itemView.findViewById(R.id.upvoteimg);

            downimg = itemView.findViewById(R.id.downvoteimg);

        }
    }

    public class UpVoteAndDownVote extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Util.ProgressDialogshow(context);
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.updateupanddownvote(strings[0], strings[1], strings[2]);
                data = jp.parse(json);
            } catch (Exception e) {
                data = e.getMessage();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("update", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String response = jsonObject.getString("status");
                if (response.compareTo("true") == 0) {
//                    Util.Progresscancel(context);
                    clicklistener.onClick("true");
                    Toast.makeText(context, "Voted", Toast.LENGTH_SHORT).show();
                } else if (response.compareTo("already") == 0) {
                    Toast.makeText(context, "Already Voted!", Toast.LENGTH_SHORT).show();
                } else {
                    Util.Progresscancel(context);
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

