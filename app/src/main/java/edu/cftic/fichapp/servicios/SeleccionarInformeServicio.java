package edu.cftic.fichapp.servicios;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import edu.cftic.fichapp.actividades.EnviarMailActivity;
import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.persistencia.DB;

// ESTE SERVICIO NO ESTA TRABAJANDO AUN , ESTA A MEDIAS Y  PRETENDE SUSTITUIR
// A LA ACTIVIDAD SELECCIONARINFORME PERO COMO SERVICIO
//TODO como convertir una base de datos sqlite en un csv para posteriormente compartirlo
// https://stackoverflow.com/questions/52358065/how-to-convert-sqlite-database-in-android-studio-and-then-share-it-as-csv-file
// lo he puesto en este Servicio porque actualmente no se usa en esta APP esta en construccion :)

// otra manera
// https://www.javahelps.com/2015/04/import-and-use-external-database-in.html
// y otra
// https://www.techrepublic.com/blog/software-engineer/export-sqlite-data-from-your-android-device/

public class SeleccionarInformeServicio   extends Service {

    public SeleccionarInformeServicio() {

    }

    private ArrayList<Empleado> datos;
    private DB bdd;

    Boolean pdfcreado;
    Boolean crearpdf;
    Boolean existenEmpleados;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //

        datos = new ArrayList<Empleado>();
        datos = (ArrayList<Empleado>) DB.empleados.getEmpleados();
        if (DB.empleados.getEmpleados().size() > 0) {
            existenEmpleados = true;
        } else {
            existenEmpleados = false;
        }

        // COMPROBACION SI HAY ALGUN EMPLEADO DEFINIDO
        if (existenEmpleados == false) {
            // COMO NO HAY NINGUNO DEFINIDO VUELVO A DEFINIR EL GESTOR DE ALARMA
            GestorAlarma gestorAlarma = new GestorAlarma(getBaseContext());
            gestorAlarma.programarAlarma();
        }


        // LA PRIMERA VEZ: QUE SE LLAMA A ESTE METODO EL FICHERO NO ESTA CREADO POR TANTO SE VA
        // POR LA OPCTION - ELSE (PARA CREAR EL PDF)
        //
        // LA SEGUNDA VEZ: EL PDF YA DEBE ESTAR CREADO Y HA DE ENVIAR EL E-MAIL CON EL PDF
        if (intent.getBooleanExtra("YACREADO", false)) {
            // YA ESTA CREADO AHORA LANZAR EN ENVIO DEL E-MAIL CON EL FICHERO DEL INFORME PDF

            // PREVIAMENTE HEMOS COMPROBADO QUE HAY AL MENOS UN EMPLEADO DEFINIDO-
            // SELECCIONO POR EJEMPLO EL PRIMER EMPLEADO Y OBTENGO EL EMAIL DE SU EMPRESA
            // PARA USARLO COMO CORREO DE DESTINO
            // TODO hay que obener la empresa de este empleado y luego el e-mail de esta empresa

            //int posicion = getIntent().getIntExtra("identidadEmpleado", 25);
            // Empleado nu = DB.empleados.getEmpleadoId(posicion);
            Empleado nu = DB.empleados.primero();

            String email = nu.getEmpresa().getEmail();
            Intent intent1 = new Intent(this, EnviarMailActivity.class);
            intent.putExtra("email", email);
            startActivity(intent1);

        } else {

            // CREAR FICHERO MENSUAL
            crearpdf = true;
            Intent intent1 = new Intent(this, edu.cftic.fichapp.pdf.CreatePdfActivity.class);
            intent.putExtra("CREARPDF", crearpdf);

            Empleado nu = DB.empleados.primero();
            intent.putExtra("empresa", new Gson().toJson(nu.getEmpresa()));
            startActivity(intent1);
            // comentario: al terminar la creaccion del pdf me tiene que devolver un dato extra
            // indicando que ya esta creado.
        }
        return Service.START_NOT_STICKY;
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
