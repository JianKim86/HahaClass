package com.jian86_android.hahaclass;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditDialog {
    Context context;
    Fragment fragment;
    Dialog dlg;

    EditText edit_msg;
    Button check_add_btn_send,check_add_btn_back;
    public EditDialog(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        dlg = new Dialog(context);
    }
    public void mkdlg(){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.dialog_edit_pinfo);
        dlg.show();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        Window window = dlg.getWindow();
        window.setLayout((int)(size.x* 0.99f),(int)(size.y*0.52f));
        // 커스텀 다이얼로그의 각 위젯들을 정의.
        edit_msg = (EditText) dlg.findViewById(R.id.edit_msg);
        check_add_btn_send= (Button)dlg.findViewById(R.id.check_add_btn_send);
        check_add_btn_back= (Button)dlg.findViewById(R.id.check_add_btn_back);
    }
 private String edited_msg;
    //수정
    public void callFunction(final TextView tv){
        mkdlg();
        edit_msg.setText(tv.getText().toString());

        check_add_btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edited_msg = edit_msg.getText().toString();
                ((FragInfo)fragment).setEditmsg(edited_msg, tv);
                dlg.dismiss();
            }
        });
        check_add_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });


    }
}//EditDialog
