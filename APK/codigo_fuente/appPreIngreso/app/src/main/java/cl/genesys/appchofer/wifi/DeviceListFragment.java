package cl.genesys.appchofer.wifi;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cl.genesys.appchofer.R;
import cl.genesys.appchofer.utilities.Constantes;

import static android.content.Context.MODE_PRIVATE;


/**
 * A ListFragment that displays available peers on discovery and requests the
 * parent activity to handle user interaction events
 */
public class DeviceListFragment extends ListFragment implements PeerListListener {

    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    ProgressDialog progressDialog = null;
    View mContentView = null;
    private WifiP2pDevice device;
    public static SharedPreferences sharedPref;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.setListAdapter(new WiFiPeerListAdapter(getActivity(), R.layout.row_devices, peers));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPref = this.getActivity().getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
        mContentView = inflater.inflate(R.layout.device_list, null);
        return mContentView;
    }

    /**
     * @return this device
     */
    public WifiP2pDevice getDevice() {
        return device;
    }

    private static String getDeviceStatus(int deviceStatus) {
        Log.d(WiFiDirectActivity.TAG, "Peer status :" + deviceStatus);
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";

        }
    }

    /**
     * Initiate a connection with the peer.
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ArrayList<Integer> coloredItems = new ArrayList<Integer>();
        WifiP2pDevice device = (WifiP2pDevice) getListAdapter().getItem(position);
        ((DeviceActionListener) getActivity()).showDetails(device);

        for (int a = 0; a < l.getChildCount(); a++) {
            l.getChildAt(a).setBackgroundColor(Color.parseColor("#ffffff"));
        }

        v.setBackgroundColor(Color.parseColor("#CCCCCC"));

       /* for (int a = 0; a < l.getChildCount(); a++) {
            l.getChildAt(a).setBackgroundColor(Color.parseColor("#ffffff"));
        }

        v.setBackgroundColor(Color.parseColor("#CCCCCC"));*/

        // Conección automática
        /*WifiP2pConfig config = new WifiP2pConfig();
        if (config != null && config.deviceAddress != null && device != null) {
            config.deviceAddress = device.deviceAddress;
            config.wps.setup = WpsInfo.PBC;
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel",
                    "Connecting to :" + device.deviceAddress, true, true
            );
            ((DeviceListFragment.DeviceActionListener) getActivity()).connect(config);
        } else {

        }*/
    }

    /**
     * Array adapter for ListFragment that maintains WifiP2pDevice list.
     */
    private class WiFiPeerListAdapter extends ArrayAdapter<WifiP2pDevice> {

        private List<WifiP2pDevice> items;

        /**
         * @param context
         * @param textViewResourceId
         * @param objects
         */
        public WiFiPeerListAdapter(Context context, int textViewResourceId, List<WifiP2pDevice> objects) {
            super(context, textViewResourceId, objects);
            items = objects;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row_devices, null);
            }
            WifiP2pDevice device = items.get(position);
            if (device != null) {
                TextView top = (TextView) v.findViewById(R.id.device_name);
                TextView bottom = (TextView) v.findViewById(R.id.device_details);
                if (top != null) {
                    top.setText(device.deviceName);
                }
                if (bottom != null) {
                    /*public static final int CONNECTED   = 0;
                    public static final int INVITED     = 1;
                    public static final int FAILED      = 2;
                    public static final int AVAILABLE   = 3;
                    public static final int UNAVAILABLE = 4;*/
                    switch (device.status){
                        case 0:
                            bottom.setText("Conectado");
                            bottom.setTypeface(null, Typeface.BOLD);
                            v.setBackgroundColor(Color.parseColor("#CCCCCC"));
                            break;
                        case 1:
                            bottom.setText("Conectando...");
                            bottom.setTypeface(null, Typeface.BOLD);
                            v.setBackgroundColor(Color.parseColor("#CCCCCC"));
                            break;
                        case 2:
                            bottom.setText("Fallado");
                            bottom.setTypeface(null, Typeface.NORMAL);
                            v.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            break;
                        case 3:
                            bottom.setText("Disponible");
                            bottom.setTypeface(null, Typeface.NORMAL);
                            v.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            break;
                        case 4:
                            bottom.setText("No disponible");
                            bottom.setTypeface(null, Typeface.NORMAL);
                            v.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            break;
                    }
                }
            }

            return v;

        }
    }

    /**
     * Update UI for this device.
     * 
     * @param device WifiP2pDevice object
     */
    public void updateThisDevice(WifiP2pDevice device) {
        this.device = device;
        TextView view = (TextView) mContentView.findViewById(R.id.tvNombre);
        view.setText(device.deviceName);

        //view = (TextView) mContentView.findViewById(R.id.my_status);
        //view.setText(getDeviceStatus(device.status));
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        File fi = new File("/storage/emulated/0/cl.genesys.appchofer/sinc/transmision_qr.txt"); // archivo que queda al descargar desde QR
        File fi2 = new File("/storage/emulated/0/cl.genesys.appchofer/sinc/transmision_wifi.txt"); // archivo que queda al descargar desde QR

        if (sharedPref.getString("doc_envio", "").equals("1") || fi.exists() || fi2.exists()) {
            DeviceDetailFragment.button.setText("Recibiendo");
            DeviceDetailFragment.button.setEnabled(false);
        }else {
            //mContentView.findViewById(R.id.btn_start_client).setVisibility(View.VISIBLE);
            DeviceDetailFragment.button.setText("Enviar");
            DeviceDetailFragment.button.setEnabled(true);
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        peers.clear();
        for (WifiP2pDevice s : peerList.getDeviceList())
        {
            String typeDevice = s.deviceName.split("_")[s.deviceName.split("_").length -1];
            if (!s.primaryDeviceType.equals("1-0050F200-0") && typeDevice.equals("D"))
                peers.add(s);
        }

        //peers.addAll(peerList.getDeviceList());
        ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
        if (peers.size() == 0) {
            Log.d(WiFiDirectActivity.TAG, "\n" + "No se encontraron dispositivos.");
            return;
        }
    }

    public void clearPeers() {
        peers.clear();
        ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
    }

    public void onInitiateDiscovery() {
       /* if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = ProgressDialog.show(getActivity(), "Presione atrás para cancelar", "Buscando dispositivos", true,
                true, new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        
                    }
                });*/
    }

    public interface DeviceActionListener {

        void showDetails(WifiP2pDevice device);

        void cancelDisconnect();

        void connect(WifiP2pConfig config);

        void disconnect();
    }

}
