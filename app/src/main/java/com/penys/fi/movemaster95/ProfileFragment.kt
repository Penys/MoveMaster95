@file:Suppress("DEPRECATION")

package com.penys.fi.movemaster95

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.penys.fi.movemaster95.ble_connection.BleConnectionFragment


class ProfileFragment : Fragment() {

    private var prefMan: SharedPreferences? = null
    private var greeting: TextView? = null
    private var weight_view: TextView? = null
    private var height_view: TextView? = null
    private var bmi_view: TextView? = null

    val REQUEST_IMAGE_CAPTURE = 1

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
        val profile_pic = view.findViewById<ImageView>(R.id.profile_pic_view)

        //data variables
        getBmi()

        heartRateButton.setOnClickListener {
            bleConnectionFragment()

        }

        //profile picture change
        profile_pic.setOnClickListener {
            val myIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (myIntent.resolveActivity(activity.packageManager) != null) {
                startActivityForResult(myIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

        return view
    }

    private fun bleConnectionFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BleConnectionFragment())
                .addToBackStack(null)
                .commit()
    }


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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val profile_pic = view?.findViewById<ImageView>(R.id.profile_pic_view)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data!!.extras
            val imageBmp = extras!!.get("data") as Bitmap
            profile_pic?.setImageBitmap(imageBmp)
        }
    }
}
