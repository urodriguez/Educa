package com.example.uciel.educa.domain;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uciel.educa.R;

public class TabFragContCurso extends Fragment {
    private int tabNumber;

    public void setTabNumber(int tabNumer){
        this.tabNumber = tabNumer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(tabNumber == 0){
            View view = inflater.inflate(R.layout.tab_cont_curso_one_unids, container, false);
            return view;
        } else {
            return inflater.inflate(R.layout.tab_cont_curso_two_foro, container, false);
        }
    }
}