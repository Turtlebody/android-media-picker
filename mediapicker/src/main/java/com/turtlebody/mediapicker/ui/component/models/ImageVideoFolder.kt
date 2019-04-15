package com.turtlebody.mediapicker.ui.component.models

/**
 * Created by WANGSUN on 26-Mar-19.
 */
data class ImageVideoFolder(var id: String = "",
                            var name: String = "",
                            var coverImageFilePath: String = "",
                            var contentCount: Int = 0) {

    companion object {
        val FOLDER_ID = "folderId"
    }
}
