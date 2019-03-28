package com.turtlebody.imagepicker.sample

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.turtlebody.imagepicker.activities.ActivityMain
import com.turtlebody.imagepicker.models.Image
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainActivity : AppCompatActivity(), AnkoLogger {

    companion object {
        val REQ_CODE = 800
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val intent = Intent(this,ActivityMain::class.java)
        startActivityForResult(intent, REQ_CODE)
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
            val a = it.extras?.getSerializable(Image.ARG_LIST) as MutableList<Image>
            info { "selected list: $a" }
        }
    }

}
