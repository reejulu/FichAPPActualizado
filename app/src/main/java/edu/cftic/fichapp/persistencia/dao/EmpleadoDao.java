package edu.cftic.fichapp.persistencia.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import edu.cftic.fichapp.bean.Empleado;
import edu.cftic.fichapp.persistencia.DB;
import edu.cftic.fichapp.persistencia.esquemas.IEmpleadoEsquema;
import edu.cftic.fichapp.persistencia.interfaces.IEmpleadoDao;

import static edu.cftic.fichapp.persistencia.esquemas.IEmpleadoEsquema.E_COL_BAJA;
import static edu.cftic.fichapp.persistencia.esquemas.IEmpleadoEsquema.E_COL_CLAVE;
import static edu.cftic.fichapp.persistencia.esquemas.IEmpleadoEsquema.E_COL_ID_EMPRESA;
import static edu.cftic.fichapp.persistencia.esquemas.IEmpleadoEsquema.E_COL_ROL;
import static edu.cftic.fichapp.persistencia.esquemas.IEmpleadoEsquema.E_COL_USUARIO;

public class EmpleadoDao extends CRUD implements IEmpleadoEsquema, IEmpleadoDao {

    private Cursor cursor;
    private ContentValues valoresIniciales;

    public EmpleadoDao(SQLiteDatabase bd) {
        super(bd);
    }

    @Override
    public Empleado getEmpleadoId(int id_empleado) {
        final String argumentos[] = { String.valueOf(id_empleado)};
        final String seleccion = E_COL_ID_EMPLEADO + " = ?";
        Empleado e = new Empleado();
        cursor = super.query(E_TABLA, E_COLUMNAS, seleccion, argumentos);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                e = cursorATabla(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return e;
    }

    @Override
    public List<Empleado> getEmpleados() {
        List<Empleado> empleadoLista = new ArrayList<Empleado>();
        cursor = super.query(E_TABLA, E_COLUMNAS, null, null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                Empleado e = cursorATabla(cursor);
                empleadoLista.add(e);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return empleadoLista;
    }

    @Override
    public Empleado primero() {
        Empleado e = null;
        String LIMITE = "1";
        cursor = super.query(E_TABLA, E_COLUMNAS, null, null, E_COL_ID_EMPLEADO, LIMITE);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            e = cursorATabla(cursor);
            cursor.close();
        }

        return e;
    }

    @Override
    public Empleado ultimo() {
        Empleado e = null;
        String LIMITE = "1";
        cursor = super.query(E_TABLA, E_COLUMNAS, null, null, E_COL_ID_EMPLEADO + " DESC ", LIMITE);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            e = cursorATabla(cursor);
            cursor.close();
        }

        return e;
    }

    @Override
    public List<String> getRoles() {
        List<String> roles = new ArrayList<String>();
        final String columnas[] = { E_COL_ROL };
        cursor = super.query(E_TABLA, columnas, null, null, E_COL_ROL, null, null, null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                if(cursor.getColumnIndex(E_COL_ROL) != -1){
                    int rolIndex = cursor.getColumnIndexOrThrow(E_COL_ROL);
                    roles.add( cursor.getString(rolIndex));
                }
                cursor.moveToNext();
            }
            cursor.close();
        }

