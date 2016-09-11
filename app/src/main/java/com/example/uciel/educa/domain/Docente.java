package com.example.uciel.educa.domain;

/**
 * Created by nestor on 11/09/16.
 */
public class Docente {
    private int id;
    private Usuario usuario;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
