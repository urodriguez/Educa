package com.example.uciel.educa;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.uciel.educa.domain.Curso;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CursoViewHolder> {

    private final String userName;
    private String orientacion;

    public static class CursoViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView nombreCurso;
        TextView profesorCurso;
        ImageView fotoCurso;
        RatingBar ratingBar;

        CursoViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            nombreCurso = (TextView)itemView.findViewById(R.id.curso_name);
            profesorCurso = (TextView)itemView.findViewById(R.id.curso_profesor);
            fotoCurso = (ImageView)itemView.findViewById(R.id.curso_photo);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
        }
    }

    List<Curso> cursos;

    RVAdapter(List<Curso> cursos, String orientacion, String userName){
        this.cursos = cursos;
        this.orientacion = orientacion;
        this.userName = userName;
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
        cursoViewHolder.nombreCurso.setText(cursos.get(i).getNombre());
        //cursoViewHolder.profesorCurso.setText(cursos.get(i).getNombreCompletoDocente());
        cursoViewHolder.fotoCurso.setImageResource(R.drawable.angular);
        cursoViewHolder.ratingBar.setRating((float)cursos.get(i).getValoracionesPromedio());
        cursoViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implementing onClick
                Intent intentDescripcionCurso = new Intent();
                intentDescripcionCurso.setClass(v.getContext(), DescripcionCurso.class);
                intentDescripcionCurso.putExtra("USER", userName);
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
