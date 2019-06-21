package edu.cftic.fichapp.actividades;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.bean.Empresa;
import edu.cftic.fichapp.pdf.CreatePdfActivity;
import edu.cftic.fichapp.persistencia.DB;
import edu.cftic.fichapp.servicios.GestorAlarma;
import edu.cftic.fichapp.util.Constantes;

import static edu.cftic.fichapp.util.Constantes.EMPLEADO;

public class MenuGestorActivity extends AppCompatActivity {

    private Empleado u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_gestor);
        u = (Empleado) getIntent().getExtras().get(Constantes.EMPLEADO);

        // recoger la empresa y setear el logo en el boton de empresa
        ImageView logo = findViewById(R.id.logo_empresa);
        Empresa empresa = null;
        empresa = DB.empresas.ultimo();
        if (empresa != null){
            String rutalogo = empresa.getRutalogo();
            if(rutalogo!= null){
                logo.setImageURI(Uri.parse(rutalogo));
            }
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

             GestorAlarma gestorAlarma = new GestorAlarma(this);
             gestorAlarma.programarAlarma();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();

        } else if (item.getItemId()==R.id.ayuda)
        {
            Intent intent = new  Intent(this, AyudaActivity.class);
            intent.putExtra("vengo_de_menu", true);
            startActivity(intent);


        }

        return super.onOptionsItemSelected(item);
    }

    public void editarEmpleadoPulsado(View view) {

//salto a la actividad de juanlu de selcionar empleado

        Intent intent = new Intent(this,ModificarCrearBorrarActivity.class);
        intent.putExtra(EMPLEADO,u);
        startActivity(intent);

    }

    public void editarEmpresaPulsado(View view) {

        Intent intent = new Intent(this,RegistroEmpresaActivity.class);
        intent.putExtra(EMPLEADO,u);
        startActivity(intent);
    }

    public void consulta(View view) {

        Intent intent = new Intent(this,ConsultaFichajeActivity.class);
        intent.putExtra(EMPLEADO,u);
        startActivity(intent);
    }

    public void plantilla(View view) {
        Intent intent = new Intent(this, CreatePdfActivity.class);
        intent.putExtra(Constantes.EMPRESA,u.getEmpresa());
        startActivity(intent);
    }


    public void fichar(View view) {

        Intent intent = new Intent(this, RegistroEntradaSalida.class);
        intent.putExtra(EMPLEADO,u);
        startActivity(intent);
    }
}
