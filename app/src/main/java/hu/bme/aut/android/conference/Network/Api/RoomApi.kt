/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 6
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Network.Api

import hu.bme.aut.android.conference.model.Room
import retrofit2.Call
import retrofit2.http.*

interface RoomApi {

    @GET("all")
    fun getAllRoom(@Header("Authorization") token: String): Call<List<Room>>

    @DELETE("delete/{id}")
    fun deleteRoom(@Header("Authorization") token: String, @Path("id") id: Long): Call<Void>

    @POST("new")
    fun newRoom(@Header("Authorization") token: String, @Body room: Room): Call<Void>
}
