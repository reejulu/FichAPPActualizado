package edu.cftic.fichapp.actividades;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.adapters.AdapterFecha;
import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.bean.Fichaje;
import edu.cftic.fichapp.persistencia.DB;
import edu.cftic.fichapp.util.Constantes;
import edu.cftic.fichapp.util.Fecha;

public class ConsultaFichajeActivity extends AppCompatActivity {

    private ArrayList<Fichaje> listaFichajes;
    private ArrayList<Empleado> listaEmpleados;
    private Map<String, ArrayList<Fichaje>> porDia;
    private RecyclerView recyclerFichajes;
    private EditText fechaInicioEdTxt;
    private EditText fechaFinEdTxt;
    private Timestamp de, hasta;
    private Button botonConsultar;
    private ImageButton btnAñadirFichaje;
    private TextView añadirFichajeText;
    private Empleado u = null;
    private TextView empleadoNombre;
    private TextView textoSelecioneEmpleado;
    private Spinner empleadoNombreSpinner;
    String dia;
    String diatratado;
    int contadordiatratado = 0;
    int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_fichaje);
        // registramos todas las vistas del activity
        fechaInicioEdTxt = findViewById(R.id.fechaInicio);
        fechaFinEdTxt = findViewById(R.id.fechaFin);
        botonConsultar = findViewById(R.id.consultarBtn);
        botonConsultar.setEnabled(false);
        recyclerFichajes = (RecyclerView) findViewById(R.id.recyclerFichajesId);
        empleadoNombreSpinner = findViewById(R.id.empleadoNombreSpinner);
        btnAñadirFichaje = findViewById(R.id.btnAñadir);
        añadirFichajeText = findViewById(R.id.añadirFichajeTextId);
        textoSelecioneEmpleado = findViewById(R.id.textoSelecioneEmpleado);

        botonConsultar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.transicion_boton));

        u = (Empleado) getIntent().getExtras().get(Constantes.EMPLEADO); // Descomentar esta linea y comentar la siguiente cuando se integre el proyecto
        // u = DB.empleados.getEmpleadoId(1); // comentar esta linea y descomentar la anterior cuando se integre el proyecto
        // Ponemos el nombre en el campo TextView "empleadoNombreFicha"
        //empleadoNombre.setText(u.getNombre());
        listaEmpleados = (ArrayList<Empleado>) DB.empleados.getEmpleados();
        ArrayList<String> arrayEmpleados = new ArrayList<>();
        for (Empleado empleado : listaEmpleados) {
            arrayEmpleados.add(empleado.getNombre());
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
                botonConsultar.setText("CONSULTAR FICHAJE");
                contadordiatratado = 0;
                // TODO borrar pantalla anterior con algun refresco del recycler

                Log.i(Constantes.TAG_APP, "pos: " + listaEmpleados.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (u.getRol().equals(Constantes.ROL_GESTOR)) {  // Descomentar esta linea y comentar la siguiente
            // if (u.getRol().equals("B")) {  //Es Gestor
            empleadoNombreSpinner.setEnabled(true);
            btnAñadirFichaje.setEnabled(false);
            btnAñadirFichaje.setVisibility(View.INVISIBLE);
            añadirFichajeText.setVisibility(View.INVISIBLE);
            textoSelecioneEmpleado.setVisibility(View.VISIBLE);
        } else {
            // Tomamos el usuario que recibiremos en un intent desde el login o desde el menu gestor
            //u = (Empleado) getIntent().getExtras().get(Constantes.EMPLEADO);
            empleadoNombreSpinner.setEnabled(false);
            for (int j = 0; j < arrayEmpleados.size(); j++) {
                if (listaEmpleados.get(j).getNombre().contains(u.getNombre())) {
                    empleadoNombreSpinner.setSelection(j);
                }
            }
            Log.i(Constantes.TAG_APP, "posicion entrada: " + listaEmpleados.indexOf(u));
            //empleadoNombreSpinner.setSelection(listaEmpleados.indexOf(u));
            textoSelecioneEmpleado.setVisibility(View.INVISIBLE);
            añadirFichajeText.setVisibility(View.VISIBLE);
        }


        //pedimos las fechas de inicio y fin de la consulta
        fechaInicioEdTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        final String selectedDate = day + " / " + (month + 1) + " / " + year;// +1 porque Enero es cero
                        fechaInicioEdTxt.setText(selectedDate);
                        de = new Timestamp(new Date().getTime());
                        Calendar cc = new GregorianCalendar();
                        cc.clear();
                        cc.setTimeInMillis(de.getTime());
                        cc.set(Calendar.YEAR, year);
                        cc.set(Calendar.MONTH, month);
                        cc.set(Calendar.DAY_OF_MONTH, day);
                        de = Fecha.inicio(new Timestamp(cc.getTimeInMillis()));
                        Log.i(Constantes.TAG_APP, " De = " + de);
                        botonConsultar.setEnabled(!fechaInicioEdTxt.getText().toString().isEmpty() && !fechaFinEdTxt.getText().toString().isEmpty());
                    }
                });
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        fechaFinEdTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 porque Enero es cero
                        final String selectedDate = day + " / " + (month + 1) + " / " + year;
                        fechaFinEdTxt.setText(selectedDate);
                        hasta = new Timestamp(new Date().getTime());
                        Calendar cc = new GregorianCalendar();
                        cc.clear();
                        cc.setTimeInMillis(hasta.getTime());
                        cc.set(Calendar.YEAR, year);
                        cc.set(Calendar.MONTH, month);
                        cc.set(Calendar.DAY_OF_MONTH, day);
                        hasta = Fecha.fin(new Timestamp(cc.getTimeInMillis()));
                        Log.i(Constantes.TAG_APP, " Hasta = " + hasta);
                        botonConsultar.setEnabled(!fechaInicioEdTxt.getText().toString().isEmpty() && !fechaFinEdTxt.getText().toString().isEmpty());
                    }
                });
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });


        botonConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprobamos que la fecha final es posterior a la fecha de inicio
                if (de.after(hasta)) {
                    Toast.makeText(ConsultaFichajeActivity.this, "Fecha inicio debe ser anterior a Fecha fin", Toast.LENGTH_LONG).show();
                    return;
                }
                listaFichajes = (ArrayList<Fichaje>) DB.fichar.getFichaje(u.getId_empleado(), de, hasta);
                Log.i("FichApp", "listaFichajes es : " + listaFichajes);
                //TODO Segun el ejemplo de listaFichajes hay que manejar el recycler de la mejor manera
