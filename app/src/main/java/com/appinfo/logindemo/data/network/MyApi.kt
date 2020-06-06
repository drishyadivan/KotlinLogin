package com.appinfo.logindemo.data.network

import com.appinfo.logindemo.data.network.responses.AuthResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

//Interface used for retrofit api calls


//Okhttp — For creating an HTTP request with all the proper headers.
//Retrofit — For making the request
//GSON — For parsing the JSON data


interface MyApi {

    @FormUrlEncoded
    @POST("login")
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Response<AuthResponse>

    companion object {
        operator fun invoke(): MyApi {

            val okHttpClient: OkHttpClient = OkHttpClient()
            return Retrofit.Builder()
                .client(okHttpClient)

                .baseUrl("https://api.simplifiedcoding.in/course-apis/mvvm/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

                .create(MyApi::class.java)


        }


    }
}