package com.jian86_android.hahaclass;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;


public class FragSettingLog extends Fragment {
    private ApplicationClass applicationClass;
    private Context context;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";

    private UserInfo userInfo;
    String state,name,email,phone,pass,img;
    int level;

    private ListView lv_apply_list,lv_my_class_list; //lv_my_class_list 레벨 2
    private View header_apply,header_my_class;// 내가 신청한 리스트 해더 , 내 강의 리스트 헤더
    private TextView tv_title_apply,tv_title_myclass;
    private HashMap<String,Schedule> applyClasses = new HashMap<>(); //서버에서 나의 apply 를 담음
    private  HashMap<String,RecivedApplicant> received_classes = new HashMap<>(); //서버에서 내 강의를 신청한 사람들의 정보를 담음

   // private AdapterSettingApplyClass mMyAdapter;
//필요한 것 number / title / date /
//강의를 구별할 수 있는 필드 저장, 날짜 저장
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_log,container,false);
        context = container.getContext();
        applicationClass = (ApplicationClass) (context.getApplicationContext());
        header_apply = inflater.inflate(R.layout.psetting_log_list_header, null, false);
        header_my_class = inflater.inflate(R.layout.psetting_log_list_header, null, false); //레벨 2용
        getData();
        return view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //리스트뷰

        lv_apply_list =view.findViewById(R.id.lv_apply_list);
        lv_my_class_list =view.findViewById(R.id.lv_my_class_list);
        tv_title_apply= header_apply.findViewById(R.id.tv_header);
        tv_title_myclass= header_my_class.findViewById(R.id.tv_header);
        lv_apply_list.addHeaderView(header_apply);
        lv_my_class_list.addHeaderView(header_my_class);
        tv_title_apply.setText(context.getResources().getString(R.string.title_setting_apply_class));
        tv_title_myclass.setText(context.getResources().getString(R.string.title_setting_my_class));

        setData(); // header 달기 , adapter 연결
    }
    ArrayList<Schedule> arrApplylist = new ArrayList<>();
    ArrayList<RecivedApplicant> arrRecivedlist = new ArrayList<>();
//각 리스트 해더뷰 세팅
    void setData(){
     //   mMyAdapter = new AdapterSettingApplyClass (applyClasses, context);
        //adapterclass MyApplyclass 추가 필요
        lv_apply_list.addHeaderView(header_apply);
        if(userInfo.getLevel()>1) lv_my_class_list.addHeaderView(header_my_class); //일반 레벨은 안보임
        /** 서버에서 읽어온 정보를 컬랙션에 Schedule로 담고 리스트뷰에 어댑터로 등록 */



        AdapterSettingLogApply mMyAdapter = new AdapterSettingLogApply(arrApplylist,context);
        /**서버에서 읽어온 정보를 컬랙션에 RecivedApplicant로  담고 리스트 뷰에 어댑터로 등록*/
        AdapterSettingLogRecivedApplicant recivedApplicant = new AdapterSettingLogRecivedApplicant(arrRecivedlist,context);
        //어뎁터 연결
        lv_apply_list.setAdapter(mMyAdapter);
        lv_my_class_list.setAdapter(recivedApplicant);




    }//setData()

    //비교
    Comparator<Schedule>sort = new Comparator<Schedule>() {
        @Override
        public int compare(Schedule o1, Schedule o2) {

            return o1.getStart().compareTo(o2.getStart()) ;
        }
    };
    Comparator<RecivedApplicant>sorts = new Comparator<RecivedApplicant>() {
        @Override
        public int compare(RecivedApplicant o1, RecivedApplicant o2) {

            return o1.getStart().compareTo(o2.getStart()) ;
        }
    };

//데이터 얻기
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

        /**서버에 담을때 스케쥴로 어레이에 담음 */
        /**DB: class_apply_list에서 filter : userEmail -> l_num과 class_code를 얻어 DB: class_list에서 정보 검색하여 schadule에 담음*/
        if(applicationClass.getMyApplyClasses() != null && applicationClass.getMyApplyClasses().size()>0 ) {
            applyClasses = applicationClass.getMyApplyClasses();
            arrApplylist.addAll(applyClasses.values());
            Collections.sort(arrApplylist, sort);
        }

        if(applicationClass.getMyReceivedClass() != null && applicationClass.getMyReceivedClass().size()>0 ) {
            received_classes = applicationClass.getMyReceivedClass();
            arrRecivedlist.addAll(received_classes.values());
            Collections.sort(arrRecivedlist, sorts);
        }
    }//getData
}//FragSettingAccount
