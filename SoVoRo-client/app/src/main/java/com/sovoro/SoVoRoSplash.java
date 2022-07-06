package com.sovoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SoVoRoSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovoro_splash);

        /**스플래시 스크린 핸들러**/
        new Handler().postDelayed(new Runnable() {
            // 현재 deprecated상태임
            // 대체할 수 있는 다른 방법이 있을까?
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent intent = new Intent(getApplicationContext(), SoVoRoSignin.class);
                startActivity(intent);
                // close this activity
                finish();
            }
            // 스플래시 스크린 타임 3초(ms단위)
        }, 3000);

    }
}