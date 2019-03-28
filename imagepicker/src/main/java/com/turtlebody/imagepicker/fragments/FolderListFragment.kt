package com.turtlebody.imagepicker.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.turtlebody.imagepicker.R
import com.turtlebody.imagepicker.activities.ActivityMain
import com.turtlebody.imagepicker.adapters.FolderListAdapter
import com.turtlebody.imagepicker.base.FragmentBase
import com.turtlebody.imagepicker.models.Folder
import com.turtlebody.imagepicker.core.FileManager
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_folder_list.*
import kotlinx.android.synthetic.main.frame_progress.*


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

    private var mAdapter: FolderListAdapter = FolderListAdapter()
    private var mFolderList: MutableList<Folder> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_folder_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()

    }

    private fun initAdapter() {
        mAdapter.setListener(object : FolderListAdapter.OnFolderClickListener{
            override fun onFolderClick(pData: Folder) {
                (activity as ActivityMain).startImageListFragment(pData.id)
            }
        })
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = mAdapter

        fetchFolders()
    }

    private fun fetchFolders() {
        val bucketFetch = Single.fromCallable<ArrayList<Folder>> { FileManager.fetchLocalFolders(context!!) }
        bucketFetch
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<ArrayList<Folder>> {

                    override fun onSubscribe(@NonNull d: Disposable) {
                        progress_view.visibility = View.VISIBLE
                    }

                    override fun onSuccess(@NonNull folders: ArrayList<Folder>) {
                        mFolderList = folders
                        mAdapter.setData(mFolderList)
                        progress_view.visibility = View.GONE
                    }

                    override fun onError(@NonNull e: Throwable) {
                        //Log.e("Error loading buckets", e.message)
                    }
                })
    }


}
