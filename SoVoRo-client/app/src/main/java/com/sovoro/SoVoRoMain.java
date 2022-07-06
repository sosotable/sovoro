package com.sovoro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.sovoro.model.DailyWords;
import com.sovoro.model.Word;
import com.sovoro.model.WordOption;
import com.sovoro.wordview.SoVoRoWordAdapter;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class SoVoRoMain extends AppCompatActivity {

    /**툴바 관련 객체**/
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    /**뷰페이저**/
    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 10;
    private CircleIndicator3 mIndicator;

    /**상단메뉴 관련 메소드**/
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
            /**상단 메뉴 버튼 클릭**/
            /**case를 통해 옵션-현재 미구현-**/
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                drawer.openDrawer(GravityCompat.START);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovoro_main);

        /**
         * 메인 단어 부분
         * 현재 하드코딩으로 구현되어 있음
         * 서버에서 받아오는 형식으로 변경해야 한다
         * **/
        ArrayList<Word> alist=new ArrayList<>();
        alist.add(new Word("Apple","사과"));
        alist.add(new Word("Banana","바나나"));
        alist.add(new Word("Cat","고양이"));
        alist.add(new Word("Dog","개"));
        alist.add(new Word("End","끝"));
        alist.add(new Word("Frog","개구리"));
        alist.add(new Word("Grape","포도"));
        alist.add(new Word("Hippo","하마"));
        alist.add(new Word("In","안(의)"));
        alist.add(new Word("Juice","쥬스"));

        // 단어 지정
        DailyWords.setMainWords(alist);

        /**툴바 관련 코드**/
        toolbar = (Toolbar) findViewById(R.id.sovoro_main_toolbar);
        setSupportActionBar(toolbar);
        // 왼쪽 메뉴바 삽입
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        // 타이틀 제거
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer=findViewById(R.id.sovoro_main_drawer);

        /**네비게이션뷰 관련 코드**/
        navigationView=findViewById(R.id.sovoro_main_drawer_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // 네비게이션뷰 선택 이벤트 처리
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
        });

        /**뷰페이저 관련 코드**/
        //ViewPager2
        mPager = findViewById(R.id.sovoro_main_viewpager);
        //Adapter
        pagerAdapter = new SoVoRoWordAdapter(this, num_page);
        mPager.setAdapter(pagerAdapter);
        //Indicator
//        mIndicator = findViewById(R.id.sovoro_main_indicator);
//        mIndicator.setViewPager(mPager);
//        mIndicator.createIndicators(num_page,0);
        //ViewPager Setting
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.setCurrentItem(1000);
        mPager.setOffscreenPageLimit(10);

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //mIndicator.animatePageSelected(position%num_page);
            }
        });


        final float pageMargin= getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);

        mPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float myOffset = position * -(2 * pageOffset + pageMargin);
                if (mPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(mPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.setTranslationX(-myOffset);
                    } else {
                        page.setTranslationX(myOffset);
                    }
                } else {
                    page.setTranslationY(myOffset);
                }
            }
        });
    }
}