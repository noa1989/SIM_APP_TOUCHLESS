package cl.genesys.appchofer.layout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;

import cl.genesys.appchofer.R;
import cl.genesys.appchofer.entities.VolleySingleton;
import cl.genesys.appchofer.utilities.Constantes;
import cl.genesys.appchofer.utilities.ServicioGPS;
import cl.genesys.appchofer.utilities.Utility;

import static android.content.Context.MODE_PRIVATE;

public class GDE extends Fragment {

    private static TextView tv_iGDE, tv_iOrigenGDE, tv_iDestinoGDE, tv_iConductorGDE, tv_iProductoGDE, tv_iCamionGDE, tv_iCarroGDE, tv_iOTGDE, tv_iLlegada_origenGDE, tv_iLlegada_DestinoGDE, tv_itv_carguioGDE;
    public static TextView tv_GDE, tv_origenGDE, tv_destinoGDE, tv_conductorGDE, tv_productoGDE, tv_camionGDE, tv_carroGDE, tv_confirma_gde, tv_OTGDE, tv_llegada_origenGDE, tv_llegada_DestinoGDE, tv_carguioGDE;
    public static SharedPreferences sharedPref;
    public static Button btnConfirmarGDE;
    public static  ProgressDialog pDialog;


    public GDE() {
        // Required empty public constructor
    }

