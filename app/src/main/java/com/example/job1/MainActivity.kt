package com.example.job1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.os.Bundle
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private var edtUsername: EditText? = null
    private var edtPassword: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)

    }


    fun onLogin(botonLogin: View) {
        if (edtUsername!!.text.toString() == "lfa@email.com") {
            if (edtPassword!!.text.toString() == "1234") {
                val intento = Intent(this, LoginActivity::class.java)
                startActivity(intento)
            }
        }
    }

    fun onRegister(botonRegister: View) {
        val intento = Intent(this, registro::class.java)
        startActivity(intento)


    }
}