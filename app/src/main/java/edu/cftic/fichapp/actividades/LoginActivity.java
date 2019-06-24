package edu.cftic.fichapp.actividades;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.bean.Empresa;
import edu.cftic.fichapp.persistencia.DB;
import edu.cftic.fichapp.util.Constantes;
import edu.cftic.fichapp.util.Utilidades;

public class LoginActivity extends AppCompatActivity  {

    EditText usuario;
    EditText contraseña;
    private Spinner empleadoNombreSpinner;
    private ArrayList<Empleado> listaEmpleados;
    private Empleado u = null;
    String nombre;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        empleadoNombreSpinner = findViewById(R.id.usuario);

        if (!Utilidades.hayEmpresa()) {

            lanzarActividad(RegistroEmpresaActivity.class);
        } else { //hay empresa
            if (!Utilidades.hayGestor())
            {//no hay gestor
                lanzarActividad(RegistroEmpleadoActivity.class);
            }  //hay empresa y gestor
            //sigo en el login
        }

        //usuario = findViewById(R.id.usuario);

        listaEmpleados = (ArrayList<Empleado>) DB.empleados.getEmpleados();
        ArrayList<String> arrayEmpleados = new ArrayList<>();
        for (Empleado empleado : listaEmpleados) {
            arrayEmpleados.add(empleado.getUsuario()+" ---> " +empleado.getRol().toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayEmpleados);
        //specify the layout to appear list items
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //data bind adapter with both spinners
        empleadoNombreSpinner.setAdapter(adapter);
        empleadoNombreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                u = listaEmpleados.get(position);
                Log.d(Constantes.TAG_APP, "pos: " + listaEmpleados.get(position));
                nombre = u.getUsuario();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




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

        //nombre = usuario.getText().toString();
        String cont = contraseña.getText().toString();
        String error = "no";
        Empleado u = DB.empleados.getEmpleadoUsuarioClave(nombre,cont);
        //Valida
        //Validador validador
        if(u.getId_empleado() == 0){

            TextView incorrecto = findViewById(R.id.incorrecto);

            limpiarText(usuario,contraseña);

            incorrecto.setText(R.string.error_login);
            error = "yes";

        }
        if (error.contains("no")) {
            Intent intent = null;
            String rol = u.getRol();
            if (u.getRol().equals(Constantes.ROL_EMPLEADO)) {

                intent = new Intent(this, MenuEmpleadoActivity.class);
                //intent.putExtra("ID_EMPLEADO",u.getId_empleado());
                intent.putExtra(Constantes.EMPLEADO, u);


            } else if (u.getRol().equals(Constantes.ROL_GESTOR)) {

                intent = new Intent(this, MenuGestorActivity.class);
                //intent.putExtra("ID_EMPLEADO",u.getId_empleado());
                intent.putExtra(Constantes.EMPLEADO, u);


            }

            if (null != intent) {

                startActivity(intent);
            }
        }
    }


    public void limpiarText(EditText ... array ) {//VARARGS

        for (EditText e : array) {

            //e.setText("");
//            e.clearFocus();

        }
    }

    public void creditos(View view) {

        Intent intent = new Intent(this, Creditos.class);
        startActivity(intent);
        finish();

    }

    private void lanzarActividad(Class actividad_destino) {
        Intent i = new Intent(this, actividad_destino);
        startActivity(i);
        finish();
    }

}

