package com.sovoro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.sovoro.databinding.ActivitySovoroNotificationBinding;
import com.sovoro.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.function.IntToDoubleFunction;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SoVoRoNotification extends AppCompatActivity {

    ActivitySovoroNotificationBinding binding;

    private SoVoRoNotificaionAdapter soVoRoNotificaionAdapter;
    private LinearLayoutManager mLayoutManager;

    private JSONObject userInfo=new JSONObject();
    {
        try {
            userInfo.put("userId", UserInfo.userid);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private Socket socket;
    {
        try {
            socket = IO.socket("http://13.58.48.132:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovoro_notification);

        socket.emit("read notification",userInfo);
        socket.on("read notification",readNotification);
        socket.connect();

        binding=ActivitySovoroNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<String> alist=new ArrayList<String>();

        soVoRoNotificaionAdapter = new SoVoRoNotificaionAdapter(alist);

        // 레이아웃 매니저를 통해 역순 출력
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        if(mLayoutManager != null)
            mLayoutManager.scrollToPositionWithOffset(0, 0);

        binding.sovoroNotificationList.setLayoutManager(mLayoutManager) ;
        binding.sovoroNotificationList.setAdapter(soVoRoNotificaionAdapter) ;

    }
    private final Emitter.Listener readNotification = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject messageInfos = (JSONObject) args[0];
                    Log.d("AAAA",messageInfos.toString());
                    messageInfos.keys().forEachRemaining((e)->{
                        try {
                            JSONObject jb=(JSONObject)(messageInfos.getJSONObject(e));
                            soVoRoNotificaionAdapter.addItem(jb.getString("commentedUser"));
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            });
        }
    };
}