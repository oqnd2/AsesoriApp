package com.oqnd.asesoriapp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.oqnd.asesoriapp.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var txtUserName:EditText
    private lateinit var txtEmail:EditText
    private lateinit var txtPass:EditText
    private lateinit var txtRpass:EditText
    private lateinit var btnRegister:Button
    private lateinit var progressBar: ProgressBar

    private lateinit var auth:FirebaseAuth
    private lateinit var reference:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initVars()

        btnRegister.setOnClickListener {
            ValidData()
        }
    }

    private fun initVars(){
        txtUserName = findViewById(R.id.txtUserName)
        txtEmail = findViewById(R.id.txtEmail)
        txtPass = findViewById(R.id.txtPass)
        txtRpass = findViewById(R.id.txtRpass)
        btnRegister = findViewById(R.id.btnRegister)
        progressBar = findViewById(R.id.rProgressBar)
        auth = FirebaseAuth.getInstance()
    }


    private fun ValidData() {
        val strUserName : String = txtUserName.text.toString()
        val strEmail : String = txtEmail.text.toString()
        val strPassword : String = txtPass.text.toString()
        val strPassword2 : String = txtRpass.text.toString()

        if (strUserName.isEmpty()){
            Toast.makeText(applicationContext, "Por favor, ingresa un numbre de usuario.", Toast.LENGTH_SHORT).show()
        }
        else if (strEmail.isEmpty()){
            Toast.makeText(applicationContext, "Por favor, ingresa un correo electrónico.", Toast.LENGTH_SHORT).show()
        }
        else if (strPassword.isEmpty()){
            Toast.makeText(applicationContext, "Por favor, ingresa una contraseña.", Toast.LENGTH_SHORT).show()
        }
        else if (strPassword2.isEmpty()){
            Toast.makeText(applicationContext, "Por favor, confirma tu contraseña.", Toast.LENGTH_SHORT).show()
        }
        else if (!strPassword.equals(strPassword2)){
            Toast.makeText(applicationContext, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show()
        }
        else{
            UserRegister(strEmail, strPassword)
        }
    }

    private fun UserRegister(email: String, password: String) {
        btnRegister.isVisible = false;
        progressBar.isVisible = true;

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                var uid : String = ""
                uid = auth.currentUser!!.uid
                reference=FirebaseDatabase.getInstance().reference.child("Users").child(uid)

                val hashmap = HashMap<String, Any>()
                val hashUserName:String = txtUserName.text.toString()
                val hashEmail:String = txtEmail.text.toString()

                hashmap["uid"]=uid
                hashmap["userName"]=hashUserName
                hashmap["email"]=hashEmail
                hashmap["image"]=""
                hashmap["search"]=hashUserName.lowercase()

                hashmap["name"] = ""
                hashmap["lastName"] = ""
                hashmap["age"] = ""
                hashmap["type"] = ""
                hashmap["status"] = "offline"
                hashmap["phone"] = ""

                reference.updateChildren(hashmap).addOnCompleteListener { task2->
                    if (task2.isSuccessful){
                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        Toast.makeText(applicationContext, "¡Te has registrado correctamente!", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    }
                }.addOnFailureListener { e ->
                    btnRegister.isVisible = true;
                    progressBar.isVisible = false;
                    Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                btnRegister.isVisible = true;
                progressBar.isVisible = false;
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            btnRegister.isVisible = true;
            progressBar.isVisible = false;
            Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}