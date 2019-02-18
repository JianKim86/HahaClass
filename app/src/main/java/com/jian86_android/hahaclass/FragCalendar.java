package com.jian86_android.hahaclass;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragCalendar extends Fragment {
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private ApplicationClass applicationClass;
    private Context context;
    private UserInfo userInfo;

    private String picPath;
    private Bitmap btmapPicPath;
    String state,name,email,phone,pass,img;
    int level;
    ItemInstructor instructor;

    private Button btn_save;
    private ListView lv_schedule;
    private MaterialCalendarView materialCalendarView;
    private ArrayList<Schedule>schedules = new ArrayList<>();
    private Boolean isChanege=false;
    private Boolean isSave = false;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar,container,false);
        context = container.getContext();
        applicationClass = (ApplicationClass) (context.getApplicationContext());
        getData();
        return view;

    }//onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        materialCalendarView =view.findViewById(R.id.calendarView);
        btn_save =view.findViewById(R.id.btn_save);
        lv_schedule =view.findViewById(R.id.lv_schedule);
        setData();

        //레벨별 접근

        if(level==3){
            btn_save.setVisibility(View.VISIBLE);

        } else{
            btn_save.setVisibility(View.INVISIBLE);
        }
    }


    void setData(){

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2001, 0, 1))
                .setMaximumDate(CalendarDay.from(2130, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OneDayDecorator());

        //TODO:서버 작업 진행시 Main에서 정보 읽어서 ItemInstructor에 넣고 거기서 빼옴

        schedules = applicationClass.getItemInstructor().getSchedules();
        AdapterCalendar mMyAdapter = new AdapterCalendar(schedules, context);
        /* 리스트뷰에 어댑터 등록 */
        lv_schedule.setAdapter(mMyAdapter);

    }//setData()
    void getData(){
        //유저정보 가져오기,
        instructor= applicationClass.getItemInstructor();
        state = applicationClass.getState();
        if(state.equals(USER)){
            userInfo= applicationClass.getUserInfo();
            name =userInfo.getName();
            email = userInfo.getEmail();
            phone = userInfo.getPhone();
            pass = userInfo.getPassword();
            img = userInfo.getImagePath();
            level = userInfo.getLevel();
        }else{
            level = 0;
            userInfo = null;
        }



    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(getActivity()!=null){
            if(isVisibleToUser)
            {

            }
            else
            {
                // Toast.makeText(context, "사라질때", Toast.LENGTH_SHORT).show();
                //화면이 사라질때
                if(level==3){
                   // if(!isSave&&isChanege) saveInstructor();
                }

            }
        }
        super.setUserVisibleHint(isVisibleToUser);


    }//setUserVisibleHint



/////////innerClass//////////////////////////////////////////////////////////////////////
    public class OneDayDecorator implements DayViewDecorator {

        private CalendarDay date;

        public OneDayDecorator() {
            date = CalendarDay.today();
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return date != null && day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1.4f));
            view.addSpan(new ForegroundColorSpan(Color.GRAY));
        }

        public void setDate(Date date) {
            this.date = CalendarDay.from(date);
        }
    }//OneDayDecorator

    public class SaturdayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SaturdayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }//SaturdayDecorator

    public class SundayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SundayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }//SundayDecorator
/////////innerClass//////////////////////////////////////////////////////////////////////


    @Override
    public void onPause() {
        //화면이 사라질때
        super.onPause();
    }//onPause
}//FragCalendar
