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
import com.example.uciel.educa.R;
import com.example.uciel.educa.activities.ContenidoCurso;
import com.example.uciel.educa.domain.Curso;
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
    private final String userName;
    private final String userID;
    private Context context;

    private static final String IMAGE_ROOT_URL =
            "http://educa-mnforlenza.rhcloud.com/api/";


    public RVAdapterMisCursos(List<Curso> cursos, SingletonUserLogin userLoginData, Context context){
        this.cursos = cursos;
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
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_for_cat, viewGroup, false);
        CursoViewHolder cvh = new CursoViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CursoViewHolder cursoViewHolder, final int i) {
        cursoViewHolder.nombreCurso.setText(cursos.get(i).getNombre());
        cursoViewHolder.ratingBar.setRating(cursos.get(i).getValoracionesPromedio());

        cursoViewHolder.profesorCurso.setText(cursos.get(i).getNombreCompletoDocente());

        cursoViewHolder.estado.setText(cursos.get(i).getEstado());

        String imageUrl = IMAGE_ROOT_URL + cursos.get(i).getLinkImagen();
        ImageLoader mImageLoader;
        // Get the NetworkImageView that will display the image.
        mImageLoader = RQSingleton.getInstance(this.context).getImageLoader();
        cursoViewHolder.fotoCursoNiv.setImageUrl(imageUrl, mImageLoader);

        cursoViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implementing onClick
                Intent intentContenidoCurso = new Intent();
                intentContenidoCurso.setClass(v.getContext(), ContenidoCurso.class);

                this.cargarInformacion(intentContenidoCurso,cursos.get(i));

                v.getContext().startActivity(intentContenidoCurso);
                System.out.println("Clicked " + String.valueOf(i));
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
