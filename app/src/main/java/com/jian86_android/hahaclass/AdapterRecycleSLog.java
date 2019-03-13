package com.jian86_android.hahaclass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AdapterRecycleSLog extends RecyclerView.Adapter {
    //applyTitle, recivedTitle // header
    //applyListF, recivedListF //body
    final static int applyTitle = 100;
    final static int recivedTitle = 200;
    final static int applyListF = 300;
    final static int recivedListF = 400;
    Context context;//
    ApplicationClass applicationClass;
    //apply
    ArrayList<Schedule> aitems;//
    //myclass
    ArrayList<RecivedApplicant> ritems;//
    ArrayList<ApplyClassInfo>applyClassInfos;
    DialogSettingLogMyClass customDialog;

    @Override
    public int getItemViewType(int position) {



        return super.getItemViewType(position);

    }//getItemViewType

    public AdapterRecycleSLog(Context context, ArrayList<Schedule> aitems, ArrayList<RecivedApplicant> ritems) {
        this.context = context;
        this.aitems = aitems;
        this.ritems = ritems;
        applicationClass = (ApplicationClass) context.getApplicationContext();
    }//constructor

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return ritems.size()+aitems.size()+2;
    }
}
