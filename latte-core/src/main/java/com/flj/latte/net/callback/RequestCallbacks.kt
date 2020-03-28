package com.flj.latte.net.callback

import android.os.Handler
import com.flj.latte.R

import com.flj.latte.global.Latte
import com.flj.latte.ui.loader.LatteLoader
import com.flj.latte.ui.loader.LoaderStyle
import com.flj.latte.util.log.LogUtil

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author 傅令杰
 */

class RequestCallbacks(
    private val success: ISuccess?,
    private val failure: IFailure?,
    private val error: IError?,
    private val complete: IComplete?,
    private val loaderStyle: LoaderStyle?
) : Callback<String> {

    override fun onResponse(call: Call<String>, response: Response<String>) {
        if (response.isSuccessful) {
            if (call.isExecuted) {
                if (success != null) {
                    val body = response.body()
                    if (body != null) {
                        success.onSuccess(body)
                    }
                }
            }
        } else {
            if (error != null) {
                error.onError(response.code(), response.message())
                LogUtil.e(
                    javaClass.simpleName,
                    response.code().toString() + " " + response.message() + " " + response.errorBody()
                )
            }
        }

        onRequestFinish()
    }

    override fun onFailure(call: Call<String>, t: Throwable) {
        LogUtil.d("ON_FAILURE", t)
        if (failure != null) {
            failure.onFailure()
            LogUtil.e(javaClass.simpleName, t.localizedMessage)
        }
        onRequestFinish()
    }

    private fun onRequestFinish() {
        val delayed = Latte.instance.getConfiguration<Long>(R.id.key_loader_delayed)
        if (loaderStyle != null) {
            HANDLER.postDelayed({ LatteLoader.instance.stopLoading() }, delayed)
        }
        complete?.onComplete()
    }

    companion object {
        private val HANDLER = Latte.instance.getConfiguration<Handler>(R.id.key_handler)
    }
}
