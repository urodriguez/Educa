package com.example.uciel.educa.domain;

public class Docente {
    private int id;
    private Usuario usuario;

    public Docente() {
        this.id = 1;
        this.usuario = new Usuario();
    }

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
