package com.timekeeping.smart.custom.dialog

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import com.timekeeping.smart.R
import com.timekeeping.smart.base.dialog.BaseFullScreenDialog
import com.timekeeping.smart.extension.onAvoidDoubleClick
import kotlinx.android.synthetic.main.dialog_image_detail.imbtn_close
import kotlinx.android.synthetic.main.dialog_image_detail.imv_image

class DetailImageDialog(context: Context) : BaseFullScreenDialog(){
    override fun initView() {
        isCancelable = false
        arguments?.let {
            if (it.containsKey(KEY_IMAGE_PREVIEW)) {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.getParcelable(KEY_IMAGE_PREVIEW, Bitmap::class.java)
                } else {
                    it.getParcelable(KEY_IMAGE_PREVIEW)
                }
                imv_image.setImageBitmap(bitmap)
            }
        }
    }

    override fun initListener() {
        imbtn_close.onAvoidDoubleClick {
            dismissAllowingStateLoss()
        }
    }

    override val layoutId: Int
        get() = R.layout.dialog_image_detail

    companion object {
        fun newInstance(bitmap: Bitmap, context: Context): DetailImageDialog {
            return DetailImageDialog(context).apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_IMAGE_PREVIEW, bitmap)
                }
            }
        }

        private const val KEY_IMAGE_PREVIEW = "KEY_IMAGE_PREVIEW"
    }
}