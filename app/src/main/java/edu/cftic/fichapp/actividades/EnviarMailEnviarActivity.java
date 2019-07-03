package edu.cftic.fichapp.actividades;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import edu.cftic.fichapp.R;

import static edu.cftic.fichapp.pdf.TemplatePdf.RUTA_INFORME;

public class EnviarMailEnviarActivity extends AppCompatActivity {
    String path;
    String pathsource;
    private static final String[] PERMISOS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int CODIGO_PETICION_PERMISOS = 15;
    Boolean compartir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, PERMISOS, CODIGO_PETICION_PERMISOS);
        // HAY QUE COMPROBAR SI EL PROCESO VIENE DESDE LOGINACTIVITY
        compartir = getIntent().getBooleanExtra("compartir", false);
        if (compartir == true) {
            path = getIntent().getStringExtra("path");
        } else {
            // PASO 1: BORRAR EL FICHERO TEMPORAL EN
            //            path = "data/data/a.bb.bbbb/files/informe.pdf"
            //            SI EL FICHERO EXISTE HAY QUE BORRARLO PUES ESO SIGNIFICA QUE ES ANTIGUO
            path = "data/data/edu.cftic.fichapp/files/informe.pdf";
            File f = new File(path);
            f.delete();
            // FICHERO ORIGEN : DONDE ESTA EL INFORME
            // storage/emulated/0/PDF/
            // FICHERO DESTINO : DONDE VAMOS A GUARDAR TEMPORALMENTE EL INFORME
            // storage/emulated/0/PDF/INFORME_FICHAJE_.pdf a --> data/data/a.bb.bbbb/files/informe.pdf

            // BUSCAMOS SI EL FICHERO DESTION ORIGEN EXISTE:
            // 1- SI EXISTE : LO COPIAMOS AL DESTINO
            // 2- NO EXISTE : ENVIAREMOS EL E-MAIL SIN EL INFORME INDICANDO QUE NO ESTA DISPONIBLE
            try {
                Context context = getApplicationContext();
                File file4 = new File(RUTA_INFORME);
                FileInputStream is = new FileInputStream(file4);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                Log.i("MIAPP", "path interno es : " + path);
                FileOutputStream fos = openFileOutput("informe.pdf", MODE_PRIVATE);
                fos.write(buffer);
                fos.close();
                Log.i("MIAPP", "Se ha creado informe.pdf en data/data... : " + path);
            } catch (
                    Exception e) {
                Log.i("MIAPP", "No exite el fichero en assest- continuo");
            }
        }
        // REQUERIMOS ENVIAR EL E-MAIL: CON O SIN FICHERO ADJUNTADO
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", path);
        setResult(2, intent);
        finish();//finishing activity
        Log.i("MIAPP", "el E-mail ha sido ordenado en SendFileEmail.send ");
        // AHORA LA EJECUCION DEL PROGRAMA CONTINUARA EN ENVIARMAILACTIVITY
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == CODIGO_PETICION_PERMISOS) {
            // en 0 corresponde al primer valor del array PERMISOS -camera y 1 a write_external_storage
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                Log.i("MIAPP", "Me ha concedido Permisos");
            } else {
                Log.i("MIAPP", "NO ME ha concedido Permisos");
                Toast.makeText(this, "NO PUEDES SEGUIR", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }
}
