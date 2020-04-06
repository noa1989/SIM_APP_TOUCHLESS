package cl.genesys.appchofer.layout;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import cl.genesys.appchofer.R;
import cl.genesys.appchofer.utilities.Constantes;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class presentacion extends Fragment {

    public static SharedPreferences sharedPref;
    public ProgressDialog pDialog;
    String GDE, _OT;
    public static TextView tv_PlantaP, tv_estadoP, txtFechaP, txtRutProP, txtNomProP, txtRutCondP, txtNomCondP, txtNumGD, txtEEPPP, txtPatCamP, txtCarroP, txtLlegadaDestinoP, txtRealDestino, txtDiferenciaHoraria, txtRecep;

    public presentacion() {
        // Required empty public constructor
    }

    public static void limpiar() {
        try {
            tv_PlantaP.setText("");
            tv_estadoP.setText("");
            txtRutProP.setText("");
            txtRutCondP.setText("");
            txtNomProP.setText("");
            txtNomCondP.setText("");
            txtNumGD.setText("");
            txtEEPPP.setText("");
            txtPatCamP.setText("");
            txtCarroP.setText("");
            txtLlegadaDestinoP.setText("");
            txtRealDestino.setText("");
            txtDiferenciaHoraria.setText("");
            txtRecep.setText("");
        }catch (Exception e) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_presentacion, container, false);


        sharedPref = this.getActivity().getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
        tv_PlantaP = v.findViewById(R.id.tv_PlantaP);
        tv_estadoP = v.findViewById(R.id.tv_estadoP);
        txtRutProP = v.findViewById(R.id.txtRutProP);
        txtRutCondP = v.findViewById(R.id.txtRutCondP);
        txtNomProP = v.findViewById(R.id.txtNomProP);
        txtNomCondP = v.findViewById(R.id.txtNomCondP);
        txtNumGD = v.findViewById(R.id.txtNumGD);
        txtEEPPP = v.findViewById(R.id.txtEEPPP);
        txtPatCamP = v.findViewById(R.id.txtPatCamP);
        txtCarroP = v.findViewById(R.id.txtCarroP);
        txtLlegadaDestinoP = v.findViewById(R.id.txtLlegadaDestinoP);
        txtRealDestino = v.findViewById(R.id.txtRealDestino);
        txtDiferenciaHoraria = v.findViewById(R.id.txtDiferenciaHoraria);
        txtRecep = v.findViewById(R.id.txtRecep);

        try {
            MainActivity.llenarPresentacion(sharedPref.getString("OT", ""));
        }catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }
}
