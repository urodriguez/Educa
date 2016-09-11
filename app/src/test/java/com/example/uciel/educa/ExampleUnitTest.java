package com.example.uciel.educa;

import com.example.uciel.educa.domain.Curso;
import com.example.uciel.educa.domain.CursoContainer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void parseHomeResponseTest(){
        String response="[{\"id\":1,\"nombre\":\"Algoritmos III\",\"categoria\":{\"id\":1,\"descripcion\":\"Programación\",\"imagen\":\"programacion.png\"},\"categoriaId\":1,\"descripcion\":\"Curso de algoritmoss III\",\"imagen\":\"\",\"docente\":{\"id\":1,\"usuario\":{\"id\":1,\"nombre\":\"Carlos\",\"apellido\":\"Fontela\",\"email\":\"carlos@mail.com\",\"foto\":null},\"identificacion\":\"Carlos Fontela (carlos@mail.com)\"},\"valoracionesPromedio\":0,\"cantidadValoraciones\":0,\"fechaEstimadaProximaSesion\":1477800000000,\"foto\":null},{\"id\":2,\"nombre\":\"Cocina por microondas\",\"categoria\":{\"id\":2,\"descripcion\":\"Gastronomía\",\"imagen\":\"gastronomia.png\"},\"categoriaId\":2,\"descripcion\":\"Curso de cocina por microondas\",\"imagen\":\"\",\"docente\":{\"id\":2,\"usuario\":{\"id\":2,\"nombre\":\"Alejandro\",\"apellido\":\"Molinari\",\"email\":\"alejandro@mail.com\",\"foto\":null},\"identificacion\":\"Alejandro Molinari (alejandro@mail.com)\"},\"valoracionesPromedio\":0,\"cantidadValoraciones\":0,\"fechaEstimadaProximaSesion\":1477800000000,\"foto\":null}\n" +
                "]";
        Gson g = new Gson();

        Type collectionType = new TypeToken<Collection<Curso>>(){}.getType();
        Collection<Curso> cusos = g.fromJson(response, collectionType);


        HashMap<String, String> hm = new HashMap<String,String>();
        for(Curso c: cusos){
            hm.put(c.getNombre(), c.getDescripcion());
        }
    }
}