package com.turtlebody.imagepicker.sample

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.turtlebody.imagepicker.core.ImagePicker
import com.turtlebody.imagepicker.core.PickerConfig
import com.turtlebody.imagepicker.fragments.ImageListFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainActivity : AppCompatActivity(), AnkoLogger {

    companion object {
        val REQ_CODE = 800
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        id_pick_image.setOnClickListener {
//            val intent = Intent(this,ActivityMain::class.java)
//            startActivityForResult(intent, REQ_CODE)

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
        ImagePicker.with(this, PickerConfig())
                .onResult()
                .subscribe({
                    info { "success: $it" }
                },{
                    info { "error: $it" }
                })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode== REQ_CODE && resultCode==Activity.RESULT_OK){
            onResultFromActivityMain(data)
        }
        else
            super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onResultFromActivityMain(data: Intent?) {
        data?.let {
            val list = it.extras?.getSerializable(ImageListFragment.URI_LIST_KEY) as MutableList<Uri>
            info { "selected list: $list" }

//            var a = ""
//            for (i in list){
//                a = "$a\n${i.name}"
//
//                val b: Uri = Uri.parse(i.filePath)//Uri.fromFile()
//                val mimeType = contentResolver.getType(b)
//                info { "$b\nType: $mimeType\n\n" }
//
//            }
//            id_path.text = a

            info { "uri: $list" }

            id_image.setImageURI(list[0])
        }
    }

}
