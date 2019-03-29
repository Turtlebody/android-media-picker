package com.turtlebody.imagepicker.core

import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.core.content.FileProvider.getUriForFile
import com.turtlebody.imagepicker.models.Folder
import com.turtlebody.imagepicker.models.Image
import java.io.File
import java.util.*


object FileManager {

    private val SHARED_PROVIDER_AUTHORITY = "com.turtlebody.imagepicker.myFileprovider"

    fun fetchLocalFolders(context: Context): ArrayList<Folder> {
        val folders = ArrayList<Folder>()
        val folderFileCountMap = HashMap<String, Int>()

        val projection = Constants.Projection.FOLDER

        // Create the cursor pointing to the SDCard
        val cursor = CursorHelper.getFolderCursor(context)

        cursor?.let {
            val columnIndexFolderId = it.getColumnIndexOrThrow(projection[0])
            val columnIndexFolderName = it.getColumnIndexOrThrow(projection[1])
            val columnIndexFilePath = it.getColumnIndexOrThrow(projection[2])

            while (it.moveToNext()) {
                val folderId = it.getString(columnIndexFolderId)
                if (folderFileCountMap.containsKey(folderId)) {
                    folderFileCountMap[folderId] = folderFileCountMap[folderId]!! + 1
                } else {
                    val folder = Folder(folderId,it.getString(columnIndexFolderName),it.getString(columnIndexFilePath), 0)
                    folders.add(folder)
                    folderFileCountMap[folderId] = 1
                }
            }
            for (fdr in folders) {
                fdr.contentCount = folderFileCountMap[fdr.id]!!
            }
            cursor.close()
        }
        return folders
    }

    fun getFilesInFolder(context: Context, folderId: String): ArrayList<Image> {
        val fileItems = ArrayList<Image>()
        val projection = Constants.Projection.IMAGE

        // Create the cursor pointing to the SDCard
        val cursor: Cursor? = CursorHelper.getImageCursor(context,folderId)

        cursor?.let {
            val columnIndexFileId = it.getColumnIndexOrThrow(projection[0])
            val columnIndexFileName = it.getColumnIndexOrThrow(projection[1])
            val columnIndexFileSize = it.getColumnIndexOrThrow(projection[2])
            val columnIndexFilePath = it.getColumnIndexOrThrow(projection[3])
            val columnIndexFileThumbPath = it.getColumnIndexOrThrow(projection[5])

            while (it.moveToNext()) {
                val fileItem = Image(it.getString(columnIndexFileId),
                        it.getString(columnIndexFileName),
                        it.getString(columnIndexFilePath),
                        it.getString(columnIndexFileSize),
                        it.getString(columnIndexFileThumbPath),
                        false)
                fileItems.add(fileItem)

            }
            cursor.close()
        }
        return fileItems
    }


    fun getContentUri(context: Context, newFile: File): Uri {
        return getUriForFile(context, SHARED_PROVIDER_AUTHORITY , newFile)
    }
}

