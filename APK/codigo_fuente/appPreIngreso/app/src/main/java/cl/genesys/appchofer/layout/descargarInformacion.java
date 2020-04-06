package cl.genesys.appchofer.layout;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.scheme.Wifi;

import java.io.File;
import java.net.InetAddress;

import cl.genesys.appchofer.App;
import cl.genesys.appchofer.FsService;
import cl.genesys.appchofer.FsSettings;
import cl.genesys.appchofer.R;
import cl.genesys.appchofer.utilities.Constantes;
import cl.genesys.appchofer.utilities.Utility;

import static cl.genesys.appchofer.layout.MainActivity.context;
import static cl.genesys.appchofer.layout.MainActivity.currentConfig;
import static cl.genesys.appchofer.layout.MainActivity.isHotspotEnabled;
import static cl.genesys.appchofer.layout.MainActivity.turnOnHotspot;

/**
 * A simple {@link Fragment} subclass.
 */
public class descargarInformacion extends Fragment {


    ImageButton imgBHostpot, imgBCompartirWIFI;
    ImageView imagenCodigo;
    private static final int ALTURA_CODIGO = 500, ANCHURA_CODIGO = 500;

    public descargarInformacion() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_descargar_informacion, container, false);
        imagenCodigo = v.findViewById(R.id.imagenCodigo);
        imgBHostpot = v.findViewById(R.id.imgBHostpot);
        imgBHostpot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                turnOnHotspot();


            }
        });
        imgBCompartirWIFI = v.findViewById(R.id.imgBCompartirWIFI);
        imgBCompartirWIFI.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String a = currentConfig.SSID;
                    }
                    if (isHotspotEnabled) {
                        try {
                            Wifi wifi = new Wifi();

                            String ipText = "";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                InetAddress address = FsService.getLocalInetAddress();

                                if (address == null) {
                                    ipText = "-";
                                } else {
                                    ipText = address.toString();
                                }
                                wifi.setSsid(currentConfig.SSID);
                                wifi.setPsk(currentConfig.preSharedKey);
                            }
                            wifi.setHidden(false);
                            wifi.setAuthentication(Wifi.Authentication.WPA);
                            String datosQR = "WIFI:" + currentConfig.SSID + ":" +currentConfig.preSharedKey + ":" + Wifi.Authentication.WPA + ":" + ipText + ":" + FsSettings.getPortNumber();

                            Bitmap bitmap = QRCode.from(datosQR).withSize(ANCHURA_CODIGO, ALTURA_CODIGO).bitmap();
                            imagenCodigo.setImageBitmap(bitmap);

                            if (imagenCodigo.getParent() != null) {
                                ((ViewGroup) imagenCodigo.getParent()).removeView(imagenCodigo); // <- fix
                            }

                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            //ImageView imageView = findViewById(R.id.imagenCodigo);
                            alert.setView(imagenCodigo);
                            alert.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dlg, int sumthin) {

                                }
                            });
                            alert.show();
                        }
                        catch   (Exception e) {
                            Toast.makeText(getContext(), "Error, vuelva a intentar.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(), "Debe activar zona WIFI", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(getContext(), "Error, vuelva a intentar.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }
}
