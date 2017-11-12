package com.example.administrator.mybletest1;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class BLEService extends Service {
    private final LocalBinder mBinder = new LocalBinder();
    private BluetoothDevice device;
    private BluetoothGatt gatt;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }
    };

    public class LocalBinder extends Binder {
        BLEService getService(){return BLEService.this;}
    }

    @Override
    public IBinder onBind(Intent intent) {
        device = (BluetoothDevice) intent.getParcelableExtra("device");

        //
        gatt = device.connectGatt(this, false, mGattCallback);
        gatt.connect();


        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        gatt.disconnect();
        gatt.close();
    }
}
