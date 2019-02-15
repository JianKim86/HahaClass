package com.jian86_android.hahaclass;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PIntroActivity extends AppCompatActivity {
    private ApplicationClass applicationClass;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private Toolbar toolbar;
    private NavigationView navMenu;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private ArrayList<ItemInstructor> itemInstructors = new ArrayList<>();
    private UserInfo userInfo;
    private Intent getintent;
    String state,name,email,phone,pass,img;
    int level;
    private ActionBarDrawerToggle drawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pintro);
        applicationClass =(ApplicationClass)getApplicationContext();
        getintent = getIntent();
        getIntentData();

        toolbar =findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        navMenu = findViewById(R.id.nav_menu);
        drawerLayout =findViewById(R.id.drawer_layout);
        listView = findViewById(R.id.lv_instructor);

       // navMenu.setNavigationItemSelectedListener(this);

        View nav_header_view = navMenu.inflateHeaderView(R.layout.nav_header);
        //View nav_header_view = navigationView.getHeaderView(0);
       // String name = getintent.getBundleExtra("Bundle").getString("Name");
        TextView nav_header_id_text = (TextView) nav_header_view.findViewById(R.id.tv_name);
        ImageView profile_image =(ImageView) nav_header_view.findViewById(R.id.profile_image);
        if(state.equals(CUSTOMER))nav_header_id_text.setText(CUSTOMER);
            else nav_header_id_text.setText(name);

        if(img != null && !(img.equals(""))) {
            Uri uRi = Uri.parse(img);
            Picasso.get().load(uRi).into(profile_image);
        }
        else{Glide.with(this).load(R.drawable.ic_launcher_background).into(profile_image);}


        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                //Todo: 아이탬 연결
                    case R.id.item1 :goSetting(0);break;
                    case R.id.item2 :goSetting(1);break;
                    case R.id.item3 :goSetting(2);break;
                    case R.id.item4 :goSetting(3);break;

                }//switch
                drawerLayout.closeDrawer(navMenu,true);
                return false;
            }//onNavigationItemSelected
        });//listener
        dataSetting();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent;
//                Bundle userBundle = new Bundle();
//
//
//                userBundle.putString("state",state);
//                if(state.equals(USER)){
//                    userBundle.putString("Name",userInfo.getName());
//                    userBundle.putString("Email",userInfo.getEmail());
//                    userBundle.putString("Phone",userInfo.getPhone());
//                    userBundle.putString("Pass",userInfo.getPassword());
//                    userBundle.putString("Image",userInfo.getImagePath());
//                    userBundle.putInt("Level",userInfo.getLevel());
//                }

                Bundle selectTeacher = new Bundle();
                selectTeacher.putString("Instructor",itemInstructors.get(position).getSubTitle());
                intent = new Intent(PIntroActivity.this, MainActivity.class);
               // intent.putExtra("userBundle", userBundle);
                intent.putExtra("selectTeacher",selectTeacher);
                startActivity(intent);

            }
        });
        //자바로 액션메뉴에 햄버거 버튼 만들기
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.draw_open, R.string.draw_close);
        //토글버튼 아이콘이 보이도록 붙이기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //삼선 아이콘 모양으로 동기마추기
        drawerToggle.syncState();
        //삼선 아이콘과 화살표아이콘이 자동 변환되도록
        drawerLayout.addDrawerListener(drawerToggle);
    }//onCreate


    //토글 버튼과 drawer 연결
    //엑티비티 입장에서 토글버튼도 액션바 메뉴 인가 느낌
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //아이템 클릭상황을 토글 버튼에 전달
        drawerToggle.onOptionsItemSelected(item);

        return super.onOptionsItemSelected(item);


    }



    private void  getIntentData(){
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
//        Bundle bundle = getintent.getBundleExtra("Bundle");
//         state = bundle.getString("state");
//        if(state.equals(USER)) {
//             name = bundle.getString("Name");
//             email = bundle.getString("Email");
//             phone = bundle.getString("Phone");
//             pass = bundle.getString("Pass");
//             img = bundle.getString("Image");
//             level = bundle.getInt("Level", 0);
//            userInfo = new UserInfo(name, email, phone, pass, img, level);
//        }else userInfo = null;


    }//getIntentData;
    private void dataSetting(){
        String title ="윤나영";
        String subTitle ="하하 웃음 클레스";
         int imgPath = R.drawable.ic_launcher_background;
        ItemInstructor item = new ItemInstructor(title,subTitle, imgPath);
        /**
         * 서버에서 instructors 정보 읽어와서 add시키기**/

        itemInstructors.add(item);
        AdapterPIntroList mMyAdapter = new AdapterPIntroList(itemInstructors,PIntroActivity.this);
        /* 리스트뷰에 어댑터 등록 */
        listView.setAdapter(mMyAdapter);

    }//dataSetting
    private void goSetting(int item){
        Intent intent= new Intent(PIntroActivity.this,SettingActivity.class);
        intent.putExtra("Item",item);
        startActivity(intent);
    }


}//PIntroActivity
