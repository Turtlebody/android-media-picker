package com.greentoad.turtlebody.mediapicker.util

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

import java.net.URISyntaxException

/**
 * Created by niraj on 18-10-2018.
 */
object UtilsFile {

    @SuppressLint("NewApi")
    @Throws(URISyntaxException::class)
    fun getFilePath(context: Context, uri_: Uri): String? {
        var contentUri: Uri? = null
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.applicationContext, uri_)) {
            when {
                isExternalStorageDocument(uri_) -> {
                    val docId = DocumentsContract.getDocumentId(uri_)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
                isDownloadsDocument(uri_) -> {
                    val id = DocumentsContract.getDocumentId(uri_)
                    contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                }
                isMediaDocument(uri_) -> {
                    val docId = DocumentsContract.getDocumentId(uri_)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    selection = "_id=?"
                    selectionArgs = arrayOf(split[1])
                }
            }
        }
        if ("content".equals(contentUri?.scheme!!, ignoreCase = true)) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(
                    contentUri,
                    projection,
                    selection,
                    selectionArgs,
                    null
                )
                val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex)
                }
            } catch (e: Exception) {
                return getFilePathFromURI(context, uri_)
            } finally {
                cursor?.close()
            }

        } else if ("file".equals(uri_.scheme!!, ignoreCase = true)) {
            return uri_.path
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun getFilePathFromURI(context:Context, contentUri:Uri):String? {
        // copy file and send new file path
        val fileName = getFileName(contentUri)
        if (!TextUtils.isEmpty(fileName))
        {
            val copyFile = File("${context.filesDir}${File.separator}$fileName")
            copy(context, contentUri, copyFile)
            return copyFile.absolutePath
        }
        return null
    }

    private fun getFileName(uri:Uri?):String? {
        if (uri == null) return null
        var fileName:String? = null
        val path = uri.path ?: return null
        val cut = path.lastIndexOf('/')
        if (cut != -1)
        {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }
    private fun copy(context:Context, srcUri:Uri, dstFile:File) {
        try
        {
            val inputStream = context.contentResolver.openInputStream(srcUri) ?: return
            val outputStream = FileOutputStream(dstFile)
            copyFile(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
    }
    @Throws(IOException::class)
    private fun copyFile(inputStream: InputStream, outputStream: OutputStream) {
        val size = 1024 * 2
        val buffer = ByteArray(size)
        val bufferedInputStream = BufferedInputStream(inputStream, size)
        val bufferedOutputStream = BufferedOutputStream(outputStream, size)
        var n:Int
        try
        {
            while (bufferedInputStream.read(buffer, 0, size).also { n = it } >= 0)
            {
                bufferedOutputStream.write(buffer, 0, n)
            }
            bufferedOutputStream.flush()
        }
        finally
        {
            try
            {
                bufferedOutputStream.close()
                bufferedInputStream.close()
            }
            catch (e:IOException) {
                e.printStackTrace()
            }
        }
    }
}
