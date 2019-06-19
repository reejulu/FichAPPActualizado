package edu.cftic.fichapp.util;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import edu.cftic.fichapp.R;


public class FocusListenerFormularios implements View.OnFocusChangeListener {

    private Activity actividad;
    private ViewGroup viewGroup;
    TextInputLayout wrapmail;

    public FocusListenerFormularios(Activity activity)
    {
        this.actividad = activity;

        viewGroup = actividad.findViewById(R.id.linearoot);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (!hasFocus) {

            String texto_introducido;
            String texto_tag;

            EditText cajatexto =  (EditText)v;

            texto_introducido = cajatexto.getText().toString();
            texto_tag = String.valueOf(cajatexto.getTag());

            switch (texto_tag){
                case "tagcif":
                    validarCif(texto_introducido,cajatexto);
                    break;
                case "tagnombreempresa":
                    validarNombreEmpresa(texto_introducido,cajatexto);
                    break;
                case "tagresponsable":
                    validarNombreResponsable(texto_introducido,cajatexto);
                    break;
                case "tagemail":
                    validarEmail(texto_introducido,cajatexto);
                    break;
                case "tagnombre":
                    validarNombreEmpleado(texto_introducido,cajatexto);
                    break;
                case "tagapellidos":
                    validarApellidosEmpleado(texto_introducido,cajatexto);
                    break;
                case "tagusername":
                    validarNombreUsuario(texto_introducido,cajatexto);
                    break;
                case "tagcontraseña":
                    validarPass(texto_introducido,cajatexto);
                case "tagrepcontraseña":
                    validarPassRep(texto_introducido,cajatexto);
                    break;
            }
        }
    }

    /**
     * METODO QUE RECIBE CADENA A COMPROBAR (CIF) Y EL EDITTEXT DONDE SE INGRESO. EN CASO DE SER INCORRECTO COMUNICA AL TEXTINPUTLAYOUT CORRESPONDIENTE
     * @param cif
     * @param caja_texto
     */
    public void validarCif (String cif, EditText caja_texto){
        wrapmail = (TextInputLayout)actividad.findViewById(R.id.tilcif);
        if (!Utilidades.nifValido (cif)) {
            wrapmail.setError("Incorrecto A12345678/1234567A");
            caja_texto.setText("");
        } else {
            wrapmail.setErrorEnabled(false);
        }
    }

    /**
     * METODO QUE RECIBE CADENA A COMPROBAR (NOMBRE EMPRESA) Y EL EDITTEXT DONDE SE INGRESO. EN CASO DE SER INCORRECTO COMUNICA AL TEXTINPUTLAYOUT CORRESPONDIENTE
     * @param empresa
     * @param caja_texto
     */
    public void validarNombreEmpresa (String empresa, EditText caja_texto) {
        wrapmail = (TextInputLayout)actividad.findViewById(R.id.tilnombreempresa);
        if (!Utilidades.validarNombre(empresa)){
            wrapmail.setError("Incorrecto.");
            caja_texto.setText("");
        } else {
            wrapmail.setErrorEnabled(false);
        }
    }


    /**
     * METODO QUE RECIBE CADENA A COMPROBAR (NOMBRE RESPONSABLE) Y EL EDITTEXT DONDE SE INGRESO. EN CASO DE SER INCORRECTO COMUNICA AL TEXTINPUTLAYOUT CORRESPONDIENTE
     * @param nombre
     * @param caja_texto
     */

    public void validarNombreResponsable (String nombre, EditText caja_texto) {
        wrapmail = (TextInputLayout)actividad.findViewById(R.id.tilresponsable);
        if (!Utilidades.validarNombre(nombre)){
            wrapmail.setError("Incorrecto.");
            caja_texto.setText("");
        } else {
            wrapmail.setErrorEnabled(false);
        }
    }
    /**
     * METODO QUE RECIBE CADENA A COMPROBAR (EMAIL) Y EL EDITTEXT DONDE SE INGRESO. EN CASO DE SER INCORRECTO COMUNICA AL TEXTINPUTLAYOUT CORRESPONDIENTE
     * @param email
     * @param caja_texto
     */

