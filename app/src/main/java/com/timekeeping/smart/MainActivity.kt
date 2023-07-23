package com.timekeeping.smart

import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.timekeeping.smart.base.BaseActivity
import com.timekeeping.smart.extension.onAvoidDoubleClick
import com.timekeeping.smart.ui.home.HomeFragment
import com.timekeeping.smart.ui.login.LoginFragment
import com.timekeeping.smart.ui.login.LoginViewModel
import com.timekeeping.smart.ui.splash.SplashFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_fragment.*

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override val layoutResId: Int
        get() = R.layout.activity_main
    override val layoutId: Int
        get() = R.id.container

    override fun initListener() {

    }

    override fun initView() {
        getViewController().addFragment(SplashFragment::class.java)
    }

    override fun initData() {
    }
}
