package com.example.job1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast

class registro : AppCompatActivity() {
    private var edtName: EditText? = null
    private var edtPass: EditText? = null
    private var edtLastname: EditText? = null
    private var edtPassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

    }

    fun onRegist(view: View) {

        var username = edtName!!.text.toString();
        var password = edtPass!!.text.toString();
        var lastname = edtLastname!!.text.toString();

        if (username.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(username, ProviderType.BASIC)
                    } else {
                        getToast(resources.getString(R.string.error_auth));
                    }
                }
        } else {
            getToast(resources.getString(R.string.error_login));
        }
    }
    private fun showHome(username: String, provider: ProviderType) {

        val homeIntent = Intent(this, LoginActivity::class.java).apply {
            putExtra("email", username)
            putExtra("provider", provider.toString())
        }

        startActivity(homeIntent)

        getToast(resources.getString(R.string.txt_welcome));
    }

    private fun getToast(message: String) {
        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_LONG
        ).show();
    }


    fun onReturn(botonReturn: View) {
        val intento = Intent(this, MainActivity::class.java)
        startActivity(intento)
        getToast("Home")
    }

}