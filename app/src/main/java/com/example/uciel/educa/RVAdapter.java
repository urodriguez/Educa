package com.example.uciel.educa;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CursoViewHolder> {

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

    RVAdapter(List<Curso> cursos){
        this.cursos = cursos;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CursoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        CursoViewHolder cvh = new CursoViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CursoViewHolder personViewHolder, int i) {
        personViewHolder.nombreCurso.setText(cursos.get(i).name);
        personViewHolder.profesorCurso.setText(cursos.get(i).profesor);
        personViewHolder.fotoCurso.setImageResource(cursos.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return cursos.size();
    }
}
