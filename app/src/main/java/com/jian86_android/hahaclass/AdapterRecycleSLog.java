package com.jian86_android.hahaclass;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterRecycleSLog extends RecyclerView.Adapter {
    //applyTitle, recivedTitle // header
    //applyListF, recivedListF //body
    private static int TYPE_applyTitle = 0;
    private static int TYPE_applyList = 1;
    private static int TYPE_recivedTitle = 3;
    private static int TYPE_recivedList = 4;
    Context context;//
    ApplicationClass applicationClass;

    //apply
    ArrayList<Schedule> aitems;//
    //myclass
    ArrayList<RecivedApplicant> ritems;//
    ArrayList<ApplyClassInfo>applyClassInfos;
    DialogSettingLogMyClass customDialog;


    public AdapterRecycleSLog(Context context, ArrayList<Schedule> aitems, ArrayList<RecivedApplicant> ritems, int aSize, int rSize) {
        this.context = context;
        this.aitems = aitems;
        this.ritems = ritems;
        applicationClass = (ApplicationClass) context.getApplicationContext();
        TYPE_recivedTitle= aSize+1;
        TYPE_recivedList= aSize+2;
//        TYPE_recivedTitle= aitems.size() + 1;
//        TYPE_recivedList= aitems.size() + 2;

    }//constructor
    private boolean isPositionApplyTitle(int position) {
        return position == TYPE_applyTitle;
    }
    private boolean isPositionApplyList(int position) {
        int num = aitems.size();
       // if(aitems.size()<= 0) num= 1;
        return position <= num;
    }
    private boolean isPositionRecivedTitle(int position) {
        int num = aitems.size()+1; //apply title
       // if (aitems.size() <= 0) num = aitems.size()+2;
        return position == num;
    }
    private boolean isPositionRecivedList(int position) {
        int num = aitems.size()+2; //apply title
        if (aitems.size() <= 0) num = aitems.size()+3;
        return position <= num;
    }
    @Override
    public int getItemViewType(int position) {
        if (isPositionApplyTitle(position)) { return TYPE_applyTitle; }
        else if (isPositionRecivedTitle(position)) { return TYPE_recivedTitle; }
        else if (aitems.size()>0 && isPositionApplyList(position)) { return TYPE_applyList; }
        else {return  TYPE_recivedList; }

    }//getItemViewType

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v;
        int layout = 0;
        if (viewType == TYPE_applyTitle) {
            layout = R.layout.psetting_log_list_header;
        } else if (aitems.size() > 0 && viewType == TYPE_applyList) {
            layout = R.layout.psetting_log_apply_list_item;
        } else if (viewType == TYPE_recivedTitle) {
            layout = R.layout.psetting_log_r_list_header;
        }else { layout = R.layout.psetting_log_my_class_list_item; }

        v = LayoutInflater.from(context).inflate(layout,viewGroup,false);
        ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view
        return vhHeader;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Log.i("suzecgecj", "type: "+TYPE_applyTitle+":"+ TYPE_applyList+":"+TYPE_recivedTitle+":"+ TYPE_recivedList );
        ViewHolder viewHolder1 = (ViewHolder) viewHolder;
        Log.i("getControw",getItemCount()+"");
        if (viewHolder1.Holderid == TYPE_applyTitle) {
            //viewHolder.header.initPage();
            Log.i("suzecgecjff", position+":1");

            viewHolder1.tv_list_title_apply.setText(context.getResources().getString(R.string.title_setting_apply_class));
            if(position!=0) viewHolder1.tv_list_title_apply.setVisibility(View.INVISIBLE);
            if(aitems.size()<=0) viewHolder1.empty_apply.setVisibility(View.VISIBLE);
            else viewHolder1.empty_apply.setVisibility(View.GONE);
//        tv_title_myclass.setText(context.getResources().getString(R.string.title_setting_my_class));

        } else if (viewHolder1.Holderid == TYPE_recivedTitle) {
            Log.i("suzecgecjff", position+":2");

            Log.i("suzecgecjff", position+"");
            //viewHolder.footer.initPage();
            viewHolder1.tv_list_title_recived.setText(context.getResources().getString(R.string.title_setting_my_class));
            if(ritems.size()<=0) viewHolder1.empty_recived.setVisibility(View.VISIBLE);
            else viewHolder1.empty_recived.setVisibility(View.GONE);

        } else if (viewHolder1.Holderid == TYPE_applyList) {
            //viewHolder.footer.initPage();
            Log.i("suzecgecjff", position+":3");

            if(aitems.size()>0) {
                Schedule myItem = (Schedule) (aitems.get(position - 1));
                /* 각 위젯에 세팅된 아이템을 뿌려준다 */
                String path = myItem.getProjectImgPath();
                viewHolder1.tv_date.setText(myItem.getDate());
                String d_day = checkDate(myItem.getStart(), myItem.getEnd());
                //현재 날짜와 비교 끝날보다 d_day가 지났으면 강의가 끝났음 을 알림
                //강의시작 날짜 보다 적으면 d_day 설정
                //강의 중이면 진행중 표시
                viewHolder1.tv_d_day.setText(d_day);
                viewHolder1.tv_title.setText(myItem.getProjectTitle());
                //TODO : btn 버튼
            }
            //else{ viewHolder1.layoout_itemA.setVisibility(View.GONE);}
        } else  {
            Log.i("suzecgecjff", position+":4");

            if (position > TYPE_recivedTitle) {
                if (ritems.size() > 0) {
                    //int num = 0;
                    //if (aitems.size() <= 0) num = 1;
                    final RecivedApplicant myItem = (RecivedApplicant) (ritems.get(position - TYPE_recivedList));
                    viewHolder1.tv_date.setText(myItem.getDate());
                    String d_day = "";

                    if (ritems.size() > 0) {
                        d_day = checkDate(myItem.getStart(), myItem.getEnd());
                    }
                    //현재 날짜와 비교 끝날보다 d_day가 지났으면 강의가 끝났음 을 알림
                    //강의시작 날짜 보다 적으면 d_day 설정
                    //강의 중이면 진행중 표시
                    viewHolder1.tv_apply_cnt.setText(myItem.getCnt());
                    viewHolder1.tv_d_day.setText(d_day);
                    viewHolder1.tv_title.setText(myItem.getProjectTitle());
                    viewHolder1.tv_date.setText(myItem.getDate());
                    applyClassInfos = new ArrayList<>();
                    //TODO : btn 버튼
                    viewHolder1.btn_desc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //applyClassInfos , context 보냄
                            //확인 클릭시 리스트를 보여줄 정보 모으기
                            if (applyClassInfos.size() > 0) applyClassInfos.clear();
                            for (int i = 0; i < applicationClass.getApplyClassUserInfos().size(); i++) {
                                if (applicationClass.getApplyClassUserInfos().get(i).getClass_code().equals(myItem.getClass_code()))
                                    applyClassInfos.add(applicationClass.getApplyClassUserInfos().get(i));
                            }
                            if (applyClassInfos.size() <= 0) {
                                new AlertDialog.Builder(context).setMessage("신청자 정보가 없습니다").show();
                                return;
                            }

                            //Todo:커스텀 다이얼 로그
                            customDialog = new DialogSettingLogMyClass(context);
                            customDialog.callFunction(applyClassInfos);

                        }
                    });


                }
                //else { viewHolder1.layoout_itemR.setVisibility(View.GONE); }
            }
        }
    }

    @Override
    public int getItemCount() {
        return ritems.size()+aitems.size() + 2;
    }

    //innerclass
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public int Holderid;
    //위젯 참조
    /* 정의된 위젯에 대한 참조 획득 */
        TextView tv_list_title_apply,tv_list_title_recived, tv_date,tv_d_day,tv_title,tv_apply_cnt;
        Button btn_desc,btn_cancel_or_modify;
        LinearLayout empty_apply,empty_recived, layoout_itemA, layoout_itemR;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            // 타입별 정의
            if (viewType == TYPE_applyTitle) {
                tv_list_title_apply = itemView.findViewById(R.id.tv_header);
                empty_apply = itemView.findViewById(R.id.empty_list);
                Holderid = TYPE_applyTitle;
            } else if (viewType == TYPE_recivedTitle){
                tv_list_title_recived = itemView.findViewById(R.id.tv_header);
                empty_recived = itemView.findViewById(R.id.empty_list);
                Holderid = TYPE_recivedTitle;
            }else if ( viewType == TYPE_applyList) {
                layoout_itemA =itemView.findViewById(R.id.layoout_item);
                 tv_date = (TextView) itemView.findViewById(R.id.tv_date) ;
                tv_d_day = (TextView) itemView.findViewById(R.id.tv_d_day) ;
                 tv_title = (TextView) itemView.findViewById(R.id.tv_title) ;
                 btn_desc = (Button)itemView.findViewById(R.id.btn_desc);
                 btn_cancel_or_modify = (Button)itemView.findViewById(R.id.btn_cancel_or_modify);
                Holderid = TYPE_applyList;
            } else {
                layoout_itemR =itemView.findViewById(R.id.layoout_item);
                tv_date = (TextView) itemView.findViewById(R.id.tv_date) ;
                tv_d_day = (TextView) itemView.findViewById(R.id.tv_d_day) ;
                tv_apply_cnt = (TextView) itemView.findViewById(R.id.tv_apply_cnt) ;
                tv_title = (TextView) itemView.findViewById(R.id.tv_title) ;
                btn_desc = (Button)itemView.findViewById(R.id.btn_desc);
                // 포지션 넘버 getLayoutPosition() - TYPE_recivedTitle
                Holderid = 1000;

            }

        }



    }//ViewHolder

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
