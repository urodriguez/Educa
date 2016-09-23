package com.example.uciel.educa.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Critica {
    private int id;
    private long fecha;
    private float calificacion;
    private String comentario;

    private String cadenaFecha;

    public Critica(String cf, float ca, String co) {
        cadenaFecha = cf;
        calificacion = ca;
        comentario = co;
    }

    public String getFecha() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fecha);
        Date fechaComienzo = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String cadenaFecha = df.format(fechaComienzo);

        return cadenaFecha;
    }

    public String getCadenaFecha() {
        return cadenaFecha;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public String getComentario() {
        return comentario;
    }


}
