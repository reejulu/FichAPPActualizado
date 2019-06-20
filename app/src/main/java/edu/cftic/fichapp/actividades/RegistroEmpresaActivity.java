package edu.cftic.fichapp.actividades;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.cftic.fichapp.R;
import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.bean.Empresa;
import edu.cftic.fichapp.persistencia.DB;
import edu.cftic.fichapp.util.Constantes;
import edu.cftic.fichapp.util.FocusListenerFormularios;
import edu.cftic.fichapp.util.Utilidades;

public class RegistroEmpresaActivity extends AppCompatActivity {


    private EditText cajatextomail;
    private EditText cajatextocif;
    private EditText cajatextoresp;
    private EditText cajatextonombreempresa;
    private Button botonM;
    private Button botonN;
    private Button botonE;
    private ImageView imageView;

    private DB dataBase;
    private Empleado empleado;
    private Empresa empresa;
    private Intent intent;
    private Uri photo_uri; // para almacenar la ruta de la imagen

    String[] cuentas_mail = null; // Cuentas de correo del dispositivo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_empresa);
        dataBase = new DB();

        empresa = dataBase.empresas.primero();

        // referenciamos todos los objetos de la vista
        referenciarObjetos();
        //Activamos el escuchador que al peder el foco relizará las posteriores validaciones por cada caja del formulario
        activarFocoValidacion();


        //Recuperación de los datos del Intent
        Intent intent = getIntent();
        empleado = (Empleado) intent.getSerializableExtra(Constantes.EMPLEADO);

