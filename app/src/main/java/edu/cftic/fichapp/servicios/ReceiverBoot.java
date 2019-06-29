package edu.cftic.fichapp.servicios;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class ReceiverBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            GestorAlarma gestorAlarma = new GestorAlarma(context);
            gestorAlarma.programarAlarma();
        }
    }
}
