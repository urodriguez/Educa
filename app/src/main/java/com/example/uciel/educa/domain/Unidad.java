package com.example.uciel.educa.domain;

public class Unidad {
    private ID id;
    private String titulo;
    private String descripcion;
    private int duracionEstimada;
    private boolean publicado;

    public Unidad(String t, String d, int de) {
        titulo = t;
        descripcion = d;
        duracionEstimada = de;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getDuracionEstimada() {
        return duracionEstimada;
    }

    public boolean unidadPublicada(){
        return publicado;
    }
}
