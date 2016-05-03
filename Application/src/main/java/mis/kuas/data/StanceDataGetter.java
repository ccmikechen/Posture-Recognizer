package mis.kuas.data;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.example.android.stancerecognizer.BluetoothLeService;

import java.util.ArrayList;
import java.util.List;

import mis.kuas.ble.GattCommunicationBuilder;
import mis.kuas.devices.Stance;

public class StanceDataGetter extends BluetoothGattCallback {

    private final static String TAG = StanceDataGetter.class.getSimpleName();

    private BluetoothLeService bleService;

    private InsoleDataGetter leftShoeBaseData = new InsoleDataGetter();

    private InsoleDataGetter rightShoeBaseData = new InsoleDataGetter();

    private BandDataGetter bandDataGetter = new BandDataGetter();

    private BeltDataGetter beltDataGetter = new BeltDataGetter();

    private boolean isBandAndBeltDataReceived = false;

    private boolean isShoeBaseDataReceived = false;

    private List<StanceDataCallbackListener> dataCallbackListenerList =
            new ArrayList<StanceDataCallbackListener>();

    public StanceDataGetter() {
    }

    public StanceDataGetter(BluetoothLeService bleService) {
        this.bleService = bleService;
    }

    public void setBleService(BluetoothLeService bleService) {
        this.bleService = bleService;
    }

    public void startReceiveData() {
        if (bleService == null) {
            return;
        }
        GattCommunicationBuilder builder = new GattCommunicationBuilder(bleService);
        builder.notification(Stance.getCharacteristic(Stance.UUID_CHARACTERISTIC_RX), true);
        builder.build().start();
    }

    public void stopReceiveData() {
        if (bleService == null) {
            return;
        }
        GattCommunicationBuilder builder = new GattCommunicationBuilder(bleService);
        builder.notification(Stance.getCharacteristic(Stance.UUID_CHARACTERISTIC_RX), false);
        builder.build().start();
    }

    public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic characteristic) {
        byte[] data = characteristic.getValue();
        if (data.length == 19) {
            handleBandAndBeltData(data);
            isBandAndBeltDataReceived = true;
        } else if (data.length == 20) {
            handleShoeBaseData(data);
            isShoeBaseDataReceived = true;
        }
        if (isBandAndBeltDataReceived && isShoeBaseDataReceived) {
            notifyDataCallback();
            isBandAndBeltDataReceived = false;
            isShoeBaseDataReceived = false;
        }
    }

    private void handleBandAndBeltData(byte[] data) {
        bandDataGetter.x = parseBandData(data[1], data[2]);
        bandDataGetter.y = parseBandData(data[3], data[4]);
        bandDataGetter.z = parseBandData(data[5], data[6]);
        beltDataGetter.x = parseBandData(data[7], data[8]);
        beltDataGetter.y = parseBandData(data[9], data[10]);
        beltDataGetter.z = parseBandData(data[11], data[12]);
    }

    private float parseBandData(byte low, byte high) {
        short result = (short) ((high << 8) | (low & 0xFF));
        return result / 16384f;
    }

    private void handleShoeBaseData(byte[] data) {
        leftShoeBaseData.x = parseShoeBaseData(data[0], data[1]);
        leftShoeBaseData.y = parseShoeBaseData(data[2], data[3]);
        leftShoeBaseData.z = parseShoeBaseData(data[4], data[5]);
        leftShoeBaseData.a = (short) (data[6] & 0xFF);
        leftShoeBaseData.b = (short) (data[7] & 0xFF);
        leftShoeBaseData.c = (short) (data[8] & 0xFF);
        leftShoeBaseData.d = (short) (data[9] & 0xFF);
        rightShoeBaseData.x = parseShoeBaseData(data[10], data[11]);
        rightShoeBaseData.y = parseShoeBaseData(data[12], data[13]);
        rightShoeBaseData.z = parseShoeBaseData(data[14], data[15]);
        rightShoeBaseData.a = (short) (data[16] & 0xFF);
        rightShoeBaseData.b = (short) (data[17] & 0xFF);
        rightShoeBaseData.c = (short) (data[18] & 0xFF);
        rightShoeBaseData.d = (short) (data[19] & 0xFF);
    }

    private float parseShoeBaseData(byte high, byte low) {
        short result = (short) ((high << 8) | (low & 0xFF));
        return result / 4096f;
    }

    public double[] getAllData() {
        double[] result = {
                leftShoeBaseData.x,
                leftShoeBaseData.y,
                leftShoeBaseData.z,
                leftShoeBaseData.a,
                leftShoeBaseData.b,
                leftShoeBaseData.c,
                leftShoeBaseData.d,
                rightShoeBaseData.x,
                rightShoeBaseData.y,
                rightShoeBaseData.z,
                rightShoeBaseData.a,
                rightShoeBaseData.b,
                rightShoeBaseData.c,
                rightShoeBaseData.d,
                beltDataGetter.x,
                beltDataGetter.y,
                beltDataGetter.z,
                bandDataGetter.x,
                bandDataGetter.y,
                bandDataGetter.z
        };
        return result;
    }
    public InsoleDataGetter getLeftInsoleDataGetter() { return leftShoeBaseData; }

    public InsoleDataGetter getRightInsoleDataGetter() { return rightShoeBaseData; }

    public BandDataGetter getBandDataGetter() { return bandDataGetter; }

    public BeltDataGetter getBeltDataGetter() { return beltDataGetter; }

    public void addDataCallbackListener(StanceDataCallbackListener listener) {
        this.dataCallbackListenerList.add(listener);
    }

    public void removeDataCallbackListener(StanceDataCallbackListener listener) {
        this.dataCallbackListenerList.remove(listener);
    }

    public void notifyDataCallback() {
        for (StanceDataCallbackListener listener : dataCallbackListenerList) {
            listener.onDataCallback();
        }
    }

}
