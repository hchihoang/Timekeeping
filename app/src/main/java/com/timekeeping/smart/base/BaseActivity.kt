package com.timekeeping.smart.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.timekeeping.smart.R
import com.timekeeping.smart.base.custom.HSBALoadingDialog
import com.timekeeping.smart.extension.enableFullScreen
import com.timekeeping.smart.extension.toast
import com.timekeeping.smart.network.NetworkCheckerInterceptor
import com.timekeeping.smart.network.entity.RequestError
import com.timekeeping.smart.utils.Define
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.system.exitProcess

abstract class BaseActivity : AppCompatActivity() {

    private var viewController: ViewController? = null

    fun getViewController(): ViewController {
        if (viewController == null) {
            viewController = ViewController(layoutId, supportFragmentManager)

        }
        return viewController!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        viewController = ViewController(layoutId, supportFragmentManager)
        enableFullScreen()
        initView()
        initData()
        initListener()
    }

    override fun onBackPressed() {
        if (viewController != null && viewController?.currentFragment != null) {
            if (viewController?.currentFragment?.backPressed() == true) {
                HSBALoadingDialog.getInstance(this).destroyLoadingDialog()
               finish()
            }
        } else {
            HSBALoadingDialog.getInstance(this).destroyLoadingDialog()
            finish()
        }

    }

    open fun handleNetworkError(throwable: Throwable?, isShowDialog: Boolean): RequestError? {
        var requestError = RequestError()
        if (throwable is NetworkCheckerInterceptor.NoConnectivityException) {
            requestError.errorCode = (Define.Api.NO_NETWORK_CONNECTION)
            requestError.errorMessage = (throwable.message)
        } else if (throwable is HttpException) {
            val errorBody: String
            try {
                throwable.response()?.errorBody()?.let {
                    errorBody = it.string()
                    val gson = GsonBuilder().create()
                    requestError = gson.fromJson(errorBody, RequestError::class.java)
                } ?: run {
                    requestError.errorCode = (Define.Api.TIME_OUT)
                    requestError.errorMessage = (getString(R.string.error_place_holder))
                }
                //                ApiObjectResponse apiResponse = gson.fromJson(errorBody, ApiObjectResponse.class);
//                if (apiResponse != null && apiResponse.getRequestError() != null) {
//                    requestError = apiResponse.getRequestError();
//                } else {
//                    requestError.errorCode(String.valueOf(httpException.code()));
//                    requestError.errorMessage =(getString(R.string.error_place_holder));
//                }
            } catch (e: IOException) {
                requestError.errorCode = (Define.Api.TIME_OUT)
                requestError.errorMessage = (getString(R.string.error_place_holder))
            } catch (e: IllegalStateException) {
                requestError.errorCode = (Define.Api.TIME_OUT)
                requestError.errorMessage = (getString(R.string.error_place_holder))
            } catch (e: JsonSyntaxException) {
                requestError.errorCode = (Define.Api.TIME_OUT)
                requestError.errorMessage = (getString(R.string.error_place_holder))
            }
        } else if (throwable is ConnectException
            || throwable is SocketTimeoutException
            || throwable is UnknownHostException
            || throwable is IOException
        ) {
            requestError.errorCode = (Define.Api.TIME_OUT)
            requestError.errorMessage = (getString(R.string.timeout_error))
        } else {
            requestError.errorCode = (Define.Api.UNKNOWN)
            requestError.errorMessage = (getString(R.string.error_place_holder))
        }
        if (isShowDialog) {
            requestError.errorMessage?.let {
                toast(it)
            }
        }
        return requestError
    }


    protected abstract val layoutResId: Int
    protected abstract val layoutId: Int
    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initListener()

}