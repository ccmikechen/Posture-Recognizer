package mis.kuas.net;

import android.util.Log;

import com.example.android.stancerecognizer.BluetoothLeService;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class StanceSocketIO {


    public static final String TAG = StanceSocketIO.class.getSimpleName();

    private BluetoothLeService bleService;

    private Socket mSocket;

    private boolean reConnect;

    private String url;

    public StanceSocketIO(String url, BluetoothLeService service) {
        this.url = url;
        this.bleService = service;
    }

    public boolean connect() {
        try {
            mSocket = IO.socket(url);
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeout);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.connect();
            reConnect = true;
            return true;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disconnect() {
        Log.d("setConnect", "Socket Disconnect");
        reConnect = false;
        if(mSocket == null)
            return;

        if(mSocket.connected()) {
            mSocket.disconnect();
        }
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket = null;
    }

    public boolean isConnected() {
        return this.reConnect;
    }

    public void setEmit(String event, String msg) {
        if(mSocket == null)
            return;

        if(mSocket.connected()) {
            mSocket.emit(event, msg);
        }
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "MSG_SOCKET_CONNECT");
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "onConnectError");
        }
    };

    private Emitter.Listener onConnectTimeout = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "onConnectTimeout");
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "onDisconnect");
        }
    };



    public void sendStanceResult(int result) {
        setEmit("stanceResult", result + "");
    }

}
