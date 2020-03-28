package com.flj.latte.net

import androidx.fragment.app.Fragment
import com.flj.latte.net.callback.*
import com.flj.latte.net.download.DownloadHandler
import com.flj.latte.ui.loader.LatteLoader
import com.flj.latte.ui.loader.LoaderStyle
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import java.io.File
import java.util.*

/**
 * @author 傅令杰
 */

class RestClient internal constructor(
    private val url: String?,
    private val params: LinkedHashMap<String, Any>?,
    private val download_dir: String?,
    private val extension: String?,
    private val name: String?,
    private val request: IRequestStart?,
    private val success: ISuccess?,
    private val failure: IFailure?,
    private val error: IError?,
    private val complete: IComplete?,
    private val body: RequestBody?,
    private val file: File?,
    private val fragment: Fragment?,
    private val loaderStyle: LoaderStyle?
) {

    private val requestCallback: Callback<String>
        get() = RequestCallbacks(
            success,
            failure,
            error,
            complete,
            loaderStyle
        )

    private fun request(method: HttpMethod) {
        val service = RestCreator.restService
        val call: Call<String>?

        request?.onRequestStart()

        if (loaderStyle != null) {
            fragment?.let { LatteLoader.instance.showLoading(it) }
        }

        call = when (method) {
            HttpMethod.GET -> service.get(url, params)
            HttpMethod.POST -> service.post(url, params)
            HttpMethod.POST_JSON -> service.post(url, body)
            HttpMethod.PUT -> service.put(url, params)
            HttpMethod.PUT_JSON -> service.put(url, body)
            HttpMethod.DELETE -> service.delete(url, params)
            HttpMethod.UPLOAD -> {
                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                val requestBody =
                    file?.asRequestBody(MultipartBody.FORM.toString().toMediaTypeOrNull())
                val body =
                    requestBody?.let { MultipartBody.Part.createFormData("file", file?.name, it) }
                service.upload(url, body)
            }
        }

        call.enqueue(requestCallback)
    }

    fun get() {
        request(HttpMethod.GET)
    }

    fun post() {
        if (body == null) {
            request(HttpMethod.POST)
        } else {
            if (!params.isNullOrEmpty()) {
                throw RuntimeException("params must be null!")
            }
            request(HttpMethod.POST_JSON)
        }
    }

    fun put() {
        if (body == null) {
            request(HttpMethod.PUT)
        } else {
            if (!params.isNullOrEmpty()) {
                throw RuntimeException("params must be null!")
            }
            request(HttpMethod.PUT_JSON)
        }
    }

    fun delete() {
        request(HttpMethod.DELETE)
    }

    fun upload() {
        request(HttpMethod.UPLOAD)
    }

    fun download() {
        DownloadHandler(
            url, params, request, complete, download_dir, extension, name,
            success, failure, error
        )
            .handleDownload()
    }

    companion object {

        fun builder(): RestClientBuilder {
            return RestClientBuilder()
        }
    }
}
