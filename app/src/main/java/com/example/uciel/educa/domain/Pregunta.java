package com.example.uciel.educa.domain;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Pregunta extends ItemDeExamen{
    private String rtaCorrecta;
    private EditText rtaIngresada;

    public Pregunta(int id, String enunciado, String rtaCorrecta, Context context){
        this.id = id;
        this.enunciado = enunciado;
        this.rtaCorrecta = rtaCorrecta;
        this.context = context;

        rtaIngresada = new TextInputEditText(context);
        rtaIngresada.setHint("Ingrese aquí su respuesta");
        rtaIngresada.setHintTextColor(Color.RED);
        completables.add(rtaIngresada);
        android.util.Log.d("MSG", "OP CORRECTA= " + rtaCorrecta);
    }

    @Override
    public boolean itemAprobado() {
        android.util.Log.d("MSG", "OP INGRESADA= " + rtaIngresada.getText());
        return rtaIngresada.getText().toString().toLowerCase().equals(rtaCorrecta.toLowerCase());
    }
}
