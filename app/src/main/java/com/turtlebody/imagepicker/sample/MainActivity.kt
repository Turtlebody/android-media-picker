package com.turtlebody.imagepicker.sample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.turtlebody.imagepicker.core.ImagePicker
import com.turtlebody.imagepicker.core.PickerConfig
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
        ImagePicker.with(this, PickerConfig().setAllowMultiImages(true))
                .onResult()
                .subscribe({
                    info { "success: $it" }
                },{
                    info { "error: $it" }
                })
    }

    @SuppressLint("CheckResult")
    private fun startSinglePicker() {
        ImagePicker.with(this, PickerConfig().setShowDialog(true))
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
