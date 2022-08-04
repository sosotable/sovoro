package com.sovoro;

import static com.sovoro.model.WordOption.MAINWORD;
import static com.sovoro.model.WordOption.TESTWORD_1;
import static com.sovoro.model.WordOption.TESTWORD_2;
import static com.sovoro.model.WordOption.TESTWORD_3;

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

import com.sovoro.databinding.FragmentSovoroTest10Binding;
import com.sovoro.databinding.FragmentSovoroTest1Binding;
import com.sovoro.databinding.FragmentSovoroTest2Binding;
import com.sovoro.databinding.FragmentSovoroTest3Binding;
import com.sovoro.databinding.FragmentSovoroTest4Binding;
import com.sovoro.databinding.FragmentSovoroTest5Binding;
import com.sovoro.databinding.FragmentSovoroTest6Binding;
import com.sovoro.databinding.FragmentSovoroTest7Binding;
import com.sovoro.databinding.FragmentSovoroTest8Binding;
import com.sovoro.databinding.FragmentSovoroTest9Binding;
import com.sovoro.databinding.FragmentSovoroWord10Binding;
import com.sovoro.databinding.FragmentSovoroWord1Binding;
import com.sovoro.databinding.FragmentSovoroWord2Binding;
import com.sovoro.databinding.FragmentSovoroWord3Binding;
import com.sovoro.databinding.FragmentSovoroWord4Binding;
import com.sovoro.databinding.FragmentSovoroWord5Binding;
import com.sovoro.databinding.FragmentSovoroWord6Binding;
import com.sovoro.databinding.FragmentSovoroWord7Binding;
import com.sovoro.databinding.FragmentSovoroWord8Binding;
import com.sovoro.databinding.FragmentSovoroWord9Binding;
import com.sovoro.model.DailyWords;
import com.sovoro.model.UserInfo;
import com.sovoro.model.WordOption;
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
import com.sovoro.wordview.SoVoRoWord1;
import com.sovoro.wordview.SoVoRoWord2;
import com.sovoro.wordview.SoVoRoWord3;
import com.sovoro.wordview.SoVoRoWord4;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// 로딩 화면
public class SoVoRoMainLoading extends AppCompatActivity {

    private RequestQueue queue;
    private ActivitySovoroMainLoadingBinding binding;

    private FragmentSovoroWord1Binding word1;
    private FragmentSovoroWord2Binding word2;
    private FragmentSovoroWord3Binding word3;
    private FragmentSovoroWord4Binding word4;
    private FragmentSovoroWord5Binding word5;
    private FragmentSovoroWord6Binding word6;
    private FragmentSovoroWord7Binding word7;
    private FragmentSovoroWord8Binding word8;
    private FragmentSovoroWord9Binding word9;
    private FragmentSovoroWord10Binding word10;

    private FragmentSovoroTest1Binding Test1;
    private FragmentSovoroTest2Binding Test2;
    private FragmentSovoroTest3Binding Test3;
    private FragmentSovoroTest4Binding Test4;
    private FragmentSovoroTest5Binding Test5;
    private FragmentSovoroTest6Binding Test6;
    private FragmentSovoroTest7Binding Test7;
    private FragmentSovoroTest8Binding Test8;
    private FragmentSovoroTest9Binding Test9;
    private FragmentSovoroTest10Binding Test10;

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

                    information.setUserinfo(getUserid, getPassword, getNickname);
                    Log.d(TAG, information.usercheck());


                    MainWord = response.getJSONObject("MainWord");
                    TestWord1 = response.getJSONObject("TestWord1");
                    TestWord2 = response.getJSONObject("TestWord2");
                    TestWord3 = response.getJSONObject("TestWord3");


                    /**System.out.println(MainWord);

                    /**mainword

                    ArrayList<Word> mainList = new ArrayList<Word>();
                    //int s = mainList.size();

                    for (int i = 0; i<=MainWord.length();i++) {
                    //Word main = new Word(MainWord.names().get(0), MainWord.get((String) MainWord.names().get(0)));

                        mainList.add(MainWord.names().get(i), MainWord.get((String)MainWord.names().get(i)));
                    }

                    System.out.println(MainWord);
                    System.out.println(mainList.get(0));
                    Log.d(TAG, mainList.toString());
                    Log.d(TAG, mainList.get(0).toString());**/




