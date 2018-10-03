@file:Suppress("DEPRECATION")

package com.penys.fi.movemaster95

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.graphics.Bitmap
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
import android.widget.ImageView
import android.widget.TextView
import java.io.File
import java.io.IOException

class ProfileFragment : Fragment() {

    val REQUEST_IMAGE_CAPTURE = 1
    private var photopath: String = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.profile_layout, container, false)
        val prefMan = PreferenceManager.getDefaultSharedPreferences(activity)

        //views init
        val greeting = view.findViewById<TextView>(R.id.greeting_text)
        val weight_view = view.findViewById<TextView>(R.id.weight_view)
        val height_view = view.findViewById<TextView>(R.id.height_view)
        val bmi_view = view.findViewById<TextView>(R.id.bmi_view)
        val profile_pic = view.findViewById<ImageView>(R.id.profile_pic_view)

        //data variables
        val weight = prefMan.getString(getString(R.string.pref_user_weight), "")
        val height = prefMan.getString(getString(R.string.pref_user_height), "")
        val bmi: Float = (weight.toFloat()/((height.toFloat()/100)*(height.toFloat()/100)))

        //assign data to views
        greeting.text = "Hello " + prefMan.getString(getString(R.string.pref_user_name), "there") + "!"
        weight_view.text = "Weight: " + weight
        height_view.text = "Height: " + height
        bmi_view.text = "BMI: " + (bmi.toInt()).toString()

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
            }
        }
    }

    private fun createImageFile(): File? {
        val fileName = "MyProfilePic"
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image = File.createTempFile(
                fileName,
                ".jpg",
                storageDir
        )
        photopath = image!!.absolutePath

        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val profile_pic = view?.findViewById<ImageView>(R.id.profile_pic_view)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            profile_pic?.setImageURI(Uri.parse(photopath))
        }
    }
}