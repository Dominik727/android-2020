package hu.bme.aut.android.conference

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.extensions.validateNonEmpty
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener { loginClick() }
        btnToRegister.setOnClickListener { registerToClick() }
        btnforget.setOnClickListener { resetPassword() }
    }

    private fun validateForm() = etEmail.validateNonEmpty() && etPassword.validateNonEmpty()

    private fun registerToClick() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun resetPassword() {
        val intent = Intent(this, ResetPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun loginClick() {
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        firebaseAuth
            .signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
            .addOnSuccessListener {
                hideProgressDialog()

                if (firebaseAuth.currentUser?.isEmailVerified!!) {
                    startActivity(Intent(this@LoginActivity, HomeDashboard::class.java))
                    finish()
                } else {
                    toast("Email cím nincs megerősítve!")
                }
            }
            .addOnFailureListener { exception ->
                hideProgressDialog()

                toast(exception.localizedMessage)
            }
    }
}
