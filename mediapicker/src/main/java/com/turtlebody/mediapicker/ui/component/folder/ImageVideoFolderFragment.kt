package com.turtlebody.mediapicker.ui.component.folder


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.turtlebody.mediapicker.ui.ActivityLibMain
import com.turtlebody.mediapicker.ui.component.folder.adapter.ImageVideoFolderAdapter
import com.turtlebody.mediapicker.ui.base.FragmentBase
import com.turtlebody.mediapicker.core.Constants
import com.turtlebody.mediapicker.ui.component.models.ImageVideoFolder
import com.turtlebody.mediapicker.core.FileManager
import com.turtlebody.mediapicker.core.MediaPicker
import com.turtlebody.mediapicker.R
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.tb_media_picker_folder_fragment.*
import kotlinx.android.synthetic.main.tb_media_picker_frame_progress.*
import org.jetbrains.anko.info


class ImageVideoFolderFragment : FragmentBase() {

    companion object {

        @JvmStatic
        fun newInstance(key: Int, b: Bundle?): Fragment {
            val bf: Bundle = b ?: Bundle()
            bf.putInt("fragment.key", key);
            val fragment = ImageVideoFolderFragment()
            fragment.arguments = bf
            return fragment
        }

    }

    private var mImageVideoFolderAdapter: ImageVideoFolderAdapter = ImageVideoFolderAdapter()
    private var mImageVideoFolderList: MutableList<ImageVideoFolder> = arrayListOf()
    private var mFileType = Constants.FileTypes.FILE_TYPE_IMAGE


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tb_media_picker_folder_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            mFileType = it.getInt(MediaPicker.FILE_TYPE)
        }
        initAdapter()
    }

    private fun initAdapter() {
        mImageVideoFolderAdapter.setListener(object : ImageVideoFolderAdapter.OnFolderClickListener{
            override fun onFolderClick(pData: ImageVideoFolder) {
                info { "fileId: ${pData.id}" }
                (activity as ActivityLibMain).startMediaListFragment(pData.id)
            }
        })
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = mImageVideoFolderAdapter
        fetchImageVideoFolders()


    }

    private fun fetchImageVideoFolders() {
        val bucketFetch: Single<ArrayList<ImageVideoFolder>> =
                if(mFileType==Constants.FileTypes.FILE_TYPE_VIDEO)
                    Single.fromCallable<ArrayList<ImageVideoFolder>> { FileManager.fetchVideoFolders(context!!) }
                else
                    Single.fromCallable<ArrayList<ImageVideoFolder>> { FileManager.fetchImageFolders(context!!) }

        bucketFetch
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<ArrayList<ImageVideoFolder>> {

                    override fun onSubscribe(@NonNull d: Disposable) {
                        progress_view.visibility = View.VISIBLE
                    }

                    override fun onSuccess(@NonNull imageVideoFolders: ArrayList<ImageVideoFolder>) {
                        mImageVideoFolderList = imageVideoFolders
                        mImageVideoFolderAdapter.setData(mImageVideoFolderList)
                        progress_view.visibility = View.GONE
                    }

                    override fun onError(@NonNull e: Throwable) {
                        progress_view.visibility = View.GONE
                        e.printStackTrace()
                        info { "error: ${e.message}" }
                    }
                })
    }

}
