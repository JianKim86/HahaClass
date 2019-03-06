package com.jian86_android.hahaclass;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterPIntroList extends BaseAdapter {
    private static final int img_length =60;
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
        String path = myItem.getImgPath();

        tv_name.setText(myItem.getTitle());
        tv_desc.setText(myItem.getSubTitle());

        if(path != null && path.length() != img_length) {
            Uri uRi = Uri.parse(path);
            Picasso.get().load(uRi)
                    .resize(400,400).centerCrop().into(iv_img, new Callback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError(Exception e) {

                }
            });
        }
        else{Glide.with(context).load(R.drawable.ic_launcher_background).into(iv_img);}


//
//        if(path!=null&&!(path.equals(""))){
//            Uri uRi = Uri.parse(path);
//            Picasso.get().load(uRi).into(iv_img);
//        }else {Glide.with(context).load(R.drawable.ic_launcher_background).into(iv_img);}
        return convertView;
    }

}
