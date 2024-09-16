package com.oqnd.asesoriapp.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.oqnd.asesoriapp.Fragments.ChatsFragment
import com.oqnd.asesoriapp.Fragments.UsersFragment
import com.oqnd.asesoriapp.Model.User
import com.oqnd.asesoriapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout : BottomNavigationView

    var reference : DatabaseReference ?= null
    var firebaseUser : FirebaseUser ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
        replaceFragment(ChatsFragment())
        obtData()

        tabLayout.setOnItemSelectedListener {
            when(it.itemId){
                R.id.btnChats -> replaceFragment(ChatsFragment())
                R.id.btnSearch -> replaceFragment(UsersFragment())
                R.id.btnProfile -> {
                    val intent = Intent(applicationContext, ProfileActivity::class.java)
                    startActivity(intent)
                }
                else ->{

                }
            }
            true
        }
    }

    private fun initComponents(){
        tabLayout = findViewById(R.id.tabLayout)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    fun obtData(){
        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user:User?=snapshot.getValue(User::class.java)
                    supportActionBar!!.title=user!!.getUsername()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.principal_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.logOut ->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@MainActivity, Start::class.java)
                Toast.makeText(applicationContext, "Se ha cerrado la sesión.", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}