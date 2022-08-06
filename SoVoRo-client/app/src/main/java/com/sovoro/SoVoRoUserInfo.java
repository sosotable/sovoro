package com.sovoro;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sovoro.databinding.ActivitySovoroMainLoadingBinding;
import com.sovoro.databinding.ActivitySovoroUserInfoBinding;
import com.sovoro.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SoVoRoUserInfo extends AppCompatActivity {
    private ActivitySovoroUserInfoBinding binding;
    private Uri imageURI;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://13.58.48.132:3000");
        } catch (URISyntaxException e) {}
    }
    private JSONObject userInfo=new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovoro_user_info);

        binding = ActivitySovoroUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(this)
                .load(UserInfo.userImage)
                .into(binding.userImage);
        binding.userId.setText(UserInfo.nickname);
        mSocket.connect();

        binding.changeUserImage.setOnClickListener(v->{
            Log.d("AAAA","AAAAAAAA");
            if (verifyPermissions()) {
                Log.d("AAAA","BBBBBBB");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                launcher.launch(intent);
            }
        });

        binding.myComments.setOnClickListener(view->{
            Intent intent=new Intent(getApplicationContext(),MyComments.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {

                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        Uri uri = intent.getData();
                        imageURI = intent.getData();
                        // imageview.setImageURI(uri);
                        Glide.with(SoVoRoUserInfo.this)
                                .load(imageURI)
                                .into(binding.userImage);
                        Glide.with(SoVoRoUserInfo.this)
                                .asBitmap()
                                .load(uri)
                                .override(120, 120)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                        resource=Bitmap.createScaledBitmap(resource, 120, 120, true);
                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        resource.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                                        try {
                                            // userInfo.put("userid", UserInfo.userid);
                                            userInfo.put("userid", "test1");
                                            userInfo.put("userImage", byteArray);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        mSocket.emit("upload image", userInfo);
                                    }
                                });
                    }
                }
            });
    public Boolean verifyPermissions() {
        Log.d("AAAA","CCCCC");
        // This will return the current Status
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
            Log.d("AAAA","DDDDD");
            String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            // If permission not granted then ask for permission real time.
            ActivityCompat.requestPermissions(this, STORAGE_PERMISSIONS, 1);
            return false;
        }

        return true;

    }
}