package com.sovoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.sovoro.databinding.ActivitySovoroSignUpSelectBinding;

public class SoVoRoSignUpSelect extends AppCompatActivity {

    ActivitySovoroSignUpSelectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovoro_sign_up_select);

        binding=ActivitySovoroSignUpSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /**
         * 여기에 소셜 회원가입 부분 처리
         * **/


        /**일반 회원가입**/
        binding.signup.setOnClickListener(v->{
            Intent intent=new Intent(getApplicationContext(),SoVoRoSignup.class);
            startActivity(intent);
        });
    }
}