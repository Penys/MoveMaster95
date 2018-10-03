package com.penys.fi.movemaster95


import android.annotation.SuppressLint
import android.app.Fragment
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.penys.fi.movemaster95.ble_connection.BleConnectionFragment


@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {


    private var prefMan: SharedPreferences? = null
    private var greeting: TextView? = null
    private var weight_view: TextView? = null
    private var height_view: TextView? = null
    private var bmi_view: TextView? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.profile_layout, container, false)
        prefMan = PreferenceManager.getDefaultSharedPreferences(activity)
        val heartRateButton = view.findViewById<Button>(R.id.heart_rate_button)
        //views init
        greeting = view.findViewById<TextView>(R.id.greeting_text)
        weight_view = view.findViewById<TextView>(R.id.weight_view)
        height_view = view.findViewById<TextView>(R.id.height_view)
        bmi_view = view.findViewById<TextView>(R.id.bmi_view)
        //data variables
        getBmi()

       // val bleService = BleConnectionFragment::class.java
       // val intent = Intent(context, bleService)

        heartRateButton.setOnClickListener {


            bleConnectionFragment()
            /*if (!isServiceRunning(bleService)) {
                activity.startService(intent)


            } else {

            }*/
        }




        return view
    }

    private fun bleConnectionFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BleConnectionFragment())
                .addToBackStack(null)
                .commit()
    }

    /*@RequiresApi(Build.VERSION_CODES.O)
    private fun isServiceRunning(bleService: Class<BleConnectionFragment>): Boolean {
        val fragmentManager = fragmentManager
        for (fragment in fragmentManager.fragments) {
            if (bleService.name == fragment.getString(0)) {
                return true
            }
        }
        return false
    }*/

    @SuppressLint("SetTextI18n")
    private fun getBmi() {
        val weight = prefMan?.getString(getString(R.string.pref_user_weight), "0")
        val height = prefMan?.getString(getString(R.string.pref_user_height), "0")
        val bmi: Float = (weight!!.toFloat() / ((height!!.toFloat() / 100) * (height.toFloat() / 100)))

        greeting?.text = "Hello " + prefMan?.getString(getString(R.string.pref_user_name), "there") + "!"
        weight_view?.text = "Weight: $weight"
        height_view?.text = "Height: $height"
        bmi_view?.text = "BMI: " + (bmi.toInt()).toString()
    }
}