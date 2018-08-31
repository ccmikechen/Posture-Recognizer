package mis.kuas.cnn;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import mis.kuas.mqtt.MqttServerConnector;

/**
 * Created by kuasmis on 16/9/21.
 */
public class CNNServerConnector extends MqttServerConnector {

    public static final String CNN_SERVER_NAME = "CNNServer";

    MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {

        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String resultCode = new String(message.getPayload());
            StanceType resultType = StanceTypes.getStanceTypeByCode(resultCode);
            onRecognizeListener.onRecognize(resultType);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };

    private StanceRecognizer.OnRecognizeListener onRecognizeListener = null;

    public CNNServerConnector(String url, Context context) {
        super(url, context);
        this.setServerName(CNN_SERVER_NAME);
    }

    public void connect() {
        this.setConnectCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.e(TAG, "Connected to MQTT Server");
                try {
                    client.setCallback(mqttCallback);
                    client.subscribe("client/stance", 1);
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.e(TAG, "Failed to connect to MQTT Server");
            }
        });
        super.connect();
    }

    public void sendSensorData(double[][] data) {
        String payload = parseFormattedString(data);
        this.sendData("rawdata", payload);
    }

    private String parseFormattedString(double[][] data) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                sb.append(data[i][j]);
                if (j < data[i].length - 1) {
                    sb.append(",");
                }
            }
            if (i < data.length - 1) {
                sb.append(";");
            }
        }
        return new String(sb);
    }

    public void setOnRecognizeListener(StanceRecognizer.OnRecognizeListener listener) {
        this.onRecognizeListener = listener;
    }
}
