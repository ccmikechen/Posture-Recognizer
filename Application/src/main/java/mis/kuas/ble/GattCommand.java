package mis.kuas.ble;

import android.bluetooth.BluetoothGatt;

import com.example.android.stancerecognizer.BluetoothLeService;

public abstract class GattCommand {

    public abstract void execute(BluetoothLeService bleService);

    public int onResponse(byte[] value, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS)
            return GattCommunication.COMMUNICATION_CONTINUE;
        else
            return GattCommunication.COMMUNICATION_DESTROY;
    }

}
