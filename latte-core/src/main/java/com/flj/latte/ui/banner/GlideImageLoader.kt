package com.flj.latte.ui.banner

import android.content.Context
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.flj.latte.ui.image.GlideUtil
import com.youth.banner.loader.ImageLoader

/**
 * @author 傅令杰
 */
class GlideImageLoader : ImageLoader() {

    companion object {
        private val options = GlideUtil.defaultOptions()
    }

    override fun createImageView(context: Context?): AppCompatImageView {
        return AppCompatImageView(context)
    }

    override fun displayImage(context: Context, path: Any?, imageView: ImageView) {
        Glide.with(context)
            .load(path)
            .apply(options)
            .into(imageView)
    }
}