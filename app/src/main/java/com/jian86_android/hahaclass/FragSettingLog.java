package com.jian86_android.hahaclass;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;


public class FragSettingLog extends Fragment {
    private ApplicationClass applicationClass;
    private Context context;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";

    private UserInfo userInfo;
    String state,name,email,phone,pass,img;
    int level;

    private ListView lv_apply_list,lv_log_list;
    private View header_apply,header_log;
    private TextView tv_title_apply,tv_title_log;
    private ArrayList<MyApplyClass> applyClasses = new ArrayList<>();
   // private AdapterSettingApplyClass mMyAdapter;
//필요한 것 number / title / date /
//강의를 구별할 수 있는 필드 저장, 날짜 저장
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_log,container,false);
        context = container.getContext();
        applicationClass = (ApplicationClass) (context.getApplicationContext());
        getData();
        header_apply = inflater.inflate(R.layout.psetting_log_list_header, null, false);
        header_log = inflater.inflate(R.layout.psetting_log_list_header, null, false);
        return view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv_apply_list =view.findViewById(R.id.lv_apply_list);
        lv_log_list =view.findViewById(R.id.lv_log_list);

        setData();
    }

    void setData(){
     //   mMyAdapter = new AdapterSettingApplyClass (applyClasses, context);
        //adapterclass MyApplyclass 추가 필요
        tv_title_apply= header_apply.findViewById(R.id.tv_header);
        tv_title_log= header_log.findViewById(R.id.tv_header);

        lv_apply_list.addHeaderView(header_apply);
        lv_log_list.addHeaderView(header_log);
        tv_title_apply.setText(context.getString(R.string.title_setting_myclass));
        tv_title_log.setText(context.getString(R.string.title_setting_mylog));

        /* 리스트뷰에 어댑터 등록 */
       // lv_apply_list.setAdapter(mMyAdapter);//리스트뷰 세팅
        // setListViewHeightBasedOnChildren(lv_schedule);

    }//setData()

    void getData() {

        state = applicationClass.getState();
        if (state.equals(USER)) {
            userInfo = applicationClass.getUserInfo();
            name = userInfo.getName();
            email = userInfo.getEmail();
            phone = userInfo.getPhone();
            pass = userInfo.getPassword();
            img = userInfo.getImagePath();
            level = userInfo.getLevel();
        } else {
            level = 0;
            userInfo = null;
        }
    }//getData
}//FragSettingAccount
