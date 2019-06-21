package edu.cftic.fichapp.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;

import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.persistencia.DB;
import edu.cftic.fichapp.servicios.GestorAlarma;

public class SeleccionarInforme extends AppCompatActivity {

    private ArrayList<Empleado> datos;
    private DB bdd;

    Boolean pdfcreado;
    Boolean crearpdf;
    Boolean existenEmpleados;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // CREO LISTA DE EMPLEADOS
        datos = new ArrayList<Empleado>();
        datos = (ArrayList<Empleado>) DB.empleados.getEmpleados();
        if (DB.empleados.getEmpleados().size()>0){
            existenEmpleados = true;
        }else {
            existenEmpleados = false;
        }

        // COMPROBACION SI HAY ALGUN EMPLEADO DEFINIDO
        if (existenEmpleados == false){
            // COMO NO HAY NINGUNO DEFINIDO VUELVO A DEFINIR EL GESTOR DE ALARMA
                 GestorAlarma gestorAlarma = new GestorAlarma(getBaseContext());
                 gestorAlarma.programarAlarma();
                 finish();
        }


        // LA PRIMERA VEZ: QUE SE LLAMA A ESTE METODO EL FICHERO NO ESTA CREADO POR TANTO SE VA
        // POR LA OPCTION - ELSE (PARA CREAR EL PDF)
        //
        // LA SEGUNDA VEZ: EL PDF YA DEBE ESTAR CREADO Y HA DE ENVIAR EL E-MAIL CON EL PDF
        if (getIntent().getBooleanExtra("YACREADO",false)) {
            // YA ESTA CREADO AHORA LANZAR EN ENVIO DEL E-MAIL CON EL FICHERO DEL INFORME PDF

            // PREVIAMENTE HEMOS COMPROBADO QUE HAY AL MENOS UN EMPLEADO DEFINIDO-
            // SELECCIONO POR EJEMPLO EL PRIMER EMPLEADO Y OBTENGO EL EMAIL DE SU EMPRESA
            // PARA USARLO COMO CORREO DE DESTINO
            // TODO hay que obener la empresa de este empleado y luego el e-mail de esta empresa

            //int posicion = getIntent().getIntExtra("identidadEmpleado", 25);
           // Empleado nu = DB.empleados.getEmpleadoId(posicion);
            Empleado nu = DB.empleados.primero();

            String email = nu.getEmpresa().getEmail();
            Intent intent = new Intent(this, EnviarMailActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        }else {

            // CREAR FICHERO MENSUAL
            crearpdf = true;
            Intent intent = new Intent(this, edu.cftic.fichapp.pdf.CreatePdfActivity.class);
            intent.putExtra("CREARPDF", crearpdf);

            Empleado nu = DB.empleados.primero();
            String me = new Gson().toJson(nu.getEmpresa());
            intent.putExtra("empresa", new Gson().toJson(nu.getEmpresa()));
            startActivity(intent);
            finish();
            // comentario: al terminar la creaccion del pdf me tiene que devolver un dato extra
            // indicando que ya esta creado.
        }
    }

}
