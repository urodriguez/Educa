package com.example.uciel.educa.domain;

public class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String foto;

    public Usuario() {
        this.id = 1;
        this.nombre = "Nombre";
        this.nombre = "Apellido";
        this.email = "nombre@mail.com";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
