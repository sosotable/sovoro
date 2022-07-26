package com.sovoro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.sovoro.databinding.ActivitySovoroCommentBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SoVoRoComment
        extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener
{

    private Socket socket;
    {
        try {
            socket = IO.socket("http://13.58.48.132:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private ActivitySovoroCommentBinding binding;

    private JSONObject commentInfo=new JSONObject();
    private ArrayList<SoVoRoCommentInfo> alist=new ArrayList<>();
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private Button sovoroCommentSubmitButton;
    private EditText sovoroCommentInput;
    private RecyclerView sovoroComments;

    private SoVoRoCommentAdapter sovoroCommentAdapter;

    private LinearLayoutManager mLayoutManager;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sovoro_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                drawer.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**네비게이션뷰 선택 이벤트 처리**/
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        drawer.closeDrawers();
        Intent intent;
        int id = menuItem.getItemId();
        String title = menuItem.getTitle().toString();
        switch (menuItem.getItemId()) {
            // 단어 학습 창-main-으로 이동
            case R.id.sovoro_word_view:
                intent = new Intent(getApplicationContext(), SoVoRoMain.class);
                startActivity(intent);
                break;
            // 단어 테스트 창으로 이동
            case R.id.sovoro_word_test:
                intent = new Intent(getApplicationContext(), SoVoRoTest.class);
                startActivity(intent);
                break;
            // 출석부 창으로 이동
            case R.id.sovoro_calendar:
                intent = new Intent(getApplicationContext(), SoVoRoAttendanceCalendar.class);
                startActivity(intent);
                break;
            // 단어 한마디 창으로 이동
            case R.id.sovoro_word_comment:
                intent = new Intent(getApplicationContext(), SoVoRoComment.class);
                startActivity(intent);
                break;
        }
        // 만약 err state라면 false return
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovoro_comment);

        binding=ActivitySovoroCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        socket.emit("read message");
        socket.on("read message",readMessage);
        socket.connect();

        binding.sovoroUserNickname.setText(UserInfo.nickname);

        // 레이아웃 매니저를 통해 역순 출력
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);



        sovoroCommentSubmitButton=findViewById(R.id.sovoro_comment_submit);
        sovoroCommentInput=findViewById(R.id.sovoro_comment_input);
        sovoroComments=findViewById(R.id.sovoro_comments);
        sovoroComments.setLayoutManager(mLayoutManager);

        toolbar = (Toolbar) findViewById(R.id.sovoro_comment_toolbar);
        setSupportActionBar(toolbar);
        // 왼쪽 메뉴바 삽입
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        // 타이틀 제거
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer=findViewById(R.id.sovoro_comment_drawer);

        /**네비게이션뷰 관련 코드**/
        navigationView=findViewById(R.id.sovoro_comment_drawer_view);
        navigationView.setNavigationItemSelectedListener(this);

        sovoroCommentSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String commentcontent=binding.sovoroCommentInput.getText().toString();
                if(!commentcontent.equals(""))
                {
                    try {
                        commentInfo.put("userid",UserInfo.userId);
                        commentInfo.put("nickname",UserInfo.nickname);
                        commentInfo.put("commentcontent",commentcontent);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socket.emit("add comment",commentInfo);
                    socket.emit("read message");

                    sovoroCommentInput.setText("");
                }
                else Toast.makeText(getApplicationContext(),"내용을 입력해주세요",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private final Emitter.Listener readMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                ArrayList<CommentInfo> commentInfoArrayList=new ArrayList<>();
                @Override
                public void run() {
                    try {
                        JSONObject messageInfos = new JSONObject(args[0].toString());
                        messageInfos.keys().forEachRemaining(key->{
                            try {
                                JSONObject messageInfo= new JSONObject(messageInfos.getString(key).toString());
                                commentInfoArrayList.add(new CommentInfo(
                                        messageInfo.getString("userid"),
                                        messageInfo.getString("nickname"),
                                        messageInfo.getString("commentcontent"),
                                        messageInfo.getInt("commentlikes"),
                                        Integer.parseInt(key)
                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                        commentInfoArrayList.forEach(element->{
                            SoVoRoCommentInfo sovoRoCommentInfo = new SoVoRoCommentInfo(
                                    "0",
                                    element.nickname,
                                    element.commentcontent,
                                    Integer.toString(element.commentlikes),
                                    Integer.toString(element.commentnumber)
                            );
                            alist.add(sovoRoCommentInfo);
                        });
                        sovoroCommentAdapter = new SoVoRoCommentAdapter(alist);

                        sovoroCommentAdapter.setOnItemClickListener(new SoVoRoCommentAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                                SoVoRoCommentInfo soVoRoCommentInfo=alist.get(position);
                                alist.set(position,new SoVoRoCommentInfo(
                                        "0",
                                        soVoRoCommentInfo.userNickname,
                                        soVoRoCommentInfo.userComment,
                                        Integer.toString(Integer.parseInt(soVoRoCommentInfo.userCommentLikesCount)+1),
                                        soVoRoCommentInfo.userCommentNumber
                                ));
                                socket.emit("add likes",soVoRoCommentInfo.userCommentNumber);
                            }
                        }) ;
                        sovoroComments.setAdapter(sovoroCommentAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
}