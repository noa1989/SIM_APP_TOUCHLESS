package cl.genesys.appchofer.layout;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cl.genesys.appchofer.R;
import cl.genesys.appchofer.bbdd.DestinosDao;
import cl.genesys.appchofer.entities.Destinos;
import cl.genesys.appchofer.utilities.Constantes;
import cl.genesys.appchofer.utilities.Utility;
import cl.genesys.appchofer.webapi.FetchDataListener;
import cl.genesys.appchofer.webapi.POSTAPIRequest;
import cl.genesys.appchofer.webapi.RequestQueueService;

import static cl.genesys.appchofer.layout.pre_ingreso.ValidaWebServiceConductor;
import static cl.genesys.appchofer.layout.pre_ingreso.ValidaWebServiceGDE;
import static cl.genesys.appchofer.layout.pre_ingreso.ValidaWebServiceProveedor;
import static cl.genesys.appchofer.layout.pre_ingreso.context;
import static cl.genesys.appchofer.layout.pre_ingreso.descargarParametros;
import static cl.genesys.appchofer.layout.pre_ingreso.validarProveedor;

/**
 * A simple {@link Fragment} subclass.
 */
public class ingreso_datos extends Fragment {


    public static ImageButton imgPDF, imgCamera;
    public static EditText edtRutConductor, edtDVConductor, edtNomConductor, edtMovilConductor;
    public static EditText edtGuia, edtEspecie, edtNomEspecie, edtProductp, edtNomProducto, edtPatenteCamion, edtPatenteAcoplado, edtPedidoCompra, edtOrigen, edtComuna, edtPredio, edtRol;
    public static EditText edtRutProveedor, edtDVProveedor, edtNomProveedor;



    Button btnFormin, btnPulp, btnCmpcMaderas;
    public static RadioButton rdbElectronica, rdbManual;
    public static Spinner spnLugarRecepcion;
    public ingreso_datos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ingreso_datos, container, false);


        edtGuia = v.findViewById(R.id.edtGuia);
        edtEspecie = v.findViewById(R.id.edtEspecie);
        edtNomEspecie = v.findViewById(R.id.edtNomEspecie);
        edtProductp = v.findViewById(R.id.edtProductp);
        edtNomProducto = v.findViewById(R.id.edtNomProducto);
        edtPatenteCamion = v.findViewById(R.id.edtPatenteCamion);
        edtPatenteAcoplado = v.findViewById(R.id.edtPatenteAcoplado);
        edtPedidoCompra = v.findViewById(R.id.edtPedidoCompra);
        edtOrigen = v.findViewById(R.id.edtOrigen);
        edtComuna = v.findViewById(R.id.edtComuna);
        edtPredio = v.findViewById(R.id.edtPredio);
        edtRol= v.findViewById(R.id.edtRol);

        rdbElectronica = v.findViewById(R.id.rdbElectronica);
        rdbElectronica.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
                try {
                    ValidaWebServiceGDE(edtRutProveedor.getText().toString(), edtGuia.getText().toString());
                }catch  (Exception ex)  {

                }
            }
        });
        rdbManual = v.findViewById(R.id.rdbManual);
        rdbManual.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
                try {

                }catch  (Exception ex)  {

                }
            }
        });

        btnFormin = v.findViewById(R.id.btnFormin);
        btnFormin.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
                try {
                    validarProveedor("91440000");
                    edtRutProveedor.setText("91440000");
                    edtNomProveedor.setText("Forestal Mininco SpA");
                    edtDVProveedor.setText("7");

                }catch  (Exception ex)  {

                }
            }
        });
        btnPulp = v.findViewById(R.id.btnPulp);
        btnPulp.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
                try {
                    validarProveedor("96532330");
                    edtRutProveedor.setText("96532330");
                    edtNomProveedor.setText("C.M.P.C. PULP SpA");
                    edtDVProveedor.setText("9");

                }catch  (Exception ex)  {

                }
            }
        });

        btnCmpcMaderas = v.findViewById(R.id.btnCmpcMaderas);
        btnCmpcMaderas.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
                try {
                    validarProveedor("95304000");
                    edtRutProveedor.setText("95304000");
                    edtNomProveedor.setText("C.M.P.C. MADERAS SpA");
                    edtDVProveedor.setText("K");

                }catch  (Exception ex)  {

                }
            }
        });

        imgPDF = v.findViewById(R.id.imgPDF);
        imgPDF.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
                try {
                    Escaner();
                }catch  (Exception ex)  {

                }
            }
        });
        imgCamera = v.findViewById(R.id.imgCamera);
        imgCamera.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i,0);
                }catch  (Exception ex)  {

                }
            }
        });
        spnLugarRecepcion = (Spinner) v.findViewById(R.id.spnLugarRecepcion);
        llenarDestinos();

        SetEditConductor(v);
        SetEditProveedor(v);

        SetEditDestino(v);

        return v;
    }

    private void llenarDestinos() {
        descargarParametros();
    }

    private void SetEditDestino(View v) {

    }

    private void SetEditProveedor(View v) {
        edtDVProveedor = v.findViewById(R.id.edtDVProveedor);
        edtNomProveedor = v.findViewById(R.id.edtNomProveedor);

        edtRutProveedor = v.findViewById(R.id.edtRutProveedor);
        edtRutProveedor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (edtRutProveedor.getText().toString().equals("")){
                        Utility.ShowAlertDialog(context, "Error", "Rut proveedor obligatorio.");
                        imgPDF.setVisibility(View.GONE);
                    }else {
                        ValidaWebServiceProveedor(edtRutProveedor.getText().toString().replace(".", ""));
                    }
                }
            }
        });
    }

    private void SetEditConductor(View v) {
        edtDVConductor = v.findViewById(R.id.edtDVConductor);
        edtNomConductor = v.findViewById(R.id.edtNomConductor);
        edtMovilConductor = v.findViewById(R.id.edtMovilConductor);
        edtRutConductor = v.findViewById(R.id.edtRutConductor);
        edtRutConductor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (edtRutConductor.getText().toString().equals("")){
                        Utility.ShowAlertDialog(context, "Error", "Rut conductor obligatorio.");
                    }else {
                        ValidaWebServiceConductor(edtRutConductor.getText().toString().replace(".", ""));
                    }
                }
            }
        });
    }

    private void Escaner() {
        Intent localIntent = new Intent(context, LectorCodigoBarras.class);
        context.startActivity(localIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode== Activity.RESULT_OK){
            Bundle ext = data.getExtras();
            Bitmap bmp = (Bitmap)ext.get("data");
            ImageView imagen = new ImageView(context);
            imagen.setImageBitmap(bmp);

            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Foto");
            alertDialog.setView(imagen);
            alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                }
            });
            alertDialog.setIcon(R.mipmap.ic_launcher);
            alertDialog.show();
        }
    }




}
