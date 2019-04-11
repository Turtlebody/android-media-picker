package com.turtlebody.mediapicker.models

import java.io.Serializable

/**
 * Created by WANGSUN on 26-Mar-19.
 */
data class Doc(var id: String = "",
               var name: String = "",
               var filePath: String = "",
               var size: String?,
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

        if (!(o is Doc)) {
            return false
        }

        return id == o.id
    }
}
