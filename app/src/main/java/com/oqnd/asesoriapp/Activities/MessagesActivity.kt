package com.oqnd.asesoriapp.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.oqnd.asesoriapp.Adapter.ChatAdapter
import com.oqnd.asesoriapp.Model.Chat
import com.oqnd.asesoriapp.Model.User
import com.oqnd.asesoriapp.R
import com.oqnd.asesoriapp.databinding.ActivityMessagesBinding

class MessagesActivity : AppCompatActivity() {

    private lateinit var txtMsg : EditText
    private lateinit var ibtnSend : ImageButton
    private lateinit var ibtnAdd : ImageButton
    private lateinit var imgProfile : ImageView
    private lateinit var txtUserName : TextView
    lateinit var rvChats : RecyclerView
    var chatAdapter : ChatAdapter ?= null
    var chatList : List<Chat> ?= null
    var uidUserSelected : String = ""
    var firebaseUser : FirebaseUser ?= null
    private var imagenUri : Uri ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        initVars()
        getUid()
        readInfo()

        ibtnAdd.setOnClickListener {
            openGallery()
        }

        ibtnSend.setOnClickListener {
            val msg = txtMsg.text.toString()
            if (msg.isEmpty()){
                Toast.makeText(applicationContext, "¡Ingresa un mensaje!", Toast.LENGTH_SHORT).show()
            }else{
                sendMsg(firebaseUser!!.uid, uidUserSelected, msg)
                txtMsg.setText("")
            }
        }
    }

    private fun initVars(){
        txtMsg = findViewById(R.id.txtMsg)
        ibtnSend = findViewById(R.id.imgbtnSend)
        ibtnAdd = findViewById(R.id.imgbtnAdd)
        imgProfile = findViewById(R.id.imgProfileChat)
        txtUserName = findViewById(R.id.txtUserChat)
        firebaseUser = FirebaseAuth.getInstance().currentUser

        rvChats = findViewById(R.id.rvChats)
        rvChats.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        rvChats.layoutManager = linearLayoutManager
    }

    private fun sendMsg(uidTransmitter : String, uidReceiveer : String, msg : String){
        val reference = FirebaseDatabase.getInstance().reference
        val msgKey = reference.push().key

        val infoMsg = HashMap<String, Any?> ()

        infoMsg["idMsg"] = msgKey
        infoMsg["transmitter"] = uidTransmitter
        infoMsg["receiver"] = uidReceiveer
        infoMsg["msg"] = msg
        infoMsg["url"] = ""
        infoMsg["seen"] = false
        reference.child("Chats").child(msgKey!!).setValue(infoMsg).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val listMsgTransmitter = FirebaseDatabase.getInstance().reference.child("MsgList")
                    .child(firebaseUser!!.uid)
                    .child(uidUserSelected)

                listMsgTransmitter.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(!snapshot.exists()){
                            listMsgTransmitter.child("uid").setValue(uidUserSelected)
                        }

                        val listMsgReceiver = FirebaseDatabase.getInstance().reference.child("MsgList")
                            .child(uidUserSelected)
                            .child(firebaseUser!!.uid)
                        listMsgReceiver.child("uid").setValue(firebaseUser!!.uid)
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
            }
        }
    }

    private fun getUid(){
        intent = intent
        uidUserSelected=intent.getStringExtra("uidUser").toString()
    }

    private fun readInfo(){
        val reference = FirebaseDatabase.getInstance().reference.child("Users").child(uidUserSelected)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user : User? = snapshot.getValue(User::class.java)
                txtUserName.text = user!!.getUsername()
                Glide.with(applicationContext).load(user.getImage()).placeholder(R.drawable.ic_person_white).into(imgProfile)

                getMsg(firebaseUser!!.uid, uidUserSelected, user.getImage())
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun getMsg(transmitterUid: String, receiverUid: String, image: String?) {
        chatList = ArrayList()
        val reference = FirebaseDatabase.getInstance().reference.child("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (chatList as ArrayList<Chat>).clear()
                for (sn in snapshot.children){
                    val chat = sn.getValue(Chat::class.java)

                    if(chat!!.getReceiver().equals(transmitterUid) && chat.getTransmitter().equals(receiverUid)
                        || chat.getReceiver().equals(receiverUid) && chat.getTransmitter().equals(transmitterUid)){
                        (chatList as ArrayList<Chat>).add(chat)
                    }

                    chatAdapter = ChatAdapter(this@MessagesActivity, (chatList as ArrayList<Chat>), image!!)
                    rvChats.adapter = chatAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryARL.launch(intent)
    }

    private val galleryARL = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                imagenUri = data!!.data

                val loadImage = ProgressDialog(this@MessagesActivity)
                loadImage.setMessage("Por favor, espere. Su imagen se esta cargando")
                loadImage.setCanceledOnTouchOutside(false)
                loadImage.show()

                val folderImgs = FirebaseStorage.getInstance().reference.child("Images from messages")
                val reference = FirebaseDatabase.getInstance().reference
                val idMessage = reference.push().key
                val nameImg = folderImgs.child("$idMessage.jpg")

                val uploadTask : StorageTask<*>
                uploadTask = nameImg.putFile(imagenUri!!)
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful){
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation nameImg.downloadUrl
                }).addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        loadImage.dismiss()
                        val downloadUrl = task.result
                        val url = downloadUrl.toString()

                        val infoMsgImg = HashMap<String, Any?>()
                        infoMsgImg["idMsg"] = idMessage
                        infoMsgImg["transmitter"] = firebaseUser!!.uid
                        infoMsgImg["receiver"] = uidUserSelected
                        infoMsgImg["msg"] = "Se ha enviado la imagen"
                        infoMsgImg["url"] = url
                        infoMsgImg["seen"] = false

                        reference.child("Chats").child(idMessage!!).setValue(infoMsgImg).addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                val listMsgTransmitter = FirebaseDatabase.getInstance().reference.child("MsgList")
                                    .child(firebaseUser!!.uid)
                                    .child(uidUserSelected)

                                listMsgTransmitter.addListenerForSingleValueEvent(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if(!snapshot.exists()){
                                            listMsgTransmitter.child("uid").setValue(uidUserSelected)
                                        }

                                        val listMsgReceiver = FirebaseDatabase.getInstance().reference.child("MsgList")
                                            .child(uidUserSelected)
                                            .child(firebaseUser!!.uid)
                                        listMsgReceiver.child("uid").setValue(firebaseUser!!.uid)
                                    }

                                    override fun onCancelled(error: DatabaseError) {

                                    }

                                })
                            }
                        }
                        Toast.makeText(applicationContext, "La imagen se ha enviado con éxito.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(applicationContext, "Cancelado.", Toast.LENGTH_SHORT).show()
            }
        }
    )
}