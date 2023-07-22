package com.timekeeping.smart.ui.time_keeping

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.timekeeping.smart.R
import com.timekeeping.smart.base.BaseFragment
import com.timekeeping.smart.base.adapter.ProgressAdapter
import com.timekeeping.smart.base.custom.dialog.SelectTimeProgressDialog
import com.timekeeping.smart.base.entity.BaseError
import com.timekeeping.smart.entity.request.DateRequest
import com.timekeeping.smart.entity.response.ProgressResponse
import com.timekeeping.smart.extension.onAvoidDoubleClick
import com.timekeeping.smart.extension.toast
import com.timekeeping.smart.utils.Constant
import com.timekeeping.smart.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.time_keeping_fragment.btn_logout
import kotlinx.android.synthetic.main.time_keeping_fragment.rcv_time_keeping
import java.util.Date
import javax.inject.Inject


@AndroidEntryPoint
class TimeKeepingFragment : BaseFragment() {
    @Inject
    lateinit var adapter: ProgressAdapter
    private val viewModel: TimeKeepingViewModel by viewModels()
    private val selectTimeProgressDialog: SelectTimeProgressDialog by lazy {
        SelectTimeProgressDialog(
            requireContext()
        )
    }
    override val layoutId: Int
        get() = R.layout.time_keeping_fragment

    override fun backFromAddFragment() {
    }

    override fun backPressed(): Boolean {
        getVC().backFromAddFragment()
        return false
    }

    override fun initView() {
        rcv_time_keeping.apply {
            setAdapter(adapter)
            setListLayoutManager(LinearLayoutManager.VERTICAL)
        }
    }

    override fun initData() {
        adapter.clear()
        val dataRequest = DateRequest(
            DateUtils.dateToString(Date(), Constant.DATE_FORMAT_1),
            DateUtils.dateToString(Date(), Constant.DATE_FORMAT_1)
        )
        viewModel.getDataProgress(dataRequest)
    }

    override fun initListener() {
        btn_logout.onAvoidDoubleClick {

        }
        viewModel.dataProgressResponse.observe(viewLifecycleOwner) {
            handleListResponse(it)
        }
    }

    override fun <U> getListResponse(data: List<U>?) {
        adapter.refresh(data as List<ProgressResponse>)
    }


    private fun selectData() {
        selectTimeProgressDialog.show()
        selectTimeProgressDialog.onClickBtnYes = {
            adapter.clear()
            viewModel.getDataProgress(it)
        }
    }

    override fun handleValidateError(throwable: BaseError?) {
        throwable?.let {
            toast(it.error)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
