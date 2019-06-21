package edu.cftic.fichapp.servicios;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GestorAlarma {
    private Context context;
    boolean prueba = true;
    long tiempo;

    public GestorAlarma(Context context) {
        this.context = context;
    }


    private long msHastaFinDeMes () {
        long tiempo = 0;

            Calendar calendar_actual = Calendar.getInstance();


            // fecha de final del presente mes
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
            cal.set(Calendar.HOUR, cal.getActualMaximum(Calendar.HOUR));
            Date lastDayOfMonth = cal.getTime();
            Log.i("MIAPP", "El e-mail se enviara el " + lastDayOfMonth.toString());

            long tiempoActual = calendar_actual.getTimeInMillis();
            long tiempoAlarma = cal.getTimeInMillis();
            long tiemporestante = tiempoAlarma - tiempoActual;
            long minutos = tiemporestante / (60000);
            long horas = tiemporestante / (60000 * 60);
            Log.i("MIAPP", "tiempo restante en minutos es : " + minutos);
            Log.i("MIAPP", "tiempo restante en horas es : " + horas);
            long dias = horas / 24;
            long horasrestantes = horas - (dias * 24);

            long tiempoenminutos = minutos - (dias * 24 * 60) - (horasrestantes * 60);

            Log.i("MIAPP", "faltan " + dias + " dias  y " + horasrestantes + " horas y " + tiempoenminutos + " minutos");

            tiempo = tiempoAlarma;


        return tiempo;
    }

    private void informarToastAlarma (long tiempo)
    {
        //mensaje informativo
        SimpleDateFormat dateformatter = new SimpleDateFormat("E dd/MM/yyyy 'a las' hh:mm:ss");
        String mensaje = "Alarma programada para " + dateformatter.format(tiempo);
        Log.i("MIAPP", mensaje);


        Toast.makeText(this.context, mensaje, Toast.LENGTH_LONG).show();
    }

    public void programarAlarma() {

        //tiempo = msHastaFinDeMes();//tiempoAlarma;
        Calendar calendar_actual = Calendar.getInstance();
        tiempo = calendar_actual.getTimeInMillis() + 10000; //en 10 ss saltará la alarma

        Intent intentAlarm = new Intent(this.context, edu.cftic.fichapp.servicios.AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(this.context, 55, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, tiempo, pi);//TIempo, No es el tiempo que falta. Es el tiempo expresado en milisegundos equivalente a la fecha y hora del omento en que se quiere ejecutar

        informarToastAlarma(tiempo);


    }
}

