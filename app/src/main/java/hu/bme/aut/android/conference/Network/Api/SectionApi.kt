/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 11. 29
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Network.Api

import hu.bme.aut.android.conference.model.Section
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface SectionApi {

    @GET("sections/all")
    fun getSections(@Header("Authorization") token: String): Call<List<Section>>
}
