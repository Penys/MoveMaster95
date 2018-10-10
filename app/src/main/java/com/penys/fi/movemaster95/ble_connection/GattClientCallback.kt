package com.penys.fi.movemaster95.ble_connection

import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import java.util.*


class GattClientCallback(val ctx: Context) : BluetoothGattCallback() {

    private val HEART_RATE_SERVICE_UUID = convertFromInteger(0x180D)
    private val HEART_RATE_MEASUREMENT_CHAR_UUID = convertFromInteger(0x2A37)
    private val CLIENT_CHARACTERISTIC_CONFIG_UUID = convertFromInteger(0x2902)


    private fun convertFromInteger(i: Int): UUID {
        val msb = 0x0000000000001000L
        val lsb = -0x7fffff7fa064cb05L
        val value = (i and -0x1).toLong()
        return UUID(msb or (value shl 32), lsb)
    }


    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)

        if (status == BluetoothGatt.GATT_FAILURE) {
            Log.d("DBG", "Gatt connection failure")

            return
        } else if (status != BluetoothGatt.GATT_SUCCESS) {
            Log.d("DBG", "Gatt connection success")

            return
        }
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Log.d("DBG", "Connected GATT service")

            gatt?.discoverServices()
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            Log.d("DBG", "Gatt state disconnect")

            return
        }
    }


    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        super.onServicesDiscovered(gatt, status)
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return
        }
        Log.d("DBG", "onServiceDiscovered()")

        for (gattService in gatt!!.services) {
            Log.d("DBG", "Service ${gattService.uuid}")

            if (gattService.uuid == HEART_RATE_SERVICE_UUID) {
                Log.d("DBG", "BINGO!")

                for (gattCharacteristic in gattService.characteristics)
                    Log.d("DBG", "Characteristic ${gattCharacteristic.uuid}")

                val characteristic = gatt.getService(HEART_RATE_SERVICE_UUID)
                        .getCharacteristic(HEART_RATE_MEASUREMENT_CHAR_UUID)
                gatt.setCharacteristicNotification(characteristic, true)


                val descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID)

                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE


                gatt.writeDescriptor(descriptor)
            }
        }


    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        Log.d("DBG", "Characteristic data received")
        Log.d("MITÄ TULEE ULOS", "${characteristic.value[0]}")
        val intent = Intent("heart_rate")
        intent.putExtra("heart_rate","${characteristic.value[1]}")
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent)


    }

    override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
        Log.d("DBG", "OnDescriptionWrite")
        val characteristic = gatt.getService(HEART_RATE_SERVICE_UUID)
                .getCharacteristic(HEART_RATE_MEASUREMENT_CHAR_UUID)

        characteristic.value = byteArrayOf(1, 1)
        gatt.writeCharacteristic(characteristic)
    }

}