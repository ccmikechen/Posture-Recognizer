package mis.kuas.ble;

import android.bluetooth.BluetoothGattCharacteristic;

import com.example.android.stancerecognizer.BluetoothLeService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mingjia on 2015/10/22.
 */
public class GattCommunicationBuilder {

    private BluetoothLeService bleService;

    public GattCommunicationBuilder(BluetoothLeService bleService) {
        this.bleService = bleService;
    }

    private List<GattCommand> commandList = new ArrayList<GattCommand>();

    public GattCommunicationBuilder write(BluetoothGattCharacteristic characteristic, byte[] value) {
        return write(new GattWriteCommand(characteristic, value));
    }

    public GattCommunicationBuilder write(GattWriteCommand command) {
        add(command);
        return this;
    }

    public GattCommunicationBuilder writeNoRsp(BluetoothGattCharacteristic characteristic, byte[] value) {
        return writeNoRsp(new GattWriteNoRspCommand(characteristic, value));
    }

    public GattCommunicationBuilder writeNoRsp(GattWriteNoRspCommand command) {
        add(command);
        return this;
    }

    public GattCommunicationBuilder read(BluetoothGattCharacteristic characteristic, byte[] value) {
        return read(new GattReadCommand(characteristic, value));
    }

    public GattCommunicationBuilder read(GattReadCommand command) {
        add(command);
        return this;
    }

    public GattCommunicationBuilder notification(BluetoothGattCharacteristic characteristic, boolean enable) {
        return notification(new GattNotificationCommand(characteristic, enable));
    }

    public GattCommunicationBuilder notification(GattNotificationCommand command) {
        add(command);
        return this;
    }

    private void add(GattCommand command) {
        commandList.add(command);
    }

    public GattCommunication build() {
        GattCommunication communication = new GattCommunication(bleService);
        for (GattCommand command : commandList)
            communication.addCommand(command);
        return communication;
    }

}
