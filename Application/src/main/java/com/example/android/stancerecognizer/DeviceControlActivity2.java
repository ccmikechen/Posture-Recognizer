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

import java.util.List;

import mis.kuas.data.StanceDataGetter;
import mis.kuas.devices.BLEDevice;

public class DeviceControlActivity2 extends Activity {
    private final static String TAG = DeviceControlActivity2.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private TextView mConnectionState;
    private String mDeviceName;
    private String mDeviceAddress;
    private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;

    // Add your components at here

    private StanceDataGetter stanceDataGetter;

    // Add your event listener at here

    Button.OnClickListener startButtonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            stanceDataGetter.startReceiveData();
        }
    };
    Button.OnClickListener stopButtonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            stanceDataGetter.stopReceiveData();
        }
    };
    Button.OnClickListener dataMonitorButtonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DeviceControlActivity2.this, DataMonitorActivity.class);
            startActivity(intent);
        }
    };
    Button.OnClickListener chartButtonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DeviceControlActivity2.this, RecordLayout.class);
            startActivity(intent);
        }
    };
    Button.OnClickListener replayButtonLListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DeviceControlActivity2.this, ReplayActivity.class);
            startActivity(intent);
        }
    };
    Button.OnClickListener cameraButtonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DeviceControlActivity2.this, CameraActivity.class);
            startActivity(intent);
        }
    };
    Button.OnClickListener recordButtonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DeviceControlActivity2.this, RecordingActivity.class);
            startActivity(intent);
        }
    };
    Button.OnClickListener recognizeButtonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DeviceControlActivity2.this, RecognizationActivity.class);
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
                        updateConnectionState(R.string.connected);
                        invalidateOptionsMenu();
                        Log.i(TAG, "Connected to GATT server.");
                        // Attempts to discover services after successful connection.
                        Log.i(TAG, "Attempting to start service discovery:" +
                                gatt.discoverServices());
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        Log.i(TAG, "Disconnected from GATT server.");
                        mConnected = false;
                        updateConnectionState(R.string.disconnected);
                        invalidateOptionsMenu();
                    }
                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    Log.d("SER", mBluetoothLeService.getSupportedGattServices().toString());
                    findCharacteristics(mBluetoothLeService.getSupportedGattServices());
                }
            };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        mConnectionState = (TextView) findViewById(R.id.connection_state);

        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        // Sets up your UI references at here

        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(startButtonListener);

        Button stopButton = (Button) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(stopButtonListener);

        Button dataMonitorButton = (Button) findViewById(R.id.data_monitor_button);
        dataMonitorButton.setOnClickListener(dataMonitorButtonListener);

        Button chartButton = (Button) findViewById(R.id.chart_button);
        chartButton.setOnClickListener(chartButtonListener);

        Button replayButton = (Button) findViewById(R.id.replay_button);
        replayButton.setOnClickListener(replayButtonLListener);

        Button cameraButton = (Button) findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(cameraButtonListener);

        Button recordButton = (Button) findViewById(R.id.record_button);
        recordButton.setOnClickListener(recordButtonListener);

        Button recognizeButton = (Button) findViewById(R.id.recognize_button);
        recognizeButton.setOnClickListener(recognizeButtonListener);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
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
