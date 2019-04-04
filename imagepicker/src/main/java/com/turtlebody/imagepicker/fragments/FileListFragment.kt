package com.turtlebody.imagepicker.fragments


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.turtlebody.imagepicker.R
import com.turtlebody.imagepicker.activities.ActivityMyLibMain
import com.turtlebody.imagepicker.adapters.AudioAdapter
import com.turtlebody.imagepicker.adapters.ImageVideoAdapter
import com.turtlebody.imagepicker.base.FragmentMyBase
import com.turtlebody.imagepicker.core.Constants
import com.turtlebody.imagepicker.core.FileManager
import com.turtlebody.imagepicker.core.ImagePicker
import com.turtlebody.imagepicker.core.PickerConfig
import com.turtlebody.imagepicker.models.Audio
import com.turtlebody.imagepicker.models.ImageVideoFolder
import com.turtlebody.imagepicker.models.ImageVideo
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_image_list.*
import kotlinx.android.synthetic.main.frame_progress.*
import org.jetbrains.anko.info
import java.io.File


class FileListFragment : FragmentMyBase(), ImageVideoAdapter.OnImageClickListener, AudioAdapter.OnAudioClickListener {


    companion object {

        @JvmStatic
        fun newInstance(key: Int, b: Bundle?): Fragment {
            val bf: Bundle = b ?: Bundle()
            bf.putInt("fragment.key", key);
            val fragment = FileListFragment()
            fragment.arguments = bf
            return fragment
        }

        val URI_LIST_KEY = "uriListKey"

    }

    private var mFileType  = Constants.FileTypes.FILE_TYPE_IMAGE
    private var mFolderId: String = ""
    private var mUriList: MutableList<Uri> = arrayListOf()

    private var mImageVideoAdapter: ImageVideoAdapter = ImageVideoAdapter()
    private var mImageVideoList: MutableList<ImageVideo> = arrayListOf()
    private var mSelectedImageVideoList: MutableList<ImageVideo> = arrayListOf()


    private var mAudioAdapter: AudioAdapter = AudioAdapter()
    private var mAudioList: MutableList<Audio> = arrayListOf()
    private var mSelectedAudioList: MutableList<Audio> = arrayListOf()

