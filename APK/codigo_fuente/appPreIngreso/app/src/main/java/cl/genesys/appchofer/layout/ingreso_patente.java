package cl.genesys.appchofer.layout;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.google.gson.Gson;

import org.json.JSONObject;

import cl.genesys.appchofer.R;
import cl.genesys.appchofer.utilities.Constantes;
import cl.genesys.appchofer.webapi.FetchDataListener;
import cl.genesys.appchofer.webapi.POSTAPIRequest;
import cl.genesys.appchofer.webapi.RequestQueueService;
import cl.genesys.appchofer.utilities.ServicioConectado;
import cl.genesys.appchofer.utilities.Utility;

public class ingreso_patente extends AppCompatActivity {
    public static Button button;
    public static RadioButton rdbConectado;
    EditText edtPatente;
    ProgressDialog pDialog;
    Context context;

    WifiManager wifiManager;
    public Boolean ValidaPatente = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ingreso_patente);
        context = this;
        //Validar versión
       /* if (android.provider.Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1){
            //Auto Rotate is on, so don't lock

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
        else {
            //Auto Rotate is off, so lock

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }*/

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);


        Utility.ShowAlertDialog(context, "Ingresar patente", "Debe ingresar patente.");


        button = findViewById(R.id.btnIngresar);
        button = findViewById(R.id.btnIngresar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                    if (!edtPatente.getText().toString().equals("")) {
                        if (Utility.isOnline(context)) {
                            button.setEnabled(false);
                            getValidaPatente();
                        } else
                            Utility.ShowMessage(context, "Equipo sin conexión a internet.");
                    } else
                        Utility.ShowMessage(context, "Debe completar patente.");
                } else {
                    wifiManager.setWifiEnabled(true);
                    Utility.ShowMessage(context, "Activando señal Wifi, intente nuevamente.");
                }
            }
        });
        edtPatente = findViewById(R.id.edtPatente);
        edtPatente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarEdt();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //System.out.println(s.toString());
            }
        });

        if (!isMyServiceRunning(ServicioConectado.class)) {
            Intent serviceIntent = new Intent(this, ServicioConectado.class);
            serviceIntent.putExtra("inputExtra", "ConvectorOT está en  funcionamiento");

            ContextCompat.startForegroundService(this, serviceIntent);
        }

        rdbConectado = findViewById(R.id.rdbConectado);


        validarEdt();


    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    /*
    public void getValidaPatente() {
        if (!ValidaPatente) {
            ValidaPatente = true;
            pDialog = new ProgressDialog(context);
            pDialog.setCancelable(false);
            pDialog.setMessage("Cargando...");
            pDialog.show();

            RequestQueue queue = VolleySingleton.getIntanciaVolley(context).getRequestQueue();

            String url = Constantes.URL_WS_DEFECTO + "OBTIENE_DATOS_PATENTE?CODIGO=" + Constantes.token + "&PATENTE=" + edtPatente.getText().toString().toUpperCase();


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                if(response.equals("3")){
                                    ValidaPatente = false;
                                    ShowAlertDialog(context, "Error", "Revisión técnica vencida.", pDialog);
                                }
                                else if (!response.equals("0")) {

                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("patente", edtPatente.getText().toString().toUpperCase());
                                    editor.putString("empresa", response);
                                    editor.commit();
                                    finish();
                                    Intent intent = new Intent(context, MainActivity.class);
                                    startActivity(intent);

                                } else if (edtPatente.getText().toString().equals("ZT4516")) {
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("patente", edtPatente.getText().toString().toUpperCase());
                                    editor.commit();
                                    finish();
                                    Intent intent = new Intent(context, MainActivity.class);
                                    startActivity(intent);

                                } else {

                                    ValidaPatente = false;
                                    ShowAlertDialog(context, "Error", "Patente no existe en CTAC.", pDialog);

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                ValidaPatente = false;
                                Utility.ShowMessage(context, "Error al rescatar información de sistema central." + e.getMessage());
                            }

                            pDialog.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Resultado.setText("That didn't work! " + error.toString());
                            //Utilities.ShowMessage(context, "That didn't work! " + error.toString());
                            ValidaPatente = false;
                            pDialog.hide();
                            pDialog.dismiss();
                        }
                    });
            queue.add(stringRequest);
        }
    }
    */


    public void getValidaPatente() {
        try {
            if (!ValidaPatente) {
                ValidaPatente = true;
                pDialog = new ProgressDialog(context);
                pDialog.setCancelable(false);
                pDialog.setMessage("Cargando...");
                pDialog.show();


                POSTAPIRequest postapiRequest = new POSTAPIRequest();
                String url = Constantes.URL_WS_DEFECTO + "OBTIENE_DATOS_PATENTE";
                JSONObject params = new JSONObject();
                //JSONObject params2 = new JSONObject();
                try {
                    params.put("CAMION", edtPatente.getText().toString());
                    //params2.put("SESION", params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postapiRequest.request(this, new FetchDataListener() {
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
                                        JSONObject resultado = data.getJSONObject("resultado");
                                        JSONObject _SESSION = data.getJSONObject("SESSION");
                                        /*if (Utility.validaTokenSession( _SESSION) == false) {
                                            ValidaPatente = false;
                                            Utility.ShowMessage(context, "Error al rescatar información de sesión.");
                                        }else {*/
                                            if (resultado != null) {
                                                String _EMP_NOMBRE = Utility.IsNull(resultado.getString("EMP_NOMBRE"), "");
                                                String _TIPO_CAMION = Utility.IsNull(resultado.getString("PV_TIPO_CAMIOM"), "");
                                                try {
                                                    if (_EMP_NOMBRE.equals("3")) {
                                                        ValidaPatente = false;
                                                        ShowAlertDialog(context, "Error", "Revisión técnica vencida.", pDialog);
                                                    } else if (!_EMP_NOMBRE.equals("0")) {
                                                        String _TOKEN = _SESSION != null ? Utility.IsNull(_SESSION.getString("TOKEN"), "") : "";
                                                        if (_TOKEN.compareTo("") == 0) {
                                                            ValidaPatente = false;
                                                            ShowAlertDialog(context, "Error", "No se recuperó información de sesión.", pDialog);
                                                        } else
                                                            {
                                                            Utility.setPreferencia("patente", edtPatente.getText().toString().toUpperCase(), context);
                                                            Utility.setPreferencia("empresa", _EMP_NOMBRE, context);
                                                            Utility.setPreferencia("tipoCamion", _TIPO_CAMION, context);
                                                            Utility.setPreferencia("session", _SESSION, context);
                                                            finish();
                                                            Intent intent = new Intent(context, MainActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    } else {
                                                        ValidaPatente = false;
                                                        ShowAlertDialog(context, "Error", "Patente no existe en CTAC.", pDialog);
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    ValidaPatente = false;
                                                    Utility.ShowMessage(context, "Error al rescatar información de sistema central. " + e.getMessage());
                                                }
                                   /*         }*/
                                        }
                                            button.setEnabled(true);
                                    } else {
                                        String message = data.getString("message");
                                        Utility.ShowMessage(context, message);
                                    }
                                }
                            } else {
                                Utility.ShowMessage(context, "No hay datos en la respuesta");
                            }
                        } catch (Exception e) {
                            Utility.ShowMessage(context, e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFetchFailure(String msg) {
                        button.setEnabled(true);
                       // RequestQueueService.cancelProgressDialog();
                        //Show if any error message is there called from POSTAPIRequest class
                        //RequestQueueService.showAlert(msg,ingreso_patente.this);
                        Utility.ShowMessageWebApi(context, msg);
                        ValidaPatente = false;
                        if (pDialog != null) {
                            pDialog.hide();
                            pDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFetchStart() {
                        //Start showing progressbar or any loader you have
                        //RequestQueueService.showProgressDialog(ingreso_patente.this);
                        pDialog.show();
                    }
                }, params, url,new DefaultRetryPolicy(1000000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void validarEdt() {
        if (edtPatente.getText().length() == 6) {
            button.setEnabled(true);
            int color = Color.parseColor("#009688");
            button.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));
        } else {
            button.setEnabled(false);
            int color = Color.parseColor("#9F2CC9B8");
            button.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));
        }
    }

    public void ShowAlertDialog(Context context, String textoHeader, String textoBody, ProgressDialog p) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(textoHeader);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(textoBody);
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ValidaPatente = false;
                button.setEnabled(true);
                alertDialog.cancel();
            }
        });
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.show();

        p.hide();
        p.dismiss();

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
                            stopService(new Intent(ingreso_patente.this, ServicioConectado.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //System.exit(0);
                        /*onDestroy();
                        finish();*/
                        moveTaskToBack(true);

                        finish();
                        //System.exit(0);
                        try {
                           /* Intent salida = new Intent(Intent.ACTION_MAIN);
                            android.os.Process.killProcess(android.os.Process.myPid());*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    //Implementing interfaces of FetchDataListener for POST api request


}