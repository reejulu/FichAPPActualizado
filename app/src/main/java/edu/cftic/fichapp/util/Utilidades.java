package edu.cftic.fichapp.util;

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
