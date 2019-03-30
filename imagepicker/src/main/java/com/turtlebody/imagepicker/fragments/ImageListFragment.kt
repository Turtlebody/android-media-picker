package com.turtlebody.imagepicker.fragments


import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.turtlebody.imagepicker.R
import com.turtlebody.imagepicker.activities.ActivityMain
import com.turtlebody.imagepicker.adapters.ImageListAdapter
import com.turtlebody.imagepicker.base.FragmentBase
import com.turtlebody.imagepicker.models.Image
import com.turtlebody.imagepicker.core.FileManager
import com.turtlebody.imagepicker.core.PickerConfig
import com.turtlebody.imagepicker.models.Folder
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


class ImageListFragment : FragmentBase(), ImageListAdapter.OnImageClickListener {
    companion object {

        @JvmStatic
        fun newInstance(key: Int, b: Bundle?): Fragment {
            val bf: Bundle = b ?: Bundle()
            bf.putInt("fragment.key", key);
            val fragment = ImageListFragment()
            fragment.arguments = bf
            return fragment
        }

        val URI_LIST_KEY = "uriListKey"

    }

    private var mAdapter: ImageListAdapter = ImageListAdapter()
    private var mImageList: MutableList<Image> = arrayListOf()
    private var mFolderId: String = ""
    private var mSelectedList: MutableList<Image> = arrayListOf()

    private var mUriList: MutableList<Uri> = arrayListOf()

    private lateinit var mPickerConfig: PickerConfig

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            mFolderId = it.getString(Folder.FOLDER_ID,"")
            mPickerConfig = it.getSerializable(PickerConfig.ARG_BUNDLE) as PickerConfig

            info { "folderId: $mFolderId" }
        }

        (activity as ActivityMain).updateCounter(mSelectedList.size)
        initAdapter()
        initButton()
    }

    private fun initButton() {
        iv_cancel.setOnClickListener {
            (activity as ActivityMain).onBackPressed()
        }
        btn_add_file.setOnClickListener {
            getAllUris()
        }

        if(!mPickerConfig.mAllowMultiImages){
            ll_bottom_layout.visibility = View.GONE
        }

    }

    private fun getAllUris() {
        if(mSelectedList.isNotEmpty()){
            for (i in mSelectedList){
                mUriList.add(FileManager.getContentUri(context!!, File(i.filePath)))
            }
            (activity as ActivityMain).sendBackData(mUriList)
        }
    }


    override fun onImageCheck(pData: Image) {
        if(!mPickerConfig.mAllowMultiImages){
            if(mPickerConfig.mShowDialog){
                val simpleAlert = AlertDialog.Builder(context!!)
                simpleAlert.setMessage("Are you sure to select ${pData.name}")
                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, which ->
                            (activity as ActivityMain).sendBackData(arrayListOf(FileManager.getContentUri(context!!, File(pData.filePath))))
                        }
                        .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss()  }
                simpleAlert.show()
            }
            else{
                (activity as ActivityMain).sendBackData(arrayListOf(FileManager.getContentUri(context!!, File(pData.filePath))))
            }
        }
        else{
            val selectedIndex = mImageList.indexOf(pData)

            if(selectedIndex >= 0){
                //toggle
                mImageList[selectedIndex].isSelected = !(mImageList[selectedIndex].isSelected)
                //update ui
                mAdapter.updateIsSelected(mImageList[selectedIndex])

                //update selectedList
                if(mImageList[selectedIndex].isSelected){
                    mSelectedList.add(mImageList[selectedIndex])
                }
                else{
                    mSelectedList.removeAt(mSelectedList.indexOf(pData))
                }
            }
            (activity as ActivityMain).updateCounter(mSelectedList.size)

            btn_add_file.isEnabled = mSelectedList.size>0
        }
    }


    private fun initAdapter() {
        mAdapter.setListener(this)
        mAdapter.mShowCheckBox = mPickerConfig.mAllowMultiImages
        recycler_view.layoutManager = GridLayoutManager(context,2)
        recycler_view.adapter = mAdapter
        fetchFiles()
    }

    private fun fetchFiles() {
        val fileItems = Single.fromCallable<Boolean> {
            mImageList.clear()
            val tempArray = FileManager.getFilesInFolder(context!!, mFolderId)

            //include only valid files
            for(i in tempArray){
                if(File(i.filePath).length()>0){
                    mImageList.add(i)
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
                        mAdapter.setData(mImageList)
                        progress_view.visibility = View.GONE
                    }

                    override fun onError(@NonNull e: Throwable) {
                        progress_view.visibility = View.GONE
                        info { "error: ${e.message}" }
                    }
                })
    }


}
