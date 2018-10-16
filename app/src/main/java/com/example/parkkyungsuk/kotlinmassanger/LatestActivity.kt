package com.example.parkkyungsuk.kotlinmassanger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.IntentCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest.*
import kotlinx.android.synthetic.main.latest_messages_row.view.*

class LatestActivity : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
    }

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest)

        recyclerview_latest_activity.adapter = adapter

        //setupDummyData()
        listenForLatestMessages()

        fetchCurrentUser()

        verifyUserIsLoggedIn()


    }

    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid

        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val chatMassage = p0.getValue(ChatMassage::class.java) ?: return

                adapter.add(LatestMessageRow(chatMassage))
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMassage = p0.getValue(ChatMassage::class.java) ?: return

                adapter.add(LatestMessageRow(chatMassage))
            }

            override fun onCancelled(p0: DatabaseError) {
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

    class LatestMessageRow(val chatMassage: ChatMassage) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.message_textView_latest_messages.text = chatMassage.text

            val toUser = FirebaseDatabase.getInstance().getReference("/users/${chatMassage.toId}")
            toUser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                   val toUser = p0.getValue(User::class.java)
                    viewHolder.itemView.username_textView_latest_messages.text = toUser?.username
                    val uri = toUser?.profileImageUrl
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

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid

        if (uid == null) {
            //検証失敗時、登録画面に戻す
            showRegisterAcitivity()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                showRegisterAcitivity()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    //カスタマイズしたnav_menuを使う
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showRegisterAcitivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
