package com.example.uciel.educa.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ComentarioForo {
    private String autor, contenido;
    private long fecha;
    private boolean estado;

    public ComentarioForo(String autor, long fecha, String contenido, boolean estado) {
        this.autor = autor;
        this.fecha = fecha;
        this.contenido = contenido;
        this.estado = estado;
    }

    public String getAutor() {
        return autor;
    }

    public String getFecha() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fecha);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(calendar.getTime());
    }

    public String getContenido() {
        return contenido;
    }

    public boolean fueAprobado(){
        return estado;
    }
}
