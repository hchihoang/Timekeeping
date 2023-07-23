package com.timekeeping.smart.ui.splash

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import com.timekeeping.smart.R
import com.timekeeping.smart.base.BaseFragment
import com.timekeeping.smart.ui.home.HomeFragment
import com.timekeeping.smart.ui.login.LoginFragment
import com.timekeeping.smart.ui.login.LoginViewModel
import com.timekeeping.smart.ui.time_keeping.TimeKeepingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment() {
    private val viewModel: LoginViewModel by viewModels()
    override val layoutId: Int
        get() = R.layout.splash_fragment

    override fun backFromAddFragment() {
    }

    override fun backPressed(): Boolean {
        return true
    }

    override fun initView() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (viewModel.isLogin()) {
                getVC().replaceFragment(HomeFragment::class.java)
            } else {
                getVC().replaceFragment(LoginFragment::class.java)
            }
        },1000)
    }

    override fun initData() {

    }

    override fun initListener() {

    }
}
