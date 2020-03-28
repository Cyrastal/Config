package com.flj.latte.ui.recycler.adapter

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.flj.latte.ui.recycler.data.LatteItemEntity

/**
 * @author 傅令杰
 */
abstract class LatteRecyclerAdapter protected constructor(data: List<LatteItemEntity>) :
    BaseMultiItemQuickAdapter<LatteItemEntity, LatteViewHolder>(data),
    BaseQuickAdapter.SpanSizeLookup {

    init {
        initView()
    }

    //添加注解限制
    @Suppress("RedundantOverride")
    override fun addItemType(@IdRes type: Int, @LayoutRes layoutResId: Int) {
        super.addItemType(type, layoutResId)
    }

    private fun initView() {
        //设置宽度监听
        setSpanSizeLookup(this)
        openLoadAnimation()
        //多次执行动画
        isFirstOnly(false)
    }

    override fun createBaseViewHolder(view: View): LatteViewHolder {
        return LatteViewHolder.create(view)
    }

    override fun getSpanSize(gridLayoutManager: GridLayoutManager, position: Int): Int {
        return data[position].getSpanSize()
    }
}
