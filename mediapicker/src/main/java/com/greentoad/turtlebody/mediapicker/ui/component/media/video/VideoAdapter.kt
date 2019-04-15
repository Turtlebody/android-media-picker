package com.greentoad.turtlebody.mediapicker.ui.component.media.video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.greentoad.turtlebody.mediapicker.R
import com.greentoad.turtlebody.mediapicker.util.UtilTime
import kotlinx.android.synthetic.main.tb_media_picker_item_video.view.*
import java.io.File

/**
 * Created by WANGSUN on 26-Mar-19.
 */
class VideoAdapter: RecyclerView.Adapter<VideoAdapter.VideoVewHolder>() {
    private var mData: MutableList<VideoModel> = arrayListOf()
    private var mOnVideoClickListener: OnVideoClickListener? = null
    var mShowCheckBox: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoVewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tb_media_picker_item_video, parent, false)
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

    fun setData(pData: MutableList<VideoModel>){
        mData = pData
        notifyDataSetChanged()
    }

    fun updateIsSelected(pData: VideoModel){
        val pos = mData.indexOf(pData)
        if(pos>=0){
            mData[pos] = pData
            notifyItemChanged(pos)
        }
    }

    inner class VideoVewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(pData: VideoModel){

            Glide.with(itemView)
                    .load(File(pData.thumbnailPath))
                    .into(itemView.tb_media_picker_item_video_image)

            itemView.tb_media_picker_item_video_checkbox.isChecked = pData.isSelected

            itemView.setOnClickListener {
                mOnVideoClickListener?.onVideoCheck(pData)
            }

            itemView.tb_media_picker_item_video_duration.text = UtilTime.timeFormatted(pData.duration.toLong())

            if(!mShowCheckBox){
                itemView.tb_media_picker_item_video_checkbox.visibility = View.GONE
            }
            else{
                itemView.tb_media_picker_item_video_checkbox.visibility = View.VISIBLE
            }
        }
    }


    interface OnVideoClickListener {
        fun onVideoCheck(pData: VideoModel)
    }
}