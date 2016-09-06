package com.example.uciel.educa;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CursoViewHolder> {

    private String orientacion;

    public static class CursoViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView nombreCurso;
        TextView profesorCurso;
        ImageView fotoCurso;

        CursoViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            nombreCurso = (TextView)itemView.findViewById(R.id.curso_name);
            profesorCurso = (TextView)itemView.findViewById(R.id.curso_profesor);
            fotoCurso = (ImageView)itemView.findViewById(R.id.curso_photo);
        }
    }

    List<Curso> cursos;

    RVAdapter(List<Curso> cursos, String orientacion ){
        this.cursos = cursos;
        this.orientacion = orientacion;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CursoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        if (orientacion.equals("HORIZONTAL")){
             v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        } else {
            //orientacion == "VERTICAL"
             v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_for_cat, viewGroup, false);
        }
        CursoViewHolder cvh = new CursoViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CursoViewHolder cursoViewHolder, final int i) {
        cursoViewHolder.nombreCurso.setText(cursos.get(i).name);
        cursoViewHolder.profesorCurso.setText(cursos.get(i).profesor);
        cursoViewHolder.fotoCurso.setImageResource(cursos.get(i).photoId);
        cursoViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implementing onClick
                Intent intentDescripcionCurso = new Intent();
                intentDescripcionCurso.setClass(v.getContext(), DescripcionCurso.class);
                v.getContext().startActivity(intentDescripcionCurso);
                System.out.println("Clicked " + String.valueOf(i));
            }
        });
    }


    @Override
    public int getItemCount() {
        return cursos.size();
    }
}
