package hu.bme.aut.android.conference

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.extensions.validateNonEmpty
import kotlinx.android.synthetic.main.activity_login.*

class Login : BaseActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener { registerClick() }
        btnLogin.setOnClickListener { loginClick() }
    }

    private fun validateForm() = etEmail.validateNonEmpty() && etPassword.validateNonEmpty()

    private fun registerClick() {
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        firebaseAuth
                .createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                .addOnSuccessListener { result ->
                    hideProgressDialog()

                    val firebaseUser = result.user
                    firebaseUser?.sendEmailVerification()
                    val profileChangeRequest = UserProfileChangeRequest.Builder()
                            .setDisplayName(firebaseUser?.email?.substringBefore('@'))
                            .build()
                    firebaseUser?.updateProfile(profileChangeRequest)

                    toast(getString(R.string.regist_successful))
                }
                .addOnFailureListener { exception ->
                    hideProgressDialog()

                    toast(exception.message)
                }
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
                        startActivity(Intent(this@Login, LoggedActivity::class.java))
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