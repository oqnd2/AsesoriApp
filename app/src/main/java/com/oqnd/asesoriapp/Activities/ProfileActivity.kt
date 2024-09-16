package com.oqnd.asesoriapp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.oqnd.asesoriapp.Model.User
import com.oqnd.asesoriapp.R

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileImage : ImageView
    private lateinit var userName : TextView
    private lateinit var email : TextView
    private lateinit var txtName : EditText
    private lateinit var txtLastName : EditText
    private lateinit var txtPhone : EditText
    private lateinit var txtAge : EditText
    private lateinit var btnUpdate : Button
    private lateinit var editImage : ImageView

    var user : FirebaseUser ?= null
    var reference : DatabaseReference ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initVars()
        obtData()

        btnUpdate.setOnClickListener {
            updateData()
        }

        editImage.setOnClickListener{
            val intent = Intent(applicationContext, EditImageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initVars(){
        profileImage = findViewById(R.id.ProfileImage)
        userName = findViewById(R.id.profileUserName)
        email = findViewById(R.id.profileEmail)
        txtName = findViewById(R.id.profileName)
        txtLastName = findViewById(R.id.profileLastName)
        txtPhone = findViewById(R.id.profilePhone)
        txtAge = findViewById(R.id.profileAge)
        btnUpdate = findViewById(R.id.btnUpdate)
        editImage = findViewById(R.id.editImage)

        user = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().reference.child("Users").child(user!!.uid)
    }

    private fun obtData(){
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user : User ?= snapshot.getValue(User::class.java)
                    val strUserName = user!!.getUsername()
                    val strEmail = user.getEmail()
                    val strName = user.getName()
                    val strLastName = user.getLastname()
                    val strPhone = user.getPhone()
                    val strAge = user.getAge()

                    userName.text=strUserName
                    email.text=strEmail
                    txtName.setText(strName)
                    txtLastName.setText(strLastName)
                    txtPhone.setText(strPhone)
                    txtAge.setText(strAge)
                    Glide.with(applicationContext).load(user.getImage()).placeholder(R.drawable.ic_profile_page).into(profileImage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun updateData(){
        val strName = txtName.text.toString()
        val strLastName = txtLastName.text.toString()
        val strPhone = txtPhone.text.toString()
        val strAge = txtAge.text.toString()

        val hashmap = HashMap<String, Any>()

        hashmap["name"] = strName
        hashmap["lastName"] = strLastName
        hashmap["phone"] = strPhone
        hashmap["age"] = strAge

        reference!!.updateChildren(hashmap).addOnCompleteListener{task ->
            if (task.isSuccessful){
                Toast.makeText(applicationContext, "¡Tus datos han sido actualizados!", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}

