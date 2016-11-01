package com.example.uciel.educa.domain;

public class Tema {
    private int idTema;
    private String titulo;
    private String descripcion;
    private String estado;


    public Tema(int idT, String t, String d, String e){
        idTema = idT;
        titulo = t;
        descripcion = d;
        estado = e;
    }

    public int getIdTema() {
        return idTema;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean fueAprobado() {
        return estado.equals("APROBADO");
    }
}
