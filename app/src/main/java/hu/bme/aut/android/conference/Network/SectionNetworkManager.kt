package hu.bme.aut.filmdatabase.network

import hu.bme.aut.android.conference.Network.Api.SectionApi
import hu.bme.aut.android.conference.model.Section
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SectionNetworkManager {
    private val retrofit: Retrofit
    private val SECTIONS_API: SectionApi

    private const val SERVICE_URL = "https://konferencia.herokuapp.com/API/"

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        SECTIONS_API = retrofit.create(SectionApi::class.java)
    }

    fun getSections(): Call<List<Section>> {
        return SECTIONS_API.getSections()
    }
}
