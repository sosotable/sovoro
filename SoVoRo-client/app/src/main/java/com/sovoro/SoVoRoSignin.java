package com.sovoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.sovoro.databinding.ActivitySovoroSigninBinding;
import com.sovoro.utils.AppHelper;
import com.sovoro.utils.RequestOption;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// 로그인 화면 액티비티
public class SoVoRoSignin extends AppCompatActivity {

    private ActivitySovoroSigninBinding activitySovoroSigninBinding;
    private JSONObject userInfo=new JSONObject();
    private String userId, password;
    // request/response 경로
    // http://address:port/path
    private final String PATH="/signin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovoro_signin);

        // 프로젝트 내부 여러 기능들을 제공하는 AppHelper 클래스 객체(static)
        AppHelper.setRequestQueue(this);

        // AppHelper의 getURL메소드를 통해 post요청을 보낼 url 생성
        // PATH는 /signin
        final String URL=AppHelper.getURL(PATH);

        activitySovoroSigninBinding=ActivitySovoroSigninBinding.inflate(getLayoutInflater());
        setContentView(activitySovoroSigninBinding.getRoot());

        // 버튼 클릭 이벤트 처리
        // 로그인 버튼 입력하였을때의 이벤트
        activitySovoroSigninBinding.sovoroSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 만약 id 혹은 password 입력창이 비어있는 경우
                // id혹은 passwordc창이 비어있다는 에러 메세지 팝업
                if(activitySovoroSigninBinding.sovoroId.getText().toString().isEmpty()
                        ||activitySovoroSigninBinding.sovoroPassword.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"ID or Password is empty",Toast.LENGTH_LONG).show();
                    // id와 password입력창에 값이 있다면 리퀘스트 송신
                else {
                    try {
                        userId=activitySovoroSigninBinding.sovoroId.getText().toString();
                        password=activitySovoroSigninBinding.sovoroPassword.getText().toString();
                        userInfo.put("userid", userId);
                        userInfo.put("password", password);
                        AppHelper.requestQueueAdd(getJsonObjectRequest(), RequestOption.JSONOBJECT);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    JsonObjectRequest getJsonObjectRequest() {
        return new JsonObjectRequest(
                Request.Method.POST,
                AppHelper.getURL(PATH),
                userInfo,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("returnValue")) {
                        Intent intent = new Intent(getApplicationContext(), SoVoRoMainLoading.class);
                        intent.putExtra("userId",userId);
                        intent.putExtra("password",password);
                        intent.putExtra("userNickname",response.getString("userNickname"));
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Wrong Id or password",Toast.LENGTH_LONG)
                                .show();
                    }
                }
                catch (JSONException e) {
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
}