/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.stancerecognizer;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mis.kuas.devices.BLEDevice;
import mis.kuas.data.StanceDataGetter;
import mis.kuas.net.StanceSocketIO;

public class DeviceControlActivity extends Activity {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;



    private boolean mConnected = false;
    // Add your components at here

    private StanceDataGetter stanceDataGetter;

    // Add your event listener at here

    Button.OnClickListener chartButtonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DeviceControlActivity.this, RecordLayout.class);
            startActivity(intent);
        }
    };
    Button.OnClickListener replayButtonLListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DeviceControlActivity.this, ReplayActivity.class);
            startActivity(intent);
        }
    };
    Button.OnClickListener recordButtonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DeviceControlActivity.this, RecordingActivity.class);
            startActivity(intent);
        }
    };
    Button.OnClickListener recognizeButtonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DeviceControlActivity.this, RecognizationActivity.class);
            startActivity(intent);
        }
    };
    Button.OnClickListener insoleChartButtonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DeviceControlActivity.this, InsoleChartActivity.class);
            startActivity(intent);
        }
    };

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            mBluetoothLeService.connect(mDeviceAddress);
            mBluetoothLeService.addBluetoothCallbackListener(connectionCallbackListener);
            Log.d("SER", mBluetoothLeService.getSupportedGattServices().toString());

            // Automatically connects to the device upon successful start-up initialization.
            stanceDataGetter = new StanceDataGetter(mBluetoothLeService);
            Toast.makeText(DeviceControlActivity.this, "Connected to device", Toast.LENGTH_SHORT).show();


        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;

        }
    };


    private final BluetoothGattCallback connectionCallbackListener =
            new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                    super.onConnectionStateChange(gatt, status, newState);
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        mConnected = true;
                        invalidateOptionsMenu();
                        Log.i(TAG, "Connected to GATT server.");
                        // Attempts to discover services after successful connection.
                        Log.i(TAG, "Attempting to start service discovery:" +
                                gatt.discoverServices());
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        Log.i(TAG, "Disconnected from GATT server.");
                        mConnected = false;
                        invalidateOptionsMenu();
                    }
                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    Log.d("SER", mBluetoothLeService.getSupportedGattServices().toString());
                    findCharacteristics(mBluetoothLeService.getSupportedGattServices());
                    stanceDataGetter.startReceiveData();
                }
            };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        // Sets up your UI references at here

        Button chartButton = (Button) findViewById(R.id.chart_button);
        chartButton.setOnClickListener(chartButtonListener);

        Button replayButton = (Button) findViewById(R.id.replay_button);
        replayButton.setOnClickListener(replayButtonLListener);

        Button recordButton = (Button) findViewById(R.id.record_button);
        recordButton.setOnClickListener(recordButtonListener);

        Button recognizeButton = (Button) findViewById(R.id.recognize_button);
        recognizeButton.setOnClickListener(recognizeButtonListener);

        Button insoleChartButton = (Button) findViewById(R.id.insole_chart_button);
        insoleChartButton.setOnClickListener(insoleChartButtonListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }


    private void findCharacteristics(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        for (BluetoothGattService gattService : gattServices) {
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                String uuid = gattCharacteristic.getUuid().toString();
                BLEDevice.addCharacteristic(uuid, gattCharacteristic);
            }

        }

    }

}
