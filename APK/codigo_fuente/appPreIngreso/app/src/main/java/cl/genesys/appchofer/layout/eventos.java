package cl.genesys.appchofer.layout;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;

import cl.genesys.appchofer.R;
import cl.genesys.appchofer.adapters.ItemSpinner;
import cl.genesys.appchofer.bbdd.ListaValoresDao;
import cl.genesys.appchofer.entities.SpinnerHelper;
import cl.genesys.appchofer.entities.VolleySingleton;
import cl.genesys.appchofer.utilities.Constantes;
import cl.genesys.appchofer.utilities.Utility;
import cl.genesys.appchofer.webapi.FetchDataListener;
import cl.genesys.appchofer.webapi.POSTAPIRequest;
import cl.genesys.appchofer.webapi.RequestQueueService;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class eventos extends Fragment {

    ArrayList<SpinnerHelper> listaHelper;
    private ItemSpinner sp_adapter;
    Spinner spnTipoEvento;
    private Button btnEnviarEvento;
    private EditText editText;
    public eventos context;
    public static SharedPreferences sharedPref;
    public ProgressDialog pDialog;
    String GDE, _OT;

    public eventos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_eventos, container, false);
        spnTipoEvento = v.findViewById(R.id.spnTipoEvento);
        btnEnviarEvento = v.findViewById(R.id.btnEnviarEvento);
        editText = v.findViewById(R.id.editText);

        ListaValoresDao dao = new ListaValoresDao(getContext());


        llenarSpinner(dao.getListaValores("-1"));
        sharedPref = this.getActivity().getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
        _OT = sharedPref.getString("OT", "");
        GDE = sharedPref.getString("GDE", "");
        btnEnviarEvento.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EnviarEvento();
                    }
                });

        return v;
    }

    private void EnviarEvento() {
        try {
            if (!editText.getText().toString().trim().equals("")) {
                if (Utility.isOnline(getContext())) {
                    SpinnerHelper vsel = (SpinnerHelper) spnTipoEvento.getSelectedItem();
                    if (vsel == null) {
                        Utility.ShowMessage(getContext(), "Debe seleccionar el tipo de evento.");
                    } else {

                        String cod = vsel.getCod();
                        POSTAPIRequest postapiRequest = new POSTAPIRequest();
                        String url = Constantes.URL_WS_DEFECTO + "SP_REGISTRA_EVENTOS";
                        JSONObject params = new JSONObject();
                        try {
                            params.put("OT", Utility.IsNull(_OT, ""));
                            params.put("EVENTO_ID", Utility.IsNull(cod, ""));
                            params.put("OBSER", Utility.IsNull(editText.getText().toString(), ""));
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
                                                        String message = data.getString("message");
                                                        if (message.equals("1")){
                                                            Toast.makeText(getContext(), "Evento enviado correctamente.", Toast.LENGTH_SHORT).show();
                                                            editText.setText("");
                                                        }else {
                                                            Toast.makeText(getContext(), "Evento no se pudo enviar, intente nuevamente.", Toast.LENGTH_SHORT).show();
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
                            }

                            @Override
                            public void onFetchStart() {

                                RequestQueueService.showProgressDialog((Activity) getContext());
                            }
                        }, params, url,null);
                    }

                } else {
                    Utility.ShowMessage(getContext(), "Equipo sin internet, favor vuelva a intentar cuando tenga conexión.");
                }
            } else {
                Utility.ShowMessage(getContext(), "Debe completar el mensaje.");
            }

        } catch (Exception ex) {
            Utility.ShowMessage(getContext(), ex.getMessage());
        }

//                SpinnerHelper vsel = (SpinnerHelper) spnTipoEvento.getSelectedItem();
//                String cod = vsel.getCod();
//
//                RequestQueue queue = VolleySingleton.getIntanciaVolley(getContext()).getRequestQueue();
//
//                String url = Constantes.URL_WS_DEFECTO + "SP_REGISTRA_EVENTOS?" +
//                        "CODIGO=" + Constantes.token +
//                        "&PN_OT=" + _OT +
//                        "&PN_EVENTO_ID=" + cod +
//                        "&PV_OBSER=" + editText.getText().toString();
//
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                        new Response.Listener<String>() {
//
//                            @Override
//                            public void onResponse(String response) {
//                                try {
//                                    if (response.equals("1")){
//                                        Toast.makeText(getContext(), "Evento enviado correctamente.", Toast.LENGTH_SHORT).show();
//                                        editText.setText("");
//                                    }else {
//                                        Toast.makeText(getContext(), "Evento no se pudo enviar, intente nuevamente.", Toast.LENGTH_SHORT).show();
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//
//                            }
//                        });
//
//                queue.add(stringRequest);

    }

    public void llenarSpinner(ArrayList<SpinnerHelper> lista) {


        try {

            sp_adapter = new ItemSpinner(getActivity(), R.layout.item_list_spinner, lista);
            //listaHelper.add(0, Utilities.item_sin_asignar);
            spnTipoEvento.setAdapter(sp_adapter);


        } catch (Exception ex) {
            Utility.ShowMessage(getContext(), "Error al cargar Idelin.");
        } finally {

        }

    }

}
