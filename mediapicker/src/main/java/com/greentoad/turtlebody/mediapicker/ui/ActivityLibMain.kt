package com.greentoad.turtlebody.mediapicker.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.greentoad.turtlebody.mediapicker.R
import com.greentoad.turtlebody.mediapicker.core.Constants
import com.greentoad.turtlebody.mediapicker.core.MediaPicker
import com.greentoad.turtlebody.mediapicker.core.PickerConfig
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

    private var mFileType: Int = Constants.FileTypes.FILE_TYPE_IMAGE
    private lateinit var mMenuItem: MenuItem
    private lateinit var mPickerConfig: PickerConfig

    private lateinit var vToolbarCounter: TextView

    companion object {
        val REQ_CODE = 5000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tb_media_picker_activity)

        initToolbar(R.drawable.ic_arrow_back_black_24dp, find(R.id.tb_media_picker_activity_toolbar))
        toolbarTitle = "Select Folder"
        vToolbarCounter = find<TextView>(R.id.toolbar_txt_count)

        if (intent.extras != null) {
            mPickerConfig = intent.getSerializableExtra(PickerConfig.ARG_BUNDLE) as PickerConfig
            mFileType = intent.getIntExtra(MediaPicker.FILE_TYPE, Constants.FileTypes.FILE_TYPE_IMAGE)
        }

    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_content)
        when (fragment) {
            is ImageVideoFolderFragment -> finish()
            is AudioFolderFragment -> finish()
            is MediaListFragment -> {
                super.onBackPressed()
                toolbarTitle = "Select Folder"
                vToolbarCounter.visibility = View.GONE
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

    @SuppressLint("CheckResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE) {
            info { "data: ${data?.data}" }
            if (data?.clipData != null) {
                val count = data.clipData.itemCount
                val finalFiles = ArrayList<Uri>()
                for (i in 1..count) {
                    val uri = data.clipData.getItemAt(i - 1).uri
                    finalFiles.add(uri)
                }
                sendBackData(finalFiles)
            } else if (data?.data != null) {
                val uri = data.data
                sendBackData(arrayListOf(uri))
            }
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }


    private fun startFolderListFragment() {
        toolbarTitle = "Select Folder"
        vToolbarCounter.visibility = View.GONE
        mMenuItem.isVisible = true

        when (mFileType) {
            Constants.FileTypes.FILE_TYPE_AUDIO -> {
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
        bundle.putSerializable(MediaListFragment.B_ARG_PICKER_CONFIG, mPickerConfig)


        val fragment: Fragment
        val fragmentTag: String
        when (mFileType) {
            Constants.FileTypes.FILE_TYPE_IMAGE -> {
                toolbarTitle = "Choose ImageModel"
                bundle.putString(ImageVideoFolder.FOLDER_ID, folderInfo)
                fragment = ImageListFragment.newInstance(Constants.Fragment.IMAGE_LIST, bundle)
                fragmentTag = ImageListFragment::class.java.simpleName
            }
            Constants.FileTypes.FILE_TYPE_VIDEO -> {
                toolbarTitle = "Choose VideoModel"
                bundle.putString(ImageVideoFolder.FOLDER_ID, folderInfo)
                fragment = VideoListFragment.newInstance(Constants.Fragment.VIDEO_LIST, bundle)
                fragmentTag = VideoListFragment::class.java.simpleName
            }
            Constants.FileTypes.FILE_TYPE_AUDIO -> {
                toolbarTitle = "Choose AudioModel"
                bundle.putString(AudioListFragment.B_ARG_FOLDER_PATH, folderInfo)
                fragment = AudioListFragment.newInstance(Constants.Fragment.AUDIO_LIST, bundle)
                fragmentTag = AudioListFragment::class.java.simpleName
            }
            else -> {
                toolbarTitle = "Choose ImageModel"
                bundle.putString(ImageVideoFolder.FOLDER_ID, folderInfo)
                fragment = ImageListFragment.newInstance(Constants.Fragment.IMAGE_LIST, bundle)
                fragmentTag = ImageListFragment::class.java.simpleName
            }
        }

        vToolbarCounter.visibility = View.VISIBLE
        mMenuItem.isVisible = false

        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.frame_content, fragment, fragmentTag)
                .addToBackStack(null)
                .commit()
    }

    fun updateCounter(counter: Int) {
        vToolbarCounter.text = "$counter"
    }

    fun sendBackData(list: ArrayList<Uri>) {
        if (list.isNotEmpty()) {
            val intent = Intent()
            intent.putExtra(MediaPicker.URI_LIST_KEY, list as Serializable)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }


    private fun createPickFromDocumentsIntent() {

        var fileType = ""
        var mimeType = arrayOf<String>()

        when (mFileType) {
            Constants.FileTypes.FILE_TYPE_IMAGE -> {
                fileType = UtilMime.FileType.IMAGE
                mimeType = UtilMime.MimeType.IMAGE
            }
            Constants.FileTypes.FILE_TYPE_VIDEO -> {
                fileType = UtilMime.FileType.VIDEO
                mimeType = UtilMime.MimeType.VIDEO
            }
            Constants.FileTypes.FILE_TYPE_AUDIO -> {
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
            intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, mPickerConfig.mAllowMultiImages)
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        } else {
            intent = Intent(Intent.ACTION_GET_CONTENT)
        }
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = fileType
        startActivityForResult(intent, REQ_CODE)
    }


    private fun startImageVideoFolderFragment() {
        val bundle = Bundle()
        bundle.putInt(MediaPicker.FILE_TYPE, mFileType)

        val fragment = ImageVideoFolderFragment.newInstance(Constants.Fragment.IMAGE_VIDEO_FOLDER, bundle)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.frame_content, fragment, ImageVideoFolderFragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()
    }

    private fun startAudioFolderFragment() {
        val fragment = AudioFolderFragment.newInstance(Constants.Fragment.AUDIO_FOLDER, Bundle())
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.frame_content, fragment, AudioFolderFragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()
    }
}
