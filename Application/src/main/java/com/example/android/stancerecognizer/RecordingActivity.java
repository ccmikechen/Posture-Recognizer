package com.example.android.stancerecognizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mis.kuas.data.DataLoggedEvent;
import mis.kuas.data.SensorDataLogger;
import mis.kuas.data.StanceDataGetter;
import mis.kuas.chart.Timer;

public class RecordingActivity extends Activity {

    private TextView timerText;

    private Timer timer;

    private EditText recordNameText;

    private Button recordButton;

    private boolean isRecordingStarted = false;

    private SensorDataLogger logger;

    private BluetoothLeService bleService;

    static StanceDataGetter stanceDataGetter = new StanceDataGetter();

    private boolean isServiceConnected = false;


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bleService = ((BluetoothLeService.LocalBinder) service).getService();
            stanceDataGetter.setBleService(bleService);
            bleService.addBluetoothCallbackListener(stanceDataGetter);
            logger = new SensorDataLogger(stanceDataGetter);
            isServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bleService.removeBluetoothCallbackListener(stanceDataGetter);
            if (logger.isLogging()) {
                logger.stop();
            }
            isServiceConnected = false;
        }
    };

    private Handler timerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String time = msg.getData().getString("time");
            timerText.setText(time);
        }
    };

    private Handler dataLoggerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataLoggedEvent.SUCCESSED:
                    String filePath = msg.getData().getString("filePath");
                    Toast.makeText(RecordingActivity.this,
                            "Successed to logged data to " + filePath,
                            Toast.LENGTH_SHORT).show();
                    break;
                case DataLoggedEvent.FAILED:
                    Toast.makeText(RecordingActivity.this,
                            "Failed to log data",
                            Toast.LENGTH_SHORT).show();
                    break;
                case DataLoggedEvent.DISCONNECTED:
                    recordButton.setEnabled(false);
                    isServiceConnected = false;
                    if (logger.isLogging()) {
                        logger.stop();
                    }
                    Toast.makeText(RecordingActivity.this,
                            "The device has been disconnected",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            timer.stop();
            setRecordingStarted(false);
            recordButton.setEnabled(true);
        }
    };

    private Button.OnClickListener recordButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isRecordingStarted) {
                recordButton.setEnabled(false);
                logger.stop();
                timer.stop();
            } else {
                if (recordNameText.getText().length() == 0) {
                    return;
                }
                setRecordingStarted(true);
                startRecord(recordNameText.getText().toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        timerText = (TextView) findViewById(R.id.timerText);
        timer = new Timer(10);
        timer.setOnTickListener(new Timer.OnTickListener() {
            @Override
            public void tick(long time) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("time", parseTime(time));
                message.setData(bundle);
                timerHandler.sendMessage(message);
            }

            private String parseTime(long time) {
                long milliSecond = (long) (time / 1e6);
                long mm = (long) (milliSecond / 1e1) % 100;
                long sc = (long) (milliSecond / 1e3) % 60;
                long mn = (long) (milliSecond / 1e3) / 60;
                return String.format("%02d:%02d:%02d", mn, sc, mm);
            }
        });

        recordNameText = (EditText) findViewById(R.id.record_name_text);

        recordButton = (Button) findViewById(R.id.record_button);
        recordButton.setOnClickListener(recordButtonListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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

    private void startRecord(String filename) {

        File file = new File("/sdcard/stance/" + getCurrentDateTime() + "_" + filename + ".csv");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        logger.start(file,
                new DataLoggedEvent() {
                    @Override
                    public void onDataLogged(int status, String filePath) {
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("filePath", filePath);
                        message.setData(bundle);
                        message.what = status;
                        dataLoggerHandler.sendMessage(message);

                    }
                });
        timer.start();
    }

    private void setRecordingStarted(boolean started) {
        if (started) {
            recordButton.setText("STOP");
        } else {
            recordButton.setText("START");
        }
        isRecordingStarted = started;
    }

    private String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public void onDestroy() {
        super.onDestroy();
        if (logger.isLogging()) {
            logger.stop();
        }
    }
}
