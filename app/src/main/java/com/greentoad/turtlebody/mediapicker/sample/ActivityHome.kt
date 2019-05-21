package com.greentoad.turtlebody.mediapicker.sample


import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.bumptech.glide.Glide
import com.greentoad.turtlebody.mediapicker.MediaPicker
import com.greentoad.turtlebody.mediapicker.core.MediaPickerConfig
import com.greentoad.turtlebody.mediapicker.sample.show_results.ActivityResults
import com.greentoad.turtlebody.mediapicker.ui.ActivityLibMain
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.info
import java.io.Serializable



class ActivityHome : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initToolbar(toolbar)
        toolbar.title = "Select Option"
        initButton()

        info { "change" }
    }

    private fun initButton() {
        home_ll_btn_images.setOnClickListener {
            showAlert(MediaPicker.MediaTypes.IMAGE)
        }

        home_ll_btn_videos.setOnClickListener {
            showAlert(MediaPicker.MediaTypes.VIDEO)
        }

        home_ll_btn_sounds.setOnClickListener {
            showAlert(MediaPicker.MediaTypes.AUDIO)
        }

        Glide.with(this)
                .load(R.drawable.pic_image)
                .into(home_iv_select_images)
    }

    private fun showAlert(fileType: Int){
        MaterialDialog(this).show {
            customView(R.layout.dialog_view,scrollable = true)
            this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val view = this.getCustomView()
            val singleBtn = view.findViewById<Button>(R.id.activity_home_single_select)
            val multiBtn = view.findViewById<Button>(R.id.activity_home_multi_select)

            singleBtn.setOnClickListener {
                this.dismiss()
                startMediaPicker(fileType,false)
            }
            multiBtn.setOnClickListener {
                this.dismiss()
                startMediaPicker(fileType,true)
            }
        }
    }


    @SuppressLint("CheckResult")
    private fun startMediaPicker(fileType: Int, allowMultiple: Boolean) {
        MediaPicker.with(this, fileType)
                .setConfig(MediaPickerConfig()
                        .setUriPermanentAccess(false)
                        .setAllowMultiSelection(allowMultiple).setShowConfirmationDialog(true)
                        .setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT))
                .setFileMissingListener(object : MediaPicker.MediaPickerImpl.OnMediaListener{
                    override fun onMissingFileWarning() {
                        Toast.makeText(this@ActivityHome,"some file is missing",Toast.LENGTH_LONG).show()
                    }
                })
                .onResult()
                .subscribe({
                    info { "success: $it" }
                    info { "list size: ${it.size}" }
                    startActivityShowResult(it)
                },{
                    info { "error: $it" }
                })
    }

    private fun startActivityShowResult(it: MutableList<Uri>?) {
        val intent = Intent(this,ActivityResults::class.java)
        intent.putExtra(ActivityLibMain.B_ARG_URI_LIST,it as Serializable)
        startActivity(intent)
    }



}
