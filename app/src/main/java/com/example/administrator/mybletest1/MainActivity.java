package com.example.administrator.mybletest1;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 123;
    private boolean isScanning;
    private Handler mHandler;

    private ListView listDevices;
    private SimpleAdapter adapter;
    private String[] from = {"name","mac","bond","type"};
    private int[] to = {R.id.device_name, R.id.device_mac,
    R.id.device_bond, R.id.device_type};
    private LinkedList<HashMap<String,String>> dataDevices;
    private LinkedList<BluetoothDevice> allDevices;
    private HashSet<BluetoothDevice> deviceSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);
        }else{
            init();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        }
    }

    private void init(){
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        listDevices = (ListView)findViewById(R.id.listDevices);
        initListView();

        deviceSet = new HashSet<>();
        mHandler = new Handler();
        scanLeDevice(true);

    }

    private void initListView(){
        allDevices = new LinkedList<>();
        dataDevices = new LinkedList<>();
        adapter = new SimpleAdapter(this, dataDevices, R.layout.item_device,from, to);
        listDevices.setAdapter(adapter);

        listDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(MainActivity.this, ConnectActivity.class);
                it.putExtra("device", allDevices.get(i));
                startActivity(it);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK){

        }
    }

    public void scanDevices(View view){
        scanLeDevice(true);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, 3000);

            isScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            isScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }



    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    if (deviceSet.add(device)) {
                        String mac = device.getAddress();
                        String name = device.getName();
                        String state = "";
                        switch(device.getBondState()){
                            case BluetoothDevice.BOND_BONDED:
                                state = "bonded"; break;
                            case BluetoothDevice.BOND_BONDING:
                                state = "bonding"; break;
                            case BluetoothDevice.BOND_NONE:
                                state = "none"; break;
                        }
                        String type= "";
                        switch(device.getType()){
                            case BluetoothDevice.DEVICE_TYPE_LE:
                                type="LE"; break;
                            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                                type="classic"; break;
                            case BluetoothDevice.DEVICE_TYPE_DUAL:
                                type="dual"; break;
                            default:
                                type="xxx"; break;
                        }

                        HashMap<String,String> data = new HashMap<>();
                        data.put(from[0], name);
                        data.put(from[1], mac);
                        data.put(from[2], state);
                        data.put(from[3], type);

                        dataDevices.add(data);
                        allDevices.add(device);
                        adapter.notifyDataSetChanged();
                    }
                }
            };

    public void stopScan(View view) {
        scanLeDevice(false);

    }
}


