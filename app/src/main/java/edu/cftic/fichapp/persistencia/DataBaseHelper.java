package edu.cftic.fichapp.persistencia;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class DataBaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    public static final String DATABASE_NAME = "DBControl.db";
    public final static String DATABASE_PATH = "/data/data/edu.cftic.fichapp/databases/";
    public static final int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.myContext = context;
    }

    //Create a empty database on the system
    public void createDatabase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            Log.i("FichApp", "db exists");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been
            // bumped
            onUpgrade(myDataBase, DATABASE_VERSION, DATABASE_VERSION);
        }

        boolean dbExist1 = checkDataBase();
        if (!dbExist1) {
            this.getReadableDatabase();

            try {
                this.close();
                //copyDataBase();
               importDB();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    //Check database already exist or not
    public boolean checkDataBase() {
        boolean checkDB = false;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
           // dbfile.delete();
        } catch (SQLiteException e) {
        }
        return checkDB;
    }

    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException {
        InputStream mInput = myContext.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[2024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //Export DB
    public void exportDatabse(String databaseName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            String sd1 = sd + "/" + "Download/" + DATABASE_NAME;

            //   File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                // SE VAN A COPIAR LOS TRES FICHEROS AL PATH INDICADO
                // DBControl.db,DBControl.db-shm,DBControl.db-wal
                String currentDBPath = DATABASE_PATH + DATABASE_NAME;
                String backupDBPath = "Download/" +DATABASE_NAME;
                String currentDBPath_shm = DATABASE_PATH + DATABASE_NAME+"-shm";
                String backupDBPath_shm = "Download/" +DATABASE_NAME+"-shm";
                String currentDBPath_wal = DATABASE_PATH + DATABASE_NAME+"-wal";
                String backupDBPath_wal = "Download/" +DATABASE_NAME+"-wal";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                File currentDB_shm = new File(currentDBPath_shm);
                File backupDB_shm = new File(sd, backupDBPath_shm);
                File currentDB_wal = new File(currentDBPath_wal);
                File backupDB_wal = new File(sd, backupDBPath_wal);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
                if (currentDB_shm.exists()) {
                    FileChannel src = new FileInputStream(currentDB_shm).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB_shm).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
                if (currentDB_wal.exists()) {
                    FileChannel src = new FileInputStream(currentDB_wal).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB_wal).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
        }
    }

    //import DB
    public void importDB() throws IOException {
        String sd = Environment.getExternalStorageDirectory().toString();
        String sd1 = sd + "/" + "Download/" + DATABASE_NAME;
        InputStream mInput = new FileInputStream(sd1);
        //   InputStream mInput = myContext.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[2024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
        openDatabase();

    }

    //delete database
    public void db_delete() {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if (file.exists()) {
            file.delete();
            System.out.println("delete database file.");
        }
    }

    //Open database
    public void openDatabase() throws SQLException {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDataBase() throws SQLException {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.i("FichApp", "Database version higher than old.");
            db_delete();
        }
    }
}