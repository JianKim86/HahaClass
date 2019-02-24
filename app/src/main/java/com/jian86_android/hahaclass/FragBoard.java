package com.jian86_android.hahaclass;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FragBoard extends Fragment {
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private ApplicationClass applicationClass;
    private Context context;
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
    private Button btn_write;
    //AdapterBoard
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board,container,false);
        context = container.getContext();
        applicationClass = (ApplicationClass) (context.getApplicationContext());
        header = inflater.inflate(R.layout.pboard_list_item_header, null, false);

        getData();
        return view;

    }//onCreateView
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv_board = view.findViewById(R.id.lv_board);
        btn_write = view.findViewById(R.id.btn_write);
        setData();


    }//onViewCreated

    private void setData(){
        adapterBoard = new AdapterBoard(boards,context);
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
}
