package com.example.parkkyungsuk.kotlinmassanger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerButton_register.setOnClickListener {

            val email = email_editText_registraion_screen.text.toString()
            val password = password_editText_registraion_screen.text.toString()

            Log.d("MainActivity", "email is" + email)
            Log.d("MainActivity", "password is $password")
        }

        already_have_account_textView.setOnClickListener {
            Log.d("MainActivity", "Try to show the Login Activity")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
