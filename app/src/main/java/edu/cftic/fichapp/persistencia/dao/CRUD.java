package edu.cftic.fichapp.persistencia.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class CRUD {

    public SQLiteDatabase bd;

    public CRUD(SQLiteDatabase bd) {
        this.bd = bd;
    }

    public long insert(String tabla, ContentValues valores) {
        return bd.insert(tabla, null, valores);
    }

    public int update(String tabla, ContentValues valores,
                      String seleccion, String[] argumentos) {
        return bd.update(tabla, valores, seleccion, argumentos);
    }

    public int delete(String tabla, String seleccion,
                      String[] argumentos) {
        return bd.delete(tabla, seleccion, argumentos);
    }

    public Cursor query(String tabla, String[] columnas, String seleccion, String[] argumentos) {
        Cursor cursor =  null ;
        try {
            cursor = bd.query(tabla, columnas, seleccion, argumentos, null, null, null);
        } catch (Throwable t){
            Log.i("APP", "Error CRUD -1 ");
            return cursor;
        }
        return cursor;
    }

    protected abstract <T> T cursorATabla(Cursor cursor);


    public Cursor query(String tabla, String[] columnas, String seleccion, String[] argumentos, String orden) {
        Cursor cursor =  null ;
        try {
            cursor = bd.query(tabla, columnas, seleccion, argumentos, null, null, orden);
        } catch (Throwable t) {
            Log.i("APP", "Error CRUD -2 ");
            return cursor;
        }
        return cursor;
    }

    public Cursor query(String tabla, String[] columnas, String seleccion, String[] argumentos, String orden,
                        String limite) {
        Cursor cursor =  null ;
        try {
            cursor = bd.query(tabla, columnas, seleccion, argumentos, null, null, orden, limite);
        } catch (Throwable t) {
            Log.i("APP", "Error CRUD -3 ");
            return cursor;
        }
        return cursor;
    }

    public Cursor query(String tabla, String[] columnas, String seleccion, String[] argumentos, String agrupado,
                        String having, String orden, String limite) {
        Cursor cursor =  null ;
        try {
            cursor = bd.query(tabla, columnas, seleccion, argumentos, agrupado, having, orden, limite);
        } catch (Throwable t) {
            Log.i("APP", "Error CRUD -4 ");
            return cursor;
        }
        return cursor;
    }


    public Cursor getDataOfMonthToPdf(int empresaId, String month) {
        return  bd.rawQuery("SELECT  e.nombre, f.fechainicio, f.fechafin \n" +
                ",substr(strftime (\"%Y-%m-%d\" ,(datetime( fechainicio/1000, 'unixepoch'))),6,2)  as month\n" +
                "from empleado as e \n" +
                "INNER JOIN fichaje as f \n" +
                "INNER JOIN empresa as emp\n" +
                "on e.id_empleado = f.id_empleado \n" +
                "and emp.id_empresa = ? \n" +
                "and e.id_empresa = emp.id_empresa\n" +
                "and MONTH = ?\n" +
                "order by e.nombre, f.fechainicio,f.fechafin DESC", new String[]{String.valueOf(empresaId), month});
    }

    public Cursor getALlDataToPdf(int empresaId) {
        return  bd.rawQuery("SELECT  e.nombre, f.fechainicio , f.fechafin\n" +
                "from empleado as e\n" +
                "INNER JOIN fichaje as f\n" +
                "INNER JOIN empresa as emp\n" +
                "on e.id_empleado = f.id_empleado\n" +
                "and emp.id_empresa = ?\n" +
                "and e.id_empresa = emp.id_empresa\n"+
                "order by e.nombre, f.fechainicio,f.fechafin DESC" , new String[]{String.valueOf(empresaId)});
    }

    public Cursor getMonthsToSpinnerSelectMonth() {
        return bd.rawQuery("SELECT  DISTINCT  id_fichaje as _id,  substr(strftime (\"%Y-%m-%d\" ,(datetime( fechainicio/1000, 'unixepoch'))),6,2)  as month\n" +
                "from  fichaje\n" +
                "GROUP BY month", null);
    }
}
