package com.jian86_android.hahaclass;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class FragSettingPwd extends Fragment implements View.OnClickListener {
    private ApplicationClass applicationClass;
    private Context context;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private UserInfo userInfo;

    String state,name,email,phone,pass,img;
    int level;

    private EditText nowPwd,newPwd,reNewPwd;
    Button submit_account;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_pwd,container,false);
        context = container.getContext();
        applicationClass = (ApplicationClass) (context.getApplicationContext());
        return view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
        //binding
        nowPwd= view.findViewById(R.id.pwd_now);
        newPwd= view.findViewById(R.id.pwd_new);
        reNewPwd= view.findViewById(R.id.pwd_r_new);
        submit_account =view.findViewById(R.id.submit_account);

        submit_account.setOnClickListener(this);



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
    private boolean isPasswordValid(String password,String repassword) {
        return (password.length() > 4) && (password.equals(repassword));
    }
    private boolean comparePwd(String pwd){
        return (pwd.length() > 4) && (pwd.equals(pass));
    }
    void submit_frg() {
        nowPwd.setError(null);
        boolean cancel = false;
        View focusView = null;
        // Store values at the time of the login attempt.
        String pwd = nowPwd.getText().toString();
        String nPwd = newPwd.getText().toString();
        String renPwd = reNewPwd.getText().toString();

        if (TextUtils.isEmpty(pwd)) {
            nowPwd.setError(getString(R.string.error_invalid_password));
            focusView = nowPwd;
            cancel = true;
        } else if (TextUtils.isEmpty(nPwd)) {
            nowPwd.setError(getString(R.string.error_invalid_password));
            focusView = newPwd;
            cancel = true;
        } else if (TextUtils.isEmpty(renPwd)) {
            nowPwd.setError(getString(R.string.error_invalid_password));
            focusView = reNewPwd;
            cancel = true;
            // if()
        } else if (!comparePwd(pwd)) {
            nowPwd.setError(getString(R.string.error_invalid_password));
            focusView = nowPwd;
            cancel = true;
        } else if (!isPasswordValid(nPwd, renPwd)) {
            reNewPwd.setError(getString(R.string.error_incorrect_rpassword));
            focusView = newPwd;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            String chanegeText = newPwd.getText().toString();
            applicationClass.getUserInfo().setPassword(chanegeText);
            //((SettingActivity) context).setUpNav();
            //TODO: 확인 후 돌아가기
        }//submit_frg

    }
    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id){
            case R.id.submit_account: submit_frg(); break;
        }
    }
}//FragSettingAccount
