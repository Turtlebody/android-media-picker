package com.turtlebody.imagepicker.core

import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.core.content.FileProvider.getUriForFile
import com.turtlebody.imagepicker.models.Audio
import com.turtlebody.imagepicker.models.AudioFolder
import com.turtlebody.imagepicker.models.ImageVideoFolder
import com.turtlebody.imagepicker.models.ImageVideo
import java.io.File
import java.util.*


object FileManager {

    private val SHARED_PROVIDER_AUTHORITY = "com.turtlebody.imagepicker.myFileprovider"


    /**
     *   For Image and Video
     */
    fun fetchImageVideoFolders(context: Context,fileType: Int): ArrayList<ImageVideoFolder> {
        val folders = ArrayList<ImageVideoFolder>()
        val folderFileCountMap = HashMap<String, Int>()

        val projection = Constants.Projection.IMAGE_VIDEO_FOLDER

        //Create the cursor pointing to the SDCard
        val cursor = CursorHelper.getImageVideoFolderCursor(context,fileType)

        cursor?.let {
            val columnIndexFolderId = it.getColumnIndexOrThrow(projection[0])
            val columnIndexFolderName = it.getColumnIndexOrThrow(projection[1])
            val columnIndexFilePath = it.getColumnIndexOrThrow(projection[2])

            while (it.moveToNext()) {
                val folderId = it.getString(columnIndexFolderId)
                if (folderFileCountMap.containsKey(folderId)) {
                    folderFileCountMap[folderId] = folderFileCountMap[folderId]!! + 1
                } else {
                    val folder = ImageVideoFolder(folderId,it.getString(columnIndexFolderName),it.getString(columnIndexFilePath), 0)
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

    fun getImageVideoFilesInFolder(context: Context, folderId: String, fileType: Int): ArrayList<ImageVideo> {
        val fileItems = ArrayList<ImageVideo>()
        val projection = Constants.Projection.IMAGE_VIDEO_FILE

        //Create the cursor pointing to the SDCard
        val cursor: Cursor? = CursorHelper.getImageVideoFileCursor(context,folderId,fileType)

        cursor?.let {
            val columnIndexFileId = it.getColumnIndexOrThrow(projection[0])
            val columnIndexFileName = it.getColumnIndexOrThrow(projection[1])
            val columnIndexFileSize = it.getColumnIndexOrThrow(projection[2])
            val columnIndexFilePath = it.getColumnIndexOrThrow(projection[3])
            val columnIndexFileThumbPath = it.getColumnIndexOrThrow(projection[5])

            while (it.moveToNext()) {
                val fileItem = ImageVideo(it.getString(columnIndexFileId),
                        it.getString(columnIndexFileName),
                        it.getString(columnIndexFileSize),
                        it.getString(columnIndexFilePath),
                        it.getString(columnIndexFileThumbPath), false)
                fileItems.add(fileItem)

            }
            cursor.close()
        }
        return fileItems
    }


    /**
     * For audio
     */
    fun fetchAudioFolders(context: Context): ArrayList<AudioFolder> {
        val folders = ArrayList<AudioFolder>()
        val folderFileCountMap = HashMap<String, Int>()

        val projection = Constants.Projection.AUDIO_FOLDER

        // Create the cursor pointing to the SDCard
        val cursor = CursorHelper.getAudioFolderCursor(context)

        cursor?.let {
            val columnIndexFolderId = it.getColumnIndexOrThrow(projection[0])
            val columnIndexFolderName = it.getColumnIndexOrThrow(projection[1])
            val columnIndexFilePath = it.getColumnIndexOrThrow(projection[2])

            while (it.moveToNext()) {
                val folderId = it.getString(columnIndexFolderId)
                if (folderFileCountMap.containsKey(folderId)) {
                    folderFileCountMap[folderId] = folderFileCountMap[folderId]!! + 1
                } else {
                    val folder = AudioFolder(folderId,it.getString(columnIndexFolderName),it.getString(columnIndexFilePath), 0)
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

    fun getAudioFilesInFolder(context: Context, folderId: String): ArrayList<Audio> {
        val fileItems = ArrayList<Audio>()
        val projection = Constants.Projection.AUDIO_FILE

        // Create the cursor pointing to the SDCard
        val cursor: Cursor? = CursorHelper.getAudioFileCursor(context,folderId)

        cursor?.let {
            val columnIndexAudioId = it.getColumnIndexOrThrow(projection[0])
            val columnIndexAudioName = it.getColumnIndexOrThrow(projection[1])
            val columnIndexAudioSize = it.getColumnIndexOrThrow(projection[2])
            val columnIndexAudioPath = it.getColumnIndexOrThrow(projection[3])
            val columnIndexAudioArtist = it.getColumnIndexOrThrow(projection[5])

            while (it.moveToNext()) {
                val fileItem = Audio(it.getString(columnIndexAudioId),
                        it.getString(columnIndexAudioName),
                        it.getString(columnIndexAudioSize),
                        it.getString(columnIndexAudioPath),
                        it.getString(columnIndexAudioArtist),
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

