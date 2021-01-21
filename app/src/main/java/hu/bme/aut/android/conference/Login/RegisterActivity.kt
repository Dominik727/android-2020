/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 11
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Login

import android.os.Bundle
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.EmailAuthProvider
import com.huawei.agconnect.auth.EmailUser
import com.huawei.agconnect.auth.VerifyCodeSettings
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.Network.UserNetworkManager
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.enum.userType
import hu.bme.aut.android.conference.extensions.validateNonEmpty
import hu.bme.aut.android.conference.model.User
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : BaseActivity() {

    private lateinit var firebaseAuth: AGConnectAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = AGConnectAuth.getInstance()

        btnToLogin.setOnClickListener { loginToClick() }
        btnRegister.setOnClickListener { registerClick() }

        btnVerification.setOnClickListener {
            sendVerificationCode()
        }
    }

    private fun sendVerificationCode() {
        val settings = VerifyCodeSettings.newBuilder()
            .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
            .sendInterval(30)
            .locale(Locale.ROOT)
            .build()
        val task = EmailAuthProvider.requestVerifyCode(etEmail.text.toString(), settings)
        task.addOnSuccessListener {
            toast(getString(R.string.sending_success))
        }.addOnFailureListener {
            toast(getString(R.string.sending_failed) + it.localizedMessage)
        }
    }

    private fun validateForm() = etEmail.validateNonEmpty() && etPassword.validateNonEmpty() &&
        etverifcation.validateNonEmpty()

    private fun loginToClick() {
        super.onBackPressed()
    }

    private fun registerClick() {
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        val emailUser = EmailUser.Builder()
            .setEmail(etEmail.text.toString())
            .setVerifyCode(etverifcation.text.toString())
            .setPassword(etPassword.text.toString())
            .build()
        AGConnectAuth.getInstance().createUser(emailUser).addOnSuccessListener {
            val user = User(
                null, etEmail.text.toString(), etPassword.text.toString(),
                etEmail.text.toString(), userType.USER,
                etPhone.text.toString(), false, ArrayList(), ArrayList()
            )

            if (etEmail.text.toString() == "suszterdominik@gmail.com" ||
                etEmail.text.toString() == "suszterd@edu.bme.hu"
            ) {
                user.role = userType.ADMIN
            }

            var attempt = 0

            UserNetworkManager.newUser(user).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        toast(getString(R.string.regist_successful))
                        loginToClick()
                        return
                    }
                    toast(getString(R.string.error_please_try_again))
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
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
        }.addOnFailureListener {
            hideProgressDialog()
            toast(it.localizedMessage)
        }
    }
}
