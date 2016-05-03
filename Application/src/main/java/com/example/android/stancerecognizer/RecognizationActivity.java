package com.example.android.stancerecognizer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

import mis.kuas.chart.Timer;
import mis.kuas.data.StanceDataGetter;
import mis.kuas.data.StanceDataPoster;
import mis.kuas.data.UDPDataSender;
import mis.kuas.hmm.*;
import mis.kuas.net.StanceSocketIO;

public class RecognizationActivity extends Activity {

    public static final String STANCE_MODEL_FILE_PATH = "/sdcard/stance/model/chen.mat";
//    stance_list = {'akimbo', 'computer', ...
//        'duck', 'left leg cross', ...
//        'phone when sitting', 'phone when standing', 'play phone when standing', ...
//        'right leg cross', 'sit', 'sleep on table', 'stand', ...
//        'walk left', 'walk right'};
    public static final int[] STANCE_IMAGE = {
        R.drawable.akimbo,
        R.drawable.computer,
        R.drawable.duck,
        R.drawable.left_leg_cross,
        R.drawable.phone_when_sitting,
        R.drawable.phone_when_standing,
        R.drawable.play_phone_when_standing,
        R.drawable.right_leg_cross,
        R.drawable.sit,
        R.drawable.sit,
        R.drawable.stand,
        R.drawable.walk,
        R.drawable.walk
    };

    public static final String[] STANCE_NAME = {
            "插腰",
            "打電腦",
            "蹲",
            "翹左腳",
            "坐著講電話",
            "站著講電話",
            "划手機",
            "翹右腳",
            "坐",
            "趴在桌上",
            "站",
            "走路",
            "走路"
    };

    public static final String serverUrl = "http://140.127.149.152:3000/";

    private ImageView stanceImageView;

    private TextView stanceNameText;

    private StanceRecognizer recognizer;

    private int lastState = -1;

    private BluetoothLeService bleService;

    static StanceDataGetter stanceDataGetter = new StanceDataGetter();

    private boolean isServiceConnected = false;

    private Timer stanceTimer;

    private TextView timerText;

    private StanceSocketIO stanceSocketIO;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bleService = ((BluetoothLeService.LocalBinder) service).getService();
            stanceDataGetter.setBleService(bleService);
            bleService.addBluetoothCallbackListener(stanceDataGetter);
            isServiceConnected = true;

            stanceSocketIO = bleService.connectSocketIO(serverUrl);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bleService.removeBluetoothCallbackListener(stanceDataGetter);
            isServiceConnected = false;
        }
    };

    private Handler stanceTimerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String time = msg.getData().getString("time");
            timerText.setText(time);
        }
    };

    private Timer.OnTickListener stanceTimerListener = new Timer.OnTickListener() {
        @Override
        public void tick(long time) {
            String formatedTime = parseTime(time);
            Bundle bundle = new Bundle();
            bundle.putString("time", formatedTime);
            Message message = new Message();
            message.setData(bundle);
            stanceTimerHandler.sendMessage(message);
        }

        private String parseTime(long time) {
            long milliSecond = (long) (time / 1e6);
            long sc = (long) (milliSecond / 1e3) % 60;
            long mn = (long) (milliSecond / 1e3) / 60;
            return String.format("%02d:%02d", mn, sc);
        }
    };

    private Handler stanceRecognizeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int state = msg.what;
            Drawable image = ContextCompat.getDrawable(RecognizationActivity.this,
                    STANCE_IMAGE[state]);
            stanceImageView.setImageDrawable(image);
            stanceNameText.setText(STANCE_NAME[state]);
            stanceSocketIO.sendStanceResult(state);
        }
    };

    private StanceRecognizer.OnRecognizeListener onRecognizeListener =
            new StanceRecognizer.OnRecognizeListener() {
                @Override
                public void onRecognize(HMMRecognizeResult result) {
                    if (result.isUnknown()) {
                        return;
                    }
                    int state = result.getState();
                    Log.d("Stance", state + "");
                    if (state == 12) {
                        state = 11;
                    }
//                    bleService.sendRecognizationResult(state);

                    if (state == lastState) {
                        return;
                    }
                    lastState = state;
                    stanceRecognizeHandler.sendEmptyMessage(state);
                    stanceTimer.restart();
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognization);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        timerText = (TextView) findViewById(R.id.timer_text);
        stanceTimer = new Timer(1000);
        stanceTimer.setOnTickListener(stanceTimerListener);

        stanceImageView = (ImageView) findViewById(R.id.stanceImageView);
        stanceNameText = (TextView) findViewById(R.id.stance_name_text);
        StanceModel model;

        try {
            model = StanceModelLoader.loadFromMatFile(STANCE_MODEL_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load stance model from " + STANCE_MODEL_FILE_PATH,
                    Toast.LENGTH_LONG).show();
            return;
        }

        recognizer = new StanceRecognizer(model, stanceDataGetter);
        recognizer.addOnRecognizeListener(onRecognizeListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recognization, menu);
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

    public void onDestroy() {
        super.onDestroy();
        recognizer.destroy();
    }
}
