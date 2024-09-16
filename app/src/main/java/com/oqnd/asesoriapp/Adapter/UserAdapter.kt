package com.oqnd.asesoriapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oqnd.asesoriapp.Activities.MessagesActivity
import com.oqnd.asesoriapp.Model.User
import com.oqnd.asesoriapp.R

class UserAdapter ( context : Context, userList : List<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder?>(){

    private val context : Context
    private val userList : List<User>

    init {
        this.context = context
        this.userList = userList
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var userName : TextView
        var userEmail : TextView
        var userImage : ImageView

        init {
            userName = itemView.findViewById(R.id.txtItemUser)
            userEmail = itemView.findViewById(R.id.txtItemEmail)
            userImage = itemView.findViewById(R.id.ImageItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user : User = userList[position]
        holder.userName.text = user.getUsername()
        holder.userEmail.text = user.getEmail()
        Glide.with(context).load(user.getImage()).placeholder(R.drawable.ic_user).into(holder.userImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, MessagesActivity::class.java)
            intent.putExtra("uidUser", user.getUid())
            context.startActivity(intent)
        }
    }
}
