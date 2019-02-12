package com.jian86_android.hahaclass;


import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity  {
    private Animation fab_open, fab_close, fab_action_close, fab_action_open;
    private Boolean isFabOpen = false;
    private Toolbar toolbar;
    private NavigationView navMenu;
    private DrawerLayout drawerLayout;
    private FloatingActionButton fab, fab1, fab2,fab3, fab4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar =findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        navMenu =findViewById(R.id.nav_menu);
        drawerLayout =findViewById(R.id.drawer_layout);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_action_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_action_close);
        fab_action_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_action_open);
        fab =findViewById(R.id.fab_btn);
        fab1 =findViewById(R.id.fab1_btn);
        fab2 =findViewById(R.id.fab2_btn);
        fab3 =findViewById(R.id.fab3_btn);
        fab4 =findViewById(R.id.fab4_btn);
        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                }//switch
                drawerLayout.closeDrawer(navMenu,true);
                return false;
            }//onNavigationItemSelected
        });//listener

        fab.setOnClickListener(onClickListener);
        fab1.setOnClickListener(onClickListener);
        fab2.setOnClickListener(onClickListener);
        fab3.setOnClickListener(onClickListener);
        fab4.setOnClickListener(onClickListener);

    }//onCreate
    @Override
    public void onPause() {
        super.onPause();
        //intent전환 효과 무효
        overridePendingTransition(0, 0);
    }
 View.OnClickListener onClickListener = new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         int id = v.getId();
         switch (id) {
             case R.id.fab_btn:
                 anim();
                 Toast.makeText(MainActivity.this, "Floating Action Button", Toast.LENGTH_SHORT).show();
                 break;
             case R.id.fab1_btn:
                 anim();
                 Toast.makeText(MainActivity.this, "Button1", Toast.LENGTH_SHORT).show();
                 break;
             case R.id.fab2_btn:
                 anim();
                 Toast.makeText(MainActivity.this, "Button2", Toast.LENGTH_SHORT).show();
                 break;
             case R.id.fab3_btn:
                 anim();
                 Toast.makeText(MainActivity.this, "Button3", Toast.LENGTH_SHORT).show();
                 break;
             case R.id.fab4_btn:
                 anim();
                 Toast.makeText(MainActivity.this, "Button4", Toast.LENGTH_SHORT).show();
                 break;
         }

     }
 };
    public void anim() {

        if (isFabOpen) {
            fab.startAnimation(fab_action_open);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab4.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(fab_action_close);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab4.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);
            isFabOpen = true;
        }
    }
}//MainActivity