//  listaFichajes es : [Fichaje{id_fichaje=2, empleado=2, fechainicio=2019-06-23 00:45:12.796, fechafin=2019-06-23 00:54:17.11, mensaje=''}]
                // se hace el primer dia encontrado en listaFichajes
                construirRecycler(contadordiatratado);
                // Si hay mas de un dia entonces el contador de fichadas manejadas (contadordiatratado)
                // no va  a coincidir con el tamaño del array listaFichajes.
                // - No coinciden ambos valores: Indica que el recycler va a mostrar por dia los fichajes del
                //   del empleado seleccionado( para eso el mismo boton es renombrado a " Continuar con siguiente dia?"
                // - Coinciden ambos valores: Indica que o solo se ha seleccionado un dia y por tanto se ha mostrado en el
                //   recycler toda la informacion de las fichadas de ese dia  o  bien es el ultimo dia buscado
                //   En ambos casos el boton hay que volverlo a renombrar a "CONSULTAR FICHAJE"
                //
                int fichadas = listaFichajes.size();
                if (contadordiatratado == fichadas) {
                    botonConsultar.setText("CONSULTAR FICHAJE");
                    contadordiatratado = 0;
                    // no quedan dias que tratar
                } else {
                    // continuar desde el siguiente index de listaFichajes
                    // pulsando el mismo boton
                    botonConsultar.setText("Continuar con siguiente dia?");
                    //construirRecycler(contadordiatratado);
                }
                Log.i("FichApp", "construir recycler");
            }
        });
    }

    private void mostrarfichadapordia(ArrayList<Fichaje> listaFichajes, Map<String, ArrayList<Fichaje>> porDia, int index) {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyyMMdd");
        // Map<String, ArrayList<Fichaje>> porDia = new TreeMap<String, ArrayList<Fichaje>>();
        ArrayList<Fichaje> tmpFichaje = new ArrayList<>();
        ArrayList<Fichaje> tmp1Fichaje = new ArrayList<>();
        diatratado = "primero";
        // continuo la busqueda en listaFichajes desde el ultimo indice buscado

        for (int ii = index; ii < listaFichajes.size(); ii++) {
            Fichaje fichaje = listaFichajes.get(ii);
            contadordiatratado = contadordiatratado + 1;
            if (diatratado.contains("primero")) {
                dia = sfd.format(fichaje.getFechainicio().getTime());

                tmp1Fichaje.add(fichaje);
                porDia.put(dia, tmp1Fichaje);
                if (porDia.containsKey(dia)) {
                    tmpFichaje.add(fichaje);
                    // dia = sfd.format(cadaFichaje.getFechafin().getTime());
                    porDia.put(dia, tmpFichaje);

                } else {
                }
                diatratado = dia;
            } else {
                dia = sfd.format(fichaje.getFechainicio().getTime());
                if (diatratado.equals(dia)) {
                    tmp1Fichaje.add(fichaje);
                    porDia.put(dia, tmp1Fichaje);
                    if (porDia.containsKey(dia)) {
                        tmpFichaje.add(fichaje);
                        // dia = sfd.format(cadaFichaje.getFechafin().getTime());
                        porDia.put(dia, tmpFichaje);

                    } else {
                    }
                } else {
                    contadordiatratado = contadordiatratado - 1;
                    // finalizar
                }
            }
        }
    }

    private void construirRecycler(int contadordiatratado) {
        Log.i(Constantes.TAG_APP, "fichajes= " + listaFichajes.size());
        //SimpleDateFormat sfd = new SimpleDateFormat("d MMM yyyy, EEEE");
        SimpleDateFormat sfd = new SimpleDateFormat("yyyyMMdd");
//  listaFichahes es : [Fichaje{id_fichaje=2, empleado=2, fechainicio=2019-06-23 00:45:12.796, fechafin=2019-06-23 00:54:17.11, mensaje=''}]
        Map<String, ArrayList<Fichaje>> porDia = new TreeMap<String, ArrayList<Fichaje>>();
        index = contadordiatratado;
        mostrarfichadapordia(listaFichajes, porDia, index);
        Log.i("FichApp", "porDia es : " + porDia);

        recyclerFichajes.setLayoutManager(new LinearLayoutManager(this));
        AdapterFecha adapter = new AdapterFecha(this, porDia, ConsultaFichajeActivity.this);
        recyclerFichajes.setAdapter(adapter);

    }


    public void añadirFichajeClick(View view) {

        Intent intentFichar = new Intent(ConsultaFichajeActivity.this, RegistroEntradaSalida.class);
        intentFichar.putExtra(Constantes.EMPLEADO, u);
        startActivity(intentFichar);
    }


    // Metodo para crear fichajes de prueba NO SE USA
/*
    private void fichajesDesdeDB(Empleado usuario) {
        for (int i = 0; i <= 10; i++) {
            DB.fichar.nuevo(new Fichaje(usuario, new Timestamp(new Date().getTime()), new Timestamp(0), "Entrada x"));
            DB.fichar.nuevo(new Fichaje(usuario, new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), "Salida x"));
        }
    }
*/


}


