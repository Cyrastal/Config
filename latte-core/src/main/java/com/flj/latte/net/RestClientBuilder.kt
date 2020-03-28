package com.flj.latte.net

import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSONObject
import com.flj.latte.net.callback.*
import com.flj.latte.ui.loader.LoaderStyle
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

/**
 * @author 傅令杰
 */

class RestClientBuilder internal constructor(
    private var url: String? = null,
    private var request: IRequestStart? = null,
    private var success: ISuccess? = null,
    private var failure: IFailure? = null,
    private var error: IError? = null,
    private var complete: IComplete? = null,
    private var body: RequestBody? = null,
    private var fragment: Fragment? = null,
    private var loaderStyle: LoaderStyle? = null,
    private var file: File? = null,
    private var downloadDir: String? = null,
    private var extension: String? = null,
    private var name: String? = null
) {

    private val mParams = LinkedHashMap<String, Any>()

    fun url(url: String): RestClientBuilder {
        this.url = url
        return this
    }

    fun params(params: Map<String, Any>): RestClientBuilder {
        mParams.putAll(params)
        return this
    }

    fun params(key: String, value: Any): RestClientBuilder {
        mParams[key] = value
        return this
    }

    fun file(file: File): RestClientBuilder {
        this.file = file
        return this
    }

    fun file(file: String): RestClientBuilder {
        this.file = File(file)
        return this
    }

    fun name(name: String): RestClientBuilder {
        this.name = name
        return this
    }

    fun dir(dir: String): RestClientBuilder {
        this.downloadDir = dir
        return this
    }

    fun extension(extension: String): RestClientBuilder {
        this.extension = extension
        return this
    }

    fun json(jsonString: String): RestClientBuilder {
        this.body =
            jsonString.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())
        return this
    }

    fun json(jsonObj: JSONObject): RestClientBuilder {
        this.body =
            jsonObj.toJSONString()
                .toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())
        return this
    }

    fun start(iRequest: IRequestStart): RestClientBuilder {
        this.request = iRequest
        return this
    }

    fun success(iSuccess: ISuccess): RestClientBuilder {
        this.success = iSuccess
        return this
    }

    fun failure(iFailure: IFailure): RestClientBuilder {
        this.failure = iFailure
        return this
    }

    fun complete(iComplete: IComplete): RestClientBuilder {
        this.complete = iComplete
        return this
    }

    fun error(iError: IError): RestClientBuilder {
        this.error = iError
        return this
    }

    fun loader(fragment: Fragment, style: LoaderStyle): RestClientBuilder {
        this.fragment = fragment
        this.loaderStyle = style
        return this
    }

    fun loader(fragment: Fragment): RestClientBuilder {
        this.fragment = fragment
        this.loaderStyle = LoaderStyle.LineScaleIndicator
        return this
    }

    fun build(): RestClient {
        return RestClient(
            url, mParams,
            downloadDir, extension, name,
            request, success, failure, error, complete,
            body, file, fragment,
            loaderStyle
        )
    }
}
