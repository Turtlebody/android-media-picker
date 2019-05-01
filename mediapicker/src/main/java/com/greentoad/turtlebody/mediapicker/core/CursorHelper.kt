package com.greentoad.turtlebody.mediapicker.core

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.greentoad.turtlebody.mediapicker.MediaPicker


/**
 * Created by WANGSUN on 27-Mar-19.
 */
object CursorHelper {


    /**
     * cursor for images and videos
     */
    @SuppressLint("Recycle")
    fun getImageVideoFolderCursor(context: Context, fileType: Int): Cursor? {
        return if(fileType == MediaPicker.MediaTypes.IMAGE){
            context.contentResolver.query(MediaConstants.Queries.imageQueryUri, MediaConstants.Projection.IMAGE_FOLDER,
                    null, null, MediaStore.MediaColumns.DATE_ADDED + " DESC")
        }
        else
            context.contentResolver.query(MediaConstants.Queries.videoQueryUri, MediaConstants.Projection.VIDEO_FOLDER,
                    null, null, MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }

    @SuppressLint("Recycle")
    fun getImageVideoFileCursor(context: Context, folderId: String, fileType: Int): Cursor?{
        return if(fileType == MediaPicker.MediaTypes.IMAGE){
            context.contentResolver.query(MediaConstants.Queries.imageQueryUri, MediaConstants.Projection.IMAGE_FILE,
                    MediaConstants.Projection.IMAGE_FILE[4] + " = '" + folderId + "'", null,
                    MediaStore.MediaColumns.DATE_ADDED + " DESC")
        }
        else
            context.contentResolver.query(MediaConstants.Queries.videoQueryUri, MediaConstants.Projection.VIDEO_FILE,
                MediaConstants.Projection.VIDEO_FILE[4] + " = '" + folderId + "'", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }


    /**
     * cursor for audio
     */
    fun getAudioFolderCursor(context: Context): Cursor? {
        return context.contentResolver.query(MediaConstants.Queries.audioQueryUri, MediaConstants.Projection.AUDIO_FOLDER,
                MediaConstants.Selection.AUDIO_FOLDER, null, MediaStore.MediaColumns.DATE_ADDED + " DESC")//arrayOf("%.ogg")
    }

    fun getAudioFilesInFolderCursor(context: Context, folderPath: String): Cursor?{
        val path = "$folderPath/"
        return context.contentResolver.query(MediaConstants.Queries.audioQueryUri, MediaConstants.Projection.AUDIO_FILE,
                MediaStore.Audio.Media.DATA + " LIKE ? AND " + MediaStore.Audio.Media.DATA + " NOT LIKE ? "
                        +" AND ("+ MediaStore.Audio.Media.MIME_TYPE +" LIKE ?"
                                +" OR "+ MediaStore.Audio.Media.MIME_TYPE +" LIKE ?)",
                arrayOf("$path%", "$path%/%","audio%","application/ogg"),//"audio%"
                MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }
}