package edu.cftic.fichapp.actividades;

import android.app.ActionBar;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.bean.Programador;

public class Creditos extends AppCompatActivity {

    private RecyclerView recView;

    private ArrayList<Programador> datos;

    private AdapterCreditos adaptador;

    private static final int rescode =1;
    private static final String TAG = "[MainActivity]";
    private static final int RES_VOICE =210;
    private static final String URL_VIDEO = "https://youtu.be/yspujOeThy4";
    private static final String PREGUNTA = "¿Quien Salvo Alabba y se enamoro con Jazmmin?";
    private static final String RES_CORRECTA = "Aladdín";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creeditos);
        //creamos la variable de Uri en la que va a contener "android.resource://" + el nombre del paquete + / + lo que contiene programadores
        Uri uri = Uri.parse(
                "android.resource://"
                        + getPackageName()
                        + "/"
                        + R.raw.programadores
        );

        // Display the raw resource uri in text view
        Log.d("MIAPP", "URI of the image : \n" + uri.toString());
        try {

            InputStream ins = getResources().openRawResource(R.raw.programadores);

            InputStreamReader br = new InputStreamReader(ins, "UTF-8");
            Gson gson = new Gson();

            //le metemos al json los datos del array
            final Type tipoEnvoltorioEmpleado = new TypeToken<ArrayList<Programador>>() {
            }.getType();
            datos = gson.fromJson(br, tipoEnvoltorioEmpleado);
            Log.d("MIAPP", "tamaño de datos" + datos.size());
            recView = (RecyclerView) findViewById(R.id.RecView);
            recView.setHasFixedSize(true); //mejora el rendimiento, es aconsejable ponerlo

            Collections.shuffle(datos); //hace que el orden en el que salen las filas sea aleatorio cada vez que carga la aplicacion

            //hacemos la referencia a AdapterCreditos
            adaptador = new AdapterCreditos(datos, this);

            recView.setAdapter(adaptador);

            recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

            recView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        } catch (Exception e) {
            Log.e( this.getClass().getCanonicalName(), "Error al procesar los datos de los creditos.",e);
        }
        //metemos la imagen de cftic
        ImageView cas= (ImageView) findViewById(R.id.imageViewLogoCas); //txt is object of TextView
        cas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("https://www.cas-training.com/ "));
                startActivity(browserIntent);
            }
        });
        //metemos el huevo de pascua
        cas.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Animation amimar = AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.slide_in_left);
                //animar
                animarImagen(5000);
                if(SpeechRecognizer.isRecognitionAvailable(getApplicationContext())){
                    Log.d(TAG,"Voz disponible");
                    preguntar();
                }else{
                    Log.w(TAG,"No se puede reconocer la voz");
                }
                return true;
            }
        });
        //metemos el texto con el enlace a la pagina cftic
        TextView txt= (TextView) findViewById(R.id.textViewAccionFormativa); //txt is object of TextView
        txt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("https://cftic.centrosdeformacion.empleo.madrid.org"));
                startActivity(browserIntent);
            }
        });
        //ponemos la flecha para volver atras
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

    //creamos el click del onClick del boton github
    public void clickGithub(View view) {
        Uri webpage = Uri.parse((String) view.getTag());
        OpenWebPage(webpage);
    }
    //creamos el metodo para abrir el link del github, linkedin o email
    private void OpenWebPage(Uri webpage) {
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        String pregunta = "Con que app quieres continuar";
        Intent chooser = Intent.createChooser(intent, pregunta);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    //creamos el click del onClick del boton linkedin
    public void clickLinkedin (View view){
        if( view.getVisibility() == View.VISIBLE) {
            Uri webpage = Uri.parse((String) view.getTag());
            OpenWebPage(webpage);
        }
    }

    private void preguntar(){
        MediaPlayer player = MediaPlayer.create(this,R.raw.question);
        player.setLooping(false);
        player.setVolume(60,60);
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ES");
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,PREGUNTA);
        startActivityForResult(i,RES_VOICE);
        player.start();

    }

    private void animarImagen(long duration){
        Animation animar = AnimationUtils.loadAnimation(this,android.R.anim.fade_out);
        animar.setDuration(duration);
        ImageView cas= (ImageView) findViewById(R.id.imageViewLogoCas); //txt is object of TextView
        cas.setAnimation(animar);
        animar.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"Se ha recibido una actividad");
        if(requestCode==RES_VOICE){
            if(resultCode==RESULT_OK){
                Log.d(TAG,"Respuesta aceptada");
                List<String> voices = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String Vanswer = voices.get(0);
                Log.d(TAG,"Se ha encontrado un resultado "+Vanswer);
                if(Vanswer.equals(RES_CORRECTA)){
                    Log.d(TAG,"Respuesta correcta,abriendo video");
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_VIDEO));
                    Toast.makeText(this, "Respuesta Correcta,adelante", Toast.LENGTH_SHORT).show();
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }else{
                    Log.w(TAG,"Respuesta erronea");
                    Toast.makeText(this, "Lo siento has fallado,  usted ha dicho "+Vanswer, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}

