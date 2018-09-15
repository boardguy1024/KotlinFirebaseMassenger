package com.example.parkkyungsuk.kotlinmassanger

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    var seletedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //写真
        selectPhoto_button_register.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        registerButton_register.setOnClickListener {

           performRegister()
        }

        already_have_account_textView.setOnClickListener {
            Log.d("RegisterActivity", "Try to show the Login Activity")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 0? pic??
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RegisterActivity","Photo was Selected")

            seletedPhotoUri = data.data
        }
    }

    private fun performRegister() {
        val email = email_editText_registraion_screen.text.toString()
        val password = password_editText_registraion_screen.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Please enter your email/password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "email is" + email)
        Log.d("RegisterActivity", "password is $password")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    //else if sucessful
                    Log.d("Main", "Successfully created user with uid ${it.result.user.uid}")

                    uploadImageToFirebaseStorage()

                }
                .addOnFailureListener {
                    Log.d("Main", "${it.message}")
                    Toast.makeText(this,"${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

    private fun uploadImageToFirebaseStorage() {

        if (seletedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(seletedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("Register", "Successfully uploaed image: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("Register","${it}")
                    }

                    saveUserToDatabase(it.toString())

                }
                .addOnFailureListener {

                }
    }

    private fun saveUserToDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/${uid}")

        val user = User(uid, username_editText_registraion_screen.text.toString(), profileImageUrl)

        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("Register","Finally we saved the user to FirebaseDatabse!")
                }
    }
}

class User(val uid: String, val username: String, val profileImageUrl: String)




















