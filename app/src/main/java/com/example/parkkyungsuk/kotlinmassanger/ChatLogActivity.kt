package com.example.parkkyungsuk.kotlinmassanger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatLog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user.username

        setDummyData()

        send_button_chatlog.setOnClickListener {
            performSendMessage()
        }
    }

    private fun performSendMessage() {

        val text = editText_chatlog.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid

        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()

        if (fromId == null) return


        val chatMassage = ChatMassage(text, ref.key!!, fromId, toId, System.currentTimeMillis() / 1000)

        ref.setValue(chatMassage)
                .addOnSuccessListener {
                    Log.d(TAG, "Saved our chat message: ${ref.key}")
                }


    }

    private fun setDummyData() {
        val adaptor = GroupAdapter<ViewHolder>()

        adaptor.add(ChatFromItem("FromText!!!"))
        adaptor.add(ChatToItem("ToText!!!") )

        recyclerview_chat_log.adapter = adaptor
    }
}

class ChatMassage(val text: String, val id: String, val fromId: String, val toId: String, val timeStamp: Long)

class ChatFromItem(val text: String): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_text_from_row.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_text_to_row.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}


















