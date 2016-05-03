package com.example.android.stancerecognizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.List;

import mis.kuas.chart.AccChart;
import mis.kuas.data.SensorDatabase;
import mis.kuas.data.SensorRecordInfo;

public class ReplayActivity extends Activity {

    private AccChart leftInsoleAccChart;

    private AccChart rightInsoleAccChart;

    private AccChart beltAccChart;

    private AccChart bandAccChart;

    private SensorDatabase database;

    private AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        leftInsoleAccChart = new AccChart(this, "Left Insole");
        rightInsoleAccChart = new AccChart(this, "Right Insole");
        beltAccChart = new AccChart(this, "Belt");
        bandAccChart = new AccChart(this, "Band");

        ((FrameLayout) findViewById(R.id.left_insole_acc_chart_layout)).addView(
                leftInsoleAccChart.getChartView());
        ((FrameLayout) findViewById(R.id.right_insole_acc_chart_layout)).addView(
                rightInsoleAccChart.getChartView());
        ((FrameLayout) findViewById(R.id.belt_acc_chart_layout)).addView(
                beltAccChart.getChartView());
        ((FrameLayout) findViewById(R.id.band_acc_chart_layout)).addView(
                bandAccChart.getChartView());

        database = new SensorDatabase(this);

        final List<SensorRecordInfo> recordInfoList = database.getRecordInfoList();
        String[] startTimeArray = new String[recordInfoList.size()];
        for (int i = 0; i < recordInfoList.size(); i++) {
            startTimeArray[i] = recordInfoList.get(i).getStartTime();
        }
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Select a record");
        dialogBuilder.setItems(startTimeArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SensorRecordInfo recordInfo = recordInfoList.get(which);
                int recordId = recordInfo.getRecordId();
                leftInsoleAccChart.setDataList(
                        database.getAccDataById(recordId, SensorDatabase.DATA_LEFT_INSOLE_ACC));
                rightInsoleAccChart.setDataList(
                        database.getAccDataById(recordId, SensorDatabase.DATA_RIGHT_INSOLE_ACC));
                beltAccChart.setDataList(
                        database.getAccDataById(recordId, SensorDatabase.DATA_BELT_ACC));
                bandAccChart.setDataList(
                        database.getAccDataById(recordId, SensorDatabase.DATA_BAND_ACC));
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_replay, menu);
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
        if (id == R.id.selectRecord) {
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

}
