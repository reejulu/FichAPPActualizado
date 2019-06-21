package edu.cftic.fichapp.servicios;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.pdf.ViewPDFActivity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static edu.cftic.fichapp.pdf.TemplatePdf.RUTA_INFORME;

public class NotificaMensaje {

    //El id
    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";

    //El nombre visible para el usuario en Ajustes
    public static final String CHANNEL_NAME = "Notification Channel";

    private static NotificationChannel crearCanalNotificacion() {
        NotificationChannel notificationChannel = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.GREEN);

            //La duración del patrón de vibración {silencio,vibración,silencio,vibración,..}
            notificationChannel.setVibrationPattern(new long[]{
                    500,
                    500,
                    500,
                    500,
                    500
            });

            //Indicamos si la notificación será visible estando la pantalla bloqueada o no
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        }


        return notificationChannel;
    }

    public static void lanzarNotificiacion(Context context, String mlastError) {

        Log.i("MIAPP", "Lanzando notificación desde FICHAAppMiercoles");
        NotificationCompat.Builder nb = null;
        Log.i("MIAPP", "NotificaMensaje-LanzarNotificacion ha recibido mlastError = " + mlastError);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel nc = crearCanalNotificacion();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(nc);
            nb = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        } else {

            nb = new NotificationCompat.Builder(context, null);

        }
        nb.setSmallIcon(R.mipmap.ic_launcher);
        nb.setContentTitle("INFORME ENVIADO");
        if (mlastError.contains("Error")) {
            nb.setContentTitle("ERROR EN EL ENVIO DEL INFORME");
            //TODO mejora: reprogramar automáticamente la alarma en caso de fallo

        }

        nb.setAutoCancel(true);
        nb.setDefaults(Notification.DEFAULT_ALL);

        if (mlastError.contains("Error")){
            // Solo aparece la notificacion del error en el mensaje
        }else {
            // AL PRESIONAR SOBRE EL AVISO DE LA NOTIFICACION SE ABRIRA EL PDF
            Intent resultIntent = new Intent(context, ViewPDFActivity.class);
            resultIntent.putExtra("path", RUTA_INFORME);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), resultIntent, PendingIntent.FLAG_ONE_SHOT);

            nb.setContentIntent(resultPendingIntent);

        }

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(555,nb.build());

        //haya ido bien o mal, hay qeue reprogramar la alarma
        GestorAlarma gestorAlarma = new GestorAlarma(context);
        gestorAlarma.programarAlarma();
    }

}

