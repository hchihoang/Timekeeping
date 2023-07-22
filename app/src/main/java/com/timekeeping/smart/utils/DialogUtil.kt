package com.timekeeping.smart.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

object DialogUtil {

    fun displayDialog(
        context: Context,
        title: String,
        message: String,
        action: () -> Unit,
        cancel: () -> Unit,
        isShowCancel: Boolean = true
    ) {

        val alertDialog = AlertDialog.Builder(context)
            .create()
        alertDialog.setTitle(title)
        alertDialog.setCancelable(false)

        alertDialog.setMessage(message)
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.ok)
        ) { dialog, _ ->
            action()
            dialog.dismiss()
        }
        if (isShowCancel) {
            alertDialog.setButton(
                AlertDialog.BUTTON_NEGATIVE, context.getString(android.R.string.cancel)
            ) { dialog, _ ->
                cancel()
                dialog.dismiss()
            }
        }
        alertDialog.show()
    }


}