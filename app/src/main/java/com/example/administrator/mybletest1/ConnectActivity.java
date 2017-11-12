package com.example.administrator.mybletest1;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ConnectActivity extends AppCompatActivity {
    private BluetoothDevice device;
    private BLEService mService;
    private boolean isConnect;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BLEService.LocalBinder binder =
                    (BLEService.LocalBinder)iBinder;
            mService = binder.getService();
            isConnect = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isConnect = false;
        }
    } ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        device = (BluetoothDevice) getIntent().getParcelableExtra("device");



    }


    @Override
    protected void onStart() {
        super.onStart();

        Intent it = new Intent(this, BLEService.class);
        it.putExtra("device", device);
        bindService(it, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isConnect){
            unbindService(mConnection);
        }

    }
}
