package com.flj.latte.net

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * @author 傅令杰
 */
interface RestService {

    @GET
    fun get(@Url url: String?, @QueryMap params: LinkedHashMap<String, Any>?): Call<String>

    @FormUrlEncoded
    @POST
    fun post(@Url url: String?, @FieldMap params: LinkedHashMap<String, Any>?): Call<String>

    //用于application/json格式数据
    @POST
    fun post(@Url url: String?, @Body body: RequestBody?): Call<String>

    @FormUrlEncoded
    @PUT
    fun put(@Url url: String?, @FieldMap params: LinkedHashMap<String, Any>?): Call<String>

    @PUT
    fun put(@Url url: String?, @Body body: RequestBody?): Call<String>

    @DELETE
    fun delete(@Url url: String?, @QueryMap params: LinkedHashMap<String, Any>?): Call<String>

    @Streaming
    @GET
    fun download(@Url url: String?, @QueryMap params: LinkedHashMap<String, Any>?): Call<ResponseBody>

    @Multipart
    @POST
    fun upload(@Url url: String?, @Part file: MultipartBody.Part?): Call<String>
}
