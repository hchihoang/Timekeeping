package com.timekeeping.smart.ui.home

import androidx.fragment.app.viewModels
import com.timekeeping.smart.R
import com.timekeeping.smart.base.BaseFragment
import com.timekeeping.smart.base.adapter.LocationAdapter
import com.timekeeping.smart.extension.gone
import com.timekeeping.smart.extension.onAvoidDoubleClick
import com.timekeeping.smart.extension.visible
import com.timekeeping.smart.ui.add_location.AddLocationFragment
import com.timekeeping.smart.ui.login.LoginFragment
import com.timekeeping.smart.ui.time_keeping.TimeKeepingFragment
import com.timekeeping.smart.utils.DialogUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_fragment.btn_logout
import kotlinx.android.synthetic.main.home_fragment.cv_add_location
import kotlinx.android.synthetic.main.home_fragment.cv_time_keeping
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private val viewModel: HomeViewModel by viewModels()
    override fun backFromAddFragment() {

    }

    override val layoutId: Int
        get() = R.layout.home_fragment

    override fun initView() {
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
    }

    override fun initData() {

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

        cv_add_location.onAvoidDoubleClick {
            getVC().addFragment(AddLocationFragment::class.java)
        }

        cv_time_keeping.onAvoidDoubleClick {
            getVC().addFragment(TimeKeepingFragment::class.java)
        }

    }

    override fun backPressed(): Boolean {
        return true
    }

    override fun <U> getObjectResponse(data: U) {
        getVC().replaceFragment(LoginFragment::class.java)
    }

}