package com.jian86_android.hahaclass;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;

public class FragCalendar extends Fragment {
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private ApplicationClass applicationClass;
    private Context context;
    private UserInfo userInfo;

    private String picPath;
    private Bitmap btmapPicPath;
    String state, name, email, phone, pass, img;
    int level;
    ItemInstructor instructor;

    private Button btn_save;
    private ListView lv_schedule;
    String time, kcal, menu;

    View header;

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    Cursor cursor;
    MaterialCalendarView materialCalendarView;
    private ArrayList<Schedule> schedules = new ArrayList<>();
    private AdapterCalendar mMyAdapter;
    private Boolean isChanege = false;
    private Boolean isSave = false;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        context = container.getContext();
        applicationClass = (ApplicationClass) (context.getApplicationContext());
        getData();
        header = inflater.inflate(R.layout.pcalendar_list_header, null, false);
        return view;

    }//onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv_schedule = view.findViewById(R.id.lv_schedule);
     //   Log.i("schedules_length",schedules.size()+"");

        if(schedules != null && schedules.size()>0) setData();
        else{ new AlertDialog.Builder(context).setMessage("강의가 준비중입니다").show();}

    }//onViewCreated

    //강의 신청 다이알로그
    EditDialogShowSchedule customDialog;
    private void editDial(int[] position, String today){
        //Todo:커스텀 다이얼 로그
        customDialog = new EditDialogShowSchedule(context, this);
        customDialog.callFunction(today, position);
    }//editDial

    void setupCalender() {
        materialCalendarView = header.findViewById(R.id.calendarView);
        btn_save = header.findViewById(R.id.btn_save);
        //레벨별 접근

        if (level == 3) {
            btn_save.setVisibility(View.VISIBLE);

        } else {
            btn_save.setVisibility(View.INVISIBLE);
        }


        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2001, 0, 1))
                .setMaximumDate(CalendarDay.from(2130, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);


        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day= String.format("%04d-%02d-%02d",Year,Month,Day);
                Log.i("shot_Day test", shot_Day + "");

                ArrayList<Integer>positions = noticCal(shot_Day);




                if (positions!=null && positions.size()!=0) {
                    int[] position = new int[positions.size()];
                        for(int i = 0; i<positions.size(); i++){
                            position[i] = i;
                        }



                       // String src = schedules.get(position).getProjectTitle();
                        Log.i("position1", Arrays.toString(position));
                        Log.i("positionsSize",positions.size()+"");
                        editDial(position , shot_Day);







                    } else {
                        Toast.makeText(applicationClass, "해당 날짜에 수업이 없습니다.", Toast.LENGTH_SHORT).show();
                    }

                materialCalendarView.clearSelection();

                //Toast.makeText(applicationClass.getApplicationContext(), shot_Day, Toast.LENGTH_SHORT).show();
            }
        });




        for (Schedule s : schedules) {

            String start =s.getStart();
            String end =s.getEnd();
            int size = (int) (doDiffOfDate(start, end));
            ArrayList<String>days = getDateData(size,start,end);
            String []result = new String [days.size()];
            for(int i=0; i<days.size();i++){

                result[i]=days.get(i);
            }
            new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());
            resultStr.add(result);
        }


        //입력받은 데이터 분리

    }//setupCalender
    public ArrayList<String[]> resultStr = new ArrayList<>();


    public ArrayList<Integer> noticCal(String getdate){

        ArrayList<Integer>integers= new ArrayList<>();

//        String getdate = String.format("%04d-%02d-%02d",year,month,day);

        for(int f=0; f<resultStr.size(); f++){

            String [] arr = resultStr.get(f);

            for(int i=0; i<arr.length;i++){

               if (arr[i].contains(getdate)){
                   int position = f;
                   integers.add(f);
                   Log.i("f.size()",f+"");
               }
            }
                //Log.i("resultStr.size()",resultStr.size()+"");
        }


        return integers;

    }//noticCal


    //날짜 계산
    public ArrayList<String> getDateData(int size,String start, String end){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        ArrayList<String> strs = new ArrayList<String>();
        strs.add(start);
        String day = start;
            for(int i=0; i<=size; i++){
                Date date = null;
                try {
                    date = df.parse(day);
                    // 날짜 더하기
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.DATE, 1);

                    Date d = new Date(cal.getTimeInMillis());
                    day = df.format(d);
                    strs.add(day);
                    //if(day.equals(end)) break;
                } catch (ParseException e) { e.printStackTrace(); }
            }

            return strs;




    }//getDateData

    //날짜차이
    public long doDiffOfDate(String start, String end) {
        long diffDays=0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = formatter.parse(start);
            Date endDate = formatter.parse(end);

            // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
            long diff = endDate.getTime() - beginDate.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return diffDays;

    }



//    public void setListViewHeightBasedOnChildren(ListView listView) {

//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            // pre-condition
//            return;
//
//        }
//        int totalHeight = 0;
//
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
//        int dividerHeight = listView.getDividerHeight();
//
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            //listItem.measure(0, 0);
//            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//            totalHeight += listItem.getMeasuredHeight()+ dividerHeight;
//
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//
//        params.height = totalHeight;
//        listView.setLayoutParams(params);
//
//        listView.requestLayout();
//
//
//
//    }//setListViewHeightBasedOnChildren


    void setData(){

        //TODO:서버 작업 진행시 Main에서 정보 읽어서 ItemInstructor에 넣고 거기서 빼옴
        mMyAdapter = new AdapterCalendar(schedules, context);
        lv_schedule.addHeaderView(header) ;
        setupCalender();//켈린더세팅
        /* 리스트뷰에 어댑터 등록 */
        lv_schedule.setAdapter(mMyAdapter);//리스트뷰 세팅
       // setListViewHeightBasedOnChildren(lv_schedule);

    }//setData()

    void getData(){
        //유저정보 가져오기,
        instructor= applicationClass.getItemInstructor();
        schedules = applicationClass.getItemInstructor().getSchedules();
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

/////////innerClass//////////////////////////////////////////////////////////////////////

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 -를 기준으로짜르고 string을 int 로 변환
            for(int i = 0 ; i < Time_Result.length ; i ++){
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result[i].split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year,month-1,dayy);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (getActivity().isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays,getActivity()));
        }

    }

    //표시
    public class EventDecorator implements DayViewDecorator {

        private final Drawable drawable;
        private int color;
        private HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates,Activity context) {
            drawable = context.getResources().getDrawable(R.drawable.more);
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {

            view.setSelectionDrawable(drawable);
            view.addSpan(new DotSpan(5, color)); // 날자밑에 점

        }
    }

    //이벤트
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

    @Override
    public void onPause() {
        //화면이 사라질때
        super.onPause();
    }//onPause
}//FragCalendar
