package com.turtlebody.imagepicker.sample


import android.annotation.SuppressLint
import android.os.Bundle
import com.turtlebody.imagepicker.core.Constants
import com.turtlebody.imagepicker.core.ImagePicker
import com.turtlebody.imagepicker.core.PickerConfig
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.info

class ActivityHome : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initToolbar(toolbar)
        toolbar.title = "Select Option"


        initButton()
    }

    private fun initButton() {
        home_ll_btn_images.setOnClickListener {
            startMultiPicker(Constants.FileTypes.FILE_TYPE_IMAGE)
        }

        home_ll_btn_videos.setOnClickListener {
            startMultiPicker(Constants.FileTypes.FILE_TYPE_VIDEO)
        }

        home_ll_btn_sounds.setOnClickListener {
            startMultiPicker(Constants.FileTypes.FILE_TYPE_AUDIO)
        }
    }


    @SuppressLint("CheckResult")
    private fun startMultiPicker(fileType: Int) {
        ImagePicker.with(this, PickerConfig().setAllowMultiImages(true), fileType)
                .onResult()
                .subscribe({
                    info { "success: $it" }
                },{
                    info { "error: $it" }
                })
    }
}
