package com.greentoad.turtlebody.mediapicker.ui.component.folder.audio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greentoad.turtlebody.mediapicker.R
import kotlinx.android.synthetic.main.tb_media_picker_item_folder.view.*

/**
 * Created by WANGSUN on 26-Mar-19.
 */
class AudioFolderAdapter: RecyclerView.Adapter<AudioFolderAdapter.FolderVewHolder>() {
    private var mData: MutableList<AudioFolder> = arrayListOf()
    private var mOnAudioFolderClickListener: OnAudioFolderClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderVewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tb_media_picker_item_folder, parent, false)
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

            itemView.tb_media_picker_item_folder_small_icon.visibility = View.GONE

            Glide.with(itemView)
                    .load(R.drawable.ic_music_folder)
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