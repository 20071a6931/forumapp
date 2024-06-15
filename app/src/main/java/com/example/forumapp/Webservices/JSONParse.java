package com.example.forumapp.Webservices;

import org.json.JSONObject;

public class JSONParse {

    public static String parse(JSONObject obj) {
        String ans = "";
        try {
            ans = obj.getString("Value");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }
}
