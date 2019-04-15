package com.greentoad.turtlebody.mediapicker.core

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore


/**
 * Created by WANGSUN on 27-Mar-19.
 */
object CursorHelper {


    /**
     * cursor for images and videos
     */
    @SuppressLint("Recycle")
    fun getImageVideoFolderCursor(context: Context, fileType: Int): Cursor? {
        return if(fileType == Constants.FileTypes.FILE_TYPE_IMAGE){
            context.contentResolver.query(Constants.Queries.imageQueryUri, Constants.Projection.IMAGE_FOLDER,
                    null, null, MediaStore.MediaColumns.DATE_ADDED + " DESC")
        }
        else
            context.contentResolver.query(Constants.Queries.videoQueryUri, Constants.Projection.VIDEO_FOLDER,
                    null, null, MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }

    @SuppressLint("Recycle")
    fun getImageVideoFileCursor(context: Context, folderId: String, fileType: Int): Cursor?{
        return if(fileType == Constants.FileTypes.FILE_TYPE_IMAGE){
            context.contentResolver.query(Constants.Queries.imageQueryUri, Constants.Projection.IMAGE_FILE,
                    Constants.Projection.IMAGE_FILE[4] + " = '" + folderId + "'", null,
                    MediaStore.MediaColumns.DATE_ADDED + " DESC")
        }
        else
            context.contentResolver.query(Constants.Queries.videoQueryUri, Constants.Projection.VIDEO_FILE,
                Constants.Projection.VIDEO_FILE[4] + " = '" + folderId + "'", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }


    /**
     * cursor for audio
     */
    fun getAudioFolderCursor(context: Context): Cursor? {
        return context.contentResolver.query(Constants.Queries.audioQueryUri, Constants.Projection.AUDIO_FOLDER,
                Constants.Selection.AUDIO_FOLDER, null, MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }

    fun getAudioFilesInFolderCursor(context: Context, folderPath: String): Cursor?{
        val path = "$folderPath/"
        return context.contentResolver.query(Constants.Queries.audioQueryUri, Constants.Projection.AUDIO_FILE,
                MediaStore.Audio.Media.DATA + " LIKE ? AND " + MediaStore.Audio.Media.DATA + " NOT LIKE ? "

                        +" AND "+ MediaStore.Audio.Media.MIME_TYPE +" LIKE ?",
                arrayOf("$path%", "$path%/%", "audio%"),
                MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }
}