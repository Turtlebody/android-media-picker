package com.turtlebody.imagepicker.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.turtlebody.imagepicker.R
import com.turtlebody.imagepicker.base.ActivityBase
import com.turtlebody.imagepicker.core.Constants
import com.turtlebody.imagepicker.fragments.FolderListFragment
import com.turtlebody.imagepicker.fragments.ImageListFragment
import com.turtlebody.imagepicker.models.Image
import kotlinx.android.synthetic.main.toolbar.*
import java.io.Serializable


class ActivityMain : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        //initToolbar(R.drawable.ic_arrow_back_white_24dp, toolbar)
        toolbar.title = "Select Folder"
        startFolderListFragment()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_content)
        when (fragment) {
            is FolderListFragment -> finish()
            is ImageListFragment -> {
                super.onBackPressed()
                toolbar.title = "Select Folder"
                toolbar_txt_count.visibility = View.GONE
            }
            else -> super.onBackPressed()
        }

    }


    private fun startFolderListFragment(){
        toolbar.title = "Select Folder"
        toolbar_txt_count.visibility = View.GONE

        val fragment = FolderListFragment.newInstance(Constants.Fragment.FOLDER_LIST, Bundle())
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.frame_content, fragment, FolderListFragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()
    }

    fun startImageListFragment(folderId: String){
        toolbar.title = "Choose Images"
        toolbar_txt_count.visibility = View.VISIBLE

        val bundle =  Bundle()
        bundle.putString("folderId",folderId)

        val fragment = ImageListFragment.newInstance(Constants.Fragment.IMAGE_LIST, bundle)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.frame_content, fragment, ImageListFragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()

    }

    fun updateCounter(counter: Int){
        toolbar_txt_count.text = "$counter"
    }

    fun sendBackData(list: MutableList<Image>){
        if(list.isNotEmpty()){
            val intent = Intent()
            intent.putExtra(Image.ARG_LIST,list as Serializable)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }
}
