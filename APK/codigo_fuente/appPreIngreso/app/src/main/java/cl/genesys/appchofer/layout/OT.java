package cl.genesys.appchofer.layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import cl.genesys.appchofer.R;
import cl.genesys.appchofer.Util;
import cl.genesys.appchofer.bbdd.OrdenesTransporteDao;
import cl.genesys.appchofer.bbdd.ProgramaAsicamDao;
import cl.genesys.appchofer.entities.ProgramaAsicam;
import cl.genesys.appchofer.entities.VolleySingleton;
import cl.genesys.appchofer.utilities.Constantes;
import cl.genesys.appchofer.utilities.GPS;
import cl.genesys.appchofer.utilities.Utility;
import cl.genesys.appchofer.webapi.FetchDataListener;
import cl.genesys.appchofer.webapi.POSTAPIRequest;
import cl.genesys.appchofer.webapi.RequestQueueService;

import static android.content.Context.MODE_PRIVATE;


public class OT extends Fragment {

    private static TextView tv_confirma_ot, tv_iOT, tv_iConductor, tv_iProducto, tv_iGrua, tv_iCarro, tv_iInicioViaje, tv_iOrigen, tv_iLlegada_origen, tv_iServicio_origen, tv_iDestino, tv_iLlegada_destino, tv_iAsicam;
    public static TextView tv_OT, tv_conductor, tv_producto, tv_grua, tv_carro, tv_inicio_viaje, tv_origen, tv_llegada_origen, tv_servicio_origen, tv_destino, tv_llegada_destino, tv_ayuda_rut, tv_asicam;
    public static SharedPreferences sharedPref;
    public static Button btnConfirmarOT, btnGuardarRutC, btnGuardarCarro;
    public static ProgressDialog pDialog;
    public static OT context;
    public static EditText edtRutChofer, edtCarro;
    public static LinearLayout lly_rut, llAutoCargante;

    public OT() {
        // Required empty public constructor
    }

