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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.uciel.educa.R;
import com.example.uciel.educa.activities.ContenidoCurso;
import com.example.uciel.educa.domain.Curso;
import com.example.uciel.educa.domain.SesionHandler;
import com.example.uciel.educa.domain.SingletonUserLogin;
import com.example.uciel.educa.network.RQSingleton;

import java.util.List;

public class RVAdapterMisCursos extends RecyclerView.Adapter<RVAdapterMisCursos.CursoViewHolder> {

    public static class CursoViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nombreCurso;
        TextView profesorCurso;
        TextView estado;
        NetworkImageView fotoCursoNiv;
        RatingBar ratingBar;

        CursoViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            nombreCurso = (TextView)itemView.findViewById(R.id.curso_name);
            profesorCurso = (TextView)itemView.findViewById(R.id.curso_profesor);
            fotoCursoNiv = (NetworkImageView)itemView.findViewById(R.id.curso_photo_niv);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
            estado = (TextView)itemView.findViewById(R.id.fecha_comienzo);
        }
    }

    private List<Curso> cursos;
    List<SesionHandler> sesionHandlers;
    private Context context;

    private static final String IMAGE_ROOT_URL =
            "http://educa-mnforlenza.rhcloud.com/api/";


    public RVAdapterMisCursos(List<Curso> cursos, List<SesionHandler> sesionHandlers, Context context){
        this.cursos = cursos;
        this.sesionHandlers = sesionHandlers;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CursoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_for_cat, viewGroup, false);
        CursoViewHolder cvh = new CursoViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CursoViewHolder cursoViewHolder, final int i) {
        cursoViewHolder.nombreCurso.setText(cursos.get(i).getNombre());

        cursoViewHolder.profesorCurso.setText(cursos.get(i).getNombreCompletoDocente());

        if(sesionHandlers.get(i).isIniciada()){
            cursoViewHolder.estado.setText("Dictandose");
        } else {
            cursoViewHolder.estado.setText("Comienza el: " + sesionHandlers.get(i).getStringFechaInicio());
        }

        cursoViewHolder.ratingBar.setRating(cursos.get(i).getValoracionesPromedio());

        String imageUrl = IMAGE_ROOT_URL + cursos.get(i).getLinkImagen();
        ImageLoader mImageLoader;
        // Get the NetworkImageView that will display the image.
        mImageLoader = RQSingleton.getInstance(this.context).getImageLoader();
        cursoViewHolder.fotoCursoNiv.setImageUrl(imageUrl, mImageLoader);

        if (cursos.get(i).tieneLaSesionActualDesaprobada()){
            cursoViewHolder.cv.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        }

        cursoViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cursos.get(i).tieneLaSesionActualDesaprobada()){
                    CharSequence text = "¡Solicitud Denegada! Tienes un examen desaprobado. " +
                            "Puedes volver a intentarlo cuando comience la proxima sesión";
                    Toast toast = Toast.makeText(v.getContext(), text, Toast.LENGTH_LONG);
                    toast.show();
                } else if (sesionHandlers.get(i).isIniciada()) {
                    //implementing onClick
                    Intent intentContenidoCurso = new Intent();
                    intentContenidoCurso.setClass(v.getContext(), ContenidoCurso.class);

                    this.cargarInformacion(intentContenidoCurso, cursos.get(i));

                    v.getContext().startActivity(intentContenidoCurso);
                    System.out.println("Clicked " + String.valueOf(i));
                } else {
                    CharSequence text = "¡Esta sesión aun no ha comenzado! Comienza el: " + sesionHandlers.get(i).getStringFechaInicio() ;
                    Toast toast = Toast.makeText(v.getContext(), text, Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            private void cargarInformacion(Intent intentDescripcionCurso, Curso curso) {
                android.util.Log.d("MSG", "ID_A= " +  curso.getId());
                intentDescripcionCurso.putExtra("ID", curso.getId());
                intentDescripcionCurso.putExtra("NOMBRE_CURSO", curso.getNombre());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursos.size();
    }
}
