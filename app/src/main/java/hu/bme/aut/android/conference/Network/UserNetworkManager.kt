package hu.bme.aut.filmdatabase.network

import hu.bme.aut.android.conference.Network.Api.UserApi
import hu.bme.aut.android.conference.Network.Api.SectionApi
import hu.bme.aut.android.conference.model.Section
import hu.bme.aut.android.conference.model.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body

object UserNetworkManager {
    private val retrofit: Retrofit
    private val USER_API: UserApi

    private const val SERVICE_URL = "https://konferencia.herokuapp.com/API/"

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        USER_API = retrofit.create(UserApi::class.java)
    }

    fun newUser(user: User): Call<Boolean> {
        return USER_API.newUser(user)
    }

    fun login(email: String, password: String): Call<User> {
        return  USER_API.login(email, password)
    }


}
