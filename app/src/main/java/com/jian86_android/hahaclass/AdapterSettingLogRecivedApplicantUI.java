package com.jian86_android.hahaclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterSettingLogRecivedApplicantUI extends BaseAdapter {
    ArrayList<ApplyClassInfo> items;
    Context context;

    public AdapterSettingLogRecivedApplicantUI(ArrayList<ApplyClassInfo> items, Context context) {
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
            convertView = inflater.inflate(R.layout.psetting_log_my_class_applicant_info_item, parent, false);

        }
        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name) ;
        TextView tv_email = (TextView) convertView.findViewById(R.id.tv_email) ;
        TextView tv_phone = (TextView) convertView.findViewById(R.id.tv_phone) ;
        TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        ApplyClassInfo myItem = (ApplyClassInfo) getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        String date="";
        if(myItem.getDate()!= null && myItem.getDate().length()>0){
            date = myItem.getDate();
            date = date.substring(0,"2019-03-12".length());
        }
        tv_name.setText(myItem.getApply_name());
        tv_date.setText(date);
        tv_email.setText(myItem.getApply_user_email());
        tv_phone.setText(myItem.getApply_phone());



        return convertView;
    }

}
