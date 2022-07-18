package com.sovoro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private RequestQueue queue;
    private final String SIGNUP_PATH="/signup";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovoro_signup);

        activitySovoroSignupBinding.sovoroSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = AppHelper.getURL(SIGNUP_PATH);
                String userid = activitySovoroSignupBinding.sovoroSignupId.getText().toString();
                String password = activitySovoroSignupBinding.sovorosignuppassword.getText().toString();
                String nickname = activitySovoroSignupBinding.sovorosignupnickname.getText().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Signup",response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(SoVoRoSignup.this, SoVoRoSignin.class);
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

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"회원가입 처리 에러",Toast.LENGTH_SHORT).show();
                        return;
                    }
                };



                RegisterRequest registerRequest = new RegisterRequest(URL, userid, password, nickname, responseListener, errorListener);
                queue = Volley.newRequestQueue(SoVoRoSignup.this);
                queue.add(registerRequest);
                //AppHelper.getURL(SIGNUP_PATH),
                // activitySovoroSignupBinding.sovoroSignupId.getText().toString(),
                // activitySovoroSignupBinding.sovorosignuppassword.getText().toString(),
                // activitySovoroSignupBinding.sovorosignupnickname.getText().toString(),
                //responseListener
                //);

            }
        });



}

    public class RegisterRequest extends StringRequest {
        private Map<String, String> map;




        //private String userid;
        //private String password;
        //private String nickname;

        /*public RegisterRequest(String userid, String password, String nickname, String URL, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            map = new HashMap<>();
        }*/

        public RegisterRequest(String URL, String userid, String password, String nickname, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(Method.POST, URL, listener, errorListener);

            map = new HashMap<>();
            map.put("userid", userid);
            map.put("password", password);
            map.put("nickname", nickname);
        }

        /*public void insertIntoMap(String key, String value) {
            map.put(key,value);
        }*/
        @Nullable
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }



    }


}

    /*public class RegisterRequest extends StringRequest {
        private Map<String, String> map;

        private String userid;
        private String password;
        private String nickname;

        public RegisterRequest(String userid, String password, String URL, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            map = new HashMap<>();
        }

        public RegisterRequest(String URL,String userid, String password, String nickname, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);

            map = new HashMap<>();
            map.put("userid", userid);
            map.put("password", password);
            map.put("nickname", nickname);
        }
        public void insertIntoMap(String key, String value) {
            map.put(key,value);
        }
        @Nullable
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }*/





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