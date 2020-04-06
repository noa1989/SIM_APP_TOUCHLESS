/*
Copyright 2016-2017 Pieter Pareit

This file is part of SwiFTP.

SwiFTP is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SwiFTP is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */
package cl.genesys.appchofer.locale;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.twofortyfouram.locale.sdk.client.receiver.AbstractPluginSettingReceiver;

import cl.genesys.appchofer.FsService;

import static cl.genesys.appchofer.locale.SettingsBundleHelper.getBundleRunningState;

/**
 * Created by ppareit on 29/04/16.
 */
public class FireReceiver extends AbstractPluginSettingReceiver {
    @Override
    protected boolean isBundleValid(@NonNull Bundle bundle) {
        return SettingsBundleHelper.isBundleValid(bundle);
    }

    @Override
    protected boolean isAsync() {
        return false;
    }

    @Override
    protected void firePluginSetting(@NonNull Context context, @NonNull Bundle bundle) {
        boolean running = getBundleRunningState(bundle);
        if (running && !FsService.isRunning()) {
            FsService.start();
        } else if (!running && FsService.isRunning()) {
            FsService.stop();
        }
    }
}
