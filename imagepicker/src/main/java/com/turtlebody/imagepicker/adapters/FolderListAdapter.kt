package com.turtlebody.imagepicker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.turtlebody.imagepicker.R
import com.turtlebody.imagepicker.models.Folder
import kotlinx.android.synthetic.main.item_folder.view.*
import java.io.File

/**
 * Created by WANGSUN on 26-Mar-19.
 */
class FolderListAdapter: RecyclerView.Adapter<FolderListAdapter.FolderVewHolder>() {
    private var mData: MutableList<Folder> = arrayListOf()
    private var mOnFolderClickListener: OnFolderClickListener? = null

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


    fun setListener(listener : OnFolderClickListener){
        mOnFolderClickListener = listener
    }

    fun setData(pData: MutableList<Folder>){
        mData = pData
        notifyDataSetChanged()
    }

    inner class FolderVewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(pData: Folder){

            itemView.folder_txt_folder_name.text = pData.name
            itemView.folder_txt_total_items.text = "${pData.contentCount} items"

            Glide.with(itemView)
                    .load(File( pData.coverImageFilePath))
                    .into(itemView.folder_image_folder_icon)

            itemView.setOnClickListener {
                mOnFolderClickListener?.onFolderClick(pData)
            }

        }
    }


    interface OnFolderClickListener {
        fun onFolderClick(pData: Folder)
    }
}