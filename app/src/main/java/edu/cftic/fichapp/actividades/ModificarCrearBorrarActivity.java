package edu.cftic.fichapp.actividades;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.bean.Empresa;
import edu.cftic.fichapp.persistencia.DB;
import edu.cftic.fichapp.util.AdapterEmpleados;
import edu.cftic.fichapp.util.Constantes;

public class ModificarCrearBorrarActivity extends AppCompatActivity {
    private RecyclerView recView;

    private ArrayList<Empleado> datos;
    private AdapterEmpleados adaptador;
    private DB bdd;
    FloatingActionButton sendFabButton;
    TextView txtBorrar;
    TextView txtNombreaBorrar;
    Button btnSi;
    Button btnNo;
    RecyclerView Recyclervista;
    int position;
    Empleado empleado = new Empleado();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_crear_borrar);

        sendFabButton = (FloatingActionButton) findViewById(R.id.fab);
        txtBorrar = (TextView) findViewById(R.id.txtConfirmarBorrar);
        txtNombreaBorrar = (TextView) findViewById(R.id.txtEmpleadoaBorrar);
        btnSi = findViewById(R.id.btnsi);
        btnNo = findViewById(R.id.btnno);

        //sendFabButton.bringToFront();

        // CREO LISTA DE EMPLEADOS
        datos = new ArrayList<Empleado>();
        // Instanciamos la BB.DD.
        bdd = new DB();

        //cargarDatosPrueba();

        //listaEmpleados = (ArrayList<Empleado>) DB.empleados.getEmpleados();
        //Log.i("FichApp","ListaEmpleados ahora es: "+listaEmpleados.toString());
        ArrayList<String> arrayEmpleados = new ArrayList<>();
        //for (Empleado empleado : listaEmpleados) {
        //    arrayEmpleados.add(empleado.getUsuario() + " ---> " + empleado.getRol().toString());
       // }

        datos = (ArrayList<Empleado>) bdd.empleados.getEmpleados();
        Log.i("FichApp","ListaEmpleados ahora es: "+datos.toString());
        for (Empleado empleado : datos) {
            arrayEmpleados.add(empleado.getUsuario() );
        }

        recView = (RecyclerView) findViewById(R.id.RecView);

        adaptador = new AdapterEmpleados(datos);

        recView.setAdapter(adaptador);

        recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Ejemplo como usar SWIPE
        // https://www.youtube.com/watch?v=aKHsfHH5Uas

        ItemTouchHelper itemTouchBorrar = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder targetBorrar, int i) {
                position = targetBorrar.getAdapterPosition();
                String nombre = datos.get(position).getNombre().toString();
                int id = datos.get(position).getId_empleado();
                txtNombreaBorrar.setText(nombre);
                txtNombreaBorrar.setVisibility(View.VISIBLE);
                txtBorrar.setVisibility(View.VISIBLE);
                btnSi.setVisibility(View.VISIBLE);
                btnNo.setVisibility(View.VISIBLE);
                recView.setVisibility(View.INVISIBLE);
                //  datos.remove(position);
                //  adaptador.notifyDataSetChanged();
                // TODO hay que actualizar BD con el elemento quitado

                //ArrayList<Empleado> at = (ArrayList<Empleado>) DB.empleados.getEmpleados();
            }
        }) {

        };
        itemTouchBorrar.attachToRecyclerView(recView);

        /*
        ItemTouchHelper itemModificar = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder targetModificar, int i) {
                int position = targetModificar.getAdapterPosition();
                Empleado empleado = datos.get(position);
                adaptador.notifyDataSetChanged();
                Intent intent = new Intent(getBaseContext(),RegistroEmpleadoActivity.class);
                // AÑADO LOS DATOS DEL EMPLEADO EN EL INTENT
                intent.putExtra(Constantes.EMPLEADO,empleado);
                startActivity(intent);
                Log.i("MIAPP", "ModificarCrearBorrarActivity-swipe to LEFT empleado "+empleado);
                //finish();
            }
        });
        itemModificar.attachToRecyclerView(recView);
        */
        sendFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegistroEmpleadoActivity.class);
                view.getContext().startActivity(intent);
                Log.i("MIAPP", "ModificarCrearBorrarActivity-onClick en floating button");
                finish();
            }
        });

        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adaptador.notifyDataSetChanged();
                txtBorrar.setVisibility(View.INVISIBLE);
                txtNombreaBorrar.setVisibility(View.INVISIBLE);
                btnSi.setVisibility(View.INVISIBLE);
                btnNo.setVisibility(View.INVISIBLE);
                recView.setVisibility(View.VISIBLE);


                // TODO hay que actualizar BD con el elemento quitado
                int posicion_tocada = position;
                empleado = datos.get(posicion_tocada);
                Context context = getApplicationContext();

                Log.i("FichApp", "ModificarCrearBorrarActivity-btnSi-empleado es : " + empleado.getNombre().toString());

                if (empleado.getRol().toString().contains("gestor")) {
                    Toast.makeText(context, "El empleado: " + empleado.getNombre() + " es gestor y no puede borrarse.", Toast.LENGTH_LONG).show();

                }else {

                    bdd.empleados.eliminar(empleado.getId_empleado());

                    datos.remove(position);

                    Log.i("FichApp", "ModificarCrearBorrarActivity-btnSi-id empleado es: " + empleado.getId_empleado());

                }
                datos = (ArrayList<Empleado>) bdd.empleados.getEmpleados();
                Log.i("FichApp","ModificarCrearBorrarActivity-btnSi-datos.size es : "+datos.size());
                recView = (RecyclerView) findViewById(R.id.RecView);

                adaptador = new AdapterEmpleados(datos);
                recView.setAdapter(adaptador);

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adaptador.notifyDataSetChanged();
                txtBorrar.setVisibility(View.INVISIBLE);
                txtNombreaBorrar.setVisibility(View.INVISIBLE);
                btnSi.setVisibility(View.INVISIBLE);
                btnNo.setVisibility(View.INVISIBLE);
                recView.setVisibility(View.VISIBLE);


            }
        });


    }

    @Override
    protected void onResume() {

        datos = (ArrayList<Empleado>) bdd.empleados.getEmpleados();
        Log.i("FichApp","ModificarCrearBorrarActivity-onResume-datos.size es : "+datos.size());
        recView = (RecyclerView) findViewById(R.id.RecView);

        adaptador = new AdapterEmpleados(datos);
        recView.setAdapter(adaptador);

        recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        super.onResume();
    }
    /*private void cargarDatosPrueba() {

                *//*
        ESTE EJEMPLO ESTÁ COMENTADO, PERO ES NECESARIO QUE EL USUARIO, LA PRIMERA VEZ QUE ENTRE EN LA
        APLICACIÓN YA EXISTA EN LA BASE DE DATOS UN REGISTRO DE FICHAJE SIN DATOS EN LA ENTRADA Y SALIDA
        PARA QUE AL RECUPERAR EL ÚLTIMO FICHAJE, NO DE ERROR.
*//*
//Empresa(String cif, String nombre_empresa, String responsable, String email, String rutalogo)
        //String cif, String nombre_empresa, String responsable, String email, String rutalogo
        Empresa em = new Empresa("B123456", "XYZYZ SA", "T T", "reejulu1@gmail.com", "pepe");
        boolean v = DB.empresas.nuevo(em);
        Empleado nu = DB.empleados.getEmpleadoUsuarioClave("", "");
        ArrayList<Empresa> ae = (ArrayList<Empresa>) DB.empresas.getEmpresas();
        em = DB.empresas.ultimo();
        Empresa trr = null;

        // for (int i = 0;i < 1;i++){
        //     trr = new Empresa("B123456"+i, "XYZYZ SA"+i, "T T"+i, "reejulu1@gmail.com","pepe");
        //     boolean t = DB.empresas.nuevo(trr);
        // }

        // Comentar esto (Es para pruebas) --------------------------------
        Log.i("APPK", "u: " + em);
        Empleado tr = null;

        for (int i = 0; i < 100; i++) {
            tr = new Empleado("JUAN YONG" + i, "JYON3" + i, "12345" + i, "B", false, em);
            boolean t = DB.empleados.nuevo(tr);
        }

        ArrayList<Empleado> at = (ArrayList<Empleado>) DB.empleados.getEmpleados();

        tr = DB.empleados.ultimo();
        Log.i("APPK", "E: " + tr);
        for (Empleado es : at) {
            Log.i("APPK", "= " + es);
        }
        at = (ArrayList<Empleado>) DB.empleados.getEmpleados();

        //   Timestamp de = new Timestamp(new Date().getTime());
        //   Timestamp hasta = new Timestamp(new Date().getTime());

        // Comentar o eliminar cuando esté implementado realmente en la APP

*//*
        Fichaje fe = new Fichaje(tr, de, hasta, "Mensaje");
        Log.i("APPK", "F: " + fe);
        boolean d = DB.fichar.nuevo(fe);
        ArrayList<Fichaje> af = (ArrayList<Fichaje>) DB.fichar.getFicheje(tr.getId_empleado());
        for (Fichaje es : af) {
            Log.i("APPK", "= " + es);
        }
        ArrayList<String> rol = (ArrayList<String>) DB.empleados.getRoles();
        for (String es : rol) {
            Log.i("APPK", "R:: " + es);
        }
*//*

    }*/
}

