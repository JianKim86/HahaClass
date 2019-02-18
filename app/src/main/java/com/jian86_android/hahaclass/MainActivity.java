package com.jian86_android.hahaclass;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity  {
    private ApplicationClass applicationClass;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private static final int PIC = 1000;
    private Toolbar toolbar;
    private NavigationView navMenu;
    private DrawerLayout drawerLayout;
    private ArrayList<ItemInstructor> itemInstructors = new ArrayList<>();
    private ItemInstructor instructor;
    private UserInfo userInfo;
    private Intent getintent;
    String state,name,email,phone,pass,img,instructorTitle;
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
        applicationClass =(ApplicationClass)getApplicationContext();
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
                    case R.id.item1 :goSetting(0); break;
                    case R.id.item2 :goSetting(1);break;
                    case R.id.item3 :goSetting(2);break;
                    case R.id.item4 :goSetting(3);break;
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
      //  String name = getintent.getBundleExtra("userBundle").getString("Name");

        TextView nav_header_id_text = (TextView) nav_header_view.findViewById(R.id.tv_name);
        ImageView profile_image =(ImageView) nav_header_view.findViewById(R.id.profile_image);

        if(state.equals(CUSTOMER))nav_header_id_text.setText(CUSTOMER);
        else nav_header_id_text.setText(name);
        //Toast.makeText(applicationClass, "nav :"+img, Toast.LENGTH_SHORT).show();
//        if(state.equals(USER)){ img = applicationClass.getUserInfo().getImagePath();
//            Toast.makeText(applicationClass, "main img :"+ img, Toast.LENGTH_SHORT).show();
//            Log.d("picPath ", "main img:"+img);
//
//        }
        if(img != null && !(img.equals(""))) {
            Uri uRi = Uri.parse(img);
            Picasso.get().load(uRi)
                    .resize(400,400).into(profile_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Log.d("picPath ", "pIntor img: load failed "+ img);
                }
            });
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
        getSupportActionBar().setTitle(instructorTitle);

    }//onCreate
//fab버튼 클릭시 화면전환 이벤트
 View.OnClickListener onClickListener = new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         int id = v.getId();
         switch (id) {
             case R.id.fab_btn:
                 anim();

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
      //  Bundle userBundle = getintent.getBundleExtra("userBundle");
       // Bundle selectTeacher =  getintent.getBundleExtra("selectTeacher");
        instructor = applicationClass.getItemInstructor();
        instructorTitle = instructor.getSubTitle();
        state = applicationClass.getState();
        if(state.equals(USER)){
            userInfo= applicationClass.getUserInfo();
            name =userInfo.getName();
            email = userInfo.getEmail();
            phone = userInfo.getPhone();
            pass = userInfo.getPassword();
            img = userInfo.getImagePath();
            level = userInfo.getLevel();
        }else{
            userInfo = null;
        }

    }//getIntentData;
    private void goSetting(int item){
        if(state.equals(USER)){
            Intent intent= new Intent(MainActivity.this,SettingActivity.class);
            intent.putExtra("Item",item);
            intent.putExtra("stage",0);
            startActivity(intent);
        }else {
            //TODO::dial 회원만 가능
            String msg =getString(R.string.cont_use_custom);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setTitle("AlertDialog Title");
            builder.setMessage(msg);
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            intent.putExtra("want",3);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.setNegativeButton("아니오", null);
            builder.show();
        }
    }

    void checkState(){

    }
    void takePic(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PIC);
    }
    private String picPath="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PIC:

                //갤러리 화면에서 이미지를 선택하고 돌아왔는지 체크
                //두 번째 파라미터 : resultCode
                if (resultCode == RESULT_OK){

                    Uri uri=data.getData();
                    picPath = uri.toString();
                    Fragment fragment = (Fragment) adapter.instantiateItem(pagerLayout,0);
                    if(uri != null){
                        //Uri 경로로 전달되었다면
                        //iv.setImageURI(uri);
                        //라이브러리 쓰자!!!!!!!  Glide(bumptech)
                        if(pagerLayout.getCurrentItem()==0) {
                            ((FragInfo)fragment).setPic(picPath);
                        }
                    }else{
                        //아니면 Intent 에 Extra 데이터로 Bitmap 이 전달되어 옴
                        Bundle bundle=data.getExtras();
                        Bitmap bm= (Bitmap) bundle.get("data"); // key 값 "data" 는 정해진거야
                        //iv.setImageBitmap(bm);
                        if(pagerLayout.getCurrentItem()==0) {
                            ((FragInfo)fragment).setPic(bm);
                        }


                    }

                }//if

                break;
        }

    }//onActivityResult


//    public Bundle sendData(){
//        //디비에서 읽어올 회원정보 이름과이메일만 보냄 하지만 지금은 전체 다 보내기
//        Bundle bundleData = new Bundle();
//        if(userInfo != null) {
//            bundleData = new Bundle();
//            int userLevel = userInfo.getLevel();
//            String userPhone = userInfo.getPhone();
//            String userName = userInfo.getName();
//            String userImgPath = userInfo.getImagePath();
//            String userEmail =userInfo.getEmail();
//            String userPassword =userInfo.getPassword();
//            bundleData.putInt("Level",userLevel);
//            bundleData.putString("Name",userName);
//            bundleData.putString("Image",userImgPath);
//            bundleData.putString("Phone",userPhone);
//            bundleData.putString("Email",userEmail);
//            bundleData.putString("Pass",userPassword);
//            bundleData.putString("state",state);
//        }else{
//            bundleData = new Bundle();
//            bundleData.putString("state",state);
//        }
//        return bundleData;
//    }


    @Override
    public void onPause() {
        super.onPause();
        //intent전환 효과 무효
        overridePendingTransition(0, 0);
    }
}//MainActivity
