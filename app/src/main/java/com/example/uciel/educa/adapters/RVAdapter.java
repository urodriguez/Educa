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
import com.example.uciel.educa.domain.SingletonUserLogin;
import com.example.uciel.educa.network.RQSingleton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CursoViewHolder> {

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

    private List<Curso> cursos;
    private final String userName;
    private final String userID;
    private String orientacion;
    private Context context;

    private static final String IMAGE_ROOT_URL =
            "http://educa-mnforlenza.rhcloud.com/api/";


    public RVAdapter(List<Curso> cursos, String orientacion, SingletonUserLogin userLoginData, Context context){
        this.cursos = cursos;
        this.orientacion = orientacion;
        this.userName = userLoginData.getUserName();
        this.userID = userLoginData.getUserID();
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
            String comienzo = "Comienza: "  + cursos.get(i).getFechaSesionMasCercana();
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
                intentDescripcionCurso.putExtra("ID", curso.getId());

                if(orientacion.equals("HORIZONTAL")){
                    intentDescripcionCurso.putExtra("ES_DE_ULT_CURSOS", true);
                } else { //orientacion vertical => se esta listando por categoria
                    intentDescripcionCurso.putExtra("ES_DE_ULT_CURSOS", false);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return cursos.size();
    }
}
