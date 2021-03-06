package com.example.android.ble_scanner.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.example.android.ble_scanner.Utils;

public final class Scanner_BLE {

    private FragmentActivity ma;

    private ScannerFragment sf;

    private DetailActivity da;

    private Handler mHandler;

    private BluetoothAdapter mBluetoothAdapter;

    private boolean mScanning;

    private long scanPeriod;

    private int signalStrength;

    public Scanner_BLE(ScannerFragment sf, long scanPeriod, int signalStrength){

       this.ma = sf.getActivity();
       this.sf = sf;
       this.scanPeriod = scanPeriod;
       this.signalStrength = signalStrength;

        final BluetoothManager bluetoothManager =
                (BluetoothManager)ma.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mHandler = new Handler();
    }

    public boolean checkBluetooth(){
        return Utils.checkBluetooth(mBluetoothAdapter);
    }

    public void start(){
        if(!Utils.checkBluetooth(mBluetoothAdapter)){
            // Allow user enable bluetooth feature
            Utils.requestUserBluetooth(ma);
            sf.stopScan();
        }else{

            if(!mScanning){
                // Start scan for one period.
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mScanning == true){
                            // Stop scanning after a given scan period
                            mScanning = false;
                            mBluetoothAdapter.stopLeScan(leScanCallback);
                            Utils.showToast(ma.getApplicationContext(),"Time out");
                            sf.stopScan();
                        }

                    }
                },scanPeriod);

                mScanning = true;
                mBluetoothAdapter.startLeScan(leScanCallback);
            }

        }
    }

    public void stop(){
        mScanning = false;
        mBluetoothAdapter.stopLeScan(leScanCallback);
    }

    public void scanLeDevice(boolean enable){
        // If it haven started yet, start scan
        if(enable && !mScanning){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Stop scanning after a given scan period
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(leScanCallback);
                    Utils.showToast(ma.getApplicationContext(),"Time out");
                    sf.stopScan();
                }
            },scanPeriod);

            mScanning = true;
            mBluetoothAdapter.startLeScan(leScanCallback);
            Utils.showToast(ma.getApplicationContext(),"Start scan BLE devices");
        }

        if(!enable){
            mScanning = false;
            mBluetoothAdapter.stopLeScan(leScanCallback);
            Utils.showToast(ma.getApplicationContext(),"Stop scan BLE devices");
        }


    }

    public boolean isScanning() {
        return mScanning;
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            // Called when system find a device
            final int new_rssi = rssi;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(new_rssi > signalStrength){
                       sf.addDevice(device,new_rssi);
                    }

                }
            });
        }
    };

}
