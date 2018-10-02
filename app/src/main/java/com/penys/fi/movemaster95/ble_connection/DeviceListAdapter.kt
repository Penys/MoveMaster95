package com.penys.fi.movemaster95.ble_connection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.penys.fi.movemaster95.R

class DeviceListAdapter(context: Context, private val devices: List<BluetoothDevs>) :
        BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.bluetooth_devices, p2, false)
        val thisBleDev = devices[position]

        var tv = rowView.findViewById(R.id.device_name_field) as TextView
        tv.text = thisBleDev.name

        var tv2 = rowView.findViewById(R.id.device_address_field) as TextView
        tv2.text = thisBleDev.deviceAddress

        var tv3 = rowView.findViewById(R.id.device_db_field) as TextView
        tv3.text = thisBleDev.deviceDesibels.toString()

        return rowView
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return devices[position]
    }

    override fun getCount(): Int {
        return devices.size
    }
}