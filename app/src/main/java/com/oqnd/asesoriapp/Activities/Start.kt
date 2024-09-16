package com.oqnd.asesoriapp.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.oqnd.asesoriapp.R

class Start : AppCompatActivity() {

    private lateinit var btnLogin : Button
    private lateinit var btnLoginGoogle : Button

    var firebaseUser : FirebaseUser? = null
    private lateinit var auth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        btnLogin=findViewById(R.id.btnGoLogin)
        btnLoginGoogle=findViewById(R.id.btnGoogleLogin)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("305521047928-o8d6l1go7cj746qu7hfcg94ffkokaqu1.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        btnLogin.setOnClickListener{
            val intent=Intent(this@Start, LoginActivity::class.java)
            Toast.makeText(applicationContext, "Iniciar Sesión", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

        btnLoginGoogle.setOnClickListener {

        }
    }

    private fun checkLogin(){
        firebaseUser=FirebaseAuth.getInstance().currentUser
        if(firebaseUser!=null){
            val intent = Intent(this@Start, MainActivity::class.java)
            Toast.makeText(applicationContext, "¡La sesión está activa!", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        checkLogin()
        super.onStart()
    }
}
