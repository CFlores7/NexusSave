package com.example.nexussave

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //Ocultando ActionBar
        this.supportActionBar?.hide()

        Handler().postDelayed({
            loggedUser()
        },3000)
    }

    private fun loggedUser(){
        var user = FirebaseAuth.getInstance().currentUser
        val intent1 = Intent(this, LoginActivity::class.java)
        if (user != null){
            val intent = Intent(this, ActivityMain::class.java)
            this.finish()
            startActivity(intent)
            Toast.makeText(this,"¡Bienvenido!", Toast.LENGTH_LONG).show()
        } else {
            this.finish()
            startActivity(intent1)
        }
    }
}
