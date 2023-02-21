package com.example.app1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var firstNameEditText: EditText
    private lateinit var middleNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var pictureButton: Button
    private lateinit var submitButton: Button
    private lateinit var thumbnailView: ImageView
    private lateinit var thumbnail: Bitmap

    var firstName = ""
    var lastName = ""
    var photoTaken = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstNameEditText = findViewById(R.id.firstNameEditText)
        middleNameEditText = findViewById(R.id.middleNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        pictureButton = findViewById(R.id.pictureButton)
        submitButton = findViewById(R.id.submitButton)
        thumbnailView = findViewById(R.id.thumbnail)
        thumbnail = createBitmap(20, 20, Bitmap.Config.ARGB_8888)

        submitButton.setOnClickListener {

            val firstNameEntry = firstNameEditText.text.toString().trim()
            val middleNameEntry = middleNameEditText.text.toString().trim()
            val lastNameEntry = lastNameEditText.text.toString().trim()

            val firstNameParts = firstNameEntry.split(" ")
            if (firstNameParts.size > 1) {
                val text = "First Name Cannot Contain Spaces!"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
                return@setOnClickListener
            }

            Log.d("hi", firstNameParts.size.toString())

            if (firstNameParts.get(0) == "") {

                val text = "Must Enter First Name!"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
                return@setOnClickListener
            }

            val middleNameParts = middleNameEntry.split(" ")
            if (middleNameParts.size > 1) {
                val text = "Middle Name Cannot Contain Spaces!"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
                return@setOnClickListener
            }

            val lastNameParts = lastNameEntry.split(" ")
            if (lastNameParts.size > 1) {
                val text = "Last Name Cannot Contain Spaces!"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
                return@setOnClickListener
            }

            if (lastNameParts.get(0) == "") {
                val text = "Must Enter Last Name!"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
                return@setOnClickListener
            }

            if (photoTaken == false) {
                val text = "Must Take A Photo!"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
                return@setOnClickListener
            }

            firstName = firstNameParts.firstOrNull() ?: ""
            lastName = lastNameParts.firstOrNull() ?: ""

            val intent = Intent(this, Activity2::class.java).apply {
                putExtra("firstName", firstName)
                putExtra("lastName", lastName)
            }
            startActivity(intent)
        }

        pictureButton.setOnClickListener {

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try {
                cameraActivity.launch(cameraIntent)
            }catch(ex: ActivityNotFoundException){
                val text = "Camera Doesn't Work!"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            }
        }

    }

    private val cameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK) {
            thumbnailView = findViewById<View>(R.id.thumbnail) as ImageView

            if (Build.VERSION.SDK_INT >= 33) {
                val thumbnailImage = result.data!!.getParcelableExtra("data", Bitmap::class.java)
                thumbnailView!!.setImageBitmap(thumbnailImage)
                if (thumbnailImage != null) {
                    thumbnail = thumbnailImage
                    photoTaken = true
                }
            }
            else{
                val thumbnailImage = result.data!!.getParcelableExtra<Bitmap>("data")
                thumbnailView!!.setImageBitmap(thumbnailImage)
                if (thumbnailImage != null) {
                    thumbnail = thumbnailImage
                    photoTaken = true
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("savedFirstName", firstName)
        outState.putString("savedLastName", lastName)
        outState.putBoolean("savedPhotoState", photoTaken)

        //Convert bitmap thumbnail to a bytearray that can be saved
        val stream = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val convertedThumbnail = stream.toByteArray()

        outState.putByteArray("savedThumbnail", convertedThumbnail)
    }

    override fun onRestoreInstanceState(savedInstanceState:Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        firstName = savedInstanceState.getString("savedFirstName", "")
        lastName = savedInstanceState.getString("savedLastName", "")
        photoTaken = savedInstanceState.getBoolean("savedPhotoState", false)

        //Convert thumbnail bytearray to a usable bitmap
        val stream = ByteArrayInputStream(savedInstanceState.getByteArray("savedThumbnail") as ByteArray?)
        val bitmapThumbnail = BitmapFactory.decodeStream(stream)
        thumbnail = bitmapThumbnail
        thumbnailView.setImageBitmap(thumbnail)

    }


}