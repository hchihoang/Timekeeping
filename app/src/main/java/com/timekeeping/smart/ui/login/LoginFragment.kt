package com.timekeeping.smart.ui.login

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.timekeeping.smart.R
import com.timekeeping.smart.base.BaseFragment
import com.timekeeping.smart.base.entity.BaseError
import com.timekeeping.smart.entity.response.LoginResponse
import com.timekeeping.smart.extension.onAvoidDoubleClick
import com.timekeeping.smart.extension.toast
import com.timekeeping.smart.ui.home.HomeFragment
import com.timekeeping.smart.ui.time_keeping.TimeKeepingFragment
import com.timekeeping.smart.utils.DeviceUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.login_fragment.*

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private val viewModel: LoginViewModel by viewModels()
    override val layoutId: Int
        get() = R.layout.login_fragment

    override fun backFromAddFragment() {
    }

    override fun backPressed(): Boolean {
        return true
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initListener() {
        btn_login.onAvoidDoubleClick {
            DeviceUtil.hideSoftKeyboard(activity)
            viewModel.login(login_edt_username.getText(), login_edt_password.getText())
            //getVC().replaceFragment(HomeFragment::class.java, null)
        }
        viewModel.loginResponse.observe(viewLifecycleOwner) {
            handleObjectResponse(it)
        }
    }

    override fun handleValidateError(throwable: BaseError?) {
        throwable?.let {
            toast(it.error)
        }
    }
    override fun <U> getObjectResponse(data: U) {
        if(data is LoginResponse && !data.maNV.isNullOrEmpty()) {
            getVC().replaceFragment(HomeFragment::class.java, null)
        }else{
            toast(getString(R.string.str_login_false))
        }
    }
}
