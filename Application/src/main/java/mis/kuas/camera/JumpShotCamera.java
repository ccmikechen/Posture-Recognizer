package mis.kuas.camera;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Arrays;
import java.util.concurrent.Semaphore;


/**
 * Created by kuasmis on 16/10/13.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JumpShotCamera {

    private static final int STATE_PREVIEW = 0;
    private static final int STATE_WAITING_CAPTURE = 1;

    private int state;

    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Handler handler;
    private String cameraId;
    private ImageReader imageReader;
    private CaptureRequest.Builder previewBuilder;
    private CameraCaptureSession session;

    private Semaphore cameraOpenAndCloseLock = new Semaphore(1);

    public JumpShotCamera(Context context, SurfaceView surfaceView) {
        this.cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        this.surfaceView = surfaceView;
        this.surfaceHolder = surfaceView.getHolder();
        this.surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                initCameraAndPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
    }

    private void initCameraAndPreview() {
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        try {
            this.cameraId = CameraCharacteristics.LENS_FACING_FRONT + "";
            this.imageReader = ImageReader.newInstance(
                    this.surfaceView.getWidth(),
                    this.surfaceView.getHeight(),
                    ImageFormat.JPEG,
                    7);
            this.imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader imageReader) {

                }
            }, this.handler);
            cameraManager.openCamera(this.cameraId, deviceStateCallback, this.handler);
        } catch (CameraAccessException e) {

        }
    }

    private CameraDevice.StateCallback deviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice _cameraDevice) {
            cameraDevice = _cameraDevice;
            cameraOpenAndCloseLock.release();
            try {
                createCameraCaptureSession();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {

        }

        @Override
        public void onError(CameraDevice cameraDevice, int i) {

        }
    };

    private void createCameraCaptureSession() throws CameraAccessException {
        this.previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        this.previewBuilder.addTarget(this.surfaceHolder.getSurface());
        this.state = STATE_PREVIEW;
        this.cameraDevice.createCaptureSession(
                Arrays.asList(this.surfaceHolder.getSurface(), this.imageReader.getSurface()),
                sessionPreviewStateCallback, this.handler);
    }

    private CameraCaptureSession.StateCallback sessionPreviewStateCallback =
            new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
            session = cameraCaptureSession;
            try {
                previewBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                previewBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                session.setRepeatingRequest(previewBuilder.build(), sessionCaptureCallback, handler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Log.e("linc", "set preview builder failed." + e.getMessage());
            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

        }
    };

    private CameraCaptureSession.CaptureCallback sessionCaptureCallback =
            new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(CameraCaptureSession cameraCaptureSession,
                                               CaptureRequest request,
                                               TotalCaptureResult result) {
                    //            Log.d("linc","mSessionCaptureCallback, onCaptureCompleted");
                    session = cameraCaptureSession;
                    checkState(result);
                }

                @Override
                public void onCaptureProgressed(CameraCaptureSession cameraCaptureSession,
                                                CaptureRequest request,
                                                CaptureResult partialResult) {
                    Log.d("linc","mSessionCaptureCallback,  onCaptureProgressed");
                    session = cameraCaptureSession;
                    checkState(partialResult);
                }

                private void checkState(CaptureResult result) {
                    switch (state) {
                        case STATE_PREVIEW:
                            // NOTHING
                            break;
                        case STATE_WAITING_CAPTURE:
                            int afState = result.get(CaptureResult.CONTROL_AF_STATE);

                            if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState
                                    || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState
                                    || CaptureResult.CONTROL_AF_STATE_PASSIVE_FOCUSED == afState
                                    || CaptureResult.CONTROL_AF_STATE_PASSIVE_UNFOCUSED == afState) {
                                //do something like save picture
                            }
                            break;
                    }
                }

            };
    public void onCapture(View view) {
        try {
            Log.i("linc", "take picture");
            this.state = STATE_WAITING_CAPTURE;
            this.session.setRepeatingRequest(this.previewBuilder.build(),
                    this.sessionCaptureCallback,
                    this.handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
