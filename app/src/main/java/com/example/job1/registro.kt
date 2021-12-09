package com.example.job1

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern
import android.view.View
import android.widget.CheckBox

class registro : AppCompatActivity() {
    private var edtName: EditText? = null
    private var edtPass: EditText? = null
    private var edtLastname: EditText? = null
    private var edtPassword: EditText? = null
    private var edtMobile: EditText? = null
    private var edtUsername: EditText? = null
    private var checkBox: CheckBox? = null

    var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtName = findViewById(R.id.edtName);
        edtLastname = findViewById(R.id.edtLastname);
        edtMobile = findViewById(R.id.edtMobile);
        checkBox = findViewById(R.id.checkBox);

        title = resources.getString(R.string.test_register)


    }

    fun onRegist(view: View) {

        var username = edtUsername!!.text.toString();
        var name = edtName!!.text.toString();
        var password = edtPass!!.text.toString();
        var lastname = edtLastname!!.text.toString();
        var mobile = edtMobile!!.text.toString();
        var terms = checkBox!!.isChecked;




        if (Validation(username, password, name, lastname, mobile, terms)) {

            // Save Auth

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        //Save User
                        db.collection("user").document(username).set(
                            hashMapOf(
                                "password" to password,
                                "name" to name,
                                "lastname" to lastname,
                                "mobile" to mobile,
                                "terms" to terms
                            )
                        )

                        showHome(username, ProviderType.BASIC)
                    } else {
                        getToast(resources.getString(R.string.error_auth));
                    }
                }
        } else {
            getToast(resources.getString(R.string.error_login));
        }
    }

    private fun Validation(
        username: String,
        password: String,
        name: String,
        lastname: String,
        mobile: String,
        terms: Boolean
    ): Boolean {


        //Reset

        edtUsername!!.setBackground(resources.getDrawable(R.drawable.customborderok))
        var edtUserNameL = findViewById<TextInputLayout>(R.id.edtUsernameL)
        edtUserNameL!!.setHint(resources.getString(R.string.test_userExample))

        edtPassword!!.setBackground(resources.getDrawable(R.drawable.customborderok))
        var edtPasswordL = findViewById<TextInputLayout>(R.id.edtPasswordL)
        edtPasswordL!!.setHint(resources.getString(R.string.test_passwordExample))

        edtName!!.setBackground(resources.getDrawable(R.drawable.customborderok))
        var edtNameL = findViewById<TextInputLayout>(R.id.edtNameL)
        edtNameL!!.setHint(resources.getString(R.string.test_NameExample))

        edtLastname!!.setBackground(resources.getDrawable(R.drawable.customborderok))
        var edtLastNameL = findViewById<TextInputLayout>(R.id.edtLastNameL)
        edtLastNameL!!.setHint(resources.getString(R.string.test_LastNameExample))

        edtMobile!!.setBackground(resources.getDrawable(R.drawable.customborderok))
        var edtMobileL = findViewById<TextInputLayout>(R.id.edtMobileL)
        edtMobileL!!.setHint(resources.getString(R.string.test_MobileExample))

        checkBox!!.setBackground(resources.getDrawable(R.drawable.customborderok))

        //Regex
        val uppercase: Pattern = Pattern.compile("[A-Z]")
        val lowercase: Pattern = Pattern.compile("[a-z]")
        val digit: Pattern = Pattern.compile("[0-9]")
        val character: Pattern = Pattern.compile("[!#\$%&'*+/=?^_`{|}~-]")
        val email: Pattern =
            Pattern.compile("^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\$")


        //Validation
        var validation: Boolean = true;

        if (name.isEmpty()) {
            edtNameL!!.setHint(resources.getString(R.string.test_EmptyName))
            edtName!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
            validation = false
        }

        if (lastname.isEmpty()) {
            edtLastNameL!!.setHint(resources.getString(R.string.test_EmptyLastName))
            edtLastname!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
            validation = false
        }

        if (mobile.isEmpty()) {
            edtMobileL!!.setHint(resources.getString(R.string.test_EmptyMobile))
            edtMobile!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
            validation = false
        }

        if (!terms) {
            checkBox!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
            validation = false
        }

        //Validate Password
        if (password.isEmpty()) {
            edtPasswordL!!.setHint(resources.getString(R.string.test_EmptyPassword))
            edtPassword!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
            validation = false

        } else {
            if (password.length < 8) {
                edtPasswordL!!.setHint(resources.getString(R.string.test_minimum8))
                edtPassword!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
                validation = false

            } else {
                if (!lowercase.matcher(password).find()) {
                    edtPasswordL!!.setHint(resources.getString(R.string.test_lowercase))
                    edtPassword!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
                    validation = false

                } else {
                    if (!uppercase.matcher(password).find()) {
                        edtPasswordL!!.setHint(resources.getString(R.string.test_uppercase))
                        edtPassword!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
                        validation = false
                    } else {

                        if (!digit.matcher(password).find()) {
                            edtPasswordL!!.setHint(resources.getString(R.string.test_digit))
                            edtPassword!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
                            validation = false

                        } else {
                            if (!character.matcher(password).find()) {
                                edtPasswordL!!.setHint(resources.getString(R.string.test_character))
                                edtPassword!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
                                validation = false
                            }
                        }
                    }
                }
            }
        }


    //Validation Email
        if (username.isEmpty()) {
        edtUserNameL!!.setHint(resources.getString(R.string.test_EmptyMail))
        edtUsername!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
        validation=false
        }else{
        if (!email.matcher(username).find()){
            edtUserNameL!!.setHint(resources.getString(R.string.test_emailerror))
            edtUsername!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
            validation=false
            }
        }

        return validation;
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

    fun onTerms(view: android.view.View) {

        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.test_TermsLink))
            .setMessage(resources.getString(R.string.test_TermsMessage))
            .setPositiveButton(resources.getString(R.string.test_ok),positiveButton)
            .setNegativeButton(resources.getString(R.string.test_cancel),negativeButton)
            .create().show();

    }

    val positiveButton={ _: DialogInterface, _:Int->
        checkBox!!.setChecked(true);
    }

    val negativeButton={ _: DialogInterface, _:Int->
        checkBox!!.setChecked(false);
    }


}



