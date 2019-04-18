package com.greentoad.turtlebody.mediapicker.sample.test

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.greentoad.turtlebody.mediapicker.sample.R
import com.greentoad.turtlebody.mediapicker.core.Constants
import com.greentoad.turtlebody.mediapicker.MediaPicker
import com.greentoad.turtlebody.mediapicker.core.PickerConfig
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.File


class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        id_pick_image.setOnClickListener {
            startSinglePicker()
        }

        id_pick_multi_image.setOnClickListener {
            startMultiPicker()
        }
    }

    @SuppressLint("CheckResult")
    private fun startMultiPicker() {
        MediaPicker.with(this, Constants.FileTypes.MEDIA_TYPE_AUDIO)
                .setConfig(PickerConfig().setAllowMultiImages(true).setShowConfirmationDialog(true))
                .setFileMissingListener(object : MediaPicker.MediaPickerImpl.OnMediaListener{
                    override fun onMissingFileWarning() {

                    }
                })
                .onResult()
                .subscribe({
                    info { "success: $it" }
                },{
                    info { "error: $it" }
                })
    }

    @SuppressLint("CheckResult")
    private fun startSinglePicker() {
        MediaPicker.with(this, Constants.FileTypes.MEDIA_TYPE_AUDIO)
                .onResult()
                .subscribe({
                    info { "success: $it" }
                    val f = File(it[0].path)
                    info { "success: size: ${f.length()}" }
                    id_image.setImageURI(it[0])
                },{
                    info { "error: $it" }
                })
    }

}
