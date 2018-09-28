package com.penys.fi.movemaster95

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.home_layout.*

@Suppress("DEPRECATION")
class HomeFragment : Fragment(), SensorEventListener {
    var running = true
    var sensorManager: SensorManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.home_layout, container, false)
        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return view
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepsSensor == null) {
            Toast.makeText(activity!!.applicationContext, "No Step Counter Sensor detected!!", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        Log.d("dbg", event?.values!![0].toString())
        if (running) {
            step_count.text = event?.values[0].toString()
        }
    }
}