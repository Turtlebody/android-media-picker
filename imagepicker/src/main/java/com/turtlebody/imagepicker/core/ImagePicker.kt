package com.turtlebody.imagepicker.core

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import com.turtlebody.imagepicker.activities.ActivityMain
import com.turtlebody.imagepicker.fragments.ImageListFragment
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import java.lang.ref.WeakReference

/**
 * Created by WANGSUN on 29-Mar-19.
 */
class ImagePicker {

    companion object {
        @JvmStatic
        fun with(activity: FragmentActivity, pickerConfig: PickerConfig): FilePickerImpl {
            return FilePickerImpl(activity, pickerConfig)
        }

        private const val REQ_CODE = 500
    }

    class FilePickerImpl(activity: FragmentActivity, private var config: PickerConfig) : PickerFragment.OnPickerListener, AnkoLogger {
        private lateinit var mEmitter: ObservableEmitter<MutableList<Uri>>
        private var mActivity: WeakReference<FragmentActivity> = WeakReference(activity)

        override fun onData(data: MutableList<Uri>) {
            mEmitter.onNext(data)
            mEmitter.onComplete()
        }

        override fun onCancel(message: String) {
            mEmitter.onError(Throwable(message));
            mEmitter.onComplete()
        }

        fun onResult(): Observable<MutableList<Uri>> {
            return Observable.create<MutableList<Uri>> { emitter: ObservableEmitter<MutableList<Uri>> ->
                this.mEmitter = emitter
                getPermission()
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }

        @SuppressLint("CheckResult")
        private fun getPermission() {
            mActivity.get()?.let {
                it.runOnUiThread {
                    val rxPermissions = RxPermissions(it)
                    rxPermissions
                            .request(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                            .subscribe { granted ->
                                if (granted) {
                                    // All requested permissions are granted
                                    startFragment()
                                } else {
                                    Toast.makeText(it, "Need permission to do this task.", Toast.LENGTH_SHORT).show()
                                }
                            }
                }
            }
        }

        private fun startFragment() {
            val bundle = Bundle()
            bundle.putSerializable(PickerConfig.ARG_BUNDLE, config)

            val fragment = PickerFragment()
            fragment.arguments = bundle

            fragment.setListener(this)
            mActivity.get()?.supportFragmentManager?.beginTransaction()?.add(fragment, PickerFragment::class.java.simpleName)?.commit()
        }

    }


    /**************************************************
     *              Fragment
     */


    class PickerFragment : Fragment(), AnkoLogger {
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            val config = arguments?.getSerializable(PickerConfig.ARG_BUNDLE)

            val intent = Intent(context, ActivityMain::class.java)
            intent.putExtra(PickerConfig.ARG_BUNDLE, config)
            startActivityForResult(intent, REQ_CODE)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == REQ_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    val list = data?.extras?.getSerializable(ImageListFragment.URI_LIST_KEY) as MutableList<Uri>
                    mListener?.onData(list)
                } else {
                    mListener?.onCancel("Cancelled")
                }
            } else
                super.onActivityResult(requestCode, resultCode, data)
        }

        private var mListener: OnPickerListener? = null

        fun setListener(listener: OnPickerListener) {
            this.mListener = listener
        }

        interface OnPickerListener {
            fun onData(data: MutableList<Uri>)
            fun onCancel(message: String)
        }
    }
}