package com.jian86_android.hahaclass;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.jian86_android.hahaclass.databinding.MainIntro;

public class MainActivity extends AppCompatActivity {
    MainIntro binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setMain(this);

        setSupportActionBar(binding.toolbarT);
        binding.navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                }//switch
                binding.drawerLayout.closeDrawer(binding.navMenu,true);
                return false;
            }//onNavigationItemSelected
        });//listener
    }//onCreate
    @Override
    public void onPause() {
        super.onPause();
        //intent전환 효과 무효
        overridePendingTransition(0, 0);
    }
}//MainActivity
