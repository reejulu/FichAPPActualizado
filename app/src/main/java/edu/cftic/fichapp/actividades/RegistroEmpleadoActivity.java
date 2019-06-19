package edu.cftic.fichapp.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.bean.Empresa;
import edu.cftic.fichapp.persistencia.DB;
import edu.cftic.fichapp.util.Constantes;
import edu.cftic.fichapp.util.FocusListenerFormularios;


public class RegistroEmpleadoActivity extends AppCompatActivity {

    EditText cajanombre;
    EditText cajaapellidos;
    EditText cajausername;
    EditText cajacontraseña;
    EditText cajarepcontraseña;
    String textocajanombre;
    String textocajaapellidos;
    String textocajausername;
    String textocajacontraseña;
    String textocajarepcontraseña;
    Button botonR;
    Button botonM;
    Button botonE;
    RadioButton radioButtonGestor;
    RadioButton radioButtonEmpleado;


    Empleado empleado = new Empleado();
    String gestor = "empleado";
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_empleado);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DB();


        referenciarObjetos();

        activarFocoValidacion();

        Intent intent = getIntent();
        empleado = (Empleado) intent.getSerializableExtra(Constantes.EMPLEADO);

        // Voy a crear una empresa de prueba y un empleado para hacer pruebas

        /*Empresa empresaPrueba = new Empresa();
        empresaPrueba.setCif("11111111A");
        empresaPrueba.setEmail("emaildemiempresa@hotmail.com");
        empresaPrueba.setId_empresa(1);
        empresaPrueba.setNombre_empresa("Mi empresa");
        empresaPrueba.setRutalogo("");
        empresaPrueba.setResponsable("Alex");
        db.empresas.nuevo(empresaPrueba); */

        /*Empleado empleadoPrueba = new Empleado();
        empleadoPrueba.setNombre("Pepito_Gonzalez Fernandez");
        empleadoPrueba.setRol("gestor");
        empleadoPrueba.setClave("Espinete");
        empleadoPrueba.setUsuario("superpepito");
        empleadoPrueba.setId_empleado(1);
        empleadoPrueba.setEmpresa(empresaPrueba);
        db.empleados.nuevo(empleadoPrueba);
         empleado = empleadoPrueba;  */




        logicaBotones();


    }

    public void logicaBotones() {

        if (empleado == null) {
            radioButtonEmpleado.setChecked(true);
            botonM.setEnabled(false);
            botonE.setEnabled(false);
            empleado = new Empleado();

        } else {
            mostrarDatos();

            if (empleado.getRol().equals("empleado")) {
                radioButtonEmpleado.setChecked(true);
                radioButtonGestor.setChecked(false);

            } else {
                radioButtonEmpleado.setChecked(false);
                radioButtonGestor.setChecked(true);
            }
            botonR.setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // HA TOCADO LA FLECHA
        if (item.getItemId() == android.R.id.home)
        // android.R recurso global no solo de mi app
        {
            Log.d("MIAPP", "Tocada la flecha");
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void registrarUsuario(View view) {

        if (recogerDatos()) {

            Empresa empresa = db.empresas.primero();
            empleado.setEmpresa(empresa);

            if(db.empleados.nuevo(empleado)) {
                Toast.makeText(this, "El empleado: " + empleado.getNombre() + " ha sido registrado.", Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }

    public void modificarUsuario(View view) {
        activarFocoValidacion();
        recogerDatos();
        db.empleados.actualizar(empleado);
        Toast.makeText(this, "El empleado: "+ empleado.getNombre() + " ha sido modificado.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MenuGestorActivity.class);
        startActivity(intent);
    }


    public void eliminarUsuario(View view) {

        db.empleados.eliminar(empleado.getId_empleado());
        Toast.makeText(this, "El empleado: "+ empleado.getNombre() + " ha sido eliminado", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MenuGestorActivity.class);
        startActivity(intent);
    }


    public void activarFocoValidacion() {
        FocusListenerFormularios focusListenerFormularios = new FocusListenerFormularios(this);

        cajanombre = (EditText) findViewById(R.id.cajanombre);
        cajanombre.setOnFocusChangeListener(focusListenerFormularios);

        cajaapellidos = (EditText) findViewById(R.id.cajaapellidos);
        cajaapellidos.setOnFocusChangeListener(focusListenerFormularios);

        cajacontraseña = (EditText) findViewById(R.id.cajacontraseña);
        cajacontraseña.setOnFocusChangeListener(focusListenerFormularios);

        cajarepcontraseña = (EditText) findViewById(R.id.cajarepcontraseña);
        cajarepcontraseña.setOnFocusChangeListener(focusListenerFormularios);

        cajausername = (EditText) findViewById(R.id.cajausername);
        cajausername.setOnFocusChangeListener(focusListenerFormularios);
    }

    public void referenciarObjetos() {

        botonR = (Button) findViewById(R.id.buttonRegistrar);
        botonM = (Button) findViewById(R.id.buttonModificar);
        botonE = (Button) findViewById(R.id.buttonEliminar);
        cajanombre = (EditText) findViewById(R.id.cajanombre);
        cajaapellidos = (EditText) findViewById(R.id.cajaapellidos);
        cajausername = (EditText) findViewById(R.id.cajausername);
        cajacontraseña = (EditText) findViewById(R.id.cajacontraseña);
        cajarepcontraseña = (EditText) findViewById(R.id.cajarepcontraseña);
        radioButtonGestor = (RadioButton) findViewById(R.id.radioButtongestor);
        radioButtonEmpleado = (RadioButton) findViewById(R.id.radioButtonempleado);
    }


    public boolean recogerDatos() {

        textocajanombre = cajanombre.getText().toString();
        textocajaapellidos = cajaapellidos.getText().toString();
        textocajausername = cajausername.getText().toString();
        textocajacontraseña = cajacontraseña.getText().toString();
        textocajarepcontraseña = cajarepcontraseña.getText().toString();

        if (!textocajanombre.equals("")) {
            if (!textocajaapellidos.equals("")) {
                if (!textocajausername.equals("")) {
                    if (!textocajacontraseña.equals("")) {
                        if ((!textocajarepcontraseña.equals("")) && textocajacontraseña.equals(textocajarepcontraseña)) {
                            // Utilizo el caracter "_" para separar nombre y apellidos porque en la base de datos solo almacenamos nombre
                            empleado.setNombre(textocajanombre + "_" + textocajaapellidos);


                            empleado.setClave(textocajacontraseña);


                            empleado.setUsuario(textocajausername);


                            if (radioButtonGestor.isChecked() == true) {
                                empleado.setRol("gestor");
                            } else {
                                empleado.setRol("empleado");
                            }
                            return true;


                        }else {

                            Toast.makeText(this, "Las contraseñas no pueden estar vacias y tienen que ser iguales.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, "El campo de contraseña no puede quedar vacio", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "Es necesario un nombre de usuario", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Es necesario los apellidos del empleado", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Es necesario un nombre de empleado", Toast.LENGTH_SHORT).show();
        }


        return false;
    }

    public void mostrarDatos()
    {
        String cadena = empleado.getNombre();
        String[]array = cadena.split("_");
        String soloNombre = array[0];
        String soloApellidos = array[1];

        cajanombre.setText(soloNombre);
        cajaapellidos.setText(soloApellidos);
        cajausername.setText(empleado.getUsuario());
        cajacontraseña.setText(empleado.getClave());
        cajarepcontraseña = cajacontraseña;
    }

}
