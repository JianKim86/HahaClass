package com.jian86_android.hahaclass;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class FragInfo extends Fragment implements View.OnClickListener {
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private ApplicationClass applicationClass;
    private Context context;
    private UserInfo userInfo;

    private String picPath;
    private Bitmap btmapPicPath;
    String state,name,email,phone,pass,img;
    int level;
    ItemInstructor instructor;
    private TextView list_license, list_field, list_career;
    private ImageView iv_infoimg, iv_edit, iv_edit_license, iv_edit_field, iv_edit_career;
    private Button btn_save;
    private Boolean isChanege=false;
    private Boolean isSave = false;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info,container,false);
        context = container.getContext();
        applicationClass = (ApplicationClass) (context.getApplicationContext());
        getData();
        return view;

    }//onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list_license = view.findViewById(R.id.list_license);
        list_field = view.findViewById(R.id.list_field);
        list_career = view.findViewById(R.id.list_career);
        iv_infoimg = view.findViewById(R.id.iv_infoimg);
        iv_edit = view.findViewById(R.id.iv_edit);
        iv_edit_license = view.findViewById(R.id.iv_edit_license);
        iv_edit_field = view.findViewById(R.id.iv_edit_field);
        iv_edit_career = view.findViewById(R.id.iv_edit_career);
        btn_save =view.findViewById(R.id.btn_save);
        setData();
        //Toast.makeText(context, ""+level, Toast.LENGTH_SHORT).show();
        if(level==3){
            iv_infoimg.setClickable(true);
            iv_edit.setVisibility(View.VISIBLE);
            iv_edit_license.setVisibility(View.VISIBLE);
            iv_edit_field.setVisibility(View.VISIBLE);
            iv_edit_career.setVisibility(View.VISIBLE);
        }

        if(picPath!=null){
            setPic(picPath);
        }else if (btmapPicPath!=null) {
            setPic(btmapPicPath);
        }

        if(level==3){iv_infoimg.setOnClickListener(this);}
        if(level==3){iv_edit_license.setOnClickListener(this);}
        if(level==3){ iv_edit_field.setOnClickListener(this);}
        if(level==3){iv_edit_career.setOnClickListener(this);}
        if(level==3){btn_save.setOnClickListener(this);}
    }//onViewCreated



 //inforImg
    public void setPic(String pic){
        //Toast.makeText(applicationClass, ""+pic, Toast.LENGTH_SHORT).show();
        picPath =pic;
        if(picPath!=null&&!(picPath.equals(""))){
        Uri uRi = Uri.parse(picPath);
        Picasso.get().load(uRi).into(iv_infoimg);
        }else {Glide.with(this).load(R.drawable.ic_launcher_background).into(iv_infoimg);}
        btmapPicPath = null;
        applicationClass.getItemInstructor().setImgPath(picPath);
    }//setPic
    public void setPic(Bitmap pic){
        btmapPicPath = pic;
        Glide.with(this).load(pic).into(iv_infoimg);
        picPath=null;
        isSave=false;

    }

    void setData(){
        setPic(instructor.getImgPath());
        list_license.setText(instructor.getLicense());
        list_field.setText(instructor.getField());
        list_career.setText( instructor.getCareer());

    }//
    void getData(){
        instructor= applicationClass.getItemInstructor();
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

    }
    EditDialog customDialog;
    public void setEditmsg(String msg, TextView tv){
        tv.setText(msg);
        isChanege = true;
        isSave= false;
        btn_save.setVisibility(View.VISIBLE);
    }
    private void editDial(TextView tv){
        //Todo:커스텀 다이얼 로그
        customDialog = new EditDialog(context, this);
        customDialog.callFunction(tv);
    }//editDial

    void saveInstructor(){
        String msg =getString(R.string.save_instructor);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle("AlertDialog Title");
        builder.setMessage(msg);
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       //저장
                        if(isChanege){
                        applicationClass.getItemInstructor().setImgPath(picPath);
                        applicationClass.getItemInstructor().setLicense(list_license.getText().toString());
                        applicationClass.getItemInstructor().setCareer(list_career.getText().toString());
                        applicationClass.getItemInstructor().setField(list_field.getText().toString());
                        }
                        isSave = true;
                        isChanege = false;
                        btn_save.setVisibility(View.GONE);
                        //TODO ::서버 연결 저장
                    }
                });
        builder.setNegativeButton("아니오", null);
        builder.show();
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_infoimg:  ((MainActivity)context).takePic();isChanege = true; btn_save.setVisibility(View.VISIBLE); break;
            case R.id.iv_edit_license: editDial(list_license); break;
            case R.id.iv_edit_field: editDial(list_field); break;
            case R.id.iv_edit_career: editDial(list_career); break;
            case R.id.btn_save: if(!isSave&&isChanege) saveInstructor(); break;

        }
    }//onClick
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(getActivity()!=null){
            if(isVisibleToUser)
            {

            }
            else
            {
                Toast.makeText(context, "사라질때", Toast.LENGTH_SHORT).show();
                //화면이 사라질때
                if(level==3){
                    if(!isSave&&isChanege) saveInstructor();
                }

            }
        }
        super.setUserVisibleHint(isVisibleToUser);


    }//setUserVisibleHint

    @Override
    public void onPause() {
        //화면이 사라질때
        super.onPause();
    }//onPause


}
