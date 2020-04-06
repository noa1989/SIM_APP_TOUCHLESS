package cl.genesys.appchofer.bbdd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cl.genesys.appchofer.entities.Cartolas;
import cl.genesys.appchofer.entities.SpinnerHelper;

public class CartolasDao {
    private SQLiteDatabase db;
    private MySQLiteOpenHelper dbHelper;
    JSONArray jsonArray = null;
    ArrayList<SpinnerHelper> listaHelper;

    public CartolasDao(Context context){
        dbHelper = new MySQLiteOpenHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public int insertCartolas(List<Cartolas> vdata, Context context) throws Exception {
        try {

            dbHelper = new MySQLiteOpenHelper(context);

            for (int i = 0; i < vdata.size(); i++) {
                Cartolas cf = new Cartolas();
                cf = vdata.get(i);

                db = dbHelper.getWritableDatabase();
                open();
                db.execSQL("INSERT INTO CARTOLAS ( " +
                                "cancha," +
                                "cancha_nombre," +
                                "cancha_nom_abrev," +
                                "fundo," +
                                "nombre_fundo," +
                                "num_cartola," +
                                "fecha_volteo," +
                                "rodal," +
                                "rol_numero," +
                                "rol_digito," +
                                "especie," +
                                "producto," +
                                "largo," +
                                "destino," +
                                "faena_cartola," +
                                "faena_pdf," +
                                "rut_eess," +
                                "cod_eess_pdf," +
                                "certfor," +
                                "glosa_certfor," +
                                "fsc," +
                                "glosa_fsc," +
                                "coord_x_fundo," +
                                "coord_y_fundo," +
                                "coord_x_cartola," +
                                "coord_y_cartola" +
                                ") " +
                                "VALUES ( " +
                                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                                "); ",
                        new Object[]{
                                cf.getCancha(),
                                cf.getCancha_nombre(),
                                cf.getCancha_nom_abrev(),
                                cf.getFundo(),
                                cf.getNombre_fundo(),
                                cf.getNum_cartola(),
                                cf.getFecha_volteo(),
                                cf.getRodal(),
                                cf.getRol_numero(),
                                cf.getRol_digito(),
                                cf.getEspecie(),
                                cf.getProducto(),
                                cf.getLargo(),
                                cf.getDestino(),
                                cf.getFaena_cartola(),
                                cf.getFaena_pdf(),
                                cf.getRut_eess(),
                                cf.getCod_eess_pdf(),
                                cf.getCertfor(),
                                cf.getGlosa_certfor(),
                                cf.getFsc(),
                                cf.getGlosa_fsc(),
                                cf.getCoord_x_fundo(),
                                cf.getCoord_y_fundo(),
                                cf.getCoord_x_cartola(),
                                cf.getCoord_y_cartola()
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

    public void deleteCartolas(Context context) throws Exception {

        try {
            dbHelper = new MySQLiteOpenHelper(context);
            db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE FROM CARTOLAS");
            dbHelper.close();
        }catch (Exception ex){
            dbHelper.close();
            throw new Exception(ex.getMessage());
        }

    }

    public Cartolas getCartolas(Context context) throws Exception {

        Cartolas cartolas = null;
        try {
            dbHelper = new MySQLiteOpenHelper(context);
            db = dbHelper.getWritableDatabase();

            String sql = "SELECT " +
                    "cancha," +
                    "cancha_nombre," +
                    "cancha_nom_abrev," +
                    "fundo," +
                    "nombre_fundo," +
                    "num_cartola," +
                    "fecha_volteo," +
                    "rodal," +
                    "rol_numero," +
                    "rol_digito," +
                    "especie," +
                    "producto," +
                    "largo," +
                    "destino," +
                    "faena_cartola," +
                    "faena_pdf," +
                    "rut_eess," +
                    "cod_eess_pdf," +
                    "certfor," +
                    "glosa_certfor," +
                    "fsc," +
                    "glosa_fsc," +
                    "coord_x_fundo," +
                    "coord_y_fundo," +
                    "coord_x_cartola," +
                    "coord_y_cartola " +
                    " FROM CARTOLAS ";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    cartolas = new Cartolas();
                    cartolas.setCancha(cursor.isNull(cursor.getColumnIndexOrThrow("cancha")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("cancha")));
                    cartolas.setCancha_nombre(cursor.isNull(cursor.getColumnIndexOrThrow("cancha_nombre")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("cancha_nombre")));
                    cartolas.setCancha_nom_abrev(cursor.isNull(cursor.getColumnIndexOrThrow("cancha_nom_abrev")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("cancha_nom_abrev")));
                    cartolas.setFundo(cursor.isNull(cursor.getColumnIndexOrThrow("fundo")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("fundo")));
                    cartolas.setNombre_fundo(cursor.isNull(cursor.getColumnIndexOrThrow("nombre_fundo")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("nombre_fundo")));
                    cartolas.setNum_cartola(cursor.isNull(cursor.getColumnIndexOrThrow("num_cartola")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("num_cartola")));
                    cartolas.setFecha_volteo(cursor.isNull(cursor.getColumnIndexOrThrow("fecha_volteo")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("fecha_volteo")));
                    cartolas.setRodal(cursor.isNull(cursor.getColumnIndexOrThrow("rodal")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("rodal")));
                    cartolas.setRol_numero(cursor.isNull(cursor.getColumnIndexOrThrow("rol_numero")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("rol_numero")));
                    cartolas.setRol_digito(cursor.isNull(cursor.getColumnIndexOrThrow("rol_digito")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("rol_digito")));
                    cartolas.setEspecie(cursor.isNull(cursor.getColumnIndexOrThrow("especie")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("especie")));
                    cartolas.setProducto(cursor.isNull(cursor.getColumnIndexOrThrow("producto")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("producto")));
                    cartolas.setLargo(cursor.isNull(cursor.getColumnIndexOrThrow("largo")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("largo")));
                    cartolas.setDestino(cursor.isNull(cursor.getColumnIndexOrThrow("destino")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("destino")));
                    cartolas.setFaena_cartola(cursor.isNull(cursor.getColumnIndexOrThrow("faena_cartola")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("faena_cartola")));
                    cartolas.setFaena_pdf(cursor.isNull(cursor.getColumnIndexOrThrow("faena_pdf")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("faena_pdf")));
                    cartolas.setRut_eess(cursor.isNull(cursor.getColumnIndexOrThrow("rut_eess")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("rut_eess")));
                    cartolas.setCod_eess_pdf(cursor.isNull(cursor.getColumnIndexOrThrow("cod_eess_pdf")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("cod_eess_pdf")));
                    cartolas.setCertfor(cursor.isNull(cursor.getColumnIndexOrThrow("certfor")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("certfor")));
                    cartolas.setGlosa_certfor(cursor.isNull(cursor.getColumnIndexOrThrow("glosa_certfor")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("glosa_certfor")));
                    cartolas.setFsc(cursor.isNull(cursor.getColumnIndexOrThrow("fsc")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("fsc")));
                    cartolas.setGlosa_fsc(cursor.isNull(cursor.getColumnIndexOrThrow("glosa_fsc")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("glosa_fsc")));
                    cartolas.setCoord_x_fundo(cursor.isNull(cursor.getColumnIndexOrThrow("coord_x_fundo")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("coord_x_fundo")));
                    cartolas.setCoord_y_fundo(cursor.isNull(cursor.getColumnIndexOrThrow("coord_y_fundo")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("coord_y_fundo")));
                    cartolas.setCoord_x_cartola(cursor.isNull(cursor.getColumnIndexOrThrow("coord_x_cartola")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("coord_x_cartola")));
                    cartolas.setCoord_y_cartola(cursor.isNull(cursor.getColumnIndexOrThrow("coord_y_cartola")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("coord_y_cartola")));

                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            dbHelper.close();
        }
        return cartolas;
    }

}
