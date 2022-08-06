package com.sovoro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.sovoro.databinding.ActivitySovoroMainBinding;
import com.sovoro.databinding.ActivitySovoroSigninBinding;
import com.sovoro.model.DailyWords;
import com.sovoro.model.UserInfo;
import com.sovoro.model.Word;
import com.sovoro.model.WordOption;
import com.sovoro.wordview.SoVoRoWordAdapter;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class SoVoRoMain
        extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        ViewPager2.PageTransformer
{

    private float pageMargin;
    private float pageOffset;

    private ActivitySovoroMainBinding binding;

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
        Intent intent;
        switch (item.getItemId()){
            /**상단 메뉴 버튼 클릭**/
            /**case를 통해 옵션-현재 미구현-**/
            case android.R.id.home: // 왼쪽 상단 버튼 눌렀을 때
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.menuNotifications:
                intent = new Intent(getApplicationContext(),SoVoRoNotification.class);
                startActivity(intent);
                break;
            case R.id.menuSearch:
                intent = new Intent(getApplicationContext(),SoVoRoSearch.class);
                startActivity(intent);
                break;
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
            case R.id.sovoro_userinfo:
                intent = new Intent(getApplicationContext(), SoVoRoUserInfo.class);
                startActivity(intent);
                break;
        }
        // 만약 err state라면 false return
        return false;
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovoro_main);

        binding=ActivitySovoroMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /**툴바 관련 코드**/
        toolbar = (Toolbar) findViewById(R.id.sovoroMainToolbar);
        setSupportActionBar(toolbar);
        // 왼쪽 메뉴바 삽입
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        // 타이틀 제거
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer=findViewById(R.id.sovoroMainDrawer);

        /**네비게이션뷰 관련 코드**/
        navigationView=findViewById(R.id.sovoroMainDrawerView);
        navigationView.setNavigationItemSelectedListener(this);

        //출처: https://ljs93kr.tistory.com/16 [건프의 소소한 개발이야기:티스토리]
        //View nav_header_view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        View nav_header_view = navigationView.getHeaderView(0);

        AppCompatImageView userImage=nav_header_view.findViewById(R.id.sovoro_userpic);
        AppCompatTextView userId=nav_header_view.findViewById(R.id.sovoro_username);
        Glide.with(this)
                .load(UserInfo.userImage)
                .into(userImage);
        userId.setText(UserInfo.nickname);

        /**뷰페이저 관련 코드**/

        pageMargin = getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);

        //ViewPager2
        mPager = findViewById(R.id.sovoroMainViewpager);
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

        mPager.registerOnPageChangeCallback(new SoVoRoViewPagerOnPageChangeCallback(mPager));

        mPager.setPageTransformer(this);
    }


}