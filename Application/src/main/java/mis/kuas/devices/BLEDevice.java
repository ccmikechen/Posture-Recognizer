package mis.kuas.devices;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.HashMap;
import java.util.Map;

public abstract class BLEDevice {

    private static final Map<String, BluetoothGattCharacteristic> characteristics =
            new HashMap<String, BluetoothGattCharacteristic>();

    public static final void addCharacteristic(String uuid, BluetoothGattCharacteristic characteristic) {
        characteristics.put(uuid, characteristic);
    }

    public static final BluetoothGattCharacteristic getCharacteristic(String uuid) {
        return characteristics.get(uuid);
    }

}
