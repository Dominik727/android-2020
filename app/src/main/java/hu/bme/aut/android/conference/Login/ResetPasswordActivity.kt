/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 11
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Login

import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import com.huawei.agconnect.auth.EmailAuthProvider
import com.huawei.agconnect.auth.VerifyCodeSettings
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.extensions.validateNonEmpty
import java.util.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_reset_password.etEmail

class ResetPasswordActivity : BaseActivity() {

    private lateinit var mAuth: FirebaseAuth

    private fun validateForm() = etEmail.validateNonEmpty() && etVerificationCode.validateNonEmpty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        btnVerificationSend.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (etEmail.text.isEmpty()) {
                toast(getString(R.string.writeEmail))
                return@setOnClickListener
            }

            val settings = VerifyCodeSettings.newBuilder()
                .action(VerifyCodeSettings.ACTION_RESET_PASSWORD)
                .sendInterval(30)
                .locale(Locale.ROOT)
                .build()
            val task = EmailAuthProvider.requestVerifyCode(email, settings)
            task.addOnSuccessListener {
                toast(getString(R.string.sendsuccess))
                newPasswordInputLayout.visibility = View.VISIBLE
            }
                .addOnFailureListener {
                    toast(getString(R.string.userNotFound))
                }
        }

        btnResetPassword.setOnClickListener {
            AGConnectAuth.getInstance().currentUser.updatePassword(
                etNewPassword.text.toString(),
                etVerificationCode.text.toString(), AGConnectAuthCredential.Email_Provider
            )
                .addOnSuccessListener {
                    showBaseAlertDialog(getString(R.string.PasswordChangeSuccess), null)
                    finish()
                }.addOnFailureListener {
                    showBaseAlertDialog(getString(R.string.passwordChangeError), null)
                }
        }
        btnBack.setOnClickListener {
            finish()
        }
    }
}
