package com.jian86_android.hahaclass;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity  {
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private Toolbar toolbar;
    private NavigationView navMenu;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private ArrayList<ItemInstructor> itemInstructors = new ArrayList<>();
    private UserInfo userInfo;
    private Intent getintent;
    String state,name,email,phone,pass,img,instructor;
    int level;
    private ActionBarDrawerToggle drawerToggle;

    private Animation fab_open, fab_close, fab_action_close, fab_action_open;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2,fab3, fab4;

    private ViewPager pagerLayout;
    private AdapterFragment adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getintent = getIntent();
        getIntentData();
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
        pagerLayout =findViewById(R.id.pager_layout);

        adapter = new AdapterFragment(getSupportFragmentManager());
        pagerLayout.setAdapter(adapter);


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

        View nav_header_view = navMenu.inflateHeaderView(R.layout.nav_header);
        //View nav_header_view = navigationView.getHeaderView(0);
        String name = getintent.getBundleExtra("userBundle").getString("Name");

        TextView nav_header_id_text = (TextView) nav_header_view.findViewById(R.id.tv_name);
        ImageView profile_image =(ImageView) nav_header_view.findViewById(R.id.profile_image);
        if(state.equals(CUSTOMER))nav_header_id_text.setText(CUSTOMER);
        else nav_header_id_text.setText(name);
        if(img != null) {
            Uri uRi = Uri.parse(img);
            Picasso.get().load(uRi).into(profile_image);
        }
        else{Glide.with(this).load(R.drawable.ic_launcher_background).into(profile_image);}
        //자바로 액션메뉴에 햄버거 버튼 만들기
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.draw_open, R.string.draw_close);
        //토글버튼 아이콘이 보이도록 붙이기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //삼선 아이콘 모양으로 동기마추기
        drawerToggle.syncState();
        //삼선 아이콘과 화살표아이콘이 자동 변환되도록
        drawerLayout.addDrawerListener(drawerToggle);
        //타이틀 변경
        getSupportActionBar().setTitle(instructor);

    }//onCreate
//fab버튼 클릭시 화면전환 이벤트
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
                 pagerLayout.setCurrentItem(0);
                 Toast.makeText(MainActivity.this, "Button1", Toast.LENGTH_SHORT).show();
                 break;
             case R.id.fab2_btn:
                 anim();
                 pagerLayout.setCurrentItem(1);
                 Toast.makeText(MainActivity.this, "Button2", Toast.LENGTH_SHORT).show();
                 break;
             case R.id.fab3_btn:
                 anim();
                 pagerLayout.setCurrentItem(2);
                 Toast.makeText(MainActivity.this, "Button3", Toast.LENGTH_SHORT).show();
                 break;
             case R.id.fab4_btn:
                 anim();
                 pagerLayout.setCurrentItem(3);
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
    }//anim
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //아이템 클릭상황을 토글 버튼에 전달
        drawerToggle.onOptionsItemSelected(item);

        return super.onOptionsItemSelected(item);


    }
    private void  getIntentData(){
        Bundle userBundle = getintent.getBundleExtra("userBundle");
        Bundle selectTeacher =  getintent.getBundleExtra("selectTeacher");
        instructor = selectTeacher.getString("Instructor");
        state = userBundle.getString("state");
        if(state.equals(USER)) {
            name = userBundle.getString("Name");
            email = userBundle.getString("Email");
            phone = userBundle.getString("Phone");
            pass = userBundle.getString("Pass");
            img = userBundle.getString("Image");
            level = userBundle.getInt("Level", 0);
            userInfo = new UserInfo(name, email, phone, pass, img, level);
        }else userInfo = null;

    }//getIntentData;
    @Override
    public void onPause() {
        super.onPause();
        //intent전환 효과 무효
        overridePendingTransition(0, 0);
    }
}//MainActivity
