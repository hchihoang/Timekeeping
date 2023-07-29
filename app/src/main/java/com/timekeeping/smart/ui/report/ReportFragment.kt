package com.timekeeping.smart.ui.report

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.timekeeping.smart.R
import com.timekeeping.smart.base.BaseFragment
import com.timekeeping.smart.custom.dialog.DetailImageDialog
import com.timekeeping.smart.extension.onAvoidDoubleClick
import com.timekeeping.smart.extension.toast
import com.timekeeping.smart.utils.Constant
import com.timekeeping.smart.utils.PermissionUtil
import com.timekeeping.smart.utils.PermissionUtil.startApplicationSettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_report.btn_take_a_photo
import kotlinx.android.synthetic.main.fragment_report.header
import kotlinx.android.synthetic.main.fragment_report.imv_image
import kotlinx.android.synthetic.main.fragment_report.input_expense
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ReportFragment : BaseFragment() {
    private val viewModel: ReportViewModel by viewModels()
    override fun backFromAddFragment() {

    }

    override val layoutId: Int
        get() = R.layout.fragment_report

    override fun initView() {
        input_expense.restrictInputOnlyNumber()
    }

    override fun initData() {

    }

    override fun initListener() {
        header.onLeftClick = {
            backPressed()
        }

        btn_take_a_photo.onAvoidDoubleClick {
            requestPermission()
        }
        imv_image.onAvoidDoubleClick {
            viewModel.imageReport?.let {
                val previewFileDialog = DetailImageDialog.newInstance(it, requireContext())
                previewFileDialog.show(childFragmentManager, previewFileDialog.tag)
            }
        }
    }

    override fun backPressed(): Boolean {
        getVC().backFromAddFragment()
        return false
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
                            requestPermission()
                        }
                    }
                    map[PermissionUtil.BLOCKED_OR_NEVER_ASKED]?.let {
                        //request denied ,send to settings
                        requireContext().startApplicationSettingsActivity()
                    }

                }

                else -> {
                    //All request are permitted
                    openCamera()
                }
            }
        }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Constant.REQUIRED_PERMISSIONS_READ_ANDROID_33)
        } else {
            requestPermissionLauncher.launch(Constant.REQUIRED_PERMISSIONS_READ)
        }

    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, Constant.REQUEST_IMAGE_CAPTURE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode === Activity.RESULT_CANCELED) {
            return
        }
        if (requestCode === Constant.REQUEST_IMAGE_CAPTURE) {
            val thumbnail = data?.extras?.get("data")
            thumbnail?.let {
                if (it is Bitmap) {
                    imv_image.setImageBitmap(it)
                    viewModel.imageReport = it
                }
            }
        }
    }

}