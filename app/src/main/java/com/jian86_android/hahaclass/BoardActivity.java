package com.jian86_android.hahaclass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {
    private ApplicationClass applicationClass;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private Toolbar toolbar;
    private NavigationView navMenu;
    private DrawerLayout drawerLayout;
    private UserInfo userInfo;

    String state,name,email,phone,pass,img;
    int level;
    private String picPath;
    private Bitmap btmapPicPath;
    View header;
    ListView lv_board;
    ArrayList<Board>boards;
    AdapterBoard adapterBoard;

    private TextView tv_total_count,tv_board_title;
    private ImageView iv_title_img,iv_edit_img,iv_edit_title;
    private Button btn_write;//메뉴
    private ActionBarDrawerToggle drawerToggle;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }//onCreateOptionsMenu
    //메뉴
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.goback:
                finish();
                break;
        }
        drawerToggle.onOptionsItemSelected(item);
        //아이템 클릭상황을 토글 버튼에 전달
        return super.onOptionsItemSelected(item);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        applicationClass =(ApplicationClass)getApplicationContext();
        header = getLayoutInflater().inflate(R.layout.pboard_list_item_header, null, false);
        getData();
        toolbar =findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        navMenu = findViewById(R.id.nav_menu);
        drawerLayout =findViewById(R.id.drawer_layout);

        lv_board = findViewById(R.id.lv_board);
        btn_write = findViewById(R.id.btn_write);
        setData();
        navSetting();
        selectListitem();
    }//onCreate
    private void navSetting(){
        View nav_header_view = navMenu.inflateHeaderView(R.layout.nav_header);
        TextView nav_header_id_text = (TextView) nav_header_view.findViewById(R.id.tv_name);
        ImageView profile_image =(ImageView) nav_header_view.findViewById(R.id.profile_image);
        if(state.equals(CUSTOMER))nav_header_id_text.setText(CUSTOMER);
        else nav_header_id_text.setText(name);

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
        //자바로 액션메뉴에 햄버거 버튼 만들기
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.draw_open, R.string.draw_close);
        //토글버튼 아이콘이 보이도록 붙이기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //삼선 아이콘 모양으로 동기마추기
        drawerToggle.syncState();
        //삼선 아이콘과 화살표아이콘이 자동 변환되도록
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setTitle("게시판");

    }//navSetting
    private void setData(){
        adapterBoard = new AdapterBoard(boards,BoardActivity.this);
        lv_board.addHeaderView(header);
        tv_total_count = header.findViewById(R.id.tv_total_count);
        iv_title_img = header.findViewById(R.id.iv_title_img);
        iv_edit_img = header.findViewById(R.id.iv_edit);

        tv_board_title = header.findViewById(R.id.tv_board_title);
        iv_edit_title = header.findViewById(R.id.iv_edit_title);
        if(level==4){
            iv_edit_img.setVisibility(View.VISIBLE);
            iv_edit_title.setVisibility(View.VISIBLE);
        }
        tv_board_title.setText(applicationClass.getBoard_title());
        tv_total_count.setText(boards.size()+"");
        lv_board.setAdapter(adapterBoard);
        if(applicationClass.getBoard_imgpath()!=null&&!(applicationClass.getBoard_imgpath().equals(""))){
            setPic(img);
        }
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.iv_title_img:
                        if(level==4); break;
                    case R.id.iv_edit_title:
                        if(level==4); break;
                    case R.id.btn_write:
                        break;
                }
            }
        };

        if(level==4) iv_title_img.setOnClickListener(clickListener);
        if(level==4) iv_edit_title.setOnClickListener(clickListener);
        btn_write.setOnClickListener(clickListener);


    }//setData
    private void getData(){
        boards = applicationClass.getBoards();
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
            level = 0;
            userInfo = null;
        }


    }//getData
    private int cPosition;
    private void selectListitem(){
        lv_board.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    cPosition= position-1;
                    Intent intent = new Intent(BoardActivity.this,BoarderDetailsActivity.class);
                    intent.putExtra("position",cPosition);
                    startActivity(intent);
                }//if 헤더 제외
            }
        });
    }//selectListitem



    public void setPic(String pic) {
        picPath = pic;
        if (picPath != null && !(picPath.equals(""))) {
            Uri uRi = Uri.parse(picPath);
            Picasso.get().load(uRi)
                    .resize(400, 400).into(iv_title_img, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Log.d("picPath ", "pIntor img: load failed " + picPath);
                }
            });
        } else {
            Glide.with(this).load(R.drawable.ic_launcher_background).into(iv_title_img);
        }


    }
    private void goSetting(int item){
        if(state.equals(USER)){
            Intent intent= new Intent(BoardActivity.this,SettingActivity.class);
            intent.putExtra("Item",item);
            intent.putExtra("stage",2);
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
                            Intent intent = new Intent(BoardActivity.this,LoginActivity.class);
                            intent.putExtra("want",3);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.setNegativeButton("아니오", null);
            builder.show();
        }
    }
}//onCreate
