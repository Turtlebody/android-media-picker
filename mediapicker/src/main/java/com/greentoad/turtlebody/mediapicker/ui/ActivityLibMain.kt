package com.greentoad.turtlebody.mediapicker.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.greentoad.turtlebody.mediapicker.MediaPicker
import com.greentoad.turtlebody.mediapicker.R
import com.greentoad.turtlebody.mediapicker.core.FileHelper
import com.greentoad.turtlebody.mediapicker.core.MediaConstants
import com.greentoad.turtlebody.mediapicker.core.MediaPickerConfig
import com.greentoad.turtlebody.mediapicker.ui.base.ActivityBase
import com.greentoad.turtlebody.mediapicker.ui.common.MediaListFragment
import com.greentoad.turtlebody.mediapicker.ui.component.folder.audio.AudioFolderFragment
import com.greentoad.turtlebody.mediapicker.ui.component.folder.image_video.ImageVideoFolder
import com.greentoad.turtlebody.mediapicker.ui.component.folder.image_video.ImageVideoFolderFragment
import com.greentoad.turtlebody.mediapicker.ui.component.media.audio.AudioListFragment
import com.greentoad.turtlebody.mediapicker.ui.component.media.image.ImageListFragment
import com.greentoad.turtlebody.mediapicker.ui.component.media.video.VideoListFragment
import com.greentoad.turtlebody.mediapicker.util.UtilMime
import org.jetbrains.anko.find
import org.jetbrains.anko.info
import java.io.Serializable


class ActivityLibMain : ActivityBase() {

    private var mFileType: Int = MediaPicker.MediaTypes.IMAGE
    private lateinit var mMenuItem: MenuItem
    private lateinit var mMediaPickerConfig: MediaPickerConfig

    private lateinit var vToolbarCounter: TextView

    companion object {
        const val MEDIA_REQ_CODE = 5000

        const val B_ARG_FILE_MISSING = "activity.lib.main.file.missing"
        const val B_ARG_FILE_TYPE = "activity.lib.main.file.type"
        const val B_ARG_URI_LIST = "activity.lib.main.uri.list"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tb_media_picker_activity_lib_main)

        initToolbar(R.drawable.tb_media_picker_ic_arrow_back_black_24dp, find(R.id.activity_lib_main_toolbar))

        toolbarTitle = "Select Folder"
        vToolbarCounter = find<TextView>(R.id.activity_lib_main_toolbar_txt_count)

