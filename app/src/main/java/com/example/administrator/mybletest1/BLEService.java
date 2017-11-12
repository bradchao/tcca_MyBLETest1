package com.example.administrator.mybletest1;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class BLEService extends Service {
    private final LocalBinder mBinder = new LocalBinder();
    private BluetoothDevice device;

    public class LocalBinder extends Binder {
        BLEService getService(){return BLEService.this;}
    }

    @Override
    public IBinder onBind(Intent intent) {
        device = (BluetoothDevice) intent.getParcelableExtra("device");

        // 


        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
