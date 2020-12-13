/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 13
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Dashboard.Rooms

import android.os.Bundle
import android.view.View
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.Dashboard.HomeDashboard
import hu.bme.aut.android.conference.Network.RoomNetWorkManager
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.model.Room
import kotlinx.android.synthetic.main.activity_room_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomDetailActivity : BaseActivity() {

    companion object {
        var listener: roomnAddedListener? = null
        var room = Room()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_detail)

        if (room.id != null) {
            disableFields()
        } else {
            this.title = getString(R.string.addRoomTitle)
        }
        initfab()
    }

    private fun initfab() {
        save_room_fab.setOnClickListener {
            room.city = RoomCityEdittext.text.toString()
            room.address = RoomAddressEdittext.text.toString()
            room.capacity = editTextRoomCapacity.text.toString().toInt()
            room.zipCode = RoomZipcodeEditText.text.toString().toInt()
            room.name = RoomNameEditText.text.toString()
            HomeDashboard.Auth_KEY?.let { token ->
                RoomNetWorkManager.newRoom(token, room).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            toast(getString(R.string.Section_saved))
                            listener?.roomAdded()
                            onBackPressed()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        toast(getString(R.string.no))
                    }
                })
            }
        }
    }

    private fun disableFields() {
        this.title =
            getString(R.string.Room_detail_Title) + room.name
        save_room_fab.visibility = View.GONE
        RoomNameEditText.setText(room.name)
        RoomNameEditText.isEnabled = false
        editTextRoomCapacity.setText(room.capacity.toString())
        editTextRoomCapacity.isEnabled = false
        RoomZipcodeEditText.setText(room.zipCode.toString())
        RoomZipcodeEditText.isEnabled = false
        RoomAddressEdittext.setText(room.address)
        RoomAddressEdittext.isEnabled = false
        RoomCityEdittext.setText(room.city)
        RoomCityEdittext.isEnabled = false
    }

    interface roomnAddedListener {
        fun roomAdded()
    }
}
