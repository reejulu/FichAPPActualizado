package edu.cftic.fichapp.servicios;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ServicioReceiver extends BroadcastReceiver {
    String mLastError = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        mLastError = intent.getStringExtra("CODIGO");

       // mLastError = intent.getStringExtra("CODIGO");
        if (mLastError.contains("NO ENVIAR NOTIFICACION")){
            // no se envia nada pues el proceso viene de loginActivity
            //            Toast.makeText(this, "LA BASE DE DATOS A SIDO COMPARTIDA CON : " + "reejulu1@gmail.com", Toast.LENGTH_SHORT).show();
            Toast.makeText(context,"LA BASE DE DATOS HA SIDO COMPARTIDA CON EL CORREO REEJULU1@GMAIL.COM",Toast.LENGTH_SHORT).show();
            Log.i("FichApp", "se ha enviado la base de datos al correo elegido");
        }else {
            Log.i("FichApp", "El servicio ha finalizado, lanzo una notificación");
            NotificaMensaje.lanzarNotificiacion(context, mLastError);
            // si quiero volver a gestionar la alarma
            //     GestorAlarma gestorAlarma = new GestorAlarma(context);
            //     gestorAlarma.programarAlarma();
        }
    }
}
