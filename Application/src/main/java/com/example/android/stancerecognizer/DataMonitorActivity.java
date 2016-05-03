package com.example.android.stancerecognizer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import mis.kuas.data.BandDataGetter;
import mis.kuas.data.BeltDataGetter;
import mis.kuas.data.InsoleDataGetter;
import mis.kuas.data.StanceDataCallbackListener;
import mis.kuas.data.StanceDataGetter;

public class DataMonitorActivity extends Activity {

    private BluetoothLeService bleService;

    private static final int ON_DATA_CALLBACK = 1;

    private StanceDataGetter stanceDataGetter;

    private TextView leftInsoleX;
    private TextView leftInsoleY;
    private TextView leftInsoleZ;
    private TextView leftInsoleA;
    private TextView leftInsoleB;
    private TextView leftInsoleC;
    private TextView leftInsoleD;

    private TextView rightInsoleX;
    private TextView rightInsoleY;
    private TextView rightInsoleZ;
    private TextView rightInsoleA;
    private TextView rightInsoleB;
    private TextView rightInsoleC;
    private TextView rightInsoleD;

    private TextView bandX;
    private TextView bandY;
    private TextView bandZ;

    private TextView beltX;
    private TextView beltY;
    private TextView beltZ;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bleService = ((BluetoothLeService.LocalBinder) service).getService();
            stanceDataGetter = new StanceDataGetter(bleService);
            stanceDataGetter.addDataCallbackListener(stanceDataCallbackListener);
            bleService.addBluetoothCallbackListener(stanceDataGetter);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bleService.removeBluetoothCallbackListener(stanceDataGetter);
        }
    };

    // Listeners

    Handler dataCallbackHandler = new Handler() {
        final String accFormat = "%s : %.2f";
        final String preFormat = "%s : %3d";
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ON_DATA_CALLBACK) {
                InsoleDataGetter leftInsoleData = stanceDataGetter.getLeftInsoleDataGetter();
                leftInsoleX.setText(String.format(accFormat, "X", leftInsoleData.getX()));
                leftInsoleY.setText(String.format(accFormat, "Y", leftInsoleData.getY()));
                leftInsoleZ.setText(String.format(accFormat, "Z", leftInsoleData.getZ()));
                leftInsoleA.setText(String.format(preFormat, "A", leftInsoleData.getA()));
                leftInsoleB.setText(String.format(preFormat, "B", leftInsoleData.getB()));
                leftInsoleC.setText(String.format(preFormat, "C", leftInsoleData.getC()));
                leftInsoleD.setText(String.format(preFormat, "D", leftInsoleData.getD()));
                InsoleDataGetter rightInsoleData = stanceDataGetter.getRightInsoleDataGetter();
                rightInsoleX.setText(String.format(accFormat, "X", rightInsoleData.getX()));
                rightInsoleY.setText(String.format(accFormat, "Y", rightInsoleData.getY()));
                rightInsoleZ.setText(String.format(accFormat, "Z", rightInsoleData.getZ()));
                rightInsoleA.setText(String.format(preFormat, "A", rightInsoleData.getA()));
                rightInsoleB.setText(String.format(preFormat, "B", rightInsoleData.getB()));
                rightInsoleC.setText(String.format(preFormat, "C", rightInsoleData.getC()));
                rightInsoleD.setText(String.format(preFormat, "D", rightInsoleData.getD()));
                BandDataGetter bandData = stanceDataGetter.getBandDataGetter();
                bandX.setText(String.format(accFormat, "X", bandData.getX()));
                bandY.setText(String.format(accFormat, "Y", bandData.getY()));
                bandZ.setText(String.format(accFormat, "Z", bandData.getZ()));
                BeltDataGetter beltData = stanceDataGetter.getBeltDataGetter();
                beltX.setText(String.format(accFormat, "X", beltData.getX()));
                beltY.setText(String.format(accFormat, "Y", beltData.getY()));
                beltZ.setText(String.format(accFormat, "Z", beltData.getZ()));
            }
        }
    };

    StanceDataCallbackListener stanceDataCallbackListener = new StanceDataCallbackListener() {
        @Override
        public void onDataCallback() {
            dataCallbackHandler.sendEmptyMessage(ON_DATA_CALLBACK);
        }
    };

    // End

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_monitor);

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        // Components

        leftInsoleX = (TextView) findViewById(R.id.left_shoe_base_x);
        leftInsoleY = (TextView) findViewById(R.id.left_shoe_base_y);
        leftInsoleZ = (TextView) findViewById(R.id.left_shoe_base_z);
        leftInsoleA = (TextView) findViewById(R.id.left_shoe_base_a);
        leftInsoleB = (TextView) findViewById(R.id.left_shoe_base_b);
        leftInsoleC = (TextView) findViewById(R.id.left_shoe_base_c);
        leftInsoleD = (TextView) findViewById(R.id.left_shoe_base_d);

        rightInsoleX = (TextView) findViewById(R.id.right_shoe_base_x);
        rightInsoleY = (TextView) findViewById(R.id.right_shoe_base_y);
        rightInsoleZ = (TextView) findViewById(R.id.right_shoe_base_z);
        rightInsoleA = (TextView) findViewById(R.id.right_shoe_base_a);
        rightInsoleB = (TextView) findViewById(R.id.right_shoe_base_b);
        rightInsoleC = (TextView) findViewById(R.id.right_shoe_base_c);
        rightInsoleD = (TextView) findViewById(R.id.right_shoe_base_d);

        bandX = (TextView) findViewById(R.id.band_x);
        bandY = (TextView) findViewById(R.id.band_y);
        bandZ = (TextView) findViewById(R.id.band_z);

        beltX = (TextView) findViewById(R.id.belt_x);
        beltY = (TextView) findViewById(R.id.belt_y);
        beltZ = (TextView) findViewById(R.id.belt_z);

        // End
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_monitor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        stanceDataGetter.removeDataCallbackListener(stanceDataCallbackListener);
    }

}
