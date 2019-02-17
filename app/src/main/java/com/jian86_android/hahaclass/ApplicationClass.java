package com.jian86_android.hahaclass;

import android.app.Application;
import android.content.res.Configuration;

import java.util.logging.Level;

public class ApplicationClass extends Application {

    public UserInfo userInfo;
    public String state;
    public int level;
    public ItemInstructor itemInstructor;

    public ItemInstructor getItemInstructor() {
        return itemInstructor;
    }

    public void setItemInstructor(ItemInstructor itemInstructor) {
        this.itemInstructor = itemInstructor;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    /** onCreate()
     * 액티비티, 리시버, 서비스가 생성되기전 어플리케이션이 시작 중일때
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * onConfigurationChanged()
     * 컴포넌트가 실행되는 동안 단말의 화면이 바뀌면 시스템이 실행
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
