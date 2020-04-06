package cl.genesys.appchofer.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import net.vrallev.android.cat.Cat;

import cl.genesys.appchofer.App;
import cl.genesys.appchofer.FsService;
import cl.genesys.appchofer.FsSettings;
import cl.genesys.appchofer.R;

public class iniciarFTP extends AppCompatActivity {
    final static int PERMISSIONS_REQUEST_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Cat.d("created");
        //setTheme(FsSettings.getTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_ftp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
        }

       /* if (App.isFreeVersion() && App.isPaidVersionInstalled()) {
            Cat.d("Running demo while paid is installed");
            AlertDialog ad = new AlertDialog.Builder(this)
                    .setTitle(R.string.demo_while_paid_dialog_title)
                    .setMessage(R.string.demo_while_paid_dialog_message)
                    .setPositiveButton(getText(android.R.string.ok), (d, w) -> finish())
                    .create();
            ad.show();
        }*/

       /* getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenceFragment())
                .commit();*/
        FsService.start();
    }
}
