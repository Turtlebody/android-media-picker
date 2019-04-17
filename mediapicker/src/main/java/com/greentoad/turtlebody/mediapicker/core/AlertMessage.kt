package com.greentoad.turtlebody.mediapicker.core

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar

/**
 * Created by WANGSUN on 17-Apr-19.
 */
class AlertMessage() {

    companion object {
        val MESSAGE = "Some file is missing."
    }

    fun showToast(context: Context) {
        Toast.makeText(context, MESSAGE, Toast.LENGTH_LONG).show()
    }

    fun showDialog(context: Context) {
        AlertDialog.Builder(context)
                .setMessage(MESSAGE)
                .setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                }.show()

    }

    fun showSnackBar(view: View) {
        Snackbar.make(view, MESSAGE, Snackbar.LENGTH_LONG).show()
    }

}