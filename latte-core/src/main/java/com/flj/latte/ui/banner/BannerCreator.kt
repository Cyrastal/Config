package com.flj.latte.ui.banner

import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer

/**
 * @author 傅令杰
 */
object BannerCreator {

    //创建默认风格的轮播
    fun setDefault(banner: Banner, data: List<*>) {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        banner.setImageLoader(GlideImageLoader())
        banner.setImages(data)
        banner.setBannerAnimation(Transformer.Default)
        banner.isAutoPlay(true)
        //设置轮播的间隔时间
        banner.setDelayTime(3000)
        //设置指示器位置
        banner.setIndicatorGravity(BannerConfig.CENTER)
        banner.start()
    }
}