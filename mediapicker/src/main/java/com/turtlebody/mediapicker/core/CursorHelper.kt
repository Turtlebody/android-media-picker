package com.turtlebody.mediapicker.core

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
import android.R.attr.path
import java.io.File
import java.nio.file.FileSystem


/**
 * Created by WANGSUN on 27-Mar-19.
 */
object CursorHelper {
    private val imageQueryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private val videoQueryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    //private val audioQueryUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    private val audioQueryUri = MediaStore.Files.getContentUri("external")

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
        return context.contentResolver.query(audioQueryUri, Constants.Projection.AUDIO_FOLDER_NEW,
                Constants.Selection.AUDIO_FOLDER, null, MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }

    fun getAudioFilesInFolderCursor(context: Context, folderPath: String): Cursor?{
        var path = "$folderPath/"
        return context.contentResolver.query(audioQueryUri, Constants.Projection.AUDIO_FILE,
                MediaStore.Audio.Media.DATA + " LIKE ? AND " + MediaStore.Audio.Media.DATA + " NOT LIKE ? "

                        +" AND "+ MediaStore.Audio.Media.MIME_TYPE +" LIKE ?",
                arrayOf("$path%", "$path%/%", "audio%"),
                MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }

    @Deprecated("won't work")
    fun getAudioFileCursor(context: Context, folderId: String): Cursor?{
        return context.contentResolver.query(audioQueryUri, Constants.Projection.AUDIO_FILE,
                Constants.Projection.AUDIO_FILE[4] + " = '" + folderId + "'", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }
}