package com.example.uciel.educa.domain;

public class Curso {
    public String name;
    public String profesor;
    public int photoId;

    private int id;
    private String nombre;
    private Categoria categoria;
    private String descripcion;
    private String linkImagen;
    private Docente docente;
    private int  valoracionesPromedio;
    private int cantidadValoraciones;
    private long fechaEstimadaProximaSesion;

    public Curso(String name, String profesor, int photoId) {
        this.name = name;
        this.profesor = profesor;
        this.photoId = photoId;
    }

    public Curso(int id, String nombre) {//, Categoria categoria, String descripcion, Docente docente, int valoracionesPromedio, int cantidadValoraciones, long fechaEstimadaProximaSesion) {
        this.id = id;
        this.nombre = nombre;
        /*this.categoria = categoria;
        this.descripcion = descripcion;
        this.docente = docente;
        this.valoracionesPromedio = valoracionesPromedio;
        this.cantidadValoraciones = cantidadValoraciones;
        this.fechaEstimadaProximaSesion = fechaEstimadaProximaSesion;*/
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Docente getDocente() {
        return docente;
    }

    public String getNombreDocente() {
        return docente.getUsuario().getNombre();
    }

    public String getNombreCompletoDocente() {
        return docente.getUsuario().getNombre() + " " + docente.getUsuario().getApellido();
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public int getValoracionesPromedio() {
        return valoracionesPromedio;
    }

    public void setValoracionesPromedio(int valoracionesPromedio) {
        this.valoracionesPromedio = valoracionesPromedio;
    }

    public int getCantidadValoraciones() {
        return cantidadValoraciones;
    }

    public void setCantidadValoraciones(int cantidadValoraciones) {
        this.cantidadValoraciones = cantidadValoraciones;
    }

    public long getFechaEstimadaProximaSesion() {
        return fechaEstimadaProximaSesion;
    }

    public void setFechaEstimadaProximaSesion(long fechaEstimadaProximaSesion) {
        this.fechaEstimadaProximaSesion = fechaEstimadaProximaSesion;
    }

    public String getLinkImagen() {
        return linkImagen;
    }

    public void setLinkImagen(String linkImagen) {
        this.linkImagen = linkImagen;
    }

}