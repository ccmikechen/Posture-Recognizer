package com.example.android.stancerecognizer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import mis.kuas.camera.Camera2BasicFragment;
import mis.kuas.camera.OnCapturedListener;
import mis.kuas.data.AccDataGetter;
import mis.kuas.data.StanceDataCallbackListener;
import mis.kuas.data.StanceDataGetter;

import java.io.File;

public class JumpShotActivity extends Activity implements OnCapturedListener, StanceDataCallbackListener {

    public static final String TAG = JumpShotActivity.class.getSimpleName();

    private BluetoothLeService bleService;

    static StanceDataGetter stanceDataGetter = new StanceDataGetter();

    private Camera2BasicFragment camera;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bleService = ((BluetoothLeService.LocalBinder) service).getService();
            stanceDataGetter.setBleService(bleService);
            bleService.addBluetoothCallbackListener(stanceDataGetter);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bleService.removeBluetoothCallbackListener(stanceDataGetter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump_catch);

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        camera = Camera2BasicFragment.newInstance();
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.camera_container, camera)
                    .commit();
        }
        this.stanceDataGetter.addDataCallbackListener(this);
        camera.setOnCapturedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jump_catch, menu);
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
    public void onCaptured(File file) {
        Log.d("PIC", file.getAbsolutePath());
        stanceDataGetter.removeDataCallbackListener(this);
        String path = file.getAbsolutePath();
        Intent intent = new Intent(this, PictureViewActivity.class);
        intent.putExtra("FILE", path);
        startActivity(intent);
    }

    @Override
    public void onDataCallback() {
        AccDataGetter dataGetter = stanceDataGetter.getRightInsoleDataGetter();

        if (dataGetter.getY() > 2.0) {
            Log.d("JUMP", "Jumped!");
            camera.takePicture();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        this.stanceDataGetter.removeDataCallbackListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.stanceDataGetter.addDataCallbackListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stanceDataGetter.removeDataCallbackListener(this);
    }
}
