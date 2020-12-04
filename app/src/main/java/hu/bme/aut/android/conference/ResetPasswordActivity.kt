package hu.bme.aut.android.conference

import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.conference.Base.BaseActivity
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
                toast("írd be az email címed!")
            } else {
                mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showBaseAlertDialog("Jelszóemlékeztető", "Kiküldés sikeres")
                        } else {
                            toast("Nincs ilyen fiók!")
                        }
                    }
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
