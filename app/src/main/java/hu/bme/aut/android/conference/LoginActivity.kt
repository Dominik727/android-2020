package hu.bme.aut.android.conference

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.extensions.validateNonEmpty
import hu.bme.aut.android.conference.model.User
import hu.bme.aut.filmdatabase.network.UserNetworkManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etEmail
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

                    var attempt = 0

                    UserNetworkManager.login(
                        etEmail.text.toString(),
                        etPassword.text.toString()
                    ).enqueue(object : Callback<User> {
                        /**
                         * Invoked for a received HTTP response.
                         *
                         *
                         * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
                         * Call [Response.isSuccessful] to determine if the response indicates success.
                         */
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            toast(getString(R.string.login_success))
                            startActivity(Intent(this@LoginActivity, HomeDashboard::class.java))
                            finish()
                        }

                        /**
                         * Invoked when a network exception occurred talking to the server or when an unexpected
                         * exception occurred creating the request or processing the response.
                         */
                        override fun onFailure(call: Call<User>, t: Throwable) {
                            if (attempt > 3) {
                                Thread.sleep(1_000)
                                toast(getString(R.string.error_please_try_again))
                                hideProgressDialog()
                                return
                            }
                            attempt += 1
                            UserNetworkManager.login(
                                etEmail.text.toString(),
                                etPassword.text.toString()
                            ).clone().enqueue(this)
                        }
                    })
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
