package com.timekeeping.smart.ui.add_location

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.timekeeping.smart.R
import com.timekeeping.smart.base.BaseFragment
import com.timekeeping.smart.base.adapter.LocationAdapter
import com.timekeeping.smart.base.entity.BaseError
import com.timekeeping.smart.entity.response.LocationResponse
import com.timekeeping.smart.extension.gone
import com.timekeeping.smart.extension.onAvoidDoubleClick
import com.timekeeping.smart.extension.onRefresh
import com.timekeeping.smart.extension.toast
import com.timekeeping.smart.extension.visible
import com.timekeeping.smart.rx.RxBus
import com.timekeeping.smart.rx.RxEvent
import com.timekeeping.smart.ui.login.LoginFragment
import com.timekeeping.smart.utils.Constant
import com.timekeeping.smart.utils.Define
import com.timekeeping.smart.utils.DeviceUtil
import com.timekeeping.smart.utils.PermissionUtil
import com.timekeeping.smart.utils.PermissionUtil.startApplicationSettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_location_fragment.btn_add
import kotlinx.android.synthetic.main.add_location_fragment.header
import kotlinx.android.synthetic.main.add_location_fragment.input_name
import kotlinx.android.synthetic.main.add_location_fragment.ll_list
import kotlinx.android.synthetic.main.add_location_fragment.rcv_list
import kotlinx.android.synthetic.main.add_location_fragment.tv_notify
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AddLocationFragment : BaseFragment() {
    @Inject
    lateinit var adapter: LocationAdapter
    private val viewModel: AddLocationViewModel by viewModels()
    override fun backFromAddFragment() {

    }

    override val layoutId: Int
        get() = R.layout.add_location_fragment

    override fun initView() {
        rcv_list.apply {
            setAdapter(adapter)
            setListLayoutManager(LinearLayoutManager.VERTICAL)
            onRefresh {
                adapter.clear()
                viewModel.getDataLocation()
                enableRefresh(false)
            }
        }
    }

    override fun initData() {
        viewModel.getDataLocation()
    }

    @SuppressLint("CheckResult")
    override fun initListener() {
        header.onLeftClick = {
            backPressed()
        }

        viewModel.logoutLiveData.observe(this) {
            handleObjectResponse(it)
        }

        viewModel.addLocationResponse.observe(this) {
            when (it.type) {
                Define.ResponseStatus.LOADING -> showLoading()
                Define.ResponseStatus.SUCCESS -> {
                    toast(getString(R.string.str_add_success))
                }

                Define.ResponseStatus.ERROR -> {
                    hideLoading()
                    if (it.isShowingError) {
                        handleNetworkError(it.error, true)
                    } else {
                        handleValidateError(it.error as? BaseError?)
                    }
                }
            }
        }
        btn_add.onAvoidDoubleClick {
            if (input_name.getText().isEmpty()) {
                toast(getString(R.string.str_empty_name))
            } else {
                DeviceUtil.hideSoftKeyboard(activity)
                requestPermissionLocation()
            }
        }
        viewModel.locationResponse.observe(this) {
            handleListResponse(it)
        }
        RxBus.listen(RxEvent.NotifyLocation::class.java)
            .subscribe(
                {
                    setLocation()
                },
                {

                }
            )
    }

    private fun setLocation() {
        val locationResult = RxBus.locationResult.value?.locationResult
        if (locationResult != null) {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val list: List<Address>? =
                locationResult.lastLocation?.latitude?.let {
                    locationResult.lastLocation?.longitude?.let { it1 ->
                        geocoder.getFromLocation(
                            it,
                            it1, 1
                        )
                    }
                }
            list?.let {
                viewModel.addLocation(
                    input_name.getText(), list.firstOrNull()?.longitude,
                    list.firstOrNull()?.latitude, list.firstOrNull()?.getAddressLine(0)
                )
            } ?: kotlin.run {
                locationResult.lastLocation?.latitude?.let {
                    locationResult.lastLocation?.longitude?.let { it1 ->
                        viewModel.addLocation(
                            input_name.getText(), it1,
                            it, ""
                        )
                    }
                }
            }
        } else {
            RxBus.publish(RxEvent.RequestLocation())
        }
    }

    override fun handleValidateError(throwable: BaseError?) {
        super.handleValidateError(throwable)
        adapter.setIsLoading(false)
        throwable?.let {
            toast(it.error)
        }
    }

    override fun handleNetworkError(throwable: Throwable?, isShowDialog: Boolean) {
        super.handleNetworkError(throwable, isShowDialog)
        adapter.setIsLoading(false)
    }

    override fun <U> getListResponse(data: List<U>?) {
        if ((data?.size ?: 0) > 0) {
            ll_list.visible()
            tv_notify.gone()
        }
        adapter.refresh(data as List<LocationResponse>)
    }

    override fun backPressed(): Boolean {
        getVC().backFromAddFragment()
        return false
    }

    override fun <U> getObjectResponse(data: U) {
        getVC().replaceFragment(LoginFragment::class.java)
    }

    private fun requestPermissionLocation() {
        requestPermissionLauncher.launch(Constant.REQUIRED_PERMISSIONS_LOCATION)
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (DeviceUtil.isLocationEnabled(requireContext())) {
            showLoading()
            setLocation()
        } else {
            toast(getString(R.string.str_turn_on_location))
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result: Map<String, Boolean> ->
            val deniedList: List<String> = result.filter {
                !it.value
            }.map {
                it.key
            }

            when {
                deniedList.isNotEmpty() -> {
                    val map = deniedList.groupBy { permission ->
                        if (shouldShowRequestPermissionRationale(permission))
                            PermissionUtil.DENIED else PermissionUtil.BLOCKED_OR_NEVER_ASKED
                    }
                    map[PermissionUtil.DENIED]?.let {
                        // request denied , request again
                        toast(getString(R.string.str_request_location))
                        lifecycleScope.launch {
                            delay(300)
                            requestPermissionLocation()
                        }
                    }
                    map[PermissionUtil.BLOCKED_OR_NEVER_ASKED]?.let {
                        //request denied ,send to settings
                        requireContext().startApplicationSettingsActivity()
                    }

                }

                else -> {
                    //All request are permitted
                    getLocation()
                }
            }
        }

}