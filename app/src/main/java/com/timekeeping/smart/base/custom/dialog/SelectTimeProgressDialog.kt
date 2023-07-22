package com.timekeeping.smart.base.custom.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.timekeeping.smart.R
import com.timekeeping.smart.entity.request.DateRequest
import com.timekeeping.smart.extension.*
import com.timekeeping.smart.utils.Constant
import com.timekeeping.smart.utils.DateUtils
import kotlinx.android.synthetic.main.view_custom_dialog.btn_no
import kotlinx.android.synthetic.main.view_custom_dialog.btn_yes
import kotlinx.android.synthetic.main.view_custom_dialog.tv_end_date
import kotlinx.android.synthetic.main.view_custom_dialog.tv_start_date
import java.util.Date

class SelectTimeProgressDialog(context: Context) : Dialog(context) {

    var onClickBtnYes: (DateRequest) -> Unit = {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.view_custom_dialog)
        this.window?.setBackgroundDrawableResource(R.drawable.cus_bg_custom_dialog)
        this.window?.setLayout(
            (Resources.getSystem().displayMetrics.widthPixels * .5).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        tv_start_date.text = DateUtils.dateToString(Date(), Constant.DATE_FORMAT_2)
        tv_end_date.text = DateUtils.dateToString(Date(), Constant.DATE_FORMAT_2)
        tv_start_date.onAvoidDoubleClick {
            val stringDate = tv_start_date.text.toString()
            val currentDate = if (stringDate.isBlank()) Date() else DateUtils.stringToDate(
                stringDate,
                Constant.DATE_FORMAT_2
            )
            showDatePickerDialog(beforeDate = currentDate, context = this.context,
                callBack = {
                    tv_start_date.text = it
                })
        }

        tv_end_date.onAvoidDoubleClick {
            val stringDate = tv_end_date.text.toString()
            val currentDate = if (stringDate.isBlank()) Date() else DateUtils.stringToDate(
                stringDate,
                Constant.DATE_FORMAT_2
            )
            showDatePickerDialog(beforeDate = currentDate, context = this.context,
                callBack = {
                    tv_end_date.text = it
                })
        }
        btn_no.onAvoidDoubleClick {
            this.dismiss()
        }

        btn_yes.onAvoidDoubleClick {
            val startDate = DateUtils.stringToDate(
                tv_start_date.text.toString(),
                Constant.DATE_FORMAT_2
            )
            val endDate = DateUtils.stringToDate(
                tv_end_date.text.toString(),
                Constant.DATE_FORMAT_2
            )
            onClickBtnYes(
                DateRequest(DateUtils.dateToString(startDate, Constant.DATE_FORMAT_1),
                DateUtils.dateToString(endDate, Constant.DATE_FORMAT_1))
            )
            this.dismiss()
        }

    }
}