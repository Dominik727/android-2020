package hu.bme.aut.android.conference.Network

import com.google.gson.GsonBuilder
import hu.bme.aut.android.conference.Network.Api.SectionApi
import hu.bme.aut.android.conference.model.Section
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SectionNetworkManager {
    private val retrofit: Retrofit
    private val SECTIONS_API: SectionApi

    private const val SERVICE_URL = "https://konferencia.herokuapp.com/API/sections/"

    init {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        SECTIONS_API = retrofit.create(SectionApi::class.java)
    }

    fun getSections(token: String): Call<List<Section>> {
        return SECTIONS_API.getSections(token)
    }

    fun newSection(token: String, section: Section): Call<Void> {
        return SECTIONS_API.newSection(token, section)
    }

    fun deleteSection(token: String, id: Long): Call<Void> {
        return SECTIONS_API.deleteSection(token, id)
    }
}
