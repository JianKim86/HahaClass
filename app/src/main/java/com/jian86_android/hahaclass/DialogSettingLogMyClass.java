package com.jian86_android.hahaclass;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class DialogSettingLogMyClass {
    Context context;
    Fragment fragment;
    Dialog dlg;
    EditText edit_msg;
    Button check_btn;
    ListView listView;
    View header_view;
    public DialogSettingLogMyClass(Context context) {
        this.context = context;
        dlg = new Dialog(context);
    }
    public void mkdlg(ArrayList<ApplyClassInfo> applyClassInfos){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.dialog_setting_log_my_class);
        dlg.show();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        Window window = dlg.getWindow();
        window.setLayout((int)(size.x* 0.99f),(int)(size.y*0.99f));
        // 커스텀 다이얼로그의 각 위젯들을 정의.
        check_btn = dlg.findViewById(R.id.check_btn);
        listView = dlg.findViewById(R.id.lv_apply_user_list);
        header_view = ((LayoutInflater)(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.psetting_log_rheader, null, false);
        listView.addHeaderView(header_view);
        AdapterSettingLogRApplyUI adapter = new AdapterSettingLogRApplyUI(applyClassInfos,context);
        listView.setAdapter(adapter);
    }

    public void callFunction(ArrayList<ApplyClassInfo> applyClassInfos){
        mkdlg(applyClassInfos);
        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}//EditDialog
