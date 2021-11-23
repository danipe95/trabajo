package com.example.job1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


enum class ProviderType {
    BASIC,
    GOOGLE,
    FACEBOOK
}

class MainActivity : AppCompatActivity() {
    private var edtUsername: EditText? = null
    private var edtPassword: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000)
        setTheme(R.style.Theme_Job1)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*setSupportActionBar(findViewById(R.id.my_toolbar))*/

        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()

        bundle.putString(
            resources.getString(R.string.fire_base),
            resources.getString(R.string.fire_base_message)
        )
        analytics.logEvent(resources.getString(R.string.mersh), bundle)

        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)

        title = resources.getString(R.string.txt_Login)

        session()

    }


    /*fun onLogin(botonLogin: View) {
        if (editUsername!!.text.toString() == "lfa@email.com") {
            if (editPassword!!.text.toString() == "1234") {
                val intento = Intent(this, LoginActivity::class.java)
                startActivity(intento)
            }
        }
    }
    */




    override fun onStart() {
        super.onStart()

        val prefs =
            getSharedPreferences(
                resources.getString(R.string.preds_file),
                Context.MODE_PRIVATE
            )
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if (email == null && provider == null) {
            var loginLayout = findViewById<LinearLayout>(R.id.loginLayout);
            loginLayout.visibility = View.VISIBLE
        }
    }


    private fun session() {
        val prefs =
            getSharedPreferences(
                resources.getString(R.string.preds_file),
                Context.MODE_PRIVATE
            )
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if (email != null && provider != null) {
            var loginLayout = findViewById<LinearLayout>(R.id.loginLayout);
            loginLayout.visibility = View.INVISIBLE

            showHome(email, ProviderType.valueOf(provider))
        }

    }


    fun onLogin(view: View) {
        var username = edtUsername!!.text.toString();
        var password = edtPassword!!.text.toString();

        if (username.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
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

    fun onRegister(botonRegister: View) {
        var username = edtUsername!!.text.toString();

        var password = edtPassword!!.text.toString();

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
}



