package com.turtlebody.imagepicker.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.turtlebody.imagepicker.R
import com.turtlebody.imagepicker.adapters.FolderListAdapter
import com.turtlebody.imagepicker.base.ActivityBase
import com.turtlebody.imagepicker.models.Folder
import com.turtlebody.imagepicker.utils.FileUtils
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_folders.*
import kotlinx.android.synthetic.main.frame_progress.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.ArrayList

class ActivityFolders : ActivityBase() {

    private var mAdapter: FolderListAdapter = FolderListAdapter()
    private var mFolderList: MutableList<Folder> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folders)

        toolbar.title = "Select Folder"

        initAdapter()
    }

    private fun initAdapter() {
        mAdapter.setListener(object : FolderListAdapter.OnFolderClickListener{
            override fun onFolderClick(pData: Folder) {
                val intent = Intent(this@ActivityFolders,ActivityImages::class.java)
                intent.putExtra("folderId",pData.folderId)
                startActivity(intent)
            }
        })
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = mAdapter

        fetchFolders()
    }

    private fun fetchFolders() {
        val bucketFetch = Single.fromCallable<ArrayList<Folder>> { FileUtils.fetchLocalFolders(this) }
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
                        Log.e("Error loading buckets", e.message)
                    }
                })
    }
}
