package com.jian86_android.hahaclass;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class PIntroActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private NavigationView navMenu;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pintro);
        toolbar =findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        navMenu =findViewById(R.id.nav_menu);
        drawerLayout =findViewById(R.id.drawer_layout);
        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                //Todo: 아이탬 연결
                }//switch
                drawerLayout.closeDrawer(navMenu,true);
                return false;
            }//onNavigationItemSelected
        });//listener
    }
}
