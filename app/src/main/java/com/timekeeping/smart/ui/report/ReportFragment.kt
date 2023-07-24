package com.timekeeping.smart.ui.report

import androidx.fragment.app.viewModels
import com.timekeeping.smart.R
import com.timekeeping.smart.base.BaseFragment
import kotlinx.android.synthetic.main.time_keeping_fragment.header

class ReportFragment : BaseFragment() {
    private val viewModel: ReportViewModel by viewModels()


    override fun backFromAddFragment() {

    }

    override val layoutId: Int
        get() = R.layout.fragment_report

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initListener() {
        header.onLeftClick = {
            backPressed()
        }
    }

    override fun backPressed(): Boolean {
        getVC().backFromAddFragment()
        return false
    }


}