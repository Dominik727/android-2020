package hu.bme.aut.android.conference

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.Network.UserNetworkManager
import hu.bme.aut.android.conference.enum.userType
import hu.bme.aut.android.conference.extensions.validateNonEmpty
import hu.bme.aut.android.conference.model.User
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : BaseActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()

        btnToLogin.setOnClickListener { loginToClick() }
        btnRegister.setOnClickListener { registerClick() }
    }

    private fun validateForm() = etEmail.validateNonEmpty() && etPassword.validateNonEmpty()

    private fun loginToClick() {
        super.onBackPressed()
    }

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
                UserProfileChangeRequest.Builder()

                val user = User(
                    null, etEmail.text.toString(), etPassword.text.toString(),
                    etEmail.text.toString(), userType.USER,
                    etPhone.text.toString(), false
                )

                var attempt = 0

                UserNetworkManager.newUser(user).enqueue(object : Callback<Boolean> {
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        toast(getString(R.string.regist_successful))
                        loginToClick()
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        if (attempt > 3) {
                            Thread.sleep(1_000)
                            toast(getString(R.string.error_please_try_again))
                            hideProgressDialog()
                            return
                        }
                        attempt += 1
                        UserNetworkManager.newUser(user).clone().enqueue(this)
                    }
                })
            }
            .addOnFailureListener { exception ->
                hideProgressDialog()

                toast(exception.message)
            }
    }
}
