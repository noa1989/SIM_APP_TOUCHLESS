package cl.genesys.appchofer;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class GlobalActivity extends Application {
    private static Context context;

    public static synchronized Context getGlobalContext() {
        return context;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        if (GlobalActivity.context == null) {
            GlobalActivity.context = getApplicationContext();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
