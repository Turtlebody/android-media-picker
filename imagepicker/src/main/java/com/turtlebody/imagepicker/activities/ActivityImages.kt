package com.turtlebody.imagepicker.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.turtlebody.imagepicker.R
import com.turtlebody.imagepicker.adapters.ImageListAdapter
import com.turtlebody.imagepicker.base.ActivityBase
import com.turtlebody.imagepicker.models.Image
import com.turtlebody.imagepicker.core.FileManager
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_images.*
import kotlinx.android.synthetic.main.frame_progress.*

class ActivityImages : ActivityBase() {

    private var mAdapter: ImageListAdapter = ImageListAdapter()
    private var mImageList: MutableList<Image> = arrayListOf()
    private var mFolderId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)

        if(intent.extras!=null){
            mFolderId = intent.getStringExtra("id")
        }

        initAdapter()
    }

    private fun initAdapter() {
        recycler_view.layoutManager = GridLayoutManager(this,2)
        recycler_view.adapter = mAdapter
        fetchFiles()
    }

    private fun fetchFiles() {
        val fileItems = Single.fromCallable<Boolean> {
            mImageList.clear()
            mImageList.addAll(FileManager.getFilesInFolder(this, mFolderId))
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
                        Log.e("Error loading file ", e.message)
                    }
                })



    }
}
