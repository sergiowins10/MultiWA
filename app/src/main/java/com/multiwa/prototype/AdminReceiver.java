package com.multiwa.prototype;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onProfileProvisioningComplete(Context context, Intent intent) {
        Toast.makeText(context, "MultiWA iş profili hazır.", Toast.LENGTH_LONG).show();
    }
}
