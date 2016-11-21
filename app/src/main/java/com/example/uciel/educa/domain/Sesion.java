package com.example.uciel.educa.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Sesion {
    private ID id;
    private long fechaDesde;
    private long fechaHasta;
    private long fechaDesdeInscripcion;

    private String cadenaFechaDesde;
    private String cadenaFechaHasta;
    private String cadenaFechaDesdeInscripcion;

    public Sesion(String cfd, String cfh, String cfdi) {
        cadenaFechaDesde = cfd;
        cadenaFechaHasta = cfh;
        cadenaFechaDesdeInscripcion = cfdi;
    }

    public String getFechaDesde() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fechaDesde);
        Date fechaComienzo = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String cadenaFechaDesde = df.format(fechaComienzo);

        return cadenaFechaDesde;
    }

    public String getFechaHasta() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fechaHasta);
        Date fechaComienzo = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String cadenaFechaHasta = df.format(fechaComienzo);

        return cadenaFechaHasta;
    }

    public String getFechaDesdeInscripcion() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fechaDesdeInscripcion);
        Date fechaComienzo = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String cadenaFechaDesdeInscripcion = df.format(fechaComienzo);

        return cadenaFechaDesdeInscripcion;
    }

    public String getCadenaFechaDesde() {
        return cadenaFechaDesde;
    }

    public String getCadenaFechaHasta() {
        return cadenaFechaHasta;
    }

    public String getCadenaFechaDesdeInscripcion() {
        return cadenaFechaDesdeInscripcion;
    }

    public int getID() {
        return this.id.getID();
    }

    public long getFechaInicio(){
        return this.fechaDesde;
    }
    public long getLongFechaDesdeInscripcion(){
        return this.fechaDesdeInscripcion;
    }

    public boolean estaVigente() {
        return Calendar.getInstance().getTimeInMillis() <= fechaHasta;
    }
}
