package com.sovoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import com.sovoro.databinding.ActivitySovoroMainLoadingBinding;
import com.sovoro.utils.AppHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// 로딩 화면
public class SoVoRoMainLoading extends AppCompatActivity {

    private TextView loadingTextView;
    private TextView tv;
    private EditText etID;
    private EditText etPW;
    private Button btnSend;
    private RequestQueue queue;
    private ActivitySovoroMainLoadingBinding binding;
    private JSONObject MainWord;
    private JSONObject TestWord1;
    private JSONObject TestWord2;
    private JSONObject TestWord3;
    private JSONObject userInfoJson;
    private static final String TAG = "MAIN";

    // POST메소드 경로
    private final String LOADING_PATH="/loading";

    /**
     * 로딩 화면에서 백엔드 서버와의 통신을 통해 model클래스에 단어 정보를 저장하도록 한다
     * 백엔드 서버와의 통신 완료되고 정보를 잘 받아왔다면 Main Activity로 intent시킬 것
     * - volley library의 StringRequest를 통해 서버와 통신할 것
     * - 가능하다면 JsonArrayRequest나 JsonObjectRequest를 사용해도 좋다
     * 1. 메인 화면에서 보여질 단어 10개를 저장할 수 있는 객체가 필요하다.
     * 2. 테스트 화면에서 보여질 단어 40개(4지선다*10문제)의 정보를 저장할 수 있는 객체가 필요하다
     * 3. 해당 정보를 제어 가능한 controller 클래스를 생성할 것(static으로 선언한다)
     * 4. 기타 필요한 클래스가 있으면 자유롭게 만들도록 한다.
     * **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = "http://13.58.48.132:3000/loading";

        setContentView(R.layout.activity_sovoro_main_loading);

        //바인딩
        binding = ActivitySovoroMainLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //queue 등록
        queue = Volley.newRequestQueue(this);

        //사용자 정보 저장할 JSON Object
        userInfoJson = new JSONObject();

        loadingTextView=findViewById(R.id.sovoro_loading_text);

        binding.sovoroLoadingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    userInfoJson.put("id",binding.sovoroLoadingText.getText().toString());
                    userInfoJson.put("password",binding.sovoroLoadingText.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                queue.add(getJsonObjectRequest());
            }
        });

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.loading_rotate);
        loadingTextView.setAnimation(animation);

        AppHelper.setRequestQueue(this);

        //GET 함수
        final String URL=AppHelper.getURL(LOADING_PATH);
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    MainWord = response.getJSONObject("MainWord");
                    TestWord1 = response.getJSONObject("TestWord1");
                    TestWord2 = response.getJSONObject("TestWord2");
                    TestWord3 = response.getJSONObject("TestWord3");
                    loadingTextView.setText("MainWord" + MainWord + "\n" + "TestWord1" + TestWord1 + "\n" + "TestWord2" + TestWord2 + "\n" + "TestWord3" + TestWord3);
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

        jsonRequest.setTag(TAG);
        queue.add(jsonRequest);

        //POST 함수
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                tv.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", etID.getText().toString());
                params.put("PassWord", etPW.getText().toString());
                return params;
            }
        };

        stringRequest.setTag(TAG);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue.add(stringRequest);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }
}