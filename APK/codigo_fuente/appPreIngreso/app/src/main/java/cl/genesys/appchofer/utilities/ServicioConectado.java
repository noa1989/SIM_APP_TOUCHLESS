package cl.genesys.appchofer.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import cl.genesys.appchofer.R;
import cl.genesys.appchofer.layout.MainActivity;
import cl.genesys.appchofer.layout.ingreso_patente;
import cl.genesys.appchofer.wifi.WiFiDirectActivity;

public class ServicioConectado extends Service {
    public static final String CHANNEL_ID = "ServicioConectadoChannel";
    private static Timer timer = new Timer();
    private Context ctx;
    public static SharedPreferences sharedPref;
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

        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("ConvectorOT")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_patente)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void startService()
    {

        timer.scheduleAtFixedRate(new mainTask(), 0, 10000);

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
            //String[] estados = obtenerEstados(ctx);

          /*  sharedPref = getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
            String OT = sharedPref.getString("OT", "");
            String OT_confirma = sharedPref.getString("OT_confirma", "");
            String documento_subido = sharedPref.getString("documento_subido", "");*/

            if (Utility.isOnline(ctx)){


                try{
                    ingreso_patente.rdbConectado.setChecked(true);
                }
                catch (Exception e) {

                }
                try{
                    WiFiDirectActivity.rdbConectadoM.setChecked(true);
                }
                catch (Exception e) {

                }
                try{
                    MainActivity.rdbConectadoM.setChecked(true);
                }
                catch (Exception e) {

                }
            }else  {
                try{
                    ingreso_patente.rdbConectado.setChecked(false);
                }
                catch (Exception e) {

                }
                try{
                    WiFiDirectActivity.rdbConectadoM.setChecked(false);
                }
                catch (Exception e) {

                }
                try{
                    MainActivity.rdbConectadoM.setChecked(false);
                }
                catch (Exception e) {

                }
            }



            /*if (OT_confirma.equals("1")) {
                File f = new File("/storage/emulated/0/cl.genesys.appchofer/recepcion/OT_" + OT + ".sql");
                if (f.exists()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    //editor.putString("documento_subido", "0");

                //editor.commit();

                    MainActivity.tx_viaje_iniciado.setText("Viaje a destino");
                    MainActivity.tx_viaje_iniciado.setBackgroundColor(0xFF1ABC9C);
                    if (!documento_subido.equals("1") ) {
                        try {
                            File fi = new File("/storage/emulated/0/cl.genesys.appchofer/confirmacion_qr/");
                            if (fi.exists()) {

                                AsyncProcesarSqlChoferTask sql = new AsyncProcesarSqlChoferTask(ctx, f.getPath(), "", "Orden Transporte");
                                sql.execute();
                                new MainActivity.ActualizaOTXMLServer().execute();
                            }else {
                                new MainActivity.ActualizaOTXMLServer().execute();
                            }

                        }catch (Exception e){

                        }
                    }else {
                        editor.putString("viaje_destino", "1");

                        editor.commit();
                        MainActivity.tvPatente.setBackgroundResource(R.drawable.patente_radius_iniciada);
                    }
                }else {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("viaje_destino", "0");
                    editor.commit();
                    MainActivity.tvPatente.setBackgroundResource(R.drawable.patente_radius);
                }
            }*/


            //Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
        }
    };

   /* private String[] obtenerEstados(Context ctx) {
        String[] estados = new String[4];
        if (Settings.System.getInt(this.getApplicationContext().getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1) estados[0] = "1"; else  estados[0] = "0";

        return estados;
    }*/

}
