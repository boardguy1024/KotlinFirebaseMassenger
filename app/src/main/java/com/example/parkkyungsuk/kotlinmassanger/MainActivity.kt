package com.example.parkkyungsuk.kotlinmassanger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerButton_register.setOnClickListener {

           performRegister()
        }

        already_have_account_textView.setOnClickListener {
            Log.d("MainActivity", "Try to show the Login Activity")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister() {
        val email = email_editText_registraion_screen.text.toString()
        val password = password_editText_registraion_screen.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Please enter your email/password", Toast.LENGTH_SHORT).show()
            return
        }


        Log.d("MainActivity", "email is" + email)
        Log.d("MainActivity", "password is $password")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    //else if sucessful
                    Log.d("Main", "Successfully created user with uid ${it.result.user.uid}")

                }
                .addOnFailureListener {
                    Log.d("Main", "${it.message}")
                    Toast.makeText(this,"${it.message}", Toast.LENGTH_SHORT).show()
                }
    }
}




















