package com.oqnd.asesoriapp.Activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.oqnd.asesoriapp.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        showWellcome()
    }

    fun showWellcome(){
        object : CountDownTimer(3500, 1000){
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
                val intent = Intent(applicationContext, Start::class.java)
                startActivity(intent)
                finish()
            }

        }.start()
    }
}