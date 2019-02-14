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

import java.util.ArrayList;
import java.util.List;

public class AdapterPIntroList extends BaseAdapter {
    ArrayList<ItemInstructor>items;
    Context context;

    public AdapterPIntroList(ArrayList<ItemInstructor> items, Context context) {
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
            convertView = inflater.inflate(R.layout.pintro_list_item, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        ImageView iv_img = (ImageView) convertView.findViewById(R.id.iv_img) ;
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_title) ;
        TextView tv_desc = (TextView) convertView.findViewById(R.id.tv_desc) ;

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        ItemInstructor myItem = (ItemInstructor) getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        Glide.with(context).load(myItem.getImgPath()).into(iv_img);
        tv_name.setText(myItem.getTitle());
        tv_desc.setText(myItem.getSubTitle());

        return convertView;
    }
}
