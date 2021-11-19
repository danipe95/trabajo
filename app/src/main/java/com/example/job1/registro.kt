package com.example.job1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class registro : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

    }
    fun onRegister(botonRegister: View) {
        val intento = Intent(this, registro::class.java)
        startActivity(intento)


    }
}