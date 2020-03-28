package com.flj.mall.fragments.sort.content

import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.flj.mall.R

class SectionAdapter(layoutResId: Int, sectionHeadResId: Int, data: List<SectionBean>) :
    BaseSectionQuickAdapter<SectionBean, BaseViewHolder>(layoutResId, sectionHeadResId, data) {

    override fun convertHead(helper: BaseViewHolder, item: SectionBean) {
        helper.setText(R.id.header, item.header)
        helper.setVisible(R.id.more, item.isMore)
    }

    override fun convert(helper: BaseViewHolder, item: SectionBean) {
        val thumb = item.t.goodsThumb
        val name = item.t.goodsName
        helper.setText(R.id.tv, name)
        val goodsImageView =
            helper.getView<AppCompatImageView>(R.id.iv)
        Glide.with(mContext)
            .load(thumb)
            .into(goodsImageView)
    }
}