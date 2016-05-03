package mis.kuas.ble;

import android.bluetooth.BluetoothGattCharacteristic;

import com.example.android.stancerecognizer.BluetoothLeService;

/**
 * Created by mingjia on 2015/10/23.
 */
public class GattNotificationCommand extends GattCommand {

    private BluetoothGattCharacteristic characteristic;
    private boolean enable;

    public GattNotificationCommand(BluetoothGattCharacteristic characteristic, boolean enable) {
        this.characteristic = characteristic;
        this.enable = enable;
    }

    @Override
    public void execute(BluetoothLeService bleService) {
        bleService.setCharacteristicNotification(characteristic, enable);
    }

}
