package mis.kuas.mqtt;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import mis.kuas.cnn.StanceRecognizer;

/**
 * Created by mingjia on 2016/10/6.
 */
public class MqttServerConnector {

    public static final String TAG = MqttServerConnector.class.getSimpleName();

    private static final String DEFAULT_SERVER_NAME = "default";

    private String serverName = DEFAULT_SERVER_NAME;

    protected MqttAndroidClient client;

    private IMqttActionListener mqttActionListener = null;

    public MqttServerConnector(String url, Context context) {
        String clientId = MqttClient.generateClientId();
        this.client = new MqttAndroidClient(context.getApplicationContext(), url, clientId);
    }

    public void connect() {
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.e("TAG", "onSuccess");
                    if (mqttActionListener != null) {
                        mqttActionListener.onSuccess(asyncActionToken);
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.e("TAG", "onFailure");
                    if (mqttActionListener != null) {
                        mqttActionListener.onFailure(asyncActionToken, exception);
                    }
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void disconnect() {
        try {
            this.client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void setConnectCallback(IMqttActionListener listener) {
        this.mqttActionListener = listener;
    }

    public void sendData(String topic, String payload) {
        MqttMessage message = new MqttMessage(payload.getBytes());
        try {
            this.client.publish(this.serverName + "/" + topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void setServerName(String name) {
        if (name == null || name.equals("")) {
            this.serverName = DEFAULT_SERVER_NAME;
            return;
        }
        this.serverName = name;
    }

}
