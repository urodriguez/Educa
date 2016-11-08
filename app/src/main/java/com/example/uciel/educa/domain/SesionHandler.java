package com.example.uciel.educa.domain;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SesionHandler {
    private int idSesion;
    private long fechaInicio;
    private boolean iniciada;

    public SesionHandler(int idSesion) {
        this.idSesion = idSesion;
    }

    public int getIdSesion() {
        return idSesion;
    }

    public long getFechaInicio() {
        return fechaInicio;
    }

    public boolean isIniciada() {
        return iniciada;
    }

    public void setFechaInicio(Curso curso) {
        this.fechaInicio = curso.getFechaInicioSesion(idSesion);

        long fechaActual = Calendar.getInstance().getTimeInMillis();

        if(fechaActual<fechaInicio){
            Log.d("MSG", "SESION AUN NO INICIADA");
            iniciada = false;
        } else {
            iniciada = true;
        }
    }

    public String getStringFechaInicio(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fechaInicio);
        Date fechaComienzo = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String cadenaFechaInicio = df.format(fechaComienzo);
        Log.d("MSG", "FECHA INICIO= " + cadenaFechaInicio);
        return cadenaFechaInicio;
    }
}
