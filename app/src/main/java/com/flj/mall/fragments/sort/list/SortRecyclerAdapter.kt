package com.flj.mall.fragments.sort.list

import android.graphics.Color
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.flj.latte.ui.recycler.adapter.LatteRecyclerAdapter
import com.flj.latte.ui.recycler.adapter.LatteViewHolder
import com.flj.latte.ui.recycler.data.LatteItemEntity
import com.flj.mall.R
import com.flj.mall.fragments.sort.content.ContentFragment
import com.flj.mall.fragments.sort.SortFragment
import me.yokeyword.fragmentation.SupportHelper

class SortRecyclerAdapter(data: List<LatteItemEntity>, private val sortFragment: SortFragment) :
    LatteRecyclerAdapter(data) {

    private var mPrePosition = 0

    init {
        //垂直菜单栏的布局文件
        addItemType(
            R.id.type_vertical_menu_list,
            R.layout.item_vertical_menu_list
        )
    }

    override fun convert(holder: LatteViewHolder, entity: LatteItemEntity) {
        when (holder.itemViewType) {
            R.id.type_vertical_menu_list -> {
                val text =
                    entity.getField<String>(R.id.field_text)
                val isClicked =
                    entity.getField<Boolean>(R.id.field_tag)
                //取出控件
                val name =
                    holder.getView<AppCompatTextView>(R.id.tv_vertical_item_name)
                val line = holder.getView<View>(R.id.view_line)
                val itemView = holder.itemView
                itemView.setOnClickListener {

                    val currentPosition = holder.adapterPosition
                    if (mPrePosition != currentPosition) {
                        //还原上一个item的颜色和状态
                        data[mPrePosition].setField(R.id.field_tag, false)
                        notifyItemChanged(mPrePosition)

                        //更新当前点击的item都状态
                        entity.setField(R.id.field_tag, true)
                        notifyItemChanged(currentPosition)
                        //已经点击过的position就成为过去式了
                        mPrePosition = currentPosition

                        val contentId =
                            data[currentPosition]
                                .getField<Int>(R.id.field_id)

                        //切换content
                        showContent(contentId)
                    }
                }
                //点击事件结束
                if (!isClicked) {
                    line.visibility = View.INVISIBLE
                    name.setTextColor(
                        ContextCompat.getColor(mContext, R.color.we_chat_black)
                    )
                    itemView.setBackgroundColor(
                        ContextCompat.getColor(mContext, R.color.item_background)
                    )
                } else {
                    line.visibility = View.VISIBLE
                    name.setTextColor(
                        ContextCompat.getColor(mContext, R.color.app_main)
                    )
                    line.setBackgroundColor(
                        ContextCompat.getColor(mContext, R.color.app_main)
                    )
                    itemView.setBackgroundColor(
                        Color.WHITE
                    )
                }
                holder.setText(R.id.tv_vertical_item_name, text)
            }
            else -> {
            }
        }
    }
    //convert结束

    private fun switchContent(fragment: ContentFragment) {
        val contentFragment =
            SupportHelper.findFragment(
                sortFragment.childFragmentManager
                , ContentFragment::class.java
            )
        contentFragment.supportDelegate.replaceFragment(fragment, false)
    }

    private fun showContent(contentId: Int) {
        val fragment =
            ContentFragment.newInstance(contentId)
        switchContent(fragment)
    }

}











