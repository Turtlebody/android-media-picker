package com.greentoad.turtlebody.mediapicker.ui.component.folder.audio


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.greentoad.turtlebody.mediapicker.R
import com.greentoad.turtlebody.mediapicker.core.FileManager
import com.greentoad.turtlebody.mediapicker.ui.ActivityLibMain
import com.greentoad.turtlebody.mediapicker.ui.base.FragmentBase
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.tb_media_picker_folder_fragment.*
import kotlinx.android.synthetic.main.tb_media_picker_frame_progress.*
import org.jetbrains.anko.info


class AudioFolderFragment : FragmentBase() {

    companion object {

        @JvmStatic
        fun newInstance(key: Int, b: Bundle?): Fragment {
            val bf: Bundle = b ?: Bundle()
            bf.putInt("fragment.key", key);
            val fragment = AudioFolderFragment()
            fragment.arguments = bf
            return fragment
        }

    }

    private var mAudioFolderAdapter: AudioFolderAdapter = AudioFolderAdapter()
    private var mAudioFolderList: MutableList<AudioFolder> = arrayListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tb_media_picker_folder_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()
    }

    private fun initAdapter() {
        mAudioFolderAdapter.setListener(object : AudioFolderAdapter.OnAudioFolderClickListener {
            override fun onFolderClick(pData: AudioFolder) {
                info { "folderPath: ${pData.path}" }
                (activity as ActivityLibMain).startMediaListFragment(pData.path)
            }
        })

        folder_fragment_recycler_view.layoutManager = LinearLayoutManager(context)
        folder_fragment_recycler_view.adapter = mAudioFolderAdapter
        fetchAudioFolders()
    }


    private fun fetchAudioFolders() {
        val bucketFetch = Single.fromCallable<ArrayList<AudioFolder>> {
            FileManager.fetchAudioFolderList(context!!) }
        bucketFetch
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<ArrayList<AudioFolder>> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        frame_progress.visibility = View.VISIBLE
                    }

                    override fun onSuccess(@NonNull audioFolders: ArrayList<AudioFolder>) {
                        mAudioFolderList = audioFolders
                        info { "folders: $audioFolders" }
                        mAudioFolderAdapter.setData(mAudioFolderList)
                        frame_progress.visibility = View.GONE
                    }

                    override fun onError(@NonNull e: Throwable) {
                        frame_progress.visibility = View.GONE
                        e.printStackTrace()
                        info { "error: ${e.message}" }
                    }
                })
    }
}
