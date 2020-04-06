package cl.genesys.appchofer.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import cl.genesys.appchofer.App;
import cl.genesys.appchofer.R;


/**
 * Created by anuj.sharma on 1/12/2017.
 */

public class Utils {

    private static Utils ourInstance = new Utils();
    /*
    *  -----------  Font Style Code
    * */
    private Typeface font_light, font_regular, font_bold, font_medium; // Typeface for set Font Style
    //    ----------- Set Font Cal
//    Calligrapher calligrapher;

    private Utils() {
    }

    public static Utils getInstance() {
        return ourInstance;
    }

    public static void e(Exception msge) {
        Log.i("TAG", msge.getMessage());
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context == null && App.getGlobalContext() != null) {
            context = App.getGlobalContext();
        }
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public final void showToast(String toast) {
        if (toast != null) {
            Toast toast1 = Toast.makeText(App.getGlobalContext(), toast, Toast.LENGTH_LONG);
//            toast1.setGravity(Gravity.CENTER, 0, 0);
            toast1.show();
        }
    }

    public String getMD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    //----------      Clipboard function        ----------
    public void copytoClipboard(String str) {
        try {
            if (str != null) {
                ClipboardManager clipboard = (ClipboardManager) App.getGlobalContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text", str);
                clipboard.setPrimaryClip(clip);
            }
            showToast("Copy to clipboard.");
        } catch (Exception e) {

        }

    }

    // --------- convert dp to pixels  -----
    float converDPtoPx(int dp) {
        Resources r = App.getGlobalContext().getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        r = null;
        return px;

    }

    public void showSnakBarwithOk(View view, String message) {
        if (view != null && message != null) {

            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            snackbar.setActionTextColor(ContextCompat.getColor(App.getGlobalContext(), R.color.colorAccent));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#444444"));
            TextView tv = (TextView) snackBarView.findViewById(R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
//            tv.setTypeface(getfontStyle(Fontstyle.FONT_REGULAR));
//            getInstance().setTextviewTypeface(tv);
            snackbar.show();
        }

    }

    // ------  Boolean for SDK Level check --------------
    public boolean isEqualLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    //------- Close Keyboard --------------
    public void hideSoftKeyboard(Context context, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    COnfirmation Dialog
     */
    public interface ConfirmDialogCallbackInterface {
        void onYesClick(String tag);

        void onNoClick(String tag);
    }

    public void confirmDialog(Context context, String alert_msg, final String tag,
                              final ConfirmDialogCallbackInterface listener) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage(alert_msg);

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    listener.onYesClick(tag);
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onNoClick(tag);
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDeviceId(Context context) {
        try {
            // GET DEVICE ID
            String DEVICEID = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            return DEVICEID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /*
     * Alert Box
     */
    public interface okDialogInterface {
        public void onOkayClicked();
    }

    public static final void showOkDialog(String dlgText, final Context context, final okDialogInterface listener) {
        if (context == null) {
            return;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(dlgText);

        alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                listener.onOkayClicked();
            }
        });
        alertDialogBuilder.show();
    }

    // A method to find height of the status bar
    public int getStatusBarHeight(Context ctx) {
        int result = 0;
        int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ctx.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public boolean isMyServiceRunning(Context ctx, String serviceName) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(ctx.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public String getFormattedTime(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.ENGLISH);
            SimpleDateFormat simpleDateFormatServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            simpleDateFormatServer.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat.format(simpleDateFormatServer.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getYMDFormattedTime(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat simpleDateFormatServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            simpleDateFormatServer.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat.format(simpleDateFormatServer.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public File saveToDirectory(Context ctx, String name, String fileExtension) {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator +
                ctx.getString(R.string.app_name));
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        File file = new File(dir, name + "." + fileExtension);
        if (file.isFile()) file.delete();
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Animate COlor if using Pallet library
     */
    public final static int COLOR_ANIMATION_DURATION = 1000;
    public final static int DEFAULT_DELAY = 0;

    @SuppressLint("NewApi")
    public static void animateViewColor(View v, int startColor, int endColor) {

        ObjectAnimator animator = ObjectAnimator.ofObject(v, "backgroundColor",
                new ArgbEvaluator(), startColor, endColor);

        if (Build.VERSION.SDK_INT >= 21) {
            animator.setInterpolator(new PathInterpolator(0.4f, 0f, 1f, 1f));
        }
        animator.setDuration(COLOR_ANIMATION_DURATION);
        animator.start();
    }

    /*
  Play Notification Sound Vibrate
   */
    private Vibrator vibrator;

    public void generateNotificationSound(Context ctx) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        vibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        vibrator.vibrate(500);
        //play sound
        Ringtone r = RingtoneManager.getRingtone(ctx, alarmSound);
        r.play();
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