    private lateinit var mPickerConfig: PickerConfig

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            mFolderId = it.getString(ImageVideoFolder.FOLDER_ID,"")
            mFileType = it.getInt(ImagePicker.FILE_TYPE)
            mPickerConfig = it.getSerializable(PickerConfig.ARG_BUNDLE) as PickerConfig
            info { "folderId: $mFolderId" }
        }

        (activity as ActivityMyLibMain).updateCounter(mSelectedImageVideoList.size)
        initAdapter()
        initButton()
    }

    private fun initButton() {
        iv_cancel.setOnClickListener {
            (activity as ActivityMyLibMain).onBackPressed()
        }
        btn_add_file.setOnClickListener {
            getAllUris()
        }

        if(!mPickerConfig.mAllowMultiImages){
            ll_bottom_layout.visibility = View.GONE
        }

    }

    private fun getAllUris() {
        if(mFileType == Constants.FileTypes.FILE_TYPE_AUDIO){
            if(mSelectedAudioList.isNotEmpty()){
                for (i in mSelectedAudioList){
                    info { "audio path: ${i.filePath}" }
                    mUriList.add(FileManager.getContentUri(context!!, File(i.filePath)))
                }
                (activity as ActivityMyLibMain).sendBackData(mUriList)
            }

        }
        else{
            if(mSelectedImageVideoList.isNotEmpty()){
                for (i in mSelectedImageVideoList){
                    info { "path: ${i.filePath}" }
                    mUriList.add(FileManager.getContentUri(context!!, File(i.filePath)))
                }
                (activity as ActivityMyLibMain).sendBackData(mUriList)
            }
        }

    }


    override fun onImageCheck(pData: ImageVideo) {

        //var directoryName = File(pData.filePath).parentFile.name
        //var dirPath = File(pData.filePath).parent
        if(!mPickerConfig.mAllowMultiImages){
            if(mPickerConfig.mShowDialog){
                val simpleAlert = AlertDialog.Builder(context!!)
                simpleAlert.setMessage("Are you sure to select ${pData.name}")
                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, which ->
                            (activity as ActivityMyLibMain).sendBackData(arrayListOf(FileManager.getContentUri(context!!, File(pData.filePath))))
                        }
                        .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss()  }
                simpleAlert.show()
            }
            else{
                (activity as ActivityMyLibMain).sendBackData(arrayListOf(FileManager.getContentUri(context!!, File(pData.filePath))))
            }
        }
        else{
            val selectedIndex = mImageVideoList.indexOf(pData)

            if(selectedIndex >= 0){
                //toggle
                mImageVideoList[selectedIndex].isSelected = !(mImageVideoList[selectedIndex].isSelected)
                //update ui
                mImageVideoAdapter.updateIsSelected(mImageVideoList[selectedIndex])

                //update selectedList
                if(mImageVideoList[selectedIndex].isSelected){
                    mSelectedImageVideoList.add(mImageVideoList[selectedIndex])
                }
                else{
                    mSelectedImageVideoList.removeAt(mSelectedImageVideoList.indexOf(pData))
                }
            }
            (activity as ActivityMyLibMain).updateCounter(mSelectedImageVideoList.size)
            btn_add_file.isEnabled = mSelectedImageVideoList.size>0
        }
    }

    override fun onAudioCheck(pData: Audio) {
        if(!mPickerConfig.mAllowMultiImages){
            if(mPickerConfig.mShowDialog){
                val simpleAlert = AlertDialog.Builder(context!!)
                simpleAlert.setMessage("Are you sure to select ${pData.name}")
                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, which ->
                            (activity as ActivityMyLibMain).sendBackData(arrayListOf(FileManager.getContentUri(context!!, File(pData.filePath))))
                        }
                        .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss()  }
                simpleAlert.show()
            }
            else{
                (activity as ActivityMyLibMain).sendBackData(arrayListOf(FileManager.getContentUri(context!!, File(pData.filePath))))
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
            (activity as ActivityMyLibMain).updateCounter(mSelectedAudioList.size)
            btn_add_file.isEnabled = mSelectedAudioList.size>0
        }
    }


    private fun initAdapter() {
        mImageVideoAdapter.setListener(this)
        mAudioAdapter.setListener(this)
        mImageVideoAdapter.mShowCheckBox = mPickerConfig.mAllowMultiImages
        mAudioAdapter.mShowCheckBox = mPickerConfig.mAllowMultiImages

        if(mFileType==Constants.FileTypes.FILE_TYPE_AUDIO){
            recycler_view.layoutManager = LinearLayoutManager(context)
            recycler_view.adapter = mAudioAdapter
            fetchAudioFiles()
        }
        else{
            recycler_view.layoutManager = GridLayoutManager(context,2)
            recycler_view.adapter = mImageVideoAdapter
            fetchImageFiles()
        }
    }

    private fun fetchImageFiles() {
        val fileItems = Single.fromCallable<Boolean> {
            mImageVideoList.clear()
            val tempArray = FileManager.getImageVideoFilesInFolder(context!!, mFolderId, mFileType)

            //include only valid files
            for(i in tempArray){
                if(File(i.filePath).length()>0){
                    mImageVideoList.add(i)
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
                        mImageVideoAdapter.setData(mImageVideoList)
                        progress_view.visibility = View.GONE
                    }

                    override fun onError(@NonNull e: Throwable) {
                        progress_view.visibility = View.GONE
                        info { "error: ${e.message}" }
                    }
                })
    }

    private fun fetchAudioFiles() {
        val fileItems = Single.fromCallable<Boolean> {
            mAudioList.clear()
            val tempArray = FileManager.getAudioFilesInFolder(context!!, mFolderId)

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
