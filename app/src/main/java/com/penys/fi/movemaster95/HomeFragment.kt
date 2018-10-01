package com.penys.fi.movemaster95

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.home_layout.*

class HomeFragment : Fragment(), SensorEventListener {
    var running = true
    var sensorManager: SensorManager? = null

    var isStarted = false
    var handler: Handler? = null
    var secondaryHandler: Handler? = Handler()
    var secondaryProgressStatus = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.home_layout, container, false)
        val arCoreButton = view.findViewById<Button>(R.id.ar_core_button)
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
            sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)
        }
        progressBar()
        ar_core_button.setOnClickListener{
            arCoreActivity()

        }

    }

    fun progressBar() {

        handler = Handler(Handler.Callback {
            if (isStarted) {


                handler?.sendEmptyMessageDelayed(0, 100)
            }
            true
        })

        handler?.sendEmptyMessage(0)


        Thread(Runnable {

            secondaryHandler?.post {
                val secProgStat = secondaryProgressStatus /100
                progressBarSecondary.setSecondaryProgress(secProgStat)
                progress_text.setText("Daily Goal progress:\n$secondaryProgressStatus of 10 000")

                if (secondaryProgressStatus == 10000) {
                    // textViewSecondary.setText("Single task complete.")
                }
            }
        }).start()
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
            secondaryProgressStatus  = event?.values[0].toInt()
        }
    }
    fun arCoreActivity() {
        val intent = Intent(activity, ARCoreActivity::class.java).apply {
        }
        startActivity(intent)
    }

}
