/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 6
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Network

import com.google.gson.GsonBuilder
import hu.bme.aut.android.conference.Network.Api.LectureApi
import hu.bme.aut.android.conference.model.Lecture
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LectureNetWorkManager {
    private val retrofit: Retrofit
    private val LECTURE_API: LectureApi

    private const val SERVICE_URL = "https://konferencia.herokuapp.com/API/lectures/"

    init {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        LECTURE_API = retrofit.create(LectureApi::class.java)
    }

    fun getAllLecture(token: String): Call<List<Lecture>> {
        return LECTURE_API.getAllRoom(token)
    }
}
