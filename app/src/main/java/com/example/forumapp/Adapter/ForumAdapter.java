package com.example.forumapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.forumapp.Model.Forum;
import com.example.forumapp.R;
import com.example.forumapp.RepliesActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.myview> {
    List<Forum> Stafflist = new ArrayList<>();
    Context context;
    View v;

    public ForumAdapter(List<Forum> Stafflist, Context context) {
        this.Stafflist = Stafflist;
        this.context = context;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);

        myview vh = new myview(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {
        Forum student = Stafflist.get(position);
        holder.name.setText("Created by: " + student.getName());
        holder.question.setText(student.getQuestion().trim());
        holder.dt.setText(student.getDt());
        holder.forum.setText(student.getForumname());
        holder.img.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RepliesActivity.class);
                intent.putExtra("forum", Stafflist.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return Stafflist.size();
    }

    public class myview extends RecyclerView.ViewHolder {
        TextView name, question, forum, dt, more;
        ImageView img;

        public myview(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_username);
            question = itemView.findViewById(R.id.item_question);
            forum = itemView.findViewById(R.id.item_forum);
            dt = itemView.findViewById(R.id.item_dt);
            more = itemView.findViewById(R.id.item_viewreplies);
            img = itemView.findViewById(R.id.item_delete);

        }
    }


}
