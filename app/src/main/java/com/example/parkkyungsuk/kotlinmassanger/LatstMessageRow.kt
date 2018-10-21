package com.example.parkkyungsuk.kotlinmassanger

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_messages_row.view.*

class LatestMessageRow(val chatMassage: ChatMassage) : Item<ViewHolder>() {

    var chatPartnerUSer: User? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message_textView_latest_messages.text = chatMassage.text

        val chatParterId: String
        if (chatMassage.fromId == FirebaseAuth.getInstance().uid) {
            chatParterId = chatMassage.toId
        }
        else {
            chatParterId = chatMassage.fromId
        }

        val toUser = FirebaseDatabase.getInstance().getReference("/users/$chatParterId")
        toUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                chatPartnerUSer = p0.getValue(User::class.java)
                viewHolder.itemView.username_textView_latest_messages.text = chatPartnerUSer?.username
                val uri = chatPartnerUSer?.profileImageUrl
                Picasso.get().load(uri).into(viewHolder.itemView.imageView_latest_messages)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }
    override fun getLayout(): Int {
        return R.layout.latest_messages_row
    }


}