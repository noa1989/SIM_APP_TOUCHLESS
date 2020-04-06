package cl.genesys.appchofer.utilities;

import android.os.Environment;

import static cl.genesys.appchofer.layout.MainActivity.context;

/**
 * Created by jorge.padilla on 08-09-2015.
 */

public interface Constantes {

    // VARIABLES EN SHAREDPREFERENCES


    public static final String token = "03]vCtL54kX8Hxjs4bb[Tzjn7gonRMMSLq46LRdc";


    //public static final String URL_WS_DEFECTO = "http://desarrollocmpc.genesys.cl:112/WS_SIM_CHOFER/WSSIMCHOFERdesa.asmx/";
    public static final String URL_WS_DEFECTO = "http://cmpc-ds119.cmpc.cl/api/Metodos/";
   // public static final String URL_WS_DEFECTO = "http://cmpc-ds119.cmpc.cl:100/WS_SIM_CHOFER/WSSIMCHOFERdesa.asmx/";

    public static final String RUTA_ENVIO = Environment.getExternalStorageDirectory() + "/cl.genesys.appchofer/sinc/";
    public static final String RUTA_ENVIO_DESPACHO = Environment.getExternalStorageDirectory() + "/cl.genesys.appdespacho/en_proceso/";
    public static final String RUTA_SINC_DESPACHO = Environment.getExternalStorageDirectory() + "/cl.genesys.appdespacho/sinc/";
    public static final String RUTA_TERMINO_DESPACHO = Environment.getExternalStorageDirectory() + "/cl.genesys.appdespacho/terminadas/";
    public static final String RUTA_RECEPCION_DESPACHO = Environment.getExternalStorageDirectory() + "/cl.genesys.appdespacho/recepcion/";
    public static final String RUTA_RECEPCION = Environment.getExternalStorageDirectory() + "/cl.genesys.appchofer/recepcion/";

    public static final String APISERVERKEY = "AIzaSyCzxeN4r_etRQKGx_h0u5VXl8k38eFIkUU";
    public static final String keyPreferences = "config";
}
