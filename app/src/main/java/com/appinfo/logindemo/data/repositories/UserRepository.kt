package com.appinfo.logindemo.data.repositories

import com.appinfo.logindemo.data.network.MyApi
import com.appinfo.logindemo.data.network.responses.AuthResponse
import retrofit2.Response

//repository is responsible for handling the data.

class UserRepository {

   suspend fun userLogin(email : String,password: String): Response<AuthResponse> {

        return MyApi().userLogin(email,password)
    }


}