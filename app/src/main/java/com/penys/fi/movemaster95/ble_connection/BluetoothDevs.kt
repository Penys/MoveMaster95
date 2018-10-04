package com.penys.fi.movemaster95.ble_connection


import android.bluetooth.BluetoothDevice

class BluetoothDevs(var device: BluetoothDevice, var name: String, var deviceAddress: String, var deviceDesibels: Int){

    override fun toString(): String {
        return "$device $name $deviceAddress $deviceDesibels"
    }
}