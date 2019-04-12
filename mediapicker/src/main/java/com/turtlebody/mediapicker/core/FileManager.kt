package com.turtlebody.mediapicker.core

import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.core.content.FileProvider.getUriForFile
import com.turtlebody.mediapicker.models.Audio
import com.turtlebody.mediapicker.models.AudioFolder
import com.turtlebody.mediapicker.models.ImageVideoFolder
import com.turtlebody.mediapicker.models.ImageVideo
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.File
import java.util.*


object FileManager : AnkoLogger {

    private val SHARED_PROVIDER_AUTHORITY = "com.turtlebody.imagepicker.myFileprovider"


    /**
     *   For Image and Video
     */
    fun fetchImageVideoFolders(context: Context, fileType: Int): ArrayList<ImageVideoFolder> {
        val folders = ArrayList<ImageVideoFolder>()
        val folderFileCountMap = HashMap<String, Int>()

        val projection = Constants.Projection.IMAGE_VIDEO_FOLDER

        //Create the cursor pointing to the SDCard
        val cursor = CursorHelper.getImageVideoFolderCursor(context, fileType)

        cursor?.let {
            val columnIndexFolderId = it.getColumnIndexOrThrow(projection[0])
            val columnIndexFolderName = it.getColumnIndexOrThrow(projection[1])
            val columnIndexFilePath = it.getColumnIndexOrThrow(projection[2])

            while (it.moveToNext()) {
                val folderId = it.getString(columnIndexFolderId)
                if (folderFileCountMap.containsKey(folderId)) {
                    folderFileCountMap[folderId] = folderFileCountMap[folderId]!! + 1
                } else {
                    val folder = ImageVideoFolder(folderId, it.getString(columnIndexFolderName), it.getString(columnIndexFilePath), 0)
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
        val cursor: Cursor? = CursorHelper.getImageVideoFileCursor(context, folderId, fileType)

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
    fun fetchAudioFolderList(context: Context): ArrayList<AudioFolder> {
        val folders = ArrayList<AudioFolder>()
        val folderFileCountMap = HashMap<String, Int>()

        val projection = Constants.Projection.AUDIO_FOLDER_NEW

        // Create the cursor pointing to the SDCard
        val cursor = CursorHelper.getAudioFolderCursor(context)

        cursor?.let {
            val columnIndexFolderId = it.getColumnIndexOrThrow(projection[0])
            val columnIndexCount = it.getColumnIndexOrThrow("dataCount")
            val columnIndexFolderParent = it.getColumnIndexOrThrow(projection[1])
            val columnIndexFilePath = it.getColumnIndexOrThrow(projection[2])
            val columnIndexDisplayName = it.getColumnIndexOrThrow(projection[3])

            while (it.moveToNext()) {
//                info(it.getString(columnIndexFolderId) +
//                        " " + it.getString(columnIndexCount) +
//                        " " + it.getString(columnIndexFolderParent) +
//                        " " + it.getString(columnIndexFilePath) +
//                        " " + it.getString(columnIndexDisplayName) + " ")
//                info { File(it.getString(columnIndexFilePath)).parentFile.name}
//                info { File(it.getString(columnIndexFilePath)).parentFile.path}
                val folderId = it.getString(columnIndexFolderId)
                var audioCounts = it.getInt(columnIndexCount)
                if (folderFileCountMap.containsKey(folderId)) {
                    folderFileCountMap[folderId] = folderFileCountMap[folderId]!! + audioCounts
                } else {
                    val folder = AudioFolder(folderId, File(it.getString(columnIndexFilePath)).parentFile.name,
                            File(it.getString(columnIndexFilePath)).parentFile.path, 0)
                    folders.add(folder)
                    folderFileCountMap[folderId] = audioCounts
                }
            }
            for (fdr in folders) {
                fdr.contentCount = folderFileCountMap[fdr.id]!!
            }
            cursor.close()
        }
        return folders
    }



    fun getAudioFilesInFolder(context: Context, folderPath: String): ArrayList<Audio> {
        val fileItems = ArrayList<Audio>()
        val projection = Constants.Projection.AUDIO_FILE

        // Create the cursor pointing to the SDCard
        val cursor: Cursor? = CursorHelper.getAudioFilesInFolderCursor(context, folderPath)

        cursor?.let {
            info { "audio query size: "+ cursor.count }
            val columnIndexAudioId = it.getColumnIndexOrThrow(projection[0])
            val columnIndexAudioTitle = it.getColumnIndexOrThrow(projection[1])
            val columnIndexAudioName = it.getColumnIndexOrThrow(projection[2])
            val columnIndexAudioSize = it.getColumnIndexOrThrow(projection[3])
            val columnIndexAudioPath = it.getColumnIndexOrThrow(projection[4])
            val columnIndexAudioMimeType = it.getColumnIndexOrThrow(projection[5])
            //TODo val columnIndexAudioArtist = it.getColumnIndexOrThrow(projection[5])

            while (it.moveToNext()) {
                var name = it.getString(columnIndexAudioName)
                if(name==null){
                   name =  it.getString(columnIndexAudioTitle)
                }
                if(name==null){
                 name = ""
                }
                val fileItem = Audio(it.getString(columnIndexAudioId),
                        name,
                        it.getInt(columnIndexAudioSize),
                        it.getString(columnIndexAudioPath),
                        //it.getString(columnIndexAudioArtist),
                        it.getString(columnIndexAudioMimeType),
                        false)
                fileItems.add(fileItem)

            }
            cursor.close()
        }
        info { "audioFiles: $fileItems" }
        return fileItems
    }


    @Deprecated("worg implemented")
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
                    val folder = AudioFolder(folderId, it.getString(columnIndexFolderName),
                            it.getString(columnIndexFilePath), 0)
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

    fun getContentUri(context: Context, newFile: File): Uri {
        return getUriForFile(context, SHARED_PROVIDER_AUTHORITY, newFile)
    }
}

