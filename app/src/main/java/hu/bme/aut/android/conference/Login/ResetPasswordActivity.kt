/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 11
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Login

import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.R
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : BaseActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        mAuth = FirebaseAuth.getInstance()

        btnResetPassword.setOnClickListener {
            val email = edtResetEmail.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                toast(getString(R.string.writeEmail))
            } else {
                mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showBaseAlertDialog(
                                getString(R.string.forgetPassword),
                                getString(R.string.sendsuccess)
                            )
                        } else {
                            toast(getString(R.string.userNotFound))
                        }
                    }
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
