package com.flj.latte.ui.recycler.divider

import com.choices.divider.Divider
import com.choices.divider.DividerItemDecoration

/**
 * @author 傅令杰
 */
class DividerLookupImpl(private val COLOR: Int, private val SIZE: Int) :
    DividerItemDecoration.DividerLookup {

    override fun getVerticalDivider(position: Int): Divider {
        return Divider.Builder()
            .size(SIZE)
            .color(COLOR)
            .build()
    }

    override fun getHorizontalDivider(position: Int): Divider {
        return Divider.Builder()
            .size(SIZE)
            .color(COLOR)
            .build()
    }
}
