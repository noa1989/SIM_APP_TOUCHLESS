package cl.genesys.appchofer.layout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cl.genesys.appchofer.R;
import cl.genesys.appchofer.bbdd.DestinosDao;
import cl.genesys.appchofer.entities.Destinos;
import cl.genesys.appchofer.entities.ProgramaAsicam;
import cl.genesys.appchofer.utilities.Constantes;
import cl.genesys.appchofer.utilities.ServicioGPS;
import cl.genesys.appchofer.utilities.ServicioOTDisponible;
import cl.genesys.appchofer.utilities.Utility;
import cl.genesys.appchofer.webapi.FetchDataListener;
import cl.genesys.appchofer.webapi.POSTAPIRequest;

import static cl.genesys.appchofer.layout.ingreso_datos.edtComuna;
import static cl.genesys.appchofer.layout.ingreso_datos.edtDVConductor;
import static cl.genesys.appchofer.layout.ingreso_datos.edtDVProveedor;
import static cl.genesys.appchofer.layout.ingreso_datos.edtEspecie;
import static cl.genesys.appchofer.layout.ingreso_datos.edtGuia;
import static cl.genesys.appchofer.layout.ingreso_datos.edtMovilConductor;
import static cl.genesys.appchofer.layout.ingreso_datos.edtNomConductor;
import static cl.genesys.appchofer.layout.ingreso_datos.edtNomEspecie;
import static cl.genesys.appchofer.layout.ingreso_datos.edtNomProducto;
import static cl.genesys.appchofer.layout.ingreso_datos.edtNomProveedor;
import static cl.genesys.appchofer.layout.ingreso_datos.edtOrigen;
import static cl.genesys.appchofer.layout.ingreso_datos.edtPatenteAcoplado;
import static cl.genesys.appchofer.layout.ingreso_datos.edtPatenteCamion;
import static cl.genesys.appchofer.layout.ingreso_datos.edtPedidoCompra;
import static cl.genesys.appchofer.layout.ingreso_datos.edtPredio;
import static cl.genesys.appchofer.layout.ingreso_datos.edtProductp;
import static cl.genesys.appchofer.layout.ingreso_datos.edtRol;
import static cl.genesys.appchofer.layout.ingreso_datos.imgPDF;
import static cl.genesys.appchofer.layout.ingreso_datos.rdbElectronica;
import static cl.genesys.appchofer.layout.ingreso_datos.spnLugarRecepcion;

public class pre_ingreso extends AppCompatActivity {

    public static ProgressDialog pDialog;
    private static final int REQUEST_CODE = 1;
    public static Context context;
    public static TabLayout tabLayoutPreIngreso;
    private ViewPager mViewPager;

