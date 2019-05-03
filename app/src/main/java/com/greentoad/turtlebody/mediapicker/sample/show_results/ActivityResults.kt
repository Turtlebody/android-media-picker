package com.greentoad.turtlebody.mediapicker.sample.show_results

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.greentoad.turtlebody.mediapicker.sample.ActivityBase
import com.greentoad.turtlebody.mediapicker.sample.R
import com.greentoad.turtlebody.mediapicker.ui.ActivityLibMain
import kotlinx.android.synthetic.main.activity_results.*
import org.jetbrains.anko.find

class ActivityResults : ActivityBase() {

    private var mList: MutableList<Uri> = arrayListOf()
    private var mAdapter: ResultsAdapter = ResultsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        initToolbar(R.drawable.tb_media_picker_ic_arrow_back_black_24dp,find(R.id.toolbar))
        toolbarTitle = "Selected Files"


        if(intent.extras!=null){
            mList = intent.getSerializableExtra(ActivityLibMain.B_ARG_URI_LIST) as MutableList<Uri>
        }

        initAdapter()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun initAdapter() {
        mAdapter.setData(mList)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = mAdapter
    }
}
