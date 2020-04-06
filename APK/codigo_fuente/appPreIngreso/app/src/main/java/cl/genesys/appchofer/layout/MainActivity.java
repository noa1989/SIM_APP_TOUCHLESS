package cl.genesys.appchofer.layout;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import cl.genesys.appchofer.FsService;
import cl.genesys.appchofer.FsSettings;
import cl.genesys.appchofer.R;
import cl.genesys.appchofer.bbdd.CartolasDao;
import cl.genesys.appchofer.bbdd.ListaValoresDao;
import cl.genesys.appchofer.bbdd.OrdenesTransporteDao;
import cl.genesys.appchofer.bbdd.ProgramaAsicamDao;
import cl.genesys.appchofer.entities.Cartolas;
import cl.genesys.appchofer.entities.ListaValores;
import cl.genesys.appchofer.entities.OrdenesTransporte;
import cl.genesys.appchofer.entities.ProgramaAsicam;
import cl.genesys.appchofer.entities.RespuestaJson;
import cl.genesys.appchofer.entities.VolleySingleton;
import cl.genesys.appchofer.server.FtpUser;
import cl.genesys.appchofer.utilities.AsyncProcesarSqlChoferTask;
import cl.genesys.appchofer.utilities.Constantes;
import cl.genesys.appchofer.utilities.GPS;
import cl.genesys.appchofer.utilities.ServicioConectado;
import cl.genesys.appchofer.utilities.ServicioGPS;
import cl.genesys.appchofer.utilities.ServicioOTDisponible;
import cl.genesys.appchofer.utilities.Utility;
import cl.genesys.appchofer.webapi.FetchDataListener;
import cl.genesys.appchofer.webapi.POSTAPIRequest;
import cl.genesys.appchofer.webapi.RequestQueueService;
import cl.genesys.appchofer.wifi.WiFiDirectActivity;

import static cl.genesys.appchofer.layout.GDE.btnConfirmarGDE;
import static cl.genesys.appchofer.layout.GDE.tv_confirma_gde;
import static cl.genesys.appchofer.layout.OT.llAutoCargante;
import static cl.genesys.appchofer.utilities.ApManager.mReservation;

public class MainActivity extends AppCompatActivity {
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_ACTION = "http://tempuri.org/";

    public static RadioButton rdbConectadoM;
    public static Button btnSolicitudAdicional;
    public static FloatingActionButton fabUpdate;
    public static ScrollView srvSinOt;
    public static ProgressDialog pDialog;
    public static Context context;
    public static SharedPreferences sharedPref;
    public static TextView txEmpresa, tvPatente, tv_sinOT, tv_update, tx_viaje_iniciado, tx_version_mainactivity;
    private ViewPager mViewPager;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static String patente, confirmaGDE = "0", viaje_destino = "0", tipoCamion = "1";
    public static TabLayout tabLayout;
    static AlertDialog alert = null;
    /*ImageView imagenCodigo;*/
    static WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    public static Boolean AlertInfo = false;
    public ImageButton imgShared, imgSinc;
    private static final int ALTURA_CODIGO = 500, ANCHURA_CODIGO = 500;
    public static boolean isHotspotEnabled = false;


    //private WifiManager wifiManager;
    static WifiConfiguration currentConfig;
    static WifiManager.LocalOnlyHotspotReservation hotspotReservation;