                    /**ArrayList<Word> mainList = new ArrayList<Word>();
                    Iterator i = MainWord.keys();
                    while(i.hasNext())
                    {
                        String b = i.next().toString();
                        Word main = new Word(MainWord.names().get(0), b);
                        System.out.println(main);
                        mainList.add(main);
                        System.out.println(main.getEnglishWord());
                        /*for (int k = 0; k<MainWord.length();k++) {
                            Word main = new Word(MainWord.names().get(0), MainWord.get((String) MainWord.names().get(0)));
                            System.out.println(main);
                            mainList.add(main);
                            //System.out.println("list" + mainList);

                            //mainList.add(MainWord.names().get(k), MainWord.get((String)MainWord.names().get(k)));
                        }

                    }

                    System.out.println(MainWord.length());
                    System.out.println(mainList.toArray().length);

                    System.out.println(MainWord);
                    System.out.println(mainList.get(0));
                    System.out.println(mainList);
                    System.out.println(mainList.get(0).getjsonFormat());
                    System.out.println(mainList.get(0).getKoreanWord());

                    Log.d(TAG, mainList.get(0).toString());
                    Log.d(TAG, mainList.toString());**/




                    /**mainword**/
                    String main = response.getString("MainWord");
                    JSONObject mainValue = new JSONObject(main); // MainWord key 에 있는 JSON Object
                    Iterator i = mainValue.keys();
                    List<String> mainKeyList = new ArrayList();  //영어단어 리스트
                    List<String> mainValueList = new ArrayList();  //한글뜻 리스트
                    ArrayList<Word> mainList = new ArrayList<Word>(); //mainword 리스트

                    while(i.hasNext())
                    {
                        String b = i.next().toString();
                        mainKeyList.add(b); // 키 값 저장
                    }
                    for(int m = 0; m<mainKeyList.size();m++)
                    {
                        mainValueList.add(mainValue.getString((String) mainKeyList.get(m)));  // Value 추출
                        Word word = new Word(mainKeyList.get(m), mainValueList.get(m));
                        mainList.add(word);
                    }
                    Log.d(TAG, mainList.get(0).check());

                    DailyWords daily = new DailyWords();
                    daily.setWordMap(MAINWORD, mainList);
                    Log.d(TAG,daily.getWordList(MAINWORD).get(0).toString());





                    /**testword1**/
                    String test1 = response.getString("TestWord1");
                    JSONObject test1Value = new JSONObject(test1); // MainWord key 에 있는 JSON Object
                    Iterator j = test1Value.keys();
                    List<String> test1KeyList = new ArrayList();  //영어단어 리스트
                    List<String> test1ValueList = new ArrayList();  //한글뜻 리스트
                    ArrayList<Word> test1List = new ArrayList<Word>(); //test1word 리스트

                    while(j.hasNext())
                    {
                        String b = j.next().toString();
                        test1KeyList.add(b); // 키 값 저장
                    }
                    for(int m = 0; m<test1KeyList.size();m++)
                    {
                        test1ValueList.add(test1Value.getString((String) test1KeyList.get(m)));  // Value 추출
                        Word word = new Word(test1KeyList.get(m), test1ValueList.get(m));
                        test1List.add(word);
                    }
                    Log.d(TAG, test1List.get(0).check());

                    daily.setWordMap(TESTWORD_1, test1List);
                    Log.d(TAG, daily.getWordList(TESTWORD_1).get(0).toString());



                    /**testword2**/
                    String test2 = response.getString("TestWord2");
                    JSONObject test2Value = new JSONObject(test2); // MainWord key 에 있는 JSON Object
                    Iterator k = test2Value.keys();
                    List<String> test2KeyList = new ArrayList();  //영어단어 리스트
                    List<String> test2ValueList = new ArrayList();  //한글뜻 리스트
                    ArrayList<Word> test2List = new ArrayList<Word>(); //test2word 리스트

                    while(k.hasNext())
                    {
                        String b = k.next().toString();
                        test2KeyList.add(b); // 키 값 저장
                    }
                    for(int m = 0; m<test2KeyList.size();m++)
                    {
                        test2ValueList.add(test2Value.getString((String) test2KeyList.get(m)));  // Value 추출
                        Word word = new Word(test2KeyList.get(m), test2ValueList.get(m));
                        test2List.add(word);
                    }
                    Log.d(TAG, test2List.get(0).check());

                    daily.setWordMap(TESTWORD_2, test2List);
                    Log.d(TAG, daily.getWordList(TESTWORD_2).get(0).toString());



                    /**testword3**/
                    String test3 = response.getString("TestWord3");
                    JSONObject test3Value = new JSONObject(test3); // MainWord key 에 있는 JSON Object
                    Iterator l = test3Value.keys();
                    List<String> test3KeyList = new ArrayList();  //영어단어 리스트
                    List<String> test3ValueList = new ArrayList();  //한글뜻 리스트
                    ArrayList<Word> test3List = new ArrayList<Word>(); //test3word 리스트

