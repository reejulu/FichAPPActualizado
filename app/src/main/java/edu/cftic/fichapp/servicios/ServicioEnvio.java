package edu.cftic.fichapp.servicios;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ServicioEnvio extends Service {
    public ServicioEnvio() {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //

        //
        try {
            //TODO 1 GENERAR INFORME 2 ENVIARLO


            Log.i("MIAPP", "Servicio iniciado!...enviando el correo");
            //TODO enviaríamos el correo

            Intent intent1 = new Intent(this,edu.cftic.fichapp.actividades.SeleccionarInforme.class);
            startActivity(intent1);
            //String email = "reejulu1@gmail.com";

            //Intent intent1 = new Intent(this, EnviarMailActivity.class);
            //intent1.putExtra("email", email);
            //startActivity(intent1);
            stopSelf(startId);  // y se ejecuta el metodo onDestroy

        } catch (Throwable t) {
            Log.e("MIAPP", "ERRORAZO", t);
        }

        return Service.START_STICKY; // en caso del que el servicio sea eliminado por algun
        // motivo se volveria a reiniciar este paso...desde onStartCommand
        // si pusieramos START_NOT_STICKY el servicio no se volveria a reiniciar.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        Log.i("MIAPP", "Servicio Terminado");

        // en el manifest esta definico el receiver del servicio
        //            android:name=".ServicioReceiver"

      //  Intent intent_reciver = new Intent("SERVICIO_TERMINADO");
      //  Exception mLastError = null;
      //  intent_reciver.putExtra("CODIGO", mLastError);
      //  sendBroadcast(intent_reciver);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}

