package com.greentoad.turtlebody.mediapicker.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.greentoad.turtlebody.mediapicker.R
import com.greentoad.turtlebody.mediapicker.ui.ActivityLibMain
import com.greentoad.turtlebody.mediapicker.ui.base.FragmentBase
import com.greentoad.turtlebody.mediapicker.core.PickerConfig
import kotlinx.android.synthetic.main.tb_media_picker_image_fragment.*

/**
 * Created by niraj on 12-04-2019.
 */
abstract class MediaListFragment : FragmentBase() {
    companion object {

        const val B_ARG_PICKER_CONFIG = "media_list_fragment.args.pickerConfig"
    }

    lateinit var mPickerConfig: PickerConfig

    abstract fun onRestoreState(savedInstanceState: Bundle?, args: Bundle?)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        arguments?.let {
            mPickerConfig = it.getSerializable(B_ARG_PICKER_CONFIG) as PickerConfig
        }
        onRestoreState(savedInstanceState, arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tb_media_picker_image_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()
    }

    private fun initButton() {
        iv_cancel.setOnClickListener {
            (activity as ActivityLibMain).onBackPressed()
        }
        btn_add_file.setOnClickListener {
            getAllUris()
        }

        if (!mPickerConfig.mAllowMultiImages) {
            ll_bottom_layout.visibility = View.GONE
        }
    }

    abstract fun getAllUris()
}