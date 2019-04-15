package com.turtlebody.mediapicker.ui.component.models

import java.io.Serializable

/**
 * Created by WANGSUN on 26-Mar-19.
 */
data class Audio(var id: String = "",
                 var name: String = "",
                 var size: Int,
                 var filePath: String = "",
        var mimeType:String? ="",
                 //TODo var artist: String = "",
                 var isSelected: Boolean = false ): Serializable {



    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null) {
            return false
        }

        if (!(other is Audio)) {
            return false
        }

        return id == other.id
    }
}
