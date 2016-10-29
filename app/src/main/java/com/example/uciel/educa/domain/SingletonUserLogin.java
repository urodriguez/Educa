package com.example.uciel.educa.domain;

import java.util.HashMap;
import java.util.Map;

public class SingletonUserLogin {
    private static SingletonUserLogin instance = null;

    private String userName, userID;
    private Map<Integer, Integer> mis_IDCURSO_IDSESSION = new HashMap<>();

    protected SingletonUserLogin() {
    }

    public static SingletonUserLogin getInstance() {
        if(instance == null) {
            instance = new SingletonUserLogin();
        }
        return instance;
    }

    public void setUserLoginData(String un, String uID){
        this.userName = un;
        this.userID = uID;
    }

    public void setUserName(String un){
        this.userName = un;
    }

    public void setUserID(String uID){
        this.userID = uID;
    }

    public String  getUserName(){
        return this.userName;
    }

    public String getUserID(){
        return this.userID;
    }

    public void registrarSesionInscriptaAcurso(int idSesion, int idCurso){
        mis_IDCURSO_IDSESSION.put(idCurso,idSesion);
    }

    public void borrarCurso(int idCurso){
        mis_IDCURSO_IDSESSION.remove(idCurso);
    }
}
