package com.jian86_android.hahaclass;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterDetailedSchedule extends BaseAdapter {
    ArrayList<DatasItem> items;
    Context context;

    public AdapterDetailedSchedule(ArrayList<DatasItem> items, Context context) {
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
            convertView = inflater.inflate(R.layout.pchalendar_apply_list_item, parent, false);

        }
        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title) ;
        TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date) ;
        TextView tv_week = (TextView) convertView.findViewById(R.id.tv_week) ;
        TextView tv_config = (TextView) convertView.findViewById(R.id.tv_config);

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        DatasItem myItem = (DatasItem) getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */

        tv_title.setText(myItem.getTitle());
        tv_date.setText(myItem.getDate());
        tv_week.setText(myItem.getWeek());
        tv_config.setText(myItem.getConfiguration());

        return convertView;
    }
}
