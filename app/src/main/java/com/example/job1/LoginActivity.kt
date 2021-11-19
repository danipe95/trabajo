package com.example.job1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000)
        setTheme(R.style.Theme_Job1)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onProductos(botonProductos: View) {
        val intento = Intent(this, products::class.java)
        startActivity(intento)
    }
    fun onLogout(botonSalir: View) {
        val intento = Intent(this, MainActivity::class.java)
        startActivity(intento)
    }
}