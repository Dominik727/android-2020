/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 6
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Network.Api

import hu.bme.aut.android.conference.model.Lecture
import retrofit2.Call
import retrofit2.http.*

interface LectureApi {

    @GET("all")
    fun getAllRoom(@Header("Authorization") token: String): Call<List<Lecture>>

    @POST("new")
    fun newLecture(@Header("Authorization") token: String, @Body lecture: Lecture): Call<Void>

    @DELETE("delete/{id}")
    fun deleteLecture(@Header("Authorization") token: String, @Path("id") id: Long): Call<Void>
}
