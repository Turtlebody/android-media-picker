package com.greentoad.turtlebody.mediapicker.util

/**
 * Created by WANGSUN on 14-Mar-19.
 */
class UtilMime {

    /**********************************************
     * File types: to set type for intent
     ********************************************/
    object FileType {
        val DEFAULT = "*/*"
        val VIDEO = "video/*"
        val AUDIO = "audio/*"
        val IMAGE = "image/*"
        val DOC = "application/*"

    }

    /************************************
     * MIME types
     ***********************************/
    object MimeType {
        val DEFAULT = arrayOf("*/*")
        val VIDEO = arrayOf("video/*")
        val AUDIO = arrayOf("audio/*")
        val IMAGE = arrayOf("image/*")

        /*Custom MimeTypes*/
        val DOC = arrayOf("application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                "text/plain", "application/pdf", "application/zip")

        /*******************************
         * specific mime type
         ******************************/
        val ONLY_JPEG = arrayOf("image/jpeg")
        val ONLY_MP3 = arrayOf("audio/mpeg")
        val ONLY_MP4 = arrayOf("video/mp4")
        val ONLY_PDF = arrayOf("application/pdf")
        /*check this link for more mimeTypes
         * https://www.lifewire.com/file-extensions-and-mime-types-3469109*/
    }

}