                    while(l.hasNext())
                    {
                        String b = l.next().toString();
                        test3KeyList.add(b); // 키 값 저장
                    }
                    for(int m = 0; m<test3KeyList.size();m++)
                    {
                        test3ValueList.add(test3Value.getString((String) test3KeyList.get(m)));  // Value 추출
                        Word word = new Word(test3KeyList.get(m), test3ValueList.get(m));
                        test3List.add(word);
                    }
                    Log.d(TAG, test3List.get(0).check());

                    daily.setWordMap(TESTWORD_3, test3List);

                    Log.d(TAG, daily.getWordList(TESTWORD_3).get(0).toString());
                    Log.d(TAG, "main: " + daily.getWordList(MAINWORD).get(0).toString());
                    Log.d(TAG, "1: " + daily.getWordList(TESTWORD_1).get(0).toString());
                    Log.d(TAG, "2: " + daily.getWordList(TESTWORD_2).get(0).toString());



                    /**word 화면**/
                    word1 = FragmentSovoroWord1Binding.inflate(getLayoutInflater());
                    word2 = FragmentSovoroWord2Binding.inflate(getLayoutInflater());
                    word3 = FragmentSovoroWord3Binding.inflate(getLayoutInflater());
                    word4 = FragmentSovoroWord4Binding.inflate(getLayoutInflater());
                    word5 = FragmentSovoroWord5Binding.inflate(getLayoutInflater());
                    word6 = FragmentSovoroWord6Binding.inflate(getLayoutInflater());
                    word7 = FragmentSovoroWord7Binding.inflate(getLayoutInflater());
                    word8 = FragmentSovoroWord8Binding.inflate(getLayoutInflater());
                    word9 = FragmentSovoroWord9Binding.inflate(getLayoutInflater());
                    word10 = FragmentSovoroWord10Binding.inflate(getLayoutInflater());


                    //SoVoRoWord1 Word1 = new SoVoRoWord1();
                    //Log.d(TAG, "check  " + word1.sovoroWordEng1.getText());
                    word1.sovoroWordEng1.setText(daily.getWordList(MAINWORD).get(0).getEnglishWord());
                    //Log.d(TAG, "check  " + word1.sovoroWordEng1.getText());
                    word2.sovoroWordEng2.setText(daily.getWordList(MAINWORD).get(1).getEnglishWord());
                    //Log.d(TAG, "check  " + word2.sovoroWordEng2.getText());
                    word3.sovoroWordEng3.setText(daily.getWordList(MAINWORD).get(2).getEnglishWord());
                    word4.sovoroWordEng4.setText(daily.getWordList(MAINWORD).get(3).getEnglishWord());
                    word5.sovoroWordEng5.setText(daily.getWordList(MAINWORD).get(4).getEnglishWord());
                    word6.sovoroWordEng6.setText(daily.getWordList(MAINWORD).get(5).getEnglishWord());
                    word7.sovoroWordEng7.setText(daily.getWordList(MAINWORD).get(6).getEnglishWord());
                    word8.sovoroWordEng8.setText(daily.getWordList(MAINWORD).get(7).getEnglishWord());
                    word9.sovoroWordEng9.setText(daily.getWordList(MAINWORD).get(8).getEnglishWord());
                    word10.sovoroWordEng10.setText(daily.getWordList(MAINWORD).get(9).getEnglishWord());
                    Log.d(TAG, "check  " + word1.sovoroWordEng1.getText() + " " + word2.sovoroWordEng2.getText()+
                            " " + word9.sovoroWordEng9.getText()+ " " +word10.sovoroWordEng10.getText());


                    /**test 화면**/
                    Test1 = FragmentSovoroTest1Binding.inflate(getLayoutInflater());
                    Test2 = FragmentSovoroTest2Binding.inflate(getLayoutInflater());
                    Test3 = FragmentSovoroTest3Binding.inflate(getLayoutInflater());
                    Test4 = FragmentSovoroTest4Binding.inflate(getLayoutInflater());
                    Test5 = FragmentSovoroTest5Binding.inflate(getLayoutInflater());
                    Test6 = FragmentSovoroTest6Binding.inflate(getLayoutInflater());
                    Test7 = FragmentSovoroTest7Binding.inflate(getLayoutInflater());
                    Test8 = FragmentSovoroTest8Binding.inflate(getLayoutInflater());
                    Test9 = FragmentSovoroTest9Binding.inflate(getLayoutInflater());
                    Test10 = FragmentSovoroTest10Binding.inflate(getLayoutInflater());

                    int random = (int)(Math.random()*10);
                    Test1.sovoroQuestion1.setText(daily.getWordList(TESTWORD_1).get(random).getEnglishWord());




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