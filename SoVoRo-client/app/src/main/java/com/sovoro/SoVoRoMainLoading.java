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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import com.sovoro.model.UserInfo;
import com.sovoro.utils.AppHelper;

import com.android.volley.toolbox.Volley;
import com.sovoro.model.Word;
import com.sovoro.utils.AppHelper;
import com.sovoro.utils.RequestOption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.Volley;
import com.sovoro.utils.AppHelper;
import com.sovoro.utils.RequestOption;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// 로딩 화면
public class SoVoRoMainLoading extends AppCompatActivity {

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
        setContentView(R.layout.activity_sovoro_main_loading);

        //바인딩
        binding = ActivitySovoroMainLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //queue 등록
        queue = Volley.newRequestQueue(this);

        //사용자 정보 저장할 JSON Object
        userInfoJson = new JSONObject();

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.loading_rotate);
        binding.sovoroLoadingImage.setAnimation(animation);

        AppHelper.setRequestQueue(this);

        //GET 함수
        final String URL=AppHelper.getURL(LOADING_PATH);

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET,
                AppHelper.getURL(LOADING_PATH),
                null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    /**intent 받아오기**/
                    Intent getIntent = getIntent();
                    String getUserid = getIntent.getStringExtra("userId");
                    String getPassword = getIntent.getStringExtra("password");
                    String getNickname = getIntent.getStringExtra("userNickname");
                    Log.d(TAG, "intent 받아오기");

                    UserInfo information = new UserInfo();
                    information.setUserinfo(getUserid, getPassword, getNickname,"aaaa");
                    Log.d(TAG, "userinfo에 저장");


                    MainWord = response.getJSONObject("MainWord");
                    TestWord1 = response.getJSONObject("TestWord1");
                    TestWord2 = response.getJSONObject("TestWord2");
                    TestWord3 = response.getJSONObject("TestWord3");



                    /**mainword**/
                    String main = response.getString("MainWord");
                    JSONObject mainValue = new JSONObject(main); // MainWord key 에 있는 JSON Object
                    Iterator i = mainValue.keys();
                    List<String> mainKeyList = new ArrayList();  //영어단어 리스트
                    List<String> mainValueList = new ArrayList();  //한글뜻 리스트

                    while(i.hasNext())
                    {
                        String b = i.next().toString();
                        mainKeyList.add(b); // 키 값 저장
                    }
                    for(int m = 0; m<mainKeyList.size();m++)
                    {
                        mainValueList.add(mainValue.getString((String) mainKeyList.get(m)));  // Value 추출
                        new Word("main", mainKeyList.get(m), mainValueList.get(m));
                    }



                    /**testword1**/
                    String test1 = response.getString("TestWord1");
                    JSONObject test1Value = new JSONObject(test1); // MainWord key 에 있는 JSON Object
                    Iterator j = test1Value.keys();
                    List<String> test1KeyList = new ArrayList();  //영어단어 리스트
                    List<String> test1ValueList = new ArrayList();  //한글뜻 리스트
                    while(i.hasNext())
                    {
                        String b = i.next().toString();
                        test1KeyList.add(b); // 키 값 저장
                    }
                    for(int m = 0; m<test1KeyList.size();m++)
                    {
                        test1ValueList.add(test1Value.getString((String) test1KeyList.get(m)));  // Value 추출
                        new Word("test1", test1KeyList.get(m), test1ValueList.get(m));
                    }



                    /**testword2**/
                    String test2 = response.getString("TestWord2");
                    JSONObject test2Value = new JSONObject(test2); // MainWord key 에 있는 JSON Object
                    Iterator k = test2Value.keys();
                    List<String> test2KeyList = new ArrayList();  //영어단어 리스트
                    List<String> test2ValueList = new ArrayList();  //한글뜻 리스트
                    while(i.hasNext())
                    {
                        String b = i.next().toString();
                        test2KeyList.add(b); // 키 값 저장
                    }
                    for(int m = 0; m<test2KeyList.size();m++)
                    {
                        test2ValueList.add(test2Value.getString((String) test2KeyList.get(m)));  // Value 추출
                        new Word("test2", test2KeyList.get(m), test2ValueList.get(m));
                    }



                    /**testword3**/
                    String test3 = response.getString("TestWord3");
                    JSONObject test3Value = new JSONObject(test3); // MainWord key 에 있는 JSON Object
                    Iterator l = test3Value.keys();
                    List<String> test3KeyList = new ArrayList();  //영어단어 리스트
                    List<String> test3ValueList = new ArrayList();  //한글뜻 리스트
                    while(i.hasNext())
                    {
                        String b = i.next().toString();
                        test3KeyList.add(b); // 키 값 저장
                    }
                    for(int m = 0; m<test3KeyList.size();m++)
                    {
                        test3ValueList.add(test3Value.getString((String) test3KeyList.get(m)));  // Value 추출
                        new Word("test3", test3KeyList.get(m), test3ValueList.get(m));
                    }
                    while(i.hasNext())
                    {
                        String b = i.next().toString();
                        test3KeyList.add(b); // 키 값 저장
                    }
                    for(int m = 0; m<test3KeyList.size();m++)
                    {
                        test3ValueList.add(test3Value.getString((String) test3KeyList.get(m)));  // Value 추출
                        new Word(test3KeyList.get(m), test3ValueList.get(m));
                    }

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
        AppHelper.requestQueueAdd(jsonRequest, RequestOption.JSONOBJECT);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }
}