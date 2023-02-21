package com.example.app1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

class Activity2 : AppCompatActivity() {
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        intent = getIntent()
        var firstName = intent.getStringExtra("firstName")
        var lastName = intent.getStringExtra("lastName")

        textView = findViewById(R.id.viewText)
        textView.text = firstName + " " + lastName + " is logged in!"
    }
}