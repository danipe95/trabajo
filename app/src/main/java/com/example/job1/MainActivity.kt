package com.example.job1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider



enum class ProviderType {
    BASIC,
    GOOGLE,
    FACEBOOK
}

class MainActivity : AppCompatActivity() {
    private var edtUsername: EditText? = null
    private var edtPassword: EditText? = null

    private val GOOGLE_SING_IN = 54321
    private val callbackManager = CallbackManager.Factory.create()


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
//Boton Login

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
//Boton register
fun onRegister(botonRegister: View) {
    val intento = Intent(this, registro::class.java)
    startActivity(intento)
    getToast("Registro")


}


//Mostrar
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
//Login Google
    fun googleLogin(view: View) {

        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resources.getString(R.string.default_web_client_id2))
            .requestEmail().build()

        val googleClient = GoogleSignIn.getClient(this, googleConf)
        googleClient.signOut()
        startActivityForResult(googleClient.signInIntent, GOOGLE_SING_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SING_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credencial = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credencial)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                showHome(account.email ?: "", ProviderType.GOOGLE)
                            } else {
                                getToast(resources.getString(R.string.error_auth));
                            }
                        }
                }
            } catch (e: ApiException) {
                getToast(resources.getString(R.string.error_auth));
            }


        }
    }
//Login Facebook
    fun facebookLogin(view:View) {

        FacebookSdk.sdkInitialize(this);
        Log.d("AppLog", "key:" + FacebookSdk.getApplicationSignature(this)+"=");

        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))

        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {

            override fun onSuccess(result: LoginResult?) {

                result?.let {

                    val token = it.accessToken
                    val credencial = FacebookAuthProvider.getCredential(token.token)

                    FirebaseAuth.getInstance().signInWithCredential(credencial)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                showHome(it.result?.user?.email ?: "", ProviderType.FACEBOOK)
                            } else {
                                getToast(resources.getString(R.string.error_auth));
                            }
                        }

                }
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {
                getToast(resources.getString(R.string.error_auth));
            }

        })
    }
}



