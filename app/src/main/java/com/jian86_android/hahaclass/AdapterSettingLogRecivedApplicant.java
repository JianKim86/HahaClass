package com.jian86_android.hahaclass;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterSettingLogRecivedApplicant extends BaseAdapter {
    ArrayList<RecivedApplicant> items;
    Context context;
    ApplicationClass applicationClass;
    ArrayList<ApplyClassInfo>applyClassInfos;
    DialogSettingLogMyClass customDialog;
    public AdapterSettingLogRecivedApplicant(ArrayList<RecivedApplicant> items, Context context) {
        this.items = items;
        this.context = context;
        applicationClass= (ApplicationClass)context.getApplicationContext();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.psetting_log_my_class_list_item, parent, false);
        }


        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date) ;
        TextView tv_apply_cnt = (TextView) convertView.findViewById(R.id.tv_apply_cnt) ;
        TextView tv_d_day = (TextView) convertView.findViewById(R.id.tv_d_day) ;
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title) ;

        Button btn_desc = (Button)convertView.findViewById(R.id.btn_desc);


        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        final RecivedApplicant myItem = (RecivedApplicant) getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
//        String path = myItem.getProjectImgPath();

        tv_date.setText(myItem.getDate());
        String d_day ="";

        if(items.size()>0){ d_day= checkDate(myItem.getStart(), myItem.getEnd());
            Log.i("sdsdsds",myItem.getStart()+"**"+ myItem.getEnd());
        }
        //현재 날짜와 비교 끝날보다 d_day가 지났으면 강의가 끝났음 을 알림
        //강의시작 날짜 보다 적으면 d_day 설정
        //강의 중이면 진행중 표시
        tv_apply_cnt.setText(myItem.getCnt());
        tv_d_day.setText(d_day);
        tv_title.setText(myItem.getProjectTitle());
        tv_date.setText(myItem.getDate());
        applyClassInfos= new ArrayList<>();
        //TODO : btn 버튼
        btn_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //applyClassInfos , context 보냄
                //확인 클릭시 리스트를 보여줄 정보 모으기
                if(applyClassInfos.size()>0) applyClassInfos.clear();
                for(int i = 0; i<applicationClass.getApplyClassUserInfos().size(); i++){
                    if(applicationClass.getApplyClassUserInfos().get(i).getClass_code().equals(myItem.getClass_code()))
                        applyClassInfos.add(applicationClass.getApplyClassUserInfos().get(i));
                }
                Log.i("lengthdd",applyClassInfos.size()+"");
                if(applyClassInfos.size()<=0){new AlertDialog.Builder(context).setMessage("신청자 정보가 없습니다").show(); return;
                }

                //Todo:커스텀 다이얼 로그
                    customDialog = new DialogSettingLogMyClass(context);
                    customDialog.callFunction(applyClassInfos);

            }
        });

        return convertView;
    }

    private String checkDate(String startDay,String endDay){
        //day form : 2019-04-22
        //오늘 날짜 얻기
        Date nDay = new Date();
        //연산을 위한 date 변환
        Date sDay=null;
        Date eDay=null;
        String returnValue="";
        try {
            sDay = new SimpleDateFormat("yyyy-MM-dd").parse(startDay);
            eDay = new SimpleDateFormat("yyyy-MM-dd").parse(endDay);
            //날짜 비교
            long eCalDate = nDay.getTime() - eDay.getTime(); //양수면 수강 날짜 지남
            long sCalDate = nDay.getTime() - sDay.getTime(); //음수면 아직 강의 전임 d_day 보내기

            if (eCalDate>0) { returnValue = "강의종료";} //강의 날짜 지남
            else if(sCalDate>=0 && eCalDate<=0){returnValue = "진행중인 강의";}
            else {
                long calDateDays = sCalDate / (24 * 60 * 60 * 1000); //날짜 차이를 날수로
                calDateDays = Math.abs(calDateDays);
                returnValue = "d-day : " + calDateDays;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return returnValue;

    }//checkDate




}