        logicaBotonesGestor();
        desactivarModoEstricto();
        // Esta instrucción pide los permisos para acceder a galería de fotos.
        ActivityCompat.requestPermissions(this, Constantes.PERMISOS, Constantes.CODIGO_PETICION_PERMISOS);

    }

    /**
     * METODO QUE RECIBI LA VUELTA DE LA PETICIÓN DE SERVICIOS, EN FUNCIÓN DE LA RESPUESTA PASA POR UNO U OTRO CAMINO
     *
     * @param requestCode  Código de respuesta
     * @param permissions  Array de String de permisos pedido
     * @param grantResults Resultado de estos permisos con su código
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            Log.d("MIAPP", "Me ha concedido los permisos");

        } else {
            Log.d("MIAPP", "NO ME ha concedido los permisos");
            Toast.makeText(this, "No puedes seguir", Toast.LENGTH_SHORT).show();
            this.finish();
        }


    }



    /**
     * METODO QUE LANZA EL ONCLINE DEL EDITTEXT cajatextomail Y CREA UN ALERT DIALOG CON LAS CUENTAS DE CORREO DEL DISPOSITIVO
     * UNA VEZ SELECCIONADA UNA SE SETEA EN EL EDITTEXT cajatextomail
     * @param view EDITTEXT cajatextomail
     */
    public void lanzarPickerCorreos(View view) {

        getCuentasMail();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione cuenta correo: ").setIcon(R.mipmap.ic_launcher)
                .setItems(cuentas_mail, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cajatextomail.setText(cuentas_mail[which]);
                    }
                });
        builder.create();
        builder.show();
    }


    /**
     * METDODO QUE RECOGE MONTA EL ARRAY DE STRING CON LAS CUENTAS DE CORREO SI SON DE GMAIL
     *
     */
    private void getCuentasMail() {
        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);

        Account[] lista_cuentas = accountManager.getAccounts();
        String str_aux = "";

        if (lista_cuentas.length>0){
            for (Account cuenta : lista_cuentas)
            {

                Log.d(getClass().getCanonicalName(), " Cuenta = " + cuenta.name);
                if (cuenta.type.equals("com.google")) //si la cuenta es de gmail
                {
                    str_aux = str_aux + cuenta.name+",";
                }
            }

            if (str_aux.length() != 0)
            {
                cuentas_mail = componerListaCorreos(str_aux.substring(0, str_aux.length() - 1));
            } else //cuentas = 0
            {
                Log.d(getClass().getCanonicalName(), " NO HAY CUENTAS ");
            }
        }else {
            Log.d(getClass().getCanonicalName(), "No consigo ver las cuentas. ");
        }


    }


    /**
     *  METODO QUE RECIBE UN STRING CON LAS CUENTAS DE CORREO Y RETURNA UN ARRAY DE STRING
     * @param listaxcomas String con las cuentas de correo separadas por comas
     * @return Array de String
     */
    private String[] componerListaCorreos(String listaxcomas) {
        String[] lista_correos = null;

        lista_correos = listaxcomas.split(",");

        return lista_correos;
    }

    /**
     * METODO QUE SE LANZA AL PULSAR EL IMAGEBUTTON PARA SELECCIONAR UNA FOTO DE LA GALERÍA
     *
     * @param view ImageButton que activa el método
     */
    public void seleccionarFoto(View view) {
        Log.d("MIAPP", "Quiere seleccionar una foto");
        Intent intent_pide_foto = new Intent();
        intent_pide_foto.setAction(Intent.ACTION_GET_CONTENT);
        intent_pide_foto.setType("image/*"); // tipo mime

        startActivityForResult(intent_pide_foto, Constantes.CODIGO_PETICION_SELECCIONAR_FOTO);
    }


    /**
     * METODO PARA PODER GUARDAR MIS FOTOS DONDE YO QUIERA PQ GOOGLE ME OBLIGA A DECLARARLO EN EL MANIFEST
     */
    private void desactivarModoEstricto()
    {
        if (Build.VERSION.SDK_INT >=24)
        {
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch (Exception e)
            {

            }
        }
    }

    /**
     * METODO QUE RECIBE LA VUELTA DE LA LLAMADA AL MÉTODO startActivityForResult
     *
     * @param requestCode Codigo envíado
     * @param resultCode  Resultado de la operación de la seleeción de foto Si/No
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // super.onActivityResult(requestCode, resultCode, data); //no llamamos al padre
        setearImagenDesdeArchivo(resultCode, data);

    }

    /**
     * MÉTODO QUE RECIBE LA FOTO Y EL RESULTADO (HA SELECCIONADO O NO LA FOTO DE LA GALERIA). EN CASO AFIRMATIVO
     * LA SETEA EN EL imageView Y CARGA EN EL OBJETO EMPRESA PARA SU POSTERIOR GUARDADO EN BD
     *
     * @param resultado Resultado de si ha seleccionado o no una foto
     * @param data      Intent implícito donde tenemos los datos de la foto
     */
    private void setearImagenDesdeArchivo(int resultado, Intent data) {
        switch (resultado) {
            case RESULT_OK:
                Log.d("MIAPP", "La foto ha sido seleccionada");
                this.photo_uri = data.getData();
                this.imageView.setImageURI(photo_uri);
                break;

            case RESULT_CANCELED:
                Log.d("MIAPP", "La foto no ha sido seleccionada canceló");
                break;
        }
    }

    /**
     * METODO QUE RECIBE EL OBJETO EMPLEADO Y EN FUNCIÓN DEL ROL Y LA ACCIÓN RECIBIDA EN EL INTENT HABILITA BOTONES
     * Y SETEA VALORES EN LOS EDITTEXT
     */
    public void logicaBotonesGestor() {
        if (empresa==null){
            //primera vez
            empresa = new Empresa();
            botonM.setEnabled(false);
            botonE.setEnabled(false);
        }else {
            botonN.setEnabled(false);
            setearDatosEmpresa();
        }
    }

    /**
     * MÉTODO QUE SETEA EN LOS CAMPOS DEL FORMULARIO TODOS LOS VALORES DE LA EMPRESA
     */
    public void setearDatosEmpresa () {

        cajatextocif.setText(empresa.getCif());
        cajatextonombreempresa.setText(empresa.getNombre_empresa());
        cajatextoresp.setText(empresa.getResponsable());
        cajatextomail.setText(empresa.getEmail());

        if(empresa.getRutalogo()!= null) {
            Log.d(Constantes.TAG_APP, "RUTA =  " + empresa.getRutalogo());
            /*String[] array = empresa.getRutalogo().split("%");
            String nuevaRuta = array[0] +"%25"+ array[1];
            Log.d(Constantes.TAG_APP, "nuevaRuta " +nuevaRuta);*/
            Log.d(Constantes.TAG_APP, "RUTA =  " + empresa.getRutalogo());
            imageView.setImageURI(Uri.parse(empresa.getRutalogo() ));
        }

    }

    /**
     * METODO QUE ACTIVA LA CLASE PARA VALIDAR LOS DATOS CUANDO VAMOS PERDIENDO EL FOCO EN CADA EDITTEXT
     */
    public void activarFocoValidacion() {

        FocusListenerFormularios focusListenerFormularios = new FocusListenerFormularios(this);
        cajatextocif.setOnFocusChangeListener(focusListenerFormularios);
        //cajatextomail.setOnFocusChangeListener(focusListenerFormularios);
        cajatextoresp.setOnFocusChangeListener(focusListenerFormularios);
        cajatextonombreempresa.setOnFocusChangeListener(focusListenerFormularios);
    }

    /**
     * METODO QUE SE LANZA AL PULSAR EL BOTON REGISTRAR, GUARDA EN BASE DE DATOS LA NUEVA EMPRESA
     *
     * @param v Boton que lanza el método
     */
    public void registrar(View v) {

        if(recogerDatosCajas()){
            dataBase.empresas.nuevo(empresa);
            lanzarIntentRegistroEmpleado();
        }

    }

    /**
     * METODO QUE SE LANZA AL PULSAR EL BOTON MODIFICAR, GUARDA EN BASE DE DATOS LA EMPRESA MODIFICADA
     *
     * @param v Boton que lanza el método
     */
    public void modificar(View v) {
        if(recogerDatosCajas()){
            dataBase.empresas.actualizar(empresa);
            lanzarIntentMenuGestor();
        }

    }

    /**
     * METODO QUE SE LANZA AL PULSAR EL BOTON ELIMINAR, ELIMINA EN BASE DE DATOS LA EMPRESA, PREVIO AVISO DE CONFIRMACIÓN
     *
     * @param v Boton que lanza el método
     */
    public void eliminar(View v) {

        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("Si elimina la empresa todos los empleados dados de alta no tendrán empresa. ¿Desea continuar?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                if (recogerDatosCajas()){
                    dataBase.empresas.eliminar(empresa.getId_empresa());
                    lanzarIntentMenuGestor();
                }
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                // Se cancela la operación de elmiminación
            }
        });
        dialogo1.show();
    }

    /**
     * METODO QUE RECOGE LOS DATOS DE LOS EDITTEXT, COMPRUEBA QUE TENGAN VALORES Y EN CASO AFIRMATIVO LOS SETEA EN EL OBJETO EMPRESA Y DEVUELVE UN TRUE
     * @return Retorna false si faltan datos y true si esta completo (logo de la empresa es opcional)
     */
    public boolean recogerDatosCajas() {

        boolean isValido = false;
        String cif=cajatextocif.getText().toString();
        String nombreEmpresa=cajatextonombreempresa.getText().toString();
        String responsable =  cajatextoresp.getText().toString();
        String email = cajatextomail.getText().toString();
        String urlLogo= null;
        if (photo_uri != null){
            Log.d(Constantes.TAG_APP," uri " + photo_uri);
            urlLogo = Utilidades.getPath(this,photo_uri);
            Log.d(Constantes.TAG_APP," uri " + urlLogo);
        }

        if(cif.length() != 0){
            if (nombreEmpresa.length() != 0){
                if (responsable.length() != 0){
                    if (email.length() != 0) {

                        empresa.setCif(cif.toUpperCase());
                        empresa.setNombre_empresa(nombreEmpresa);
                        empresa.setResponsable(responsable);
                        empresa.setEmail(email);
                        Log.d(Constantes.TAG_APP," uri " + urlLogo);
                        if(urlLogo!=null){
                            empresa.setRutalogo(urlLogo);
                        }

                        return isValido=true;
                    }else{
                        Toast.makeText(this,"EL EMAIL DEBE CONTENER DATOS",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(this,"EL NOMBRE DEL RESPONSABLE DEBE CONTENER DATOS",Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this,"EL NOMBRE DE LA EMPRESA DEBE CONTENER DATOS",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"EL CIF DEBE CONTENER DATOS",Toast.LENGTH_LONG).show();
        }
        return isValido;

    }

    /**
     *  PROBLEMAS CUANDO RECUPERAMOS UNA FOTO DE LA GALERÍA Y LA INTENTAMOS SETEAR EN UN IMAGEVIEW PREVIA CONVERSIÓN A UNA URI
     *  YA QUE NOS DEVUELVE UNA URI QUE COMIENZA CON CONTENT:
     *
     *  EN CAMBIO ESTE MÉTODO UTILIZAR UN EL getContentResolver() PARA DEVOLVER UNA RUTA CONVERTIDA DE ESTE TIPO:
     *  /storage/emulated/0/DCIM/Camera/IMG_20190607_095229.jpg
     *  // https://stackoverflow.com/questions/5657411/android-getting-a-file-uri-from-a-content-uri#answer-12603415
     * @return

    private String getFilePath() {
    String filePath = null;
    if (photo_uri != null && "content".equals(photo_uri.getScheme())) {
    Cursor cursor = this.getContentResolver().query(photo_uri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
    cursor.moveToFirst();
    filePath = cursor.getString(0);
    cursor.close();
    } else {
    if( photo_uri != null) {
    filePath = photo_uri.getPath();
    }
    }
    Log.d("MIAPP", "Chosen path = " + filePath);

    return filePath;
    }
     */


    /**
     * MÉTODO QUE LANZA EL INTENT HACIA EL REGISTRO DEL GESTOR
     */
    public void lanzarIntentRegistroEmpleado() {
        intent = new Intent(this, RegistroEmpleadoActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * MÉTODO QUE LANZA EL INTENT HACIA EL MENÚ DEL GESTOR
     */
    public void lanzarIntentMenuGestor() {
        intent = new Intent(this, MenuGestorActivity.class);
        intent.putExtra("EMPLEADO", empleado);
        startActivity(intent);
        this.finish();
    }


    /**
     * MÉTODO QUE REFERENCIA TODOS LOS OBJETOS DE LA VISTA PARA SU POSTERIOR USO
     */
    public void referenciarObjetos() {
        imageView = findViewById(R.id.imagen_empresa);
        botonM = (Button) findViewById(R.id.botonmodificar);
        botonE = (Button) findViewById(R.id.botoneliminar);
        botonN = (Button) findViewById(R.id.botonenviar);
        cajatextocif = (EditText) findViewById(R.id.cajacif);
        cajatextomail = (EditText) findViewById(R.id.cajaemail);
        cajatextoresp = (EditText) findViewById(R.id.cajaresponsable);
        cajatextonombreempresa = (EditText) findViewById(R.id.cajanombreempresa);
    }

}