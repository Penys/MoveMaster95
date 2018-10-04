@file:Suppress("DEPRECATION")

package com.penys.fi.movemaster95

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.io.File
import java.io.IOException
import com.penys.fi.movemaster95.ble_connection.BleConnectionFragment

class ProfileFragment : Fragment() {

    private var prefMan: SharedPreferences? = null
    private var greeting: TextView? = null
    private var weight_view: TextView? = null
    private var height_view: TextView? = null
    private var bmi_view: TextView? = null

    val REQUEST_IMAGE_CAPTURE = 1
    private var photopath: String = ""

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
        val weight = prefMan.getString(getString(R.string.pref_user_weight), "")
        val height = prefMan.getString(getString(R.string.pref_user_height), "")
        val bmi: Float = (weight.toFloat()/((height.toFloat()/100)*(height.toFloat()/100)))
        photopath = prefMan.getString(getString(R.string.photo_pref),"")

        //assign data to views
        greeting.text = "Hello " + prefMan.getString(getString(R.string.pref_user_name), "there") + "!"
        weight_view.text = "Weight: " + weight
        height_view.text = "Height: " + height
        bmi_view.text = "BMI: " + (bmi.toInt()).toString()
        profile_pic.setImageURI(Uri.parse(photopath))
        getBmi()

        heartRateButton.setOnClickListener {
            bleConnectionFragment()

        }

        //profile picture change
        profile_pic.setOnClickListener {
            takePicture()
        }

        return view
    }

    private fun takePicture() {
        val myIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (myIntent.resolveActivity(activity.packageManager) != null) {

            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            }catch (e: IOException){}
            if(photoFile != null){
                val photoUri = FileProvider.getUriForFile(
                        activity,
                        "com.penys.fi.movemaster95",
                        photoFile
                )
                myIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(myIntent, REQUEST_IMAGE_CAPTURE)
                val prefMan = PreferenceManager.getDefaultSharedPreferences(activity)
                with(prefMan.edit()){
                    putString(getString(R.string.photo_pref), photoFile.toString())
                    apply()
                }
            }
        }
    }

    private fun createImageFile(): File? {
        val fileName = "MyProfilePic"
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                fileName,
                ".jpg",
                storageDir
        )
        photopath = image!!.absolutePath

        return image
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
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            profile_pic?.setImageURI(Uri.parse(photopath))
        }
    }
}
