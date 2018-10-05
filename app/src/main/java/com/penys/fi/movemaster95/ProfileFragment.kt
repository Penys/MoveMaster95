@file:Suppress("DEPRECATION")

package com.penys.fi.movemaster95

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.content.FileProvider
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

    lateinit var prefMan: SharedPreferences
    lateinit var greeting: TextView
    lateinit var weightView: TextView
    lateinit var heightView: TextView
    lateinit var bmiView: TextView
    lateinit var profilePic: ImageView
    val REQUEST_IMAGE_CAPTURE = 1
    private var photoPath: String? = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.profile_layout, container, false)
        val heartRateButton = view.findViewById<Button>(R.id.heart_rate_button)

        //views init
        greeting = view.findViewById(R.id.greeting_text)
        weightView = view.findViewById(R.id.weight_view)
        heightView = view.findViewById(R.id.height_view)
        bmiView = view.findViewById(R.id.bmi_view)
        profilePic = view.findViewById(R.id.profile_pic_view)

        //prefs init
        prefMan = PreferenceManager.getDefaultSharedPreferences(activity)
        photoPath = prefMan.getString(getString(R.string.photo_pref),"")
        profilePic.setImageURI(Uri.parse(photoPath))

        //calculate bmi
        getBmi()
        //bluetooth connection button
        heartRateButton.setOnClickListener { bleConnectionFragment() }
        //profile picture change
        profilePic.setOnClickListener { takePicture() }

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
                myIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                startActivityForResult(myIntent, REQUEST_IMAGE_CAPTURE)
                val prefMan = PreferenceManager.getDefaultSharedPreferences(activity)
                with(prefMan.edit()){
                    putString(getString(R.string.photo_pref), photoFile.toString())
                    putString(getString(R.string.photo_pref_sum), photoFile.toString())
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
        photoPath = image!!.absolutePath

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
        val weight = prefMan.getString(getString(R.string.pref_user_weight), "0")
        val height = prefMan.getString(getString(R.string.pref_user_height), "0")
        val bmi: Float = (weight!!.toFloat() / ((height!!.toFloat() / 100) * (height.toFloat() / 100)))

        greeting.text = "Hello " + prefMan.getString(getString(R.string.pref_user_name), "there") + "!"
        weightView.text = "Weight: $weight"
        heightView.text = "Height: $height"
        bmiView.text = "BMI: " + (bmi.toInt()).toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val profilePic = view?.findViewById<ImageView>(R.id.profile_pic_view)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            profilePic?.setImageURI(Uri.parse(photoPath))
        }
    }
}
