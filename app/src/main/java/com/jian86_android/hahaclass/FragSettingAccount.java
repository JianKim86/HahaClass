package com.jian86_android.hahaclass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;


public class FragSettingAccount extends Fragment implements View.OnClickListener{
    private static final int img_length = 60;
    private ApplicationClass applicationClass;
    private Context context;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private UserInfo userInfo;

    String state,name,email,phone,pass,img;
    int level;
    private String picPath;
    private Bitmap btmapPicPath;

    private TextView tv_name,tv_email,tv_phon;
    private ImageView edit_phone,iv_infoimg;
    private EditText phone_numer;
    TextInputLayout edit_layout;
    Button submit_account;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_account,container,false);
        context = container.getContext();
        applicationClass = (ApplicationClass) (context.getApplicationContext());

        return view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
        //binding
        tv_name= view.findViewById(R.id.tv_name);
        tv_email= view.findViewById(R.id.tv_email);
        tv_phon= view.findViewById(R.id.tv_phon);
        iv_infoimg= view.findViewById(R.id.iv_myimg);
        submit_account= view.findViewById(R.id.submit_account);
        edit_phone= view.findViewById(R.id.edit_phone);
        edit_layout= view.findViewById(R.id.edit_layout);
        phone_numer =view.findViewById(R.id.phone_numer);
        settingData();
        edit_phone.setOnClickListener(this);
        iv_infoimg.setOnClickListener(this);
        submit_account.setOnClickListener(this);
        //필터
        phone_numer.setFilters(new InputFilter[]{filterAlphaNum});

    }

    //inforImg
    String picPathn;
    public void setPic(String pic, String realpath){
        picPathn= pic;
        picPath =realpath;

        if(img != null && img.length() != img_length) {
            Uri uRi = Uri.parse(picPathn);
            Picasso.get().load(uRi)
                    .resize(400,400).centerCrop().into(iv_infoimg, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Log.d("picPath ", "pIntor img: load failed "+ picPath);
                }
            });
        }else if(img != null && !(picPathn.equals(""))){
            Uri uRi = Uri.parse(picPathn);
            Picasso.get().load(uRi)
                    .resize(400,400).centerCrop().into(iv_infoimg, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Log.d("picPath ", "pIntor img: load failed "+ picPath);
                }
            });
        }
        else{Glide.with(this).load(R.drawable.ic_launcher_background).into(iv_infoimg);}
//        Uri uRi = Uri.parse(picPath);
//        Picasso.get().load(uRi).into(iv_infoimg);
//
//
        btmapPicPath = null;
        //applicationClass.getUserInfo().setImagePath(picPath);

    }//setPic
    public void setPic(Bitmap pic){
        btmapPicPath = pic;
        Glide.with(this).load(pic).into(iv_infoimg);
        picPathn=null;
    }


    // 숫자만 입력 되도록
    public InputFilter filterAlphaNum = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            //  숫자만 입력 되도록 : "^[0-9]*$"
            Pattern ps = Pattern.compile("^[0-9]*$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };
    void getData() {

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
            level = 0;
            userInfo = null;
        }
    }//getData
    void settingData(){
        tv_name.setText(userInfo.getName());
        tv_email.setText(userInfo.getEmail());
        tv_phon.setText(userInfo.getPhone());

        if(img != null && img.length()!=img_length) {
            setPic(img,img);
        }else if (btmapPicPath!=null) {
            setPic(btmapPicPath);
        }

    }
    private void editPhone(){
        edit_layout.setVisibility(View.VISIBLE);
    }
    private  void submit_frg(){
        phone_numer.setError(null);
        boolean cancel = false;
        View focusView = null;
        // Store values at the time of the login attempt.
        String phonenum= phone_numer.getText().toString();
        if (TextUtils.isEmpty(phonenum)) {
            cancel = false;
        } else if (!isPoneValid(phonenum)) {
            phone_numer.setError(getString(R.string.error_invalid_phone));
            focusView = phone_numer;
            cancel = true;
       // if()
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            String chanegeText;
            if(TextUtils.isEmpty(phonenum)){chanegeText= tv_phon.getText().toString();}
            else { chanegeText= phone_numer.getText().toString(); tv_phon.setText(chanegeText);}



            if(ischangePhone) applicationClass.getUserInfo().setPhone(chanegeText);
            if(ischangeImg) applicationClass.getUserInfo().setImagePath(picPathn);

           // Toast.makeText(applicationClass, ""+ , Toast.LENGTH_SHORT).show();
            phone_numer.setText("");
            edit_layout.setVisibility(View.GONE);
            ((SettingActivity)getActivity()).setUpNav();

            //TODO: 확인 후 돌아가기
            saveDB();
        }

    }//submit_frg


    private void saveDB(){

        new Thread() {
            @Override
            public void run() {

                //1. userinfo 에 있는 정보 유저 테이블에 넣기
                String serverUrl = "http://jian86.dothome.co.kr/HahaClass/change_data.php";
                SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        new AlertDialog.Builder(context).setMessage(response).show(); return;

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        return;
                    }
                });
                multiPartRequest.addStringParam("email",applicationClass.getUserInfo().getEmail());
                if(ischangeImg) multiPartRequest.addFile("image_path", picPath);
                if(ischangePhone) multiPartRequest.addStringParam("phone", applicationClass.getUserInfo().getPhone());
                //요청객체를 실제 서버쪽으로 보내기 위해 우체통같은 객체
                RequestQueue requestQueue = Volley.newRequestQueue(context);

                //요청 객체를 우체통에 넣기
                requestQueue.add(multiPartRequest);
            }

        }.start();

    }//saveDB



    private boolean ischangeImg =false;
    private boolean ischangePhone =false;


    private boolean isPoneValid(String phone) {
        return phone.length() > 10;
    }
    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.iv_myimg: ((SettingActivity)getActivity()).takePic(); ischangeImg = true; break;
            case R.id.edit_phone: if(!ischangePhone){editPhone(); ischangePhone = true;} else{ ischangePhone= false; phone_numer.setText(""); edit_layout.setVisibility(View.GONE);} break;
            case R.id.submit_account: submit_frg(); break;
        }

    }//onclick


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

            }
        }
        super.setUserVisibleHint(isVisibleToUser);


    }//setUserVisibleHint



}//FragSettingAccount
