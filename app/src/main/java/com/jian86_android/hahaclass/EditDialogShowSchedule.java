package com.jian86_android.hahaclass;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EditDialogShowSchedule {
    Context context;
    Fragment fragment;
    Dialog dlg;
    ApplicationClass applicationClass;

    TextView tv_date;
    Button check_add_btn_send,check_add_btn_back;
    ListView listView;
    ArrayList<Schedule> items= new ArrayList<>();
    public EditDialogShowSchedule(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        dlg = new Dialog(context);
        applicationClass = (ApplicationClass) (context.getApplicationContext());
    }
    public void mkdlg(){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.dialog_show_schedule);
        dlg.show();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        Window window = dlg.getWindow();
        window.setLayout((int)(size.x* 0.99f),(int)(size.y*0.52f));
        // 커스텀 다이얼로그의 각 위젯들을 정의.
        tv_date = (TextView) dlg.findViewById(R.id.tv_date);
        check_add_btn_send= (Button)dlg.findViewById(R.id.check_add_btn_send);
        listView =(ListView)dlg.findViewById(R.id.lv_schedule);






    }


    private String edited_msg;
    //수정
    public void callFunction(String today, int[] position){
        mkdlg();

        if(position.length!=0){
            for(int i=0; i<position.length;i++){
                Schedule schedule = applicationClass.getItemInstructor().getSchedules().get(i);
                items.add(schedule);
            }
        }

        AdapterSchedule adapterSchedule =new AdapterSchedule(items, context);
        listView.setAdapter(adapterSchedule);

        tv_date.setText(today);
        check_add_btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

    } //callFunction

    class AdapterSchedule extends BaseAdapter {
        ArrayList<Schedule> items;
        Context context;

        public AdapterSchedule(ArrayList<Schedule> items, Context context) {
            this.items = items;
            this.context = context;
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
                convertView = inflater.inflate(R.layout.pcalendar_cal_list_item, parent, false);
            }

            /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
            TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            Button btn_apply = (Button) convertView.findViewById(R.id.btn_apply);


            /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
            Schedule myItem = (Schedule) getItem(position);

            /* 각 위젯에 세팅된 아이템을 뿌려준다 */
       //     String path = myItem.getProjectImgPath();

            tv_date.setText(myItem.getDate());
            tv_title.setText(myItem.getProjectTitle());


            final int i = position;
            btn_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ApplyActivity.class);
                    intent.putExtra("position", i);
                    context.startActivity(intent);

                    //  Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
                }
            });


            return convertView;

        }
    }



}//EditDialogShowSchedule
