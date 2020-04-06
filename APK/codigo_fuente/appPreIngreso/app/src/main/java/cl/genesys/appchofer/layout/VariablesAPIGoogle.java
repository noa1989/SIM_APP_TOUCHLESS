package cl.genesys.appchofer.layout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import cl.genesys.appchofer.R;
import cl.genesys.appchofer.Util;
import cl.genesys.appchofer.utilities.GPS;
import cl.genesys.appchofer.utilities.NetworkUtils;
import cl.genesys.appchofer.utilities.Utility;
import cl.genesys.appchofer.wifi.DeviceDetailFragment;

import static cl.genesys.appchofer.utilities.Constantes.APISERVERKEY;

public class VariablesAPIGoogle extends AppCompatActivity {
    public static Context context;
    public static String json;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variables_apigoogle);
        context = this;
        String lat1 = "-36.741985";
        String lon1 = "-73.090829";
        GPS gps = new GPS(this);

        Runnable r = new Runnable() {
            public void run() {


                if (gps.isGPSEnabled) {
                    String URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + gps.getLatitude() + "," + gps.getLongitude() + "&destinations=" + lat1 + "," + lon1 + "&key=" + APISERVERKEY;


                    json = NetworkUtils.getJSONFromAPI(URL);
                    String hola = json;
                    mHandler.sendEmptyMessage(0);
                    //
                }

            }

            Handler mHandler = new Handler()
            {
                public void handleMessage(Message msg)
                {
                    Utility.ShowAlertDialog(context, "Datos", json);
                }
            };
        };



        new Thread(r).start();


    }
}
