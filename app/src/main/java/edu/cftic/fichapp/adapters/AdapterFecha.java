package edu.cftic.fichapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.bean.Fichaje;


public class AdapterFecha extends RecyclerView.Adapter <AdapterFecha.ViewHolderFecha> {


    private Map<String, ArrayList<Fichaje>> porDia; // datos a visualizar
    private LayoutInflater inflador;

    private Activity activity;


    public AdapterFecha(Context contexto, Map<String, ArrayList<Fichaje>> listaFichajes, Activity activity) {
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.porDia = listaFichajes;
        this.activity = activity;
    }

    public class ViewHolderFecha extends RecyclerView.ViewHolder {

        TextView mensaje;
        RecyclerView rViewHijo;

        public ViewHolderFecha(@NonNull View itemView) {
            super(itemView);

            mensaje = itemView.findViewById(R.id.textoFechaDia);
          rViewHijo = itemView.findViewById(R.id.recyclerHijoId);

        }
    }

    @NonNull
    @Override
    public ViewHolderFecha onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_fechas,null,false);

        return new ViewHolderFecha(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderFecha holder, int posicion) {

        SimpleDateFormat sf = new SimpleDateFormat("d MMM yyyy, EEEE");
        SimpleDateFormat sfd =  new SimpleDateFormat("yyyyMMdd");

        Collection<String> fechaLarga = porDia.keySet() ;
        Object[] dd = fechaLarga.toArray();

        try {
            holder.mensaje.setText(sf.format(sfd.parse((String)dd[posicion])));


            ArrayList <Fichaje> fichajeDelDia = porDia.get((String)dd[posicion]);


            LinearLayoutManager hs_linearLayout = new LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false);
            holder.rViewHijo.setLayoutManager(hs_linearLayout);

            holder.rViewHijo.setVisibility(View.VISIBLE);

           holder.rViewHijo.setHasFixedSize(true);
            AdapterFichaje hijoAdapter = new AdapterFichaje(this.activity,fichajeDelDia);
            holder.rViewHijo.setAdapter(hijoAdapter);

        } catch (ParseException e) {
            Log.e( "FichApp", "", e);
        }

    }

    @Override
    public int getItemCount() {
        return porDia.size();
    }





    //MÉTODO PARA CONVERTIR UN TIMESPAMP EN UN CHARSEQUENCE

  /*  public static CharSequence crearFecha (long timestamp) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(timestamp);

        Date d = calendario.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(d);
    }

    public static String getDateTimeFromTimeStamp(Long time, String mDateFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(mDateFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dateTime = new Date(time);
        return dateFormat.format(dateTime);
    }*/


}
