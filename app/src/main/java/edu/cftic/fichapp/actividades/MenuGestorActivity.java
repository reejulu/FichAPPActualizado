package edu.cftic.fichapp.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.util.Constantes;

import static edu.cftic.fichapp.util.Constantes.EMPLEADO;

public class MenuGestorActivity extends AppCompatActivity {

    private Empleado u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_gestor);
        u = (Empleado) getIntent().getExtras().get(Constantes.EMPLEADO);


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

    public void editarEmpleadoPulsado(View view) {

//salto a la actividad de juanlu de selcionar empleado

        Intent intent = new Intent(this,ModificarCrearBorrarActivity.class);
        intent.putExtra("EMPLEADO",u);
        startActivity(intent);

    }

    public void editarEmpresaPulsado(View view) {

        Intent intent = new Intent(this,RegistroEmpresaActivity.class);
        intent.putExtra("EMPLEADO",u);
        startActivity(intent);
    }

    public void consulta(View view) {

        Intent intent = new Intent(this,ConsultaFichajeActivity.class);
        intent.putExtra("EMPLEADO",u);
        startActivity(intent);
    }

    public void plantilla(View view) {

    }
}
