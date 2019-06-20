package edu.cftic.fichapp.util;

import android.Manifest;

public class Constantes {

    public static final String TAG_APP = "FichApp";
    public static final String ROL_GESTOR = "gestor";
    public static final String ROL_EMPLEADO = "empleado";
    public static final String EMPLEADO = "objeto_empleado";
    public static final String EMPRESA = "objeto_empresa";

    public static final String[] PERMISOS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_CONTACTS
    };

    public static final int CODIGO_PETICION_PERMISOS = 150;
    public static final int CODIGO_PETICION_SELECCIONAR_FOTO = 100;

}
