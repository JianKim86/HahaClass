package com.jian86_android.hahaclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterBoard extends BaseAdapter {
    ArrayList<Board> items;
    Context context;
    ApplicationClass applicationClass;

    public AdapterBoard(ArrayList<Board> items, Context context) {
        this.items = items;
        this.context = context;
        this.applicationClass = (ApplicationClass) context.getApplicationContext();
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
            convertView = inflater.inflate(R.layout.pboard_list_item, parent, false);
        }
        TextView tv_no=(TextView)convertView.findViewById(R.id.tv_no);
        TextView tv_writer=(TextView)convertView.findViewById(R.id.tv_writer);
        TextView tv_instructor=(TextView)convertView.findViewById(R.id.tv_instructor);
        TextView tv_title=(TextView)convertView.findViewById(R.id.tv_title);
        TextView tv_date=(TextView)convertView.findViewById(R.id.tv_date);
        TextView tv_reply=(TextView)convertView.findViewById(R.id.tv_reply);
        ImageView iv_img=(ImageView) convertView.findViewById(R.id.iv_img);

        Board myItem = (Board) getItem(position);

        tv_no.setText(myItem.getBoard_no());
        tv_title.setText(myItem.getBoard_title());
        tv_date.setText(myItem.getBoard_date());
        tv_reply.setText(myItem.getBoard_reply());
        tv_writer.setText(myItem.getBoard_writer());
        tv_instructor.setText(myItem.getBoard_instructor());


        if(myItem.isImg()) Glide.with(context).load(R.drawable.ic_launcher_background).into(iv_img);
        else iv_img.setVisibility(View.GONE);



        return convertView;
    }
}//AdapterBoard
