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
import com.oqnd.asesoriapp.R

class LoginActivity : AppCompatActivity() {
    
    private lateinit var txtEmail : EditText
    private lateinit var txtPass : EditText
    private lateinit var btnLogin : Button
    private lateinit var btnRegister : Button
    private lateinit var progress : ProgressBar
    
    private lateinit var auth:FirebaseAuth
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initVars()
        
        btnLogin.setOnClickListener { 
            ValidData()
        }

        btnRegister.setOnClickListener{
            val intent=Intent(this@LoginActivity, RegisterActivity::class.java)
            Toast.makeText(applicationContext, "Registro", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }

    private fun initVars(){
        txtEmail=findViewById(R.id.txtLEmail)
        txtPass=findViewById(R.id.txtLPass)
        btnLogin=findViewById(R.id.btnLogin)
        progress=findViewById(R.id.lProgressBar)
        auth=FirebaseAuth.getInstance()
        btnRegister=findViewById(R.id.btnGoRegister)
    }

    private fun ValidData() {
        val email = txtEmail.text.toString()
        val pass = txtPass.text.toString()
        
        if (email.isEmpty()){
            Toast.makeText(applicationContext, "Por favor, ingresa tu correo electrónico.", Toast.LENGTH_SHORT).show()
        }
        else if(pass.isEmpty()){
            Toast.makeText(applicationContext, "Por favor, ingresa tu contraseña.", Toast.LENGTH_SHORT).show()
        }
        else{
            login(email, pass)
        }
    }

    private fun login(email: String, pass: String) {
        btnLogin.isVisible = false
        progress.isVisible = true;

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                Toast.makeText(applicationContext, "Sesión iniciada.", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }
            else{
                btnLogin.isVisible = true
                progress.isVisible = false
                Toast.makeText(applicationContext, "Usuario o contraseña incorrectos.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
