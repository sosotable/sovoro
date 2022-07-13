package com.sovoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sovoro.databinding.ActivitySovoroSigninBinding;
import com.sovoro.utils.AppHelper;

import java.util.HashMap;
import java.util.Map;

// 로그인 화면 액티비티
public class SoVoRoSignin extends AppCompatActivity {

    private ActivitySovoroSigninBinding activitySovoroSigninBinding;

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


        // Post request 송신
        // volley 라이브러리의 StringRequest사용
        // JsonObjectRequest, JsonArrayRequest도 존재하지만 해당 프로젝트에서는 StringRequest만 사용할 예정
        final StringRequest stringRequest = new StringRequest(
                // 리퀘스트 형태
                // POST 리퀘스트임
                // 만약 GET(단순 해당 URL의 정보만을 받아옴)의 경우 Request.Method.GET
                Request.Method.POST,
                // AppHelper를 통해 만든 request url
                // http://13.58.48.132:3000/signin
                URL,
                // 리스폰스 리스너 콜백 함수
                new Response.Listener<String>() {
                    // 리스폰스를 String형태로 받아온다
                    @Override
                    public void onResponse(String response) {
                        // switch를 통해 리스폰스 분별
                        // 0일 경우 로그인 인증 완료 main페이지로 넘어김
                        // -1일 경우 로그인 인증 실패
                        switch (response) {
                            case "0": // 인증 성공
                                // Main페이지로 넘어가는 intent
                                Intent intent = new Intent(getApplicationContext(), SoVoRoMain.class);
                                startActivity(intent);
                                break;
                            case "-1": // 인증 실패
                                // id와 password를 빈칸으로 초기화
                                activitySovoroSigninBinding.sovoroId.setText("");
                                activitySovoroSigninBinding.sovoroPassword.setText("");
                                // id 혹은 password가 잘못되었다는 메세지 출력
                                Toast.makeText(
                                        getApplicationContext(),"Wrong Id or Password", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                },
                // 에러 발생 콜백 함수
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 에러 발생시 로그창에 에러를 띄운다
                        System.err.println(error.toString());
                    }
                }) {
            // POST 리퀘스트에서 override한 메세지 송신 객체 getParams
            // Map 형태로 정보를 해당 url로 post
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // 사용자 id와 password를 map에 저장하여 송신한다
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", activitySovoroSigninBinding.sovoroId.getText().toString());
                params.put("password", activitySovoroSigninBinding.sovoroPassword.getText().toString());
                return params;
            }
        };

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
                else
                    AppHelper.requestQueueAdd(stringRequest,"STRING");
            }
        });
    }
}