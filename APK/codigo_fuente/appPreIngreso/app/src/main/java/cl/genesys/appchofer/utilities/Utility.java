package cl.genesys.appchofer.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import cl.genesys.appchofer.R;
import cl.genesys.appchofer.entities.RespuestaJson;
import cl.genesys.appchofer.webapi.Respuesta;

public class Utility {
    public static String caac_geo_sup_izq_x = "", caac_geo_sup_izq_y = "", caac_geo_inf_der_x = "", caac_geo_inf_der_y = "";
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
        //return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static RespuestaJson getResponseWS(SoapObject response) {
        RespuestaJson res = new RespuestaJson();
        res.setSuccess(Boolean.parseBoolean(response.getProperty("success").toString()));
        res.setMessage(response.getProperty("message").toString());
        res.setVersion(response.getProperty("version").toString());
        return res;
    }

    public static void ShowMessageWebApi(Context vcontext, String texto) {
        try {
            Gson gson = new Gson();
            Respuesta result = gson.fromJson(texto, Respuesta.class);
            ShowMessage( vcontext, result.message);
        }catch (Exception ex){
            ShowMessage( vcontext,  texto);
        }
    }


    static Toast toast;
    public static void ShowMessage(Context vcontext, String texto) {
        //RepeatSafeToast.show(vcontext, texto);


        toast = Toast.makeText(vcontext, texto, Toast.LENGTH_SHORT);
        if (toast != null)
            toast.cancel();

        boolean condition = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB;
        if ((toast == null && condition) || !condition)
            toast = Toast.makeText(vcontext, texto, Toast.LENGTH_LONG);
        if ((toast != null && condition))
            toast.setText(texto);




 /*       TextView tv = new TextView(vcontext);
        tv.setBackgroundColor(Color.BLUE);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(R.dimen.text_size_medium);
        tv.setText(texto);
        toast.setView(tv);*/

        toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
        View view = toast.getView();

        //view.setBackgroundResource(R.drawable.background_toast);
        //view.setAlpha(0.99F);

        //TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
        //toastMessage.setTextColor(Color.WHITE);
        //toastMessage.setTextSize(18);
        toast.show();

      /*  Context context = getContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();*/

//        Toast toast = Toast.makeText(vcontext, texto, Toast.LENGTH_SHORT);
//        if(toast.is){
//            toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
//            View view = toast.getView();
//            view.setBackgroundResource(R.drawable.background_toast);
//            view.setAlpha(0.75F);
//            toast.show();
//        }
    }

    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    public static boolean validaTokenSession(JSONObject _SESSION) throws Exception{
        boolean res = true;
        if (_SESSION == null){
            throw new  Exception("No existe información de sesión");
        }
        String TOKEN = Utility.IsNull(_SESSION.getString("TOKEN"), "");
        if(TOKEN.compareTo("") == 0){
            throw new  Exception("No existe información de token");
        }
        return  res;
    }

    public  static void setPreferencia(String nombre, Object valor, Context context){
        try {
            if(valor != null && nombre != null) {
                SharedPreferences sharedPref;
                sharedPref = context.getSharedPreferences(Constantes.keyPreferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (valor.getClass() == Integer.class || valor.getClass() == int.class) {
                    editor.putInt(nombre, (int) valor);
                } else if (valor.getClass() == String.class) {
                    editor.putString(nombre, (String) valor);
                } else if (valor.getClass() == Boolean.class || valor.getClass() == boolean.class) {
                    editor.putBoolean(nombre, (boolean) valor);
                } else if (valor.getClass() == Float.class || valor.getClass() == float.class) {
                    editor.putFloat(nombre, (float) valor);
                } else if (valor.getClass() == Long.class || valor.getClass() == long.class) {
                    editor.putLong(nombre, (long) valor);
                } else {
                    Gson gson = new Gson();
                    String json = gson.toJson(valor);

                    editor.putString(nombre, json);
                }
                editor.commit();
            }

        }catch (Exception ex){

        }
    }

    public  static String getPreferenciaString(String nombre, Context context){
        try {
            if( nombre != null) {
                SharedPreferences sharedPref;
                sharedPref = context.getSharedPreferences(Constantes.keyPreferences, Context.MODE_PRIVATE);
                if(sharedPref.contains(nombre)){
                    return sharedPref.getString(nombre,"");
                }else{
                    return "";
                }
            }else{
                return "";
            }

        }catch (Exception ex){
            return  "";
        }
    }

    public  static Object getPreferenciaObject(String nombre, Context context){
        try {
            if( nombre != null) {
                SharedPreferences sharedPref;
                sharedPref = context.getSharedPreferences(Constantes.keyPreferences, Context.MODE_PRIVATE);
                if(sharedPref.contains(nombre)){
                    String json = sharedPref.getString(nombre,"");
                    if(json.compareTo("")==0){
                        return  null;
                    }else{
                        Gson gson = new Gson();
                        JSONObject result = gson.fromJson(json, JSONObject.class);
                        return result;
                    }
                }else{
                    return null;
                }
            }else{
                return null;
            }

        }catch (Exception ex){
            return  null;
        }
    }

    public static final  String RUTA_ENVIO(Context _context){
        String res = Utility.IsNull(Environment.getExternalStorageDirectory().getAbsolutePath(),"") + "/" + Utility.IsNull( _context.getPackageName(),"") + "/sinc/";
        return res;
    }

    public static Integer IsNull(Integer voriginal, int vreemplazo) {
        if (voriginal == null) {
            return vreemplazo;
        } else {
            return voriginal;
        }
    }
    public static Double IsNull(Double voriginal, double vreemplazo) {
        if (voriginal == null) {
            return vreemplazo;
        } else {
            return voriginal;
        }
    }
    public static String IsNull(String voriginal, String vreemplazo) {
        if (voriginal == null || voriginal.trim().compareTo("") == 0) {
            return vreemplazo;
        } else {
            return voriginal;
        }
    }

    public static Boolean IsNull(Boolean voriginal, boolean vreemplazo) {
        if (voriginal == null) {
            return vreemplazo;
        } else {
            return voriginal;
        }
    }

    private static final int PERMISSION_REQUEST_CODE = 1;
    public static void solicitar_permisos(Context context, String[] permissions) {
        ArrayList<String> permissions_solicitar = new ArrayList<String>();
        for (String permiso : permissions) {
            if (ContextCompat.checkSelfPermission(context,
                    permiso)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions_solicitar.add(permiso);
            }
        }

        if (permissions_solicitar.size() > 0) {
            String[] mStringArray = new String[permissions_solicitar.size()];
            mStringArray = permissions_solicitar.toArray(mStringArray);

            ActivityCompat.requestPermissions((Activity) context,
                    mStringArray,
                    PERMISSION_REQUEST_CODE);
        }


//            if (ContextCompat.checkSelfPermission(context,
//                Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,
//                    Manifest.permission.READ_PHONE_STATE)) {
//
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions((Activity) context,
//                        permissions,
//                        PERMISSION_REQUEST_CODE);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        }
    }

    public static void ShowAlertDialog(Context context, String textoHeader, String textoBody){

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(textoHeader);
        alertDialog.setMessage(textoBody);
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });
        //alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.show();

    }

    public static String validarRutDV(Integer rut) {
        if(rut!=null){
            int M = 0, S = 1;
            int T = rut;
            for (; T != 0; T /= 10) {
                S = (S + T % 10 * (9 - M++ % 6)) % 11;
            }

            return (""+(char) (S != 0 ? S + 47 : 75));
        }else{
            return null;
        }

    }

    public static boolean validaGeocerca(Double lat1, Double lat2, Double lon1, Double lon2, Double latUsuario, Double lonUsuario) {

        boolean lat = false;
        boolean lon = false;

        if (lat1 == 0 || lat2 == 0 || lon1 == 0 || lon2 == 0 || latUsuario == 0 || lonUsuario == 0) {
            return false;
        }
        try {

            if (Math.abs(lat1) <= Math.abs(latUsuario) && Math.abs(lat2) >= Math.abs(latUsuario))
                lat = true;
            else
                lat = false;

            if (Math.abs(lon1) <= Math.abs(lonUsuario) && Math.abs(lon2) >= Math.abs(lonUsuario))
                lon = true;
            else
                lon = false;


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (lat == true && lon == true)
            return true;
        else
            return false;
    }


    public static String RUTA_RECEPCION(Context _context) {
        String res = Utility.IsNull(Environment.getExternalStorageDirectory().getAbsolutePath(),"") + "/" + Utility.IsNull( _context.getPackageName(),"") + "/recepcion/";
        return res;
    }

    public static String RUTA_CONFIRMACION(Context _context) {
        String res = Utility.IsNull(Environment.getExternalStorageDirectory().getAbsolutePath(),"") + "/" + Utility.IsNull( _context.getPackageName(),"") + "/confirmacion_qr/";
        return res;
    }

}
