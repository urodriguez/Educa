package com.example.uciel.educa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private List<Curso> cursos;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
    }

    private void initializeData(){
        cursos = new ArrayList<>();
        cursos.add(new Curso("Angular", "Jose", R.drawable.angular));
        cursos.add(new Curso("JavaScript", "Carlos", R.drawable.cursojs));
        cursos.add(new Curso("Matematica", "Juan", R.drawable.matematica));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(cursos, "HORIZONTAL");
        rv.setAdapter(adapter);
    }
}
