package com.turtlebody.imagepicker.core

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.turtlebody.imagepicker.activities.ActivityMain
import com.turtlebody.imagepicker.fragments.ImageListFragment
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger

/**
 * Created by WANGSUN on 29-Mar-19.
 */
class ImagePicker {



    companion object {
        @JvmStatic
        fun with(activity: FragmentActivity, pickerConfig: PickerConfig): FilePickerImpl {
            return FilePickerImpl(activity,pickerConfig)
        }
        private const val REQ_CODE = 500
    }

    class FilePickerImpl(private var activity: FragmentActivity,private var config: PickerConfig) : PickerFragment.OnPickerListener,AnkoLogger {
        private lateinit var mEmitter: ObservableEmitter<MutableList<Uri>>

        override fun onData(data: MutableList<Uri>) {
            mEmitter.onNext(data)
            mEmitter.onComplete()
        }

        override fun onCancel(message:String) {
            mEmitter.onError(Throwable(message));
            mEmitter.onComplete()
        }

        fun onResult(): Observable<MutableList<Uri>> {
            return Observable.create<MutableList<Uri>> { emitter: ObservableEmitter<MutableList<Uri>> ->
                this.mEmitter = emitter
                startFragment()
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }

        private fun startFragment() {
            val bundle = Bundle()
            bundle.putSerializable(PickerConfig.ARG_BUNDLE, config)

            val fragment = PickerFragment()
            fragment.arguments = bundle

            fragment.setListener(this)
            activity.supportFragmentManager.beginTransaction().add(fragment,PickerFragment::class.java.simpleName).commit()
        }

    }



    /**************************************************
     *              Fragment
     */


    class PickerFragment: Fragment(), AnkoLogger {
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            val config = arguments?.getSerializable(PickerConfig.ARG_BUNDLE)

            val intent = Intent(context,ActivityMain::class.java)
            intent.putExtra(PickerConfig.ARG_BUNDLE,config)
            startActivityForResult(intent, REQ_CODE)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == REQ_CODE){
                if(resultCode == Activity.RESULT_OK){
                    val list = data?.extras?.getSerializable(ImageListFragment.URI_LIST_KEY) as MutableList<Uri>
                    mListener?.onData(list)
                }
                else{
                    mListener?.onCancel("Cancelled")
                }
            }
            else
                super.onActivityResult(requestCode, resultCode, data)
        }

        private var mListener: OnPickerListener? = null

        fun setListener(listener: OnPickerListener) {
            this.mListener = listener
        }

        interface OnPickerListener {
            fun onData(data: MutableList<Uri>)
            fun onCancel(message:String)
        }
    }
}