package com.sovoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sovoro.utils.AppHelper;

import java.util.HashMap;
import java.util.Map;

// 로딩 화면
public class SoVoRoMainLoading extends AppCompatActivity {

    private ImageView loadingImageView;
    private TextView loadingTextView;

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

        loadingImageView=findViewById(R.id.sovoro_loading_image);
        loadingTextView=findViewById(R.id.sovoro_loading_text);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.loading_rotate);
        loadingImageView.setAnimation(animation);

        AppHelper.setRequestQueue(this);

        final String URL=AppHelper.getURL(LOADING_PATH);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                StringBuilder text = new StringBuilder("response:");
                for (int i = 0; i < response.length(); i++) {
                    text.append("\n").append(response.getJSONObject(i).getString("title"));
                }
                loadingImageView.setimage(text.toString());
                /** 원래 textView.settext(text.toString()); 이었는데 위에 코드를 보니 loadingImageView로 바꾸고 싶었는데, setimage가 없고, 뭘로 해야 될 지 모르겠음,,,**/
                
            } catch (Exception error) {
                textView.setText("error: " + error.getMessage());
            }
        }, error -> {
            textView.setText("error: " + error.getMessage());
        });

        queue.add(jsonArrayRequest);
    }
}