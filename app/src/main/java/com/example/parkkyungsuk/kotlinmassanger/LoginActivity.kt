package com.example.parkkyungsuk.kotlinmassanger

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        button_login_loginActivity.setOnClickListener {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(editText_email_loginActivity.text.toString(), editText_password_loginActivity.text.toString())
                    .addOnCompleteListener {
                        if (!it.isSuccessful) return@addOnCompleteListener

                        val intent = Intent(this, LatestActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)

                    }
                    .addOnFailureListener {
                        Toast.makeText(this,"${it.message}", Toast.LENGTH_SHORT).show()
                    }
        }

        textView_backToRegistration.setOnClickListener {
            finish()
        }
    }
}