package com.example.uciel.educa.domain;

/**
 * Created by nestor on 31/10/16.
 */

public class Respuesta {

    private Integer idPregunta;
    private boolean multipleChoice;
    private Integer idOpcionElegida;
    private String respuesta;

    public Integer getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Integer idPregunta) {
        this.idPregunta = idPregunta;
    }

    public boolean isMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(boolean multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public Integer getIdOpcionElegida() {
        return idOpcionElegida;
    }

    public void setIdOpcionElegida(Integer idOpcionElegida) {
        this.idOpcionElegida = idOpcionElegida;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
