package edu.cftic.fichapp.actividades;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import edu.cftic.fichapp.R;
import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.bean.Empresa;
import edu.cftic.fichapp.persistencia.DB;
import edu.cftic.fichapp.persistencia.DataBaseHelper;
import edu.cftic.fichapp.servicios.GestorAlarma;
import edu.cftic.fichapp.util.Constantes;
import edu.cftic.fichapp.util.Preferencias;
import edu.cftic.fichapp.util.Utilidades;
import static edu.cftic.fichapp.persistencia.DataBaseHelper.DATABASE_NAME;

public class MainActivity extends AppCompatActivity {

    private CheckBox checkBox;
    GestorAlarma gestorAlarma;


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
            // GESTIONAMOS EL ENVIO DE LA ALARMA
            // con fecha ultimo dia de mes a las 23:59:45 se
            // gestionara la creaccion del informe pdf del mes en curso y su
            // posterior envio al correo de la empresa que se cree.
            // solo se enviara la primera vez que se ejecute la APP
            gestorAlarma = new GestorAlarma(this);
            gestorAlarma.programarAlarma();

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
                    if (!Utilidades.hayGestor()) {//no hay gestor
                        lanzarActividad(RegistroEmpleadoActivity.class);
                    } else //hay empresa y gestor
                    {
                        lanzarActividad(LoginActivity.class);
                    }
                }
            }
        }
     }
}
