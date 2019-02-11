package com.jian86_android.hahaclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends AppCompatActivity {
    Timer timer = new Timer();
    ProgressDialog dialog;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        relativeLayout =findViewById(R.id.relativeLayout);
        Animation animation = AnimationUtils.loadAnimation(LaunchActivity.this,R.anim.appear_logo);
        relativeLayout.startAnimation(animation);
        timer.schedule(task,4000);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Intent intent = new Intent(LaunchActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }//run
    };
}
