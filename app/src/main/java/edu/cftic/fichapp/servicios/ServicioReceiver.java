package edu.cftic.fichapp.servicios;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.persistencia.DB;

public class ServicioReceiver extends BroadcastReceiver {
    String mLastError = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        mLastError = intent.getStringExtra("CODIGO");
        Empleado nu = DB.empleados.primero();
        // SE USARA EL CORREO DEFINIDO EN LOS DATOS DE LA EMPRESA
        String email = nu.getEmpresa().getEmail();
        if (mLastError.contains("NO ENVIAR NOTIFICACION")) {
            // no se envia nada pues el proceso viene de loginActivity
            //            Toast.makeText(this, "LA BASE DE DATOS A SIDO COMPARTIDA CON : " + "reejulu1@gmail.com", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "LA BASE DE DATOS HA SIDO COMPARTIDA CON EL CORREO : " +email, Toast.LENGTH_SHORT).show();
            Log.i("FichApp", "se ha enviado la base de datos al correo elegido");
        } else {
            Log.i("FichApp", "El servicio ha finalizado, lanzo una notificación");
            NotificaMensaje.lanzarNotificiacion(context, mLastError);
        }
    }
}
