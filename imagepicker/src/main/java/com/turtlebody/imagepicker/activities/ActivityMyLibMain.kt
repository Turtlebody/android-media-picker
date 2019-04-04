package com.turtlebody.imagepicker.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.turtlebody.imagepicker.R
import com.turtlebody.imagepicker.base.ActivityMyBase
import com.turtlebody.imagepicker.core.Constants
import com.turtlebody.imagepicker.core.FileManager
import com.turtlebody.imagepicker.core.ImagePicker
import com.turtlebody.imagepicker.core.PickerConfig
import com.turtlebody.imagepicker.fragments.FolderListFragment
import com.turtlebody.imagepicker.fragments.FileListFragment
import com.turtlebody.imagepicker.models.ImageVideoFolder
import com.wangsun.custompicker.api.FilePicker
import com.wangsun.custompicker.api.Picker
import com.wangsun.custompicker.api.callbacks.FilePickerCallback
import com.wangsun.custompicker.api.entity.ChosenFile
import com.wangsun.custompicker.utils.MimeUtils
import kotlinx.android.synthetic.main.lib_toolbar.*
import java.io.File
import java.io.Serializable




class ActivityMyLibMain : ActivityMyBase() {


    private lateinit var mFilePicker: FilePicker
    private var mFileType: Int = Constants.FileTypes.FILE_TYPE_IMAGE
    private lateinit var mMenuItem: MenuItem
    private lateinit var mPickerConfig: PickerConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lib_main)

        initToolbar(R.drawable.ic_arrow_back_black_24dp,my_toolbar)
        my_toolbar.title = "Select ImageVideoFolder"

        mFilePicker = FilePicker(this)

        if(intent.extras!=null){
            mPickerConfig = intent.getSerializableExtra(PickerConfig.ARG_BUNDLE) as PickerConfig
            mFileType = intent.getIntExtra(ImagePicker.FILE_TYPE,Constants.FileTypes.FILE_TYPE_IMAGE)
        }

    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_content)
        when (fragment) {
            is FolderListFragment -> finish()
            is FileListFragment -> {
                super.onBackPressed()
                my_toolbar.title = "Select Folder"
                toolbar_txt_count.visibility = View.GONE
                mMenuItem.isVisible = true
            }
            else -> super.onBackPressed()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        mMenuItem = menu.getItem(0)
        startFolderListFragment()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_open ->{
                startImagePicker()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    @SuppressLint("CheckResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == Picker.PICK_FILE){
            mFilePicker.submit(data)
            println("filePath: $data")
        }
        else
            super.onActivityResult(requestCode, resultCode, data)
    }


    private fun startFolderListFragment(){
        my_toolbar.title = "Select Folder"
        toolbar_txt_count.visibility = View.GONE
        mMenuItem.isVisible = true

        val bundle =  Bundle()
        bundle.putInt(ImagePicker.FILE_TYPE,mFileType)

        val fragment = FolderListFragment.newInstance(Constants.Fragment.FOLDER_LIST, bundle)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.frame_content, fragment, FolderListFragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()
    }

    fun startImageListFragment(folderId: String, fileType: Int){
        my_toolbar.title = "Choose Images"
        toolbar_txt_count.visibility = View.VISIBLE
        mMenuItem.isVisible = false

        val bundle =  Bundle()
        bundle.putString(ImageVideoFolder.FOLDER_ID,folderId)
        bundle.putSerializable(PickerConfig.ARG_BUNDLE, mPickerConfig)
        bundle.putSerializable(ImagePicker.FILE_TYPE, mFileType)

        val fragment = FileListFragment.newInstance(Constants.Fragment.IMAGE_LIST, bundle)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.frame_content, fragment, FileListFragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()

    }

    fun updateCounter(counter: Int){
        toolbar_txt_count.text = "$counter"
    }

    fun sendBackData(list: MutableList<Uri>){
        if(list.isNotEmpty()){
            val intent = Intent()
            intent.putExtra(FileListFragment.URI_LIST_KEY,list as Serializable)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }

    private fun startImagePicker() {
        mFilePicker.setFilePickerCallback(object : FilePickerCallback{
            override fun onFilesChosen(files: MutableList<ChosenFile>?) {
                files?.let {
                    val finalFiles = ArrayList<Uri>()
                    if (!files.isEmpty()) {
                        for (i in files) {
                            if (i.isSuccess && i.size!=0L) {
                                finalFiles.add(FileManager.getContentUri(this@ActivityMyLibMain, File(i.originalPath)))
                            }
                        }
                    }
                    sendBackData(finalFiles)
                }
            }
            override fun onError(message: String?) {}
        })
                .allowMultipleFiles(mPickerConfig.mAllowMultiImages)
                .setFileType(MimeUtils.FileType.IMAGE)
                .setMimeTypes(MimeUtils.MimeType.IMAGE)
                .pickFile()
    }
}
