package com.example.forumapp.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

public class Util {
    private static ProgressDialog pd;

    public static void setSP(Context con, String uid) {
        SharedPreferences sp = con.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("uid", uid);
        editor.apply();
        editor.commit();
    }

    public static String getSP(Context con) {
        SharedPreferences sp = con.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sp.getString("uid", "");
    }

    public static void ProgressDialogshow(Context context) {
       Progresscancel(context);
        pd = new ProgressDialog(context);
        pd.setMessage("Loading");
        pd.show();

    }
    public static void  Progresscancel(Context context) {
    if (pd!=null) {
        pd.cancel();
    }

    }
}
