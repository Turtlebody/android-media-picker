package com.turtlebody.imagepicker.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.turtlebody.imagepicker.R
import com.turtlebody.imagepicker.activities.ActivityMyLibMain
import com.turtlebody.imagepicker.adapters.AudioFolderAdapter
import com.turtlebody.imagepicker.adapters.ImageVideoFolderAdapter
import com.turtlebody.imagepicker.base.FragmentMyBase
import com.turtlebody.imagepicker.core.Constants
import com.turtlebody.imagepicker.models.ImageVideoFolder
import com.turtlebody.imagepicker.core.FileManager
import com.turtlebody.imagepicker.core.ImagePicker
import com.turtlebody.imagepicker.models.AudioFolder
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_folder_list.*
import kotlinx.android.synthetic.main.frame_progress.*
import org.jetbrains.anko.info


class FolderListFragment : FragmentMyBase() {

    companion object {

        @JvmStatic
        fun newInstance(key: Int, b: Bundle?): Fragment {
            val bf: Bundle = b ?: Bundle()
            bf.putInt("fragment.key", key);
            val fragment = FolderListFragment()
            fragment.arguments = bf
            return fragment
        }

    }

    private var mImageVideoFolderAdapter: ImageVideoFolderAdapter = ImageVideoFolderAdapter()
    private var mImageVideoFolderList: MutableList<ImageVideoFolder> = arrayListOf()
    private var mFileType = Constants.FileTypes.FILE_TYPE_IMAGE

    private var mAudioFolderAdapter: AudioFolderAdapter = AudioFolderAdapter()
    private var mAudioFolderList: MutableList<AudioFolder> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_folder_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            mFileType = it.getInt(ImagePicker.FILE_TYPE)
        }

        initAdapter()
    }

    private fun initAdapter() {
        mImageVideoFolderAdapter.setListener(object : ImageVideoFolderAdapter.OnFolderClickListener{
            override fun onFolderClick(pData: ImageVideoFolder) {
                (activity as ActivityMyLibMain).startImageListFragment(pData.id,mFileType)
            }
        })

        mAudioFolderAdapter.setListener(object : AudioFolderAdapter.OnAudioFolderClickListener {
            override fun onFolderClick(pData: AudioFolder) {
                (activity as ActivityMyLibMain).startImageListFragment(pData.id,mFileType)
            }
        })
        recycler_view.layoutManager = LinearLayoutManager(context)

        if(mFileType==Constants.FileTypes.FILE_TYPE_AUDIO){
            recycler_view.adapter = mAudioFolderAdapter
            fetchAudioFolders()
        }
        else{
            recycler_view.adapter = mImageVideoFolderAdapter
            fetchImageVideoFolders()
        }


    }

    private fun fetchImageVideoFolders() {
        val bucketFetch = Single.fromCallable<ArrayList<ImageVideoFolder>> { FileManager.fetchImageVideoFolders(context!!,mFileType) }
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
                        info { "error: ${e.message}" }
                    }
                })
    }

    private fun fetchAudioFolders() {
        val bucketFetch = Single.fromCallable<ArrayList<AudioFolder>> { FileManager.fetchAudioFolders(context!!) }
        bucketFetch
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<ArrayList<AudioFolder>> {

                    override fun onSubscribe(@NonNull d: Disposable) {
                        progress_view.visibility = View.VISIBLE
                    }

                    override fun onSuccess(@NonNull audioFolders: ArrayList<AudioFolder>) {
                        mAudioFolderList = audioFolders
                        mAudioFolderAdapter.setData(mAudioFolderList)
                        progress_view.visibility = View.GONE
                    }

                    override fun onError(@NonNull e: Throwable) {
                        progress_view.visibility = View.GONE
                        info { "error: ${e.message}" }
                    }
                })
    }




}
