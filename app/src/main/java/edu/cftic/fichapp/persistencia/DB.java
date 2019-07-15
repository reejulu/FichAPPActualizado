package edu.cftic.fichapp.persistencia;

import android.app.Application;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.cftic.fichapp.persistencia.dao.EmpleadoDao;
import edu.cftic.fichapp.persistencia.dao.EmpresaDao;
import edu.cftic.fichapp.persistencia.dao.FichajeDao;
import edu.cftic.fichapp.persistencia.esquemas.IEmpleadoEsquema;
import edu.cftic.fichapp.persistencia.esquemas.IEmpresaEsquema;
import edu.cftic.fichapp.persistencia.esquemas.IFichajeEsquema;


public class DB extends Application {
    private static final String APP = "FichApp";
    private static final String DBNAME = "DBControl.db";
    private DBH db;
    private static final int DBVERSION = 1;
    private Context contexto ;
    public static EmpresaDao empresas;
    public static EmpleadoDao empleados;
    public static FichajeDao fichar;

    // new
    public static final String DATABASE_NAME = "DBControl.db";
    public final static String DATABASE_PATH = "/data/data/edu.cftic.fichapp/databases/";
    private SQLiteDatabase myDataBase;
    private DBH db1;
    SQLiteDatabase _bd;

    public DB open() throws SQLException {
        return this;
    }

    public void close(){
        db.close();
    }


//super(context, DBNAME, null, DBVERSION);
    public void onCreate() {
        super.onCreate();
        contexto = getApplicationContext();
        //db = new DBH(this);
        db = new DBH(contexto);
        _bd = db.getWritableDatabase();
       // myDataBase = db.getWritableDatabase();
        empresas = new EmpresaDao(_bd);
        empleados = new EmpleadoDao(_bd);
        fichar = new FichajeDao(_bd);
    }
    //import DB
    public void importDB() throws IOException {
        String sd = Environment.getExternalStorageDirectory().toString();
        String sd1 = sd + "/" + "Download/" + DATABASE_NAME;
        String inFileName1 = sd1;
        String outFileName1 = DATABASE_PATH + DATABASE_NAME;
        String inFileName2 = sd1 + "-shm";
        String outFileName2 = DATABASE_PATH + DATABASE_NAME+"-shm";
        String inFileName3 = sd1 + "-wal";
        String outFileName3 = DATABASE_PATH + DATABASE_NAME+"-wal";
        File fichero = new File(inFileName1);
        if (fichero.exists()) {
            Log.i("FichApp", "El fichero DBControl.db se copia a la memoria interna");
            InputStream mInput = new FileInputStream(inFileName1);
            String outFileName = outFileName1;
            OutputStream mOutput = new FileOutputStream(outFileName);
            byte[] mBuffer = new byte[2024];
            int mLength;
            while ((mLength = mInput.read(mBuffer)) > 0) {
                mOutput.write(mBuffer, 0, mLength);
            }
            mOutput.flush();
            mOutput.close();
            mInput.close();

        } else {
            Log.i("FichApp", "El fichero DBControl.db no esta disponible");

        }
        fichero = new File(inFileName2);
        if (fichero.exists()) {
            Log.i("FichApp", "El fichero DBControl.db se copia a la memoria interna");
            InputStream mInput = new FileInputStream(inFileName2);
            String outFileName = outFileName2;
            OutputStream mOutput = new FileOutputStream(outFileName);
            byte[] mBuffer = new byte[2024];
            int mLength;
            while ((mLength = mInput.read(mBuffer)) > 0) {
                mOutput.write(mBuffer, 0, mLength);
            }
            mOutput.flush();
            mOutput.close();
            mInput.close();

        } else {
            Log.i("FichApp", "El fichero DBControl.db-shm no esta disponible");

        }
        fichero = new File(inFileName3);
        if (fichero.exists()) {
            Log.i("FichApp", "El fichero DBControl.db-wal se copia a la memoria interna");
            InputStream mInput = new FileInputStream(inFileName3);
            String outFileName = outFileName3;
            OutputStream mOutput = new FileOutputStream(outFileName);
            byte[] mBuffer = new byte[2024];
            int mLength;
            while ((mLength = mInput.read(mBuffer)) > 0) {
                mOutput.write(mBuffer, 0, mLength);
            }
            mOutput.flush();
            mOutput.close();
            mInput.close();

        } else {
            Log.i("FichApp", "El fichero DBControl.db-wal no esta disponible");

        }
    }

    //Open database
    public void openDatabase() throws SQLException {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        db = new DBH(contexto);
        // SQLiteDatabase _bd;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }


    public static class DBH extends SQLiteOpenHelper {

        public DBH(Context context) {
            super(context, DBNAME, null, DBVERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(IEmpresaEsquema.C_CREAR_TABLA);
            db.execSQL(IEmpleadoEsquema.E_CREAR_TABLA);
            db.execSQL(IFichajeEsquema.F_CREAR_TABLA);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(APP, "Actualizando tablas de version " + oldVersion + " a " + newVersion
                    + ".  Se destruyen todos los datos anteriores");
            db.execSQL("DROP TABLE IF EXISTS "+ IEmpresaEsquema.C_TABLA);
            db.execSQL("DROP TABLE IF EXISTS "+ IEmpleadoEsquema.E_TABLA);
            db.execSQL("DROP TABLE IF EXISTS "+ IFichajeEsquema.F_TABLA);
            onCreate(db);
        }
    }
}
