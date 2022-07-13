package com.sovoro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.sovoro.databinding.ActivitySovoroAttendanceCalendarBinding;
import com.sovoro.databinding.ActivitySovoroSigninBinding;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.List;

public class SoVoRoAttendanceCalendar extends AppCompatActivity {
    // 툴바 관련 객체
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private FragmentManager fragmentManager;
    private SoVoRoCalendarContent sovoroCalendarContent;
    private FragmentTransaction transaction;

    private ActivitySovoroAttendanceCalendarBinding binding;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovoro_attendance_calendar);

        binding = ActivitySovoroAttendanceCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();

        sovoroCalendarContent = new SoVoRoCalendarContent();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.sovoro_calendar_content, sovoroCalendarContent).commitAllowingStateLoss();

        // 첫 시작 요일이 월요일이 되도록 설정
        binding.sovoroCalendar.state()
                .edit()
                .setFirstDayOfWeek(DayOfWeek.of(Calendar.MONDAY))
                .commit();

        // 월, 요일을 한글로 보이게 설정 (MonthArrayTitleFormatter의 작동을 확인하려면 밑의 setTitleFormatter()를 지운다)
        binding.sovoroCalendar.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        binding.sovoroCalendar.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));

        // 좌우 화살표 사이 연, 월의 폰트 스타일 설정
        binding.sovoroCalendar.setHeaderTextAppearance(R.style.CalendarWidgetHeader);

        // 요일 선택 시 내가 정의한 드로어블이 적용되도록 함
        binding.sovoroCalendar.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                // 아래 로그를 통해 시작일, 종료일이 어떻게 찍히는지 확인하고 본인이 필요한 방식에 따라 바꿔 사용한다
                // UTC 시간을 구하려는 경우 이 라이브러리에서 제공하지 않으니 별도의 로직을 짜서 만들어내 써야 한다
                String startDay = dates.get(0).getDate().toString();
                String endDay = dates.get(dates.size() - 1).getDate().toString();
                Log.e("CalendarTag", "시작일 : " + startDay + ", 종료일 : " + endDay);
            }
        });

        // 일자 선택 시 내가 정의한 드로어블이 적용되도록 한다
        binding.sovoroCalendar.addDecorators(new DayDecorator(getApplicationContext()));

        // 좌우 화살표 가운데의 연/월이 보이는 방식 커스텀
        binding.sovoroCalendar.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                // CalendarDay라는 클래스는 LocalDate 클래스를 기반으로 만들어진 클래스다
                // 때문에 MaterialCalendarView에서 연/월 보여주기를 커스텀하려면 CalendarDay 객체의 getDate()로 연/월을 구한 다음 LocalDate 객체에 넣어서
                // LocalDate로 변환하는 처리가 필요하다
                LocalDate inputText = day.getDate();
                String[] calendarHeaderElements = inputText.toString().split("-");
                StringBuilder calendarHeaderBuilder = new StringBuilder();
                calendarHeaderBuilder.append(calendarHeaderElements[0])
                        .append(" ")
                        .append(calendarHeaderElements[1]);
                return calendarHeaderBuilder.toString();
            }
        });


        binding.sovoroCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget,
                                       @NonNull CalendarDay date,
                                       boolean selected) {
                Toast.makeText(getApplicationContext(),date.toString(), Toast.LENGTH_LONG);
                System.out.println(date.getDate());
                String[] dates=date.getDate().toString().split("-");
                String fmt="%s년 %s월 %s일";
                sovoroCalendarContent.setDate(String.format(fmt,dates[0],dates[1],dates[2]));
            }
        });

        /**툴바 관련 코드**/
        toolbar = (Toolbar) findViewById(R.id.sovoro_calendar_toolbar);
        setSupportActionBar(toolbar);
        // 왼쪽 메뉴바 삽입
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        // 타이틀 제거
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer=findViewById(R.id.sovoro_calendar_drawer);

        /**네비게이션뷰 관련 코드**/
        navigationView=findViewById(R.id.sovoro_calendar_drawer_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawer.closeDrawers();
                Intent intent;
                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();
                switch (menuItem.getItemId()) {
                    case R.id.sovoro_word_view:
                        intent = new Intent(getApplicationContext(), SoVoRoMain.class);
                        startActivity(intent);
                        break;
                    case R.id.sovoro_word_test:
                        intent = new Intent(getApplicationContext(), SoVoRoTest.class);
                        startActivity(intent);
                        break;
                    case R.id.sovoro_calendar:
                        intent = new Intent(getApplicationContext(), SoVoRoAttendanceCalendar.class);
                        startActivity(intent);
                        break;
                    case R.id.sovoro_word_comment:
                        intent = new Intent(getApplicationContext(), SoVoRoComment.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

    /* 선택된 요일의 background를 설정하는 Decorator 클래스 */
    private static class DayDecorator implements DayViewDecorator {

        private final Drawable drawable;

        public DayDecorator(Context context) {
            drawable = ContextCompat.getDrawable(context, R.drawable.calendar_selector);
        }

        // true를 리턴 시 모든 요일에 내가 설정한 드로어블이 적용된다
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return true;
        }

        // 일자 선택 시 내가 정의한 드로어블이 적용되도록 한다
        @Override
        public void decorate(DayViewFacade view) {
            view.setSelectionDrawable(drawable);
//            view.addSpan(new StyleSpan(Typeface.BOLD));   // 달력 안의 모든 숫자들이 볼드 처리됨
        }
    }
}