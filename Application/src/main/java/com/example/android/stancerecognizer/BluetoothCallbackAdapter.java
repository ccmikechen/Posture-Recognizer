package com.example.android.stancerecognizer;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mingjia on 2015/10/22.
 */
public class BluetoothCallbackAdapter extends BluetoothGattCallback {

    private List<BluetoothGattCallback> gattCallbackList = new ArrayList<BluetoothGattCallback>();
private List a = new LinkedList();
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        for (BluetoothGattCallback callback : gattCallbackList)
            callback.onConnectionStateChange(gatt, status, newState);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        for (BluetoothGattCallback callback : gattCallbackList)
            callback.onServicesDiscovered(gatt, status);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt,
                                     BluetoothGattCharacteristic characteristic,
                                     int status) {
        logCallbackData("Read", characteristic.getValue());
        for (BluetoothGattCallback callback : gattCallbackList)
            callback.onCharacteristicRead(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt,
                                      BluetoothGattCharacteristic characteristic,
                                      int status) {
        String tag = characteristic.getWriteType() ==
                BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE ?
                "WriteNoRsp" :
                "Write";
        logCallbackData(tag, characteristic.getValue());

        for (BluetoothGattCallback callback : gattCallbackList)
            callback.onCharacteristicWrite(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic characteristic) {
        logCallbackData("Notify", characteristic.getValue());
        for (BluetoothGattCallback callback : gattCallbackList)
            callback.onCharacteristicChanged(gatt, characteristic);

    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                  int status) {
        for (BluetoothGattCallback callback : gattCallbackList)
            callback.onDescriptorWrite(gatt, descriptor, status);
    }

    private void logCallbackData(String tag, byte[] data) {
        final StringBuilder stringBuilder = new StringBuilder(data.length);
        for(byte byteChar : data)
            stringBuilder.append(String.format("%02X ", byteChar));
        Log.d(tag, stringBuilder.toString());
    }

    public void addBluetoothCallbackListener(BluetoothGattCallback listener) {
        List<BluetoothGattCallback> newCallbackList =
                new ArrayList<BluetoothGattCallback>(gattCallbackList);
        newCallbackList.add(listener);
        this.gattCallbackList = newCallbackList;
    }

    public void removeBluetoothCallbackListener(BluetoothGattCallback listener) {
        List<BluetoothGattCallback> newCallbackList =
                new ArrayList<BluetoothGattCallback>(gattCallbackList);
        newCallbackList.remove(listener);
        this.gattCallbackList = newCallbackList;

    }

}
