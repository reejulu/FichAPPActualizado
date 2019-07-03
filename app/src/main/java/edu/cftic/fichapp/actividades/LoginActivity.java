package edu.cftic.fichapp.actividades;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.persistencia.DB;
import edu.cftic.fichapp.persistencia.DataBaseHelper;
import edu.cftic.fichapp.util.Constantes;
import edu.cftic.fichapp.util.Utilidades;

import static edu.cftic.fichapp.persistencia.DataBaseHelper.DATABASE_NAME;

public class LoginActivity extends AppCompatActivity  {

    EditText usuario;
    EditText contraseña;
    TextView incorrecto;
    Button btn1;
    private Spinner empleadoNombreSpinner;
    private ArrayList<Empleado> listaEmpleados;
    private Empleado u = null;
    String nombre;
    View v;
    View v1;



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
                v = findViewById(R.id.compartir);
                v1 = findViewById(R.id.importar);

            if (u.getRol().equals(Constantes.ROL_GESTOR)) {

                v.setVisibility(View.VISIBLE);
                v1.setVisibility(View.VISIBLE);

            }else {

                v.setVisibility(View.INVISIBLE);
                v1.setVisibility(View.INVISIBLE);

            }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        contraseña = findViewById(R.id.contraseña);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    //creamos este metodo para que el ActionBar(la flecha hacia atras) funcione bien
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DataBaseHelper dbhelper = new DataBaseHelper(getApplicationContext());
        String sd = Environment.getExternalStorageDirectory().toString();
        String sd1 = sd + "/" + "Download/" + DATABASE_NAME;
        File mInput = new File(sd1);
        //SE ENTRAN EN ESTA OPCION SOLO COMO GESTOR
        // Entras como GESTOR ahora y que verificar el usuario y contraseña
        Toast.makeText(this, "ESCRIBA CONTRASEÑA CORRECTA PARA CONTINUAR", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "ESCRIBA CONTRASEÑA INCORRECTA PARA CANCELAR", Toast.LENGTH_SHORT).show();
        empleadoNombreSpinner = findViewById(R.id.usuario);
        incorrecto = findViewById(R.id.incorrecto);
        TextView txt1 = findViewById(R.id.bienvenida);
        txt1.setText("***** ATENCION ***** ESTA OPCION PUEDE BORRAR LA ACTUAL BASE DE DATOS"+"\n");
        txt1.append("** SOLO EN CASO EXTRICTAMENTE NECESARIO **");
        txt1.setTextColor(getResources().getColor(R.color.colorAccent));
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.importar) {

            if (mInput.exists() == true) {
                empleadoNombreSpinner.setVisibility(View.INVISIBLE);
                btn1 = findViewById(R.id.aceptar);
                btn1.setText("IMPORTAR BASE DATOS EXTERNA -> CONTRASEÑA CORRECTA");
                //entrar2();
                /*
                Log.i("FichApp", "LoginActivity- Existe BD en External memory y puede ser importado");
                try {
                    dbhelper.importDB();
                    Toast.makeText(this, "LA BASE DATOS HA SIDO IMPORTADA", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */
            }

        } else if (item.getItemId() == R.id.compartir) {

            empleadoNombreSpinner.setVisibility(View.INVISIBLE);
                        btn1 = findViewById(R.id.aceptar);
            btn1.setText("CONTINUAR BASE DATOS -> CONTRASEÑA CORRECTA");
            //entrar1(1);

        }
        return super.onOptionsItemSelected(item);
    }

    public void  compartir1(){
        DataBaseHelper dbhelper = new DataBaseHelper(getApplicationContext());
        Boolean existe = dbhelper.checkDataBase();

        if (existe == true) {
            Log.i("FichApp", "LoginActivity- Existe BD");
            File dbFile = getDatabasePath("DBcontrol.db");
            Log.i("FichApp", dbFile.toString());
            // AHORA CREAMOS EL FICHERO COPIA EN EL SDCARD EXTERNAL
            dbhelper.exportDatabse("DBControl.db");
            //TODO LLAMAR AL ENVIO DEL MAIL PERO EN ESTA OCASION CON
            // POSIBILIDAD DE ELEGIR CORREO DESTINO
            File sd = Environment.getExternalStorageDirectory();
            String backupDBPath = DATABASE_NAME;
           // File currentDB = new File(currentDBPath);
            File backupDB = new File(sd, backupDBPath);

            String test = String.valueOf(backupDB);
            Intent intent = new Intent(this, EnviarMailActivity.class);
            intent.putExtra("email", "reejulu1@gmail.com");
            intent.putExtra("compartir", true);
            intent.putExtra("path", test);
            startActivity(intent);
 //           Toast.makeText(this, "LA BASE DE DATOS HA SIDO COMPARTIDA CON : " + "reejulu1@gmail.com", Toast.LENGTH_SHORT).show();
            //finish();
        }

    }


    /**
     * COTEJAR LOS DATOS INTRODUCIDOS CON LA BASE DE DATOS (USUARIO Y CONTRASEÑA)
     * @return
     */
    public void entrar(View view) {
        btn1 = findViewById(R.id.aceptar);
        if (btn1.getText().toString().contains("CONTINUAR")){
           entrar1(1);
        }else {
            if (btn1.getText().toString().contains("IMPORTAR")){
                entrar1(2);
            }else {
                entrar1(0);
            }
        }
    }

    public void entrar2(){
        DataBaseHelper dbhelper = new DataBaseHelper(getApplicationContext());
        String sd = Environment.getExternalStorageDirectory().toString();
        String sd1 = sd + "/" + "Download/" + DATABASE_NAME;
        File mInput = new File(sd1);
        Log.i("FichApp", "LoginActivity- Existe BD en External memory y puede ser importado");
        try {
            dbhelper.importDB();
            Toast.makeText(this, "LA BASE DATOS HA SIDO IMPORTADA", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public void entrar1(int i) {
        //nombre = usuario.getText().toString();
        String cont = contraseña.getText().toString();
        String error = "no";
        Empleado u = DB.empleados.getEmpleadoUsuarioClave(nombre,cont);
        contraseña = findViewById(R.id.contraseña);
            incorrecto = findViewById(R.id.incorrecto);

        if(u.getId_empleado() == 0){



            //limpiarText(usuario,contraseña);

            incorrecto.setText(R.string.error_login);
            error = "yes";

        }


        if (error.contains("no")) {
            Intent intent = null;
            String rol = u.getRol();
            contraseña.setText("");
            if (u.getRol().equals(Constantes.ROL_EMPLEADO)) {

                intent = new Intent(this, MenuEmpleadoActivity.class);
                //intent.putExtra("ID_EMPLEADO",u.getId_empleado());
                intent.putExtra(Constantes.EMPLEADO, u);


            } else if (u.getRol().equals(Constantes.ROL_GESTOR)) {

                if (i == 1){
                    // venimos de opcion compartir . solo hay que iniciar dicho proceso
                    compartir1();
                    btn1 = findViewById(R.id.aceptar);
                    empleadoNombreSpinner = findViewById(R.id.usuario);
                    incorrecto = findViewById(R.id.incorrecto);
                    btn1.setText("ACEPTAR");
                    empleadoNombreSpinner.setVisibility(View.VISIBLE);
                    incorrecto.setVisibility(View.VISIBLE);
                    TextView txt1 = findViewById(R.id.bienvenida);
                    txt1.setText("Bienvenid@");
                    txt1.setTextColor(getResources().getColor(R.color.black_overlay));
                }else {
                    if (i == 2){
                        // venimos de opcion importar . solo hay que iniciar dicho proceso
                        entrar2();
                        btn1 = findViewById(R.id.aceptar);
                        empleadoNombreSpinner = findViewById(R.id.usuario);
                        incorrecto = findViewById(R.id.incorrecto);
                        btn1.setText("ACEPTAR");
                        empleadoNombreSpinner.setVisibility(View.VISIBLE);
                        incorrecto.setVisibility(View.VISIBLE);
                        TextView txt1 = findViewById(R.id.bienvenida);
                        txt1.setText("Bienvenid@");
                        txt1.setTextColor(getResources().getColor(R.color.black_overlay));
                    }else {

                        intent = new Intent(this, MenuGestorActivity.class);
                        //intent.putExtra("ID_EMPLEADO",u.getId_empleado());
                        intent.putExtra(Constantes.EMPLEADO, u);
                    }
                }

            }

            if (null != intent) {

                startActivity(intent);
            }
        }
        if (error.contains("yes")&& (i== 1)){
            // venimos de error en contraseña para enviar DB
            // en este caso se entiende que no queremos continuara con la generacion y envio de la DB
            btn1 = findViewById(R.id.aceptar);
            empleadoNombreSpinner = findViewById(R.id.usuario);
            incorrecto = findViewById(R.id.incorrecto);
            contraseña = findViewById(R.id.contraseña);
            incorrecto.setText("");
            contraseña.setText("");
            btn1.setText("ACEPTAR");
            empleadoNombreSpinner.setVisibility(View.VISIBLE);
            incorrecto.setVisibility(View.VISIBLE);
            TextView txt1 = findViewById(R.id.bienvenida);
            txt1.setText("Bienvenid@");
            txt1.setTextColor(getResources().getColor(R.color.black_overlay));
        }
            if (error.contains("yes")&& (i== 2)){
                // venimos de error en contraseña para enviar DB
                // en este caso se entiende que no queremos continuara con la generacion y envio de la DB
                btn1 = findViewById(R.id.aceptar);
                empleadoNombreSpinner = findViewById(R.id.usuario);
                incorrecto = findViewById(R.id.incorrecto);
                contraseña = findViewById(R.id.contraseña);
                incorrecto.setText("");
                contraseña.setText("");
                btn1.setText("ACEPTAR");
                empleadoNombreSpinner.setVisibility(View.VISIBLE);
                incorrecto.setVisibility(View.VISIBLE);
                TextView txt1 = findViewById(R.id.bienvenida);
                txt1.setText("Bienvenid@");
                txt1.setTextColor(getResources().getColor(R.color.black_overlay));
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

