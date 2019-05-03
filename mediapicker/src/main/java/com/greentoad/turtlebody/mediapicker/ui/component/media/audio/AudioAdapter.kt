package com.greentoad.turtlebody.mediapicker.ui.component.media.audio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greentoad.turtlebody.mediapicker.R
import kotlinx.android.synthetic.main.tb_media_picker_item_audio.view.*
import org.jetbrains.anko.AnkoLogger


/**
 * Created by WANGSUN on 26-Mar-19.
 */
class AudioAdapter: RecyclerView.Adapter<AudioAdapter.AudioVewHolder>(), AnkoLogger {
    private var mData: MutableList<AudioModel> = arrayListOf()
    private var mOnAudioClickListener: OnAudioClickListener? = null
    var mShowCheckBox: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioVewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.tb_media_picker_item_audio, parent, false)
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

    fun setData(pData: MutableList<AudioModel>){
        mData = pData
        notifyDataSetChanged()
    }

    fun updateIsSelected(pData: AudioModel){
        val pos = mData.indexOf(pData)
        if(pos>=0){
            mData[pos] = pData
            notifyItemChanged(pos)
        }
    }

    inner class AudioVewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(pData: AudioModel){

            Glide.with(itemView)
                    .load(getDrawableForMime(pData.mimeType, pData.filePath))
                    .into(itemView.item_audio_mimetype_icon)

            itemView.item_audio_checkbox.isChecked = pData.isSelected
            val size = (pData.size/1000).toString()

            itemView.item_audio_name.text = pData.name
            itemView.item_audio_size.text = "$size KB"

            itemView.setOnClickListener {
                mOnAudioClickListener?.onAudioCheck(pData)
            }

            itemView.item_audio_checkbox.setOnClickListener {
                mOnAudioClickListener?.onAudioCheck(pData)
            }

            if(!mShowCheckBox){
                itemView.item_audio_checkbox.visibility = View.GONE
            }
            else{
                itemView.item_audio_checkbox.visibility = View.VISIBLE
            }
        }

        private fun getDrawableForMime(mimeType: String?, filePath: String): Int {
            //info { "mimeType: "+ mimeType }
            var extType = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
            //info { "extType: "+ extType }

            if(extType==null){
                val i = filePath.lastIndexOf('.')
                if (i > 0 && i< filePath.length-1) {
                    extType = filePath.substring(i + 1)
                }
            }
            return when(extType){
                "mp3"-> R.drawable.tb_media_picker_ic_audio_mp3
                "m4a"-> R.drawable.tb_media_picker_ic_audio_m4a
                "mp4"-> R.drawable.tb_media_picker_ic_audio_m4a
                "aac" -> R.drawable.tb_media_picker_ic_audio_aac
                "wav" -> R.drawable.tb_media_picker_ic_audio_wav
                else -> R.drawable.tb_media_picker_ic_audio_aud
            }
        }
    }


    interface OnAudioClickListener {
        fun onAudioCheck(pData: AudioModel)
    }
}