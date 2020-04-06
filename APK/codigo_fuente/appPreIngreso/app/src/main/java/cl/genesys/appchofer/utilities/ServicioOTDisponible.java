package cl.genesys.appchofer.utilities;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import cl.genesys.appchofer.R;
import cl.genesys.appchofer.bbdd.OrdenesTransporteDao;
import cl.genesys.appchofer.entities.OrdenesTransporte;
import cl.genesys.appchofer.layout.MainActivity;


public class ServicioOTDisponible extends Service {
    private Context ctx;
    private static Timer timer = new Timer();
    public static SharedPreferences sharedPref;

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

        timer.scheduleAtFixedRate(new ServicioOTDisponible.mainTask(), 0, 30000);

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
        public void handleMessage(Message msg)
        {
            MainActivity.validaViaje();
        }
    };
}