    public static void turnOffHotspot() {
        if (hotspotReservation != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                hotspotReservation.close();
            }
            isHotspotEnabled = false;
        }
    }

    public static void turnOnHotspot() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                wifiManager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {

                    @Override
                    public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                        super.onStarted(reservation);
                        hotspotReservation = reservation;
                        currentConfig = hotspotReservation.getWifiConfiguration();

                        Log.v("DANG", currentConfig.preSharedKey + " " + currentConfig.SSID);

                        hotspotDetaisDialog();
                        FsService.start();
                        File chrootDir = new File(Environment.getExternalStorageDirectory() + "/cl.genesys.appchofer/recepcion/");
                        if (!chrootDir.exists()) {
                            chrootDir.mkdirs();
                        }
                        FtpUser user = new FtpUser("ftp2", "ftp2", Environment.getExternalStorageDirectory() + "/cl.genesys.appchofer/recepcion");
                        FsSettings.addUser(user);
                        isHotspotEnabled = true;
                    }

                    @Override
                    public void onStopped() {
                        super.onStopped();
                        Log.v("DANG", "Local Hotspot Stopped");
                        isHotspotEnabled = false;
                    }

                    @Override
                    public void onFailed(int reason) {
                        super.onFailed(reason);
                        Log.v("DANG", "Local Hotspot failed to start");
                        isHotspotEnabled = false;
                    }
                }, new Handler());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void hotspotDetaisDialog() {

        //Utility.ShowAlertDialog(context, "Conexión", "Conexión: SSID:" + currentConfig.SSID + " Pass:" + currentConfig.preSharedKey);

    }

    public static void validarOTInactiva(final String _OT) {
        try {
            if (Utility.isOnline(context)) {
                POSTAPIRequest postapiRequest = new POSTAPIRequest();
                String url = Constantes.URL_WS_DEFECTO + "GET_OT_ANULADA";
                JSONObject params = new JSONObject();

                try {
                    params.put("OT", Utility.IsNull(_OT, ""));
                    params.put("SESSION", Utility.getPreferenciaObject("session", context));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postapiRequest.request(context, new FetchDataListener() {
                    @Override
                    public void onFetchComplete(JSONObject data) {
                        //Fetch Complete. Now stop progress bar  or loader
                        //you started in onFetchStart
                        //RequestQueueService.cancelProgressDialog();
                        try {
                            if (pDialog != null) {
                                pDialog.hide();
                                pDialog.dismiss();
                            }
                            //Now check result sent by our POSTAPIRequest class
                            if (data != null) {
                                if (data.has("success")) {
                                    boolean success = data.getBoolean("success");
                                    if (success == true) {
                                        JSONObject _SESSION = data.getJSONObject("SESSION");
                                        if (Utility.validaTokenSession(_SESSION) == false) {
                                            Utility.ShowMessage(context, "Error al rescatar información de sesión.");
                                        } else {
                                            Utility.setPreferencia("session", _SESSION, context);
                                            try {
                                                JSONArray jsonArray = null;
                                                //JSONObject json = new JSONObject(response);
                                                jsonArray = data.getJSONArray("vTabla");
                                                JSONObject jsonConfig = jsonArray.getJSONObject(0);
                                                if (jsonConfig.getInt("C") == 1) {
                                                    final String numero_OT = _OT;
                                                    OrdenesTransporteDao o = new OrdenesTransporteDao(context);
                                                    o.open();
                                                    o.deleteOrdenTransporte(numero_OT);
                                                    o.close();
                                                    SharedPreferences.Editor editor = sharedPref.edit();
                                                    editor.remove("OT");
                                                    editor.remove("OT_confirma");
                                                    editor.remove("GDE");
                                                    editor.remove("confirmaGDE");
                                                    editor.remove("RECEPID");
                                                    try {
                                                        editor.remove("doc_envio");
                                                    } catch (Exception e) {

                                                    }
                                                    try {
                                                        editor.remove("viaje_destino");
                                                    } catch (Exception e) {

                                                    }
                                                    try {
                                                        editor.remove("documento_subido");
                                                    } catch (Exception e) {

                                                    }
                                                    editor.commit();
                                                    tvPatente.setBackgroundResource(R.drawable.patente_radius);
                                                    try {
                                                        // Eliminar carpetas

                                                        String folder;
                                                        folder = Utility.RUTA_ENVIO(context);
                                                        File directory = new File(folder).getAbsoluteFile();
                                                        if (directory.exists()) {
                                                            String[] children = directory.list();
                                                            for (int i = 0; i < children.length; i++) {
                                                                new File(directory, children[i]).delete();
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    try {
                                                        // Eliminar carpetas
                                                        String folder;
                                                        folder = Utility.RUTA_RECEPCION(context);
                                                        File directory = new File(folder).getAbsoluteFile();
                                                        if (directory.exists()) {
                                                            String[] children = directory.list();
                                                            for (int i = 0; i < children.length; i++) {
                                                                new File(directory, children[i]).delete();
                                                            }
                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    try {
                                                        String folder = Utility.RUTA_CONFIRMACION(context);

                                                        //Create androiddeft folder if it does not exist
                                                        File directory = new File(folder).getAbsoluteFile();
                                                        if (directory.exists()) {
                                                            directory.delete();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    try {
                                                        btnConfirmarGDE.setVisibility(View.GONE);
                                                    }catch (Exception e) {

                                                    }

                                                    btnSolicitudAdicional.setVisibility(View.VISIBLE);
                                                    fabUpdate.show(); //.setVisibility(View.VISIBLE);
                                                    tv_sinOT.setVisibility(View.VISIBLE);
                                                    tv_update.setVisibility(View.VISIBLE);

                                                    // Esta bien
                                                    tx_viaje_iniciado.setText("Viaje no iniciado");
                                                    tx_viaje_iniciado.setBackgroundColor(0xFFCC0000);
                                                    srvSinOt.setVisibility(View.VISIBLE);

                                                    MainActivity.validarOT(0);
                                                    GDE.limpiar();
                                                    presentacion.limpiar();
                                                    OT.limpiar();
                                                    Utility.ShowAlertDialog(context, "Información", "OT Anulada");
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
                                            }
                                        }
                                    } else {
                                        String message = data.getString("message");
                                        Utility.ShowMessage(context, message);
                                    }
                                }
                            } else {
                                Utility.ShowMessage(context, "Error al rescatar información de sistema central.");

                            }
                        } catch (Exception e) {
                            Utility.ShowMessage(context, "Ocurrió un error inesperado." + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFetchFailure(String msg) {
                        //RequestQueueService.cancelProgressDialog();
                        Utility.ShowMessageWebApi(context, msg);
                        if (pDialog != null) {
                            pDialog.hide();
                            pDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFetchStart() {

                        //RequestQueueService.showProgressDialog((Activity) context);
                    }
                }, params, url, null);
            } else {
                Utility.ShowMessage(context, "Error, verificar conexión a internet.");
            }

        } catch (Exception ex) {
            Utility.ShowMessage(context, ex.getMessage());
        }
//            RequestQueue queue = VolleySingleton.getIntanciaVolley(context).getRequestQueue();
//
//            String url = Constantes.URL_WS_DEFECTO + "GET_OT_ANULADA?" +
//                    "numero_ot=" + _OT +
//                    "&CODIGO=" + Constantes.token;
//
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                    new Response.Listener<String>() {
//
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONArray jsonArray = null;
//                                JSONObject json = new JSONObject(response);
//
//                                boolean success = json.getBoolean("success");
//
//                                if (success == true) {
//
//                                    jsonArray = json.getJSONArray("dataset");
//
//                                    JSONObject jsonConfig = jsonArray.getJSONObject(0);
//
//                                    if (jsonConfig.getInt("C") == 1) {
//                                        final String numero_OT = _OT;
//                                        // ot anulada
//                                        // código para eliminar ot y gde del sistema
//                                        //Toast.makeText(context, "OT anulada", Toast.LENGTH_SHORT).show();
//
//                                        OrdenesTransporteDao o = new OrdenesTransporteDao(context);
//
//                                        o.open();
//                                        o.deleteOrdenTransporte(numero_OT);
//                                        o.close();
//
//                                        SharedPreferences.Editor editor = sharedPref.edit();
//
//                                        editor.remove("OT");
//                                        editor.remove("OT_confirma");
//                                        editor.remove("GDE");
//                                        editor.remove("confirmaGDE");
//                                        editor.remove("RECEPID");
//                                        try {
//                                            editor.remove("doc_envio");
//                                        } catch (Exception e) {
//
//                                        }
//                                        try {
//                                            editor.remove("viaje_destino");
//                                        } catch (Exception e) {
//
//                                        }
//                                        try {
//                                            editor.remove("documento_subido");
//                                        } catch (Exception e) {
//
//                                        }
//                                        editor.commit();
//                                        tvPatente.setBackgroundResource(R.drawable.patente_radius);
//                                        try {
//                                            // Eliminar carpetas
//
//                                            String folder;
//                                            folder = Utility.RUTA_ENVIO(context);
//                                            File directory = new File(folder).getAbsoluteFile();
//                                            if (directory.exists()) {
//                                                String[] children = directory.list();
//                                                for (int i = 0; i < children.length; i++) {
//                                                    new File(directory, children[i]).delete();
//                                                }
//                                            }
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        try {
//                                            // Eliminar carpetas
//                                            String folder;
//                                            folder = Utility.RUTA_RECEPCION(context);
//                                            File directory = new File(folder).getAbsoluteFile();
//                                            if (directory.exists()) {
//                                                String[] children = directory.list();
//                                                for (int i = 0; i < children.length; i++) {
//                                                    new File(directory, children[i]).delete();
//                                                }
//                                            }
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        try {
//                                            String folder = Utility.RUTA_CONFIRMACION(context);
//
//                                            //Create androiddeft folder if it does not exist
//                                            File directory = new File(folder).getAbsoluteFile();
//                                            if (directory.exists()) {
//                                                directory.delete();
//                                            }
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        btnConfirmarGDE.setVisibility(View.GONE);
//                                        btnSolicitudAdicional.setVisibility(View.VISIBLE);
//                                        fabUpdate.show(); //.setVisibility(View.VISIBLE);
//                                        tv_sinOT.setVisibility(View.VISIBLE);
//                                        tv_update.setVisibility(View.VISIBLE);
//
//                                        // Esta bien
//                                        tx_viaje_iniciado.setText("Viaje no iniciado");
//                                        tx_viaje_iniciado.setBackgroundColor(0xFFCC0000);
//                                        srvSinOt.setVisibility(View.VISIBLE);
//
//                                        MainActivity.validarGDE();
//                                        MainActivity.validarOT(0);
//                                        GDE.limpiar();
//                                        presentacion.limpiar();
//                                        OT.limpiar();
//
//
//                                        Utility.ShowAlertDialog(context, "Información", "OT Anulada");
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                Utility.ShowMessage(context, "Error al rescatar información de sistema central.");
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Utility.ShowMessage(context, "Error al rescatar información de sistema central.");
//                        }
//                    });
//
//            queue.add(stringRequest);
    }

    public static void validarOTTerminada(final String _OT, double latitud, double longitude) {
        try {


            if (Utility.isOnline(context)) {


                POSTAPIRequest postapiRequest = new POSTAPIRequest();
                String url = Constantes.URL_WS_DEFECTO + "GET_OT_TERMINADA";
                JSONObject params = new JSONObject();
                try {
                    params.put("OT", Utility.IsNull(_OT, ""));
                    params.put("LONGITUD", Utility.IsNull(longitude, 0.0));
                    params.put("LATITUD", Utility.IsNull(latitud, 0.0));
                    params.put("SESSION", Utility.getPreferenciaObject("session", context));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postapiRequest.request(context, new FetchDataListener() {
                    @Override
                    public void onFetchComplete(JSONObject data) {
                        //Fetch Complete. Now stop progress bar  or loader
                        //you started in onFetchStart

                        try {
                            //Now check result sent by our POSTAPIRequest class
                            if (data != null) {
                                if (data.has("success")) {
                                    boolean success = data.getBoolean("success");
                                    if (success == true) {
                                        JSONObject _SESSION = data.getJSONObject("SESSION");
                                        if (Utility.validaTokenSession(_SESSION) == false) {
                                            Utility.ShowMessage(context, "Error al rescatar información de sesión.");
                                        } else {
                                            Utility.setPreferencia("session", _SESSION, context);
                                            try {
                                                //LLENAR RESPUESTA
                                                JSONArray jsonArray = null;
                                                jsonArray = data.getJSONArray("vTabla");
                                                JSONObject jsonConfig = jsonArray.getJSONObject(0);
                                                if (jsonConfig.getInt("C") == 1) {
                                                    final String numero_OT = _OT;
                                                    // ot anulada
                                                    // código para eliminar ot y gde del sistema
                                                    //Toast.makeText(context, "OT anulada", Toast.LENGTH_SHORT).show();
                                                    Utility.ShowAlertDialog(context, "Información", "OT finalizada correctamente.");
                                                    OrdenesTransporteDao o = new OrdenesTransporteDao(context);

                                                    o.open();
                                                    o.deleteOrdenTransporte(numero_OT);
                                                    o.close();

                                                    SharedPreferences.Editor editor = sharedPref.edit();

                                                    editor.remove("OT");
                                                    editor.remove("OT_confirma");
                                                    editor.remove("GDE");
                                                    editor.remove("confirmaGDE");
                                                    editor.remove("RECEPID");
                                                    try {
                                                        editor.remove("doc_envio");
                                                    } catch (Exception e) {

                                                    }

                                                    try {
                                                        editor.remove("viaje_destino");
                                                    } catch (Exception e) {

                                                    }

                                                    try {
                                                        editor.remove("documento_subido");
                                                    } catch (Exception e) {

                                                    }
                                                    editor.commit();
                                                    tvPatente.setBackgroundResource(R.drawable.patente_radius);
                                                    try {
                                                        // Eliminar carpetas
                                                        String folder;
                                                        folder = Constantes.RUTA_ENVIO;
                                                        File directory = new File(folder).getAbsoluteFile();
                                                        if (directory.exists()) {
                                                            String[] children = directory.list();
                                                            for (int i = 0; i < children.length; i++) {
                                                                new File(directory, children[i]).delete();
                                                            }
                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    try {
                                                        // Eliminar carpetas
                                                        String folder;
                                                        folder = Constantes.RUTA_RECEPCION;
                                                        File directory = new File(folder).getAbsoluteFile();
                                                        if (directory.exists()) {
                                                            String[] children = directory.list();
                                                            for (int i = 0; i < children.length; i++) {
                                                                new File(directory, children[i]).delete();
                                                            }
                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    try {
                                                        String folder = Utility.RUTA_CONFIRMACION(context);

                                                        //Create androiddeft folder if it does not exist
                                                        File directory = new File(folder).getAbsoluteFile();
                                                        if (directory.exists()) {
                                                            directory.delete();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    btnSolicitudAdicional.setVisibility(View.VISIBLE);
                                                    try{
                                                        btnConfirmarGDE.setVisibility(View.GONE);
                                                    }catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    fabUpdate.show();  //.setVisibility(View.VISIBLE);
                                                    tv_sinOT.setVisibility(View.VISIBLE);
                                                    tv_update.setVisibility(View.VISIBLE);

                                                    tx_viaje_iniciado.setText("Viaje no iniciado");
                                                    tx_viaje_iniciado.setBackgroundColor(0xFFCC0000);
                                                    srvSinOt.setVisibility(View.VISIBLE);

                                                    MainActivity.validarOT(0);
                                                    GDE.limpiar();
                                                    presentacion.limpiar();
                                                    OT.limpiar();
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
                                            }
                                        }

                                    } else {
                                        String message = data.getString("message");
                                        Utility.ShowMessage(context, message);
                                    }
                                }
                            } else {
                                Utility.ShowMessage(context, "Error al rescatar información de sistema central.");

                            }
                        } catch (Exception e) {
                            Utility.ShowMessage(context, "Ocurrió un error inesperado." + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFetchFailure(String msg) {


                    }

                    @Override
                    public void onFetchStart() {

                    }
                }, params, url, null);

//            RequestQueue queue = VolleySingleton.getIntanciaVolley(context).getRequestQueue();
//
//            String url = Constantes.URL_WS_DEFECTO + "GET_OT_TERMINADA?" +
//                    "numero_ot=" + _OT +
//                    "&longitud=" + longitude +
//                    "&latitud=" + latitud +
//                    "&CODIGO=" + Constantes.token;
//
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                    new Response.Listener<String>() {
//
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONArray jsonArray = null;
//                                JSONObject json = new JSONObject(response);
//
//                                boolean success = json.getBoolean("success");
//
//                                if (success == true) {
//
//                                    jsonArray = json.getJSONArray("dataset");
//
//                                    JSONObject jsonConfig = jsonArray.getJSONObject(0);
//
//                                    if (jsonConfig.getInt("C") == 1) {
//                                        final String numero_OT = _OT;
//                                        // ot anulada
//                                        // código para eliminar ot y gde del sistema
//                                        //Toast.makeText(context, "OT anulada", Toast.LENGTH_SHORT).show();
//                                        Utility.ShowAlertDialog(context, "Información", "OT finalizada correctamente.");
//                                        OrdenesTransporteDao o = new OrdenesTransporteDao(context);
//
//                                        o.open();
//                                        o.deleteOrdenTransporte(numero_OT);
//                                        o.close();
//
//                                        SharedPreferences.Editor editor = sharedPref.edit();
//
//                                        editor.remove("OT");
//                                        editor.remove("OT_confirma");
//                                        editor.remove("GDE");
//                                        editor.remove("confirmaGDE");
//                                        editor.remove("RECEPID");
//                                        try {
//                                            editor.remove("doc_envio");
//                                        } catch (Exception e) {
//
//                                        }
//
//                                        try {
//                                            editor.remove("viaje_destino");
//                                        } catch (Exception e) {
//
//                                        }
//
//                                        try {
//                                            editor.remove("documento_subido");
//                                        } catch (Exception e) {
//
//                                        }
//                                        editor.commit();
//                                        tvPatente.setBackgroundResource(R.drawable.patente_radius);
//                                        try {
//                                            // Eliminar carpetas
//                                            String folder;
//                                            folder = Constantes.RUTA_ENVIO;
//                                            File directory = new File(folder).getAbsoluteFile();
//                                            if (directory.exists()) {
//                                                String[] children = directory.list();
//                                                for (int i = 0; i < children.length; i++) {
//                                                    new File(directory, children[i]).delete();
//                                                }
//                                            }
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        try {
//                                            // Eliminar carpetas
//                                            String folder;
//                                            folder = Constantes.RUTA_RECEPCION;
//                                            File directory = new File(folder).getAbsoluteFile();
//                                            if (directory.exists()) {
//                                                String[] children = directory.list();
//                                                for (int i = 0; i < children.length; i++) {
//                                                    new File(directory, children[i]).delete();
//                                                }
//                                            }
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        try {
//                                            String folder = Utility.RUTA_CONFIRMACION(context);
//
//                                            //Create androiddeft folder if it does not exist
//                                            File directory = new File(folder).getAbsoluteFile();
//                                            if (directory.exists()) {
//                                                directory.delete();
//                                            }
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        btnSolicitudAdicional.setVisibility(View.VISIBLE);
//                                        btnConfirmarGDE.setVisibility(View.GONE);
//                                        fabUpdate.show();  //.setVisibility(View.VISIBLE);
//                                        tv_sinOT.setVisibility(View.VISIBLE);
//                                        tv_update.setVisibility(View.VISIBLE);
//
//                                        // Esta bien
//                                        tx_viaje_iniciado.setText("Viaje no iniciado");
//                                        tx_viaje_iniciado.setBackgroundColor(0xFFCC0000);
//                                        srvSinOt.setVisibility(View.VISIBLE);
//
//                                        MainActivity.validarGDE();
//                                        MainActivity.validarOT(0);
//                                        GDE.limpiar();
//                                        presentacion.limpiar();
//                                        OT.limpiar();
//
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                Utility.ShowMessage(context, "Error al rescatar información de sistema central.");
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Utility.ShowMessage(context, "Error al rescatar información de sistema central.");
//                        }
//
//                    });
//
//            queue.add(stringRequest);
            }
        } catch (Exception ex) {
            Utility.ShowMessage(context, ex.getMessage());
        }
    }

    public static void CargaWebService() {
        try {
            GPS gps = new GPS(context);
            if (gps.isGPSEnabled) {
                double latitud = gps.getLatitude();
                double longitude = gps.getLongitude();

                String _OT = sharedPref.getString("OT", "");
                String _patente = sharedPref.getString("patente", "");
                String fechaEnvioWifi = ""; // sharedPref.getString("fechaEnvioWifi", "");
                OrdenesTransporteDao o = new OrdenesTransporteDao(context);
                o.open();
                String MensajeEstado = "";
                OrdenesTransporte orden = null;
                orden = o.getOrdenTransporteById(context, _OT);
                fechaEnvioWifi = orden.getHora_fecha_transmision();
                String dirPath = Environment.getExternalStorageDirectory() + "/" + context.getPackageName() + "/recepcion/";
                File file = new File(dirPath + "/GDE_" + _OT + "_" + orden.getFolio_dte() + "_" + _patente + ".xml");
                String xml = obtieneXML(file);

                POSTAPIRequest postapiRequest = new POSTAPIRequest();
                String url = Constantes.URL_WS_DEFECTO + "SP_ACTUALIZA_OT";
                JSONObject params = new JSONObject();
                try {
                    //params.put("CODIGO", Utility.IsNull("03]vCtL54kX8Hxjs4bb[Tzjn7gonRMMSLq46LRdc", ""));
                    params.put("OT", Utility.IsNull(Integer.parseInt(_OT), 0));
                    byte[] data = xml.getBytes("UTF-8");
                    String xml64 = Base64.encodeToString(data, Base64.DEFAULT);
                    params.put("XML",  xml64.toString());
                    params.put("PLANTA_DEST", Utility.IsNull(Integer.parseInt(orden.getPlanta_destino()), 0));
                    params.put("DEST_COD", Utility.IsNull(Integer.parseInt(orden.getDestino_codigo()), 0));
                    params.put("DEST_DSC", Utility.IsNull(orden.getDestino_dsc(), ""));
                    params.put("CHOF_RUT", Utility.IsNull(Integer.parseInt(orden.getRut_chofer()), 0));
                    params.put("CHOF_DSC", Utility.IsNull(orden.getNombre_chofer(), ""));
                    params.put("PROD_ID", Utility.IsNull(orden.getProducto_codigo(), ""));
                    params.put("ID_GRUA", Utility.IsNull(orden.getGrua(), ""));
                    params.put("LATITUD", Utility.IsNull(String.valueOf(gps.getLatitude()), ""));
                    params.put("LONGITUD", Utility.IsNull(String.valueOf(gps.getLongitude()), ""));
                    params.put("LLEGADA_WIFI", Utility.IsNull(fechaEnvioWifi, ""));
                    params.put("SALIDA_WIFI", Utility.IsNull(orden.getHora_salida_origen_wifi(), ""));
                    params.put("RODAL", Utility.IsNull(orden.getRodal().split("-")[0], ""));
                    params.put("SESSION", Utility.getPreferenciaObject("session", context));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postapiRequest.request(context, new FetchDataListener() {
                    @Override
                    public void onFetchComplete(JSONObject data) {
                        //Fetch Complete. Now stop progress bar  or loader
                        //you started in onFetchStart
                       // RequestQueueService.cancelProgressDialog();
                        try {
                            if (pDialog != null) {
                                pDialog.hide();
                                pDialog.dismiss();
                            }
                            //Now check result sent by our POSTAPIRequest class
                            if (data != null) {
                                if (data.has("success")) {
                                    boolean success = data.getBoolean("success");
                                    if (success == true) {
                                        JSONObject _SESSION = data.getJSONObject("SESSION");
                                        if (Utility.validaTokenSession(_SESSION) == false) {
                                            Utility.ShowMessage(context, "Error al rescatar información de sesión.");
                                        } else {
                                            Utility.setPreferencia("session", _SESSION, context);
                                            try {
                                                //LLENAR RESPUESTA
                                                JSONObject resultado = data.getJSONObject("resultado");
                                                if (resultado != null) {
                                                    String _SALIDA = Utility.IsNull(resultado.getString("SALIDA"), "");
                                                    String plantaCTAC = resultado.getString("PLANTA_CTAC");


                                                    String CAAC_GEO_SUP_IZQ_X = resultado.getString("CAAC_GEO_SUP_IZQ_X");
                                                    String CAAC_GEO_SUP_IZQ_Y = resultado.getString("CAAC_GEO_SUP_IZQ_Y");
                                                    String CAAC_GEO_INF_DER_X = resultado.getString("CAAC_GEO_INF_DER_X");
                                                    String CAAC_GEO_INF_DER_Y = resultado.getString("CAAC_GEO_INF_DER_Y");


                                                    sharedPref = context.getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPref.edit();
                                                    editor.putString("documento_subido", "1");
                                                    editor.putString("plantaCTAC", plantaCTAC);
                                                    editor.commit();

                                                    editor = sharedPref.edit();
                                                    editor.remove("fechaEnvioWifi");
                                                    editor.commit();

                                                    OrdenesTransporteDao o = new OrdenesTransporteDao(context);
                                                    o.open();
                                                    o.coordenadas(CAAC_GEO_SUP_IZQ_X, CAAC_GEO_SUP_IZQ_Y, CAAC_GEO_INF_DER_X, CAAC_GEO_INF_DER_Y);
                                                    o.close();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
                                            }
                                        }

                                    } else {
                                        String message = data.getString("message");
                                        //Utility.ShowMessage(context, message);
                                    }
                                }
                            } else {
                                //Utility.ShowMessage(context, "Error al rescatar información de sistema central.");

                            }
                        } catch (Exception e) {
                            //tility.ShowMessage(context, "Ocurrió un error inesperado." + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFetchFailure(String msg) {
                       // RequestQueueService.cancelProgressDialog();
                        Utility.ShowMessageWebApi(context, msg);
                        if (pDialog != null) {
                            pDialog.hide();
                            pDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFetchStart() {

                        //RequestQueueService.showProgressDialog((Activity) context);
                    }
                }, params, url, new DefaultRetryPolicy(60000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


//            double latitud = gps.getLatitude();
//            double longitude = gps.getLongitude();
//            try {
//                sharedPref = context.getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
//                String _OT = sharedPref.getString("OT", "");
//                String _patente = sharedPref.getString("patente", "");
//                String fechaEnvioWifi = ""; // sharedPref.getString("fechaEnvioWifi", "");
//                OrdenesTransporteDao o = new OrdenesTransporteDao(context);
//                o.open();
//                String MensajeEstado = "";
//                String URL = "http://desarrollocmpc.genesys.cl:112/ws_sim_chofer/WSsimchoferdesa.asmx";
//                OrdenesTransporte orden = null;
//
//                orden = o.getOrdenTransporteById(context, _OT);
//                fechaEnvioWifi = orden.getHora_fecha_transmision();
//                String dirPath = Environment.getExternalStorageDirectory() + "/" + context.getPackageName() + "/recepcion/";
//
//                File file = new File(dirPath + "/GDE_" + _OT + "_" + orden.getFolio_dte() + "_" + _patente + ".xml");
//                String xml = obtieneXML(file);
//
//                String metodo = "SP_ACTUALIZA_OT";
//                SoapObject request = new SoapObject(NAMESPACE, metodo);
//                PropertyInfo paramPI;
//
//                paramPI = new PropertyInfo();
//                paramPI.setName("CODIGO");
//                paramPI.setValue("03]vCtL54kX8Hxjs4bb[Tzjn7gonRMMSLq46LRdc");
//                paramPI.setType(String.class);
//                request.addProperty(paramPI);
//
//                paramPI = new PropertyInfo();
//                paramPI.setName("PN_OT");
//                paramPI.setValue(Integer.parseInt(_OT));
//                paramPI.setType(String.class);
//                request.addProperty(paramPI);
//
//                paramPI = new PropertyInfo();
//                paramPI.setName("PN_XML");
//                paramPI.setValue(xml);
//                paramPI.setType(String.class);
//                request.addProperty(paramPI);
//
//                paramPI = new PropertyInfo();
//                paramPI.setName("PN_PLANTA_DEST");
//                paramPI.setValue(Integer.parseInt(orden.getPlanta_destino()));
//                paramPI.setType(String.class);
//                request.addProperty(paramPI);
//
//                paramPI = new PropertyInfo();
//                paramPI.setName("PN_DEST_COD");
//                paramPI.setValue(Integer.parseInt(orden.getDestino_codigo()));
//                paramPI.setType(String.class);
//                request.addProperty(paramPI);
//
//                paramPI = new PropertyInfo();
//                paramPI.setName("PV_DEST_DSC");
//                paramPI.setValue(orden.getDestino_dsc());
//                paramPI.setType(String.class);
//                request.addProperty(paramPI);
//
//                paramPI = new PropertyInfo();
//                paramPI.setName("PN_CHOF_RUT");
//                paramPI.setValue(Integer.parseInt(orden.getRut_chofer()));
//                paramPI.setType(String.class);
//                request.addProperty(paramPI);
//
//                paramPI = new PropertyInfo();
//                paramPI.setName("PV_CHOF_DSC");
//                paramPI.setValue(orden.getNombre_chofer());
//                paramPI.setType(String.class);
//                request.addProperty(paramPI);
//
//                paramPI = new PropertyInfo();
//                paramPI.setName("PV_PROD_ID");
//                paramPI.setValue(orden.getProducto_codigo());
//                paramPI.setType(String.class);
//                request.addProperty(paramPI);
//
//                paramPI = new PropertyInfo();
//                paramPI.setName("latitud");
//                paramPI.setValue(String.valueOf(gps.getLatitude()));
//                paramPI.setType(String.class);
//                request.addProperty(paramPI);
//
//                paramPI = new PropertyInfo();
//                paramPI.setName("longitud");
//                paramPI.setValue(String.valueOf(gps.getLongitude()));
//                paramPI.setType(String.class);
//                request.addProperty(paramPI);
//
//                paramPI = new PropertyInfo();
//                paramPI.setName("PV_ID_GRUA");
//                paramPI.setValue(orden.getGrua());
//                paramPI.setType(String.class);
//                request.addProperty(paramPI);
//
//                paramPI = new PropertyInfo();
//                paramPI.setName("llegada_wifi");
//                paramPI.setValue(fechaEnvioWifi);
//                paramPI.setType(String.class);
//                request.addProperty(paramPI);
//
//                paramPI = new PropertyInfo();
//                paramPI.setName("salida_wifi");
//                paramPI.setValue(orden.getHora_salida_origen_wifi());
//                paramPI.setType(String.class);
//                request.addProperty(paramPI);
//
//                paramPI = new PropertyInfo();
//                paramPI.setName("PN_RODAL");
//                paramPI.setValue(orden.getRodal().split("-")[0]);
//                paramPI.setType(String.class);
//                request.addProperty(paramPI);
//
//                // Crear envelope
//                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//                        SoapEnvelope.VER11);
//                envelope.dotNet = true;
//                // Setear objeto SOAP de salida
//                envelope.setOutputSoapObject(request);
//                envelope.addMapping(NAMESPACE, "RespuestaJson", new RespuestaJson().getClass());
//                // Crear llamada HTTP
//                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 1000000);
//
//                try {
//
//                    System.setProperty("http.keepAlive", "false");
//                    androidHttpTransport.call(SOAP_ACTION + metodo, envelope);
//                    // Obtener respuesta
//                    SoapObject response = (SoapObject) envelope.getResponse();
//                    if (response != null) {
//                        RespuestaJson res = Utility.getResponseWS(response);
//                        if (res.getSuccess() == false) {
//                            throw new Exception(res.getMessage());
//                        } else {
//                            sharedPref = context.getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPref.edit();
//                            editor.putString("documento_subido", "1");
//
//                            editor.commit();
//                            editor = sharedPref.edit();
//                            editor.remove("fechaEnvioWifi");
//                            editor.commit();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                o.close();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            } else {
                MainActivity.AlertNoGps();
            }
        } catch (Exception ex) {
            Utility.ShowMessage(context, ex.getMessage());
        }
    }

    public static class ActualizaOTXMLServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            CargaWebService();
            return null;
        }

        @Override
        protected void onPostExecute(Void params) {

        }

    }

    private static String obtieneXML(File archivo) {
        String texto = "";
        try {
            //File archivo = new File("/ruta/datos.xml");
            String cadena;
            FileReader f = new FileReader(archivo);
            BufferedReader b = new BufferedReader(f);
            int cont = 0;
            while ((cadena = b.readLine())!=null) {
                if (cont > 0) {
                    texto += "\r";
                }
                cont++;
                texto += cadena;
            }
            b.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return texto;
    }

    private boolean isLocationPermissionEnable() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        context = this;

        /*OrdenesTransporteDao oDao = new OrdenesTransporteDao(context);
        try {
            oDao.open();
            oDao.coordenadas(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        oDao.close();*/
//        OrdenesTransporteDao oDao = new OrdenesTransporteDao(context);
//        oDao.modificaHoraOrigenWifi(context);

        //Toast.makeText(getApplicationContext(), "sdfvnsgv", Toast.LENGTH_SHORT).show();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        rdbConectadoM = findViewById(R.id.rdbConectadoM);
        txEmpresa = findViewById(R.id.txEmpresa);
        tx_viaje_iniciado = findViewById(R.id.tx_viaje_iniciado);
        tvPatente = findViewById(R.id.tvPatente);
        tv_sinOT = findViewById(R.id.tv_sinOT);
        tv_update = findViewById(R.id.tv_update);
        srvSinOt = findViewById(R.id.srvSinOt);
        imgShared = findViewById(R.id.imgShared);
        /*imagenCodigo = findViewById(R.id.imagenCodigo);*/
        imgSinc = findViewById(R.id.imgSinc);


        try {
            Utility.solicitar_permisos(context, new String[]{
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            });
            // Manifest.permission.READ_PHONE_NUMBERS,
        } catch (Exception ex) {
            Log.d("SOLITAR PERMISOS", ex.getMessage());
        }
        sharedPref = getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
        patente = sharedPref.getString("patente", "");
        tipoCamion = sharedPref.getString("tipoCamion", "");

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("estado_viaje", "0");

        editor.commit();

//        validaViaje();

        if (!patente.equals("")) {

            confirmaGDE = sharedPref.getString("confirmaGDE", "");
            viaje_destino = sharedPref.getString("viaje_destino", "");
            llenarListaValores();
            //llenarPrgramaAsicam();
            /*try {
                descargaCartolas(context);
            } catch (IOException e) {
                Toast.makeText(context, "Error al descargar cartolas, favor validar", Toast.LENGTH_SHORT).show();
            }*/


            if (!isMyServiceRunning(ServicioConectado.class)) {
                Intent serviceIntent = new Intent(this, ServicioConectado.class);
                serviceIntent.putExtra("inputExtra", "ConvectorOT está en  funcionamiento");

                ContextCompat.startForegroundService(this, serviceIntent);
            }

            if (!isMyServiceRunning(ServicioOTDisponible.class)) {
                Intent service = new Intent(context, ServicioOTDisponible.class);
                startService(service);
            }



           /* service = new Intent(MainActivity.context, ServicioGPS.class);
            startService(service);*/

            tx_version_mainactivity = findViewById(R.id.tx_version_mainactivity);
            btnSolicitudAdicional = findViewById(R.id.btnSolicitudAdicional);
            btnSolicitudAdicional.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    validarOT(1);

                }
            });

            fabUpdate = findViewById(R.id.fabUpdate);
            fabUpdate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    validarOT(0);
                }
            });
            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //turnOnHotspot();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
            }


            imgShared.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            imgSinc.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //turnOnHotspot();
                }
            });

            MyPagerAdapter myPagerAdapter =
                    new MyPagerAdapter(
                            getSupportFragmentManager());
            mViewPager = (ViewPager) findViewById(R.id.viewPager);
            mViewPager.setAdapter(myPagerAdapter);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);

            tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(mViewPager);


            /*try {
                descargaCartolas(context);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            if (viaje_destino.equals("1"))
            {
                MainActivity.tvPatente.setBackgroundResource(R.drawable.patente_radius_iniciada);
                tx_viaje_iniciado.setText("Viaje a destino");
                tx_viaje_iniciado.setBackgroundColor(0xFF1ABC9C);
            }
            else if (confirmaGDE.equals("1")) {
                tx_viaje_iniciado.setText("Viaje a origen");
                tx_viaje_iniciado.setBackgroundColor(0xFF1ABC9C);
            } else {
                tx_viaje_iniciado.setText("Viaje no iniciado");
                tx_viaje_iniciado.setBackgroundColor(0xFFCC0000);
            }


            try {
                File file = new File(Constantes.RUTA_ENVIO);
                if (!file.exists()){
                    file.mkdirs();
                }

                File file2 = new File(Constantes.RUTA_RECEPCION);
                if (!file2.exists()){
                    file2.mkdirs();
                }

                if (tipoCamion.equals("2")){
                    File file3 = new File(Constantes.RUTA_RECEPCION_DESPACHO);
                    if (!file3.exists()){
                        file3.mkdirs();
                    }

                    File file4 = new File(Constantes.RUTA_ENVIO_DESPACHO);
                    if (!file4.exists()){
                        file4.mkdirs();
                    }

                    File file5 = new File(Constantes.RUTA_TERMINO_DESPACHO);
                    if (!file5.exists()){
                        file5.mkdirs();
                    }

                    File file6 = new File(Constantes.RUTA_SINC_DESPACHO);
                    if (!file6.exists()){
                        file6.mkdirs();
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        else {
            editor = sharedPref.edit();

            try {
                editor.remove("doc_envio");
            } catch (Exception e) {

            }
            try {
                editor.remove("viaje_destino");
            } catch (Exception e)  {

            }
            editor.commit();
        }
        //getJsonResponsePost();
        //MainActivity.actualizaDocServer();
        //CargaWebService();
        //new ActualizaOTXMLServer().execute();

        try {
            String menu_gde = getIntent().getStringExtra("menu_gde");
            if (menu_gde != null && menu_gde.equals("1")) {
                MainActivity.tabLayout.getTabAt(2).select();
            }
        } catch (Exception e) {

        }

        // Metodo que guarda el xml con acentos y caracteres especiales.
       /* String base64 = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iSVNPLTg4NTktMSI/PjxHREVfRk9STUlOX1NDPjxFbmNhYmV6YWRvPjxJZERvYz48VGlwb0RURT41MjwvVGlwb0RURT48Rm9saW8+Mjc5MjwvRm9saW8+PEZjaEVtaXM+MjAyMC0wMy0wOSAxNjoyMzo1NSA8L0ZjaEVtaXM+PC9JZERvYz48U3VjdXJzYWxfU0lJPkxPUyBBTkdFTEVTPC9TdWN1cnNhbF9TSUk+PFJlc29sX0ZFX1NJST5Gb3JtYXRvIEV4cHJlc3MgYXV0b3JpemFkbyBwb3IgUmVzLiBFeC4gTiA2MSwgZGUgZmVjaGEgMjIuMDYuMjAwNTwvUmVzb2xfRkVfU0lJPjxFbWlzb3I+PFJVVEVtaXNvcj4gOTEuNDQwLjAwMC03PC9SVVRFbWlzb3I+PFJ6blNvYz5GT1JFU1RBTCBNSU5JTkNPIFNwQTwvUnpuU29jPjxHaXJvRW1pcz5FeHRyYWNjacOzbiBkZSBNYWRlcmFzPC9HaXJvRW1pcz48RGlyT3JpZ2VuPkF2LkxhcyBJbmR1c3RyaWFzLCBQLlN0YXJrIDEwMDwvRGlyT3JpZ2VuPjxDbW5hT3JpZ2VuPkxPUyBBTkdFTEVTPC9DbW5hT3JpZ2VuPjwvRW1pc29yPjxSZWNlcHRvcj48UlVUUmVjZXA+OTY1MzIzMzAtOTwvUlVUUmVjZXA+PFJ6blNvY1JlY2VwPkNNUEMgUFVMUCBTcEEuPC9Sem5Tb2NSZWNlcD48R2lyb1JlY2VwPkZBQlJJQ0FDScOTTiBERSBDRUxVTE9TQSBZIFBBUEVMLCBDT01QUkEgWSBWRU5UQSBERTwvR2lyb1JlY2VwPjxEaXJSZWNlcD5BR1VTVElOQVMgMTM0MywgUElTTyAzIDEzNDM8L0RpclJlY2VwPjxDbW5hUmVjZXA+U0FOVElBR088L0NtbmFSZWNlcD48L1JlY2VwdG9yPjxUcmFuc3BvcnRlPjxSem5Tb2NUcmFucz5TT1RSQVNFUiBTLkEuPC9Sem5Tb2NUcmFucz48UlVUVHJhbnM+NzgwNTcwMDAtODwvUlVUVHJhbnM+PFBhdGVudGVDPkhMWkQ3MzwvUGF0ZW50ZUM+PFBhdGVudGVBPkdSQ0wyMTwvUGF0ZW50ZUE+PERlc3Rpbm8+PENvZERlc3Rpbm8+MTwvQ29kRGVzdGlubz48Tm9tZGVzdGlubz5MQUpBPC9Ob21kZXN0aW5vPjxEaXJEZXN0PkF2ZW5pZGEgQmFsbWFjZWRhICMgMzA8L0RpckRlc3Q+PENtbmFEZXN0PkxBSkE8L0NtbmFEZXN0PjwvRGVzdGlubz48L1RyYW5zcG9ydGU+PENhcmd1aW8+PFJ6blNvY0Nhcmd1aW8+U09UUkFTRVI8L1J6blNvY0Nhcmd1aW8+PFJVVEVFU1NDYXJndWlvPjc4MDU3MDAwLTg8L1JVVEVFU1NDYXJndWlvPjwvQ2FyZ3Vpbz48Q2FuY2hhT3JpZ2VuPjxDb2ROb21icmVPcmk+NzgwMCBGLkNPSUhVRUM8L0NvZE5vbWJyZU9yaT48Q29tdW5hT3JpPlFVSUxMRUNPPC9Db211bmFPcmk+PEZ1bmRvT3JpPjc5MzA8L0Z1bmRvT3JpPjwvQ2FuY2hhT3JpZ2VuPjxSb2RhbC1Sb2w+PFJvZGFsLz48Um9sLz48UGlsYS8+PFBlcmlvZG9jb3J0YS8+PC9Sb2RhbC1Sb2w+PC9FbmNhYmV6YWRvPjxEZXRhbGxlPjxOcm9MaW5EZXQvPjxQcm9kSXRlbT4wMTQxPC9Qcm9kSXRlbT48Tm1iSXRlbT5QLlJBRElBVEEgICAgICAgIFJQRyAyNDRjL2M8L05tYkl0ZW0+PFF0eUl0ZW0+MjY8L1F0eUl0ZW0+PFVubWRJdGVtPk0zPC9Vbm1kSXRlbT48UHJjSXRlbT4yMzExOTwvUHJjSXRlbT48TW9udG9JdGVtPjYwMTA5NDwvTW9udG9JdGVtPjwvRGV0YWxsZT48Q2VydGZvcj5DRVJURk9SIDEwMCU6Q0wwNC8wMDAyRk0gPC9DZXJ0Zm9yPjxGU0M+RlNDIDEwMCUgUkEtQ09DLTAwNTcyNiA8L0ZTQz48VG90YWxlcz48TW50TmV0bz42MDEwOTQ8L01udE5ldG8+PFRhc2FJVkE+MTk8L1Rhc2FJVkE+PElWQT4xMTQyMDg8L0lWQT48TW50VG90YWw+NzE1MzAyPC9NbnRUb3RhbD48L1RvdGFsZXM+PEZpcm1hRWxlY3Ryb25pY2FTSUk+PFRFRD48REQ+PFJFPiA5MS40NDAuMDAwLTc8L1JFPjxURD41MjwvVEQ+PEY+Mjc5MjwvRj48RkU+MjAyMC0wMy0wOSAxNjoyMzo1NSA8L0ZFPjxSUj45NjUzMjMzMC05PC9SUj48UlNSPkNNUEMgUFVMUCBTcEEuPC9SU1I+PE1OVD43MTUzMDI8L01OVD48SVQxPlJQRyAyNDRjL2M8L0lUMT48Q0FGIHZlcnNpb249IjEuMCI+PERBPjxSRT45MTQ0MDAwMC03PC9SRT48UlM+Rk9SRVNUQUwgTUlOSU5DTyBTUEE8L1JTPjxURD41MjwvVEQ+PFJORz48RD4yMjM2PC9EPjxIPjMyMzU8L0g+PC9STkc+PEZBPjIwMTktMDktMDY8L0ZBPjxSU0FQSz48TT55Z2xUSE5oQUg2TzBFVE1wMkljT3FsVFRKWEVvRGFiR3g5R2hzSEsyTTFVemlTWUk1Q2pGcmgzclRwbCtmTW84WW1WWkNscVROYkhBWmR6M0dzR25Wdz09PC9NPjxFPkF3PT08L0U+PC9SU0FQSz48SURLPjEwMDwvSURLPjwvREE+PEZSTUEgYWxnb3JpdG1vPSJTSEExd2l0aFJTQSI+ckMva2I5UkQyVVhsVTBRdG1NQ3VRVldxK1JlbHczSnpVSHl5TldRUkg2WVhTa2J5ZnFKdzI3MUtWVmpVY1RabzdhYXB1NTBxZTk1T0d4ZVdWTXY3eGc9PTwvRlJNQT48L0NBRj48VFNURUQ+MjAyMC0wMy0wOSAxNjoyMzo1NSA8L1RTVEVEPjwvREQ+PEZSTVQgYWxnb3JpdG1vPSJTSEExd2l0aFJTQSIvPjwvVEVEPjwvRmlybWFFbGVjdHJvbmljYVNJST48VG1zdEZpcm1hPjIwMjAtMDMtMDkgMTY6MjM6NTUgPC9UbXN0RmlybWE+PEdsb3NhX3RpbWJyZT5UaW1icmUgZWxlY3Ryb25pY28gU0lJPC9HbG9zYV90aW1icmU+PFJlc29sX1NJST5SZXMuICAgMTEzIGRlbCAyOS0wOS0yMDExIC0gVmVyaWZpcXVlIGRvY3VtZW50bzogd3d3LnNpaS5jbDwvUmVzb2xfU0lJPjxHbG9zYUVzcGVjaWFsMT5PcGVyYWNpb24gQ29uc3RpdHV5ZSBWZW50YTwvR2xvc2FFc3BlY2lhbDE+PEdsb3NhRXNwZWNpYWwyPlZhbG9yZXMgeSBjYW50aWRhZGVzIHNvbG8gcmVmZXJlbmNpYWxlcywgYSBjb25maXJtYXIgZW4gbGEgcmVjZXBjacOzbi48L0dsb3NhRXNwZWNpYWwyPjxQREY0MTdfRk9STUlOPjAwMDAwMDI3OTI3ODAwMjAyMDAzMDkxNjIzNTUwMDAwMDIwMTg3MDAwMTAxNDEwMDAwMDAyMDIwMDIyMzAwMDAwMDQxMDAwMDAwMDAwMDAwMDAwMjQ0MDAwMDEwMDkwMDAwMDAwMDAwMDAwMDA3ODA1NzAwMDAxODcwMDAwMjAyMDAzMDkwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDQxOTE0NDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDIzQTI1N0Y1NEE8L1BERjQxN19GT1JNSU4+PC9HREVfRk9STUlOX1NDPg==";
        ConversionBase64(base64);*/


       // API Google distance matrix

        //https://maps.googleapis.com/maps/api/distancematrix/json?origins=Seattle&destinations=San+Francisco&key=AIzaSyCzxeN4r_etRQKGx_h0u5VXl8k38eFIkUU


    }



    public static void ConversionBase64ToXML(String base64, String folder, String fileName) {

        try {
            byte[] data = Base64.decode(base64, Base64.DEFAULT);
            String text = new String(data);

            FileOutputStream fOut = new FileOutputStream(folder + fileName);
            OutputStreamWriter osw = new OutputStreamWriter(fOut, "windows-1252");

            osw.write(text);
            osw.flush();
            osw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static Document convertStringToXMLDocument(String xmlString)  {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void validaViaje() {
        sharedPref = context.getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
        String OT = sharedPref.getString("OT", "");
        String OT_confirma = sharedPref.getString("OT_confirma", "");
        String recepid = sharedPref.getString("RECEPID", "");
        String viaje_destino = sharedPref.getString("viaje_destino", "");
        String documento_subido = sharedPref.getString("documento_subido", "");
        String confirmaGDE = sharedPref.getString("confirmaGDE", "");
        String fechaEnvioWifi = sharedPref.getString("fechaEnvioWifi", "");
        String registroP = sharedPref.getString("registroP", "");
        String plantaCTAC = sharedPref.getString("plantaCTAC", "");


        File f = new File("/storage/emulated/0/cl.genesys.appchofer/recepcion/OT_" + OT + ".sql");
        File fc = new File("/storage/emulated/0/cl.genesys.appchofer/sinc/cartolas_produccion.sql");
        File fi = new File("/storage/emulated/0/cl.genesys.appchofer/sinc/transmision_qr.txt"); // este debe ser un archivo
        File fiW = new File("/storage/emulated/0/cl.genesys.appchofer/sinc/transmision_wifi.txt"); // este debe ser un archivo
        File fi2 = new File("/storage/emulated/0/cl.genesys.appchofer/recepcion/transmision_qr.txt"); // este debe ser un archivo
        String estado_viaje = "0";
            /* 0 -> viaje no iciado (rojo)
               1 -> viaje al origen (verde)
               2 -> viaje en origen (verde)
               3 -> viaje al destino (verde)
               4 -> viaje en destino (verde)
             */
        GPS gps = new GPS(context);





        // Validar OT cuando no tiene
        if (OT.equals("")) {
            MainActivity.validarOT(0);
        }
        else {
            // Validación GPS
            double latitud = 0;
            double longitude = 0;
            if (OT_confirma.equals("1")) {
                if (confirmaGDE.equals("1")) {
                    estado_viaje = "1";
                }
                MainActivity.llenarPrgramaAsicam();
                if (gps.isGPSEnabled) {

                    latitud = gps.getLatitude();
                    longitude = gps.getLongitude();
                    MainActivity.actualizaGPS(OT, latitud, longitude);
                } else {
                    MainActivity.AlertNoGps();
                }
                boolean tieneRecepcion = false, tieneEnvio = false;

                if (f.exists()) {
                    tieneRecepcion = true;
                }

                if (fc.exists()) {
                    tieneEnvio = true;
                }

                        /* if (fi.exists()) {
                            MainActivity.tx_viaje_iniciado.setText("Viaje en origen");
                        }*/
                if ((fi.exists() || fiW.exists()) && !tieneRecepcion) {
                    estado_viaje = "2";
                }else if ((fi.exists() || fiW.exists()) && tieneRecepcion)  {
                    turnOffHotspot();
                    FsService.stop();
                    estado_viaje = "3";
                }else if(tieneRecepcion){
                    estado_viaje = "3";
                }

                if (!documento_subido.equals("1") && f.exists()) {
                    try {
                        AsyncProcesarSqlChoferTask sql = new AsyncProcesarSqlChoferTask(context, f.getPath(), "", "Orden Transporte");
                        sql.execute();
                        if ((fi.exists() && f.exists()) || (fi2.exists() && f.exists())) {
                            new MainActivity.ActualizaOTXMLServer().execute();
                        } else {
                            new MainActivity.ActualizaOTXMLServer().execute();
                        }

                    } catch (Exception e) {

                    }
                }

                String caac_geo_sup_izq_x = "", caac_geo_sup_izq_y = "", caac_geo_inf_der_x = "", caac_geo_inf_der_y = "";
                OrdenesTransporteDao o = new OrdenesTransporteDao(context);
                o.open();
                String rutChofer = "", horaDestinoProgramada = "";
                try {
                    OrdenesTransporte orden = o.getOrdenTransporteById(context, OT);
                    try {
                        caac_geo_sup_izq_x = orden.getCaac_geo_sup_izq_x();
                        caac_geo_sup_izq_y = orden.getCaac_geo_sup_izq_y();
                        caac_geo_inf_der_x = orden.getCaac_geo_inf_der_x();
                        caac_geo_inf_der_y = orden.getCaac_geo_inf_der_y();
                        rutChofer = orden.getRut_chofer();
                        horaDestinoProgramada = orden.getHora_llegada_destino();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                o.close();
                if (documento_subido.equals("1")){
                    if (caac_geo_inf_der_x != null && caac_geo_inf_der_y != null && caac_geo_sup_izq_x != null && caac_geo_sup_izq_y != null) {
                        if (!caac_geo_inf_der_x.equals("") && !caac_geo_inf_der_y.equals("") && !caac_geo_sup_izq_x.equals("") && !caac_geo_sup_izq_y.equals("")) {
                            if (Utility.validaGeocerca(Double.valueOf(caac_geo_sup_izq_x), Double.valueOf(caac_geo_inf_der_x), Double.valueOf(caac_geo_inf_der_y), Double.valueOf(caac_geo_sup_izq_y), latitud, longitude)) {
                                estado_viaje = "4";
                                // Falta validación si aplica planta ctac
                                if (documento_subido.equals("1") && recepid.equals("")) {
                                    MainActivity.obtienePresentacion(OT, rutChofer, horaDestinoProgramada, viaje_destino, latitud, longitude, plantaCTAC);
                                    //MainActivity.obtienePresentacion(_OT, rutChofer, horaDestinoProgramada, viaje_destino, latitud, longitude);
                                }
                            }
                        }
                    }
                }

                if (estado_viaje.equals("4")) {
                    MainActivity.validarOTTerminada(OT, latitud, longitude);
                }else {
                    MainActivity.validarOTInactiva(OT);
                }
            }
        }

        switch (estado_viaje){
            case "0":
                MainActivity.tx_viaje_iniciado.setText("Viaje no iniciado");
                MainActivity.tx_viaje_iniciado.setBackgroundColor(0xFFCC0000);
                break;
            case "1":
                MainActivity.tx_viaje_iniciado.setBackgroundColor(0xFF1ABC9C);
                MainActivity.tx_viaje_iniciado.setText("Viaje a origen");
                break;
            case "2":
                MainActivity.tx_viaje_iniciado.setText("Viaje en origen");
                MainActivity.tx_viaje_iniciado.setBackgroundColor(0xFF1ABC9C);
                break;
            case "3":
                MainActivity.tvPatente.setBackgroundResource(R.drawable.patente_radius_iniciada);
                MainActivity.tx_viaje_iniciado.setBackgroundColor(0xFF1ABC9C);
                MainActivity.tx_viaje_iniciado.setText("Viaje a destino");
                break;
            case "4":
                MainActivity.tvPatente.setBackgroundResource(R.drawable.patente_radius_iniciada);
                MainActivity.tx_viaje_iniciado.setBackgroundColor(0xFF1ABC9C);
                MainActivity.tx_viaje_iniciado.setText("Viaje en destino");
                break;
        }


            /*if (OT_confirma.equals("1") && recepid.equals(""))
                MainActivity.validarOTInactiva(OT);
            else if (OT_confirma.equals("1") && !recepid.equals("")) {



                if(viaje_destino.equals("1") && OT_confirma.equals("1") && recepid.equals("")){
                    MainActivity.tvPatente.setBackgroundResource(R.drawable.patente_radius_iniciada);
                    MainActivity.tx_viaje_iniciado.setText("Viaje a destino");
                }else if(viaje_destino.equals("1") && OT_confirma.equals("1") && !recepid.equals("")){
                    MainActivity.tvPatente.setBackgroundResource(R.drawable.patente_radius_iniciada);
                    MainActivity.tx_viaje_iniciado.setText("Viaje en destino");
                }
                else {
                    MainActivity.tvPatente.setBackgroundResource(R.drawable.patente_radius);
                }
            }*/
        /* */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public static void AlertNoGps() {
        if (!AlertInfo) {
            AlertInfo = true;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("El sistema GPS esta desactivado");
            builder.setMessage("Favor activar.")
                    .setCancelable(true)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            AlertInfo = false;
                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
            alert = builder.create();
            alert.show();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    public static void validarOT(final Integer valida) {
        try {
            if (Utility.isOnline(context)) {
                //descargaMaestro();
                /*pDialog = new ProgressDialog(context);
                pDialog.setTitle("Buscando OT disponible.");*/
                POSTAPIRequest postapiRequest = new POSTAPIRequest();
                String url = Constantes.URL_WS_DEFECTO + "OT_DISPONIBLE";
                JSONObject params = new JSONObject();
                try {
                    params.put("CAMION", Utility.IsNull(Utility.getPreferenciaString("patente", context), ""));
                    params.put("SESSION", Utility.getPreferenciaObject("session", context));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postapiRequest.request(context, new FetchDataListener() {
                    @Override
                    public void onFetchComplete(JSONObject data) {
                        //Fetch Complete. Now stop progress bar  or loader
                        //you started in onFetchStart
                        //RequestQueueService.cancelProgressDialog();

                        try {
                            //Now check result sent by our POSTAPIRequest class
                            if (data != null) {
                                if (data.has("success")) {
                                    boolean success = data.getBoolean("success");
                                    if (success == true) {
                                        JSONObject _SESSION = data.getJSONObject("SESSION");
                                        if (_SESSION == null) {
                                            Utility.ShowMessage(context, "Error al rescatar información de sesión.");
                                        } else {
                                            Utility.setPreferencia("session", _SESSION, context);
                                            try {
                                                OrdenesTransporte cf = new OrdenesTransporte();
                                                boolean acceso = false;
                                                String _OT = "";
                                                try {
                                                    // Eliminar carpetas
                                                    String folder;
                                                    folder = Utility.RUTA_ENVIO(context);
                                                    File directory = new File(folder).getAbsoluteFile();
                                                    if (directory.exists()) {
                                                        String[] children = directory.list();
                                                        for (int i = 0; i < children.length; i++) {
                                                            new File(directory, children[i]).delete();
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    // Eliminar carpetas
                                                    String folder;
                                                    folder = Constantes.RUTA_RECEPCION;
                                                    File directory = new File(folder).getAbsoluteFile();
                                                    if (directory.exists()) {
                                                        String[] children = directory.list();
                                                        for (int i = 0; i < children.length; i++) {
                                                            new File(directory, children[i]).delete();
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    String folder = Utility.RUTA_CONFIRMACION(context);

                                                    //Create androiddeft folder if it does not exist
                                                    File directory = new File(folder).getAbsoluteFile();
                                                    if (directory.exists()) {
                                                        directory.delete();
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                JSONArray jsonArray = null;
                                                jsonArray = data.getJSONArray("vTabla");
                                                ArrayList<OrdenesTransporte> OT = new ArrayList<OrdenesTransporte>();
                                                if (jsonArray.length() > 0) {

                                                    /*pDialog.setMessage("Buscando OT disponible...");
                                                    pDialog.setCancelable(true);*/
                                                    try {
                                                        pDialog.show();
                                                    } catch (Exception ex) {

                                                    }
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jsonConfig = jsonArray.getJSONObject(i);
                                                        if (sharedPref.getString("OT", "").equals("")) {
                                                            SharedPreferences.Editor editor = sharedPref.edit();
                                                            editor.putString("OT", String.valueOf(jsonConfig.getInt("NUMERO_OT")));
                                                            _OT = String.valueOf(jsonConfig.getInt("NUMERO_OT"));
                                                            cf.setNumero_ot(jsonConfig.isNull("NUMERO_OT") ? null : jsonConfig.getInt("NUMERO_OT"));
                                                            cf.setNombre_chofer(jsonConfig.isNull("NOMBRE_CHOFER") ? null : jsonConfig.getString("NOMBRE_CHOFER"));
                                                            cf.setRut_chofer(jsonConfig.isNull("RUT_CHOFER") ? null : jsonConfig.getString("RUT_CHOFER"));
                                                            cf.setProducto_codigo(jsonConfig.isNull("PRODUCTO_CODIGO") ? null : jsonConfig.getString("PRODUCTO_CODIGO"));
                                                            cf.setProducto_codigo_desc(jsonConfig.isNull("PRODUCTO_CODIGO_DESC") ? null : jsonConfig.getString("PRODUCTO_CODIGO_DESC"));
                                                            cf.setGrua(jsonConfig.isNull("GRUA") ? null : jsonConfig.getString("GRUA"));
                                                            cf.setPatente_carro(jsonConfig.isNull("PATENTE_CARRO") ? null : jsonConfig.getString("PATENTE_CARRO"));
                                                            cf.setHora_inicio_viaje(jsonConfig.isNull("HORA_INICIO_VIAJE") ? null : jsonConfig.getString("HORA_INICIO_VIAJE"));
                                                            cf.setOrigen_desc(jsonConfig.isNull("ORIGEN_DSC") ? null : jsonConfig.getString("ORIGEN_DSC"));
                                                            cf.setOrigen_codigo(jsonConfig.isNull("ORIGEN_CODIGO") ? null : jsonConfig.getString("ORIGEN_CODIGO"));
                                                            cf.setOrigen_formin(jsonConfig.isNull("ORIGEN_FORMIN") ? null : jsonConfig.getString("ORIGEN_FORMIN"));
                                                            cf.setPlanta_origen(jsonConfig.isNull("PLANTA_ORIGEN") ? null : jsonConfig.getString("PLANTA_ORIGEN"));
                                                            cf.setHora_llegada_origen(jsonConfig.isNull("HORA_LLEGADA_ORIGEN") ? null : jsonConfig.getString("HORA_LLEGADA_ORIGEN"));
                                                            cf.setHora_servicio_origen(jsonConfig.isNull("HORA_SERVICIO_ORIGEN") ? null : jsonConfig.getString("HORA_SERVICIO_ORIGEN"));
                                                            cf.setDestino_dsc(jsonConfig.isNull("DESTINO_DSC") ? null : jsonConfig.getString("DESTINO_DSC"));
                                                            cf.setDestino_codigo(jsonConfig.isNull("DESTINO_CODIGO") ? null : jsonConfig.getString("DESTINO_CODIGO"));
                                                            cf.setDestino_formin(jsonConfig.isNull("DESTINO_FORMIN") ? null : jsonConfig.getString("DESTINO_FORMIN"));
                                                            cf.setHora_llegada_destino(jsonConfig.isNull("HORA_LLEGADA_DESTINO") ? null : jsonConfig.getString("HORA_LLEGADA_DESTINO"));
                                                            cf.setPlanta_destino(jsonConfig.isNull("PLANTA_DESTINO") ? null : jsonConfig.getString("PLANTA_DESTINO"));
                                                            cf.setProv_rut_folio(jsonConfig.isNull("PROV_RUT_FOLIO") ? null : jsonConfig.getString("PROV_RUT_FOLIO"));
                                                            cf.setProv_razon_social(jsonConfig.isNull("PROV_RAZON_SOCIAL") ? null : jsonConfig.getString("PROV_RAZON_SOCIAL"));
                                                            cf.setCaac_geo_sup_izq_x(jsonConfig.isNull("CAAC_GEO_SUP_IZQ_X") ? null : jsonConfig.getString("CAAC_GEO_SUP_IZQ_X"));
                                                            cf.setCaac_geo_sup_izq_y(jsonConfig.isNull("CAAC_GEO_SUP_IZQ_Y") ? null : jsonConfig.getString("CAAC_GEO_SUP_IZQ_Y"));
                                                            cf.setCaac_geo_inf_der_x(jsonConfig.isNull("CAAC_GEO_INF_DER_X") ? null : jsonConfig.getString("CAAC_GEO_INF_DER_X"));
                                                            cf.setCaac_geo_inf_der_y(jsonConfig.isNull("CAAC_GEO_INF_DER_Y") ? null : jsonConfig.getString("CAAC_GEO_INF_DER_Y"));
                                                            cf.setCum_id_asicam(jsonConfig.isNull("CUM_ID_ASICAM") ? null : jsonConfig.getInt("CUM_ID_ASICAM"));
                                                            cf.setClave_asicam(jsonConfig.isNull("CLAVE_ASICAM") ? null : jsonConfig.getString("CLAVE_ASICAM"));
                                                            cf.setPeriodo_asicam(jsonConfig.isNull("cum_periodo") ? null : jsonConfig.getString("cum_periodo"));
                                                            OT.add(cf);
                                                            OrdenesTransporteDao o = new OrdenesTransporteDao(context);
                                                            try {
                                                                o.open();
                                                                if (o.insertOrdenTransporte(OT, patente)) {
                                                                    editor.putString("OT_confirma", "1");
                                                                } else {
                                                                    editor.putString("OT_confirma", "0");
                                                                }
                                                                o.close();

                                                                ocultarSinOT();
                                                                llenarOT(_OT);
                                                            } catch (Exception e) {
                                                                o.close();
                                                            }
                                                            editor.commit();

                                                            MainActivity.tabLayout.getTabAt(1).select();
                                                        } else {
                                                            ocultarSinOT();
                                                            llenarOT(sharedPref.getString("OT", ""));
                                                        }
                                                    }
                                                    //Utilities.ShowMessage(context, resp[0].toString());
                                                } else {
                                                    if (valida == 1) {
                                                        solicitudAdicional();
                                                    }
                                                  /*  if (pDialog != null) {
                                                        pDialog.hide();
                                                        pDialog.dismiss();
                                                    }*/
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
                                            }
                                            /*if (pDialog != null) {
                                                pDialog.hide();
                                                pDialog.dismiss();
                                            }*/
                                        }
                                    } else {
                                        String message = data.getString("message");
                                        Utility.ShowMessage(context, message);
                                    }
                                }
                            } else {
                               /* if (pDialog != null) {
                                    pDialog.hide();
                                    pDialog.dismiss();
                                }*/
                                Utility.ShowMessage(context, "Error al rescatar información de sistema central.");

                            }
                        } catch (Exception e) {
                            /*if (pDialog != null) {
                                pDialog.hide();
                                pDialog.dismiss();
                            }*/
                            Utility.ShowMessage(context, "Ocurrió un error inesperado." + e.getMessage());
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onFetchFailure(String msg) {
                        //RequestQueueService.cancelProgressDialog();
                        Utility.ShowMessageWebApi(context, msg);
                        /*if (pDialog != null) {
                            pDialog.hide();
                            pDialog.dismiss();
                        }*/
                    }

                    @Override
                    public void onFetchStart() {

                        //RequestQueueService.showProgressDialog((Activity) context);
                        //pDialog.show();
                    }
                }, params, url, null);
            }
        } catch (Exception ex) {
            Utility.ShowMessage(context, ex.getMessage());
        }


//        if (Utility.isOnline(context)) {
//            //descargaMaestro();
//
//
//            pDialog = new ProgressDialog(context);
//            RequestQueue queue = VolleySingleton.getIntanciaVolley(context).getRequestQueue();
//
//            String url = Constantes.URL_WS_DEFECTO + "OT_DISPONIBLE?CODIGO=" + cl.genesys.appchofer.utilities.Constantes.token + "&PATENTE=" + sharedPref.getString("patente", "").toUpperCase();
//
//
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                    new Response.Listener<String>() {
//
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONArray jsonArray = null;
//                                JSONObject json = new JSONObject(response.toString());
//
//                                OrdenesTransporte cf = new OrdenesTransporte();
//                                boolean acceso = false;
//                                String _OT = "";
//                                boolean success = json.getBoolean("success");
//
//                                if (success == true) {
//
//                                    try {
//                                        // Eliminar carpetas
//
//                                        String folder;
//                                        folder = Utility.RUTA_ENVIO(context);
//                                        File directory = new File(folder).getAbsoluteFile();
//                                        if (directory.exists()) {
//                                            String[] children = directory.list();
//                                            for (int i = 0; i < children.length; i++) {
//                                                new File(directory, children[i]).delete();
//                                            }
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    try {
//                                        // Eliminar carpetas
//                                        String folder;
//                                        folder = Constantes.RUTA_RECEPCION;
//                                        File directory = new File(folder).getAbsoluteFile();
//                                        if (directory.exists()) {
//                                            String[] children = directory.list();
//                                            for (int i = 0; i < children.length; i++) {
//                                                new File(directory, children[i]).delete();
//                                            }
//                                        }
//                                    }  catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    try {
//                                        String folder = Utility.RUTA_CONFIRMACION(context);
//
//                                        //Create androiddeft folder if it does not exist
//                                        File directory = new File(folder).getAbsoluteFile();
//                                        if (directory.exists()) {
//                                            directory.delete();
//                                        }
//                                    }
//                                    catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    jsonArray = json.getJSONArray("dataset");
//                                    ArrayList<OrdenesTransporte> OT = new ArrayList<OrdenesTransporte>();
//                                    if (jsonArray.length() > 0) {
//
//                                        pDialog.setMessage("Buscando OT disponible...");
//                                        pDialog.setCancelable(true);
//                                        try {
//                                            pDialog.show();
//                                        } catch (Exception ex)  {
//
//                                        }
//                                        for (int i = 0; i < jsonArray.length(); i++) {
//                                            JSONObject jsonConfig = jsonArray.getJSONObject(i);
//                                            if (sharedPref.getString("OT", "").equals("")) {
//                                                SharedPreferences.Editor editor = sharedPref.edit();
//                                                editor.putString("OT", String.valueOf(jsonConfig.getInt("NUMERO_OT")));
//                                                _OT = String.valueOf(jsonConfig.getInt("NUMERO_OT"));
//                                                cf.setNumero_ot(jsonConfig.isNull("NUMERO_OT") ? null : jsonConfig.getInt("NUMERO_OT"));
//                                                cf.setNombre_chofer(jsonConfig.isNull("NOMBRE_CHOFER") ? null : jsonConfig.getString("NOMBRE_CHOFER"));
//                                                cf.setRut_chofer(jsonConfig.isNull("RUT_CHOFER") ? null : jsonConfig.getString("RUT_CHOFER"));
//                                                cf.setProducto_codigo(jsonConfig.isNull("PRODUCTO_CODIGO") ? null : jsonConfig.getString("PRODUCTO_CODIGO"));
//                                                cf.setProducto_codigo_desc(jsonConfig.isNull("PRODUCTO_CODIGO_DESC") ? null : jsonConfig.getString("PRODUCTO_CODIGO_DESC"));
//                                                cf.setGrua(jsonConfig.isNull("GRUA") ? null : jsonConfig.getString("GRUA"));
//                                                cf.setPatente_carro(jsonConfig.isNull("PATENTE_CARRO") ? null : jsonConfig.getString("PATENTE_CARRO"));
//                                                cf.setHora_inicio_viaje(jsonConfig.isNull("HORA_INICIO_VIAJE") ? null : jsonConfig.getString("HORA_INICIO_VIAJE"));
//                                                cf.setOrigen_desc(jsonConfig.isNull("ORIGEN_DSC") ? null : jsonConfig.getString("ORIGEN_DSC"));
//                                                cf.setOrigen_codigo(jsonConfig.isNull("ORIGEN_CODIGO") ? null : jsonConfig.getString("ORIGEN_CODIGO"));
//                                                cf.setOrigen_formin(jsonConfig.isNull("ORIGEN_FORMIN") ? null : jsonConfig.getString("ORIGEN_FORMIN"));
//                                                cf.setPlanta_origen(jsonConfig.isNull("PLANTA_ORIGEN") ? null : jsonConfig.getString("PLANTA_ORIGEN"));
//                                                cf.setHora_llegada_origen(jsonConfig.isNull("HORA_LLEGADA_ORIGEN") ? null : jsonConfig.getString("HORA_LLEGADA_ORIGEN"));
//                                                cf.setHora_servicio_origen(jsonConfig.isNull("HORA_SERVICIO_ORIGEN") ? null : jsonConfig.getString("HORA_SERVICIO_ORIGEN"));
//                                                cf.setDestino_dsc(jsonConfig.isNull("DESTINO_DSC") ? null : jsonConfig.getString("DESTINO_DSC"));
//                                                cf.setDestino_codigo(jsonConfig.isNull("DESTINO_CODIGO") ? null : jsonConfig.getString("DESTINO_CODIGO"));
//                                                cf.setDestino_formin(jsonConfig.isNull("DESTINO_FORMIN") ? null : jsonConfig.getString("DESTINO_FORMIN"));
//                                                cf.setHora_llegada_destino(jsonConfig.isNull("HORA_LLEGADA_DESTINO") ? null : jsonConfig.getString("HORA_LLEGADA_DESTINO"));
//                                                cf.setPlanta_destino(jsonConfig.isNull("PLANTA_DESTINO") ? null : jsonConfig.getString("PLANTA_DESTINO"));
//                                                cf.setProv_rut_folio(jsonConfig.isNull("PROV_RUT_FOLIO") ? null : jsonConfig.getString("PROV_RUT_FOLIO"));
//                                                cf.setProv_razon_social(jsonConfig.isNull("PROV_RAZON_SOCIAL") ? null : jsonConfig.getString("PROV_RAZON_SOCIAL"));
//                                                cf.setCaac_geo_sup_izq_x(jsonConfig.isNull("CAAC_GEO_SUP_IZQ_X") ? null : jsonConfig.getString("CAAC_GEO_SUP_IZQ_X"));
//                                                cf.setCaac_geo_sup_izq_y(jsonConfig.isNull("CAAC_GEO_SUP_IZQ_Y") ? null : jsonConfig.getString("CAAC_GEO_SUP_IZQ_Y"));
//                                                cf.setCaac_geo_inf_der_x(jsonConfig.isNull("CAAC_GEO_INF_DER_X") ? null : jsonConfig.getString("CAAC_GEO_INF_DER_X"));
//                                                cf.setCaac_geo_inf_der_y(jsonConfig.isNull("CAAC_GEO_INF_DER_Y") ? null : jsonConfig.getString("CAAC_GEO_INF_DER_Y"));
//                                                cf.setCum_id_asicam(jsonConfig.isNull("CUM_ID_ASICAM") ? null : jsonConfig.getInt("CUM_ID_ASICAM"));
//                                                cf.setClave_asicam(jsonConfig.isNull("CLAVE_ASICAM") ? null : jsonConfig.getString("CLAVE_ASICAM"));
//                                                cf.setPeriodo_asicam(jsonConfig.isNull("cum_periodo") ? null : jsonConfig.getString("cum_periodo"));
//                                                OT.add(cf);
//                                                OrdenesTransporteDao o = new OrdenesTransporteDao(context);
//                                                try {
//                                                    o.open();
//                                                    if (o.insertOrdenTransporte(OT, patente)) {
//                                                        editor.putString("OT_confirma", "1");
//                                                    } else {
//                                                        editor.putString("OT_confirma", "0");
//                                                    }
//                                                    o.close();
//
//                                                    ocultarSinOT();
//                                                    llenarOT(_OT);
//                                                } catch (Exception e) {
//                                                    o.close();
//                                                }
//                                                editor.commit();
//
//                                                MainActivity.tabLayout.getTabAt(1).select();
//                                            } else {
//                                                ocultarSinOT();
//                                                llenarOT(sharedPref.getString("OT", ""));
//                                            }
//                                        }
//                                        //Utilities.ShowMessage(context, resp[0].toString());
//                                    } else {
//                                        if (valida == 1) {
//                                            solicitudAdicional();
//                                        }
//                                        pDialog.hide();
//                                        pDialog.dismiss();
//                                    }
//                                } else {
//                                    Utility.ShowMessage(context, "Error al rescatar información de sistema central.");
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
//                            }
//
//                            pDialog.dismiss();
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            pDialog.hide();
//                            pDialog.dismiss();
//                        }
//                    });
//            queue.add(stringRequest);
//        }
    }

    public static void descargaCartolas() throws IOException {
        try {
            String OT = sharedPref.getString("OT", "");
            POSTAPIRequest postapiRequest = new POSTAPIRequest();
            String url = Constantes.URL_WS_DEFECTO + "GET_CARTOLAS_PRODUCCION";
            JSONObject params = new JSONObject();
            try {
                params.put("OT", Utility.IsNull(OT, ""));
                params.put("SESSION", Utility.getPreferenciaObject("session", context));
            } catch (Exception e) {
                e.printStackTrace();
            }
            postapiRequest.request(context, new FetchDataListener() {
                        @Override
                        public void onFetchComplete(JSONObject data) {
                            //Fetch Complete. Now stop progress bar  or loader
                            //you started in onFetchStart
                            try {
                                //Now check result sent by our POSTAPIRequest class
                                if (data != null) {
                                    if (data.has("success")) {
                                        boolean success = data.getBoolean("success");
                                        if (success == true) {
                                            JSONObject _SESSION = data.getJSONObject("SESSION");
                                            if (Utility.validaTokenSession(_SESSION) == false) {
                                                Utility.ShowMessage(context, "Error al rescatar información de sesión.");
                                            } else {
                                                Utility.setPreferencia("session", _SESSION, context);
                                                try {
                                                    //LLENAR RESPUESTA
                                                    JSONArray jsonArray = null;
                                                    List<String> JCartolas = new ArrayList<>();
                                                    String texto = "";
                                                    jsonArray = data.getJSONArray("vTabla");
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jsonConfig = jsonArray.getJSONObject(i);
                                                        texto += "" + jsonConfig.getString("T") + "; " +
                                                                "\n";
                                                    }


                                                    File file = null;
                                                    if (tipoCamion.equals("2")){
                                                        file = new File(Constantes.RUTA_ENVIO_DESPACHO, "_cartolas_produccion.sql");
                                                        file.createNewFile();
                                                    }else {
                                                        file = new File(Constantes.RUTA_ENVIO, "cartolas_produccion.sql");
                                                        file.createNewFile();
                                                    }

                                                    try {
                                                        Gson gson = new GsonBuilder().serializeNulls().create();
                                                        OutputStreamWriter outputStreamWriter = null;
                                                        outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
                                                        outputStreamWriter.write(texto);
                                                        outputStreamWriter.close();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
                                                }
                                            }

                                        } else {
                                            String message = data.getString("message");
                                            Utility.ShowMessage(context, message);
                                        }
                                    }
                                } else {
                                    Utility.ShowMessage(context, "Error al rescatar información de sistema central.");

                                }
                            } catch (Exception e) {
                                Utility.ShowMessage(context, "Ocurrió un error inesperado." + e.getMessage());
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFetchFailure(String msg) {
                            Utility.ShowMessageWebApi(context, msg);

                        }

                        @Override
                        public void onFetchStart() {

                        }
                    }, params,
                    url,
                    new DefaultRetryPolicy(1000000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            );
        } catch (Exception ex) {
            Utility.ShowMessage(context, ex.getMessage());
        }
//        String OT = sharedPref.getString("OT", "");
//        RequestQueue queue = VolleySingleton.getIntanciaVolley(context).getRequestQueue();
//        String url = Constantes.URL_WS_DEFECTO + "GET_CARTOLAS_PRODUCCION?CODIGO=" + Constantes.token + "&OT=" + OT;
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray jsonArray = null;
//                            JSONObject json = new JSONObject(response);
//
//                            List<String> JCartolas = new ArrayList<>();
//                            boolean success = json.getBoolean("success");
//                            String texto = "";
//                            if (success == true) {
//                                jsonArray = json.getJSONArray("dataset");
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject jsonConfig = jsonArray.getJSONObject(i);
//                                    texto += "" + jsonConfig.getString("T") + "; " +
//                                            "\n";
//                                }
//                            } else {
//
//                            }
//                            File file = new File(Constantes.RUTA_ENVIO, "cartolas_produccion.sql");
//                            file.createNewFile();
//
//                            try {
//
//                                Gson gson = new GsonBuilder().serializeNulls().create();
//                                OutputStreamWriter outputStreamWriter = null;
//                                outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
//
//                                outputStreamWriter.write(texto);
//
//                                outputStreamWriter.close();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                    }
//                });
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(stringRequest);

    }

    public static void obtienePresentacion(final String ot, String rutChofer, final String horaProgramada, String viaje_iniciado, double latitud, double longitude, String plantaCTAC) {
        try {
            if (Utility.isOnline(context)) {
                pDialog = new ProgressDialog(context);
                POSTAPIRequest postapiRequest = new POSTAPIRequest();
                String url = Constantes.URL_WS_DEFECTO + "GET_REGISTRO_PRESENTACION";
                JSONObject params = new JSONObject();
                try {
                    params.put("OT", Utility.IsNull(ot, ""));
                    params.put("RUT_CHOFER", Utility.IsNull(rutChofer, ""));
                    params.put("LONGITUD", Utility.IsNull(longitude, 0.0));
                    params.put("LATITUD", Utility.IsNull(latitud, 0.0));
                    params.put("SESSION", Utility.getPreferenciaObject("session", context));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postapiRequest.request(context, new FetchDataListener() {
                    @Override
                    public void onFetchComplete(JSONObject data) {
                        //Fetch Complete. Now stop progress bar  or loader
                        //you started in onFetchStart
                        try {

                            //Now check result sent by our POSTAPIRequest class
                            if (data != null) {
                                if (data.has("success")) {
                                    boolean success = data.getBoolean("success");
                                    if (success == true) {
                                        JSONObject _SESSION = data.getJSONObject("SESSION");
                                        if (Utility.validaTokenSession(_SESSION) == false) {
                                            Utility.ShowMessage(context, "Error al rescatar información de sesión.");
                                        } else {
                                            Utility.setPreferencia("session", _SESSION, context);
                                            try {
                                                //LLENAR RESPUESTA
                                                JSONArray jsonArray = null;
                                                String fechaRecep = "";
                                                Integer recepId = 0;
                                                jsonArray = data.getJSONArray("vTabla");

                                                JSONObject resultado = data.getJSONObject("resultado");
                                                if (resultado != null) {
                                                    if(plantaCTAC.toUpperCase().equals("S")) {
                                                        pDialog.setMessage("Registrando presentación...");
                                                    }else {
                                                        pDialog.setMessage("Llegando a destino...");
                                                    }
                                                    pDialog.show();
                                                    pDialog.setCancelable(true);

                                                    Integer _ERROR = Utility.IsNull(resultado.getInt("ERROR"), 0);
                                                    String _MENSAJE = Utility.IsNull(resultado.getString("MENSAJE"), "");
                                                    Integer _RECEP_ID = Utility.IsNull(resultado.getInt("RECEP_ID"), 0);
                                                    String _FECHA_RECEP = Utility.IsNull(resultado.getString("FECHA_RECEP"), "");
                                                    String _DIFERENCIA = Utility.IsNull(resultado.getString("DIFERENCIA"), "");



                                                    OrdenesTransporteDao o = new OrdenesTransporteDao(context);
                                                    o.open();
                                                    o.updatePresentacion(context, ot, _FECHA_RECEP, _DIFERENCIA, _RECEP_ID);
                                                    o.close();
                                                    llenarPresentacion(ot);
                                                    MainActivity.tabLayout.getTabAt(3).select();
                                                    if(plantaCTAC.toUpperCase().equals("S")) {
                                                        Utility.ShowAlertDialog(context, "Llegada destino", "Registro presentación generado correctamente. ");
                                                    }else {
                                                        Utility.ShowAlertDialog(context, "Llegada destino", "Debe esperar atención. ");
                                                    }

                                                    tx_viaje_iniciado.setText("Viaje en destino");

                                                    if (!_FECHA_RECEP.equals("") && _RECEP_ID >= 0) {
                                                        SharedPreferences.Editor editor = sharedPref.edit();
                                                        editor.putString("RECEPID", _RECEP_ID.toString());
                                                        editor.commit();
                                                    }else {
                                                        SharedPreferences.Editor editor = sharedPref.edit();
                                                        editor.putString("RECEPID", "0");
                                                        editor.commit();
                                                    }
                                                }

                                               /* if (jsonArray.length() > 0) {
                                                    pDialog.setMessage("Registrando presentación...");
                                                    pDialog.setCancelable(true);
                                                    try {
                                                        pDialog.show();
                                                    } catch (Exception e) {

                                                    }
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jsonConfig = jsonArray.getJSONObject(i);
                                                        fechaRecep = jsonConfig.getString("fechaRecep");
                                                        recepId = jsonConfig.getInt("recepId");
                                                        String diferenciaRecep = String.valueOf(jsonConfig.get("diferenciaRecep"));
                                                        OrdenesTransporteDao o = new OrdenesTransporteDao(context);
                                                        o.open();
                                                        o.updatePresentacion(context, ot, fechaRecep, diferenciaRecep, recepId);
                                                        o.close();
                                                        llenarPresentacion(ot);
                                                        MainActivity.tabLayout.getTabAt(3).select();
                                                        Utility.ShowAlertDialog(context, "Llegada destino", "Registro presentación generado correctamente. ");
                                                        tx_viaje_iniciado.setText("Viaje en destino");
                                                    }
                                                } else {
                                                    pDialog.hide();
                                                    pDialog.dismiss();
                                                }*/


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
                                            }
                                        }

                                    } else {
                                        String message = data.getString("message");
                                        Utility.ShowMessage(context, message);
                                    }
                                }
                                if (pDialog != null) {
                                    pDialog.hide();
                                    pDialog.dismiss();
                                }
                            } else {
                                Utility.ShowMessage(context, "Error al rescatar información de sistema central.");

                            }
                        } catch (Exception e) {
                            Utility.ShowMessage(context, "Ocurrió un error inesperado." + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFetchFailure(String msg) {
                        Utility.ShowMessageWebApi(context, msg);
                        if (pDialog != null) {
                            pDialog.hide();
                            pDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFetchStart() {



                    }
                }, params, url, new DefaultRetryPolicy(60000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

//                pDialog = new ProgressDialog(context);
//                RequestQueue queue = VolleySingleton.getIntanciaVolley(context).getRequestQueue();
//
//                String url = Constantes.URL_WS_DEFECTO + "GET_REGISTRO_PRESENTACION?CODIGO=" + Constantes.token + "&numero_ot=" + ot + "&RUT_CHOFER=" + rutChofer + "&LONGITUD=" + longitude + "&LATITUD=" + latitud;
//
//
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                        new Response.Listener<String>() {
//
//                            @Override
//                            public void onResponse(String response) {
//                                try {
//                                    JSONArray jsonArray = null;
//                                    JSONObject json = new JSONObject(response);
//
//                                    boolean success = json.getBoolean("success");
//                                    String fechaRecep = "";
//                                    Integer recepId = 0;
//                                    if (success == true) {
//                                        jsonArray = json.getJSONArray("dataset");
//                                        if (jsonArray.length() > 0) {
//
//                                            pDialog.setMessage("Registrando presentación...");
//                                            pDialog.setCancelable(true);
//                                            try {
//                                                pDialog.show();
//                                            } catch (Exception e) {
//
//                                            }
//
//                                            for (int i = 0; i < jsonArray.length(); i++) {
//
//                                                JSONObject jsonConfig = jsonArray.getJSONObject(i);
//
//                                                fechaRecep = jsonConfig.getString("fechaRecep");
//                                                recepId = jsonConfig.getInt("recepId");
//                                                String diferenciaRecep = String.valueOf(jsonConfig.get("diferenciaRecep"));
//
//                                                OrdenesTransporteDao o = new OrdenesTransporteDao(context);
//                                                o.open();
//                                                o.updatePresentacion(context, ot, fechaRecep, diferenciaRecep, recepId);
//                                                o.close();
//
//                                                llenarPresentacion(ot);
//
//                                                MainActivity.tabLayout.getTabAt(3).select();
//
//                                                Utility.ShowAlertDialog(context, "Llegada destino", "Registro presentación generado correctamente. ");
//                                                tx_viaje_iniciado.setText("Viaje en destino");
//                                            }
//                                        } else {
//                                            pDialog.hide();
//                                            pDialog.dismiss();
//                                        }
//
//                                        if (!fechaRecep.equals("") && recepId > 0) {
//                                            SharedPreferences.Editor editor = sharedPref.edit();
//                                            editor.putString("RECEPID", recepId.toString());
//                                            editor.commit();
//                                        }
//
//                                    } else {
//                                        //Utility.ShowMessage(context, "Error al rescatar información de sistema central.");
//                                    }
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
//                                }
//
//                                pDialog.dismiss();
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                pDialog.hide();
//                                pDialog.dismiss();
//                            }
//                        });
//
//                stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                queue.add(stringRequest);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void validarGDE() {
        try {
            pDialog = new ProgressDialog(context);
            pDialog.setCancelable(false);

            POSTAPIRequest postapiRequest = new POSTAPIRequest();
            String url = Constantes.URL_WS_DEFECTO + "GDE_DISPONIBLE";
            JSONObject params = new JSONObject();
            try {
                params.put("OT", Utility.IsNull(Utility.getPreferenciaString("OT", context).toUpperCase(), ""));
                params.put("SESSION", Utility.getPreferenciaObject("session", context));
            } catch (Exception e) {
                e.printStackTrace();
            }
            postapiRequest.request(context, new FetchDataListener() {
                @Override
                public void onFetchComplete(JSONObject data) {
                    //Fetch Complete. Now stop progress bar  or loader
                    //you started in onFetchStart
                    //RequestQueueService.cancelProgressDialog();
                    try {
                        //Now check result sent by our POSTAPIRequest class
                        if (data != null) {
                            if (data.has("success")) {
                                boolean success = data.getBoolean("success");
                                if (success == true) {
                                    JSONObject _SESSION = data.getJSONObject("SESSION");
                                    if (Utility.validaTokenSession(_SESSION) == false) {
                                        Utility.ShowMessage(context, "Error al rescatar información de sesión.");
                                    } else {
                                        Utility.setPreferencia("session", _SESSION, context);
                                        try {
                                            //LLENAR RESPUESTA

                                            JSONArray jsonArray = null;
                                            OrdenesTransporte cf = new OrdenesTransporte();
                                            boolean acceso = false;
                                            String _OT = "";
                                            jsonArray = data.getJSONArray("vTabla");
                                            ArrayList<OrdenesTransporte> OT = new ArrayList<OrdenesTransporte>();
                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                JSONObject jsonConfig = jsonArray.getJSONObject(i);

                                                if (sharedPref.getString("GDE", "").equals("")) {
                                                    SharedPreferences.Editor editor = sharedPref.edit();
                                                    editor.putString("GDE", String.valueOf(jsonConfig.getInt("FOLIO_DTE")));
                                                    editor.commit();
                                                    _OT = String.valueOf(jsonConfig.getInt("NUMERO_OT"));
                                                    cf.setNumero_ot(jsonConfig.isNull("NUMERO_OT") ? null : jsonConfig.getInt("NUMERO_OT"));
                                                    cf.setNombre_chofer(jsonConfig.isNull("NOMBRE_CHOFER") ? null : jsonConfig.getString("NOMBRE_CHOFER"));

                                                    int cont = 0;
                                                    if (jsonConfig.isNull("RUT_CHOFER")) {
                                                        cl.genesys.appchofer.layout.OT.tv_ayuda_rut.setVisibility(View.VISIBLE);
                                                        cl.genesys.appchofer.layout.OT.lly_rut.setVisibility(View.VISIBLE);
                                                        cl.genesys.appchofer.layout.OT.btnGuardarRutC.setVisibility(View.VISIBLE);
                                                        cl.genesys.appchofer.layout.OT.edtRutChofer.setVisibility(View.VISIBLE);

                                                    } else {
                                                        // Descomentar para rut pulp
                                                        cf.setRut_chofer(jsonConfig.getString("RUT_CHOFER"));
                                                        cl.genesys.appchofer.layout.OT.tv_ayuda_rut.setVisibility(View.VISIBLE);
                                                        cl.genesys.appchofer.layout.OT.lly_rut.setVisibility(View.VISIBLE);
                                                        cl.genesys.appchofer.layout.OT.btnGuardarRutC.setVisibility(View.VISIBLE);
                                                        cl.genesys.appchofer.layout.OT.edtRutChofer.setVisibility(View.VISIBLE);
                                                        cont++;
                                                    }
                                                    cl.genesys.appchofer.layout.OT.btnConfirmarOT.setVisibility(View.GONE);
                                                    cf.setProducto_codigo(jsonConfig.isNull("PRODUCTO_CODIGO") ? null : jsonConfig.getString("PRODUCTO_CODIGO"));
                                                    cf.setProducto_codigo_desc(jsonConfig.isNull("PRODUCTO_CODIGO_DESC") ? null : jsonConfig.getString("PRODUCTO_CODIGO_DESC"));
                                                    cf.setGrua(jsonConfig.isNull("GRUA") ? null : jsonConfig.getString("GRUA"));
                                                    if (!jsonConfig.isNull("PATENTE_CARRO")) {
                                                        cf.setPatente_carro(jsonConfig.getString("PATENTE_CARRO"));
                                                    }
                                                    cf.setHora_inicio_viaje(jsonConfig.isNull("HORA_INICIO_VIAJE") ? null : jsonConfig.getString("HORA_INICIO_VIAJE"));
                                                    cf.setOrigen_desc(jsonConfig.isNull("ORIGEN_DSC") ? null : jsonConfig.getString("ORIGEN_DSC"));
                                                    cf.setOrigen_codigo(jsonConfig.isNull("ORIGEN_CODIGO") ? null : jsonConfig.getString("ORIGEN_CODIGO"));
                                                    cf.setOrigen_formin(jsonConfig.isNull("ORIGEN_FORMIN") ? null : jsonConfig.getString("ORIGEN_FORMIN"));
                                                    cf.setHora_llegada_origen(jsonConfig.isNull("HORA_LLEGADA_ORIGEN") ? null : jsonConfig.getString("HORA_LLEGADA_ORIGEN"));
                                                    cf.setHora_servicio_origen(jsonConfig.isNull("HORA_SERVICIO_ORIGEN") ? null : jsonConfig.getString("HORA_SERVICIO_ORIGEN"));
                                                    cf.setDestino_dsc(jsonConfig.isNull("DESTINO_DSC") ? null : jsonConfig.getString("DESTINO_DSC"));
                                                    cf.setDestino_codigo(jsonConfig.isNull("DESTINO_CODIGO") ? null : jsonConfig.getString("DESTINO_CODIGO"));
                                                    cf.setDestino_formin(jsonConfig.isNull("DESTINO_FORMIN") ? null : jsonConfig.getString("DESTINO_FORMIN"));
                                                    cf.setHora_llegada_destino(jsonConfig.isNull("HORA_LLEGADA_DESTINO") ? null : jsonConfig.getString("HORA_LLEGADA_DESTINO"));
                                                    cf.setFolio_dte(jsonConfig.isNull("FOLIO_DTE") ? null : jsonConfig.getInt("FOLIO_DTE"));
                                                    cf.setPlanta_destino(jsonConfig.isNull("PLANTA_DESTINO") ? null : jsonConfig.getString("PLANTA_DESTINO"));
                                                    cf.setProv_rut_folio(jsonConfig.isNull("PROV_RUT_FOLIO") ? null : jsonConfig.getString("PROV_RUT_FOLIO"));
                                                    cf.setProv_razon_social(jsonConfig.isNull("PROV_RAZON_SOCIAL") ? null : jsonConfig.getString("PROV_RAZON_SOCIAL"));
                                                    cf.setPlanta_origen(jsonConfig.isNull("PLANTA_ORIGEN") ? null : jsonConfig.getString("PLANTA_ORIGEN"));
                                                    cf.setCaac_geo_sup_izq_x(jsonConfig.isNull("CAAC_GEO_SUP_IZQ_X") ? null : jsonConfig.getString("CAAC_GEO_SUP_IZQ_X"));
                                                    cf.setCaac_geo_sup_izq_y(jsonConfig.isNull("CAAC_GEO_SUP_IZQ_Y") ? null : jsonConfig.getString("CAAC_GEO_SUP_IZQ_Y"));
                                                    cf.setCaac_geo_inf_der_x(jsonConfig.isNull("CAAC_GEO_INF_DER_X") ? null : jsonConfig.getString("CAAC_GEO_INF_DER_X"));
                                                    cf.setCaac_geo_inf_der_y(jsonConfig.isNull("CAAC_GEO_INF_DER_Y") ? null : jsonConfig.getString("CAAC_GEO_INF_DER_Y"));
                                                    cf.setCum_id_asicam(jsonConfig.isNull("CUM_ID_ASICAM") ? null : jsonConfig.getInt("CUM_ID_ASICAM"));
                                                    cf.setClave_asicam(jsonConfig.isNull("CLAVE_ASICAM") ? null : jsonConfig.getString("CLAVE_ASICAM"));
                                                    cf.setPeriodo_asicam(jsonConfig.isNull("cum_periodo") ? null : jsonConfig.getString("cum_periodo"));
                                                    OT.add(cf);
                                                    OrdenesTransporteDao o = new OrdenesTransporteDao(context);
                                                    try {
                                                        o.open();
                                                        o.deleteOrdenTransporte(_OT);
                                                        o.close();
                                                        o.open();
                                                        o.insertOrdenTransporteGDE(OT, patente);
                                                        o.close();
                                                        llenaGDE(sharedPref.getString("OT", ""));
                                                        recuperaGDE(sharedPref.getString("OT", ""), sharedPref.getString("GDE", ""));
                                                        //new DownloadFile().execute(Constantes.URL_WS_DEFECTO + "recuperaGDE?OT=" + sharedPref.getString("OT", ""), sharedPref.getString("OT", ""), sharedPref.getString("GDE", ""));
                                                        MainActivity.tabLayout.getTabAt(2).select();
                                                        if (pDialog != null) {
                                                            pDialog.hide();
                                                            pDialog.dismiss();
                                                        }
                                                        Toast.makeText(context, "GDE descargada correctamente.", Toast.LENGTH_LONG).show();
                                                    } catch (Exception e) {
                                                        o.close();
                                                        Utility.ShowMessage(context, e.getMessage());
                                                    }
                                                } else {
                                                    if (pDialog != null) {
                                                        pDialog.hide();
                                                        pDialog.dismiss();
                                                    }
                                                    ocultarSinOT();
                                                    llenaGDE(sharedPref.getString("OT", ""));
                                                }
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            if (pDialog != null) {
                                                pDialog.hide();
                                                pDialog.dismiss();
                                            }
                                            Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
                                        }
                                    }
                                } else {
                                    String message = data.getString("message");
                                    if (pDialog != null) {
                                        pDialog.hide();
                                        pDialog.dismiss();
                                    }
                                    Utility.ShowMessage(context, message);
                                }
                            }
                        } else {
                            if (pDialog != null) {
                                pDialog.hide();
                                pDialog.dismiss();
                            }
                            Utility.ShowMessage(context, "Error al rescatar información de sistema central.");

                        }
                    } catch (Exception e) {
                        if (pDialog != null) {
                            pDialog.hide();
                            pDialog.dismiss();
                        }
                        Utility.ShowMessage(context, "Ocurrió un error inesperado." + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFetchFailure(String msg) {
                   // RequestQueueService.cancelProgressDialog();
                    if (pDialog != null) {
                        pDialog.hide();
                        pDialog.dismiss();
                    }
                    Utility.ShowMessageWebApi(context, msg);

                }

                @Override
                public void onFetchStart() {
                    pDialog.show();
                    pDialog.setMessage("Descargando GDE...");
                }
            }, params, url, null);
            try {
                descargaCartolas();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            Utility.ShowMessage(context, ex.getMessage());
        }
//        pDialog = new ProgressDialog(context);
//        RequestQueue queue = VolleySingleton.getIntanciaVolley(context).getRequestQueue();
//
//        String url = Constantes.URL_WS_DEFECTO + "GDE_DISPONIBLE?CODIGO=" + Constantes.token + "&OT=" + sharedPref.getString("OT", "").toUpperCase();
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray jsonArray = null;
//                            JSONObject json = new JSONObject(response.toString());
//
//                            OrdenesTransporte cf = new OrdenesTransporte();
//                            boolean acceso = false;
//                            String _OT = "";
//                            boolean success = json.getBoolean("success");
//
//                            if (success == true) {
//
//                                jsonArray = json.getJSONArray("dataset");
//                                ArrayList<OrdenesTransporte> OT = new ArrayList<OrdenesTransporte>();
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    pDialog.setMessage("Descargando GDE...");
//                                    pDialog.setCancelable(false);
//                                    try {
//                                        pDialog.show();
//                                    } catch (Exception e) {
//
//                                    }
//
//                                    JSONObject jsonConfig = jsonArray.getJSONObject(i);
//
//                                    if (sharedPref.getString("GDE", "").equals("")) {
//                                        SharedPreferences.Editor editor = sharedPref.edit();
//                                        editor.putString("GDE", String.valueOf(jsonConfig.getInt("FOLIO_DTE")));
//                                        editor.commit();
//                                        _OT = String.valueOf(jsonConfig.getInt("NUMERO_OT"));
//                                        cf.setNumero_ot(jsonConfig.isNull("NUMERO_OT") ? null : jsonConfig.getInt("NUMERO_OT"));
//                                        cf.setNombre_chofer(jsonConfig.isNull("NOMBRE_CHOFER") ? null : jsonConfig.getString("NOMBRE_CHOFER"));
//
//                                        int cont = 0;
//                                        if (jsonConfig.isNull("RUT_CHOFER")) {
//                                            cl.genesys.appchofer.layout.OT.tv_ayuda_rut.setVisibility(View.VISIBLE);
//                                            cl.genesys.appchofer.layout.OT.lly_rut.setVisibility(View.VISIBLE);
//                                            cl.genesys.appchofer.layout.OT.btnGuardarRutC.setVisibility(View.VISIBLE);
//                                            cl.genesys.appchofer.layout.OT.edtRutChofer.setVisibility(View.VISIBLE);
//
//                                        } else {
//                                            // Descomentar para rut pulp
////                                            cl.genesys.appchofer.layout.OT.tv_ayuda_rut.setVisibility(View.GONE);
////                                            cl.genesys.appchofer.layout.OT.lly_rut.setVisibility(View.GONE);
////                                            cl.genesys.appchofer.layout.OT.btnGuardarRutC.setVisibility(View.GONE);
////                                            cl.genesys.appchofer.layout.OT.edtRutChofer.setVisibility(View.GONE);
////                                            cf.setRut_chofer(jsonConfig.getString("RUT_CHOFER"));
//                                            cf.setRut_chofer(jsonConfig.getString("RUT_CHOFER"));
//                                            cl.genesys.appchofer.layout.OT.tv_ayuda_rut.setVisibility(View.VISIBLE);
//                                            cl.genesys.appchofer.layout.OT.lly_rut.setVisibility(View.VISIBLE);
//                                            cl.genesys.appchofer.layout.OT.btnGuardarRutC.setVisibility(View.VISIBLE);
//                                            cl.genesys.appchofer.layout.OT.edtRutChofer.setVisibility(View.VISIBLE);
//
//                                            cont++;
//                                        }
//
//                                        cl.genesys.appchofer.layout.OT.btnConfirmarOT.setVisibility(View.GONE);
//
//                                        cf.setProducto_codigo(jsonConfig.isNull("PRODUCTO_CODIGO") ? null : jsonConfig.getString("PRODUCTO_CODIGO"));
//                                        cf.setProducto_codigo_desc(jsonConfig.isNull("PRODUCTO_CODIGO_DESC") ? null : jsonConfig.getString("PRODUCTO_CODIGO_DESC"));
//                                        cf.setGrua(jsonConfig.isNull("GRUA") ? null : jsonConfig.getString("GRUA"));
//
//                                        if (!jsonConfig.isNull("PATENTE_CARRO")) {
//                                            cf.setPatente_carro(jsonConfig.getString("PATENTE_CARRO"));
//                                        }
//
//
//                                        cf.setHora_inicio_viaje(jsonConfig.isNull("HORA_INICIO_VIAJE") ? null : jsonConfig.getString("HORA_INICIO_VIAJE"));
//                                        cf.setOrigen_desc(jsonConfig.isNull("ORIGEN_DSC") ? null : jsonConfig.getString("ORIGEN_DSC"));
//                                        cf.setOrigen_codigo(jsonConfig.isNull("ORIGEN_CODIGO") ? null : jsonConfig.getString("ORIGEN_CODIGO"));
//                                        cf.setOrigen_formin(jsonConfig.isNull("ORIGEN_FORMIN") ? null : jsonConfig.getString("ORIGEN_FORMIN"));
//
//                                        cf.setHora_llegada_origen(jsonConfig.isNull("HORA_LLEGADA_ORIGEN") ? null : jsonConfig.getString("HORA_LLEGADA_ORIGEN"));
//                                        cf.setHora_servicio_origen(jsonConfig.isNull("HORA_SERVICIO_ORIGEN") ? null : jsonConfig.getString("HORA_SERVICIO_ORIGEN"));
//                                        cf.setDestino_dsc(jsonConfig.isNull("DESTINO_DSC") ? null : jsonConfig.getString("DESTINO_DSC"));
//                                        cf.setDestino_codigo(jsonConfig.isNull("DESTINO_CODIGO") ? null : jsonConfig.getString("DESTINO_CODIGO"));
//                                        cf.setDestino_formin(jsonConfig.isNull("DESTINO_FORMIN") ? null : jsonConfig.getString("DESTINO_FORMIN"));
//
//                                        cf.setHora_llegada_destino(jsonConfig.isNull("HORA_LLEGADA_DESTINO") ? null : jsonConfig.getString("HORA_LLEGADA_DESTINO"));
//                                        cf.setFolio_dte(jsonConfig.isNull("FOLIO_DTE") ? null : jsonConfig.getInt("FOLIO_DTE"));
//                                        cf.setPlanta_destino(jsonConfig.isNull("PLANTA_DESTINO") ? null : jsonConfig.getString("PLANTA_DESTINO"));
//                                        //cf.setPlanta_dsc(jsonConfig.isNull("PLANTA_DSC")?null: jsonConfig.getString("PLANTA_DSC"));
//                                        cf.setProv_rut_folio(jsonConfig.isNull("PROV_RUT_FOLIO") ? null : jsonConfig.getString("PROV_RUT_FOLIO"));
//                                        cf.setProv_razon_social(jsonConfig.isNull("PROV_RAZON_SOCIAL") ? null : jsonConfig.getString("PROV_RAZON_SOCIAL"));
//                                        cf.setPlanta_origen(jsonConfig.isNull("PLANTA_ORIGEN") ? null : jsonConfig.getString("PLANTA_ORIGEN"));
//                                        /*cf.setPrma_codigo_presentacion(jsonConfig.isNull("PRMA_CODIGO_PRESENTACION")?null: jsonConfig.getString("PRMA_CODIGO_PRESENTACION"));
//                                        cf.setPrma_esma_codigo_especie(jsonConfig.isNull("PRMA_ESMA_CODIGO_ESPECIE")?null: jsonConfig.getString("PRMA_ESMA_CODIGO_ESPECIE"));
//                                        cf.setPrma_nombre(jsonConfig.isNull("PRMA_NOMBRE")?null: jsonConfig.getString("PRMA_NOMBRE"));*/
//
//                                        cf.setCaac_geo_sup_izq_x(jsonConfig.isNull("CAAC_GEO_SUP_IZQ_X") ? null : jsonConfig.getString("CAAC_GEO_SUP_IZQ_X"));
//                                        cf.setCaac_geo_sup_izq_y(jsonConfig.isNull("CAAC_GEO_SUP_IZQ_Y") ? null : jsonConfig.getString("CAAC_GEO_SUP_IZQ_Y"));
//                                        cf.setCaac_geo_inf_der_x(jsonConfig.isNull("CAAC_GEO_INF_DER_X") ? null : jsonConfig.getString("CAAC_GEO_INF_DER_X"));
//                                        cf.setCaac_geo_inf_der_y(jsonConfig.isNull("CAAC_GEO_INF_DER_Y") ? null : jsonConfig.getString("CAAC_GEO_INF_DER_Y"));
//                                        cf.setCum_id_asicam(jsonConfig.isNull("CUM_ID_ASICAM") ? null : jsonConfig.getInt("CUM_ID_ASICAM"));
//                                        //cf.setClave_asicam("1234");
//                                        cf.setClave_asicam(jsonConfig.isNull("CLAVE_ASICAM") ? null : jsonConfig.getString("CLAVE_ASICAM"));
//                                        cf.setPeriodo_asicam(jsonConfig.isNull("cum_periodo") ? null : jsonConfig.getString("cum_periodo"));
//
//                                        OT.add(cf);
//                                        OrdenesTransporteDao o = new OrdenesTransporteDao(context);
//                                        try {
//                                            o.open();
//                                            o.deleteOrdenTransporte(_OT);
//                                            o.close();
//                                            o.open();
//                                            o.insertOrdenTransporteGDE(OT, patente);
//                                            o.close();
//
//                                            llenaGDE(sharedPref.getString("OT", ""));
//                                            //
//                                            new DownloadFile().execute(Constantes.URL_WS_DEFECTO + "recuperaGDE?OT=" + sharedPref.getString("OT", ""), sharedPref.getString("OT", ""), sharedPref.getString("GDE", ""));
//                                            MainActivity.tabLayout.getTabAt(2).select();
//
//                                            // new DownloadFileCartolas().execute();
//
//                                            //descargaCartolas(context);
//                                        } catch (Exception e) {
//                                            o.close();
//                                            Utility.ShowMessage(context, e.getMessage());
//                                        }
//                                    } else {
//                                        ocultarSinOT();
//                                        llenaGDE(sharedPref.getString("OT", ""));
//                                    }
//                                }
//                                //Utilities.ShowMessage(context, resp[0].toString());
//                            } else {
//                                Utility.ShowMessage(context, "Error al rescatar información de sistema central.");
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
//                        }
//
//                        pDialog.dismiss();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Resultado.setText("That didn't work! " + error.toString());
//                        //Utilities.ShowMessage(context, "That didn't work! " + error.toString());
//                        pDialog.hide();
//                        pDialog.dismiss();
//                    }
//                });
//
//        queue.add(stringRequest);
//        try {
//            descargaCartolas();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void recuperaGDE(String OT, String GDE) {
        try {
            POSTAPIRequest postapiRequest = new POSTAPIRequest();
            String url = Constantes.URL_WS_DEFECTO + "RECUPERA_GDE";
            JSONObject params = new JSONObject();
            try {
                params.put("OT", Utility.IsNull(OT, ""));
                params.put("SESSION", Utility.getPreferenciaObject("session", context));
            } catch (Exception e) {
                e.printStackTrace();
            }
            postapiRequest.request(context, new FetchDataListener() {
                @Override
                public void onFetchComplete(JSONObject data) {
                    //Fetch Complete. Now stop progress bar  or loader
                    //you started in onFetchStart
                    //RequestQueueService.cancelProgressDialog();
                    try {
                        //Now check result sent by our POSTAPIRequest class
                        if (data != null) {
                            if (data.has("success")) {
                                boolean success = data.getBoolean("success");
                                if (success == true) {
                                    JSONObject _SESSION = data.getJSONObject("SESSION");
                                    if (Utility.validaTokenSession(_SESSION) == false) {

                                        Utility.ShowMessage(context, "Error al rescatar información de sesión.");
                                    } else {
                                        Utility.setPreferencia("session", _SESSION, context);
                                        try {
                                            //LLENAR RESPUESTA
                                            JSONObject resultado = data.getJSONObject("resultado");
                                            if (resultado != null) {
                                                String fileName;
                                                String folder, despacho;
                                                folder = Constantes.RUTA_ENVIO;
                                                despacho = Constantes.RUTA_ENVIO_DESPACHO;
                                                fileName = "GDE_" + OT + "_" + GDE + "_" + sharedPref.getString("patente", "") + ".xml";

                                                String base64Data = Utility.IsNull(resultado.getString("XML"), "");
                                                ConversionBase64ToXML(base64Data, folder, fileName);
                                                if (tipoCamion.equals("2")) {
                                                    ConversionBase64ToXML(base64Data, despacho, fileName);
                                                    try {
                                                        File fOrigen = new File(folder + "OT_" + OT + ".sql");
                                                        File fDestino = new File(despacho + "OT_" + OT + ".sql");
                                                    }
                                                    catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }


                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
                                        }
                                    }



                                } else {
                                    String message = data.getString("message");
                                    Utility.ShowMessage(context, message);
                                }
                            }
                        } else {
                            Utility.ShowMessage(context, "Error al rescatar información de sistema central.");

                        }
                    } catch (Exception e) {
                        Utility.ShowMessage(context, "Ocurrió un error inesperado." + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFetchFailure(String msg) {
                    //RequestQueueService.cancelProgressDialog();
                    Utility.ShowMessageWebApi(context, msg);
                }

                @Override
                public void onFetchStart() {
                    //RequestQueueService.showProgressDialog((Activity) context);
                    //pDialog.show();
                }
            }, params, url, null);
        } catch (Exception ex) {
            Utility.ShowMessage(context, ex.getMessage());
        }

    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

//    private static void descargaMaestro() {
//        RequestQueue queue = VolleySingleton.getIntanciaVolley(context).getRequestQueue();
//
//        String url = Constantes.URL_WS_DEFECTO + "GET_PARAMETROS?CODIGO=" + Constantes.token + "&p_cancha=1&p_fecha=20190210";
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray jsonArray = null;
//                            JSONObject json = new JSONObject(response);
//
//                            Cartolas cf = new Cartolas();
//                            boolean success = json.getBoolean("success");
//
//                            if (success == true) {
//
//                                jsonArray = json.getJSONArray("dataset");
//                                ArrayList<Cartolas> cartolasArrayList = new ArrayList<Cartolas>();
//                                for (int i = 0; i < jsonArray.length(); i++) {
//
//                                    JSONObject jsonConfig = jsonArray.getJSONObject(i);
//
//                                    cf.setCancha(jsonConfig.isNull("cancha") ? null : jsonConfig.getInt("cancha"));
//                                    cf.setCancha_nombre(jsonConfig.isNull("cancha_nombre") ? null : jsonConfig.getString("cancha_nombre"));
//                                    cf.setCancha_nom_abrev(jsonConfig.isNull("cancha_nom_abrev") ? null : jsonConfig.getString("cancha_nom_abrev"));
//                                    cf.setFundo(jsonConfig.isNull("fundo") ? null : jsonConfig.getInt("fundo"));
//                                    cf.setNombre_fundo(jsonConfig.isNull("nombre_fundo") ? null : jsonConfig.getString("nombre_fundo"));
//                                    cf.setNum_cartola(jsonConfig.isNull("num_cartola") ? null : jsonConfig.getString("num_cartola"));
//                                    cf.setFecha_volteo(jsonConfig.isNull("fecha_volteo") ? null : jsonConfig.getString("fecha_volteo"));
//                                    cf.setRodal(jsonConfig.isNull("rodal") ? null : jsonConfig.getInt("rodal"));
//                                    cf.setRol_numero(jsonConfig.isNull("rol_numero") ? null : jsonConfig.getInt("rol_numero"));
//                                    cf.setRol_digito(jsonConfig.isNull("rol_digito") ? null : jsonConfig.getInt("rol_digito"));
//                                    cf.setEspecie(jsonConfig.isNull("especie") ? null : jsonConfig.getInt("especie"));
//                                    cf.setProducto(jsonConfig.isNull("producto") ? null : jsonConfig.getInt("producto"));
//                                    cf.setLargo(jsonConfig.isNull("largo") ? null : jsonConfig.getInt("largo"));
//                                    cf.setDestino(jsonConfig.isNull("destino") ? null : jsonConfig.getInt("destino"));
//                                    cf.setFaena_cartola(jsonConfig.isNull("faena_cartola") ? null : jsonConfig.getInt("faena_cartola"));
//                                    cf.setFaena_pdf(jsonConfig.isNull("faena_pdf") ? null : jsonConfig.getInt("faena_pdf"));
//                                    cf.setRut_eess(jsonConfig.isNull("rut_eess") ? null : jsonConfig.getInt("rut_eess"));
//                                    cf.setCod_eess_pdf(jsonConfig.isNull("cod_eess_pdf") ? null : jsonConfig.getInt("cod_eess_pdf"));
//                                    cf.setCertfor(jsonConfig.isNull("certfor") ? null : jsonConfig.getInt("certfor"));
//                                    cf.setGlosa_certfor(jsonConfig.isNull("glosa_certfor") ? null : jsonConfig.getString("glosa_certfor"));
//                                    cf.setFsc(jsonConfig.isNull("fsc") ? null : jsonConfig.getInt("fsc"));
//                                    cf.setGlosa_fsc(jsonConfig.isNull("glosa_fsc") ? null : jsonConfig.getString("glosa_fsc"));
//                                    cf.setCoord_x_fundo(jsonConfig.isNull("coord_x_fundo") ? null : jsonConfig.getInt("coord_x_fundo"));
//                                    cf.setCoord_y_fundo(jsonConfig.isNull("coord_y_fundo") ? null : jsonConfig.getInt("coord_y_fundo"));
//                                    cf.setCoord_x_cartola(jsonConfig.isNull("coord_x_cartola") ? null : jsonConfig.getInt("coord_x_cartola"));
//                                    cf.setCoord_y_cartola(jsonConfig.isNull("coord_y_cartola") ? null : jsonConfig.getInt("coord_y_cartola"));
//
//                                    cartolasArrayList.add(cf);
//                                }
//                                CartolasDao o = new CartolasDao(context);
//                                o.open();
//                                o.deleteCartolas(context);
//                                o.close();
//                                o.open();
//                                o.insertCartolas(cartolasArrayList, context);
//                                o.close();
//
//
//                            }else {
//
//                            }
//
//                        } catch (Exception e) {
//
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//
//        queue.add(stringRequest);
//    }

    public static void llenarPresentacion(String OT) {
        OrdenesTransporteDao o = new OrdenesTransporteDao(context);
        o.open();
        try {
            OrdenesTransporte orden = o.getOrdenTransporteById(context, OT);
            presentacion.tv_PlantaP.setText("PLANTA " + orden.getDestino_dsc().toUpperCase());
            presentacion.tv_estadoP.setText("PENDIENTE");
            presentacion.txtPatCamP.setText(patente);
            //presentacion.txtFechaP.setText("");
            presentacion.txtRutProP.setText(orden.getProv_rut_folio() + "-" + Utility.validarRutDV(Integer.parseInt(orden.getProv_rut_folio())));
            presentacion.txtNomProP.setText(orden.getProv_razon_social());
            presentacion.txtRutCondP.setText(orden.getRut_chofer() + "-" + Utility.validarRutDV(Integer.parseInt(orden.getProv_rut_folio())));
            presentacion.txtNomCondP.setText(orden.getNombre_chofer());
            try {
                presentacion.txtNumGD.setText(orden.getFolio_dte().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Utility.caac_geo_sup_izq_x = orden.getCaac_geo_sup_izq_x();
                Utility.caac_geo_sup_izq_y = orden.getCaac_geo_sup_izq_y();
                Utility.caac_geo_inf_der_x = orden.getCaac_geo_inf_der_x();
                Utility.caac_geo_inf_der_y = orden.getCaac_geo_inf_der_y();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                presentacion.txtDiferenciaHoraria.setText(orden.getDiferenciaRecep().replace(".", ":"));
                presentacion.txtRecep.setText(orden.getRecepId().toString());
                presentacion.txtRealDestino.setText(orden.getFechaRecep().replace("/", "-"));
                presentacion.txtLlegadaDestinoP.setText(orden.getHora_llegada_destino());
                presentacion.tv_estadoP.setText("ACEPTADO");
            } catch (Exception e) {
                e.printStackTrace();
            }
            presentacion.txtCarroP.setText(orden.getPatente_carro());
            presentacion.txtEEPPP.setText(orden.getProducto_codigo_desc());

        } catch (Exception e) {
            e.printStackTrace();
        }
        o.close();
    }

    public static void llenarOT(String OT) {
        OrdenesTransporteDao o = new OrdenesTransporteDao(context);
        o.open();
        try {

            OrdenesTransporte orden = o.getOrdenTransporteById(context, OT);
            cl.genesys.appchofer.layout.OT.tv_OT.setText("OT N: " + orden.getNumero_ot().toString());
            if (orden.getRut_chofer() != null) {
                try {
                    if (orden.getRut_chofer() != null) {
                        cl.genesys.appchofer.layout.OT.tv_conductor.setText(orden.getRut_chofer() + "-" + Utility.validarRutDV(Integer.parseInt(orden.getRut_chofer())) + " " + orden.getNombre_chofer());

                    }
                    cl.genesys.appchofer.layout.OT.tv_ayuda_rut.setVisibility(View.VISIBLE);
                    cl.genesys.appchofer.layout.OT.lly_rut.setVisibility(View.VISIBLE);
                    cl.genesys.appchofer.layout.OT.edtRutChofer.setVisibility(View.VISIBLE);
                    cl.genesys.appchofer.layout.OT.btnGuardarRutC.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    cl.genesys.appchofer.layout.OT.tv_ayuda_rut.setVisibility(View.VISIBLE);
                    cl.genesys.appchofer.layout.OT.lly_rut.setVisibility(View.VISIBLE);
                    cl.genesys.appchofer.layout.OT.edtRutChofer.setVisibility(View.VISIBLE);
                    cl.genesys.appchofer.layout.OT.btnGuardarRutC.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            } else {
                cl.genesys.appchofer.layout.OT.tv_ayuda_rut.setVisibility(View.VISIBLE);
                cl.genesys.appchofer.layout.OT.lly_rut.setVisibility(View.VISIBLE);
                cl.genesys.appchofer.layout.OT.edtRutChofer.setVisibility(View.VISIBLE);
                cl.genesys.appchofer.layout.OT.btnGuardarRutC.setVisibility(View.VISIBLE);
            }
            if (tipoCamion.equals("2")) {
                llAutoCargante.setVisibility(View.VISIBLE);

            }
            cl.genesys.appchofer.layout.OT.tv_producto.setText(orden.getProducto_codigo_desc());
            cl.genesys.appchofer.layout.OT.tv_grua.setText(orden.getGrua());
            cl.genesys.appchofer.layout.OT.tv_carro.setText(orden.getPatente_carro());
            cl.genesys.appchofer.layout.OT.tv_inicio_viaje.setText(orden.getHora_inicio_viaje());
            cl.genesys.appchofer.layout.OT.tv_origen.setText(orden.getOrigen_formin() + " " + orden.getOrigen_desc());
            cl.genesys.appchofer.layout.OT.tv_llegada_origen.setText(orden.getHora_llegada_origen());
            cl.genesys.appchofer.layout.OT.tv_servicio_origen.setText(orden.getHora_servicio_origen());
            cl.genesys.appchofer.layout.OT.tv_destino.setText(orden.getDestino_formin() + " " + orden.getDestino_dsc());
            cl.genesys.appchofer.layout.OT.tv_llegada_destino.setText(orden.getHora_llegada_destino());
            try {
                Utility.caac_geo_sup_izq_x = orden.getCaac_geo_sup_izq_x();
                Utility.caac_geo_sup_izq_y = orden.getCaac_geo_sup_izq_y();
                Utility.caac_geo_inf_der_x = orden.getCaac_geo_inf_der_x();
                Utility.caac_geo_inf_der_y = orden.getCaac_geo_inf_der_y();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Integer asicam = orden.getCum_id_asicam();
            cl.genesys.appchofer.layout.OT.tv_asicam.setText("ASICAM: " + asicam);
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }

            o.open();
            if (o.validaConfirmaOt()) {
                cl.genesys.appchofer.layout.OT.btnConfirmarOT.setVisibility(View.VISIBLE);
            } else {
                cl.genesys.appchofer.layout.OT.btnConfirmarOT.setVisibility(View.GONE);
            }
            o.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        o.close();
    }

    public static void llenaGDE(String OT) {
        OrdenesTransporteDao o = new OrdenesTransporteDao(context);
        o.open();
        try {
            OrdenesTransporte orden = o.getOrdenTransporteById(context, OT);
            GDE.tv_OTGDE.setText("OT N: " + OT);
            GDE.tv_GDE.setText("GDE N: " + orden.getFolio_dte());
            GDE.tv_conductorGDE.setText(orden.getRut_chofer() + "-" + Utility.validarRutDV(Integer.parseInt(orden.getRut_chofer())) + " " + orden.getNombre_chofer());
            GDE.tv_productoGDE.setText(orden.getProducto_codigo_desc());
            GDE.tv_carroGDE.setText(orden.getPatente_carro());
            GDE.tv_origenGDE.setText(orden.getOrigen_formin() + " " + orden.getOrigen_desc());
            GDE.tv_destinoGDE.setText(orden.getDestino_formin() + " " + orden.getDestino_dsc());
            GDE.tv_llegada_origenGDE.setText(orden.getHora_llegada_origen());
            GDE.tv_llegada_DestinoGDE.setText(orden.getHora_llegada_destino());
            GDE.tv_carguioGDE.setText(orden.getHora_servicio_origen());
            GDE.tv_camionGDE.setText(patente);
            try {
                Utility.caac_geo_sup_izq_x = orden.getCaac_geo_sup_izq_x();
                Utility.caac_geo_sup_izq_y = orden.getCaac_geo_sup_izq_y();
                Utility.caac_geo_inf_der_x = orden.getCaac_geo_inf_der_x();
                Utility.caac_geo_inf_der_y = orden.getCaac_geo_inf_der_y();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        o.close();
    }

    private static void solicitudAdicional() {
        try {


            POSTAPIRequest postapiRequest = new POSTAPIRequest();
            String url = Constantes.URL_WS_DEFECTO + "SOLICITUD_ADICIONAL";
            JSONObject params = new JSONObject();
            try {
                params.put("CAMION", Utility.IsNull(Utility.getPreferenciaString("patente", context), "").toUpperCase());
                params.put("SESSION", Utility.getPreferenciaObject("session", context));
            } catch (Exception e) {
                e.printStackTrace();
            }
            postapiRequest.request(context, new FetchDataListener() {
                @Override
                public void onFetchComplete(JSONObject data) {
                    //Fetch Complete. Now stop progress bar  or loader
                    //you started in onFetchStart
                    RequestQueueService.cancelProgressDialog();
                    try {
                        if (pDialog != null) {
                            pDialog.hide();
                            pDialog.dismiss();
                        }
                        //Now check result sent by our POSTAPIRequest class
                        if (data != null) {
                            if (data.has("success")) {
                                boolean success = data.getBoolean("success");
                                if (success == true) {
                                    JSONObject _SESSION = data.getJSONObject("SESSION");
                                    if (Utility.validaTokenSession(_SESSION) == false) {
                                        Utility.ShowMessage(context, "Error al rescatar información de sesión.");
                                    } else {
                                        Utility.setPreferencia("session", _SESSION, context);
                                        try {
                                            //LLENAR RESPUESTA
                                            JSONObject resultado = data.getJSONObject("resultado");
                                            if (resultado != null) {
                                                String _SALIDA = Utility.IsNull(resultado.getString("SALIDA"), "");
                                                if (_SALIDA.equals("1")) {
                                                    Toast.makeText(context, "Solicitud enviada correctamente.", Toast.LENGTH_SHORT).show();
                                                } else if (_SALIDA.equals("2")) {
                                                    Toast.makeText(context, "Solicitud modificada correctamente.", Toast.LENGTH_SHORT).show();
                                                } else if (_SALIDA.equals("0")) {
                                                    Toast.makeText(context, "Ocurrió un error, favor contactar.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
                                        }
                                    }
                                } else {
                                    String message = data.getString("message");
                                    Utility.ShowMessage(context, message);
                                }
                            }
                        } else {
                            Utility.ShowMessage(context, "Error al rescatar información de sistema central.");

                        }
                    } catch (Exception e) {
                        Utility.ShowMessage(context, "Ocurrió un error inesperado." + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFetchFailure(String msg) {
                    RequestQueueService.cancelProgressDialog();
                    Utility.ShowMessageWebApi(context, msg);
                    if (pDialog != null) {
                        pDialog.hide();
                        pDialog.dismiss();
                    }
                }

                @Override
                public void onFetchStart() {

                    RequestQueueService.showProgressDialog((Activity) context);
                }
            }, params, url, null);

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Enviando solicitud...");
            pDialog.setCancelable(true);
            try {
                pDialog.show();
            } catch (Exception e) {

            }
        } catch (Exception ex) {
            Utility.ShowMessage(context, ex.getMessage());
        }

//        sharedPref = context.getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
//
//        RequestQueue queue = VolleySingleton.getIntanciaVolley(context).getRequestQueue();
//
//        String url = Constantes.URL_WS_DEFECTO + "SOLICITUD_ADICIONAL?CODIGO=" + cl.genesys.appchofer.utilities.Constantes.token + "&PATENTE=" + sharedPref.getString("patente", "").toUpperCase();
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            if (response.equals("1")) {
//                                Toast.makeText(context, "Solicitud enviada correctamente.", Toast.LENGTH_SHORT).show();
//                            } else if (response.equals("2")) {
//                                Toast.makeText(context, "Solicitud modificada correctamente.", Toast.LENGTH_SHORT).show();
//                            } else if (response.equals("0")) {
//                                Toast.makeText(context, "Ocurrió un error, favor contactar.", Toast.LENGTH_SHORT).show();
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
//                        }
//
//                        pDialog.dismiss();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Resultado.setText("That didn't work! " + error.toString());
//                        //Utilities.ShowMessage(context, "That didn't work! " + error.toString());
//                        pDialog.hide();
//                        pDialog.dismiss();
//                    }
//                });
//
//        queue.add(stringRequest);
//
//        pDialog = new ProgressDialog(context);
//        pDialog.setMessage("Enviando solicitud...");
//        pDialog.setCancelable(true);
//        try {
//            pDialog.show();
//        } catch (Exception e) {
//
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (patente.equals("")){
            Intent intent = new Intent(context, ingreso_patente.class);
            startActivityForResult(intent, 0);
        } else {
            String empresa = sharedPref.getString("empresa", "");
           /* if (empresa.length() > 23) {
                empresa = empresa.substring(0, 23);
            }*/
            txEmpresa.setText(empresa);
            tvPatente.setText(sharedPref.getString("patente", ""));
            String OT = sharedPref.getString("OT", "");
            if (OT.equals("")) {
                srvSinOt.setVisibility(View.VISIBLE);
            } else {
                ocultarSinOT();
            }


            /*String viaje_destino = sharedPref.getString("viaje_destino", "");

            if(viaje_destino.equals("1")){
                MainActivity.tvPatente.setBackgroundResource(R.drawable.patente_radius_iniciada);
            }else {
                MainActivity.tvPatente.setBackgroundResource(R.drawable.patente_radius);
            }*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        try {
            super.onPause();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ocultarSinOT() {
        srvSinOt.setVisibility(View.GONE);
        tx_version_mainactivity.setVisibility(View.GONE);
        btnSolicitudAdicional.setVisibility(View.GONE);
        fabUpdate.hide();  //.setVisibility(View.GONE);
        tv_sinOT.setVisibility(View.GONE);
        tv_update.setVisibility(View.GONE);
    }

    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        if (tipoCamion.equals("2")) {
            MenuItem item = menu.findItem(R.id.btn_compartir);
            item.setVisible(false);

            item = menu.findItem(R.id.btn_compartir_QR);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        String confirmaGDE = sharedPref.getString("confirmaGDE", "");
        //Checking if the item is in checked state or not, if not make it in checked state
        if (menuItem.isChecked()) menuItem.setChecked(false);
        else menuItem.setChecked(true);

        //Closing drawer on item click

        //Check to see which item was being clicked and perform appropriate action
        switch (menuItem.getItemId()) {
           /* case R.id.btnCodigo:
                if (!ApManager.isApOn(getApplicationContext())) {
                    ApManager.turnOnHotspot(getApplicationContext());
                }
                return true;
            */

            case R.id.btn_compartir:
                if (confirmaGDE.equals("1")) {
                    finish();
                    Intent intent = new Intent(context, WiFiDirectActivity.class);
                    startActivity(intent);
                } else {
                    Utility.ShowAlertDialog(context, "Viaje no iniciado", "Debe confirmar GDE para compartir información con despacho.");
                }
                return true;
            case R.id.btn_compartir_QR:
                if (confirmaGDE.equals("1")) {
                    MainActivity.tabLayout.getTabAt(4).select();
                } else {
                    Utility.ShowAlertDialog(context, "Viaje no iniciado", "Debe confirmar GDE para compartir información con despacho.");
                }
                return true;
            case R.id.btnSalir:
                alertDialogBuilderClose();
                return true;
           /* case R.id.btnPrueba:
                new DownloadFile().execute(Constantes.URL_WS_DEFECTO + "recuperaGDE?OT=" + "223600", "223600", "775115");
                return true;*/
            default:
                return true;
        }
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment;

        if (tipoCamion.equals("2")) {
            switch (i) {
                case 0:

                    fragment = new programa_dia();
                    break;
                case 1:
                    fragment = new OT();
                    break;
                case 2:
                    fragment = new GDE();
                    break;
                case 3:
                    fragment = new presentacion();
                    break;
                case 4:
                    fragment = new eventos();
                    break;

                default:
                    fragment = null;
            }
        }else {
            switch (i) {
                case 0:

                    fragment = new programa_dia();
                    break;
                case 1:
                    fragment = new OT();
                    break;
                case 2:
                    fragment = new GDE();
                    break;
                case 3:
                    fragment = new presentacion();
                    break;
                case 4:
                    fragment = new descargarInformacion();
                    break;
                case 5:
                    fragment = new eventos();
                    break;

                default:
                    fragment = null;
            }
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (tipoCamion.equals("2")) {
            switch (position) {
                case 0:
                    return "P. DÍA ";
                case 1:
                    return "OT";
                case 2:
                    return "GDE ";
                case 3:
                    return "DESTINOS";
                case 4:
                    return "EVENTOS";
            }
        }
        else {
            switch (position) {
                case 0:
                    return "P. DÍA ";
                case 1:
                    return "OT";
                case 2:
                    return "GDE ";
                case 3:
                    return "DESTINOS";
                case 4:
                    return "TRANSMITIR";
                case 5:
                    return "EVENTOS";
            }
        }
        return null;
    }
}

    private static class DownloadFile extends AsyncTask<String, String, String> {

    private ProgressDialog progressDialog;
    private String fileName;
    private String folder;
    private boolean isDownloaded;

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.progressDialog.setCancelable(false);
        try {
            this.progressDialog.show();

        } catch (Exception e) {

        }
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();
            // getting file length
            int lengthOfFile = connection.getContentLength();


            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            //Extract file name from URL
            fileName = "GDE_" + f_url[1] + "_" + f_url[2] + "_" + sharedPref.getString("patente", "") + ".xml";

            //Append timestamp to file name

            //External directory path to save file
          /*  File rutaArchivos = new File(Environment.getExternalStorageDirectory() + "/"
                    + context.getPackageName());*/
            folder = Constantes.RUTA_ENVIO;

            //Create androiddeft folder if it does not exist
            File directory = new File(folder).getAbsoluteFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            try {
               /* File file = new File(Constantes.RUTA_ENVIO, "OT_" + f_url[1] + ".sql");
                file.createNewFile();
                OrdenesTransporteDao o = new OrdenesTransporteDao(context);
                ProgramaAsicamDao _PDao = new ProgramaAsicamDao(context);
                //CartolasDao cartolasDao = new CartolasDao(context);
                o.open();
                try {
                    String _script = o.getOrdenTransporteInsertScriptById(context, f_url[1], patente);

                    OutputStreamWriter outputStreamWriter = null;
                    outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));

                    outputStreamWriter.write(_script);

                    outputStreamWriter.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                o.close();
*/

                /*file = new File(Constantes.RUTA_ENVIO, "ProgramaDia.sql");
                file.createNewFile();
                _PDao.open();
                try {
                    //String _script = _PDao.getProgramaDiaInsertScriptById(context);

                    OutputStreamWriter outputStreamWriter = null;
                    outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));

                    outputStreamWriter.write(_script);

                    outputStreamWriter.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                _PDao.close();*/

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Output stream to write file
            OutputStream output = new FileOutputStream(folder + fileName);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lengthOfFile));
                Log.d("", "Descargando: " + (int) ((total * 100) / lengthOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
            return "GDE descargada";

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return "Error, favor contactar";
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        progressDialog.setProgress(Integer.parseInt(progress[0]));
    }


    @Override
    protected void onPostExecute(String message) {
        // dismiss the dialog after the file was downloaded
        this.progressDialog.dismiss();

        // Display File path after downloading
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();


    }
}

    public static void actualizaGPS(String _OT, double latitud, double longitud) {
        try {


            POSTAPIRequest postapiRequest = new POSTAPIRequest();
            String url = Constantes.URL_WS_DEFECTO + "MODIFICA_COORDENADAS";
            JSONObject params = new JSONObject();
            try {
                params.put("OT", Utility.IsNull(_OT, ""));
                params.put("LATITUD", Utility.IsNull(latitud, 0.0));
                params.put("LONGITUD", Utility.IsNull(longitud, 0.0));
                params.put("SESSION", Utility.getPreferenciaObject("session", context));
            } catch (Exception e) {
                e.printStackTrace();
            }
            postapiRequest.request(context, new FetchDataListener() {
                @Override
                public void onFetchComplete(JSONObject data) {
                    //Fetch Complete. Now stop progress bar  or loader
                    //you started in onFetchStart
                    //RequestQueueService.cancelProgressDialog();
                    try {
                        if (pDialog != null) {
                            pDialog.hide();
                            pDialog.dismiss();
                        }
                        //Now check result sent by our POSTAPIRequest class
                        if (data != null) {
                            if (data.has("success")) {
                                boolean success = data.getBoolean("success");
                                if (success == true) {
                                    JSONObject _SESSION = data.getJSONObject("SESSION");
                                    if (Utility.validaTokenSession(_SESSION) == false) {
                                        Utility.ShowMessage(context, "Error al rescatar información de sesión.");
                                    } else {
                                        Utility.setPreferencia("session", _SESSION, context);
                                        try {
                                            //LLENAR RESPUESTA
//                                            String _SALIDA = Utility.IsNull(resultado.getString("SALIDA"), "");
//                                            if (_SALIDA.equals("1")) {
//
//                                            } else {
//                                                //Toast.makeText(context,  response, Toast.LENGTH_SHORT).show();
//                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            //Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
                                        }
                                    }

                                } else {
                                    String message = data.getString("message");
//                                    Utility.ShowMessage(context, message);
                                }
                            }
                        } else {
//                            Utility.ShowMessage(context, "Error al rescatar información de sistema central.");

                        }
                    } catch (Exception e) {
//                        Utility.ShowMessage(context, "Ocurrió un error inesperado." + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFetchFailure(String msg) {
                    //RequestQueueService.cancelProgressDialog();
                    if (pDialog != null) {
                        pDialog.hide();
                        pDialog.dismiss();
                    }
//                    Utility.ShowMessageWebApi(context, msg);
                }

                @Override
                public void onFetchStart() {

                    //RequestQueueService.showProgressDialog((Activity) context);
                }
            }, params, url, null);
        } catch (Exception ex) {
            Utility.ShowMessage(context, ex.getMessage());
        }
//        RequestQueue queue = VolleySingleton.getIntanciaVolley(context).getRequestQueue();
//
//        String url = Constantes.URL_WS_DEFECTO + "modifica_coordenadas?" +
//                "CODIGO=" + Constantes.token +
//                "&numero_ot=" + _OT +
//                "&latitud=" + latitud +
//                "&longitud=" + longitud;
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//
//                            if (response.equals("1")) {
//
//                            } else {
//                                //Toast.makeText(context,  response, Toast.LENGTH_SHORT).show();
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//
//        queue.add(stringRequest);
    }

    public static void llenarPrgramaAsicam() {
        try {

            POSTAPIRequest postapiRequest = new POSTAPIRequest();
            String url = Constantes.URL_WS_DEFECTO + "DESCARGAPROGRAMAASICAM";
            JSONObject params = new JSONObject();
            try {
                params.put("CAMION", Utility.IsNull(Utility.getPreferenciaString("patente", context), ""));
                params.put("SESSION", Utility.getPreferenciaObject("session", context));
            } catch (Exception e) {
                e.printStackTrace();
            }
            postapiRequest.request(context, new FetchDataListener() {
                @Override
                public void onFetchComplete(JSONObject data) {
                    //Fetch Complete. Now stop progress bar  or loader
                    //you started in onFetchStart
                    //RequestQueueService.cancelProgressDialog();
                    try {
                        if (pDialog != null) {
                            pDialog.hide();
                            pDialog.dismiss();
                        }
                        //Now check result sent by our POSTAPIRequest class
                        if (data != null) {
                            if (data.has("success")) {
                                boolean success = data.getBoolean("success");
                                if (success == true) {
                                    JSONObject _SESSION = data.getJSONObject("SESSION");
                                    if (Utility.validaTokenSession(_SESSION) == false) {
                                        Utility.ShowMessage(context, "Error al rescatar información de sesión.");
                                    } else {
                                        Utility.setPreferencia("session", _SESSION, context);
                                        try {
                                            //LLENAR RESPUESTA
                                            JSONArray jsonArray = null;
                                            List<ProgramaAsicam> programaAsicams = new ArrayList<ProgramaAsicam>();
                                            final ProgramaAsicamDao _dao = new ProgramaAsicamDao(context);
                                            jsonArray = data.getJSONArray("vTabla");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonConfig = jsonArray.getJSONObject(i);
                                                _dao.open();
                                                try {

                                                    ProgramaAsicam programaAsicam = new ProgramaAsicam();
                                                    programaAsicam.setOT(jsonConfig.getInt("OT"));
                                                    programaAsicam.setGde(jsonConfig.getInt("GDE"));
                                                    programaAsicam.setAsicam(jsonConfig.getInt("ASICAM"));
                                                    programaAsicam.setPatente(jsonConfig.getString("PATENTE"));
                                                    programaAsicam.setTransportista(jsonConfig.getString("TRANSPORTISTA"));
                                                    programaAsicam.setHora_atencion(jsonConfig.getString("HORA_ATENCION"));
                                                    programaAsicam.setProducto(jsonConfig.getString("PRODUCTO"));
                                                    programaAsicam.setDestino(jsonConfig.getString("DESTINO"));
                                                    programaAsicam.setConfirma_ot(jsonConfig.getString("CONFIRMA_OT"));
                                                    programaAsicam.setEstado(jsonConfig.getInt("ESTADO"));
                                                    programaAsicams.add(programaAsicam);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                _dao.close();
                                            }

                                            ProgramaAsicamDao dao = new ProgramaAsicamDao(context);
                                            dao.open();
                                            dao.deletePrograma(context);
                                            dao.close();
                                            dao.insertPrograma(programaAsicams, context);
                                            _dao.open();
                                            List<ProgramaAsicam> _OTs = null;
                                            try {
                                                _OTs = _dao.getProductoProgramaAsicam(MainActivity.context, patente);
                                                if (_OTs.size() > 0) {
                                                    programa_dia.RVAdapter adapter = new programa_dia.RVAdapter(_OTs);
                                                    programa_dia.rv.setAdapter(adapter);
                                                } else {

                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            _dao.close();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
                                        }
                                    }

                                } else {
                                    String message = data.getString("message");
                                    Utility.ShowMessage(context, message);
                                }
                            }
                        } else {
                            Utility.ShowMessage(context, "Error al rescatar información de sistema central.");

                        }
                    } catch (Exception e) {
                        Utility.ShowMessage(context, "Ocurrió un error inesperado." + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFetchFailure(String msg) {
                    //RequestQueueService.cancelProgressDialog();
                    Utility.ShowMessageWebApi(context, msg);
                    if (pDialog != null) {
                        pDialog.hide();
                        pDialog.dismiss();
                    }
                }

                @Override
                public void onFetchStart() {

                    //RequestQueueService.showProgressDialog((Activity) context);
                }
            }, params, url, new DefaultRetryPolicy(30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } catch (Exception ex) {
            Utility.ShowMessage(context, ex.getMessage());
        }

//        RequestQueue queue = VolleySingleton.getIntanciaVolley(context).getRequestQueue();
//
//        String url = Constantes.URL_WS_DEFECTO + "descargaProgramaAsicam?CODIGO=" + Constantes.token + "&PATENTE=" + patente;
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray jsonArray = null;
//                            JSONObject json = new JSONObject(response.toString());
//                            List<ProgramaAsicam> programaAsicams = new ArrayList<ProgramaAsicam>();
//                            final ProgramaAsicamDao _dao = new ProgramaAsicamDao(context);
//
//                            boolean success = json.getBoolean("success");
//
//                            if (success == true) {
//
//                                jsonArray = json.getJSONArray("dataset");
//
//                                for (int i = 0; i < jsonArray.length(); i++) {
//
//
//                                    JSONObject jsonConfig = jsonArray.getJSONObject(i);
//
//                                    _dao.open();
//                                    try {
//
//                                        ProgramaAsicam programaAsicam = new ProgramaAsicam();
//                                        programaAsicam.setOT(jsonConfig.getInt("OT"));
//                                        programaAsicam.setGde(jsonConfig.getInt("GDE"));
//                                        programaAsicam.setAsicam(jsonConfig.getInt("ASICAM"));
//                                        programaAsicam.setPatente(jsonConfig.getString("PATENTE"));
//                                        programaAsicam.setTransportista(jsonConfig.getString("TRANSPORTISTA"));
//                                        programaAsicam.setHora_atencion(jsonConfig.getString("HORA_ATENCION"));
//                                        programaAsicam.setProducto(jsonConfig.getString("PRODUCTO"));
//                                        programaAsicam.setDestino(jsonConfig.getString("DESTINO"));
//                                        programaAsicam.setConfirma_ot(jsonConfig.getString("CONFIRMA_OT"));
//                                        programaAsicam.setEstado(jsonConfig.getInt("ESTADO"));
//
//                                        programaAsicams.add(programaAsicam);
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    _dao.close();
//                                }
//
//                                ProgramaAsicamDao dao = new ProgramaAsicamDao(context);
//                                dao.open();
//                                dao.deletePrograma(context);
//                                dao.close();
//
//                                dao.insertPrograma(programaAsicams, context);
//
//
//                                _dao.open();
//                                List<ProgramaAsicam> _OTs = null;
//                                try {
//                                    _OTs = _dao.getProductoProgramaAsicam(MainActivity.context, patente);
//                                    if (_OTs.size() > 0) {
//                                        programa_dia.RVAdapter adapter = new programa_dia.RVAdapter(_OTs);
//                                        programa_dia.rv.setAdapter(adapter);
//                                    } else {
//
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                //_ot = _dao.getOrdenTransporteByFolioDte(getContext(), folio);
//
//                                //adapter.llenaInfo(_ot);
//                                _dao.close();
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            //Utility.ShowMessage(context, "Error al rescatar información de sistema central.");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//
//                });
//
//        queue.add(stringRequest);
    }

    public void llenarListaValores() {
        try {
            POSTAPIRequest postapiRequest = new POSTAPIRequest();
            String url = Constantes.URL_WS_DEFECTO + "LISTA_VALORES";
            JSONObject params = new JSONObject();
            try {
                params.put("SESSION", Utility.getPreferenciaObject("session", context));
            } catch (Exception e) {
                e.printStackTrace();
            }
            postapiRequest.request(context, new FetchDataListener() {
                @Override
                public void onFetchComplete(JSONObject data) {
                    //Fetch Complete. Now stop progress bar  or loader
                    //you started in onFetchStart
                    RequestQueueService.cancelProgressDialog();
                    try {
                        if (pDialog != null) {
                            pDialog.hide();
                            pDialog.dismiss();
                        }

                        //Now check result sent by our POSTAPIRequest class
                        if (data != null) {
                            if (data.has("success")) {
                                boolean success = data.getBoolean("success");
                                if (success == true) {
                                    JSONObject _SESSION = data.getJSONObject("SESSION");
                                    if (Utility.validaTokenSession(_SESSION) == false) {
                                        Utility.ShowMessage(context, "Error al rescatar información de sesión.");
                                    } else {
                                        Utility.setPreferencia("session", _SESSION, context);
                                        try {
                                            //LLENAR RESPUESTA
                                            JSONArray jsonArray = null;
                                            List<ListaValores> listaValores = new ArrayList<ListaValores>();
                                            jsonArray = data.getJSONArray("vTabla");
                                            // looping through All restaurantes
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                ListaValores cf = new ListaValores();
                                                JSONObject jsonConfig = jsonArray.getJSONObject(i);
                                                cf.setListId(jsonConfig.getString("LIST_ID"));
                                                cf.setListDesc(jsonConfig.getString("LIST_DESC"));
                                                cf.setListTipo(jsonConfig.getInt("LIST_TIPO"));
                                                listaValores.add(cf);
                                            }
                                            ListaValoresDao dao = new ListaValoresDao(context);
                                            dao.open();
                                            dao.deleteListaValores(context);
                                            dao.close();

                                            dao.insertListaValores(listaValores, context);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
                                        }
                                    }

                                } else {
                                    String message = data.getString("message");
                                    Utility.ShowMessage(context, message);
                                }
                            }
                        } else {
                            Utility.ShowMessage(context, "Error al rescatar información de sistema central.");

                        }
                    } catch (Exception e) {
                        Utility.ShowMessage(context, "Ocurrió un error inesperado." + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFetchFailure(String msg) {
                    RequestQueueService.cancelProgressDialog();
                    Utility.ShowMessageWebApi(context, msg);
                    if (pDialog != null) {
                        pDialog.hide();
                        pDialog.dismiss();
                    }
                }

                @Override
                public void onFetchStart() {

                    RequestQueueService.showProgressDialog((Activity) context);
                }
            }, params, url, null);

        } catch (Exception ex) {
            Utility.ShowMessage(context, ex.getMessage());
        }

//        RequestQueue queue = VolleySingleton.getIntanciaVolley(context).getRequestQueue();
//
//        String url = Constantes.URL_WS_DEFECTO + "LISTA_VALORES?CODIGO=" + Constantes.token;
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray jsonArray = null;
//                            JSONObject json = new JSONObject(response.toString());
//                            List<ListaValores> listaValores = new ArrayList<ListaValores>();
//
//
//                            boolean success = json.getBoolean("success");
//
//                            if (success == true) {
//
//                                jsonArray = json.getJSONArray("dataset");
//
//                                // looping through All restaurantes
//                                for (int i = 0; i < jsonArray.length(); i++) {
//
//                                    ListaValores cf = new ListaValores();
//
//                                    JSONObject jsonConfig = jsonArray.getJSONObject(i);
//                                    cf.setListId(jsonConfig.getString("LIST_ID"));
//                                    cf.setListDesc(jsonConfig.getString("LIST_DESC"));
//                                    cf.setListTipo(jsonConfig.getInt("LIST_TIPO"));
//                                    listaValores.add(cf);
//                                }
//
//                                ListaValoresDao dao = new ListaValoresDao(context);
//                                dao.open();
//                                dao.deleteListaValores(context);
//                                dao.close();
//
//                                dao.insertListaValores(listaValores, context);
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Utility.ShowMessage(context, "Error al rescatar información de sistema central.");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//
//                });
//
//        queue.add(stringRequest);

    }

    public void onBackPressed() {
        alertDialogBuilderClose();
    }

    public void alertDialogBuilderClose() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmación");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("¿Desea salir de la aplicación?")
                .setCancelable(false)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNeutralButton("Minimizar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                })
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            stopService(new Intent(MainActivity.this, ServicioConectado.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            stopService(new Intent(MainActivity.this, ServicioOTDisponible.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            //stopService(new Intent(MainActivity.this, ServicioGPS.class));
                        }catch (Exception e)  {
                            e.printStackTrace();
                        }
                        //System.exit(0);
                      /*  onDestroy();
                        finish();
                        moveTaskToBack(true);*/

                        finish();
                        //System.exit(0);
                        Intent salida = new Intent(Intent.ACTION_MAIN);
                        android.os.Process.killProcess(android.os.Process.myPid());


                        //finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private static class DownloadFileCartolas extends AsyncTask<Void, Void, Void> {

    private ProgressDialog progressDialog;
    private String fileName;
    private String folder;
    private boolean isDownloaded;

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.progressDialog.setCancelable(false);
        try {
            this.progressDialog.show();

        } catch (Exception e) {

        }
    }


    @Override
    protected Void doInBackground(Void... f_url) {
        int count;


        return null;
    }

    protected void onProgressUpdate(String... progress) {

    }


    @Override
    protected void onPostExecute(Void voids) {
        // dismiss the dialog after the file was downloaded

    }
}

}