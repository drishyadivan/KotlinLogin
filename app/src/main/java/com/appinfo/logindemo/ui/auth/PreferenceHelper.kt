package com.appinfo.logindemo.ui.auth

import android.content.Context
import android.content.SharedPreferences


// For session management

class PreferenceHelper(private val context: Context) {

    private val INTRO = "intro"
    private val NAME = "name"
    private val SERVICEAREA = "servicearea"
    private val app_prefs: SharedPreferences

    init {
        app_prefs = context.getSharedPreferences(
            "shared",
            Context.MODE_PRIVATE
        )
    }

    fun putIsLogin(loginorout: Boolean) {
        val edit = app_prefs.edit()
        edit.putBoolean(INTRO, loginorout)
        edit.commit()
    }

    fun getIsLogin(): Boolean {
        return app_prefs.getBoolean(INTRO, false)
    }

    fun putName(loginorout: String) {
        val edit = app_prefs.edit()
        edit.putString(NAME, loginorout)
        edit.commit()
    }

    fun getNames(): String? {
        return app_prefs.getString(NAME, "")
    }

    fun putServiceArea(loginorout: String) {
        val edit = app_prefs.edit()
        edit.putString(SERVICEAREA, loginorout)
        edit.commit()
    }

    fun getServiceArea(): String? {
        return app_prefs.getString(SERVICEAREA, "")
    }

}