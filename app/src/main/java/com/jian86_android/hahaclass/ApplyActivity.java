package com.jian86_android.hahaclass;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ApplyActivity extends AppCompatActivity {
    private ApplicationClass applicationClass;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private static final int PIC = 1000;
    private Toolbar toolbar;private NavigationView navMenu;
    private ArrayList<ItemInstructor> itemInstructors = new ArrayList<>();
    private ItemInstructor instructor;
    private UserInfo userInfo;
    private Intent getintent;
    String state,name,email,phone,pass,img,instructorTitle;
    int level;

    private Schedule schedule;
    private int position;
    private ArrayList<DatasItem>datasItems= new ArrayList<>();

    private View header;
    private ListView lv_apply;
    private AdapterDetailedSchedule mMyAdapter;
    private EditText et_name,et_phone,et_email,et_pwd;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        getintent = getIntent();
        applicationClass =(ApplicationClass)getApplicationContext();
        getData();
        toolbar =findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("강의 신청하기");

        setData();

    }

    public void getData(){
        position = getintent.getIntExtra("position",0);
        //받은 포지션으로 스케쥴리스트의 몇번째인지 확인

//강사정보 가져오기
        instructor= applicationClass.getItemInstructor();
        schedule = applicationClass.getItemInstructor().getSchedules().get(position);
        datasItems = schedule.getDatas(); //null일수 있음 유의


//유저정보 가져오기
        state = applicationClass.getState();
        if(state.equals(USER)){
            userInfo= applicationClass.getUserInfo();
            name =userInfo.getName();
            email = userInfo.getEmail();
            phone = userInfo.getPhone();
            pass = userInfo.getPassword();
            img = userInfo.getImagePath();
            level = userInfo.getLevel();
            Log.i("naemw",userInfo.getName());
        }else{
            level = 0;
            userInfo = null;
        }

    }//getIntentData
    public void setData(){
        //리스트뷰
        header = getLayoutInflater().inflate(R.layout.pchalendar_apply_list_header, null, false);
        lv_apply = findViewById(R.id.lv_apply);
        et_name = lv_apply.findViewById(R.id.name);
        et_email = lv_apply.findViewById(R.id.email);
        et_phone = lv_apply.findViewById(R.id.phone);
        et_pwd = lv_apply.findViewById(R.id.password);
        if(userInfo!=null) {
            et_name.setText(userInfo.getName());
            et_email.setText(userInfo.getEmail());
            et_phone.setText(userInfo.getPhone());
        }
        mMyAdapter = new AdapterDetailedSchedule(datasItems, this);
        lv_apply.addHeaderView(header) ;

        /* 리스트뷰에 어댑터 등록 */
        lv_apply.setAdapter(mMyAdapter);







    }

}
