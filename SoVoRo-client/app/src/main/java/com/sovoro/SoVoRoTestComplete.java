package com.sovoro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sovoro.databinding.ActivitySovoroMainBinding;
import com.sovoro.databinding.ActivitySovoroTestCompleteBinding;
import com.sovoro.model.TestResult;

public class SoVoRoTestComplete extends AppCompatActivity {

    ActivitySovoroTestCompleteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovoro_test_complete);

        binding= ActivitySovoroTestCompleteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String fmt="결과 %s/10";
        binding.result.setText(String.format(fmt, Integer.toString(TestResult.correct)));
    }
}