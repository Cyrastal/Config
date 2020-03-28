package com.flj.latte.ui.recycler.adapter

import android.view.View

import com.chad.library.adapter.base.BaseViewHolder

/**
 * @author 傅令杰
 */
class LatteViewHolder private constructor(view: View) : BaseViewHolder(view) {

    companion object {
        fun create(view: View): LatteViewHolder {
            return LatteViewHolder(view)
        }
    }
}
