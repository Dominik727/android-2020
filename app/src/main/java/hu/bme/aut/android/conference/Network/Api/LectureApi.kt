/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 6
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Network.Api

import hu.bme.aut.android.conference.model.Lecture
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface LectureApi {

    @GET("all")
    fun getAllRoom(@Header("Authorization") token: String): Call<List<Lecture>>
}
