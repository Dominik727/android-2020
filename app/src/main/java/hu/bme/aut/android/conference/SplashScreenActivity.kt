/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 11
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat.postDelayed

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splas_screen)

        Handler(mainLooper).postDelayed(
            {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            },
            0
        )
    }
}
