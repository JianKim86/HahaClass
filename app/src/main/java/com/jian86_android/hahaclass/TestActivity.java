package com.jian86_android.hahaclass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class TestActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        listView =findViewById(R.id.lv);
        View v = getLayoutInflater().inflate(R.layout.header,null);
        listView.addHeaderView(v);
    }
}
