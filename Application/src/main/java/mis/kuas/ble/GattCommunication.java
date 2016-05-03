package mis.kuas.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.example.android.stancerecognizer.BluetoothLeService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mingjia on 2015/10/22.
 */
public class GattCommunication extends BluetoothGattCallback {

    public static final int COMMUNICATION_CONTINUE = 0;

    public static final int COMMUNICATION_RESTART = 1;

    public static final int COMMUNICATION_RE_EXECUTE = 2;

    public static final int COMMUNICATION_STOP = 3;

    public static final int COMMUNICATION_DESTROY = 4;

    private BluetoothLeService bleService;
    private List<GattCommand> commandList = new ArrayList<GattCommand>();

    private int current = 0;
    private boolean isDestroyed = false;

    public GattCommunication(BluetoothLeService bleService) {
        this.bleService = bleService;
        bleService.addBluetoothCallbackListener(this);
    }

    public void addCommand(GattCommand command) {
        this.commandList.add(command);
    }

    public void start() {
        executeCurrentCommand();
    }

    public void restart() {
        current = 0;
        executeCurrentCommand();
    }

    public void executeNextCommand() {
        current++;
        executeCurrentCommand();
    }

    private void executeCurrentCommand() {
        if (!(isDestroyed) && (current < commandList.size())) {
            getCurrentCommand().execute(bleService);
        } else {
            destroy();
        }
    }

    private void destroy() {
        if (!isDestroyed) {
            bleService.removeBluetoothCallbackListener(this);
            commandList.clear();
            isDestroyed = true;
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt,
                                     BluetoothGattCharacteristic characteristic,
                                     int status) {
        if (!isDestroyed) {
            handleResponseCommand(getCurrentCommand()
                    .onResponse(characteristic.getValue(), status));
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt,
                                      BluetoothGattCharacteristic characteristic,
                                      int status) {
        if (!isDestroyed) {
            handleResponseCommand(getCurrentCommand()
                    .onResponse(characteristic.getValue(), status));
        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                  int status) {
        if (!isDestroyed) {
            handleResponseCommand(getCurrentCommand()
                    .onResponse(descriptor.getValue(), status));
        }
    }

    private GattCommand getCurrentCommand() {
        return commandList.get(current);
    }

    private void handleResponseCommand(int responseCommand) {
        switch(responseCommand) {
            case COMMUNICATION_CONTINUE:
                executeNextCommand();
                break;
            case COMMUNICATION_RESTART:
                restart();
                break;
            case COMMUNICATION_RE_EXECUTE:
                executeCurrentCommand();
                break;
            case COMMUNICATION_STOP:
                // Stop until executeNextCommand() called
                break;
            case COMMUNICATION_DESTROY:
                destroy();
                break;
        }
    }

}
