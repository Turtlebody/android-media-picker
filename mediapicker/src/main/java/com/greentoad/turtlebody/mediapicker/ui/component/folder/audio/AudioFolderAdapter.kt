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


    /**
     * Register a callback to be invoked when folder view is clicked.
     * @param listener The callback that will run
     */
    fun setListener(listenerAudio : OnAudioFolderClickListener){
        mOnAudioFolderClickListener = listenerAudio
    }

    /**
     * @param pData mutable-list-of AudioFolder
     */
    fun setData(pData: MutableList<AudioFolder>){
        mData = pData
        notifyDataSetChanged()
    }

    inner class FolderVewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(pData: AudioFolder){

            itemView.item_folder_txt_folder_name.text = pData.name
            itemView.item_folder_txt_total_items.text = "${pData.contentCount} items"

            itemView.item_folder_fl.visibility = View.GONE

            Glide.with(itemView)
                    .load(R.drawable.tb_media_picker_ic_music_folder)
                    .into(itemView.item_folder_icon)

            itemView.setOnClickListener {
                mOnAudioFolderClickListener?.onFolderClick(pData)
            }
        }
    }


    interface OnAudioFolderClickListener {
        fun onFolderClick(pData: AudioFolder)
    }
}