package cl.genesys.appchofer.server;

import androidx.annotation.NonNull;

import java.io.File;

import cl.genesys.appchofer.FsSettings;

public class FtpUser {

    final private String mUsername;
    final private String mPassword;
    final private String mChroot;

    public FtpUser(@NonNull String username, @NonNull String password, @NonNull String chroot) {
        mUsername = username;
        mPassword = password;

        final File rootPath = new File(chroot);
        mChroot = rootPath.isDirectory() ? chroot : FsSettings.getDefaultChrootDir().getPath();

    }

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getChroot() {
        return mChroot;
    }
}
