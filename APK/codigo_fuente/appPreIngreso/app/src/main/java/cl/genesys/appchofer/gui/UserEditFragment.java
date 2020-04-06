package cl.genesys.appchofer.gui;


import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cl.genesys.appchofer.FsSettings;
import cl.genesys.appchofer.R;
import cl.genesys.appchofer.server.FtpUser;

public class UserEditFragment extends Fragment {

    private FtpUser item;
    private OnEditFinishedListener editFinishedListener;
    private boolean isShowingFolderPicker = false;

    public static UserEditFragment newInstance(@Nullable FtpUser item, @NonNull OnEditFinishedListener listener) {
        UserEditFragment fragment = new UserEditFragment();
        fragment.editFinishedListener = listener;
        if (item != null) {
            fragment.item = item;
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_edit_layout, container, false);
        EditText username = (EditText) root.findViewById(R.id.user_edit_name);
        EditText password = (EditText) root.findViewById(R.id.user_edit_password);
        TextView chroot = (TextView) root.findViewById(R.id.user_edit_chroot);
        chroot.setText(FsSettings.getDefaultChrootDir().getPath());
        chroot.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                return;
            showFolderPicker(chroot);
        });
        chroot.setOnClickListener(view -> showFolderPicker(chroot));

        if (item != null) {
            username.setText(item.getUsername());
            password.setText(item.getPassword());
            chroot.setText(item.getChroot());
        }

        root.findViewById(R.id.user_save_btn).setOnClickListener((buttonView) -> {
            String newUsername = username.getText().toString();
            String newPassword = password.getText().toString();
            String newChroot = chroot.getText().toString();
            if (validateInput(newUsername, newPassword)) {
                editFinishedListener.onEditActionFinished(item, new FtpUser(newUsername, newPassword, newChroot));
                getActivity().onBackPressed();
            }
        });
        root.findViewById(R.id.user_cancel_btn).setOnClickListener((buttonView) -> getActivity().onBackPressed());
        return root;
    }

    private void showFolderPicker(TextView chrootView) {
        if (isShowingFolderPicker)
            return;
        isShowingFolderPicker = true;
        final File startDir;
        if (chrootView.getText().toString().isEmpty()) {
            startDir = Environment.getExternalStorageDirectory();
        } else {
            startDir = new File((chrootView.getText().toString()));
        }
        AlertDialog folderPicker = new FolderPickerDialogBuilder(getActivity(), startDir)
                .setSelectedButton(R.string.select, path -> {
                    final File root = new File(path);
                    if (!root.canRead()) {
                        showToast(R.string.notice_cant_read_write);
                    } else if (!root.canWrite()) {
                        showToast(R.string.notice_cant_write);
                    }
                    chrootView.setText(path);
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        folderPicker.setOnDismissListener(dialog -> isShowingFolderPicker = false);
        folderPicker.show();
    }

    private boolean validateInput(String username, String password) {
        if (!username.matches("[a-zA-Z0-9]+")) {
            showToast(R.string.username_validation_error);
            return false;
        }
        if (!password.matches("[a-zA-Z0-9]+")) {
            showToast(R.string.password_validation_error);
            return false;
        }
        return true;
    }

    private void showToast(int errorResId) {
        Toast.makeText(getActivity(), errorResId, Toast.LENGTH_LONG).show();
    }

    interface OnEditFinishedListener {
        void onEditActionFinished(FtpUser oldItem, FtpUser newItem);
    }
}
