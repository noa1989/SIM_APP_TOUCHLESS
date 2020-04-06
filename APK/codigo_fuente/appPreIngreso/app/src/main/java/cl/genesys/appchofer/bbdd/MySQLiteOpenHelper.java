package cl.genesys.appchofer.bbdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by juan.venegas on 26-02-2019.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AppChofer.db";

    private static final int DATABASE_VERSION = 1;//1;

    /*
    VERSION 2: SE AGREGA COLUMNA RECEPCION.REC_TIPO_GUIA
    */

    public Context mContext;

    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREAR_TABLA_DESTINOS);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        //db.execSQL("delete table if exists " + "usuario");
        //onCreate(db);

        switch(oldVersion) {
            case 1:
                if(newVersion >= 2){

                }

                if(newVersion >= 3){

                }
                if(newVersion >= 4){
                }
                break;
            case 2:
                if(newVersion >= 3){

                }
                if(newVersion >= 4){
                }
                break;

            default:
                throw new IllegalStateException(
                        "onUpgrade() with unknown oldVersion " + oldVersion);
        }

    }



    public static final String CREAR_TABLA_DESTINOS = " CREATE TABLE destinos( " +
            "planta                  INTEGER NOT NULL,  " +
            "cancha                  INTEGER NOT NULL ,  " +
            "cancha_eerr             INTEGER,  " +
            "nombre                  NVARCHAR(200) ,  " +
            "rut                     INTEGER NOT NULL ,  " +
            "geo_sup_izq_x           NVARCHAR(20) ,  " +
            "geo_sup_izq_y           NVARCHAR(20) ,  " +
            "geo_inf_der_x           NVARCHAR(20) ,  " +
            "geo_inf_der_y           NVARCHAR(20) ,  " +
            "tipo_lugar              INTEGER,  " +
            "utiliza_ctac            NVARCHAR(2),  " +
            "PRIMARY KEY  ([planta], [cancha]) " +
            ");";
}
