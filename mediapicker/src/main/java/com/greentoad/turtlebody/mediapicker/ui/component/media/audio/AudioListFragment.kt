package com.greentoad.turtlebody.mediapicker.ui.component.media.audio

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.greentoad.turtlebody.mediapicker.core.FileManager
import com.greentoad.turtlebody.mediapicker.ui.ActivityLibMain
import com.greentoad.turtlebody.mediapicker.ui.common.MediaListFragment
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.tb_media_picker_frame_progress.*
import kotlinx.android.synthetic.main.tb_media_picker_file_fragment.*
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

        const val B_ARG_FOLDER_PATH = "args.folder.path"
    }

    private var mFolderPath: String = ""

    private var mAudioAdapter: AudioAdapter = AudioAdapter()
    private var mAudioModelList: MutableList<AudioModel> = arrayListOf()
    private var mSelectedAudioModelList: MutableList<AudioModel> = arrayListOf()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
    }

    override fun onRestoreState(savedInstanceState: Bundle?, args: Bundle?) {
        arguments?.let {
            mFolderPath= it.getString(B_ARG_FOLDER_PATH,"")
            info { "folderPath: $mFolderPath" }
        }
    }


    override fun getAllUris() {
        if(mSelectedAudioModelList.isNotEmpty()){
            for (i in mSelectedAudioModelList){
                info { "audio path: ${i.filePath}" }
                mUriList.add(FileManager.getContentUri(context!!, File(i.filePath)))
            }
            (activity as ActivityLibMain).sendBackData(mUriList)
        }
    }


    override fun onAudioCheck(pData: AudioModel) {
        if(!mMediaPickerConfig.mAllowMultiSelection){
            if(mMediaPickerConfig.mShowConfirmationDialog){
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
            val selectedIndex = mAudioModelList.indexOf(pData)

            if(selectedIndex >= 0){
                //toggle
                mAudioModelList[selectedIndex].isSelected = !(mAudioModelList[selectedIndex].isSelected)
                //update ui
                mAudioAdapter.updateIsSelected(mAudioModelList[selectedIndex])

                //update selectedList (add/remove audio)
                if(mAudioModelList[selectedIndex].isSelected){
                    mSelectedAudioModelList.add(mAudioModelList[selectedIndex])
                }
                else{
                    mSelectedAudioModelList.removeAt(mSelectedAudioModelList.indexOf(pData))
                }
            }
            (activity as ActivityLibMain).updateCounter(mSelectedAudioModelList.size)
            file_fragment_btn_done.isEnabled = mSelectedAudioModelList.size>0
        }
    }


    private fun initAdapter() {
        mAudioAdapter.setListener(this)
        mAudioAdapter.mShowCheckBox = mMediaPickerConfig.mAllowMultiSelection

        file_fragment_recycler_view.layoutManager = LinearLayoutManager(context)
        file_fragment_recycler_view.adapter = mAudioAdapter
        fetchAudioFiles()

    }

    private fun fetchAudioFiles() {
        val fileItems = Single.fromCallable<Boolean> {
            mAudioModelList.clear()
            val tempArray = FileManager.getAudioFilesInFolder(context!!, mFolderPath)

            //include only valid files
            for(i in tempArray){
                if(i.size>0){
                    mAudioModelList.add(i)
                    info { "size: ${i.size}" }
                    info { "mimetype: ${i.mimeType}" }
                    //info { "size: ${File(i.filePath).length()}" }
                }
            }
            true
        }

        fileItems.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Boolean> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        frame_progress.visibility = View.VISIBLE
                    }

                    override fun onSuccess(t: Boolean) {
                        mAudioAdapter.setData(mAudioModelList)
                        frame_progress.visibility = View.GONE
                    }

                    override fun onError(@NonNull e: Throwable) {
                        frame_progress.visibility = View.GONE
                        info { "error: ${e.message}" }
                    }
                })
    }

}