        return roles;
    }

    @Override
    public List<Empleado> getRol(String rol) {
        final String argumentos[] = { rol };
        final String seleccion = E_COL_ROL + " = ? " ;
        List<Empleado> e = new ArrayList<>();
        cursor = super.query(E_TABLA, E_COLUMNAS, seleccion, argumentos);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                e.add( cursorATabla(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return e;
    }

    @Override
    public Empleado getEmpleadoUsuarioClave(String usuario, String clave) {
        final String argumentos[] = { usuario, clave };
        final String seleccion = E_COL_USUARIO + " = ? AND " + E_COL_CLAVE + " = ?";
        Empleado e = new Empleado(usuario,clave);
        cursor = super.query(E_TABLA, E_COLUMNAS, seleccion, argumentos);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                e = cursorATabla(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return e;
    }

    private boolean getEmpleadoUsuario(String usuario){
        final String argumentos[] = { usuario };
        final String seleccion = E_COL_USUARIO + " = ? " ;
        boolean e = false;
        cursor = super.query(E_TABLA, E_COLUMNAS, seleccion, argumentos);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                e = true;
                cursor.moveToNext();
            }
            cursor.close();
        }
        return e;
    }

    @Override
    public boolean nuevo(Empleado e) {
        boolean exito = false;
        boolean existe = getEmpleadoUsuario(e.getUsuario());
        if( !existe) {
            setRegistro(e);
            try {
                exito = super.insert(E_TABLA, getRegistro()) > 0;
            } catch (SQLiteConstraintException ex) {
                Log.i("APPK", "Empleado nuevo DAO: " + ex.getMessage());
                exito = false;
            }
        }
        return exito;
    }

    @Override
    public boolean eliminar(int id_empleado) {
        boolean exito = false;
        final String argumentos[] = { String.valueOf(id_empleado) };
        final String seleccion = E_COL_ID_EMPLEADO + " = ?";
        try {
            exito = super.delete(E_TABLA, seleccion, argumentos) > 0;
        } catch (SQLiteConstraintException ex) {
            Log.i("APPK", "Empleado eliminar DAO: "+ ex.getMessage());
            exito = false;
        }
        return exito;
    }

    @Override
    public boolean actualizar(Empleado e) {
        boolean exito = false;
        setRegistro(e);
        final String argumentos[] = { String.valueOf(e.getId_empleado()) };
        final String seleccion = E_COL_ID_EMPLEADO + " = ?";
        try {
            exito = super.update(E_TABLA, getRegistro(), seleccion, argumentos) > 0;
        } catch ( SQLiteConstraintException ex) {
            Log.i("APPK", "Empleado actualizar DAO: "+ ex.getMessage());
            exito = false;
        }
        return exito;
    }

    @Override
    public boolean baja(Empleado e) {
        e.setBaja(true);
        boolean exito = actualizar(e);
        return  exito;
    }

    @Override
    protected Empleado cursorATabla(Cursor cursor) {
        Empleado e = new Empleado();
        int id_empleadoIndex;
        int nombreIndex;
        int usuarioIndex;
        int claveIndex;
        int rolIndex;
        int bajaIndex;
        int id_empresaIndex;

        if(cursor.getColumnIndex(E_COL_ID_EMPLEADO) != -1){
            id_empleadoIndex = cursor.getColumnIndexOrThrow(E_COL_ID_EMPLEADO);
            e.setId_empleado( cursor.getInt(id_empleadoIndex));
        }
        if(cursor.getColumnIndex(E_COL_NOMBRE) != -1){
            nombreIndex = cursor.getColumnIndexOrThrow(E_COL_NOMBRE);
            e.setNombre( cursor.getString(nombreIndex));
        }
        if(cursor.getColumnIndex(E_COL_USUARIO) != -1){
            usuarioIndex = cursor.getColumnIndexOrThrow(E_COL_USUARIO);
            e.setUsuario( cursor.getString(usuarioIndex));
        }
        if(cursor.getColumnIndex(E_COL_CLAVE) != -1){
            claveIndex = cursor.getColumnIndexOrThrow(E_COL_CLAVE);
            e.setClave( cursor.getString(claveIndex));
        }
        if(cursor.getColumnIndex(E_COL_ROL) != -1){
            rolIndex = cursor.getColumnIndexOrThrow(E_COL_ROL);
            e.setRol( cursor.getString(rolIndex));
        }
        if(cursor.getColumnIndex(E_COL_BAJA) != -1){
            bajaIndex = cursor.getColumnIndexOrThrow(E_COL_BAJA);
            e.setBaja( cursor.getInt(bajaIndex) == 0);
        }
        if(cursor.getColumnIndex(E_COL_ID_EMPRESA) != -1){
            id_empresaIndex = cursor.getColumnIndexOrThrow(E_COL_ID_EMPRESA);
            e.setEmpresa( DB.empresas.getEmpresaId( cursor.getInt(id_empresaIndex)));
        }
        return e;
    }

    private void setRegistro(Empleado e){
        valoresIniciales = new ContentValues();
        // valoresIniciales.put(C_COL_ID_EMPLEADO, e.getId_empleado());  // Activar si el campo No es Autoincremental
        valoresIniciales.put(E_COL_NOMBRE, e.getNombre());
        valoresIniciales.put(E_COL_USUARIO, e.getUsuario());
        valoresIniciales.put(E_COL_CLAVE, e.getClave());
        valoresIniciales.put(E_COL_ROL, e.getRol());
        valoresIniciales.put(E_COL_BAJA, e.isBaja());
        valoresIniciales.put(E_COL_ID_EMPRESA, e.getEmpresa().getId_empresa());
    }

    private ContentValues getRegistro(){
        return valoresIniciales;
    }
}
