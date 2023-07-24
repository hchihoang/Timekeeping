package com.timekeeping.smart.ui.home

import android.view.View
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.timekeeping.smart.R
import com.timekeeping.smart.base.BaseFragment
import com.timekeeping.smart.base.adapter.LocationAdapter
import com.timekeeping.smart.base.entity.BaseError
import com.timekeeping.smart.extension.gone
import com.timekeeping.smart.extension.onAvoidDoubleClick
import com.timekeeping.smart.extension.toast
import com.timekeeping.smart.extension.visible
import com.timekeeping.smart.ui.add_location.AddLocationFragment
import com.timekeeping.smart.ui.login.LoginFragment
import com.timekeeping.smart.ui.report.ReportFragment
import com.timekeeping.smart.ui.time_keeping.TimeKeepingFragment
import com.timekeeping.smart.utils.DialogUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.layout_base_recyclerview.view.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private val viewModel: HomeViewModel by viewModels()
    override fun backFromAddFragment() {

    }

    override val layoutId: Int
        get() = R.layout.home_fragment

    override fun initView() {
        checkPermission()
        cv_add_location.gone()
        cv_time_keeping.gone()
    }

    override fun initData() {
        viewModel.getPermission()
    }

    private fun checkPermission(){
        if(viewModel.checkDecentralizationAddLocation()){
            cv_add_location.visible()
        }else{
            cv_add_location.gone()
        }
        if(viewModel.checkDecentralizationTimeKeeping()){
            cv_time_keeping.visible()
        }else{
            cv_time_keeping.gone()
        }
        if(cv_add_location.visibility == View.GONE && cv_time_keeping.visibility == View.GONE){
            tv_notify_none_permission.visible()
        }else{
            tv_notify_none_permission.gone()
        }
    }
    override fun initListener() {
        btn_logout.onAvoidDoubleClick {
            DialogUtil.displayDialog(requireContext(), getString(R.string.str_title_logout),
                getString(R.string.str_message_logout),
                {
                    viewModel.logout()
                },
                {}
            )
        }
        viewModel.logoutLiveData.observe(this) {
            handleObjectResponse(it)
        }

        viewModel.permissionLiveData.observe(this) {
            handleObjectResponse(it)
        }

        cv_add_location.onAvoidDoubleClick {
            getVC().addFragment(AddLocationFragment::class.java)
        }

        cv_time_keeping.onAvoidDoubleClick {
            getVC().addFragment(TimeKeepingFragment::class.java)
        }

        swipeRefresh.setOnRefreshListener( SwipeRefreshLayout.OnRefreshListener {
            viewModel.getPermission()
            swipeRefresh.isRefreshing = false
        })

        cv_report.onAvoidDoubleClick {
            getVC().addFragment(ReportFragment::class.java)
        }

    }

    override fun backPressed(): Boolean {
        return true
    }

    override fun handleNetworkError(throwable: Throwable?, isShowDialog: Boolean) {
        super.handleNetworkError(throwable, isShowDialog)
    }

    override fun handleValidateError(throwable: BaseError?) {
        super.handleValidateError(throwable)
        throwable?.let {
            toast(it.error)
        }
    }

    override fun <U> getObjectResponse(data: U) {
        if(data is Boolean){
            checkPermission()
        }else {
            getVC().replaceFragment(LoginFragment::class.java)
        }
    }

}