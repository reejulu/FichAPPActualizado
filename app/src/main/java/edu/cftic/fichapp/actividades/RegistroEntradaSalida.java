package edu.cftic.fichapp.actividades;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.Date;
import edu.cftic.fichapp.R;

import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.bean.Fichaje;
import edu.cftic.fichapp.persistencia.DB;
import edu.cftic.fichapp.util.Constantes;

public class RegistroEntradaSalida extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private LinearLayout linearBase;
    private TextView horaEntrada, horaSalida, textoCronoFichaje, tituloTV;
    private EditText mensajeET;
    private Button botonEntrada, botonSalida;
    private Date objDate;
    private CountDownTimer cronoFichaje;
    private CheckBox mensajeCheck;
    private Spinner spinner;
    private boolean haFichado;
    private int tipoFichaje;
    private DB bdd;
    private Fichaje ultimoFichaje;


    private final int tiempoFichar = 60000;
    private final int tiempoActualizaCrono = 1000;

    private Fichaje nuevoFichaje;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_registro_entrada_salida);




        // Inicializar vistas y variables
        inicializarVistas();

        // Instanciamos la BB.DD.
        bdd = new DB();

        cargarDatos();


        // Cronometro, tiene un tiempo máximo para fichar (1 min.) si finaliza, vuelve a la Actividad Anterior
        cronoFichaje = new CountDownTimer(tiempoFichar, tiempoActualizaCrono) {
            @Override
            public void onTick(long millisUntilFinished) {
                textoCronoFichaje.setText(millisUntilFinished / 1000 + "");
                //    Log.d("MIAPP", "Ha pasado un segundo " + millisUntilFinished / 1000 + textoCronoFichaje.toString());
            }

            @Override
            public void onFinish() {
                // Si acaba el cronometro y no ha fichado, volver a la pantalla de login
                textoCronoFichaje.setText(R.string.guardado);

                // cronoFichaje.cancel();
                salir(null);


            }
        };
        cronoFichaje.start();


        Log.i("FichApp", "RegistroEntradaSalida-Entrada: " + ultimoFichaje.getFechainicio());
        Log.i("FichApp", "RegistroEntradaSalida-Salida: " + ultimoFichaje.getFechafin());


        // Establecemos el tipo de fichaje si es de salida o de entrada
        if (!ultimoFichaje.getFechafin().equals(new Timestamp(0))) {
            // Tipo fichaje 2 es fichaje de entrada (Fichaje Nuevo)
            tipoFichaje = 1;
        } else {
            // Tipo fichaje 1 es fichaje de salida (Modificar fichaje)
            tipoFichaje = 2;

           // Timestamp t = new Timestamp(0);   //1970-01-01T01:00:00.000+0100
            java.text.SimpleDateFormat sfd = new java.text.SimpleDateFormat("yyyyMMdd");
            // LEEMOS DIA ACTUAL
            Date dateactual = new Date();
            String diaactual = sfd.format(dateactual.getTime());
            // OBTENEMOS EL DIA DE LA FICAHADA INICIAL
            String diafichada = sfd.format(ultimoFichaje.getFechainicio());

            if (!diaactual.equals(diafichada)){
                // asumo que se olvido fichar en salida y esta fichada
                // pasa a ser de llegada
                tipoFichaje = 1;
            }
            //TODO hay que reasignar tipoFichaje cuando
            // por ejemplo , falta fichaje de salida pero el dia es diferente.
            // Tenemos diferentes opciones:
            // 1- Se deja omision de fichada en inicio
            //    La fichada pasa a ser como de entrada en lugar de salida

        }

        // Actualizamos la vista dependiendo si el último fichaje fue de entrada o de salida
        actualizarVista(tipoFichaje);


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

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mimenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
*/

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ficharEntrada(View view) {

        // MOSTRAR HORA ACTUAL EN LA VISTA CUANDO PULSE EL BOTON DE FICHAR

        Log.d("MIAPP", horaFichaje());

        horaEntrada.setVisibility(View.VISIBLE);
        horaEntrada.setText(horaFichaje());
        mensajeET.setEnabled(true);

        haFichado = true;


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ficharSalida(View view) {

        // MOSTRAR HORA ACTUAL EN LA VISTA CUANDO PULSE EL BOTON DE FICHAR

        Log.d("MIAPP", horaFichaje());

        horaSalida.setVisibility(View.VISIBLE);
        horaSalida.setText(horaFichaje());
        mensajeET.setEnabled(true);
        haFichado = true;


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private String horaFichaje() {
        objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate

        Log.d("MIAPP", objDate.toString());

        String strDateFormat = "HH:mm dd-MMMM-YYYY"; // El formato de fecha está especificado
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat); // La cadena de formato de fecha se pasa como un argumento al objeto

        return objSDF.format(objDate);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void actualizarVista(int tipoFichaje) {

        switch (tipoFichaje) {
            case 1:

                // El último fichaje fue de salida
                // Fichaje nuevo
                horaEntrada.setVisibility(View.VISIBLE);
                botonSalida.setEnabled(false);
                botonEntrada.setEnabled(true);

                break;
            case 2:

                // El último fichaje fue Entrada
                // Mostramos en pantalla la hora del fichaje de entrada y
                // Habilitamos la salida (Botón)
                String strDateFormat = "HH:mm dd-MMMM-YYYY"; // El formato de fecha está especificado
                SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat); // La cadena de formato de fecha se pasa como un argumento al objeto
                String ultimoFichajeFormateado = objSDF.format(ultimoFichaje.getFechainicio());
                Log.i("FichApp","RegistroEntradaSalida-ultimofichajeFormateado - es :"+ultimoFichajeFormateado);
                horaEntrada.setVisibility(View.VISIBLE);
                horaEntrada.setText(ultimoFichajeFormateado);
                botonSalida.setEnabled(true);
                botonEntrada.setEnabled(false);
                break;


        }
    }

    private void guardarBBDD() {

        objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        Timestamp fichajeTimestamp = new Timestamp(new Date().getTime());

        switch (tipoFichaje) {
            case 1:
                // Actualizamos el nuevo fichaje y lo guardamos en la BB.DD
                Empleado empleado_ultimo = ultimoFichaje.getEmpleado();
                nuevoFichaje = new Fichaje(empleado_ultimo, fichajeTimestamp, new Timestamp(0), "");
                if (mensajeCheck.isChecked()) {
                    // Guardar mensaje
                    nuevoFichaje.setMensaje(mensajeET.getText().toString());
                }

                DB.fichar.nuevo(nuevoFichaje);
                break;
            case 2:
                // Actualizar el fichaje que existía, añadiendo el campo de salida y lo guardamos en la BB.DD
                ultimoFichaje.setFechafin(fichajeTimestamp);
                // Si hemos incluido mensaje, ponerlo
                // Guardar en Base de Datos
                if (mensajeCheck.isChecked()) {
                    // Guardar mensaje
                    ultimoFichaje.setMensaje(mensajeET.getText().toString());
                }

                DB.fichar.actualizar(ultimoFichaje);
                break;

        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        salir(null);
        return super.onSupportNavigateUp();
    }

    // Seleccionar texto en el Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position != 0) {
            Log.d("MIAPP", "SELECCIONADO SPINNER");
            mensajeET.setText(getResources().getTextArray(R.array.mensajes_array)[position]);
            mensajeCheck.setChecked(true);
        } else {
            hideKeyboard(this);
            view.clearFocus();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void salir(View view) {

        cronoFichaje.cancel();

        if (haFichado) {
            guardarBBDD();
        }

        // Volvemos a la actividad de login
        Intent intent0 = new Intent(this, MainActivity.class);// vamos a menu y pasamos el empleado
        //intent0.putExtra(Constantes.EMPLEADO, empleado);
        startActivity(intent0);
        finish();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        salir(null);
    }

    private void cargarDatos() {



        // RECUPERAR DATOS DEL USUARIO (A TRAVES DEL INTENT
        Intent intent = getIntent();

        Empleado empleado = (Empleado)intent.getExtras().get(Constantes.EMPLEADO);            // Descomentar esta linea para recuperar del intent el empleado


        // Recuperar el último fichaje
        //  Fichaje ul = DB.fichar.getFichajeUltimo(empleado.getId_empleado());     // Descomenta esta linea para que use el empleado recuperado del intent
        Fichaje ul = DB.fichar.getFichajeUltimo(empleado.getId_empleado());              // Comentar esta linea para el uso real en la APP
        if(ul == null){
            boolean exito = DB.fichar.nuevo(new Fichaje(empleado, new Timestamp( new Date().getTime()), new Timestamp(0), "" ));
            if(exito){
                ul = DB.fichar.getFichajeUltimo(empleado.getId_empleado());
            }
        }
        ultimoFichaje = ul;
        Log.i("FichApp", "RegistroEntradaSalida" + ul.toString());
    }

    private void inicializarVistas () {
        String[] mensajes_tipo = getResources().getStringArray(R.array.mensajes_array);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mensajes_tipo);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(this);


        // Ocultar teclado Virtual
        hideKeyboard(this);


        horaEntrada = findViewById(R.id.horaEntrada);
        horaSalida = findViewById(R.id.horaSalida);

        botonEntrada = findViewById(R.id.ficharentradaBTN);
        botonSalida = findViewById(R.id.ficharsalidaBTN);

        textoCronoFichaje = findViewById(R.id.cronoFichaje);
        mensajeCheck = findViewById(R.id.checkmensaje);
        mensajeET = findViewById(R.id.mensajeET);

        tituloTV = findViewById(R.id.tituloTV);

        tituloTV.requestFocus();


        // Utilizamos un boolean para saber si ha fichado o no. Cuando entra en esta Actividad, de primeras es false hasta que pulse el botón de fichar
        // También desactivamos el EditText del mensaje y el check para incluir un mensaje hasta que hayamos fichado
        haFichado = false;
        mensajeET.setEnabled(false);
        mensajeCheck.setEnabled(false);


    }
}

