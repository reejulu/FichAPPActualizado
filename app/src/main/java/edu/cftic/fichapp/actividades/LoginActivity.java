package edu.cftic.fichapp.actividades;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.bean.Empresa;
import edu.cftic.fichapp.persistencia.DB;
import edu.cftic.fichapp.util.Constantes;

public class LoginActivity extends AppCompatActivity  {

    EditText usuario;
    EditText contraseña;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (!hayEmpresa()) {

            lanzarActividad(RegistroEmpresaActivity.class);
        } else { //hay empresa
            if (!hayGestor())
            {//no hay gestor
                lanzarActividad(RegistroEmpleadoActivity.class);
            }  //hay empresa y gestor
            //sigo en el login
        }

        usuario = findViewById(R.id.usuario);
        contraseña = findViewById(R.id.contraseña);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //creamos este metodo para que el ActionBar(la flecha hacia atras) funcione bien
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * COTEJAR LOS DATOS INTRODUCIDOS CON LA BASE DE DATOS (USUARIO Y CONTRASEÑA)
     * @return
     */
    public void entrar(View view) {

        String nombre = usuario.getText().toString();
        String cont = contraseña.getText().toString();
        Empleado u = DB.empleados.getEmpleadoUsuarioClave(nombre,cont);
        //Valida
        //Validador validador
        if(u.getId_empleado() == 0){

            TextView incorrecto = findViewById(R.id.incorrecto);

            limpiarText(usuario,contraseña);

            incorrecto.setText(R.string.error_login);

        }

        Intent intent = null;
           String rol = u.getRol();
        if (u.getRol().equals(Constantes.ROL_EMPLEADO) ){

             intent = new Intent(this,MenuEmpleadoActivity.class);
             intent.putExtra("ID_EMPLEADO",u.getId_empleado());
            intent.putExtra("EMPLEADO",u);



        }else if(u.getRol().equals(Constantes.ROL_GESTOR) ){

             intent = new Intent(this,MenuGestorActivity.class);
             intent.putExtra("ID_EMPLEADO",u.getId_empleado());
            intent.putExtra("EMPLEADO",u);


        }

        if( null != intent) {

            startActivity( intent);
        }
    }


    public void limpiarText(EditText ... array ) {//VARARGS

        for (EditText e : array) {

            e.setText("");
            e.clearFocus();

        }
    }

    public void creditos(View view) {

        Intent intent = new Intent(this, Creditos.class);
        startActivity(intent);
        finish();

    }

    private Boolean hayGestor() {
       ArrayList<Empleado> empleado = null;

        empleado = (ArrayList<Empleado>) DB.empleados.getRol(Constantes.ROL_GESTOR);


        return empleado.size()>0;
    }

    private boolean hayEmpresa() {
        boolean b = false;

        Empresa empresa = DB.empresas.primero();
        if (empresa != null) {
            b = true;
        }

        return b;
    }

    private void lanzarActividad(Class actividad_destino) {
        Intent i = new Intent(this, actividad_destino);
        startActivity(i);
        finish();
    }

}

