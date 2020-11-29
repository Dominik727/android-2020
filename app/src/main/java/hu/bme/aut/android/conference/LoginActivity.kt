package hu.bme.aut.android.conference

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.call.LOGINAPI
import hu.bme.aut.android.conference.call.UserApi
import hu.bme.aut.android.conference.extensions.validateNonEmpty
import hu.bme.aut.android.conference.model.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etEmail
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : BaseActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private val userApi: LOGINAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(UserApi.ENDPOINT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        this.userApi = retrofit.create(LOGINAPI::class.java)
    }

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

                    val loginCall = userApi.login(
                        etEmail.text.toString(),
                        etPassword.text.toString()
                    )

                    loginCall.enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if (response.isSuccessful) {
                                toast(getString(R.string.registration_success))
                                startActivity(Intent(this@LoginActivity, HomeDashboard::class.java))
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            if (attempt > 3) {
                                Thread.sleep(1_000)
                                toast(getString(R.string.error_please_try_again))
                                hideProgressDialog()
                                return
                            }
                            attempt += 1
                            loginCall?.clone()?.enqueue(this)
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
