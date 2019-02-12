package com.jian86_android.hahaclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.jian86_android.hahaclass.databinding.Lunch;

import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends AppCompatActivity {
    Lunch binding;
    Timer timer = new Timer();

    //RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_launch);
        binding.setLaunch(this);
//        setContentView(R.layout.activity_launch);
//        relativeLayout =findViewById(R.id.relativeLayout);
//        Animation animation = AnimationUtils.loadAnimation(LaunchActivity.this,R.anim.appear_logo);
//        binding.layoutRelative.startAnimation(animation);
        Glide.with(this).load(R.drawable.loding).into(binding.ivLoding);
        timer.schedule(task,4000);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Intent intent = new Intent(LaunchActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }//run
    };
    @Override
    public void onPause() {
        super.onPause();
        //intent전환 효과 무효
        overridePendingTransition(0, 0);
    }
}
