package com.turtlebody.imagepicker.core

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import java.util.HashMap

/**
 * Created by WANGSUN on 27-Mar-19.
 */
object CursorHelper {

    private val URI_MAP: Map<Int, Uri> = object : HashMap<Int, Uri>() {
        init {
            put(Constants.Query.FOLDER_IMAGE, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        }
    }

    private val folderProjection = Constants.Projection.FOLDER
    private val imageProjection = Constants.Projection.IMAGE
    private val queryUri = URI_MAP[Constants.Query.FOLDER_IMAGE]

    /**
     * cursor for folder
     */
    fun getFolderCursor(context: Context): Cursor? {
        return context.contentResolver.query(queryUri!!, folderProjection, null, null, MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }

    /**
     * cursor for image
     */
    fun getImageCursor(context: Context, folderId: String): Cursor?{
        return context.contentResolver.query(queryUri!!, imageProjection, imageProjection[4] + " = '" + folderId + "'", null, MediaStore.MediaColumns.DATE_ADDED + " DESC")
    }
}