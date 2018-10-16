package com.example.parkkyungsuk.kotlinmassanger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.abc_alert_dialog_material.*
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatLog"
    }

    var adaptor = GroupAdapter<ViewHolder>()
    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        supportActionBar?.title = toUser?.username

        listenForMessages()

        send_button_chatlog.setOnClickListener {
            performSendMessage()
        }
    }
    private fun listenForMessages() {

        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMassage::class.java)

                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = LatestActivity.currentUser
                        adaptor.add(ChatFromItem(chatMessage.text, currentUser!!))
                    } else {
                        adaptor.add(ChatToItem(chatMessage.text, toUser!!))
                    }
                }

                recyclerview_chat_log.adapter = adaptor
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun performSendMessage() {

        val text = editText_chatlog.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid

        //val ref = FirebaseDatabase.getInstance().getReference("/messages").push()
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toRef = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        if (fromId == null) return

        // chatMassageオブジェクトをFirebaseのmessagesに保持
        val chatMassage = ChatMassage(text, ref.key!!, fromId, toId, System.currentTimeMillis() / 1000)

        ref.setValue(chatMassage)
                .addOnSuccessListener {
                    Log.d(TAG, "Saved our chat message: ${ref.key}")

                    editText_chatlog.text.clear()
                    //最新メッセージまでにスクロールさせる
                    recyclerview_chat_log.scrollToPosition(adaptor.itemCount - 1)
                }
        toRef.setValue(chatMassage)

        val letestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        letestMessageRef.setValue(chatMassage)

        val letestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        letestMessageRef.setValue(chatMassage)
    }

}

class ChatMassage(val text: String, val id: String, val fromId: String, val toId: String, val timeStamp: Long) {
   constructor(): this("","","","",-1)
}

class ChatFromItem(val text: String, val user: User): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_text_from_row.text = text

        val currentUser = LatestActivity.currentUser
        val url = currentUser?.profileImageUrl
        val imageview = viewHolder.itemView.imageview_chat_from_row
        Picasso.get().load(url).into(imageview)
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String, val user: User): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_text_to_row.text = text

        val url = user.profileImageUrl
        val imageView = viewHolder.itemView.imageview_chat_to_row
        Picasso.get().load(url).into(imageView)
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}


















