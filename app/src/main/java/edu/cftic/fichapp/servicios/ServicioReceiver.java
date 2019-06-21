package edu.cftic.fichapp.servicios;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServicioReceiver extends BroadcastReceiver {
    String mLastError = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        mLastError = intent.getStringExtra("CODIGO");

       // mLastError = intent.getStringExtra("CODIGO");

        Log.i("MIAPP", "El servicio ha finalizado, lanzo una notificación");
        NotificaMensaje.lanzarNotificiacion(context,mLastError);
        // si quiero volver a gestionar la alarma
        //     GestorAlarma gestorAlarma = new GestorAlarma(context);
        //     gestorAlarma.programarAlarma();

    }
}
