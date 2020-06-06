package com.appinfo.logindemo.ui.auth


interface AuthListner {
    fun onStarted()
    fun onSuccess(message: String)
    fun onFailure(message: String)

}