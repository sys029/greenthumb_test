package com.example.greenthumb_test

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity(),ConnectivityReceiver.ConnectivityReceiverListener {


    private var mSnackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }


    private fun showMessage(isConnected: Boolean) {



        if (!isConnected) {

            mSnackBar = Snackbar.make(findViewById(R.id.rootLayout), "You are offline now.", Snackbar.LENGTH_LONG)
            mSnackBar?.show()
        } else {

            mSnackBar = Snackbar.make(findViewById(R.id.rootLayout), "You are online now.", Snackbar.LENGTH_LONG)
            mSnackBar?.show()
        }


    }

    override fun onResume() {
        super.onResume()

        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }


    //ProgressBar

    private var progressBar: ProgressBar? = null

    fun setProgressBar(bar: ProgressBar) {
        progressBar = bar
    }

    fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar?.visibility = View.INVISIBLE
    }


    public override fun onStop() {
        super.onStop()
        hideProgressBar()
    }
}