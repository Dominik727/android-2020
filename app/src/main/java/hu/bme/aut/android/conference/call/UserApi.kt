package hu.bme.aut.android.conference.call

import hu.bme.aut.android.conference.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class UserApi {

    companion object {
        const val ENDPOINT_URL = "https://konferencia.herokuapp.com/API/"
    }
}

interface REGISTERAPI {
    @POST("users/new")
    fun newUser(@Body user: User): Call<Boolean>
}

interface LOGINAPI {
    @POST("/users/{email}")
    fun login(@Path("email") email: String, @Body password: String): Call<User>
}
