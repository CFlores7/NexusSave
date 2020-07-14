package com.royallock.nexussave

/*
    This file is part of Nexus$ave.

    Nexus$ave is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Nexus$ave is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Nexus$ave.  If not, see <https://www.gnu.org/licenses/>.
 */

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
