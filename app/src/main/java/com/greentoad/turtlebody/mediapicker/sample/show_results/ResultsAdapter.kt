package com.greentoad.turtlebody.mediapicker.sample.show_results

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greentoad.turtlebody.mediapicker.sample.R
import kotlinx.android.synthetic.main.item_result.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.File

/**
 * Created by WANGSUN on 26-Mar-19.
 */
class ResultsAdapter : RecyclerView.Adapter<ResultsAdapter.FolderVewHolder>(),AnkoLogger {
    private var mData: MutableList<Uri> = arrayListOf()
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderVewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false)
        return FolderVewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: FolderVewHolder, position: Int) {
        holder.bind(mData[position])
    }

    fun setData(pData: MutableList<Uri>) {
        mData = pData
        notifyDataSetChanged()
    }

    inner class FolderVewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(pData: Uri) {

            val file = File(pData.path)

            itemView.item_result_file_name.text = file.name
            val mimeType = mContext.contentResolver.getType(pData)!!

            when {
                mimeType.contains("audio") -> {
                    Glide.with(itemView)
                            .load(R.drawable.mp3_icon)
                            .into(itemView.item_result_icon)
                }
                mimeType.contains("ogg") -> {
                    Glide.with(itemView)
                            .load(R.drawable.mp3_icon)
                            .into(itemView.item_result_icon)
                }
                else->{
                    Glide.with(itemView)
                            .load(pData)
                            .into(itemView.item_result_icon)
                }
            }
        }
    }
}