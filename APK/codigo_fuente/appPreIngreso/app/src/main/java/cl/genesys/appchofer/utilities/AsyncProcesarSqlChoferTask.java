package cl.genesys.appchofer.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import cl.genesys.appchofer.bbdd.MySQLiteOpenHelper;

public class AsyncProcesarSqlChoferTask extends AsyncTask<String, Integer, String> {
    private Context context;
    private PowerManager.WakeLock mWakeLock;
    //ProgressDialog mProgressDialog;
    String gFileName = "";
    String gRuta = "";
    private String MensajeEstado = "";
    private String TipoProceso = "";

    public AsyncProcesarSqlChoferTask(Context context, String vRuta, String vfilename, String vTipoProceso) {
        this.context = context;
        gFileName = vfilename;
        gRuta = vRuta;
        TipoProceso = (Utility.IsNull(vTipoProceso,"").compareTo("") == 0? "" : "(" + vTipoProceso + ")");
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        MensajeEstado = "Transmitiendo OT..." + TipoProceso ;
        publishProgress(0);
        try {
            MySQLiteOpenHelper dbHelper = null;
            SQLiteDatabase db = null;
            try {
                dbHelper = new MySQLiteOpenHelper(context);
                db = dbHelper.getWritableDatabase();
                if (db != null) {
                    MensajeEstado = "Actualizando OT..." + TipoProceso;
                    publishProgress(0);
                    execFileSQL(context, db, gRuta + gFileName );
                    File file = new File(gRuta  + gFileName );
//                    if(file!=null) {
//                        boolean deleted = file.delete();
//                    }
                }
            } catch (Exception ex) {
                try {
                    if (dbHelper != null) {
                        dbHelper.close();
                    }
                } catch  (Exception e) {

                }
                MensajeEstado = "Error de actualización..."+ TipoProceso;
                publishProgress(99);
                return ex.getMessage();
            } finally {
                try {
                    if (dbHelper != null) {
                        dbHelper.close();
                    }
                } catch (Exception ex) {

                }
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        //mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
       /* mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(progress[0]);
        mProgressDialog.setMessage(MensajeEstado);*/
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        //mProgressDialog.dismiss();
        if (result != null) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setMessage("Error de actualización: " + result);
            builder.setTitle("Fallo de actualización");
            builder.setCancelable(false);
            builder.setPositiveButton("Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {

                        }
                    });
            //builder.create().show();
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

            builder.setTitle("Actualización OT completa");
            builder.setMessage("Puede continuar con su viaje.");
            builder.setCancelable(false);
            builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                    }
                });
            //builder.create().show();
        }
    }

    public void execFileSQL(Context context, SQLiteDatabase db, String filePath) throws Exception {
        File vfile = new File(filePath);
        FileInputStream is = new FileInputStream(vfile);
        Boolean exito = false;
        try {

            if (is != null) {

                //BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                int total = Utility.countLines(filePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                int contador = 0;
                String line = reader.readLine();
                is = new FileInputStream(vfile);
                reader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                line = reader.readLine();
                db.beginTransaction();
                while (!TextUtils.isEmpty(line)) {
                    try {
                        //No ejecutamos si la linea viene con comentario
                        if (!line.startsWith("--") && !line.startsWith("/*")) {
                            db.execSQL(line);
                            //if(vtask != null && lineNumberReader != null){
                            if(Utility.IsNull(total,0) >  0) {
                                publishProgress((int) (contador * 100 / total));
                            }
                        }
                        line = reader.readLine();
                        contador ++;
                    } catch (Exception ex) {
                        throw new Exception("no se puede procesar línea: '" + line + "'." + ex.getMessage());
                    }
                }
                db.setTransactionSuccessful();
            }
            exito = true;
        } catch (Exception ex) {
            // Muestra log
            Log.e("insertInitialData", ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (db.inTransaction()) {
                db.endTransaction();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // Muestra log
                    Log.e("insertInitialData", e.getMessage());
                    e.printStackTrace();
                }
            }

//            if(vfile != null && exito){
//                try {
//                    vfile.delete();
//                }catch (Exception ex){
//
//                }
//            }
        }
    }

}