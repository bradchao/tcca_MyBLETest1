package com.example.administrator.mybletest1;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class BLEService extends Service {
    private final LocalBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        BLEService getService(){return BLEService.this;}
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
