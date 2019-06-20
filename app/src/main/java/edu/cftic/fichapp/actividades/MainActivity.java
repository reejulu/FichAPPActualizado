package edu.cftic.fichapp.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;

import java.util.ArrayList;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.bean.Empresa;
import edu.cftic.fichapp.persistencia.DB;
import edu.cftic.fichapp.util.Constantes;
import edu.cftic.fichapp.util.Preferencias;
import edu.cftic.fichapp.util.Utilidades;

public class MainActivity extends AppCompatActivity {

    private CheckBox checkBox;



    private void lanzarActividad(Class actividad_destino) {
        Intent i = new Intent(this, actividad_destino);
        startActivity(i);
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Preferencias.terminosAceptados(this)) {
            lanzarActividad(AvisosLegalesActivity.class);

        } else //no es la primera, vemos ayuda
            {
                setContentView(R.layout.activity_ayuda);
                checkBox = findViewById(R.id.no_mostrar);
            if (!Preferencias.isCheck(this, checkBox)) {
                lanzarActividad(AyudaActivity.class);

            } else { //ayuda desactivada

                if (!Utilidades.hayEmpresa()) {

                    lanzarActividad(RegistroEmpresaActivity.class);
                } else { //hay empresa
                    if (!Utilidades.hayGestor())
                    {//no hay gestor
                        lanzarActividad(RegistroEmpleadoActivity.class);
                    } else //hay empresa y gestor
                        {
                            lanzarActividad(LoginActivity.class);
                        }
                }
            }

        }

        // Comprobar si hay un empleado que sea el Responsable
        //Empleado empleadoControlador = new Empleado(this);


        //ejemplo de uso de la base de datos
        /*Empresa em = new Empresa("B123456","XYZYZ SA","T T","xyz@xyz.com");
        boolean v = DB.empresas.nuevo(em);
        Empleado nu = DB.empleados.getEmpleadoUsuarioClave("","");
        ArrayList<Empresa> ae = (ArrayList<Empresa>) DB.empresas.getEmpresas();
        em = DB.empresas.ultimo();

        Log.i(Constantes.TAG_APP, "u: "+ em);

        Empleado tr = new Empleado("JUAN YONG 2","JYON3","12345","B", false, em);
        boolean t = DB.empleados.nuevo(tr);
        ArrayList<Empleado> at = (ArrayList<Empleado>) DB.empleados.getEmpleados();

        tr = DB.empleados.ultimo();
        Log.i(Constantes.TAG_APP, "E: "+tr);
        for(Empleado es : at){
            Log.i(Constantes.TAG_APP, "= "+es);
        }
        at = (ArrayList<Empleado>) DB.empleados.getEmpleados();

        Timestamp de = new Timestamp(new Date().getTime());
        Timestamp hasta = new Timestamp(new Date().getTime());

        Fichaje fe = new Fichaje(tr, de, hasta, "Mensaje");
        Log.i(Constantes.TAG_APP, "F: "+fe);
        boolean d = DB.fichar.nuevo(fe);
        ArrayList<Fichaje> af = (ArrayList<Fichaje>) DB.fichar.getFichaje(tr.getId_empleado());

        for(Fichaje es : af){
            Log.i(Constantes.TAG_APP, "= "+es);
        }

        ArrayList<String> rol = (ArrayList<String>) DB.empleados.getRoles();
        for(String es : rol){
            Log.i(Constantes.TAG_APP, "R:: "+es);
        }

        Fichaje ul = DB.fichar.getFichajeUltimo(tr.getId_empleado());
        Log.i(Constantes.TAG_APP, ""+ul.toString());

        ArrayList<Fichaje> fee = (ArrayList<Fichaje>) DB.fichar.getFichaje(1, de, hasta);
        for(Fichaje es : fee){
            Log.i("APPK", "Fichaje :: "+es);
        }



        */
    }
}
