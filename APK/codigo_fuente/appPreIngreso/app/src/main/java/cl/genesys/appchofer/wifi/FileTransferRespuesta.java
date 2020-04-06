package cl.genesys.appchofer.wifi;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import cl.genesys.appchofer.layout.MainActivity;
import cl.genesys.appchofer.utilities.Constantes;
import cl.genesys.appchofer.beans.WiFiTransferModal;

import static cl.genesys.appchofer.layout.MainActivity.context;

public class FileTransferRespuesta extends IntentService {
    Handler mHandler;
    public static SharedPreferences sharedPref;
    public static final int SOCKET_TIMEOUT = 5000;
    public static final String ACTION_SEND_FILE = "cl.genesys.android.AppChofer.SEND_FILE";
    public static final String EXTRAS_FILE_PATH = "file_url";
    public static final String EXTRAS_GROUP_OWNER_ADDRESS = "go_host";
    public static final String EXTRAS_GROUP_OWNER_PORT = "go_port";

    public static  int PORT = 8888;
    public static final String inetaddress = "inetaddress";
    public static final int ByteSize = 512;
    public static final String Extension = "extension";
    public static final String Filelength = "filelength";
    public FileTransferRespuesta(String name) {
        super(name);
    }

    public FileTransferRespuesta() {
        super("FileTransferRespuesta");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = getApplicationContext();
        String directory = Environment.getExternalStorageDirectory() + "/" + context.getPackageName() + "/prueba_doc/";
        File  f = new File("/storage/emulated/0/prueba_doc/respuesta_doc_1.xml");

        String extension = "";
        int i = f.getName().lastIndexOf('.');
        if (i > 0) {
            extension = f.getName().substring(i + 1);
        }
        String host = intent.getExtras().getString(EXTRAS_GROUP_OWNER_ADDRESS);
        Socket socket = new Socket();
        int port = intent.getExtras().getInt(EXTRAS_GROUP_OWNER_PORT);

        String filelength = String.valueOf(f.length());

        try {
            Log.d(WiFiDirectActivity.TAG, "Opening client socket - ");
            socket.bind(null);
            socket.connect((new InetSocketAddress(host, port)), SOCKET_TIMEOUT);

            Log.d(WiFiDirectActivity.TAG, "Client socket - " + socket.isConnected());
            OutputStream stream = socket.getOutputStream();
            ContentResolver cr = context.getContentResolver();
            InputStream is = null;

            Long FileLength = Long.parseLong(filelength);
            WiFiTransferModal transObj = null;
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            if (transObj == null) transObj = new WiFiTransferModal();


            transObj = new WiFiTransferModal(f.getName(), FileLength);

            oos.writeObject(transObj);

            File dir = new File(directory);

            File[] files = dir.listFiles();
            int total = 0;
            for (File fi : files) {
                total += 1;
            }

            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            DataOutputStream dos = new DataOutputStream(bos);

            dos.writeInt(files.length);
            int cont = 0;

            for (File file : files) {
                cont++;
                long length = file.length();
                dos.writeLong(length);

                String name = file.getName();
                dos.writeUTF(name);
                DeviceDetailFragment.progress(total, cont);
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);

                int theByte = 0;
                while ((theByte = bis.read()) != -1) bos.write(theByte);

                bis.close();
            }


            dos.close();


            try {
                is = cr.openInputStream(Uri.parse("file://cl.genesys.fileprovider/storage/emulated/0/prueba_doc/prueba_doc.xml"));
            } catch (FileNotFoundException e) {
                Log.d(WiFiDirectActivity.TAG, e.toString());
            }
            byte buf[] = new byte[FileTransferRespuesta.ByteSize];
            int len;

            Log.d(WiFiDirectActivity.TAG, "Client: Data written");
            oos.close();    //close the ObjectOutputStream after sending data.

        } catch(IOException e){
            Log.e(WiFiDirectActivity.TAG, e.getMessage());
            e.printStackTrace();
            CommonMethods.e("Unable to connect host", "service socket error in wififiletransferservice class");
            mHandler.post(new Runnable() {

                public void run() {
                    // TODO Auto-generated method stub
                    Toast.makeText(FileTransferRespuesta.this, "El dispositivo vinculado no está listo para recibir el archivo.", Toast.LENGTH_LONG).show();
                }
            });
            DeviceDetailFragment.DismissProgressDialog();

        } finally{
            if (socket != null) {
                if (socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // Give up
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
