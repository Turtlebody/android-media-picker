package com.greentoad.turtlebody.mediapicker.ui.component.media.audio

import java.io.Serializable

/**
 * Created by WANGSUN on 26-Mar-19.
 */
data class AudioModel(var id: String = "",
                      var name: String = "",
                      var size: Int=0,
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

        if (!(other is AudioModel)) {
            return false
        }

        return id == other.id
    }
}
