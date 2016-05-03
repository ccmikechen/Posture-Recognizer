package com.example.android.stancerecognizer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import mis.kuas.chart.FootPressureView;
import mis.kuas.data.NikeDataGetter;
import mis.kuas.data.StanceDataGetter;
import mis.kuas.data.StanceNikeDataGetter;
import mis.kuas.sensor.Direction;


public class InsoleChartActivity extends Activity {

    private BluetoothLeService bleService;

    static StanceDataGetter stanceDataGetter = new StanceDataGetter();

    private NikeDataGetter leftNikeDataGetter;

    private NikeDataGetter rightNikeDataGetter;

    private boolean isServiceConnected = false;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bleService = ((BluetoothLeService.LocalBinder) service).getService();
            stanceDataGetter.setBleService(bleService);
            bleService.addBluetoothCallbackListener(stanceDataGetter);
            isServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bleService.removeBluetoothCallbackListener(stanceDataGetter);
            isServiceConnected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insole_chart);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        LinearLayout layout = (LinearLayout) findViewById(R.id.sensor_layout);

        leftNikeDataGetter = new StanceNikeDataGetter(stanceDataGetter, Direction.LEFT);
        FootPressureView leftView = new FootPressureView(this, leftNikeDataGetter);

        rightNikeDataGetter = new StanceNikeDataGetter(stanceDataGetter, Direction.RIGHT);
        FootPressureView rightView = new FootPressureView(this, rightNikeDataGetter);

        leftView.setLayoutParams(
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        rightView.setLayoutParams(
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        layout.addView(leftView);
        layout.addView(rightView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        leftNikeDataGetter.close();
        rightNikeDataGetter.close();
    }
}
