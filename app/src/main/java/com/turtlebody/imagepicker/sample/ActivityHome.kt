package com.turtlebody.imagepicker.sample


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.turtlebody.mediapicker.core.Constants
import com.turtlebody.mediapicker.core.MediaPicker
import com.turtlebody.mediapicker.core.PickerConfig
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
            showAlert(Constants.FileTypes.FILE_TYPE_IMAGE)
        }

        home_ll_btn_videos.setOnClickListener {
            showAlert(Constants.FileTypes.FILE_TYPE_VIDEO)
        }

        home_ll_btn_sounds.setOnClickListener {
            showAlert(Constants.FileTypes.FILE_TYPE_AUDIO)
        }

        Glide.with(this)
                .load(R.drawable.pic_image)
                .into(home_iv_select_images)

    }

    private fun showAlert(fileType: Int){
        AlertDialog.Builder(this)
                //.setTitle("New User?")
                .setMessage("Type of file selection?")
                .setPositiveButton("Single") { dialog, which ->
                    startMediaPicker(fileType,false)
                }.setNegativeButton("Multiple") { dialog, which ->
                    startMediaPicker(fileType,true)
                }.show()
    }


    @SuppressLint("CheckResult")
    private fun startMediaPicker(fileType: Int, allowMultiple: Boolean) {
        MediaPicker.with(this, PickerConfig().setAllowMultiImages(allowMultiple).setShowDialog(true), fileType)
                .onResult()
                .subscribe({
                    info { "success: $it" }
                },{
                    info { "error: $it" }
                })
    }
}
