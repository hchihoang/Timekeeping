package com.timekeeping.smart.ui.time_keeping

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
import com.timekeeping.smart.base.adapter.TimeKeepingAdapter
import com.timekeeping.smart.base.custom.dialog.SelectTimeProgressDialog
import com.timekeeping.smart.base.entity.BaseError
import com.timekeeping.smart.entity.request.DateRequest
import com.timekeeping.smart.entity.response.LocationResponse
import com.timekeeping.smart.extension.*
import com.timekeeping.smart.rx.RxBus
import com.timekeeping.smart.rx.RxEvent
import com.timekeeping.smart.utils.*
import com.timekeeping.smart.utils.PermissionUtil.startApplicationSettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.time_keeping_fragment.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TimeKeepingFragment : BaseFragment() {
    @Inject
    lateinit var adapter: TimeKeepingAdapter
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
            onRefresh {
                adapter.clear()
                viewModel.dateRequest?.let {
                    viewModel.getDataTimeKeeping(it)
                }
                enableRefresh(false)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        val dataRequest = DateRequest(
            DateUtils.dateToString(Date(), Constant.DATE_FORMAT_1),
            DateUtils.dateToString(Date(), Constant.DATE_FORMAT_1)
        )
        viewModel.getDataTimeKeeping(dataRequest)
        tv_name.text = viewModel.getNameUser()
        tv_title_list.text =
            getString(R.string.str_title_list_keeping) + "\n" + DateUtils.dateToString(
                Date(), Constant.DATE_FORMAT_2
            )

    }

    @SuppressLint("CheckResult")
    override fun initListener() {
        viewModel.timeKeepingResponse.observe(viewLifecycleOwner) {
            handleListResponse(it)
        }
        header.onLeftClick = {
            backPressed()
        }
        btn_time_keeping.onAvoidDoubleClick {
            requestPermissionLocation()
        }
        viewModel.addTimekeepingResponse.observe(this) {
            when (it.type) {
                Define.ResponseStatus.LOADING -> showLoading()
                Define.ResponseStatus.SUCCESS -> {
                    toast(it.data?.result ?: "")
                    if (it.data?.isChecking == false) {
                        hideLoading()
                    }
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
        tv_title_list.onAvoidDoubleClick {
            selectTimeProgressDialog.show()
            selectTimeProgressDialog.onClickBtnYes = {
                tv_title_list.text =
                    getString(R.string.str_title_list_keeping) + "\n" + it.startDate + " - " + it.endDate
                adapter.clear()
                viewModel.getDataTimeKeeping(it)
            }
        }

        RxBus.listen(RxEvent.NotifyLocation::class.java)
            .subscribe(
                {
                    setTimekeeping()
                },
                {

                }
            )
    }

    private fun requestPermissionLocation() {
        requestPermissionLauncher.launch(Constant.REQUIRED_PERMISSIONS_LOCATION)
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

    private fun setTimekeeping() {
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
                viewModel.timekeeping(
                    list.firstOrNull()?.longitude,
                    list.firstOrNull()?.latitude,
                    list.firstOrNull()?.getAddressLine(0)
                )
            } ?: kotlin.run {
                locationResult.lastLocation?.latitude?.let {
                    locationResult.lastLocation?.longitude?.let { it1 ->
                        viewModel.timekeeping(
                            it1,
                            it, ""
                        )
                    }
                }
            }
        } else {
            RxBus.publish(RxEvent.RequestLocation())
        }

    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (DeviceUtil.isLocationEnabled(requireContext())) {
            showLoading()
            setTimekeeping()
        } else {
            toast(getString(R.string.str_turn_on_location))
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
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
            ll_list_keeping.visible()
            tv_notify.gone()
        }
        adapter.refresh(data as List<LocationResponse>)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
