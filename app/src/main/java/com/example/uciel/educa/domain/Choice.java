package com.example.uciel.educa.domain;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.uciel.educa.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Choice extends ItemDeExamen {
    private int idOpcionCorrecta;
    private ArrayList<RadioButton> radioButtons = new ArrayList<>();


    public Choice(int id, String enunciado, JSONArray opciones, Context context){
        this.id = id;
        this.enunciado = enunciado;
        this.context = context;

        android.util.Log.d("MSG", "CREANDO RADIO GROUP");
        RadioGroup rg = new RadioGroup(context);
        rg.setId(id);
        for (int i = 0; i < opciones.length(); i++){
            RadioButton rb = new RadioButton(context);
            try {
                String texto = opciones.getJSONObject(i).getString("texto");
                rb.setText(texto);
                if(opciones.getJSONObject(i).getBoolean("esCorrecta") == true){
                    idOpcionCorrecta = i+1;
                    android.util.Log.d("MSG", "OP CORRECTA= " + idOpcionCorrecta);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            rb.setTextColor(Color.BLACK);
            radioButtons.add(rb);
            rg.addView(rb);

        }
        completables.add(rg);
    }

    @Override
    public boolean itemAprobado() {
        int idOpcionElegida = 0;
        for (int i = 0; i < radioButtons.size(); i++){
            if(radioButtons.get(i).isChecked()){
                idOpcionElegida = i+1;
            }
        }
        android.util.Log.d("MSG", "OP ELEGIDA= " + idOpcionElegida);
        if (idOpcionElegida == idOpcionCorrecta){
            return true;
        } else {
            return false;
        }





        /*int idOpcionElegida = ((RadioGroup)getCompletable()).getCheckedRadioButtonId() - 4*(id - 1);
        android.util.Log.d("MSG", "OP ELEGIDA= " + idOpcionElegida);
        if (idOpcionElegida == idOpcionCorrecta){
            return true;
        }
        return false;*/
    }
}
