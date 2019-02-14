package com.jian86_android.hahaclass;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdapterFragment extends FragmentPagerAdapter {

    Fragment[] frags = new Fragment[4];
    public AdapterFragment(FragmentManager fm) {
        super(fm);
        frags[0] = new FragInfo();
        frags[1] = new FragLog();
        frags[2] = new FragCalendar();
        frags[3] = new FragBoard();
    }//AdapterFragment

    @Override
    public Fragment getItem(int i) {
        return frags[i];
    }

    @Override
    public int getCount() {
        return frags.length;
    }
}//AdapterFragment
