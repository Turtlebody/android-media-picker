package com.turtlebody.imagepicker.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.turtlebody.imagepicker.R
import com.turtlebody.imagepicker.activities.ActivityMain
import com.turtlebody.imagepicker.adapters.ImageListAdapter
import com.turtlebody.imagepicker.base.FragmentBase
import com.turtlebody.imagepicker.models.Image
import com.turtlebody.imagepicker.core.FileManager
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_image_list.*
import kotlinx.android.synthetic.main.frame_progress.*


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

    }

    private var mAdapter: ImageListAdapter = ImageListAdapter()
    private var mImageList: MutableList<Image> = arrayListOf()
    private var mFolderId: String = ""
    private var mSelectedList: MutableList<Image> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            it.getString("folderId")?.let { id->
                mFolderId = id
            }
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
            (activity as ActivityMain).sendBackData(mSelectedList)
        }
    }


    override fun onImageCheck(pData: Image) {
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

//        if(mSelectedList.size>0){
//            btn_add_file.visibility = View.VISIBLE
//        }
//        else{
//            btn_add_file.visibility = View.GONE
//        }
    }


    private fun initAdapter() {
        mAdapter.setListener(this)
        recycler_view.layoutManager = GridLayoutManager(context,2)
        recycler_view.adapter = mAdapter
        fetchFiles()
    }

    private fun fetchFiles() {
        val fileItems = Single.fromCallable<Boolean> {
            mImageList.clear()
            mImageList.addAll(FileManager.getFilesInFolder(context!!, mFolderId))
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
                        //Log.e("Error loading file ", e.message)
                    }
                })
    }


}
