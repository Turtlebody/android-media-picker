package com.turtlebody.imagepicker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.turtlebody.imagepicker.R
import com.turtlebody.imagepicker.models.Audio
import kotlinx.android.synthetic.main.item_audio.view.*

/**
 * Created by WANGSUN on 26-Mar-19.
 */
class AudioAdapter: RecyclerView.Adapter<AudioAdapter.AudioVewHolder>() {
    private var mData: MutableList<Audio> = arrayListOf()
    private var mOnAudioClickListener: OnAudioClickListener? = null
    var mShowCheckBox: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioVewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_audio, parent, false)
        return AudioVewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: AudioVewHolder, position: Int) {
        holder.bind(mData[position])
    }


    fun setListener(listener : OnAudioClickListener){
        mOnAudioClickListener = listener
    }

    fun setData(pData: MutableList<Audio>){
        mData = pData
        notifyDataSetChanged()
    }

    fun updateIsSelected(pData: Audio){
        val pos = mData.indexOf(pData)
        if(pos>=0){
            mData[pos] = pData
            notifyItemChanged(pos)
        }
    }

    inner class AudioVewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(pData: Audio){

            Glide.with(itemView)
                    .load(R.drawable.mp3_icon)
                    .into(itemView.iv_audio_icon)

            itemView.cb_btn_selection.isChecked = pData.isSelected

            itemView.txt_audio_name.text = pData.name
            itemView.txt_audio_artist.text = pData.artist

            itemView.setOnClickListener {
                mOnAudioClickListener?.onAudioCheck(pData)
            }

            itemView.cb_btn_selection.setOnClickListener {
                mOnAudioClickListener?.onAudioCheck(pData)
            }

            if(!mShowCheckBox){
                itemView.cb_btn_selection.visibility = View.GONE
            }
            else{
                itemView.cb_btn_selection.visibility = View.VISIBLE
            }
        }
    }


    interface OnAudioClickListener {
        fun onAudioCheck(pData: Audio)
    }
}