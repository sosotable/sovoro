package com.sovoro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class SoVoRoComment
        extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener
{
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private Button sovoroCommentSubmitButton;
    private EditText sovoroCommentInput;
    private RecyclerView sovoroComments;

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

        // 레이아웃 매니저를 통해 역순 출력ㅇㅁ
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        ArrayList<SoVoRoCommentInfo> alist=new ArrayList<>();

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
                String txt=sovoroCommentInput.getText().toString();
                if(!txt.equals(""))
                {
                    SoVoRoCommentInfo sovoRoCommentInfo = new SoVoRoCommentInfo("0", txt, "0");
                    alist.add(sovoRoCommentInfo);
                    SoVoRoCommentAdapter sovoroCommentAdapter = new SoVoRoCommentAdapter(alist);
                    sovoroComments.setAdapter(sovoroCommentAdapter);
                    sovoroCommentInput.setText("");
                }
                else Toast.makeText(getApplicationContext(),"내용을 입력해주세요",Toast.LENGTH_SHORT).show();
            }
        });

    }
}