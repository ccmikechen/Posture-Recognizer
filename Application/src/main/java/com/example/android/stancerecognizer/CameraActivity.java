package com.example.android.stancerecognizer;

import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;
public class CameraActivity extends Activity implements SurfaceHolder.Callback{

    private Camera mCamera;

    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    public MediaRecorder mrec = new MediaRecorder();


    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mCamera = Camera.open();
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        menu.add("Start recording");
        menu.add("Stop recording");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getOrder()) {
            case 0:
                try {
                    startRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                stopRecording();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startRecording() throws IOException {
        mrec = new MediaRecorder();  // Works well
        mCamera.lock();
        mCamera.unlock();

        mrec.setCamera(mCamera);

        mrec.setPreviewDisplay(surfaceHolder.getSurface());
        mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mrec.setAudioSource(MediaRecorder.AudioSource.MIC);

        mrec.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mrec.setPreviewDisplay(surfaceHolder.getSurface());
        mrec.setOutputFile("/sdcard/zzzz.3gp");

        mrec.prepare();
        mrec.start();
        Toast.makeText(this, "Start recording", Toast.LENGTH_SHORT).show();
    }

    public void stopRecording() {
        mrec.stop();
        mrec.release();
        mrec = null;
        Toast.makeText(this, "Stop recording", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera != null){
            Camera.Parameters params = mCamera.getParameters();
            mCamera.setParameters(params);
        }
        else {
            Toast.makeText(getApplicationContext(), "Camera not available!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
    }

}
