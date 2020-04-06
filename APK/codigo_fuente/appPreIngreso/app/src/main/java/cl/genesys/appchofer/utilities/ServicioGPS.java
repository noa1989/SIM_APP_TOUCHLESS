package cl.genesys.appchofer.utilities;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

import cl.genesys.appchofer.layout.pre_ingreso;

import static cl.genesys.appchofer.layout.MainActivity.confirmaGDE;
import static cl.genesys.appchofer.layout.MainActivity.viaje_destino;

public class ServicioGPS extends Service {
    private Context ctx;
    private static Timer timer = new Timer();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate()
    {
        super.onCreate();

        ctx = this;


        startService();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    private void startService()
    {

        timer.scheduleAtFixedRate(new ServicioGPS.mainTask(), 0, 30000);
    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
            toastHandler.sendEmptyMessage(0);
        }
    }

    private final Handler toastHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            if (Utility.isOnline(ctx)) {

                GPS gps = new GPS(ctx);
                if (gps.isGPSEnabled) {
                    double latitud = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    pre_ingreso.validaLugarRecepcion(latitud, longitude);
                } else {
                    pre_ingreso.AlertNoGps();
                }

            }else {
                pre_ingreso.AlertNoInternet();
            }
        }
    };

}
