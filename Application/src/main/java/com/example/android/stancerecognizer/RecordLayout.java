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
import android.widget.FrameLayout;

import mis.kuas.chart.AccRealTimeChart;
import mis.kuas.chart.ChartTimer;
import mis.kuas.data.StanceDataGetter;

public class RecordLayout extends Activity {

    private AccRealTimeChart leftInsoleAccChart;

    private AccRealTimeChart rightInsoleAccChart;

    private AccRealTimeChart beltAccChart;

    private AccRealTimeChart bandAccChart;

    private FrameLayout leftInsoleChartLayout;

    private FrameLayout rightInsoleChartLayout;

    private FrameLayout beltChartLayout;

    private FrameLayout bandChartLayout;

    private Handler timerHandler;

    private ChartTimer chartTimer;

    private BluetoothLeService bleService;

    private MenuItem startItem;

    private MenuItem stopItem;

    static StanceDataGetter stanceDataGetter = new StanceDataGetter();

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bleService = ((BluetoothLeService.LocalBinder) service).getService();
            stanceDataGetter.setBleService(bleService);
            bleService.addBluetoothCallbackListener(stanceDataGetter);
            leftInsoleAccChart = new AccRealTimeChart(RecordLayout.this,
                    stanceDataGetter.getLeftInsoleDataGetter(), "Left Insole");
            rightInsoleAccChart = new AccRealTimeChart(RecordLayout.this,
                    stanceDataGetter.getRightInsoleDataGetter(), "Right Insole");
            beltAccChart = new AccRealTimeChart(RecordLayout.this,
                    stanceDataGetter.getBeltDataGetter(), "Belt");
            bandAccChart = new AccRealTimeChart(RecordLayout.this,
                    stanceDataGetter.getBandDataGetter(), "Band");
            leftInsoleChartLayout.addView(leftInsoleAccChart.getChartView());
            rightInsoleChartLayout.addView(rightInsoleAccChart.getChartView());
            beltChartLayout.addView(beltAccChart.getChartView());
            bandChartLayout.addView(bandAccChart.getChartView());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bleService.removeBluetoothCallbackListener(stanceDataGetter);
        }
    };

    private Handler chartUpdateTimerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            leftInsoleAccChart.update();
            rightInsoleAccChart.update();
            beltAccChart.update();
            bandAccChart.update();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_layout);

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        leftInsoleChartLayout = (FrameLayout) findViewById(R.id.left_insole_chart);
        rightInsoleChartLayout = (FrameLayout) findViewById(R.id.right_insole_chart);
        beltChartLayout = (FrameLayout) findViewById(R.id.belt_chart);
        bandChartLayout = (FrameLayout) findViewById(R.id.band_chart);

        chartTimer = new ChartTimer(chartUpdateTimerHandler, 100);
        chartTimer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chartTimer != null) {
            chartTimer.stop();
        }
    }

}
