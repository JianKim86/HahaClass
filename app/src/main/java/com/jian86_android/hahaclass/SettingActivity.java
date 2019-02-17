package com.jian86_android.hahaclass;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    private ApplicationClass applicationClass;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private static final int PIC = 1000;
    private Toolbar toolbar;
    private NavigationView navMenu;
    private DrawerLayout drawerLayout;
    private UserInfo userInfo;
    private String state,name,email,phone,pass,img;
    private int level;
    private ActionBarDrawerToggle drawerToggle;
    private View nav_header_view;
    private  TextView nav_header_id_text;
    private  ImageView profile_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar =findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        navMenu =findViewById(R.id.nav_menu);
        drawerLayout =findViewById(R.id.drawer_layout);
        nav_header_view = navMenu.inflateHeaderView(R.layout.nav_header);
        nav_header_id_text = (TextView) nav_header_view.findViewById(R.id.tv_name);
        profile_image =(ImageView) nav_header_view.findViewById(R.id.profile_image);
        setUpNav();
        setFragmentPlace();
        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    //Todo: 아이탬 연결
                    case R.id.item1 :goSetting(0); break;
                    case R.id.item2 :goSetting(1); break;
                    case R.id.item3 :goSetting(2); break;
                    case R.id.item4 :goSetting(3); break;
                }//switch
                drawerLayout.closeDrawer(navMenu,true);
                return false;
            }//onNavigationItemSelected
        });//listener


        //자바로 액션메뉴에 햄버거 버튼 만들기
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.draw_open, R.string.draw_close);
        //토글버튼 아이콘이 보이도록 붙이기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Setting");
        //삼선 아이콘 모양으로 동기마추기
        drawerToggle.syncState();
        //삼선 아이콘과 화살표아이콘이 자동 변환되도록
        drawerLayout.addDrawerListener(drawerToggle);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    }//onCreate
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }//onCreateOptionsMenu
    private int stage;
    private final int MAINACTIVITY=0;
    private final int PINTROACTIVITY=1;
    //토글 버튼과 drawer 연결
    //엑티비티 입장에서 토글버튼도 액션바 메뉴 인가 느낌
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.goback:
                Intent intents;
                switch (stage){
                    case MAINACTIVITY :
                        startActivity(new Intent(SettingActivity.this,MainActivity.class));
                        finish();
                        break;
                    case PINTROACTIVITY :
                        startActivity(new Intent(SettingActivity.this,PIntroActivity.class));
                        finish();
                        break;
                }
                break;
        }
        //아이템 클릭상황을 토글 버튼에 전달
        drawerToggle.onOptionsItemSelected(item);

        return super.onOptionsItemSelected(item);


    }
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    Fragment fragment;
    //데이터 세팅
    private void  getIntentData() {

        applicationClass =(ApplicationClass)getApplicationContext();
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
            userInfo = null;
        }
    }
    public void setUpNav(){
        getIntentData();
       Toast.makeText(applicationClass, "2::"+applicationClass.getUserInfo().getImagePath(), Toast.LENGTH_SHORT).show();
        if(state.equals(CUSTOMER))nav_header_id_text.setText(CUSTOMER);
        else nav_header_id_text.setText(name);
        if(img != null && !(img.equals(""))) {
            Uri uRi = Uri.parse(img);
            Toast.makeText(applicationClass, ""+applicationClass.getUserInfo().getImagePath(), Toast.LENGTH_SHORT).show();
            Picasso.get().load(uRi).into(profile_image);
        } else{Glide.with(this).load(R.drawable.ic_launcher_background).into(profile_image);}
    }
    public void setFragmentPlace(){
        Intent intent = getIntent();
        int item = intent.getIntExtra("Item",0);
        stage = intent.getIntExtra("stage",0);
        goSetting(item);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (item){
            case 0: fragment = new FragSettingAccount();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
            case 1: fragment = new FragSettingPwd();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
            case 2: fragment = new FragSettingLog();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
            case 3: fragment = new FragSettingQnA();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
        }
    }
//세팅페이지이동
    private void goSetting(int item){
      //세팅페이지 이동

       fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (item){
            case 0: fragment = new FragSettingAccount();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
            case 1: fragment = new FragSettingPwd();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
            case 2: fragment = new FragSettingLog();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
            case 3: fragment = new FragSettingQnA();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
        }




        //TODO: 프레그먼트 이동
    }


    void takePic(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PIC);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PIC:

                //갤러리 화면에서 이미지를 선택하고 돌아왔는지 체크
                //두 번째 파라미터 : resultCode
                if (resultCode == RESULT_OK){
                    Uri uri=data.getData();
                    String picPathh = uri.toString();

                        //Uri 경로로 전달되었다면
                        //iv.setImageURI(uri);
                        //라이브러리 쓰자!!!!!!!  Glide(bumptech)

                            ((FragSettingAccount)fragment).setPic(picPathh);

                    }else{
                        //아니면 Intent 에 Extra 데이터로 Bitmap 이 전달되어 옴
                        Bundle bundle=data.getExtras();
                        Bitmap bm= (Bitmap) bundle.get("data"); // key 값 "data" 는 정해진거야
                        //iv.setImageBitmap(bm);

                            ((FragSettingAccount)fragment).setPic(bm);



                }//if

                break;
        }


    }//onActivityResult




}//SettingActivity
