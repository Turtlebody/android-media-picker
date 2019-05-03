package com.greentoad.turtlebody.mediapicker.ui.common

import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.greentoad.turtlebody.mediapicker.core.MediaPickerConfig
import com.greentoad.turtlebody.mediapicker.ui.ActivityLibMain
import com.greentoad.turtlebody.mediapicker.ui.base.FragmentBase
import kotlinx.android.synthetic.main.tb_media_picker_file_fragment.*


/**
 * Created by niraj on 12-04-2019.
 */
abstract class MediaListFragment : FragmentBase() {
    companion object {

        const val B_ARG_PICKER_CONFIG = "media_list_fragment.args.pickerConfig"
    }

    var mMediaPickerConfig: MediaPickerConfig = MediaPickerConfig()
    var mFolderId: String = ""
    var mUriList: ArrayList<Uri> = arrayListOf()

    abstract fun onRestoreState(savedInstanceState: Bundle?, args: Bundle?)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        arguments?.let {
            mMediaPickerConfig = it.getSerializable(B_ARG_PICKER_CONFIG) as MediaPickerConfig
        }
        onRestoreState(savedInstanceState, arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.greentoad.turtlebody.mediapicker.R.layout.tb_media_picker_file_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()


        /*******************************************************
         * Dynamically change marginBottom for recyclerView
         *******************************************************/
        if(mMediaPickerConfig.mAllowMultiSelection){
            val params = CoordinatorLayout.LayoutParams(
                    CoordinatorLayout.LayoutParams.MATCH_PARENT,
                    CoordinatorLayout.LayoutParams.MATCH_PARENT)

            val r = context!!.resources
            val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56f, r.displayMetrics).toInt()

            params.setMargins(0, 0, 0, px)
            file_fragment_recycler_view.layoutParams = params
        }
    }

    private fun initButton() {
        file_fragment_btn_cancel.setOnClickListener {
            (activity as ActivityLibMain).onBackPressed()
        }
        file_fragment_btn_done.setOnClickListener {
            getAllUris()
        }

        if (!mMediaPickerConfig.mAllowMultiSelection) {
            file_fragment_bottom_ll.visibility = View.GONE
        }
    }

    abstract fun getAllUris()
}