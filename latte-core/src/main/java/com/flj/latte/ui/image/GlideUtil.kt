package com.flj.latte.ui.image

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

/**
 * @author 傅令杰
 */
object GlideUtil {

    fun defaultOptions(): RequestOptions {
        return RequestOptions()
            .centerCrop()
            /**
             * 图片全缓存
             */
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    }

    fun loadDefault(context: Context, target: AppCompatImageView, url: String) {
        Glide.with(context)
            .load(url)
            .apply(defaultOptions())
            .into(target)
    }
}