package com.greentoad.turtlebody.mediapicker.ui.component.folder.image_video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greentoad.turtlebody.mediapicker.R
import kotlinx.android.synthetic.main.tb_media_picker_item_folder.view.*
import java.io.File

/**
 * Created by WANGSUN on 26-Mar-19.
 */
class ImageVideoFolderAdapter: RecyclerView.Adapter<ImageVideoFolderAdapter.FolderVewHolder>() {


    private var mData: MutableList<ImageVideoFolder> = arrayListOf()
    private var mOnFolderClickListener: OnFolderClickListener? = null

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
    fun setListener(listener : OnFolderClickListener){
        mOnFolderClickListener = listener
    }

    /**
     * @param pData mutable-list-of ImageVideoFolder
     */
    fun setData(pData: MutableList<ImageVideoFolder>){
        mData = pData
        notifyDataSetChanged()
    }

    inner class FolderVewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(pData: ImageVideoFolder){

            itemView.item_folder_txt_folder_name.text = pData.name
            itemView.item_folder_txt_total_items.text = "${pData.contentCount} items"

            itemView.item_folder_small_icon.visibility = View.VISIBLE

            Glide.with(itemView)
                    .load(File( pData.coverImageFilePath))
                    .into(itemView.item_folder_icon)

            itemView.setOnClickListener {
                mOnFolderClickListener?.onFolderClick(pData)
            }

        }
    }


    interface OnFolderClickListener {
        fun onFolderClick(pData: ImageVideoFolder)
    }
}