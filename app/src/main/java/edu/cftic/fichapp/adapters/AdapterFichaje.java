package edu.cftic.fichapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.bean.Fichaje;

public class AdapterFichaje extends RecyclerView.Adapter<AdapterFichaje.ViewHolderFichaje> {

    Context contexto;
    private ArrayList<Fichaje> listaFichajes; // datos a visualizar
    private LayoutInflater inflador;
    // Timestamp a;
    // Timestamp b;


    public AdapterFichaje(Context contexto, ArrayList<Fichaje> listaFichajes) {
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // a = listaFichajes.get(0).getFechainicio();
        // b = listaFichajes.get(0).getFechafin();
        this.contexto = contexto;
        this.listaFichajes = listaFichajes;
    }

    public class ViewHolderFichaje extends RecyclerView.ViewHolder {

        TextView hora, mensaje, horab, mensajeb;
        ImageView iconoEntradaSalida, iconoEntradaSalidab;
        RelativeLayout contenedorItemFichaje, contenedorItemFichajeb;

        public ViewHolderFichaje(@NonNull View itemView) {
            super(itemView);
            contenedorItemFichaje = itemView.findViewById(R.id.itemFichaje);
            hora = itemView.findViewById(R.id.textoHora);
            mensaje = itemView.findViewById(R.id.textoMensaje);
            iconoEntradaSalida = itemView.findViewById(R.id.imgIO);
            contenedorItemFichajeb = itemView.findViewById(R.id.itemFichajeb);
            horab = itemView.findViewById(R.id.textoHorab);
            mensajeb = itemView.findViewById(R.id.textoMensajeb);
            iconoEntradaSalidab = itemView.findViewById(R.id.imgIOb);
        }
    }

    @NonNull
    @Override
    public ViewHolderFichaje onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_fichaje, null, false);
        return new ViewHolderFichaje(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFichaje holder, int posicion) {
        // Si es fichaje de entrada
        SimpleDateFormat sfd = new SimpleDateFormat("yyyyMMdd");
        Log.i("FichApp", "En AdapterFichaje -position es : " + posicion);
        Log.i("FichApp", "En AdapterFichaje es : " + listaFichajes.get(posicion).getFechainicio().toString());
        Log.i("FichApp", "En AdapterFichaje es : " + listaFichajes.get(posicion).getFechafin().toString());
        Log.i("FichApp", "valor de Tiemstamp(0) es : " + new Timestamp(0));

        Timestamp ts = listaFichajes.get(posicion).getFechainicio();
        Date fecha = new Date();
        fecha.setTime(ts.getTime());
        String fechaFormateada = new SimpleDateFormat("HH:mm").format(fecha);

        holder.contenedorItemFichaje.setAnimation(AnimationUtils.loadAnimation(contexto, R.anim.transicion01));
        holder.hora.setText(fechaFormateada);
        holder.mensaje.setText(listaFichajes.get(posicion).getMensaje());
        holder.iconoEntradaSalida.setImageResource(R.drawable.entrada);
        Log.i("FichApp", "escribo fecha entrada despue icono " + fechaFormateada);

        Timestamp ts1 = listaFichajes.get(posicion).getFechafin();

        Date fechab = new Date();
        fechab.setTime(ts1.getTime());
        // En caso de faltar la fichada de salida se detecta porque el array "listaFichajes" viene con la
        // fecha-año cargada con "1970". Asi pues en este caso escribiremos en pantalla "Pendiente de fichada"
        boolean fechavalidad = fechab.toString().contains("1970");
        if (fechavalidad == true) {
            holder.horab.setText("Pendiente de fichada");
        } else {
            String fechaFormateadab = new SimpleDateFormat("HH:mm").format(fechab);
            holder.horab.setText(fechaFormateadab);
            Log.i("FichApp", "escribo fecha entrada despue icono " + fechaFormateadab);
        }
        holder.contenedorItemFichajeb.setAnimation(AnimationUtils.loadAnimation(contexto, R.anim.transicion01));
        holder.mensajeb.setText(listaFichajes.get(posicion).getMensaje());
        holder.iconoEntradaSalidab.setImageResource(R.drawable.salida);
    }

    @Override
    public int getItemCount() {
        return listaFichajes.size();
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
