package com.sovoro;

import static android.view.KeyEvent.KEYCODE_ENTER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.sovoro.model.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SoVoRoSearch extends AppCompatActivity {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://13.58.48.132:3000");
        } catch (URISyntaxException e) {}
    }
    private ArrayList<Word> wordArrayList;
    private RecyclerView recyclerView;
    private SoVoRoWordSearchAdapter adapter;
    InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovoro_search);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = findViewById(R.id.searchResult) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        wordArrayList=new ArrayList<Word>();
        wordArrayList.add(new Word("",""));
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new SoVoRoWordSearchAdapter(wordArrayList) ;
        recyclerView.setAdapter(adapter) ;

        mSocket.on("search result",searchResult);
        mSocket.connect();

        AppCompatEditText editText=findViewById(R.id.searchWord);
        AppCompatButton button=findViewById(R.id.searchBtn);
        AppCompatImageView imageView=findViewById(R.id.back);

        imageView.setOnClickListener(view ->{
            finish();
        });

        editText.setOnClickListener(v ->{
            editText.setCursorVisible(true);
        });

        button.setOnClickListener(view -> {
            mSocket.emit("word search", editText.getText().toString());
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            editText.setText("");
            editText.setCursorVisible(false);
        });
        editText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                mSocket.emit("word search", editText.getText().toString());
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                editText.setText("");
                editText.setCursorVisible(false);
            }
            return true;
        });
    }
    private Emitter.Listener searchResult = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray jsonArr = (JSONArray)args[0];
                    wordArrayList=new ArrayList<>();
                    try {
                        for(int i=0; i <jsonArr.length();i++) {
                            JSONObject obj= jsonArr.getJSONObject(i);
                            wordArrayList.add(new Word(obj.getString("engword"), obj.getString("korword")));
                        }
                        adapter.setData(wordArrayList);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

}