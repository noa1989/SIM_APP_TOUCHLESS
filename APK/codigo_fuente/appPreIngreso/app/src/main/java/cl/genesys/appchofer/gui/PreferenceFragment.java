/*******************************************************************************
 * Copyright (c) 2012-2013 Pieter Pareit.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Contributors:
 * Pieter Pareit - initial API and implementation
 ******************************************************************************/

package cl.genesys.appchofer.gui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.TwoStatePreference;
import android.text.util.Linkify;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import net.vrallev.android.cat.Cat;

import java.net.InetAddress;
import java.util.List;
import java.util.Set;

import cl.genesys.android.DynamicMultiSelectListPreference;
import cl.genesys.appchofer.App;
import cl.genesys.appchofer.AutoConnect;
import cl.genesys.appchofer.FsService;
import cl.genesys.appchofer.FsSettings;
import cl.genesys.appchofer.R;
import lombok.val;

/**
 * This is the main activity for swiftp, it enables the user to start the server service
 * and allows the users to change the settings.
 */
public class PreferenceFragment extends android.preference.PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 14;
    private static final int ACTION_OPEN_DOCUMENT_TREE = 42;

    private DynamicMultiSelectListPreference mAutoconnectListPref;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        TwoStatePreference runningPref = findPref("running_switch");
        updateRunningState();
        FsService.start();
        runningPref.setOnPreferenceChangeListener((preference, newValue) -> {
           /* if ((Boolean) newValue) {

            } else {
                FsService.stop();
            }*/
            return true;
        });

        PreferenceScreen prefScreen = findPref("preference_screen");
        Preference marketVersionPref = findPref("market_version");
        if (!App.isFreeVersion()) {
            prefScreen.removePreference(marketVersionPref);
        }
        if (!(App.isPackageInstalled("com.android.vending") ||
                App.isPackageInstalled("com.google.market"))) {
            prefScreen.removePreference(marketVersionPref);
        }
        marketVersionPref.setOnPreferenceClickListener(preference -> {
            // start the market at our application
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=cl.genesys.appchofer"));
            startActivity(intent);
            return false;
        });

        Preference manageUsersPref = findPref("manage_users");
        updateUsersPref();
        manageUsersPref.setOnPreferenceClickListener((preference) -> {
            startActivity(new Intent(getActivity(), ManageUsersActivity.class));
            return true;
        });

        mAutoconnectListPref = findPref("autoconnect_preference");
        mAutoconnectListPref.setOnPopulateListener(
                pref -> {
                    Cat.d("autoconnect populate listener");

                    WifiManager wifiManager = (WifiManager)
                            getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
                    if (configs == null) {
                        Cat.e("Unable to receive wifi configurations, bark at user and bail");
                        Toast.makeText(getActivity(),
                                R.string.autoconnect_error_enable_wifi_for_access_points,
                                Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    CharSequence[] ssids = new CharSequence[configs.size()];
                    CharSequence[] niceSsids = new CharSequence[configs.size()];
                    for (int i = 0; i < configs.size(); ++i) {
                        ssids[i] = configs.get(i).SSID;
                        String ssid = configs.get(i).SSID;
                        if (ssid.length() > 2 && ssid.startsWith("\"") && ssid.endsWith("\"")) {
                            ssid = ssid.substring(1, ssid.length() - 1);
                        }
                        niceSsids[i] = ssid;
                    }
                    pref.setEntries(niceSsids);
                    pref.setEntryValues(ssids);
                    pref.setValues(FsSettings.getAutoConnectList());
                });
        mAutoconnectListPref.setOnPreferenceClickListener(preference -> {
            Cat.d("Clicked to open auto connect list preference");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        new AlertDialog.Builder(getContext())
                                .setTitle(R.string.request_coarse_location_dlg_title)
                                .setMessage(R.string.request_coarse_location_dlg_message)
                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_REQUEST_CODE);
                                })
                                .setOnCancelListener(dialog -> {
                                    mAutoconnectListPref.getDialog().cancel();
                                })
                                .create()
                                .show();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_REQUEST_CODE);
                    }
                }
            }
            return false;
        });
        mAutoconnectListPref.setOnPreferenceChangeListener((preference, newValue) -> {
            Cat.d("Changed auto connect list preference");

            Set<String> oldList = FsSettings.getAutoConnectList();
            Set<?> newList = (Set<?>) newValue;

            Cat.d("Old List: " + oldList + " New List: " + newList);

            WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null) {
                return true;
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo == null) {
                Cat.e("Null wifi info received, bailing");
                return true;
            }
            Cat.d("We are connected to " + wifiInfo.getSSID());
            if (newList.contains(wifiInfo.getSSID())) {
                FsService.start();
            }
            if (oldList.contains(wifiInfo.getSSID()) && !newList.contains(wifiInfo.getSSID())) {
                FsService.stop();
            }
            return true;
        });

        EditTextPreference portNumberPref = findPref("portNum");
        portNumberPref.setSummary(String.valueOf(FsSettings.getPortNumber()));
        portNumberPref.setOnPreferenceChangeListener((preference, newValue) -> {
            String newPortNumberString = (String) newValue;
            if (preference.getSummary().equals(newPortNumberString))
                return false;
            int portNumber = 0;
            try {
                portNumber = Integer.parseInt(newPortNumberString);
            } catch (Exception e) {
                Cat.d("Error parsing port number! Moving on...");
            }
            if (portNumber <= 0 || 65535 < portNumber) {
                Toast.makeText(getActivity(),
                        R.string.port_validation_error, Toast.LENGTH_LONG).show();
                return false;
            }
            preference.setSummary(newPortNumberString);
            FsService.stop();
            return true;
        });

        final CheckBoxPreference wakelockPref = findPref("stayAwake");
        wakelockPref.setOnPreferenceChangeListener((preference, newValue) -> {
            FsService.stop();
            return true;
        });

        final CheckBoxPreference writeExternalStoragePref = findPref("writeExternalStorage");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String externalStorageUri = FsSettings.getExternalStorageUri();
            if (externalStorageUri == null) {
                writeExternalStoragePref.setChecked(false);
            }
            writeExternalStoragePref.setOnPreferenceChangeListener((preference, newValue) -> {
                if ((boolean) newValue) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    startActivityForResult(intent, ACTION_OPEN_DOCUMENT_TREE);
                    return false;
                } else {
                    FsSettings.setExternalStorageUri(null);
                    return true;
                }
            });
        } else {
            writeExternalStoragePref.setEnabled(false);
            writeExternalStoragePref.setChecked(true);
            writeExternalStoragePref.setSummary(getString(R.string.write_external_storage_old_android_version_summary));
        }


        ListPreference themePref = findPref("theme");
        themePref.setSummary(themePref.getEntry());
        themePref.setOnPreferenceChangeListener((preference, newValue) -> {
            themePref.setSummary(themePref.getEntry());
            getActivity().recreate();
            return true;
        });

        val showNotificationIconPref = findPref("show_notification_icon_preference");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val appearanceScreen = (PreferenceScreen) findPreference("appearance_screen");
            appearanceScreen.removePreference(showNotificationIconPref);
        }
        showNotificationIconPref.setOnPreferenceChangeListener((preference, newValue) -> {
            FsService.stop();
            return true;
        });

        Preference helpPref = findPref("help");
        helpPref.setOnPreferenceClickListener(preference -> {
            Cat.v("On preference help clicked");
            Context context = getActivity();
            AlertDialog ad = new AlertDialog.Builder(context)
                    .setTitle(R.string.help_dlg_title)
                    .setMessage(R.string.help_dlg_message)
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
            ad.show();
            Linkify.addLinks((TextView) ad.findViewById(android.R.id.message),
                    Linkify.ALL);
            return true;
        });

        Preference aboutPref = findPref("about");
        aboutPref.setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(getActivity(), AboutActivity.class));
            return true;
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACCESS_COARSE_LOCATION_REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                mAutoconnectListPref.getDialog().cancel();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        updateRunningState();
        updateUsersPref();

        Cat.d("onResume: Registering the FTP server actions");
        IntentFilter filter = new IntentFilter();
        filter.addAction(FsService.ACTION_STARTED);
        filter.addAction(FsService.ACTION_STOPPED);
        filter.addAction(FsService.ACTION_FAILEDTOSTART);
        getActivity().registerReceiver(mFsActionsReceiver, filter);

        PreferenceManager.getDefaultSharedPreferences(App.getAppContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        Cat.v("onPause: Unregistering the FTPServer actions");
        getActivity().unregisterReceiver(mFsActionsReceiver);

        PreferenceManager.getDefaultSharedPreferences(App.getAppContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        Cat.d("onActivityResult called");
        if (requestCode == ACTION_OPEN_DOCUMENT_TREE && resultCode == Activity.RESULT_OK) {
            Uri treeUri = resultData.getData();
            String path = treeUri.getPath();

            final CheckBoxPreference writeExternalStorage_pref = findPref("writeExternalStorage");
            if (!":".equals(path.substring(path.length() - 1)) || path.contains("primary")) {
                writeExternalStorage_pref.setChecked(false);
            } else {
                FsSettings.setExternalStorageUri(treeUri.toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    getActivity().getContentResolver().takePersistableUriPermission(treeUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                writeExternalStorage_pref.setChecked(true);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            // The AutoConnect service must be started or stopped depending if there are
            // networks to monitor or not.
            case "autoconnect_preference":
                AutoConnect.maybeStartService(App.getAppContext());
                break;
        }
    }

    /**
     * Update the summary for the users. When there are no users, ask to add at least one user.
     * When there is one user, display helpful message about user/password. When there are
     * multiple users, refer to the list.
     */
    private void updateUsersPref() {
        val manageUsersPref = findPref("manage_users");
        val users = FsSettings.getUsers();
        switch (users.size()) {
            case 0:
                manageUsersPref.setSummary(R.string.manage_users_no_users);
                break;
            case 1:
                val user = users.get(0);
                manageUsersPref.setSummary(user.getUsername() + ":" + user.getPassword());
                break;
            default:
                manageUsersPref.setSummary(R.string.manage_users_multiple_users);
        }
    }

    /**
     * Display helpful message in the summary about the state of the ftp server. When the
     * server is running, display the ip address to reach it. When there was
     * a failure starting the server, let this know.
     */
    private void updateRunningState() {
        Resources res = getResources();
        TwoStatePreference runningPref = findPref("running_switch");
        if (FsService.isRunning()) {
            runningPref.setChecked(true);
            // Fill in the FTP server address
            InetAddress address = FsService.getLocalInetAddress();
            if (address == null) {
                Cat.v("Unable to retrieve wifi ip address");
                runningPref.setSummary(R.string.running_summary_failed_to_get_ip_address);
                return;
            }
            String ipText = "ftp://" + address.getHostAddress() + ":"
                    + FsSettings.getPortNumber() + "/";
            String summary = res.getString(R.string.running_summary_started, ipText);
            runningPref.setSummary(summary);
        } else {
            runningPref.setChecked(false);
            runningPref.setSummary(R.string.running_summary_stopped);
        }
    }

    /**
     * This receiver will check FTPServer.ACTION* messages and will update the button,
     * running_state, if the server is running and will also display at what url the
     * server is running.
     */
    BroadcastReceiver mFsActionsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Cat.v("action received: " + intent.getAction());
            if (intent.getAction() == null) {
                return;
            }
            // remove all pending callbacks
            mHandler.removeCallbacksAndMessages(null);
            // action will be ACTION_STARTED or ACTION_STOPPED
            updateRunningState();
            // or it might be ACTION_FAILEDTOSTART
            final TwoStatePreference runningPref = findPref("running_switch");
            if (intent.getAction().equals(FsService.ACTION_FAILEDTOSTART)) {
                runningPref.setChecked(false);
                mHandler.postDelayed(
                        () -> runningPref.setSummary(R.string.running_summary_failed),
                        100);
                mHandler.postDelayed(
                        () -> runningPref.setSummary(R.string.running_summary_stopped),
                        5000);
            }
        }
    };

    @SuppressWarnings({"unchecked"})
    protected <T extends Preference> T findPref(CharSequence key) {
        return (T) findPreference(key);
    }

}
