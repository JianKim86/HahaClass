package com.jian86_android.hahaclass;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragSettingQnA extends Fragment {
    private ApplicationClass applicationClass;
    private Context context;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";

    private UserInfo userInfo;

    String state,name,email,phone,pass,img;
    int level;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_qna,container,false);
        context = container.getContext();
        applicationClass = (ApplicationClass) (context.getApplicationContext());
        getData();
        return view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

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
