package com.jian86_android.hahaclass;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class AdapterCalendar extends BaseAdapter {
    ArrayList<Schedule> items;
    Context context;

    public AdapterCalendar(ArrayList<Schedule> items, Context context) {
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
            convertView = inflater.inflate(R.layout.pcalendar_list_item, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        ImageView iv_img = (ImageView) convertView.findViewById(R.id.iv_img) ;
        TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date) ;
        TextView tv_desc = (TextView) convertView.findViewById(R.id.tv_desc) ;
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title) ;
        Button btn_apply = (Button)convertView.findViewById(R.id.btn_apply);


        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        Schedule myItem = (Schedule) getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        String path = myItem.getProjectImgPath();

        tv_date.setText(myItem.getDate());
        tv_desc.setText(myItem.getHost());
        tv_title.setText(myItem.getProjectTitle());

        if(path!=null&&!(path.equals(""))){
            Uri uRi = Uri.parse(path);
            Picasso.get().load(uRi).into(iv_img);
        }else {Glide.with(context).load(R.drawable.ic_launcher_background).into(iv_img);}

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
