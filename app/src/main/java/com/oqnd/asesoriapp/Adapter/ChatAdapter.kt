package com.oqnd.asesoriapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.oqnd.asesoriapp.Model.Chat
import com.oqnd.asesoriapp.R

class ChatAdapter (context: Context, chatList : List<Chat>, imageUrl : String) : RecyclerView.Adapter<ChatAdapter.ViewHolder?>() {

    private val context : Context
    private val chatList : List<Chat>
    private val imageUrl : String
    var firebaseUser : FirebaseUser = FirebaseAuth.getInstance().currentUser!!

    init {
        this.context = context
        this.chatList = chatList
        this.imageUrl = imageUrl
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var imgProfileImageMsg : ImageView ?= null
        var txtSeeMsg : TextView ?= null
        var imgSendLeft : ImageView ?= null
        var txtSeen : TextView ?= null

        var imgSendRight : ImageView ?= null

        init {
            imgProfileImageMsg = itemView.findViewById(R.id.imgProfileImageMsg)
            txtSeeMsg = itemView.findViewById(R.id.txtSeeMsg)
            imgSendLeft = itemView.findViewById(R.id.imgSendLeft)
            txtSeen = itemView.findViewById(R.id.txtSeen)
            imgSendRight = itemView.findViewById(R.id.imgSendRight)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return if (position==1){
            val view : View = LayoutInflater.from(context).inflate(com.oqnd.asesoriapp.R.layout.item_message_right, parent, false)
            ViewHolder(view)
        }else{
            val view : View = LayoutInflater.from(context).inflate(com.oqnd.asesoriapp.R.layout.item_message_left, parent, false)
            ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat : Chat = chatList[position]
        Glide.with(context).load(imageUrl).placeholder(R.drawable.ic_person).into(holder.imgProfileImageMsg!!)

        if(chat.getMsg().equals("Se ha enviado la imagen") && !chat.getUrl().equals("")){
            if(chat.getTransmitter().equals(firebaseUser.uid)){
                holder.txtSeeMsg!!.visibility = View.GONE
                holder.imgSendRight!!.visibility = View.VISIBLE
                Glide.with(context).load(chat.getUrl()).placeholder(R.drawable.ic_gallery).into(holder.imgSendRight!!)
            }
            else if(!chat.getTransmitter().equals(firebaseUser.uid)){
                holder.txtSeeMsg!!.visibility = View.GONE
                holder.imgSendLeft!!.visibility = View.VISIBLE
                Glide.with(context).load(chat.getUrl()).placeholder(R.drawable.ic_gallery).into(holder.imgSendLeft!!)
            }
        }
        else{
            holder.txtSeeMsg!!.text = chat.getMsg()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].getTransmitter().equals(firebaseUser.uid)){
            1
        }else{
            0
        }
    }
}