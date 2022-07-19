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
import com.android.volley.toolbox.JsonObjectRequest;
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
    private JSONObject signupInfo;
    private final String SIGNUP_PATH="/signup";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovoro_signup);

        /**바인딩 위치 변경**/
        /**onclick 내부에서 바인딩을 설정할 경우 외부에서는 사용할 수 없게 돼요**/
        activitySovoroSignupBinding=ActivitySovoroSignupBinding.inflate(getLayoutInflater());
        setContentView(activitySovoroSignupBinding.getRoot());

        String URL = AppHelper.getURL(SIGNUP_PATH);
        signupInfo=new JSONObject();

        activitySovoroSignupBinding.sovoroSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    signupInfo.put("userId", activitySovoroSignupBinding.sovoroSignupId.getText().toString());
                    signupInfo.put("password", activitySovoroSignupBinding.sovorosignuppassword.getText().toString());
                    signupInfo.put("userNickname", activitySovoroSignupBinding.sovorosignupnickname.getText().toString());
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("AAAA",response.toString());
                        try {
                            if (response.getBoolean("success"))
                            {
                                Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), SoVoRoSignin.class);
                                startActivity(intent);
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
                        Log.d("SignUperr",error.toString());
                        Toast.makeText(getApplicationContext(),"회원가입 처리 에러",Toast.LENGTH_SHORT).show();
                        return;
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(URL, signupInfo, responseListener, errorListener);
                queue = Volley.newRequestQueue(SoVoRoSignup.this);
                queue.add(registerRequest);
            }
        });
}

    public class RegisterRequest extends JsonObjectRequest {
        private Map<String, String> map;

        public RegisterRequest(String URL, JSONObject jsonObject, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(
                    Method.POST,
                    URL,
                    jsonObject,
                    listener,
                    errorListener);
        }
    }
}