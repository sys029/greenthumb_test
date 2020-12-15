package com.example.greenthumb_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setProgressBar(progressBar)

        showProgressBar()

        progressStop.setOnClickListener(View.OnClickListener {

            hideProgressBar()
        })

        progressStart.setOnClickListener(View.OnClickListener {
            showProgressBar()
        })
    }
}
