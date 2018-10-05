@file:Suppress("DEPRECATION")

package com.penys.fi.movemaster95

import android.annotation.SuppressLint
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.util.Log
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.home_layout.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HomeFragment : Fragment(), SensorEventListener {

    private var running = true
    private var sensorManager: SensorManager? = null
    private var isStarted = false
    private var handler: Handler? = null
    private var secondaryHandler: Handler? = Handler()
    private var secondaryProgressStatus = 0
    //private val heartReceiver by lazy { makeBroadcastReceiver() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.home_layout, container, false)
        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        return view
    }

    /*private fun makeBroadcastReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                Toast.makeText(context, "Broadcast Intent Detected.",
                        Toast.LENGTH_LONG).show()

                val bundle = intent.extras
                var heartRate = ""
                if (bundle != null) {
                    heartRate = bundle.get("heartRate") as String
                    Log.d("MITÄ", heartRate)

                }
            }
        }
    }*/

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        // LocalBroadcastManager.getInstance(context)
        //        .registerReceiver(heartReceiver, IntentFilter())

        running = true
        val stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepsSensor == null) {
            Toast.makeText(activity!!.applicationContext, "No Step Counter Sensor detected!!", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)
        }

        step_count.setOnClickListener {
            val st = step_count.text.toString()
            val sIntent = Intent()
            sIntent.action = Intent.ACTION_SEND
            sIntent.type = "text/plain"
            sIntent.putExtra(Intent.EXTRA_TEXT, st + " steps")
            sIntent.putExtra(Intent.EXTRA_SUBJECT, "Look how much have I walked today?!")
            startActivity(Intent.createChooser(sIntent, "Share steps via"))
        }

        progressBar()

        progressBarSecondary.setOnClickListener {

            if (secondaryProgressStatus >= 10000) {
                arCoreActivity()
            } else {
                Toast.makeText(context, "You don't have enough steps for playing this game!!", Toast.LENGTH_SHORT).show()
            }

        }
        progressBarSecondary.setOnLongClickListener{
            arCoreActivity()
            true
        }
    }

    @SuppressLint("SetTextI18n")
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
                val secProgStat = secondaryProgressStatus / 100
                progressBarSecondary.secondaryProgress = secProgStat
                progress_text.text = "Daily Goal progress:\n $secondaryProgressStatus of 10 000"

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

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    override fun onSensorChanged(event: SensorEvent?) {
        val prefMan = PreferenceManager.getDefaultSharedPreferences(activity)
        with(prefMan.edit()) {
            putInt(getString(R.string.step_count), event!!.values[0].toInt())
            putString(getString(R.string.step_count_summary), event.values[0].toString())
            apply()
        }

        if (running) {

            step_count.text = event.values[0].toString()
            secondaryProgressStatus = event.values[0].toInt()

        }
    }

    private fun arCoreActivity() {
        val intent = Intent(activity, ARCoreActivity::class.java).apply {
        }
        startActivity(intent)
    }

}
