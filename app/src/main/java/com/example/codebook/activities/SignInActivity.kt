package com.example.codebook.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.codebook.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth


        setContentView(R.layout.activity_sign_in)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()


        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnSignIn = findViewById(R.id.btn_sign_in)



        btnSignIn.setOnClickListener {
            signInRegisteredUser()
        }
    }

    private fun setupActionBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_sign_in_activity)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun signInRegisteredUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (validateForm(email, password)) {
            showProgressDialog(resources.getString(R.string.please_wait))

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()

                    if (task.isSuccessful) {
                        Log.d("SignInActivity", "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)


                        val intent = Intent(this, StartActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    else {
                        Log.w("SignInActivity", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            this,
                            resources.getString(R.string.auth_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }.addOnFailureListener { exception ->
                    Log.e("SignInActivity", "signInWithEmail:failure", exception)
                    Toast.makeText(
                        this,
                        "Authentication failed: ${exception.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    hideProgressDialog()
                    updateUI(null)
                }
        }
    }

    private fun updateUI(user: com.google.firebase.auth.FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, resources.getString(R.string.sign_in_successful), Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, resources.getString(R.string.sign_in_failed), Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorToast(resources.getString(R.string.enter_email))
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorToast(resources.getString(R.string.enter_password))
                false
            }
            else -> true
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
