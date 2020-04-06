package cl.genesys.appchofer.bbdd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cl.genesys.appchofer.entities.ListaValores;
import cl.genesys.appchofer.entities.ProgramaAsicam;

public class ProgramaAsicamDao {

    private SQLiteDatabase db;
    private MySQLiteOpenHelper dbHelper;
    JSONArray jsonArray = null;

    public ProgramaAsicamDao(Context context){
        dbHelper = new MySQLiteOpenHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }



    public String getProgramaDiaInsertScriptById(Context context) throws Exception {

        String res = "";
        try {
            dbHelper = new MySQLiteOpenHelper(context);
            db = dbHelper.getWritableDatabase();

            String sql = "SELECT " +
                    "'INSERT OR REPLACE INTO PROGRAMA_ASICAM " +
                    "(OT," +
                    "gde," +
                    "asicam," +
                    "patente," +
                    "transportista," +
                    "hora_atencion," +
                    "producto," +
                    "destino," +
                    "grua," +
                    "confirma_ot," +
                    "estado" +
                    ") VALUES('  " +
                    "|| IFNULL(OT,'0')   " +
                    "|| ',' ||IFNULL(gde,'0')   " +
                    "|| ',' ||IFNULL(asicam,'0')   " +
                    "|| ',''' || IFNULL(patente,'')  || '''' " +
                    "|| ',''' || IFNULL(transportista,'')  || '''' " +
                    "|| ',''' || IFNULL(hora_atencion,'')  || '''' " +
                    "|| ',''' || IFNULL(producto,'')  || '''' " +
                    "|| ',''' || IFNULL(destino,'')  || '''' " +
                    "|| ',''' || IFNULL(grua,'')  || '''' " +
                    "|| ',''' || IFNULL(confirma_ot,'')  || '''' " +
                    "|| ',' || IFNULL(estado,'0')   " +
                    "||  ')' TEXTO " +
                    " FROM PROGRAMA_ASICAM ";

            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    res = cursor.getString(cursor.isNull(cursor.getColumnIndexOrThrow("TEXTO")) ? null : cursor.getColumnIndexOrThrow("TEXTO"));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            dbHelper.close();
        }
        return res;
    }



    public ArrayList<ProgramaAsicam> getProductoProgramaAsicam(Context context, String CodGrua) throws Exception {

        ArrayList<ProgramaAsicam> programaAsicams = new ArrayList<ProgramaAsicam>();
        try {
            dbHelper = new MySQLiteOpenHelper(context);
            db = dbHelper.getWritableDatabase();
            String sql = "SELECT " +
                    "       p.OT as OT," +
                    "       IFNULL(o.FOLIO_DTE, 0) as gde," +
                    "       p.asicam as asicam," +
                    "       p.patente patente," +
                    "       p.transportista transportista," +
                    "       p.hora_atencion hora_atencion," +
                    "       p.producto producto," +
                    "       p.destino destino," +
                    "       p.confirma_ot confirma_ot," +
                    "       p.estado estado" +
                    "  FROM PROGRAMA_ASICAM p" +
                    "  LEFT join DUT_ORDENES_TRANSPORTE o" +
                    "  on o.NUMERO_OT = p.OT " +
                    "WHERE p.patente = ?1 ";
            Cursor cursor = db.rawQuery(sql, new String[]{CodGrua} );

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    ProgramaAsicam programaAsicam = new ProgramaAsicam();
                    programaAsicam.setOT(cursor.isNull(cursor.getColumnIndexOrThrow("OT")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("OT")));
                    programaAsicam.setGde(cursor.isNull(cursor.getColumnIndexOrThrow("gde")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("gde")));
                    programaAsicam.setAsicam(cursor.isNull(cursor.getColumnIndexOrThrow("asicam")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("asicam")));
                    programaAsicam.setPatente(cursor.isNull(cursor.getColumnIndexOrThrow("patente")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("patente")));
                    programaAsicam.setTransportista(cursor.isNull(cursor.getColumnIndexOrThrow("transportista")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("transportista")));
                    programaAsicam.setHora_atencion(cursor.isNull(cursor.getColumnIndexOrThrow("hora_atencion")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("hora_atencion")));
                    programaAsicam.setProducto(cursor.isNull(cursor.getColumnIndexOrThrow("producto")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("producto")));
                    programaAsicam.setDestino(cursor.isNull(cursor.getColumnIndexOrThrow("destino")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("destino")));
                    programaAsicam.setConfirma_ot(cursor.isNull(cursor.getColumnIndexOrThrow("confirma_ot")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("confirma_ot")));
                    programaAsicam.setEstado(cursor.isNull(cursor.getColumnIndexOrThrow("estado")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("estado")));

                    programaAsicams.add(programaAsicam);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            dbHelper.close();
        }
        return programaAsicams;
    }

    public void deletePrograma(Context context) throws Exception {
        try {
            dbHelper = new MySQLiteOpenHelper(context);
            db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE FROM PROGRAMA_ASICAM");
            dbHelper.close();
        }catch (Exception ex) {
            dbHelper.close();
            throw new Exception(ex.getMessage());
        }
    }

    public int insertPrograma(List<ProgramaAsicam> vdata, Context context) throws Exception {
        try {

            dbHelper = new MySQLiteOpenHelper(context);

            for (int i = 0; i < vdata.size(); i++) {
                ProgramaAsicam cf = new ProgramaAsicam();
                cf = vdata.get(i);

                db = dbHelper.getWritableDatabase();
                open();
                db.execSQL("INSERT into PROGRAMA_ASICAM ( " +
                                "       OT," +
                                "       gde," +
                                "       asicam," +
                                "       patente," +
                                "       transportista," +
                                "       hora_atencion," +
                                "       producto," +
                                "       destino," +
                                "       confirma_ot," +
                                "       estado" +
                                ") " +
                                "VALUES ( " +
                                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                                "); ",
                        new Object[]{
                                cf.getOT(),
                                cf.getGde(),
                                cf.getAsicam(),
                                cf.getPatente(),
                                cf.getTransportista(),
                                cf.getHora_atencion(),
                                cf.getProducto(),
                                cf.getDestino(),
                                cf.getConfirma_ot(),
                                cf.getEstado()
                        });
                dbHelper.close();
                close();
            }

        } catch (Exception ex) {
            dbHelper.close();
            close();
            throw new Exception(ex.getMessage());
        }

        return vdata.size();
    }

}