package com.example.parkkyungsuk.kotlinmassanger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = "Chat Log"
    }
}
