package com.example.job1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class products : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
    }

    fun onCats(botonPCats: View) {
        val intento = Intent(this, cats::class.java)
        startActivity(intento)
    }
    fun onDogs(botonPDogs: View) {
        val intento = Intent(this, dogs::class.java)
        startActivity(intento)

    }
}