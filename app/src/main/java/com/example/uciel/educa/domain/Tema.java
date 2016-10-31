package com.example.uciel.educa.domain;

public class Tema {
    private int idTema;
    private String titulo, descripcion;

    public Tema(int idT,String t,String d){
        idTema = idT;
        titulo = t;
        descripcion = d;
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
}