    public void validarEmail (String email, EditText caja_texto) {
        wrapmail = (TextInputLayout)actividad.findViewById(R.id.tilemail);
        if (!Utilidades.emailValido (email)) {
            wrapmail.setError("Mail incorrecto");
            caja_texto.setText("");
        } else {
            wrapmail.setErrorEnabled(false);
        }
    }


    /**
     * METODO QUE RECIBE CADENA A COMPROBAR (NOMBRE EMPLEADO) Y EL EDITTEXT DONDE SE INGRESO. EN CASO DE SER INCORRECTO COMUNICA AL TEXTINPUTLAYOUT CORRESPONDIENTE
     * @param nombre
     * @param caja_texto
     */
    public void validarNombreEmpleado (String nombre, EditText caja_texto) {
        wrapmail = (TextInputLayout)actividad.findViewById(R.id.tilcajanombre);
        if (!Utilidades.validarNombre(nombre)){
            wrapmail.setError("Incorrecto. Entre 1 y 30 caracteres");
            caja_texto.setText("");
        } else {
            wrapmail.setErrorEnabled(false);
        }
    }

    /**
     * METODO QUE RECIBE CADENA A COMPROBAR (NOMBRE USUARIO) Y EL EDITTEXT DONDE SE INGRESO. EN CASO DE SER INCORRECTO COMUNICA AL TEXTINPUTLAYOUT CORRESPONDIENTE
     * @param nombre
     * @param caja_texto
     */

    public void validarNombreUsuario (String nombre, EditText caja_texto) {
        wrapmail = (TextInputLayout)actividad.findViewById(R.id.tilcajausername);
        if (!Utilidades.validarNombre(nombre)){
            wrapmail.setError("Incorrecto. Entre 1 y 30 caracteres");
            caja_texto.setText("");
        } else {
            wrapmail.setErrorEnabled(false);
        }
    }

    /**
     * METODO QUE RECIBE CADENA A COMPROBAR (APELLIDOS USUARIO) Y EL EDITTEXT DONDE SE INGRESO. EN CASO DE SER INCORRECTO COMUNICA AL TEXTINPUTLAYOUT CORRESPONDIENTE
     * @param nombre
     * @param caja_texto
     */

    public void validarApellidosEmpleado (String nombre, EditText caja_texto) {
        wrapmail = (TextInputLayout)actividad.findViewById(R.id.tilcajaapellidos);
        if (!Utilidades.validarNombre(nombre)){
            wrapmail.setError("Incorrecto. Entre 1 y 30 caracteres");
            caja_texto.setText("");
        } else {
            wrapmail.setErrorEnabled(false);
        }
    }
    /**
     * METODO QUE RECIBE CADENA A COMPROBAR (PASSWORD) Y EL EDITTEXT DONDE SE INGRESO. EN CASO DE SER INCORRECTO COMUNICA AL TEXTINPUTLAYOUT CORRESPONDIENTE
     * @param pass
     * @param caja_texto
     */

    public void validarPass(String pass, EditText caja_texto){
        wrapmail = (TextInputLayout)actividad.findViewById(R.id.tilcajacontraseña);
        if (!Utilidades.contrasenaValida(pass)){
            wrapmail.setError("Incorrecto. Min 6 caracteres");
            caja_texto.setText("");
        } else {
            wrapmail.setErrorEnabled(false);
        }

    }
    /**
     * METODO QUE RECIBE CADENA A COMPROBAR (PASS) Y EL EDITTEXT DONDE SE INGRESO. COMPARA QUE LOS PASSWORD COINCIDAN EN CASO DE SER INCORRECTO COMUNICA AL TEXTINPUTLAYOUT CORRESPONDIENTE
     * @param pass2
     * @param caja_texto
     */

    public void validarPassRep (String pass2, EditText caja_texto) {
        wrapmail = (TextInputLayout)actividad.findViewById(R.id.tilrepcontraseña);
        EditText edit_pass1 = actividad.findViewById(R.id.cajacontraseña);
        String pass1 = edit_pass1.getText().toString();
        if (!Utilidades.comprobarIgual(pass1,pass2)){
            wrapmail.setError("Las contraseñas no coinciden");
            caja_texto.setText("");
        } else {
            wrapmail.setErrorEnabled(false);
        }
    }


}
