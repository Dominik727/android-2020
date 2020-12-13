/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 13
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Dashboard.Rooms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.model.Room

class RoomDetailActivity : AppCompatActivity() {

    companion object {
        var listener: RoomDetailActivity.RoomnAddedListener? = null
        var room = Room()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_detail)
    }

    interface RoomnAddedListener {
        fun roomAdded()
    }
}
