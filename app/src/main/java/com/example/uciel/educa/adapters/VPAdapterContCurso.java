package com.example.uciel.educa.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.uciel.educa.domain.TabFragContCurso;
import com.example.uciel.educa.domain.TabFragment;

public class VPAdapterContCurso extends FragmentStatePagerAdapter {
    public VPAdapterContCurso(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        TabFragContCurso tb = new TabFragContCurso();
        //Returning the current tabs
        switch (position) {
            case 0:
                tb.setTabNumber(0);
                return tb;
            case 1:
                tb.setTabNumber(1);
                return tb;
            case 2:
                tb.setTabNumber(2);
                return tb;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;           // As there are only 3 Tabs
    }

}
