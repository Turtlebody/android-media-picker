package com.turtlebody.mediapicker.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.turtlebody.mediapicker.activities.ActivityLibMain
import com.turtlebody.imagepicker.adapters.AudioFolderAdapter
import com.turtlebody.imagepicker.adapters.ImageVideoFolderAdapter
import com.turtlebody.mediapicker.base.FragmentBase
import com.turtlebody.mediapicker.core.Constants
import com.turtlebody.mediapicker.models.ImageVideoFolder
import com.turtlebody.mediapicker.core.FileManager
import com.turtlebody.mediapicker.core.ImagePicker
import com.turtlebody.mediapicker.models.AudioFolder
import com.turtlebody.mediapicker.R
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.tb_media_pciker_folder_fragment.*
import kotlinx.android.synthetic.main.tb_media_picker_frame_progress.*
import org.jetbrains.anko.info


class FolderListFragment : FragmentBase() {

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
        return inflater.inflate(R.layout.tb_media_pciker_folder_fragment, container, false)
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
                (activity as ActivityLibMain).startImageListFragment(pData.id)
            }
        })

        mAudioFolderAdapter.setListener(object : AudioFolderAdapter.OnAudioFolderClickListener {
            override fun onFolderClick(pData: AudioFolder) {
                (activity as ActivityLibMain).startImageListFragment(pData.id)
            }
        })
        recycler_view.layoutManager = LinearLayoutManager(context)

        if(mFileType== Constants.FileTypes.FILE_TYPE_AUDIO){
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
