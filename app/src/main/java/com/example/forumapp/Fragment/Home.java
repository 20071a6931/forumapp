package com.example.forumapp.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forumapp.Adapter.ForumAdapter;
import com.example.forumapp.Model.Domain;
import com.example.forumapp.Model.Forum;
import com.example.forumapp.R;
import com.example.forumapp.Util.Util;
import com.example.forumapp.Webservices.JSONParse;
import com.example.forumapp.Webservices.RestAPI;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Home extends Fragment {
    Domain entity;
    ChipGroup chipGroup;
    Context context;
    ForumAdapter adapter;
    RecyclerView rv;
    Forum entity1;
    ExtendedFloatingActionButton fab;

    List<Domain> domainList = new ArrayList<>();
    String did,name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chipGroup = view.findViewById(R.id.chip_group);
        rv = view.findViewById(R.id.rv_home);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);
        new getDomain().execute();
    }


    public class getDomain extends AsyncTask<String, String, String> {

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
                JSONObject json = restAPI.getDomains();
                data = jp.parse(json);
            } catch (Exception e) {
                data = e.getMessage();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("getdomain", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray array = jsonObject.getJSONArray("Data");
                Log.d("Json", array.toString());
                float sum = 0;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObj = array.getJSONObject(i);
                    entity = new Domain();
                    entity.setDid(jsonObj.getString("data0"));
                    entity.setDomainName(jsonObj.getString("data1"));
                    domainList.add(entity);

                }

                for (int i = 0; i < domainList.size(); i++) {
                    Chip chip = new Chip(context);
                    String name = domainList.get(i).getDomainName();
                    String did = domainList.get(i).getDid();
                    chip.setText(name);
                    chip.setChipBackgroundColorResource(R.color.grey);

                    // Set text color
                    chip.setTextColor(ContextCompat.getColor(context, R.color.black));
                    // Set padding
                    chip.setPadding(10, 3, 16, 3);
                    // Set corner radius
                    chip.setChipCornerRadius(16);
                    chipGroup.addView(chip);
                    chip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ChatsActivity.class);
                            intent.putExtra("name", chip.getText().toString());
                            intent.putExtra("did", did);
                            startActivity(intent);
                        }
                    });
                }

                Util.Progresscancel(context);
            } catch (JSONException e) {
                e.printStackTrace();
                Util.Progresscancel(context);
            }
            Util.Progresscancel(context);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        new getQuestion().execute(Util.getSP(context));
    }

    public class getQuestion extends AsyncTask<String, String, String> {
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
                JSONObject json = restAPI.getMyForum(strings[0]);
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
                JSONArray array = jsonObject.getJSONArray("Data");
                Log.d("Json", array.toString());
                float sum = 0;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObj = array.getJSONObject(i);
                    entity1 = new Forum();
                    entity1.setFid(jsonObj.getString("data0"));
                    entity1.setDid(jsonObj.getString("data1"));
                    entity1.setUid(jsonObj.getString("data2"));
                    entity1.setForumname(jsonObj.getString("data3"));
                    entity1.setQuestion(jsonObj.getString("data4"));
                    entity1.setDt(jsonObj.getString("data5"));
                    entity1.setName(jsonObj.getString("data7"));
                    entity1.setEmail(jsonObj.getString("data8"));
                    forumlist.add(entity1);
                }
                adapter = new ForumAdapter(forumlist, context);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Util.Progresscancel(context);
            } catch (JSONException e) {
                e.printStackTrace();
                Util.Progresscancel(context);
            }
            Util.Progresscancel(context);
        }
    }

    private void opendialog() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_question);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        Button submit = dialog.findViewById(R.id.submit_question);
        EditText edt = dialog.findViewById(R.id.edt_question);
        Spinner spinner = dialog.findViewById(R.id.spinner_category);
        if (domainList.size()>0){
            ArrayAdapter aa = new ArrayAdapter(context,android.R.layout.simple_spinner_item,domainList);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(aa);
            spinner.setAdapter(aa);
        }else {
            new getDomain().execute();
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               name=domainList.get(position).getDomainName();
               did=domainList.get(position).getDid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Enter Something", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current dateTime => " + c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("YYYY/MM/dd HH:mm");
                    String formattedDate = df.format(c.getTime());
                    System.out.println("Format dateTime => " + formattedDate);
                    new AddQuestion().execute(did, Util.getSP(context), name, edt.getText().toString(), formattedDate);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public class AddQuestion extends AsyncTask<String, String, String> {
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
                JSONObject json = restAPI.AddQuestion(strings[0], strings[1], strings[2], strings[3], strings[4]);
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
                    Util.Progresscancel(context);
                    onResume();


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
