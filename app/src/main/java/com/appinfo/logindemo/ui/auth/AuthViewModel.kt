package com.appinfo.logindemo.ui.auth

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.lifecycle.ViewModel
import com.appinfo.logindemo.data.repositories.UserRepository
import com.appinfo.logindemo.util.Coroutines
import okhttp3.Headers

//a link between Model and View.
// Will interact with repository to perform login action

class AuthViewModel : ViewModel() {
    var email: String? = null
    var password: String? = null
    var authListner: AuthListner? = null
    private val TAG = "AuthViewModel"



    fun onBtnLoginClick(view: View) {



        if (email.isNullOrEmpty()) {

            authListner?.onFailure("Please enter email")
        } else if (!isValidEmail(email.toString())) {

            authListner?.onFailure("Invalid email")
        } else if (password.isNullOrEmpty()) {

            authListner?.onFailure("Please enter password")
        } else {
            authListner?.onStarted()


            LoginActivity.user=email!!
            Coroutines.main {
                val response =
                    UserRepository().userLogin(
                        email!!, password!!
                    )
                if (response.isSuccessful) {
                    val headerList: Headers = response.headers()
                    for (header in headerList) {
                        Log.d(TAG, header.first + " " + header.second)
              }
                    authListner?.onSuccess("Login Successful ${response.body()}")
                } else {
                    authListner?.onFailure("Error code ${response.code()} ")
                }
            }
        }
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches())
    }


    fun onBtnLogoutClick(view: View) {
        authListner?.onSuccess("Logout ")

    }


}