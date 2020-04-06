package cl.genesys.appchofer.wifi;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cl.genesys.appchofer.FsService;
import cl.genesys.appchofer.R;
import cl.genesys.appchofer.layout.MainActivity;
import cl.genesys.appchofer.utilities.Constantes;
import cl.genesys.appchofer.utilities.ServicioConectado;
import cl.genesys.appchofer.utilities.ServicioOTDisponible;
import cl.genesys.appchofer.utils.PermissionsAndroid;

import static cl.genesys.appchofer.layout.MainActivity.turnOffHotspot;


public class WiFiDirectActivity extends AppCompatActivity implements ChannelListener, DeviceListFragment.DeviceActionListener {


    private Toolbar mToolbar;
    public static final String TAG = "AppChofer";
    public static WifiP2pManager manager;
    private boolean isWifiP2pEnabled = false;
    private boolean retryChannel = false;
    Context context;
    private final IntentFilter intentFilter = new IntentFilter();
    public static Channel channel;
    private BroadcastReceiver receiver = null;
    public static TextView txEmpresa, tvPatente, tvOrigenT;
    public static RadioButton rdbConectadoM;
    public static FloatingActionButton ftbActualizarWIFI, ftbVolverWifi, fltWIFIReload;
    public static SharedPreferences sharedPref;
    public static String usuario;
    WifiManager wifiManager;
    private BluetoothAdapter bAdapter;
    /**
     * @param isWifiP2pEnabled the isWifiP2pEnabled to set
     */
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);

        context = this;


        bAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bAdapter == null)
        {

        }else {
            if(bAdapter.isEnabled())
                bAdapter.disable();
        }
        /*IntentFilter filtro = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(bReceiver, filtro);*/

        wifiManager= (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        /* Estado batería */
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

        /* Fin Estado batería */

        //Integer estadoAion =  Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 1);
        rdbConectadoM = findViewById(R.id.rdbConectadoM);


        /*wifiManager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);*/


        sharedPref = getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", "");

        fltWIFIReload = findViewById(R.id.fltWIFIReload);
        fltWIFIReload.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove("doc_envio");
                    editor.commit();
                    wifiManager.setWifiEnabled(false);
                    wifiManager.setWifiEnabled(true);
                    modificarNanemWifiDirect();
                }
            });

        ftbActualizarWIFI = findViewById(R.id.ftbActualizarWIFI);
        ftbVolverWifi = findViewById(R.id.ftbVolverWifi);
        ftbActualizarWIFI.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isWifiP2pEnabled) {
                        Toast.makeText(WiFiDirectActivity.this, "Búsqueda ha fallado, sin conexión de WIFI, activando señal.", Toast.LENGTH_SHORT).show();
                        wifiManager.setWifiEnabled(true);
                    }else {
                        final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
                                .findFragmentById(R.id.frag_list);
                        fragment.onInitiateDiscovery();
                        manager.discoverPeers(channel, new ActionListener() {

                            @Override
                            public void onSuccess() {
                                Toast.makeText(WiFiDirectActivity.this, "Buscando dispositivos.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int reasonCode) {
                                if (reasonCode == 2) {
                                    Toast.makeText(WiFiDirectActivity.this, "Búsqueda ha fallado, sin conexión de WIFI, activando señal.", Toast.LENGTH_SHORT).show();
                                    wifiManager.setWifiEnabled(true);
                                }else {
                                    Toast.makeText(WiFiDirectActivity.this, "Error, intente nuevamente o reinicie señal de WIFI.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });

        ftbVolverWifi.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ftbVolverWifi.setEnabled(false);
                        try {
                            //System.exit(0);
                            ((DeviceListFragment.DeviceActionListener) DeviceDetailFragment.context).disconnect();
                            stopService(new Intent(WiFiDirectActivity.this, FileTransferService.class));
                            finish();


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("menu_gde", "1");
                        startActivity(intent);

                    }
                });

        initViews();

        try{
            solicitar_permisos(context,new String[]{
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            });
            // Manifest.permission.READ_PHONE_NUMBERS,
        } catch (Exception ex){
            Log.d("SOLITAR PERMISOS", ex.getMessage());
        }

        //final Uri data = FileProvider.getUriForFile(context, "myprovider", new File(file_path));
        //context.grantUriPermission(context.getPackageName(), data, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        checkStoragePermission();

        Runnable r = new Runnable() {
            public void run() {

                DeviceDetailFragment.runYourBackgroundTaskHere();
            }
        };

        new Thread(r).start();
    }

    public void _alertDialogBuilderClose() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmación");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("¿Desea salir de la aplicación?")
                .setCancelable(false)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNeutralButton("Minimizar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                })
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            stopService(new Intent(WiFiDirectActivity.this, ServicioConectado.class));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        try {
                            stopService(new Intent(WiFiDirectActivity.this, ServicioOTDisponible.class));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        try {
                            //stopService(new Intent(WiFiDirectActivity.this, ServicioGPS.class));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        //System.exit(0);
                      /*  onDestroy();
                        finish();
                        moveTaskToBack(true);*/

                        finish();
                        //System.exit(0);
                        Intent salida = new Intent(Intent.ACTION_MAIN);
                        android.os.Process.killProcess(android.os.Process.myPid());


                        //finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private final BroadcastReceiver bReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            final int estado = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR);
            switch (estado)
            {
                // Apagado
                case BluetoothAdapter.STATE_OFF:
                {
                    Toast.makeText(context, "Bluetooth desactivado.", Toast.LENGTH_SHORT).show();
                    /*((Button)findViewById(R.id.btnBluetooth)).setText(R.string.ActivarBluetooth);*/
                    break;
                }

                // Encendido
                case BluetoothAdapter.STATE_ON:
                {
                    Toast.makeText(context, "El bluetooth debe estar desactivado para transmitir, desactivando bluetooth.", Toast.LENGTH_SHORT).show();
                    bAdapter.disable();
                   /* ((Button)findViewById(R.id.btnBluetooth)).setText(R.string.DesactivarBluetooth);*/
                    break;
                }
                default:
                    break;
            }
        }
    };



    private static final int PERMISSION_REQUEST_CODE = 1;
    public static void solicitar_permisos(Context context, String[] permissions) {
        ArrayList<String> permissions_solicitar = new ArrayList<String>();
        for (String permiso : permissions) {
            if (ContextCompat.checkSelfPermission(context,
                    permiso)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions_solicitar.add(permiso);
            }
        }

        if (permissions_solicitar.size() > 0) {
            String[] mStringArray = new String[permissions_solicitar.size()];
            mStringArray = permissions_solicitar.toArray(mStringArray);

            ActivityCompat.requestPermissions((Activity) context,
                    mStringArray,
                    PERMISSION_REQUEST_CODE);
        }


//            if (ContextCompat.checkSelfPermission(context,
//                Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,
//                    Manifest.permission.READ_PHONE_STATE)) {
//
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions((Activity) context,
//                        permissions,
//                        PERMISSION_REQUEST_CODE);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        }
    }

    /*
      Ask permissions for Filestorage if device api > 23
       */
    //  @TargetApi(Build.VERSION_CODES.M)
    private void checkStoragePermission() {
        boolean isExternalStorage = PermissionsAndroid.getInstance().checkWriteExternalStoragePermission(this);
        if (!isExternalStorage) {
            PermissionsAndroid.getInstance().requestForWriteExternalStoragePermission(this);
        }
    }

    private void initViews() {
        // add necessary intent values to be matched.

        txEmpresa = findViewById(R.id.txEmpresa);
        String empresa = sharedPref.getString("empresa", "");
        txEmpresa.setText(empresa);
        tvPatente = findViewById(R.id.tvPatente);
        tvPatente.setText(sharedPref.getString("patente", ""));
        rdbConectadoM = findViewById(R.id.rdbConectadoM);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), this);


        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank. Code for peer discovery goes in the
                // onReceive method, detailed below.
            }

            @Override
            public void onFailure(int reasonCode) {
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.
            }
        });


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);




        /*final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
                .findFragmentById(R.id.frag_list);
        fragment.onInitiateDiscovery();
        manager.discoverPeers(channel, new ActionListener() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int reasonCode) {
            }
        });*/

        obtenerPreferencias();

        modificarNanemWifiDirect();

        MainActivity.turnOffHotspot();
        FsService.stop();
    }

    private void modificarNanemWifiDirect() {
        try {
            Class[] paramTypes = new Class[3];
            paramTypes[0] = WifiP2pManager.Channel.class;
            paramTypes[1] = String.class;
            paramTypes[2] = WifiP2pManager.ActionListener.class;
            Method setDeviceName = manager.getClass().getMethod(
                    "setDeviceName", paramTypes);
            setDeviceName.setAccessible(true);

            Object arglist[] = new Object[3];
            arglist[0] = channel;
            arglist[1] = sharedPref.getString("patente", "") + "_C";
            arglist[2] = new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    Log.d("setDeviceName succeeded", "true");
                }

                @Override
                public void onFailure(int reason) {
                    Log.d("setDeviceName failed", "true");
                }
            };
            setDeviceName.invoke(manager, arglist);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void obtenerPreferencias() {
        //getPrefUsers
    }


    /**
     * register the BroadcastReceiver with the intent values to be matched
     */
    @Override
    public void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank. Code for peer discovery goes in the
                // onReceive method, detailed below.
            }

            @Override
            public void onFailure(int reasonCode) {
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        turnOffHotspot();
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank. Code for peer discovery goes in the
                // onReceive method, detailed below.
            }

            @Override
            public void onFailure(int reasonCode) {
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.
            }
        });
    }

    /**
     * Remove all peers and clear all fields. This is called on
     * BroadcastReceiver receiving a state change event.
     */
    public void resetData() {
        DeviceListFragment fragmentList = (DeviceListFragment) getFragmentManager()
                .findFragmentById(R.id.frag_list);
        DeviceDetailFragment fragmentDetails = (DeviceDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_detail);
        if (fragmentList != null) {
            fragmentList.clearPeers();
        }

        if (fragmentDetails != null) {
            fragmentDetails.resetViews();
        }
    }

    /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_items, menu);
        return true;
    }*/

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */

    @Override
    public void showDetails(WifiP2pDevice device) {
        DeviceDetailFragment fragment = (DeviceDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_detail);
        fragment.showDetails(device);

    }

    @Override
    public void connect(WifiP2pConfig config) {
        manager.connect(channel, config, new ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(WiFiDirectActivity.this, "Conexión fallida, reintentar.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void disconnect() {
        final DeviceDetailFragment fragment = (DeviceDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_detail);
        fragment.resetViews();
        manager.removeGroup(channel, new ActionListener() {

            @Override
            public void onFailure(int reasonCode) {
                Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);

            }

            @Override
            public void onSuccess() {
                fragment.getView().setVisibility(View.GONE);
            }

        });
    }

    @Override
    public void onChannelDisconnected() {
        // we will try once more
        try {
            if (manager != null && !retryChannel) {
                Toast.makeText(this, "Channel lost. Trying again", Toast.LENGTH_LONG).show();
                resetData();
                retryChannel = true;
                manager.initialize(this, getMainLooper(), this);
            } else {
                Toast.makeText(this,
                        "Intente reiniciar la WIFI.",
                        Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){

        }
    }

    @Override
    public void cancelDisconnect()  {

        /*
         * A cancel abort request by user. Disconnect i.e. removeGroup if
         * already connected. Else, request WifiP2pManager to abort the ongoing
         * request
         */
        if (manager != null) {
            final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
                    .findFragmentById(R.id.frag_list);
            if (fragment.getDevice() == null
                    || fragment.getDevice().status == WifiP2pDevice.CONNECTED) {
                disconnect();
            } else if (fragment.getDevice().status == WifiP2pDevice.AVAILABLE
                    || fragment.getDevice().status == WifiP2pDevice.INVITED) {

                manager.cancelConnect(channel, new ActionListener() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(WiFiDirectActivity.this, "Aborting connection",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(WiFiDirectActivity.this,
                                "Connect abort request failed. Reason Code: " + reasonCode,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> listOfFragments = getSupportFragmentManager().getFragments();

        if(listOfFragments.size()>=1){
            for (Fragment fragment : listOfFragments) {
                if(fragment instanceof DeviceDetailFragment){
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
//        (getSupportFragmentManager().findFragmentById(R.id.device_detail_container)).
//                onActivityResult(requestCode,resultCode,data);
    }

    public void onBackPressed() {
        _alertDialogBuilderClose();
    }

    public void alertDialogBuilderClose() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmación");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("¿Desea salir de la aplicación?")
                .setCancelable(false)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNeutralButton("Minimizar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                })
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int p = android.os.Process.myPid();
                        android.os.Process.killProcess(p);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