        if (intent.extras != null) {
            mMediaPickerConfig = intent.getSerializableExtra(MediaPickerConfig.ARG_BUNDLE) as MediaPickerConfig
            mFileType = intent.getIntExtra(B_ARG_FILE_TYPE, MediaPicker.MediaTypes.IMAGE)

            mMediaPickerConfig.mScreenOrientation?.let {
                requestedOrientation = it
            }
        }
    }


    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.activity_lib_main_frame_content)
        when (fragment) {
            is ImageVideoFolderFragment -> finish()
            is AudioFolderFragment -> finish()
            is MediaListFragment -> {
                super.onBackPressed()
                toolbarTitle = "Select Folder"
                vToolbarCounter.visibility = View.GONE
                mMenuItem.isVisible = true
                updateCounter(0)
            }
            else -> super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tb_media_picker_activity_main, menu)
        mMenuItem = menu.getItem(0)
        startFolderListFragment()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_open -> {
                createPickFromDocumentsIntent()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //    @SuppressLint("CheckResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == MEDIA_REQ_CODE) {
            info { "data: ${data?.data}" }
            handleFileData(data)
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }


    private fun handleFileData(intent: Intent?) {
        val uriList: ArrayList<Uri> = arrayListOf()
        var isMissing = false
        if (intent != null) {
            if (intent.data != null) {
                val uri = intent.data
                info { "handleFileData: $uri" }
                if(FileHelper.isFileExist(this,uri))
                    uriList.add(uri)
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (intent.clipData != null) {
                    val clipData = intent.clipData
                    info { "handleFileData: Multiple files with ClipData"}
                    for (i in 0 until clipData!!.itemCount) {
                        val item = clipData.getItemAt(i)
                        info { "Item [" + i + "]: " + item.uri.toString()}
                        if(FileHelper.isFileExist(this,item.uri)){
                            //important as many time android return duplicate uri when selecting multiple media files
                            if(!uriList.contains(item.uri)){
                                uriList.add(item.uri)
                            }
                            else
                                info { "duplicate uri found" }
                        }
                        else
                            isMissing = true
                    }
                }
            }
            if (intent.hasExtra("uris")) {
                val paths = intent.getParcelableArrayListExtra<Uri>("uris")
                for (i in paths.indices) {
                    uriList.add(paths[i])
                }
            }
            if(uriList.isNotEmpty()){
                sendBackData(uriList,isMissing)
            }
        }
    }


    private fun startFolderListFragment() {
        toolbarTitle = "Select Folder"
        vToolbarCounter.visibility = View.GONE
        mMenuItem.isVisible = true

        when (mFileType) {
            MediaPicker.MediaTypes.AUDIO -> {
                startAudioFolderFragment()
            }
            else -> startImageVideoFolderFragment()
        }
    }

    /**
     * @param folderInfo if(audio) then folderPath else folderInfo
     */
    fun startMediaListFragment(folderInfo: String) {
        val bundle = Bundle()
        bundle.putSerializable(MediaListFragment.B_ARG_PICKER_CONFIG, mMediaPickerConfig)


        val fragment: Fragment
        val fragmentTag: String
        when (mFileType) {
            MediaPicker.MediaTypes.IMAGE -> {
                toolbarTitle = "Choose Image"
                bundle.putString(ImageVideoFolder.FOLDER_ID, folderInfo)
                fragment = ImageListFragment.newInstance(MediaConstants.Fragment.IMAGE_LIST, bundle)
                fragmentTag = ImageListFragment::class.java.simpleName
            }
            MediaPicker.MediaTypes.VIDEO -> {
                toolbarTitle = "Choose Video"
                bundle.putString(ImageVideoFolder.FOLDER_ID, folderInfo)
                fragment = VideoListFragment.newInstance(MediaConstants.Fragment.VIDEO_LIST, bundle)
                fragmentTag = VideoListFragment::class.java.simpleName
            }
            MediaPicker.MediaTypes.AUDIO -> {
                toolbarTitle = "Choose Audio"
                bundle.putString(AudioListFragment.B_ARG_FOLDER_PATH, folderInfo)
                fragment = AudioListFragment.newInstance(MediaConstants.Fragment.AUDIO_LIST, bundle)
                fragmentTag = AudioListFragment::class.java.simpleName
            }
            else -> {
                toolbarTitle = "Choose Image"
                bundle.putString(ImageVideoFolder.FOLDER_ID, folderInfo)
                fragment = ImageListFragment.newInstance(MediaConstants.Fragment.IMAGE_LIST, bundle)
                fragmentTag = ImageListFragment::class.java.simpleName
            }
        }

        vToolbarCounter.visibility = View.VISIBLE
        mMenuItem.isVisible = false

        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.activity_lib_main_frame_content, fragment, fragmentTag)
                .addToBackStack(null)
                .commit()
    }

    /**
     * @param counter counter for selecting multiple media files
     */
    fun updateCounter(counter: Int) {
        vToolbarCounter.text = "$counter"
    }

    fun sendBackData(list: ArrayList<Uri>,isFileMissing: Boolean=false) {
        if (list.isNotEmpty()) {
            val intent = Intent()
            intent.putExtra(B_ARG_URI_LIST, list as Serializable)

            if(isFileMissing)
                intent.putExtra(B_ARG_FILE_MISSING, true)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }


    private fun createPickFromDocumentsIntent() {
        val fileType: String
        val mimeType: Array<String>

        when (mFileType) {
            MediaPicker.MediaTypes.IMAGE -> {
                fileType = UtilMime.FileType.IMAGE
                mimeType = UtilMime.MimeType.IMAGE
            }
            MediaPicker.MediaTypes.VIDEO -> {
                fileType = UtilMime.FileType.VIDEO
                mimeType = UtilMime.MimeType.VIDEO
            }
            MediaPicker.MediaTypes.AUDIO -> {
                fileType = UtilMime.FileType.AUDIO
                mimeType = UtilMime.MimeType.AUDIO
            }
            else -> {
                fileType = UtilMime.FileType.IMAGE
                mimeType = UtilMime.MimeType.IMAGE
            }
        }

        val intent: Intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = if(mMediaPickerConfig.mUriPermanentAccess)
                Intent(Intent.ACTION_OPEN_DOCUMENT)
            else
                Intent(Intent.ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, mMediaPickerConfig.mAllowMultiSelection)
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        } else {
            intent = Intent(Intent.ACTION_GET_CONTENT)
        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = fileType
        startActivityForResult(intent, MEDIA_REQ_CODE)
    }


    private fun startImageVideoFolderFragment() {
        val bundle = Bundle()
        bundle.putInt(B_ARG_FILE_TYPE, mFileType)

        val fragment = ImageVideoFolderFragment.newInstance(MediaConstants.Fragment.IMAGE_VIDEO_FOLDER, bundle)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.activity_lib_main_frame_content, fragment, ImageVideoFolderFragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()
    }

    private fun startAudioFolderFragment() {
        val fragment = AudioFolderFragment.newInstance(MediaConstants.Fragment.AUDIO_FOLDER, Bundle())
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.activity_lib_main_frame_content, fragment, AudioFolderFragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()
    }
}
