package com.turtlebody.imagepicker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.turtlebody.imagepicker.R
import com.turtlebody.imagepicker.models.AudioFolder
import kotlinx.android.synthetic.main.item_folder.view.*

/**
 * Created by WANGSUN on 26-Mar-19.
 */
class AudioFolderAdapter: RecyclerView.Adapter<AudioFolderAdapter.FolderVewHolder>() {
    private var mData: MutableList<AudioFolder> = arrayListOf()
    private var mOnAudioFolderClickListener: OnAudioFolderClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderVewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_folder, parent, false)
        return FolderVewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: FolderVewHolder, position: Int) {
        holder.bind(mData[position])
    }


    fun setListener(listenerAudio : OnAudioFolderClickListener){
        mOnAudioFolderClickListener = listenerAudio
    }

    fun setData(pData: MutableList<AudioFolder>){
        mData = pData
        notifyDataSetChanged()
    }

    inner class FolderVewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(pData: AudioFolder){

            itemView.folder_txt_folder_name.text = pData.name
            itemView.folder_txt_total_items.text = "${pData.contentCount} items"

            Glide.with(itemView)
                    .load(R.drawable.mp3_icon)
                    .into(itemView.folder_image_folder_icon)

            itemView.setOnClickListener {
                mOnAudioFolderClickListener?.onFolderClick(pData)
            }

        }
    }


    interface OnAudioFolderClickListener {
        fun onFolderClick(pData: AudioFolder)
    }
}