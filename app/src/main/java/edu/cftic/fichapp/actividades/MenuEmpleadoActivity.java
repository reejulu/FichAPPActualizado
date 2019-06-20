package edu.cftic.fichapp.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.util.Constantes;

public class MenuEmpleadoActivity extends AppCompatActivity {

   private Empleado u ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_empleado);
        u = (Empleado) getIntent().getExtras().get(Constantes.EMPLEADO);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }





    //creamos este metodo para que el ActionBar(la flecha hacia atras) funcione bien
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

    public void fichar(View view) {

        Intent intent = new Intent(this,RegistroEntradaSalida.class);
        intent.putExtra(Constantes.EMPLEADO,u);
        startActivity(intent);

    }

    public void consulta(View view) {
        Intent intent = new Intent(this,ConsultaFichajeActivity.class);
        intent.putExtra(Constantes.EMPLEADO,u);
        startActivity(intent);
    }
}
