/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 11. 29
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Network.Api

import hu.bme.aut.android.conference.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @POST("users/new")
    fun newUser(@Body user: User): Call<Boolean>

    @POST("/login")
    fun login(@Body user: User): Call<Void>
}
