package com.flj.latte.net.download

import android.os.AsyncTask
import com.flj.latte.net.RestCreator
import com.flj.latte.net.callback.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author 傅令杰
 */

class DownloadHandler(
    private val url: String?,
    private val params: LinkedHashMap<String, Any>?,
    private val request: IRequestStart?,
    private val complete: IComplete?,
    private val downloadDir: String?,
    private val extension: String?,
    private val name: String?,
    private val success: ISuccess?,
    private val failure: IFailure?,
    private val error: IError?
) {

    fun handleDownload() {
        request?.onRequestStart()

        RestCreator
            .restService
            .download(url, params)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val task = SaveFileTask(complete, success)
                        task.executeOnExecutor(
                            AsyncTask.THREAD_POOL_EXECUTOR,
                            downloadDir, extension, responseBody, name
                        )

                        //这里一定要注意判断，否则文件下载不全
                        if (task.isCancelled) {
                            complete?.onComplete()
                        }
                    } else {
                        error?.onError(response.code(), response.message())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    failure?.onFailure()
                }
            })
    }
}
