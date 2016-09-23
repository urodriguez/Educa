package com.example.uciel.educa.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.uciel.educa.activities.DescripcionCurso;
import com.example.uciel.educa.R;
import com.example.uciel.educa.domain.Curso;
import com.example.uciel.educa.network.RQSingleton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CursoViewHolder> {

    private final String userName;
    private String orientacion;
    private Context context;

    private static final String IMAGE_ROOT_URL =
                    "http://educa-mnforlenza.rhcloud.com/api/";

    public static class CursoViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView nombreCurso;
        TextView profesorCurso;
        TextView fechaComienzo;
        NetworkImageView fotoCursoNiv;
        RatingBar ratingBar;

        CursoViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            nombreCurso = (TextView)itemView.findViewById(R.id.curso_name);
            profesorCurso = (TextView)itemView.findViewById(R.id.curso_profesor);
            fotoCursoNiv = (NetworkImageView)itemView.findViewById(R.id.curso_photo_niv);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
            fechaComienzo = (TextView)itemView.findViewById(R.id.fecha_comienzo);
        }
    }

    List<Curso> cursos;

    public RVAdapter(List<Curso> cursos, String orientacion, String userName, Context context){
        this.cursos = cursos;
        this.orientacion = orientacion;
        this.userName = userName;
        this.context = context;
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
        cursoViewHolder.ratingBar.setRating(cursos.get(i).getValoracionesPromedio());

        if (orientacion.equals("HORIZONTAL")){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(cursos.get(i).getFechaEstimadaProximaSesion());
            Date fechaComienzo = calendar.getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String cadenaFechaComienzo = df.format(fechaComienzo);

            String comienzo = "Comienza: "  + cadenaFechaComienzo;
            cursoViewHolder.fechaComienzo.setText(comienzo);

        } else {
            //orientacion == "VERTICAL"
            cursoViewHolder.profesorCurso.setText(cursos.get(i).getNombreCompletoDocente());
        }

        String imageUrl = IMAGE_ROOT_URL + cursos.get(i).getLinkImagen();
        ImageLoader mImageLoader;
        // Get the NetworkImageView that will display the image.
        mImageLoader = RQSingleton.getInstance(this.context).getImageLoader();
        cursoViewHolder.fotoCursoNiv.setImageUrl(imageUrl, mImageLoader);

        cursoViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implementing onClick
                Intent intentDescripcionCurso = new Intent();
                intentDescripcionCurso.setClass(v.getContext(), DescripcionCurso.class);
                this.cargarInformacion(intentDescripcionCurso,cursos.get(i));

                v.getContext().startActivity(intentDescripcionCurso);
                System.out.println("Clicked " + String.valueOf(i));
            }

            private void cargarInformacion(Intent intentDescripcionCurso, Curso curso) {
                intentDescripcionCurso.putExtra("USER", userName);
                intentDescripcionCurso.putExtra("NOMBRE", curso.getNombre());
                intentDescripcionCurso.putExtra("ESTADO", curso.getEstado());
                intentDescripcionCurso.putExtra("PROFESOR", curso.getNombreCompletoDocente());
                intentDescripcionCurso.putExtra("DESCRIPCION", curso.getDescripcion());
                intentDescripcionCurso.putExtra("VALORACION", curso.getValoracionesPromedio());

                intentDescripcionCurso.putExtra("CANT_SESIONES", curso.getSesiones().size());
                for (int i = 0; i < curso.getSesiones().size(); i++){
                    intentDescripcionCurso.putExtra("SESION" + String.valueOf(i) + "FECHADESDE", curso.getSesiones().get(i).getFechaDesde());
                    intentDescripcionCurso.putExtra("SESION" + String.valueOf(i) + "FECHAHASTA", curso.getSesiones().get(i).getFechaHasta());
                    intentDescripcionCurso.putExtra("SESION" + String.valueOf(i) + "FECHADESDEINCRIP", curso.getSesiones().get(i).getFechaDesdeInscripcion());
                }

                intentDescripcionCurso.putExtra("CANT_UNIDADES", curso.getUnidades().size());
                for (int i = 0; i < curso.getUnidades().size(); i++){
                    intentDescripcionCurso.putExtra("UNIDAD" + String.valueOf(i) + "TITULO", curso.getUnidades().get(i).getTitulo());
                    intentDescripcionCurso.putExtra("UNIDAD" + String.valueOf(i) + "DESCRIPCION", curso.getUnidades().get(i).getDescripcion());
                    intentDescripcionCurso.putExtra("UNIDAD" + String.valueOf(i) + "DURACIONESTIMADA", curso.getUnidades().get(i).getDuracionEstimada());
                }

                intentDescripcionCurso.putExtra("CANT_CRITICAS", curso.getCriticas().size());
                for (int i = 0; i < curso.getCriticas().size(); i++){
                    intentDescripcionCurso.putExtra("CRITICA" + String.valueOf(i) + "FECHA", curso.getCriticas().get(i).getFecha());
                    intentDescripcionCurso.putExtra("CRITICA" + String.valueOf(i) + "CALIFICACION", curso.getCriticas().get(i).getCalificacion());
                    intentDescripcionCurso.putExtra("CRITICA" + String.valueOf(i) + "COMENTARIO", curso.getCriticas().get(i).getComentario());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return cursos.size();
    }
}
