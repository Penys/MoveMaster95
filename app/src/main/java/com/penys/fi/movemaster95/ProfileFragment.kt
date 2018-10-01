package com.penys.fi.movemaster95

import android.os.Bundle
import android.app.Fragment
import android.preference.PreferenceManager
import android.provider.Settings.Global.getInt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.profile_layout, container, false)
        val prefMan = PreferenceManager.getDefaultSharedPreferences(activity)
        //views init
        val greeting = view.findViewById<TextView>(R.id.greeting_text)
        val weight_view = view.findViewById<TextView>(R.id.weight_view)
        val height_view = view.findViewById<TextView>(R.id.height_view)
        val bmi_view = view.findViewById<TextView>(R.id.bmi_view)
        //data variables
        val weight = prefMan.getString(getString(R.string.pref_user_weight), "")
        val height = prefMan.getString(getString(R.string.pref_user_height), "")
        val bmi: Float = (weight.toFloat()/((height.toFloat()/100)*(height.toFloat()/100)))

        greeting.text = "Hello " + prefMan.getString(getString(R.string.pref_user_name), "there") + "!"
        weight_view.text = "Weight: " + weight
        height_view.text = "Height: " + height
        bmi_view.text = "BMI: " + (bmi.toInt()).toString()

        return view
    }
}