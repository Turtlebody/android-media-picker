package com.turtlebody.imagepicker.core

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore

/**
 * Created by WANGSUN on 27-Mar-19.
 */
object CursorHelper {
    private val imageQueryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private val videoQueryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    private val audioQueryUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    /**
     * cursor for images and videos
     */
    @SuppressLint("Recycle")
    fun getImageVideoFolderCursor(context: Context, fileType: Int): Cursor? {
        return if(fileType == Constants.FileTypes.FILE_TYPE_IMAGE){
            context.contentResolver.query(imageQueryUri, Constants.Projection.IMAGE_VIDEO_FOLDER,
                    null, null, MediaStore.MediaColumns.DATE_ADDED + " DESC")
        }
        else
            context.contentResolver.query(videoQueryUri, Constants.Projection.IMAGE_VIDEO_FOLDER,
                    null, null, MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }

    @SuppressLint("Recycle")
    fun getImageVideoFileCursor(context: Context, folderId: String, fileType: Int): Cursor?{

        return if(fileType == Constants.FileTypes.FILE_TYPE_IMAGE){
            context.contentResolver.query(imageQueryUri, Constants.Projection.IMAGE_VIDEO_FILE,
                    Constants.Projection.IMAGE_VIDEO_FILE[4] + " = '" + folderId + "'", null,
                    MediaStore.MediaColumns.DATE_ADDED + " DESC")
        }
        else
            context.contentResolver.query(videoQueryUri, Constants.Projection.IMAGE_VIDEO_FILE,
                Constants.Projection.IMAGE_VIDEO_FILE[4] + " = '" + folderId + "'", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }


    /**
     * cursor for audio
     */
    fun getAudioFolderCursor(context: Context): Cursor? {
        return context.contentResolver.query(audioQueryUri, Constants.Projection.AUDIO_FOLDER,
                null, null, MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }

    fun getAudioFileCursor(context: Context, folderId: String): Cursor?{
        return context.contentResolver.query(audioQueryUri, Constants.Projection.AUDIO_FILE,
                Constants.Projection.AUDIO_FILE[4] + " = '" + folderId + "'", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }
}