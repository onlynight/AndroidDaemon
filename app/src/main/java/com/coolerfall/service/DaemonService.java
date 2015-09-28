package com.coolerfall.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.coolerfall.daemon.Daemon;
import com.coolerfall.ui.LauncherActivity;

public class DaemonService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Daemon.run(this, DaemonService.class, Daemon.INTERVAL_FIVE_SECONDS, Daemon.INTERVAL_FIVE_SECONDS * 2);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /* do something here */
        Intent tipsIntent = new Intent(this, LauncherActivity.class);
        tipsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(tipsIntent);
        return super.onStartCommand(intent, flags, startId);
    }
}
