package com.greentoad.turtlebody.mediapicker.ui.component.media.image

import java.io.Serializable

/**
 * Created by WANGSUN on 26-Mar-19.
 */
data class ImageModel(var id: String = "",
                      var name: String = "",
                      var size: Int=0,
                      var filePath: String = "",
                      var thumbnailPath: String = "",
                      var isSelected: Boolean = false ): Serializable {

    companion object {
        val ARG_BUNDLE = javaClass.canonicalName + ".bundle_arg"
        val ARG_LIST = javaClass.canonicalName + ".list_arg"
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }

        if (o == null) {
            return false
        }

        if (!(o is ImageModel)) {
            return false
        }

        return id == o.id
    }
}
