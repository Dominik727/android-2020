/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 6
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Network.Api

import hu.bme.aut.android.conference.model.Section
import hu.bme.aut.android.conference.model.User
import retrofit2.Call
import retrofit2.http.*

interface SectionApi {

    @GET("all")
    fun getSections(@Header("Authorization") token: String): Call<List<Section>>

    @POST("new")
    fun newSection(@Header("Authorization") token: String, @Body section: Section): Call<Void>

    @DELETE("delete/{id}")
    fun deleteSection(@Header("Authorization") token: String, @Path("id") id: Long): Call<Void>

    @POST("adduser/{id}")
    fun newUserToSection(
        @Header("Authorization") token: String,
        @Body user: User,
        @Path("id") id: Long
    ): Call<Void>

    @POST("removeuser/{id}")
    fun removeUserFromSection(
        @Header("Authorization") token: String,
        @Body user: User,
        @Path("id") id: Long
    ): Call<Void>
}
