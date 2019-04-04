package com.turtlebody.imagepicker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.turtlebody.imagepicker.R
import com.turtlebody.imagepicker.models.ImageVideo
import kotlinx.android.synthetic.main.item_image.view.*
import java.io.File

/**
 * Created by WANGSUN on 26-Mar-19.
 */
class ImageVideoAdapter: RecyclerView.Adapter<ImageVideoAdapter.ImageVewHolder>() {
    private var mData: MutableList<ImageVideo> = arrayListOf()
    private var mOnImageClickListener: OnImageClickListener? = null
    var mShowCheckBox: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageVewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageVewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ImageVewHolder, position: Int) {
        holder.bind(mData[position])
    }


    fun setListener(listener : OnImageClickListener){
        mOnImageClickListener = listener
    }

    fun setData(pData: MutableList<ImageVideo>){
        mData = pData
        notifyDataSetChanged()
    }

    fun updateIsSelected(pData: ImageVideo){
        val pos = mData.indexOf(pData)
        if(pos>=0){
            mData[pos] = pData
            notifyItemChanged(pos)
        }
    }

    inner class ImageVewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(pData: ImageVideo){

            Glide.with(itemView)
                    .load(File(pData.thumbnailPath))
                    .into(itemView.iv_image)

            itemView.cb_btn_selection.isChecked = pData.isSelected

            itemView.setOnClickListener {
                mOnImageClickListener?.onImageCheck(pData)
            }

            itemView.cb_btn_selection.setOnClickListener {
                mOnImageClickListener?.onImageCheck(pData)
            }

            if(!mShowCheckBox){
                itemView.cb_btn_selection.visibility = View.GONE
            }
            else{
                itemView.cb_btn_selection.visibility = View.VISIBLE
            }
        }
    }


    interface OnImageClickListener {
        fun onImageCheck(pData: ImageVideo)
    }
}