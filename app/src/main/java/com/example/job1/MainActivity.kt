package com.example.job1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.os.Bundle
import android.widget.EditText
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private var editUsername: EditText? = null
    private var editPassword: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*setSupportActionBar(findViewById(R.id.my_toolbar))*/

        editUsername = findViewById(R.id.edtUsername)
        editPassword = findViewById(R.id.edtPassword)

    }


    fun onLogin(botonLogin: View) {
        if (editUsername!!.text.toString() == "lfa@email.com") {
            if (editPassword!!.text.toString() == "1234") {
                val intento = Intent(this, LoginActivity::class.java)
                startActivity(intento)
            }
        }
    }


    fun onRegister(botonRegister: View) {
        val intento = Intent(this, registro::class.java)
            startActivity(intento)
        }

    /*override fun onStart() {...}
    private fun session() {...}

    fun onLogin(view:View){
        var username = editUsername!!.text.toString();
        var password = editPassword!!.text.toString();

        if (username.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { Task<AuthResult>
                }*/
        }




