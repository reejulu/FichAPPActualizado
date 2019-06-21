package edu.cftic.fichapp.servicios;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.GmailScopes;

import java.io.File;
import java.util.Arrays;

import edu.cftic.fichapp.actividades.EnviarMailEnviarActivity;
import edu.cftic.fichapp.helper.InternetDetector;
import edu.cftic.fichapp.helper.Utils;

public class EnviarMailServicio extends Service {

    public EnviarMailServicio() {

    }
    FloatingActionButton sendFabButton;
    EditText edtToAddress, edtSubject, edtMessage, edtAttachmentData;
    private ImageView img11;
    Toolbar toolbar;
    GoogleAccountCredential mCredential;
    String emaildestion, emailtitulo, emailmensaje, emailfichero;

    ProgressDialog mProgress;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {
            GmailScopes.GMAIL_LABELS,
            GmailScopes.GMAIL_COMPOSE,
            GmailScopes.GMAIL_INSERT,
            GmailScopes.GMAIL_MODIFY,
            GmailScopes.GMAIL_READONLY,
            GmailScopes.MAIL_GOOGLE_COM
    };
    private InternetDetector internetDetector;
    private final int SELECT_PHOTO = 1;
    public String fileName = "";
    private Uri photo_uri;
    String email;
    String mlastErrorString= "valor inicial";
    String path;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //
        email = intent.getStringExtra("email");

        init();

        return Service.START_NOT_STICKY;


    }

    // startforActivity result en service
    //https://stackoverflow.com/questions/3248132/analog-of-startactivityforresult-for-service

    class MessageReceiver extends ResultReceiver {

        public MessageReceiver() {
            // Pass in a handler or null if you don't care about the thread
            // on which your code is executed.
            super(null);
        }

        /**
         * Called when there's a result available.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // Define and handle your own result codes
            if (resultCode != 2) {

                return;
            }

            // Let's assume that a successful result includes a message.
            path = resultData.getString("MESSAGE");
            // check if file is present
            File file = new File(path);

            if (file.exists()) {
                // IT WILL SEND THE MAIL WITH THE REPORT FILE
                photo_uri = Uri.parse(path);
                fileName = path;
                Log.i("MIAPP", "fileName es : " + fileName);
                emailfichero = fileName;

            } else { // REPORT FILE IS NOT SENT AND E-MAIL subjet and message body are changed.
                emailtitulo = "Informe No disponible";
                emailmensaje = "Contacte con el el administrador";
            }
            getResultsFromApisinTemplate();

            // Now you can do something with it.
        }

    }

    private void getResultsFromApisinTemplate() {
        Log.i("MIAPP", "Lista de mCredential es : " + mCredential.getAllAccounts().toString());
        if (!isGooglePlayServicesAvailable()) {
  //          acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
  //          chooseAccountsinPlantilla();
        } else if (!internetDetector.checkMobileInternetConn()) {
            //        showMessage(view, "No network connection available.");
            Log.i("MIAPP", "get result api es :" + "sin Red");
            //    } else if (!Utils.isNotEmpty(edtToAddress)) {
            //        showMessage(view, "To address Required");
            //    } else if (!Utils.isNotEmpty(edtSubject)) {
            //        showMessage(view, "Subject Required");
            //    } else if (!Utils.isNotEmpty(edtMessage)) {
            //        showMessage(view, "Message Required");
        } else {
  //          new EnviarMailActivity.MakeRequestTask(this, mCredential).execute();
        }

    }

    // Method for Checking Google Play Service is Available
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    // Method to Show Info, If Google Play Service is Not Available.
    /*
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }
    */

    // Method for Google Play Services Error Info
    /*
    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                EnviarMailActivity.this,
                connectionStatusCode,
                Utils.REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }
*/
    // Storing Mail ID using Shared Preferences

    /*
    private void chooseAccountsinPlantilla() {

        if (Utils.checkPermission(getApplicationContext(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApisinTemplate();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(mCredential.newChooseAccountIntent(), Utils.REQUEST_ACCOUNT_PICKER);
            }
        } else {
            ActivityCompat.requestPermissions(EnviarMailActivity.this,
                    new String[]{Manifest.permission.GET_ACCOUNTS}, Utils.REQUEST_PERMISSION_GET_ACCOUNTS);
        }
    }
    */




    /**
     * Starts an activity for retrieving a message.
     */
    private void startMessageActivity() {
        Intent intent = new Intent(this, EnviarMailEnviarActivity.class);

        // Pack the parcelable receiver into the intent extras so the
        // activity can access it.
        intent.putExtra(String.valueOf(Utils.REQUEST_INSERT_FILE_REPORT), new MessageReceiver());

        startActivity(intent);
    }


    private void init() {
        // Initializing Internet Checker
        internetDetector = new InternetDetector(getApplicationContext());

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        //DATOS PARA ENVIAR EL INFORME

        emaildestion = email;
        emailtitulo = "Informe";
        emailmensaje = "Adjunto se envia el fichero .pdf con el reporte mensual";

        // request to attacth the report pdf file to the e-mail
        startMessageActivity();
 //       startActivityForResult(intent, Utils.REQUEST_INSERT_FILE_REPORT);
        // The execution will continue in onActivityResult for REQUEST_INSERT_FILE_REPORT(2)
    }


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
