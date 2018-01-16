package com.vinaysshenoy.oklognet.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.vinaysshenoy.oklognet.app.R.layout
import kotlinx.android.synthetic.main.activity_main.btn_execute
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

private const val LOG_TAG = "MainActivity"

@SuppressLint("LogNotTimber")
class MainActivity : AppCompatActivity() {

    private lateinit var okHttpClient: OkHttpClient

    private var requestCounter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        okHttpClient = (application as App).okHttpClient
        btn_execute.setOnClickListener {
            okHttpClient.newCall(Request.Builder().url(
                    "https://jsonplaceholder.typicode.com/posts/${nextPostNumber()}").get().build())
                    .enqueue(object : Callback {

                        override fun onFailure(call: Call?, e: IOException?) {
                            Log.d(LOG_TAG, "${call?.request()?.url()} failed")
                        }

                        override fun onResponse(call: Call?, response: Response?) {
                            Log.d(LOG_TAG, "${call?.request()?.url()} succeeded")
                        }
                    })
        }
    }

    private fun nextPostNumber(): Int {
        val counter = requestCounter
        requestCounter = (counter + 1) % 100
        if (requestCounter == 0) {
            requestCounter = 1
        }
        return counter
    }
}
