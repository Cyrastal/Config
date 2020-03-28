package com.flj.latte.ui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import com.flj.latte.R
import com.mikepenz.iconics.view.IconicsTextView
import java.util.*

/**
 * @author 傅令杰
 */
class StarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayoutCompat(context, attrs, defStyleAttr), View.OnClickListener {

    companion object {
        private const val ICON_UN_SELECT = "{faw-star-o}"
        private const val ICON_SELECTED = "{faw-star}"
        private const val STAR_TOTAL_COUNT = 5
        private val STARS = ArrayList<IconicsTextView>()
    }

    val starCount: Int
        get() {
            var count = 0
            for (i in 0 until STAR_TOTAL_COUNT) {
                val star = STARS[i]
                val isSelect = star.getTag(R.id.star_is_select) as Boolean
                if (isSelect) {
                    count++
                }
            }
            return count
        }

    init {
        initStarIcon()
    }

    private fun initStarIcon() {
        for (i in 0 until STAR_TOTAL_COUNT) {
            val star = IconicsTextView(context)
            star.gravity = Gravity.CENTER
            val lp = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            lp.weight = 1f
            star.layoutParams = lp
            star.text = ICON_UN_SELECT
            star.setTag(R.id.star_count, i)
            star.setTag(R.id.star_is_select, false)
            star.setOnClickListener(this)
            STARS.add(star)
            this.addView(star)
        }
    }

    private fun selectStar(count: Int) {
        for (i in 0..count) {
            val star = STARS[i]
            star.text = ICON_SELECTED
            star.setTextColor(Color.RED)
            star.setTag(R.id.star_is_select, true)
        }
    }

    private fun unSelectStar(count: Int) {
        for (i in 0 until STAR_TOTAL_COUNT) {
            if (i >= count) {
                val star = STARS[i]
                star.text = ICON_UN_SELECT
                star.setTextColor(Color.GRAY)
                star.setTag(R.id.star_is_select, false)
            }
        }
    }

    override fun onClick(v: View) {
        val star = v as IconicsTextView
        //获取第几个星星
        val count = star.getTag(R.id.star_count) as Int
        //获取点击状态
        val isSelect = star.getTag(R.id.star_is_select) as Boolean
        if (!isSelect) {
            selectStar(count)
        } else {
            unSelectStar(count)
        }
    }
}
