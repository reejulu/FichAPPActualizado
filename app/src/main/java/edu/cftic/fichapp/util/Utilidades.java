package edu.cftic.fichapp.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Patterns;


import com.aeat.valida.Validador;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilidades {


    //########################## VALIDACIONES FORMULARIOS REGISTRO #######################################

    private static final String PATRON_PWD = "\\w{6,45}";



    /**
     * VALIDA QUE EL EMAIL PASADO COMO PARÁMETRO SEA VÁLIDO
     * @param email
     * @return TRUE EN CASO DE QUE ESTE CORRECTO EL EMAIL
     */

    public static boolean emailValido (String email)
    {
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    /**
     * VALIDA QUE EL CIF ESTE INGRESADO CORRECTAMENTE
     * @param cif
     * @return TRUE EN CASO DE QUE ESTE CORRECTO EL CIF
     *
     * librería oficial enlace nexus.emergya.es/nexus/content/groups/public/valnif/valnif/2.0.1/
     */

    public static boolean nifValido (String cif) {
        cif = cif.toUpperCase();
        Validador val = new Validador();
        int ret = val.checkNif(cif);
        if( ret > 0) {
            return true;
        }
        else {

            return false;
        }

    }

    /**
     * Valida que una cadena tenga un tamaño. Se usa para validar nombre de usuario y empresa
     * @param nombre
     * @return TRUE EN CASO CORRECTO
     */

    public static boolean validarNombre (String nombre)
    {
        boolean bdev = false;

        bdev = (nombre.length() == 0 || nombre == null )? false :true;

        return bdev;
    }

    /**
     * VALIDA QUE LAS CONTRASEÑAS COINCIDAN
     * @param pass1
     * @param pass2
     * @return TRUE EN CASO CORRECTO
     */
    public static boolean comprobarIgual(
            String pass1, String pass2){
        return pass1.equals(pass2);
    }

    /**
     * VALIDA QUE LA CONTRASEÑA SEA VALIDA CON UN PATRÓN DECLARADO
     * @param p1
     * @return TRUE EN CASO CORRECTO
     */

    public static boolean contrasenaValida (String p1)
    {
        boolean bdev = false;

        Pattern p = Pattern.compile(PATRON_PWD);
        Matcher m = p.matcher(p1);
        bdev = m.matches();

        return bdev;
    }


    //########################## FIN VALIDACIONES FORMULARIOS REGISTRO #######################################


    /**
     * https://stackoverflow.com/questions/20067508/get-real-path-from-uri-android-kitkat-new-storage-access-framework
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                // This is for checking Main Memory
                if ("primary".equalsIgnoreCase(type)) {
                    if (split.length > 1) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1] + "/";
                    } else {
                        return Environment.getExternalStorageDirectory() + "/";
                    }
                    // This is for checking SD Card
                } else {
                    return "storage" + "/" + docId.replace(":", "/");
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



    /**
     * METODO QUE LIMPIAR EL FOCO DE LOS TIPOS DE OBJETOS ENVIADOS QUE TENGAMOS EN LA ACTIVITY
     * @param vista_raiz Parámetro que marca el objeto raiz de un layout
     * @param tipo_buscado Parámetro que marca el tipo de objetos que vamos a limpiar
    public static void limpiarFocusEditText (ViewGroup vista_raiz, Class tipo_buscado)
    {
    List<View> lvistas = null;
    int nhijos = 0;
    ViewGroup vactual = null;
    View vistahija = null;
    List<ViewGroup> lista_vistas = new ArrayList<ViewGroup>();
    lista_vistas.add(vista_raiz);
    lvistas = new ArrayList<View>();
    for (int i = 0; i<lista_vistas.size(); i++)
    {
    vactual = lista_vistas.get(i);
    Log.d("MIAPP", "Mostrando " + vactual.getClass().getCanonicalName());
    nhijos = vactual.getChildCount();
    for (int j = 0;j<nhijos;j++ )
    {
    vistahija = vactual.getChildAt(j);
    if (tipo_buscado.isAssignableFrom(vistahija.getClass()))
    {
    vistahija.clearFocus();
    }
    if (vistahija instanceof  ViewGroup)
    {
    lista_vistas.add((ViewGroup)vistahija);
    }
    else
    {
    Log.d("MIAPP", "Mostrando " + vistahija.getClass().getCanonicalName());
    }
    }
    }
    }
     */
}