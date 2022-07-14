package com.sovoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sovoro.databinding.ActivitySovoroCommentBinding;
import com.sovoro.databinding.ActivitySovoroSigninBinding;
import com.sovoro.databinding.ActivitySovoroSignupBinding;
import com.sovoro.utils.AppHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// 회원가입 액티비티
public class SoVoRoSignup extends AppCompatActivity {
    private ActivitySovoroSignupBinding activitySovoroSignupBinding;

    private final String SIGNUP_PATH="/signup";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovoro_signup);

        AppHelper.setRequestQueue(this);


        final String URL = AppHelper.getURL(SIGNUP_PATH);

        activitySovoroSignupBinding = ActivitySovoroSignupBinding.inflate(getLayoutInflater());
        setContentView(activitySovoroSignupBinding.getRoot());

       /* private JsonObjectRequest getJsonObjectRequest() {
            return new JsonObjectRequest(
                    Request.Method.POST,
                    URL,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("volley response", response.toString());
                            try {
                                if (response.getString("result").equals("0"))
                                {
                                    Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), SoVoRoSignin.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_LONG).show();
                                }
                            } catch(JSONException e) {e.printStackTrace();}
                        }


            );
        }
        }*/


        /*final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("volley response", response.toString());
                        try {
                            if (response.getString("result").equals("0")) {
                                Intent intent = new Intent(getApplicationContext(), SoVoRoMain.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 에러 발생시 로그창에 에러를 띄운다
                        System.err.println(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // 사용자 id와 password를 map에 저장하여 송신한다
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", activitySovoroSignupBinding.sovoroSignupId.getText().toString());
                params.put("password", activitySovoroSignupBinding.sovorosignuppassword.getText().toString());
                params.put("password", activitySovoroSignupBinding.sovorosignupnickname.getText().toString());
                return params;
            }
    };*/

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), SoVoRoSignin.class);
                        startActivity(intent);
                        //finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_LONG).show();
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        };

        RegisterRequest registerRequest = new RegisterRequest(activitySovoroSignupBinding.sovoroSignupId, activitySovoroSignupBinding.sovorosignuppassword, activitySovoroSignupBinding.sovorosignupnickname, reponseListener);
        RequestQueue queue = Volley.newRequestQueue(SoVoRoSignup.this);
        queue.add(registerRequest);


    }

    public class RegisterRequest extends StringRequest {
        final static private String URL ="";
        private Map<String, String> map;

        public RegisterRequest(String userid, String password, String nickname, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);

            map = new HashMap<>();
            map.put("userid", userid);
            map.put("password", password);
            map.put("nickname", nickname);
        }

        @Override
        protected Map<String, String> getMap() throws AuthFailureError{
            return map;
        }
    }
}