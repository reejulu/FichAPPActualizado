package edu.cftic.fichapp.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.util.Constantes;
import edu.cftic.fichapp.util.Preferencias;

public class AyudaActivity extends AppCompatActivity {
    private boolean estado_barra = false;

    private boolean viene_de_menu;

    //TODO mejora, añadir la barra de navegación hacia atrás
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);

        viene_de_menu = getIntent().getBooleanExtra("vengo_de_menu", false);

        //Cargar el fichero HTML en el web view
        WebView wv = (WebView) findViewById(R.id.webView2);

        wv.loadUrl("file:///android_asset/texto_ayuda.html");

    }


    public void check(View view) {
        Preferencias.check(this, (CheckBox)findViewById(R.id.no_mostrar));
    }

    public void aceptarAyuda(View view) {
        Intent intent_salida = null;
        if (!viene_de_menu)
        {
            Log.d(Constantes.TAG_APP, "Transitando a Login desde Ayuda");
            intent_salida = new Intent(this, LoginActivity.class);
            startActivity(intent_salida);
        }


        finish(); //si vengo de menú, simplemente cierro
    }
}