    public static void limpiar() {
        tv_GDE.setText("");
        tv_OTGDE.setText("");
        tv_confirma_gde.setText("");
        tv_origenGDE.setText("");
        tv_llegada_DestinoGDE.setText("");
        tv_destinoGDE.setText("");
        tv_conductorGDE.setText("");
        tv_productoGDE.setText("");
        tv_carguioGDE.setText("");
        tv_llegada_origenGDE.setText("");
        tv_camionGDE.setText("");
        tv_carroGDE.setText("");
        btnConfirmarGDE.setVisibility(View.GONE);
        tv_confirma_gde.setVisibility(View.GONE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            sharedPref = this.getActivity().getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
            String GDE = sharedPref.getString("GDE", "");
            String _OT = sharedPref.getString("OT", "");
            String confirmaGDE = sharedPref.getString("confirmaGDE", "");

            if (isVisibleToUser) {
                // Refresh your fragment here
                if (!GDE.equals(""))  {
                    MainActivity.llenaGDE(_OT);
                    if (confirmaGDE.equals("1")){

                        btnConfirmarGDE.setVisibility(View.GONE);
                        tv_confirma_gde.setVisibility(View.VISIBLE);

                    }else  {
                        btnConfirmarGDE.setVisibility(View.VISIBLE);
                        tv_confirma_gde.setVisibility(View.GONE);
                    }
                    //MainActivity.llenaGDE(_OT);
                }else {
                    limpiar();
                    btnConfirmarGDE.setVisibility(View.GONE);
                    tv_confirma_gde.setVisibility(View.GONE);
                }
            }
        }
        catch (Exception e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_gde, container, false);
        Typeface fontA = Typeface.createFromAsset(getContext().getAssets(), "FontAwesome.ttf");
        Typeface fontMCI = Typeface.createFromAsset(getContext().getAssets(), "MaterialCommunityIcons.ttf");
        Typeface fontMI = Typeface.createFromAsset(getContext().getAssets(), "MaterialIcons.ttf");

        tv_iGDE = v.findViewById(R.id.tv_iGDE);
        tv_iOTGDE = v.findViewById(R.id.tv_iOTGDE);
        tv_iOrigenGDE = v.findViewById(R.id.tv_iOrigenGDE);
        tv_iLlegada_origenGDE = v.findViewById(R.id.tv_iLlegada_origenGDE);
        tv_iLlegada_DestinoGDE = v.findViewById(R.id.tv_iLlegada_DestinoGDE);
        tv_itv_carguioGDE = v.findViewById(R.id.tv_itv_carguioGDE);
        tv_iDestinoGDE = v.findViewById(R.id.tv_iDestinoGDE);
        tv_iConductorGDE = v.findViewById(R.id.tv_iConductorGDE);
        tv_iProductoGDE = v.findViewById(R.id.tv_iProductoGDE);
        tv_iCamionGDE = v.findViewById(R.id.tv_iCamionGDE);
        tv_iCarroGDE = v.findViewById(R.id.tv_iCarroGDE);


        tv_GDE = v.findViewById(R.id.tv_GDE);
        tv_OTGDE = v.findViewById(R.id.tv_OTGDE);
        tv_confirma_gde = v.findViewById(R.id.tv_confirma_gde);
        tv_origenGDE = v.findViewById(R.id.tv_origenGDE);
        tv_llegada_DestinoGDE = v.findViewById(R.id.tv_llegada_DestinoGDE);
        tv_destinoGDE = v.findViewById(R.id.tv_destinoGDE);
        tv_conductorGDE = v.findViewById(R.id.tv_conductorGDE);
        tv_productoGDE = v.findViewById(R.id.tv_productoGDE);
        tv_carguioGDE = v.findViewById(R.id.tv_carguioGDE);
        tv_llegada_origenGDE = v.findViewById(R.id.tv_llegada_origenGDE);
        tv_camionGDE = v.findViewById(R.id.tv_camionGDE);
        tv_carroGDE = v.findViewById(R.id.tv_carroGDE);

        tv_iGDE.setTypeface(fontMCI);
        tv_iOTGDE.setTypeface(fontMCI);
        tv_iLlegada_origenGDE.setTypeface(fontMCI);
        tv_itv_carguioGDE.setTypeface(fontMCI);
        tv_iConductorGDE.setTypeface(fontA);
        tv_iProductoGDE.setTypeface(fontA);
        tv_iCamionGDE.setTypeface(fontMCI);
        tv_iCarroGDE.setTypeface(fontMCI);
        tv_iOrigenGDE.setTypeface(fontMI);
        tv_iDestinoGDE.setTypeface(fontMCI);
        tv_iLlegada_DestinoGDE.setTypeface(fontMCI);

        tv_iGDE.setText("\uF219");
        tv_iOTGDE.setText("\uF219");
        tv_iConductorGDE.setText("\uf2c2");
        tv_iLlegada_origenGDE.setText("\uF953");
        tv_itv_carguioGDE.setText("\uF152");
        tv_iProductoGDE.setText("\uf1bb");
        tv_iCamionGDE.setText("\uF53D");
        tv_iCarroGDE.setText("\uF726");
        tv_iOrigenGDE.setText("\ue1b4");
        tv_iDestinoGDE.setText("\uF1A4");
        tv_iLlegada_DestinoGDE.setText("\uF8C9");



        sharedPref = this.getActivity().getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
        String GDE = sharedPref.getString("GDE", "");
        String _OT = sharedPref.getString("OT", "");
        String confirmaGDE = sharedPref.getString("confirmaGDE", "");

        btnConfirmarGDE = v.findViewById(R.id.btnConfirmarGDE);
        btnConfirmarGDE.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
                sharedPref = getContext().getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
                String GDE = sharedPref.getString("GDE", "");
                String _OT = sharedPref.getString("OT", "");
                try {
                    //MainActivity.validarGDE();
                    btnConfirmarGDE.setVisibility(View.GONE);
                    tv_confirma_gde.setVisibility(View.VISIBLE);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("confirmaGDE", "1");
                    editor.commit();

                    MainActivity.tx_viaje_iniciado.setText("Viaje al origen");
                    MainActivity.tx_viaje_iniciado.setBackgroundColor(0xFF1ABC9C);
                }catch  (Exception ex)  {

                }
                /*try {
                    //MainActivity.descargaCartolas();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                //confirmarGDE(GDE, _OT);
            }
        });

        if (!GDE.equals("")){
            MainActivity.llenaGDE(_OT);
            if (confirmaGDE.equals("1")){

                btnConfirmarGDE.setVisibility(View.GONE);
                tv_confirma_gde.setVisibility(View.VISIBLE);

            }else{
                btnConfirmarGDE.setVisibility(View.VISIBLE);
                tv_confirma_gde.setVisibility(View.GONE);
            }
            //MainActivity.llenaGDE(_OT);
        }else {
            btnConfirmarGDE.setVisibility(View.GONE);
            tv_confirma_gde.setVisibility(View.GONE);
        }
        return v;
    }


    //    private void confirmarGDE(String gde, String _OT) {
    //
    //        RequestQueue queue = VolleySingleton.getIntanciaVolley(getContext()).getRequestQueue();
    //
    //        String url = Constantes.URL_WS_DEFECTO + "gde_en_tablet?" +
    //                "CODIGO=" + Constantes.token +
    //                "&numero_ot=" + _OT +
    //                "&num_folio=" + gde;
    //
    //        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
    //                new Response.Listener<String>() {
    //
    //                    @Override
    //                    public void onResponse(String response) {
    //                        try {
    //
    //                            if (response.equals("Folio y / o Nº OT incorrectos")){
    //                                MainActivity.validarGDE();
    //                                btnConfirmarGDE.setVisibility(View.GONE);
    //                                tv_confirma_gde.setVisibility(View.VISIBLE);
    //
    //                                SharedPreferences.Editor editor = sharedPref.edit();
    //                                editor.putString("confirmaGDE", "1");
    //                                editor.commit();
    //
    //                                MainActivity.tx_viaje_iniciado.setText("Viaje iniciado");
    //                                MainActivity.tx_viaje_iniciado.setBackgroundColor(0xFF1ABC9C);
    //
    //
    //
    //                            } else {
    //                                Toast.makeText(getContext(),  response, Toast.LENGTH_SHORT).show();
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
    //                        Utility.ShowMessage(getContext(), error.toString());
    //                    }
    //                });
    //
    //        queue.add(stringRequest);
    //
    //        pDialog = new ProgressDialog(getContext());
    //        pDialog.setMessage("Cargando...");
    //        pDialog.setCancelable(false);
    //        try {
    //            pDialog.show();
    //        }catch (Exception e){
    //
    //        }
    //    }
}
