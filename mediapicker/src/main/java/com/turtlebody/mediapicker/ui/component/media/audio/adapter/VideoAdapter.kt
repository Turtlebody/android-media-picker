package com.turtlebody.mediapicker.ui.component.media.audio.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.turtlebody.mediapicker.R
import com.turtlebody.mediapicker.ui.component.models.Video
import kotlinx.android.synthetic.main.tb_media_picker_item_image.view.*
import java.io.File

/**
 * Created by WANGSUN on 26-Mar-19.
 */
class VideoAdapter: RecyclerView.Adapter<VideoAdapter.VideoVewHolder>() {
    private var mData: MutableList<Video> = arrayListOf()
    private var mOnVideoClickListener: OnVideoClickListener? = null
    var mShowCheckBox: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoVewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tb_media_picker_item_image, parent, false)
        return VideoVewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: VideoVewHolder, position: Int) {
        holder.bind(mData[position])
    }


    fun setListener(listener : OnVideoClickListener){
        mOnVideoClickListener = listener
    }

    fun setData(pData: MutableList<Video>){
        mData = pData
        notifyDataSetChanged()
    }

    fun updateIsSelected(pData: Video){
        val pos = mData.indexOf(pData)
        if(pos>=0){
            mData[pos] = pData
            notifyItemChanged(pos)
        }
    }

    inner class VideoVewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(pData: Video){

            Glide.with(itemView)
                    .load(File(pData.thumbnailPath))
                    .into(itemView.iv_image)

            itemView.cb_btn_selection.isChecked = pData.isSelected

            itemView.setOnClickListener {
                mOnVideoClickListener?.onVideoCheck(pData)
            }

            if(!mShowCheckBox){
                itemView.cb_btn_selection.visibility = View.GONE
            }
            else{
                itemView.cb_btn_selection.visibility = View.VISIBLE
            }
        }
    }


    interface OnVideoClickListener {
        fun onVideoCheck(pData: Video)
    }
}