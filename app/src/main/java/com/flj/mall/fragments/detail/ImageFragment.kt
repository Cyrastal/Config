package com.flj.mall.fragments.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flj.latte.fragments.LatteFragment
import com.flj.latte.ui.recycler.data.LatteItemEntity
import com.flj.mall.R

class ImageFragment : LatteFragment() {

    private lateinit var mRecyclerView: RecyclerView

    companion object {
        private const val ARG_PICTURES = "ARG_PICTURES"

        fun create(pictures: ArrayList<String>): ImageFragment {
            val args = Bundle()
            args.putStringArrayList(ARG_PICTURES, pictures)
            val fragment = ImageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun initImages() {
        val arguments = arguments
        if (arguments != null) {
            val pictures = arguments.getStringArrayList(ARG_PICTURES)
            val entities = ArrayList<LatteItemEntity>()
            val size: Int
            if (pictures != null) {
                size = pictures.size
                for (i in 0 until size) {
                    val imageUrl = pictures[i]
                    val entity = LatteItemEntity
                        .builder()
                        .setItemType(R.id.type_goods_detail_image)
                        .setField(R.id.field_image_url, imageUrl)
                        .build()
                    entities.add(entity)
                    val adapter = DetailImageAdapter(entities)
                    mRecyclerView.adapter = adapter
                }
            }

        }
    }

    override fun setLayout(): Any {
        return R.layout.fragment_image
    }

    override fun onBindView(inflater: LayoutInflater, savedInstanceState: Bundle?, rootView: View) {
        mRecyclerView = rootView.findViewById(R.id.rv_image_container)
        val manager = LinearLayoutManager(context)
        mRecyclerView.layoutManager = manager
        initImages()
    }
}