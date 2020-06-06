package com.appinfo.logindemo.ui.auth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.appinfo.logindemo.R
import com.appinfo.logindemo.databinding.ActivityLoginBinding
import com.appinfo.logindemo.ui.home.MainActivity
import com.appinfo.logindemo.util.LocationClass
import com.appinfo.logindemo.util.toast

class LoginActivity : AppCompatActivity(), AuthListner {


    private val PERMISSION_ID = 42

    private var mProgressDialog: ProgressDialog? = null
    private var preferenceHelper: PreferenceHelper? = null

    private var locationClass: LocationClass? = null


    companion object {
        var latitude: String = "0.0"
        var longitude: String = "0.0"
        var servicearea: String = "Not Available"
        var user: String = "NA"
    }

    @SuppressLint("ServiceCast")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceHelper = PreferenceHelper(this)

        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        val viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.authListner = this

        checkPermissions()

    }

    override fun onStarted() {
        showSimpleProgressDialog(this@LoginActivity, null, "Loading...", false)

    }

    override fun onSuccess(message: String) {
        removeSimpleProgressDialog()  //will remove progress dialog

        saveInfo();
        Intent(this, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }

        toast(message + "\nWelcome " + preferenceHelper!!.getNames() + "\nYour location is " + preferenceHelper!!.getServiceArea())
        //toast("Welcome "+preferenceHelper!!.getNames())
    }

    override fun onFailure(message: String) {
        removeSimpleProgressDialog()  //will remove progress dialog
        toast(message)
    }


    fun showSimpleProgressDialog(
        context: Context,
        title: String?,
        msg: String,
        isCancelable: Boolean
    ) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg)
                mProgressDialog!!.setCancelable(isCancelable)
            }
            if (!mProgressDialog!!.isShowing) {
                mProgressDialog!!.show()
            }

        } catch (ie: IllegalArgumentException) {
            ie.printStackTrace()
        } catch (re: RuntimeException) {
            re.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog!!.isShowing) {
                    mProgressDialog!!.dismiss()
                    mProgressDialog = null
                }
            }
        } catch (ie: IllegalArgumentException) {
            ie.printStackTrace()

        } catch (re: RuntimeException) {
            re.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun saveInfo() {
        preferenceHelper!!.putIsLogin(true)
        preferenceHelper!!.putName(user)
        preferenceHelper!!.putServiceArea(servicearea)


    }


    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            locationClass = LocationClass(this)
        } else {

                showPermissionDialogMain(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    PERMISSION_ID,
                    false
                )

            }



    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )

    }

    //@Override
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                locationClass = LocationClass(this)
            }
        }
    }

    private var permissionsDialog: Dialog? = null

    //show Dialog when permission is not enabled
    private fun showPermissionDialogMain(
        permission: String
        , requestCode: Int
        , isDontAskMeAgainChecked: Boolean
    ) {
        Log.d("permissionsDialog", "showPermissionDialogMain")
        try {
            if (permissionsDialog == null) {
                showPermissionDialog(permission, requestCode, isDontAskMeAgainChecked)
            } else if (!permissionsDialog!!.isShowing()) {
                showPermissionDialog(permission, requestCode, isDontAskMeAgainChecked)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()

        }
    }

    /**
     * Method to show dialog if location permission is not allowed to application by user.
     *
     * @param permission  type of permission (Location or Storage)
     * @param requestCode to differentiate the callback of permission in "onRequestPermissionsResult" method
     */
    private fun showPermissionDialog(
        permission: String
        , requestCode: Int
        , isDontAskMeAgainChecked: Boolean
    ) {
        Log.d("permissionsDialog", "showPermissionDialog")
        this.runOnUiThread {
            permissionsDialog =
                Dialog(this, android.R.style.Theme_Light_NoTitleBar)
            permissionsDialog!!.setCancelable(false)
            permissionsDialog!!.setContentView(R.layout.dialog_alert)
            val textHeader: TextView = permissionsDialog!!.findViewById(R.id.alert_header)
            val textMessage: TextView = permissionsDialog!!.findViewById(R.id.alert_message)
            textHeader.text = getString(R.string.app_name)
            if (permission == "android.permission.WRITE_EXTERNAL_STORAGE" || permission == "android.permission.READ_EXTERNAL_STORAGE") textMessage.text =
                getString(R.string.enable_storage) else textMessage.text =
                getString(R.string.enable_location)
            val okButton: Button = permissionsDialog!!.findViewById(R.id.alert_ok)
            val cancelButton: Button = permissionsDialog!!.findViewById(R.id.alert_cancel)
            okButton.setText(getString(com.appinfo.logindemo.R.string.enable))
            cancelButton.setVisibility(View.GONE)
            okButton.setOnClickListener { view ->
                permissionsDialog!!.dismiss()
                if (isDontAskMeAgainChecked) {
                    val myAppSettings = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + this.getPackageName())
                    )
                    myAppSettings.addCategory(Intent.CATEGORY_DEFAULT)
                    myAppSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    this.startActivityForResult(myAppSettings, requestCode)
                } else {
                    requestPermissions()

                }
            }

            permissionsDialog!!.show()
        }
    }
}