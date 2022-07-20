package com.sovoro.model;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.sovoro.SoVoRoMainLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class Userinfo {

    private JSONObject userid;
    private JSONObject password;

    String url = "http://13.58.48.132:3000/loading";

    public static Map<String, JSONObject> userinfoMap = new HashMap<String, JSONObject>();



    /*final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            }
        }) {

        @Override
        public Map<String, String> userinfoMap = new HashMap<String, String>(); {
            Map<String, String> params = new HashMap<String, String>();
            userid = params.ID;
            password = params.PassWord;
            userinfoMap.put("ID", userid);
            userinfoMap.put("PassWord", password);
            return params;
        }
    };*/



    final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                userid = response.getJSONObject("ID");
                password = response.getJSONObject("PassWord");
                userinfoMap.put("userid", userid);
                userinfoMap.put("password", password);


            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("Volley Error",error.toString());
        }
    });


}
