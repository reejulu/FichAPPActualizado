package edu.cftic.fichapp.persistencia.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import edu.cftic.fichapp.bean.Empresa;
import edu.cftic.fichapp.persistencia.esquemas.IEmpresaEsquema;
import edu.cftic.fichapp.persistencia.interfaces.IEmpresaDao;

public class EmpresaDao extends CRUD implements IEmpresaEsquema, IEmpresaDao {

    private Cursor cursor;
    private ContentValues valoresIniciales;

    public EmpresaDao(SQLiteDatabase bd) {
        super(bd);
    }

    @Override
    public Empresa getEmpresaId(int id_empresa) {
        final String argumentos[] = { String.valueOf(id_empresa)};
        final String seleccion = C_COL_ID_EMPRESA + " = ?";
        Empresa e = new Empresa();
        cursor = super.query(C_TABLA, C_COLUMNAS, seleccion, argumentos);
        if(cursor != null){
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
    public List<Empresa> getEmpresas() {
        List<Empresa> empresaLista = new ArrayList<Empresa>();
        cursor = super.query(C_TABLA, C_COLUMNAS, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                Empresa e = cursorATabla(cursor);
                empresaLista.add(e);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return empresaLista;
    }

    @Override
    public Empresa primero() {
        Empresa e = null;
        String LIMITE = "1";
        cursor = super.query(C_TABLA, C_COLUMNAS, null, null, C_COL_ID_EMPRESA, LIMITE);
        if(cursor != null && cursor.getCount()>0){
            cursor.moveToFirst();
            e = cursorATabla(cursor);
            cursor.close();
        }
        return e;
    }

    @Override
    public Empresa ultimo() {
        Empresa e = null;
        String LIMITE = "1";
        cursor = super.query(C_TABLA, C_COLUMNAS, null, null, C_COL_ID_EMPRESA + " DESC ", LIMITE);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            e = cursorATabla(cursor);
            cursor.close();
        }
        return e;
    }

    @Override
    public boolean nuevo(Empresa e) {
        setRegistro(e);
        try {
            return super.insert(C_TABLA, getRegistro()) > 0;
        } catch ( SQLiteConstraintException ex) {
            Log.i("APPK", "Empresa nuevo DAO: "+ ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id_empresa) {
        final String argumentos[] = { String.valueOf(id_empresa) };
        final String seleccion = C_COL_ID_EMPRESA + " = ?";
        try {
            return super.delete(C_TABLA, seleccion, argumentos) > 0;
        } catch (SQLiteConstraintException ex) {
            Log.i("APPK", "Empresa eliminar DAO: "+ ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Empresa e) {
        setRegistro(e);
        final String argumentos[] = { String.valueOf(e.getId_empresa()) };
        final String seleccion = C_COL_ID_EMPRESA + " = ?";
        try {
            return super.update(C_TABLA, getRegistro(), seleccion, argumentos) > 0;
        } catch ( SQLiteConstraintException ex) {
            Log.i("APPK", "Empresa actualizar DAO: "+ ex.getMessage());
            return false;
        }
    }

    @Override
    protected Empresa cursorATabla(Cursor cursor) {
        Empresa e = new Empresa();
        int id_empresaIndex;
        int cifIndex;
        int nombreIndex;
        int responsableIndex;
        int emailIndex;
        int rutalogoIndex;

        if(cursor.getColumnIndex(C_COL_ID_EMPRESA) != -1){
            id_empresaIndex = cursor.getColumnIndexOrThrow(C_COL_ID_EMPRESA);
            e.setId_empresa( cursor.getInt(id_empresaIndex));
        }
        if(cursor.getColumnIndex(C_COL_CIF) != -1){
            cifIndex = cursor.getColumnIndexOrThrow(C_COL_CIF);
            e.setCif( cursor.getString(cifIndex));
        }
        if(cursor.getColumnIndex(C_COL_NOMBRE) != -1){
            nombreIndex = cursor.getColumnIndexOrThrow(C_COL_NOMBRE);
            e.setNombre_empresa( cursor.getString(nombreIndex));
        }
        if(cursor.getColumnIndex(C_COL_RESPONSABLE) != -1){
            responsableIndex = cursor.getColumnIndexOrThrow(C_COL_RESPONSABLE);
            e.setResponsable( cursor.getString(responsableIndex));
        }
        if(cursor.getColumnIndex(C_COL_EMAIL) != -1){
            emailIndex = cursor.getColumnIndexOrThrow(C_COL_EMAIL);
            e.setEmail( cursor.getString(emailIndex));
        }
        if(cursor.getColumnIndex(C_COL_RUTA_LOGO) != -1){
            rutalogoIndex = cursor.getColumnIndexOrThrow(C_COL_RUTA_LOGO);
            e.setRutalogo( cursor.getString(rutalogoIndex));
        }
        return e;
    }

    private void setRegistro(Empresa e){
        valoresIniciales = new ContentValues();
        // valoresIniciales.put(C_COL_ID_EMPRESA, e.getId_empresa());  // Activar si el campo No es Autoincremental
        valoresIniciales.put(C_COL_CIF, e.getCif());
        valoresIniciales.put(C_COL_NOMBRE, e.getNombre_empresa());
        valoresIniciales.put(C_COL_RESPONSABLE, e.getResponsable());
        valoresIniciales.put(C_COL_EMAIL, e.getEmail());
        valoresIniciales.put(C_COL_RUTA_LOGO, e.getRutalogo());
    }

    private ContentValues getRegistro(){
        return valoresIniciales;
    }

}
