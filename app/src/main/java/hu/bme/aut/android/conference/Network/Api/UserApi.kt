/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 11. 29
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Network.Api

import hu.bme.aut.android.conference.model.User
import retrofit2.Call
import retrofit2.http.*

interface UserApi {

    @POST("users/signup")
    fun newUser(@Body user: User): Call<Void>

    @POST("/login")
    fun login(@Body user: User): Call<Void>

    @GET("users/{email}")
    fun getUser(@Header("Authorization") token: String, @Path("email") email: String): Call<User>
}
