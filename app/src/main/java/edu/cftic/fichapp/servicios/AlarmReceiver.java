package edu.cftic.fichapp.servicios;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        try {

            Log.i("MIAPP", "Alarma ejecutándose");


            Intent intent_serv = new Intent(context, ServicioEnvio.class);
            context.startService(intent_serv);
            Log.i("MIAPP", "Lanzando el servicio");

        } catch (Throwable t) {
            Log.e("MIAPP", "ERRORAZO", t);
        }
    }
}

