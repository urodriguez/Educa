package com.example.uciel.educa.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.uciel.educa.R;
import com.example.uciel.educa.activities.ContenidoCurso;
import com.example.uciel.educa.activities.MiDiploma;
import com.example.uciel.educa.domain.Curso;
import com.example.uciel.educa.domain.DataMisDiplomas;
import com.example.uciel.educa.domain.SingletonUserLogin;
import com.example.uciel.educa.network.RQSingleton;

import java.util.List;

public class RVAdapterMisDiplomas extends RecyclerView.Adapter<RVAdapterMisDiplomas.DiplomaViewHolder> {

    public static class DiplomaViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView nombreCurso;
        TextView fecha;
        ImageView imagenDiploma;

        DiplomaViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            nombreCurso = (TextView)itemView.findViewById(R.id.curso_name);
            fecha = (TextView)itemView.findViewById(R.id.fecha);
            imagenDiploma = (ImageView)itemView.findViewById(R.id.imagen_diploma);
        }
    }

    private List<DataMisDiplomas> datosDeCampos;

    public RVAdapterMisDiplomas(List<DataMisDiplomas> datosDeCampos){
        this.datosDeCampos = datosDeCampos;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public DiplomaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_for_diplomas, viewGroup, false);
        DiplomaViewHolder dvh = new DiplomaViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(DiplomaViewHolder diplomaViewHolder, final int i) {
        diplomaViewHolder.nombreCurso.setText(datosDeCampos.get(i).nombreCurso);
        diplomaViewHolder.fecha.setText(datosDeCampos.get(i).fechaInicio);

        diplomaViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implementing onClick
                Intent intentContenidoCurso = new Intent();
                intentContenidoCurso.setClass(v.getContext(), MiDiploma.class);

                this.cargarInformacion(intentContenidoCurso,datosDeCampos.get(i));

                v.getContext().startActivity(intentContenidoCurso);
                System.out.println("Clicked " + String.valueOf(i));
            }

            private void cargarInformacion(Intent intentDescripcionCurso, DataMisDiplomas data) {
                android.util.Log.d("MSG", "INFO DIPLOMAS= " +  data.nombreCurso + " * " + data.fechaInicio);
                intentDescripcionCurso.putExtra("NOMBRE_CURSO", data.nombreCurso);
                intentDescripcionCurso.putExtra("FECHA_INICIO", data.fechaInicio);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datosDeCampos.size();
    }
}
