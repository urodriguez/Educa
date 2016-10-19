package com.example.uciel.educa.domain;


import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;

public abstract class ItemDeExamen {
    protected int id;
    protected String enunciado;
    protected ArrayList<View> completables = new ArrayList<>();
    protected Context context;

    public int getId() {
        return id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public abstract boolean itemAprobado();

    public View getCompletable(){
        return this.completables.get(0);
    }
}
