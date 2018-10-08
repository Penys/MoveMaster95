@file:Suppress("DEPRECATION")

package com.penys.fi.movemaster95.ble_connection

import android.Manifest
import android.app.Fragment
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.penys.fi.movemaster95.R
import kotlinx.android.synthetic.main.bluetooth_layout.*
import java.util.*

class BleConnectionFragment : Fragment() {


    private var mBluetoothLeScanner: BluetoothLeScanner? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mScanCallback: ScanCallback? = null
    private var mHandler: Handler? = null
    private var mScanning = false
    private var mScanResults: HashMap<String, ScanResult>? = null
    private var mBluetoothGatt: BluetoothGatt? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bluetooth_layout, container, false)
        val bluetoothManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.GONE
        mBluetoothAdapter = bluetoothManager.adapter
        hasPermissions()

        return view
    }

    override fun onResume() {
        super.onResume()

        scan_devices.setOnClickListener {

            showProgressBar()
            DeviceList.devicesList.clear()
            startScan()
        }

        list_view.setOnItemClickListener { _, _, position, _ ->

            Log.d("USR", "Selected $position")
            val selectedBluetooth = list_view.getItemAtPosition(position) as BluetoothDevs
            Log.d("DEBUGGER", "Device: $selectedBluetooth")


            val gattClientCallback = GattClientCallback(context)
            mBluetoothGatt = selectedBluetooth.device.connectGatt(context, false, gattClientCallback)
        }
    }

    private fun showProgressBar() {

        Thread(Runnable {
            activity.runOnUiThread { progressBar.visibility = View.VISIBLE }
    }).start()
}

companion object {
    const val SCAN_PERIOD: Long = 15000
}

private fun startScan() {
    Log.d("DBG", "Scan start")
    mScanResults = HashMap()
    mScanCallback = BtleScanCallback()
    mBluetoothLeScanner = mBluetoothAdapter!!.bluetoothLeScanner

    val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
            .build()

    val filter: List<ScanFilter>? = null

    // Stops scanning after a pre-defined scan period.
    mHandler = Handler()
    mHandler!!.postDelayed({ stopScan() }, SCAN_PERIOD)

    mScanning = true

    mBluetoothLeScanner!!.startScan(filter, settings, mScanCallback)
}

private fun stopScan() {
    mBluetoothLeScanner!!.stopScan(mScanCallback)
    mScanning = false
    progressBar.visibility = View.GONE

    list_view.adapter = DeviceListAdapter(
            context,
            DeviceList.devicesList)
}

@RequiresApi(Build.VERSION_CODES.M)
private fun hasPermissions(): Boolean {
    if (mBluetoothAdapter == null || !mBluetoothAdapter!!.isEnabled) {
        Log.d("BUG", "No Bluetooth LE capability")
        return false
    } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
        Log.d("DBG", "No fine location access")
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        return true
    }
    return true
}


private inner class BtleScanCallback : ScanCallback() {

    override fun onScanResult(callbackType: Int, result: ScanResult) {
        addScanResult(result)
    }

    override fun onBatchScanResults(results: List<ScanResult>) {
        for (result in results) {
            addScanResult(result)
        }
    }

    override fun onScanFailed(errorCode: Int) {
        Log.d("DBG", "BLE Scan Failed with code $errorCode")
    }


    private fun addScanResult(result: ScanResult) {

        val device = result.device
        val deviceAddress = device.address
        val deviceName = device.name
        val deviceDesibels = result.rssi

        if (deviceName != null) {
            DeviceList.devicesList.add(BluetoothDevs(device, deviceName, deviceAddress, deviceDesibels))
        } else {
            return
        }

        mScanResults!![deviceAddress] = result

        Log.d("DBG", "Device address: $deviceAddress device: $device")
    }
}


object DeviceList {
    val devicesList: kotlin.collections.MutableList<BluetoothDevs> = java.util.ArrayList()
}

}