package cl.genesys.appchofer.bbdd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cl.genesys.appchofer.entities.ListaValores;
import cl.genesys.appchofer.entities.SpinnerHelper;

public class ListaValoresDao {
    private SQLiteDatabase db;
    private MySQLiteOpenHelper dbHelper;
    JSONArray jsonArray = null;
    ArrayList<SpinnerHelper> listaHelper;

    public ListaValoresDao(Context context){
        dbHelper = new MySQLiteOpenHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public int insertListaValores(List<ListaValores> vdata, Context context) throws Exception {
        try {

            dbHelper = new MySQLiteOpenHelper(context);

            for (int i = 0; i < vdata.size(); i++) {
                ListaValores cf = new ListaValores();
                cf = vdata.get(i);

                db = dbHelper.getWritableDatabase();
                open();
                db.execSQL("INSERT or replace into LISTA_VALORES ( " +
                                "LIST_ID" +
                                " ,LIST_DESC" +
                                " ,LIST_TIPO" +
                                ") " +
                                "VALUES ( " +
                                "?, ?, ?" +
                                "); ",
                        new Object[]{
                                cf.getListId(),cf.getListDesc(),cf.getListTipo()
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

    public void deleteListaValores(Context context) throws Exception {

        try {
            dbHelper = new MySQLiteOpenHelper(context);
            db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE FROM LISTA_VALORES");
            dbHelper.close();
        }catch (Exception ex){
            dbHelper.close();
            throw new Exception(ex.getMessage());
        }

    }

    public ArrayList<SpinnerHelper> getListaValores(String tipo){
        open();
        ArrayList<SpinnerHelper> listaHelper = new ArrayList<SpinnerHelper>();

        String sql = "SELECT LIST_ID,LIST_DESC, LIST_TIPO " +
                "FROM LISTA_VALORES " +
                "WHERE LIST_TIPO = ? ";

        String[] selectionsArgs = new String[]{tipo};
        ArrayList<ListaValores> lista = new ArrayList<ListaValores>();
        Cursor cursor = db.rawQuery(sql, selectionsArgs);

        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                SpinnerHelper spn = new SpinnerHelper("", "");
                //ListaValores nuevo = new ListaValores();
                spn.setCod(cursor.getString(cursor.getColumnIndexOrThrow("LIST_ID")));
                spn.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("LIST_DESC")));
                listaHelper.add(spn);
                cursor.moveToNext();
            }
        }

        close();
        return listaHelper;
    }

}