    public static void limpiar() {
        btnConfirmarOT.setVisibility(View.GONE);
        tv_confirma_ot.setVisibility(View.GONE);
        tv_conductor.setText("");
        tv_carro.setText("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_ot, container, false);
        Typeface fontA = Typeface.createFromAsset(getContext().getAssets(), "FontAwesome.ttf");
        Typeface fontMCI = Typeface.createFromAsset(getContext().getAssets(), "MaterialCommunityIcons.ttf");
        Typeface fontMI = Typeface.createFromAsset(getContext().getAssets(), "MaterialIcons.ttf");


        context = this;
        tv_OT = v.findViewById(R.id.tv_OT);
        tv_asicam = v.findViewById(R.id.tv_asicam);
        tv_confirma_ot = v.findViewById(R.id.tv_confirma_ot);
        tv_conductor = v.findViewById(R.id.tv_conductor);
        tv_producto = v.findViewById(R.id.tv_producto);
        tv_grua = v.findViewById(R.id.tv_grua);
        tv_carro = v.findViewById(R.id.tv_carro);
        tv_inicio_viaje = v.findViewById(R.id.tv_inicio_viaje);
        tv_origen = v.findViewById(R.id.tv_origen);
        tv_llegada_origen = v.findViewById(R.id.tv_llegada_origen);
        tv_servicio_origen = v.findViewById(R.id.tv_servicio_origen);
        tv_destino = v.findViewById(R.id.tv_destino);
        edtRutChofer = v.findViewById(R.id.edtRutChofer);
        edtCarro = v.findViewById(R.id.edtCarro);
        tv_llegada_destino = v.findViewById(R.id.tv_llegada_destino);
        btnGuardarRutC = v.findViewById(R.id.btnGuardarRutC);
        lly_rut = v.findViewById(R.id.lly_rut);
        llAutoCargante = v.findViewById(R.id.llAutoCargante);
        tv_ayuda_rut = v.findViewById(R.id.tv_ayuda_rut);

        tv_iOT = v.findViewById(R.id.tv_iOT);
        tv_iAsicam = v.findViewById(R.id.tv_iAsicam);
        tv_iConductor = v.findViewById(R.id.tv_iConductor);
        tv_iProducto = v.findViewById(R.id.tv_iProducto);
        tv_iGrua = v.findViewById(R.id.tv_iGrua);
        tv_iCarro = v.findViewById(R.id.tv_iCarro);
        tv_iInicioViaje = v.findViewById(R.id.tv_iInicioViaje);
        tv_iOrigen = v.findViewById(R.id.tv_iOrigen);
        tv_iLlegada_origen = v.findViewById(R.id.tv_iLlegada_origen);
        tv_iServicio_origen = v.findViewById(R.id.tv_iServicio_origen);
        tv_iDestino = v.findViewById(R.id.tv_iDestino);
        tv_iLlegada_destino = v.findViewById(R.id.tv_iLlegada_destino);

        tv_iOT.setTypeface(fontMCI);
        tv_iAsicam.setTypeface(fontMCI);
        tv_iConductor.setTypeface(fontA);
        tv_iProducto.setTypeface(fontA);
        tv_iGrua.setTypeface(fontMCI);
        tv_iCarro.setTypeface(fontMCI);
        tv_iInicioViaje.setTypeface(fontMI);
        tv_iOrigen.setTypeface(fontMI);
        tv_iLlegada_origen.setTypeface(fontMCI);
        tv_iServicio_origen.setTypeface(fontA);
        tv_iDestino.setTypeface(fontMCI);
        tv_iLlegada_destino.setTypeface(fontMCI);

        tv_iOT.setText("\uF219");
        tv_iAsicam.setText("\uF219");
        tv_iConductor.setText("\uf2c2");
        tv_iProducto.setText("\uf1bb");
        tv_iGrua.setText("\uF8AA");
        tv_iCarro.setText("\uF726");
        tv_iOrigen.setText("\ue1b4");
        tv_iInicioViaje.setText("\ue192");
        tv_iLlegada_origen.setText("\uF953");
        tv_iServicio_origen.setText("\uf07a");
        tv_iDestino.setText("\uF1A4");
        tv_iLlegada_destino.setText("\uF8C9");

        btnGuardarCarro = v.findViewById(R.id.btnGuardarCarro);
        btnConfirmarOT = v.findViewById(R.id.btnConfirmarOT);
        sharedPref = this.getActivity().getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
        String _OT = sharedPref.getString("OT", "");
        String GDE = sharedPref.getString("GDE", "");


        btnConfirmarOT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                GPS gps = new GPS(MainActivity.context);
                if (gps.isGPSEnabled) {
                    double latitud = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    sharedPref = getContext().getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
                    String _OT = sharedPref.getString("OT", "");
                    confirmarOT(_OT, latitud, longitude);
                } else {
                    MainActivity.AlertNoGps();
                }
            }
        });

        btnGuardarRutC.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sharedPref = getContext().getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
                String _OT = sharedPref.getString("OT", "");
                actualizaRut(_OT);

            }
        });

        btnGuardarCarro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sharedPref = getContext().getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
                String _OT = sharedPref.getString("OT", "");
                String patente = sharedPref.getString("patente", "");
                actualizaCarro(_OT, patente);
            }

        });

        if (_OT.equals("")) {
            MainActivity.validarOT(0);
            tv_confirma_ot.setVisibility(v.GONE);
        } else {
            MainActivity.llenarOT(_OT);
            if (!GDE.equals("")) {
                btnConfirmarOT.setVisibility(v.GONE);
                tv_confirma_ot.setVisibility(v.VISIBLE);
            } else
                tv_confirma_ot.setVisibility(v.GONE);
        }

        return v;
    }

    private void actualizaCarro(String _OT, String patente) {
        try {
            if (!edtCarro.getText().toString().equals("")) {
                if (Utility.isOnline(getContext())) {

                    POSTAPIRequest postapiRequest = new POSTAPIRequest();
                    String url = Constantes.URL_WS_DEFECTO + "ACTUALIZACARRO";
                    JSONObject params = new JSONObject();
                    try {
                        params.put("OT", Utility.IsNull(_OT, ""));
                        params.put("CAMION", Utility.IsNull(patente, ""));
                        params.put("CARRO", Utility.IsNull(edtCarro.getText().toString(), ""));
                        params.put("SESSION", Utility.getPreferenciaObject("session", getContext()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    postapiRequest.request(getContext(), new FetchDataListener() {
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
                                                Utility.ShowMessage(getContext(), "Error al rescatar información de sesión.");
                                            } else {
                                                Utility.setPreferencia("session", _SESSION, getContext());
                                                try {
                                                    //LLENAR RESPUESTA
                                                    JSONObject resultado = data.getJSONObject("resultado");
                                                    if (resultado != null) {
                                                        String _SALIDA = Utility.IsNull(resultado.getString("SALIDA"), "");
                                                        if (_SALIDA.equals("0")) {
                                                            Utility.ShowAlertDialog(getContext(), "Error", "Carro no asociado a patente  " + patente + " favor contactar a central.");

                                                        } else if (_SALIDA.equals("1")) {

                                                            Toast.makeText(getContext(), "Error en actualización, favor intente nuevamente." + patente + ".", Toast.LENGTH_SHORT).show();

                                                            cl.genesys.appchofer.layout.OT.btnConfirmarOT.setVisibility(View.GONE);
                                                        } else {
                                                            //Update
                                                            OrdenesTransporteDao o = new OrdenesTransporteDao(getContext());
                                                            o.open();
                                                            if (o.UpdatePatenteCarro(_OT, edtCarro.getText().toString(), getContext())) {
                                                                edtCarro.setText("");
                                                            }
                                                            o.close();

                                                            o.open();
                                                            if (o.validaConfirmaOt()) {
                                                                cl.genesys.appchofer.layout.OT.btnConfirmarOT.setVisibility(View.VISIBLE);
                                                            }
                                                            o.close();
                                                            MainActivity.llenarOT(_OT);
                                                            MainActivity.llenaGDE(_OT);
                                                            MainActivity.llenarPresentacion(_OT);
                                                            Toast.makeText(getContext(), "Carro modificado correctamente.", Toast.LENGTH_SHORT).show();
                                                        }
                                                        edtCarro.setText("");
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Utility.ShowMessage(getContext(), "Error al rescatar información de sistema central." + e.getMessage());
                                                }
                                            }

                                        } else {
                                            String message = data.getString("message");
                                            Utility.ShowMessage(getContext(), message);
                                        }
                                    }
                                } else {
                                    Utility.ShowMessage(getContext(), "Error al rescatar información de sistema central.");

                                }
                            } catch (Exception e) {
                                Utility.ShowMessage(getContext(), "Ocurrió un error inesperado." + e.getMessage());
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFetchFailure(String msg) {
                            RequestQueueService.cancelProgressDialog();
                            Utility.ShowMessageWebApi(getContext(), msg);
                            if (pDialog != null) {
                                pDialog.hide();
                                pDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFetchStart() {

                            RequestQueueService.showProgressDialog((Activity) getContext());
                        }
                    }, params, url, null);

                    pDialog = new ProgressDialog(getContext());
                    pDialog.setMessage("Modificando información...");
                    pDialog.setCancelable(true);
                    pDialog.show();

//                RequestQueue queue = VolleySingleton.getIntanciaVolley(getContext()).getRequestQueue();
//
//                String url = Constantes.URL_WS_DEFECTO + "actualizaCarro?" +
//                        "CODIGO=" + Constantes.token +
//                        "&PV_PATENTE_CAMION=" + patente +
//                        "&PV_PATENTE_CARRO=" + edtCarro.getText().toString() +
//                        "&PN_OT=" + _OT;
//
//
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                        new Response.Listener<String>() {
//
//                            @Override
//                            public void onResponse(String response) {
//                                try {
//
//                                    if (response.equals("0")) {
//                                        Toast.makeText(getContext(), "Carro no asociado a patente  " + patente + " favor contactar a central.", Toast.LENGTH_LONG).show();
//
//                                        /*btnConfirmarOT.setVisibility(View.GONE);
//                                        tv_confirma_ot.setVisibility(View.VISIBLE);*/
//                                    } else if (response.equals("1")) {
//
//                                        Toast.makeText(getContext(), "Error en actualización, favor intente nuevamente." + patente + ".", Toast.LENGTH_SHORT).show();
//
//                                        cl.genesys.appchofer.layout.OT.btnConfirmarOT.setVisibility(View.GONE);
//                                        /*btnConfirmarOT.setVisibility(View.GONE);
//                                        tv_confirma_ot.setVisibility(View.VISIBLE);*/
//                                    } else {
//                                        //Update
//                                        OrdenesTransporteDao o = new OrdenesTransporteDao(getContext());
//                                        o.open();
//                                        if (o.UpdatePatenteCarro(_OT, edtCarro.getText().toString(), getContext())) {
//                                            edtCarro.setText("");
//                                        }
//                                        o.close();
//
//                                        o.open();
//                                        if (o.validaConfirmaOt()) {
//                                            cl.genesys.appchofer.layout.OT.btnConfirmarOT.setVisibility(View.VISIBLE);
//                                        }
//                                        o.close();
//
//
//                                        MainActivity.llenarOT(_OT);
//                                        MainActivity.llenaGDE(_OT);
//                                        MainActivity.llenarPresentacion(_OT);
//                                        Toast.makeText(getContext(), "Carro modificado correctamente.", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    edtCarro.setText("");
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    Utility.ShowMessage(getContext(), "Error al rescatar información de sistema central." + e.getMessage());
//                                }
//
//                                pDialog.dismiss();
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                // Resultado.setText("That didn't work! " + error.toString());
//                                //Utilities.ShowMessage(context, "That didn't work! " + error.toString());
//                                pDialog.hide();
//                                pDialog.dismiss();
//                                //Utility.ShowMessage(getContext(), error.toString());
//                                Utility.ShowMessage(getContext(), "Error al modificar");
//
//                            }
//
//                        });
//
//                queue.add(stringRequest);
//
//                pDialog = new ProgressDialog(getContext());
//                pDialog.setMessage("Modificando información...");
//                pDialog.setCancelable(true);
//                pDialog.show();
                } else {
                    Utility.ShowMessage(getContext(), "Error, verificar conexión a internet.");
                }
            } else {
                Utility.ShowMessage(getContext(), "Error, patente carro obligatorio.");
            }
        } catch (Exception ex) {
            Utility.ShowMessage(getContext(), ex.getMessage());
        }
    }

    private void actualizaRut(final String _OT) {
        try {
            if (Utility.isOnline(getContext())) {


                POSTAPIRequest postapiRequest = new POSTAPIRequest();
                String url = Constantes.URL_WS_DEFECTO + "VALIDA_RUT";
                JSONObject params = new JSONObject();

//            String url = Constantes.URL_WS_DEFECTO + "valida_rut?" +
//                    "CODIGO=" + Constantes.token +
//                    "&PN_RUT=" +  +
//                    "&PN_OT=" + _OT;

                try {
                    params.put("OT", Utility.IsNull(_OT, ""));
                    params.put("RUT_CHOFER", Utility.IsNull(edtRutChofer.getText().toString(), ""));
                    params.put("SESSION", Utility.getPreferenciaObject("session", getContext()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postapiRequest.request(getContext(), new FetchDataListener() {
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
                                            Utility.ShowMessage(getContext(), "Error al rescatar información de sesión.");
                                        } else {
                                            Utility.setPreferencia("session", _SESSION, getContext());
                                            try {
                                                JSONObject resultado = data.getJSONObject("resultado");
                                                if (resultado != null) {
                                                    String _SALIDA = Utility.IsNull(resultado.getString("SALIDA"), "");
                                                    String _NOMBRE_CHOFER = Utility.IsNull(resultado.getString("NOMBRE_CHOFER"), "");

                                                    if (_SALIDA.equals("0")) {
                                                        Toast.makeText(getContext(), "No existe chófer", Toast.LENGTH_SHORT).show();
                                                        cl.genesys.appchofer.layout.OT.btnConfirmarOT.setVisibility(View.GONE);
                                                    } else {
                                                        //Update
                                                        OrdenesTransporteDao o = new OrdenesTransporteDao(getContext());
                                                        o.open();
                                                        if (o.UpdateChofer(edtRutChofer.getText().toString(), _NOMBRE_CHOFER, _OT, getContext())) {
                                                            edtRutChofer.setText("");
                                                        }
                                                        o.close();
                                                        o.open();
                                                        if (o.validaConfirmaOt()) {
                                                            cl.genesys.appchofer.layout.OT.btnConfirmarOT.setVisibility(View.VISIBLE);
                                                        }
                                                        o.close();


                                                        MainActivity.llenarOT(_OT);
                                                        MainActivity.llenaGDE(_OT);
                                                        MainActivity.llenarPresentacion(_OT);

                                                        Toast.makeText(getContext(), "Chofer modificado correctamente.", Toast.LENGTH_SHORT).show();
                                                    }
                                                    edtRutChofer.setText("");
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Utility.ShowMessage(getContext(), "Error al rescatar información de sistema central." + e.getMessage());
                                            }

                                            pDialog.dismiss();
                                        }

                                    } else {
                                        String message = data.getString("message");
                                        Utility.ShowMessage(getContext(), message);
                                    }
                                }
                            } else {
                                Utility.ShowMessage(getContext(), "Error al rescatar información de sistema central.");

                            }
                        } catch (Exception e) {
                            Utility.ShowMessage(getContext(), "Ocurrió un error inesperado." + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFetchFailure(String msg) {
                        RequestQueueService.cancelProgressDialog();
                        Utility.ShowMessageWebApi(getContext(), msg);
                        if (pDialog != null) {
                            pDialog.hide();
                            pDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFetchStart() {

                        RequestQueueService.showProgressDialog((Activity) getContext());
                    }
                }, params, url, null);
                pDialog = new ProgressDialog(getContext());
                pDialog.setMessage("Modificando información...");
                pDialog.setCancelable(true);
                pDialog.show();

            } else {
                Utility.ShowMessage(getContext(), "Error, verificar conexión a internet.");
            }
        } catch (Exception ex) {
            Utility.ShowMessage(getContext(), ex.getMessage());
        }

//            RequestQueue queue = VolleySingleton.getIntanciaVolley(getContext()).getRequestQueue();
//
//            String url = Constantes.URL_WS_DEFECTO + "valida_rut?" +
//                    "CODIGO=" + Constantes.token +
//                    "&PN_RUT=" + edtRutChofer.getText().toString() +
//                    "&PN_OT=" + _OT;
//
//
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                    new Response.Listener<String>() {
//
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//
//                                if (response.equals("0")) {
//
//                                    Toast.makeText(getContext(), "No existe chófer", Toast.LENGTH_SHORT).show();
//
//                                    cl.genesys.appchofer.layout.OT.btnConfirmarOT.setVisibility(View.GONE);
//                                /*btnConfirmarOT.setVisibility(View.GONE);
//                                tv_confirma_ot.setVisibility(View.VISIBLE);*/
//                                } else {
//                                    //Update
//                                    OrdenesTransporteDao o = new OrdenesTransporteDao(getContext());
//                                    o.open();
//                                    if(o.UpdateChofer(edtRutChofer.getText().toString(), response, _OT, getContext())){
//                                        edtRutChofer.setText("");
//                                    }
//                                    o.close();
//                                    o.open();
//                                    if (o.validaConfirmaOt()){
//                                        cl.genesys.appchofer.layout.OT.btnConfirmarOT.setVisibility(View.VISIBLE);
//                                    }
//                                    o.close();
//
//
//                                    MainActivity.llenarOT(_OT);
//                                    MainActivity.llenaGDE(_OT);
//                                    MainActivity.llenarPresentacion(_OT);
//
//                                    Toast.makeText(getContext(), "Chofer modificado correctamente.", Toast.LENGTH_SHORT).show();
//                                }
//
//                                edtRutChofer.setText("");
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                Utility.ShowMessage(getContext(), "Error al rescatar información de sistema central." + e.getMessage());
//                            }
//
//                            pDialog.dismiss();
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            // Resultado.setText("That didn't work! " + error.toString());
//                            //Utilities.ShowMessage(context, "That didn't work! " + error.toString());
//                            pDialog.hide();
//                            pDialog.dismiss();
//                            //Utility.ShowMessage(getContext(), error.toString());
//                            Utility.ShowMessage(getContext(), "Error al modificar");
//
//                        }
//                    });
//
//            queue.add(stringRequest);
//
//            pDialog = new ProgressDialog(getContext());
//            pDialog.setMessage("Modificando información...");
//            pDialog.setCancelable(true);
//            pDialog.show();

    }

    private void confirmarOT(final String _OT, double latitud, double longitude) {
        try {
            // Eliminar carpetas

            String folder;
            folder = Utility.RUTA_ENVIO(getContext());
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
            String folder = Utility.RUTA_CONFIRMACION(getContext());

            //Create androiddeft folder if it does not exist
            File directory = new File(folder).getAbsoluteFile();
            if (directory.exists()) {
                directory.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(false);

            POSTAPIRequest postapiRequest = new POSTAPIRequest();
            String url = Constantes.URL_WS_DEFECTO + "CONFIRMA_OT";
            JSONObject params = new JSONObject();
            try {
                params.put("OT", Utility.IsNull(_OT, "")); //OK
                params.put("HORA_INICIO_ORIGEN", Utility.IsNull("", ""));
                params.put("ID_ORIGEN_FUNDO", Utility.IsNull("", ""));
                params.put("ID_GRUA", Utility.IsNull("", ""));
                params.put("HORA_LLEGADA_FUNDO", Utility.IsNull("", ""));
                params.put("HORA_SALIDA_FUNDO", Utility.IsNull("", ""));
                params.put("DEST_COD", Utility.IsNull("", ""));
                params.put("EQUIPO_DESCARGA", Utility.IsNull("", ""));
                params.put("HORA_LLEGADA_PLANTA", Utility.IsNull("", ""));
                params.put("CAMION", Utility.IsNull("", ""));
                params.put("EMPRESA", Utility.IsNull("", ""));
                params.put("CARRO", Utility.IsNull("", ""));
                params.put("PROD_ID", Utility.IsNull("", ""));
                params.put("ESTADO", Utility.IsNull("", ""));
                params.put("CHOF_RUT", Utility.IsNull("", ""));
                params.put("MODIFICA", Utility.IsNull("0", ""));
                params.put("LONGITUD", Utility.IsNull(longitude, 0.0));
                params.put("LATITUD", Utility.IsNull(latitud, 0.0));
                params.put("SESSION", Utility.getPreferenciaObject("session", getContext()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            postapiRequest.request(getContext(), new FetchDataListener() {
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
                                        Utility.ShowMessage(getContext(), "Error al rescatar información de sesión.");
                                    } else {
                                        Utility.setPreferencia("session", _SESSION, getContext());
                                        try {
                                            //LLENAR RESPUESTA

                                            JSONObject resultado = data.getJSONObject("resultado");
                                            if (resultado != null) {
                                                String _SALIDA = Utility.IsNull(resultado.getString("SALIDA"), "");
                                                String _DES_ERROR = Utility.IsNull(resultado.getString("DES_ERROR"), "");
                                                if (_SALIDA.equals("0")) {

                                                    MainActivity.validarGDE();
                                                    btnConfirmarOT.setVisibility(View.GONE);
                                                    try {
                                                        GDE.btnConfirmarGDE.setVisibility(View.VISIBLE);
                                                    } catch (Exception ex) {

                                                    }
                                                    tv_confirma_ot.setVisibility(View.VISIBLE);
                                                    MainActivity.llenarOT(_OT);

                                                    final ProgramaAsicamDao _dao = new ProgramaAsicamDao(getContext());

                                                    _dao.open();
                                                    List<ProgramaAsicam> _OTs = null;
                                                    try {
                                                        String patente = sharedPref.getString("patente", "");
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
                                                } else {
                                                    Utility.ShowAlertDialog(getContext(), "Error", _DES_ERROR);
                                                }
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Utility.ShowMessage(getContext(), "Error al rescatar información de sistema central." + e.getMessage());
                                        }
                                    }

                                } else {
                                    String message = data.getString("message");
                                    Utility.ShowMessage(getContext(), message);
                                }
                            }
                        } else {
                            Utility.ShowMessage(getContext(), "Error al rescatar información de sistema central.");

                        }
                    } catch (Exception e) {
                        Utility.ShowMessage(getContext(), "Ocurrió un error inesperado." + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFetchFailure(String msg) {
                    //RequestQueueService.cancelProgressDialog();
                    Utility.ShowMessageWebApi(getContext(), msg);
                    if (pDialog != null) {
                        pDialog.hide();
                        pDialog.dismiss();
                    }
                }

                @Override
                public void onFetchStart() {
                    pDialog.show();
                    //RequestQueueService.showProgressDialog((Activity) getContext());
                }
            }, params, url,  new DefaultRetryPolicy(1000000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } catch (Exception ex) {
            Utility.ShowMessage(getContext(), ex.getMessage());
        }


//        pDialog = new ProgressDialog(getContext());
//        pDialog.setMessage("Cargando...");
//        pDialog.setCancelable(false);
//        RequestQueue queue = VolleySingleton.getIntanciaVolley(getContext()).getRequestQueue();
//
//        String url = Constantes.URL_WS_DEFECTO + "CONFIRMA_OT?" +
//                "CODIGO=" + Constantes.token +
//                "&Numero_OT=" + _OT +
//                "&Hora_Inicio_Origen=null" +
//                "&ID_origen_fundo=null" +
//                "&Grua=null" +
//                "&Hora_Llegada_Fundo=null" +
//                "&Hora_Salida_Fundo=null" +
//                "&ID_destino_Planta=null" +
//                "&Equipo_Descarga=null" +
//                "&Hora_Llegada_planta=null" +
//                "&Camion=null" +
//                "&RUT=null" +
//                "&Empresa=null" +
//                "&carro=null" +
//                "&Producto=null" +
//                "&Estado=null" +
//                "&latitud=" + latitud +
//                "&longitud=" + longitude +
//                "&modifica=0";
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//
//                            if (response.equals("0")) {
//
//                                MainActivity.validarGDE();
//                                btnConfirmarOT.setVisibility(View.GONE);
//                                try {
//                                    GDE.btnConfirmarGDE.setVisibility(View.VISIBLE);
//                                } catch (Exception ex) {
//
//                                }
//                                tv_confirma_ot.setVisibility(View.VISIBLE);
//                                MainActivity.llenarOT(_OT);
//
//                                final ProgramaAsicamDao _dao = new ProgramaAsicamDao(getContext());
//
//                                _dao.open();
//                                List<ProgramaAsicam> _OTs = null;
//                                try {
//                                    String patente = sharedPref.getString("patente", "");
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
//
//                                _dao.close();
//                            } else {
//                                Utility.ShowAlertDialog(getContext(), "Error", response);
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Utility.ShowMessage(getContext(), "Error al rescatar información de sistema central." + e.getMessage());
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
//                        //Utility.ShowMessage(getContext(), error.toString());
//                        Utility.ShowMessage(getContext(), "Error al rescatar información, favor revisar conexión a internet");
//
//                    }
//                });
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(stringRequest);
//
//
//        pDialog.show();
    }

}