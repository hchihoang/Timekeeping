package com.timekeeping.smart.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.timekeeping.smart.R
import com.timekeeping.smart.base.BaseFragment
import com.timekeeping.smart.extension.onAvoidDoubleClick
import com.timekeeping.smart.extension.toast
import com.timekeeping.smart.ui.login.LoginFragment
import com.timekeeping.smart.utils.Constant
import com.timekeeping.smart.utils.DeviceUtil
import com.timekeeping.smart.utils.DialogUtil
import com.timekeeping.smart.utils.PermissionUtil
import com.timekeeping.smart.utils.PermissionUtil.startApplicationSettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_fragment.btn_add
import kotlinx.android.synthetic.main.home_fragment.btn_logout
import kotlinx.android.synthetic.main.home_fragment.input_name
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val viewModel: HomeViewModel by viewModels()
    override fun backFromAddFragment() {

    }

    override val layoutId: Int
        get() = R.layout.home_fragment

    override fun initView() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
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
        btn_add.onAvoidDoubleClick {
            if(input_name.getText().isEmpty()){
                toast(getString(R.string.str_empty_name))
            }else{
                DeviceUtil.hideSoftKeyboard(activity)
                requestPermissionLocation()
            }
        }

    }

    override fun backPressed(): Boolean {
        return true
    }

    override fun <U> getObjectResponse(data: U) {
        getVC().replaceFragment(LoginFragment::class.java)
    }

    private fun requestPermissionLocation(){
        requestPermissionLauncher.launch(Constant.REQUIRED_PERMISSIONS_LOCATION)
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (isLocationEnabled()) {
            mFusedLocationClient.lastLocation.addOnCompleteListener(activity!!) { task ->
                val location: Location? = task.result
                if (location != null) {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val list: List<Address>? =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    list?.let {
                        toast(
                            "La: " + list.firstOrNull()?.latitude +
                                    " Lo: " + list.firstOrNull()?.longitude +
                                    " Country name: " + list.firstOrNull()?.countryName +
                                    " Locality: " + list.firstOrNull()?.locality +
                                    " Address: " + list.firstOrNull()?.getAddressLine(0)
                        )
                    }
                }else{
                    toast(getString(R.string.str_can_not_get_location))
                }
            }
        } else {
            toast(getString(R.string.str_turn_on_location))
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
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