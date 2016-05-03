package mis.kuas.ble;

import android.bluetooth.BluetoothGattCharacteristic;

import com.example.android.stancerecognizer.BluetoothLeService;

/**
 * Created by mingjia on 2015/10/23.
 */
public class GattWriteCommand extends GattCommand {

    private BluetoothGattCharacteristic characteristic;
    private byte[] value;

    public GattWriteCommand(BluetoothGattCharacteristic characteristic, byte[] value) {
        this.characteristic = characteristic;
        this.value = value;
    }

    @Override
    public void execute(BluetoothLeService bleService) {
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
        characteristic.setValue(value);
        bleService.writeCharacteristic(characteristic);
    }

}
