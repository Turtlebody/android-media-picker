package com.greentoad.turtlebody.mediapicker.sample

import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import org.jetbrains.anko.startActivity

class ActivityLauncher : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        setContentView(R.layout.activity_launcher)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delayedStartApp()
    }

    private fun delayedStartApp() {
        Handler().postDelayed({startActivity<ActivityHome>()
        finish()}, 1000)
    }

}
