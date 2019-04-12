package com.turtlebody.mediapicker.ui.component.media.audio

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.turtlebody.mediapicker.ui.ActivityLibMain
import com.turtlebody.mediapicker.ui.component.media.audio.adapter.AudioAdapter
import com.turtlebody.mediapicker.core.Constants
import com.turtlebody.mediapicker.core.FileManager
import com.turtlebody.mediapicker.core.ImagePicker
import com.turtlebody.mediapicker.ui.common.MediaListFragment
import com.turtlebody.mediapicker.models.Audio
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.tb_media_picker_frame_progress.*
import kotlinx.android.synthetic.main.tb_media_picker_image_fragment.*
import org.jetbrains.anko.info
import java.io.File

/**
 * Created by niraj on 12-04-2019.
 */
class AudioListFragment : MediaListFragment(), AudioAdapter.OnAudioClickListener {


    companion object {

        @JvmStatic
        fun newInstance(key: Int, b: Bundle?): Fragment {
            val bf: Bundle = b ?: Bundle()
            bf.putInt("fragment.key", key);
            val fragment = AudioListFragment()
            fragment.arguments = bf
            return fragment
        }

        const val B_ARG_FOLDER_PATH = "args.folderPath"


    }

    private var mFileType  = Constants.FileTypes.FILE_TYPE_AUDIO
    private var mFolderPath: String = ""

    private var mUriList: MutableList<Uri> = arrayListOf()



    private var mAudioAdapter: AudioAdapter = AudioAdapter()
    private var mAudioList: MutableList<Audio> = arrayListOf()
    private var mSelectedAudioList: MutableList<Audio> = arrayListOf()




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
    }

    override fun onRestoreState(savedInstanceState: Bundle?, args: Bundle?) {
        arguments?.let {
            mFolderPath= it.getString(B_ARG_FOLDER_PATH,"")
            mFileType = it.getInt(ImagePicker.FILE_TYPE)
            info { "folderPath: $mFolderPath" }
        }
    }


    override fun getAllUris() {
        if(mFileType == Constants.FileTypes.FILE_TYPE_AUDIO){
            if(mSelectedAudioList.isNotEmpty()){
                for (i in mSelectedAudioList){
                    info { "audio path: ${i.filePath}" }
                    mUriList.add(FileManager.getContentUri(context!!, File(i.filePath)))
                }
                (activity as ActivityLibMain).sendBackData(mUriList)
            }

        }

    }


    override fun onAudioCheck(pData: Audio) {
        if(!mPickerConfig.mAllowMultiImages){
            if(mPickerConfig.mShowDialog){
                val simpleAlert = AlertDialog.Builder(context!!)
                simpleAlert.setMessage("Are you sure to select ${pData.name}")
                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, which ->
                            (activity as ActivityLibMain).sendBackData(arrayListOf(FileManager.getContentUri(context!!, File(pData.filePath))))
                        }
                        .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss()  }
                simpleAlert.show()
            }
            else{
                (activity as ActivityLibMain).sendBackData(arrayListOf(FileManager.getContentUri(context!!, File(pData.filePath))))
            }
        }
        else{
            val selectedIndex = mAudioList.indexOf(pData)

            if(selectedIndex >= 0){
                //toggle
                mAudioList[selectedIndex].isSelected = !(mAudioList[selectedIndex].isSelected)
                //update ui
                mAudioAdapter.updateIsSelected(mAudioList[selectedIndex])

                //update selectedList
                if(mAudioList[selectedIndex].isSelected){
                    mSelectedAudioList.add(mAudioList[selectedIndex])
                }
                else{
                    mSelectedAudioList.removeAt(mSelectedAudioList.indexOf(pData))
                }
            }
            (activity as ActivityLibMain).updateCounter(mSelectedAudioList.size)
            btn_add_file.isEnabled = mSelectedAudioList.size>0
        }
    }


    private fun initAdapter() {
        mAudioAdapter.setListener(this)
        mAudioAdapter.mShowCheckBox = mPickerConfig.mAllowMultiImages

        if(mFileType== Constants.FileTypes.FILE_TYPE_AUDIO){
            recycler_view.layoutManager = LinearLayoutManager(context)
            recycler_view.adapter = mAudioAdapter
            fetchAudioFiles()
        }

    }

    private fun fetchAudioFiles() {
        val fileItems = Single.fromCallable<Boolean> {
            mAudioList.clear()
            val tempArray = FileManager.getAudioFilesInFolder(context!!, mFolderPath)

            //include only valid files
            for(i in tempArray){
                if(File(i.filePath).length()>0){
                    mAudioList.add(i)
                }
            }
            true
        }

        fileItems.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Boolean> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        progress_view.visibility = View.VISIBLE
                    }

                    override fun onSuccess(t: Boolean) {
                        mAudioAdapter.setData(mAudioList)
                        progress_view.visibility = View.GONE
                    }

                    override fun onError(@NonNull e: Throwable) {
                        progress_view.visibility = View.GONE
                        info { "error: ${e.message}" }
                    }
                })
    }

}