    public static void AlertNoInternet() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alert = null;
        builder.setTitle("El internet esta desactivado")
                .setMessage("Favor activar.")
                .setCancelable(true);
        alert = builder.create();
        alert.show();
    }

    public static void validaLugarRecepcion(double latitud, double longitude) {

        DestinosDao _Dao = new DestinosDao(context);
        _Dao.open();

        ArrayAdapter dataAdapter = null;
        try {
            dataAdapter = new ArrayAdapter(context, R.layout.spinner_item, _Dao.getDestinos(context, latitud, longitude));
        } catch (Exception e) {
            e.printStackTrace();
        }

        _Dao.close();
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spnLugarRecepcion.setAdapter(dataAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pre_ingreso);

        context = this;

        try {
            Utility.solicitar_permisos(context, new String[]{
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            });
            // Manifest.permission.READ_PHONE_NUMBERS,
        } catch (Exception ex) {
            Log.d("SOLITAR PERMISOS", ex.getMessage());
        }


        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPagerPreIngreso);
        mViewPager.setAdapter(myPagerAdapter);
        tabLayoutPreIngreso = (TabLayout) findViewById(R.id.tabLayoutPreIngreso);
        tabLayoutPreIngreso.setupWithViewPager(mViewPager);

        String pdf = getIntent().getStringExtra("pdf417");
        if (pdf != null)
            if (!pdf.equals(""))
                llenarPDF417(pdf);

        if (!isMyServiceRunning(ServicioGPS.class)) {
            Intent service = new Intent(context, ServicioGPS.class);
            startService(service);
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

    private void llenarPDF417(String pdf) {
        Toast.makeText(context, pdf, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public static void ValidaWebServiceConductor(String conductor) {
        try {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Buscando conductor...");
            pDialog.setCancelable(true);
            POSTAPIRequest postapiRequest = new POSTAPIRequest();
            String url = Constantes.URL_WS_DEFECTO + "SP_WS_CHOFER";
            JSONObject params = new JSONObject();
            try {
                params.put("RUT", Utility.IsNull(conductor, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            postapiRequest.request(context, new FetchDataListener() {
                @Override
                public void onFetchComplete(JSONObject data) {
                    try {
                        if (pDialog != null) {
                            pDialog.hide();
                            pDialog.dismiss();
                        }
                        //Now check result sent by our POSTAPIRequest class
                        if (data != null) {

                            Integer ERROR_COD  = Utility.IsNull(data.getInt("ERROR_COD"), 0);
                            String ERROR_DSC = Utility.IsNull(data.getString("ERROR_DSC"), "");
                            if (ERROR_COD > 0){
                                Utility.ShowAlertDialog(context, "Error", ERROR_DSC);
                                edtMovilConductor.setText("");
                                edtDVConductor.setText("");
                                edtNomConductor.setText("");
                            }else {
                                JSONObject json = data.getJSONObject("CHOFER");
                                String CHOFER_DV = Utility.IsNull(json.getString("CHOFER_DV"), "");
                                String CHOFER_DSC = Utility.IsNull(json.getString("CHOFER_DSC"), "");
                                String CELULAR = Utility.IsNull(json.getString("CELULAR"), "");

                                edtMovilConductor.setText(CELULAR);
                                edtDVConductor.setText(CHOFER_DV);
                                edtNomConductor.setText(CHOFER_DSC);
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
                    pDialog.show();
                }
            }, params, url, null);
        } catch (Exception ex) {
            Utility.ShowMessage(context, ex.getMessage());
        }
    }

    public static void ValidaWebServiceProveedor(String proveedor) {
        try {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Buscando proveedor...");
            pDialog.setCancelable(true);
            POSTAPIRequest postapiRequest = new POSTAPIRequest();
            String url = Constantes.URL_WS_DEFECTO + "SP_WS_PROVEEDOR";
            JSONObject params = new JSONObject();
            try {
                params.put("RUT", Utility.IsNull(proveedor, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            postapiRequest.request(context, new FetchDataListener() {
                @Override
                public void onFetchComplete(JSONObject data) {
                    try {
                        if (pDialog != null) {
                            pDialog.hide();
                            pDialog.dismiss();
                        }
                        //Now check result sent by our POSTAPIRequest class
                        if (data != null) {
                           // if (json)
                            Integer ERROR_COD = Utility.IsNull(data.getInt("ERROR_COD"), 0);
                            String ERROR_DSC = Utility.IsNull(data.getString("ERROR_DSC"), "");

                            if (ERROR_COD > 0){
                                Utility.ShowAlertDialog(context, "Error", ERROR_DSC);
                                edtDVProveedor.setText("");
                                edtNomProveedor.setText("");
                                imgPDF.setVisibility(View.GONE);
                            }else {
                                JSONObject json = data.getJSONObject("PROVEEDOR");
                                String PROVEEDOR_DV = Utility.IsNull(json.getString("PROVEEDOR_DV"), "");
                                String PROVEEDOR_DSC = Utility.IsNull(json.getString("PROVEEDOR_DSC"), "");
                                edtDVProveedor.setText(PROVEEDOR_DV);
                                edtNomProveedor.setText(PROVEEDOR_DSC);
                                validarProveedor(proveedor);
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
                    pDialog.show();
                }
            }, params, url, null);
        } catch (Exception ex) {
            Utility.ShowMessage(context, ex.getMessage());
        }
    }

    public static void ValidaWebServiceGDE(String proveedor, String guia) {
        try {
            if (rdbElectronica.isChecked() && !proveedor.equals("91440000") && !proveedor.equals("955304000")&& !guia.equals("")) {

                if (Utility.isOnline(context)) {
                    pDialog = new ProgressDialog(context);
                    pDialog.setMessage("Buscando proveedor...");
                    pDialog.setCancelable(true);
                    POSTAPIRequest postapiRequest = new POSTAPIRequest();
                    String url = Constantes.URL_WS_DEFECTO + "SP_WS_DATOS_GDE";
                    JSONObject params = new JSONObject();
                    try {
                        params.put("PROVEEDOR_RUT", Utility.IsNull(proveedor, ""));
                        params.put("GUIA", Utility.IsNull(guia, ""));
                        params.put("PDF417", Utility.IsNull("0", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    postapiRequest.request(context, new FetchDataListener() {
                        @Override
                        public void onFetchComplete(JSONObject data) {
                            try {
                                if (pDialog != null) {
                                    pDialog.hide();
                                    pDialog.dismiss();
                                }
                                //Now check result sent by our POSTAPIRequest class
                                if (data != null) {
                                    // if (json)
                                    Integer ERROR_COD = Utility.IsNull(data.getInt("ERROR_COD"), 0);
                                    String ERROR_DSC = Utility.IsNull(data.getString("ERROR_DSC"), "");

                                    if (ERROR_COD > 0) {
                                        Utility.ShowAlertDialog(context, "Error", ERROR_DSC);
                                        edtDVProveedor.setText("");
                                        edtNomProveedor.setText("");
                                        imgPDF.setVisibility(View.GONE);
                                    } else {
                                    /*JSONObject json = data.getJSONObject("PROVEEDOR");
                                    String PROVEEDOR_DV = Utility.IsNull(json.getString("PROVEEDOR_DV"), "");
                                    String PROVEEDOR_DSC = Utility.IsNull(json.getString("PROVEEDOR_DSC"), "");
                                    edtDVProveedor.setText(PROVEEDOR_DV);
                                    edtNomProveedor.setText(PROVEEDOR_DSC);
                                    validarProveedor(proveedor);*/
                                        JSONObject json = data.getJSONObject("RECEP");
                                        edtEspecie.setText(Utility.IsNull(json.getString("ESMA_COD"), ""));
                                        edtNomEspecie.setText(Utility.IsNull(json.getString("ESMA_DSC"), ""));
                                        edtProductp.setText(Utility.IsNull(json.getString("PRMA_COD"), ""));
                                        edtNomProducto.setText(Utility.IsNull(json.getString("PRMA_DSC"), ""));
                                        edtPatenteCamion.setText(Utility.IsNull(json.getString("CAMION"), ""));
                                        edtPatenteAcoplado.setText(Utility.IsNull(json.getString("CARRO"), ""));
                                        edtPedidoCompra.setText(Utility.IsNull(json.getString("PEDIDO"), ""));
                                        edtOrigen.setText(Utility.IsNull(json.getString("CANCHA_ORIGEN"), ""));
                                        edtComuna.setText(Utility.IsNull(json.getString("COMUNA_ID"), "") + " " + Utility.IsNull(json.getString("COMUNA_DSC"), ""));
                                        edtPredio.setText(Utility.IsNull(json.getString("PREDIO_ID"), "") + " " + Utility.IsNull(json.getString("PREDIO_DSC"), ""));
                                        edtRol.setText(Utility.IsNull(json.getString("ROL"), ""));

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
                            pDialog.show();
                        }
                    }, params, url, null);
                }
            }
        } catch (Exception ex) {
            Utility.ShowMessage(context, ex.getMessage());
        }
    }

    public static void validarProveedor(String proveedor) {
        if (proveedor.equals("91440000")){
            imgPDF.setVisibility(View.VISIBLE);
        }else {
            imgPDF.setVisibility(View.GONE);
        }
    }

    public static void AlertNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alert = null;
        builder.setTitle("El sistema GPS esta desactivado");
        builder.setMessage("Favor activar.")
                .setCancelable(true)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        alert = builder.create();
        alert.show();
    }

    public static void descargarParametros() {
        try {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Descargando...");
            pDialog.setCancelable(true);
            POSTAPIRequest postapiRequest = new POSTAPIRequest();
            String url = Constantes.URL_WS_DEFECTO + "SP_WS_DESTINO";
            JSONObject params = new JSONObject();
            try {
                params.put("TOKEN", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            postapiRequest.request(context, new FetchDataListener() {
                @Override
                public void onFetchComplete(JSONObject data) {
                    try {
                        if (pDialog != null) {
                            pDialog.hide();
                            pDialog.dismiss();
                        }
                        //Now check result sent by our POSTAPIRequest class
                        if (data != null) {
                            JSONArray jsonArray = null;
                            List<Destinos> destinos = new ArrayList<Destinos>();
                            jsonArray = data.getJSONArray("LISTA");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonConfig = jsonArray.getJSONObject(i);
                                try {

                                    Destinos destino = new Destinos();
                                    destino.setPlanta(jsonConfig.getInt("PLANTA"));
                                    destino.setCancha(jsonConfig.getInt("CANCHA"));
                                    destino.setCancha_eerr(jsonConfig.getInt("CANCHA_EERR"));
                                    destino.setNombre(jsonConfig.getString("NOMBRE"));
                                    destino.setRut(jsonConfig.getInt("RUT"));
                                    destino.setGeo_sup_izq_x(jsonConfig.getString("GEO_SUP_IZQ_X"));
                                    destino.setGeo_sup_izq_y(jsonConfig.getString("GEO_SUP_IZQ_Y"));
                                    destino.setGeo_inf_der_x(jsonConfig.getString("GEO_INF_DER_X"));
                                    destino.setGeo_inf_der_y(jsonConfig.getString("GEO_INF_DER_Y"));
                                    //destino.setTipo_lugar(Utility.IsNull(jsonConfig.getInt("TIPO_LUGAR"), null));
                                    destino.setUtiliza_ctac(jsonConfig.getString("UTILIZA_CTAC"));

                                    destinos.add(destino);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            DestinosDao dao = new DestinosDao(context);
                            dao.open();
                            dao.deleteDestinos(context);

                            dao.insertDestinos(destinos, context);
                            dao.close();

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
                    pDialog.show();
                }
            }, params, url, null);
        } catch (Exception ex) {
            Utility.ShowMessage(context, ex.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        //Checking if the item is in checked state or not, if not make it in checked state
        if (menuItem.isChecked()) menuItem.setChecked(false);
        else menuItem.setChecked(true);

        //Closing drawer on item click

        //Check to see which item was being clicked and perform appropriate action
        switch (menuItem.getItemId()) {
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
                switch (i) {
                    case 0:

                        fragment = new ingreso_datos();
                        break;
                    case 1:
                        fragment = new mapa();
                        break;
                    case 2:
                        fragment = new fin_faena();
                        break;
                    case 3:
                        fragment = new gmm();
                        break;

                    default:
                        fragment = null;
                }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                    case 0:
                        return "Registro Presentación";
                    case 1:
                        return "LUGAR DESCARGA";
                    case 2:
                        return "Fin \nFaena";
                    case 3:
                        return "GMM";
                }

            return null;
        }
    }

}
