package com.appinfo.logindemo.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.appinfo.logindemo.R
import com.appinfo.logindemo.databinding.ActivityMainBinding
import com.appinfo.logindemo.ui.auth.AuthListner
import com.appinfo.logindemo.ui.auth.AuthViewModel
import com.appinfo.logindemo.ui.auth.LoginActivity
import com.appinfo.logindemo.ui.auth.PreferenceHelper

class MainActivity : AppCompatActivity(), AuthListner {

    private var preferenceHelper: PreferenceHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)
        preferenceHelper = PreferenceHelper(this)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        val viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.authListner = this
    }

    override fun onStarted() {

    }

    override fun onSuccess(message: String) {
        preferenceHelper!!.putIsLogin(false)
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        this.finish()
    }

    override fun onFailure(message: String) {

    }


}
