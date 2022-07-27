package com.sovoro.model;

import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.sovoro.R;
import com.sovoro.SoVoRoMainLoading;
import com.sovoro.databinding.ActivitySovoroSigninBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {

    public static String userid, password, nickname;

    public UserInfo() {
    }


    public static void setUserinfo (String id, String pass, String nick) {
        userid = id;
        password = pass;
        nickname = nick;
    }


}
