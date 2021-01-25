/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 11
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Login

import android.content.Intent
import android.os.Bundle
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.EmailAuthProvider
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.Dashboard.HomeDashboard
import hu.bme.aut.android.conference.Network.UserNetworkManager
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.enum.userType
import hu.bme.aut.android.conference.extensions.validateNonEmpty
import hu.bme.aut.android.conference.model.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etEmail
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    private lateinit var agConnectAuth: AGConnectAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        agConnectAuth = AGConnectAuth.getInstance()

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

        if (agConnectAuth.currentUser != null) {
            loginSuccessful()
            return
        }

        showProgressDialog()
        val credential = EmailAuthProvider.credentialWithPassword(
            etEmail.text.toString(), etPassword.text.toString()
        )

        AGConnectAuth.getInstance().signIn(credential).addOnSuccessListener {
            loginSuccessful()
        }.addOnFailureListener {
            hideProgressDialog()

            toast(it.localizedMessage)
        }
    }

    private fun loginSuccessful() {
        var attempt = 0
        val user = User(
            null, etEmail.text.toString(), etPassword.text.toString(),
            etEmail.text.toString(), userType.USER, null, true, ArrayList(), ArrayList()
        )
        UserNetworkManager.login(
            user
        ).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val result = response.headers().toList().filter { x ->
                        x.first.startsWith("Authorization")
                    }
                    HomeDashboard.Auth_KEY = result.first().second
                    HomeDashboard.USER = user
                    toast(getString(R.string.login_success))
                    startActivity(Intent(this@LoginActivity, HomeDashboard::class.java))
                    finish()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                if (attempt > 3) {
                    Thread.sleep(5_000)
                    toast(getString(R.string.error_please_try_again))
                    hideProgressDialog()
                    return
                }
                attempt += 1
                UserNetworkManager.login(
                    user
                ).clone().enqueue(this)
            }
        })
    }
}
