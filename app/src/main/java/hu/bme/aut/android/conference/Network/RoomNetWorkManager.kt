/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 6
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Network

import com.google.gson.GsonBuilder
import hu.bme.aut.android.conference.Network.Api.RoomApi
import hu.bme.aut.android.conference.model.Room
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RoomNetWorkManager {
    private val retrofit: Retrofit
    private val ROOM_API: RoomApi

    private const val SERVICE_URL = "https://konferencia.herokuapp.com/API/rooms/"

    init {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        ROOM_API = retrofit.create(RoomApi::class.java)
    }

    fun getAllRoom(token: String): Call<List<Room>> {
        return ROOM_API.getAllRoom(token)
    }
}
