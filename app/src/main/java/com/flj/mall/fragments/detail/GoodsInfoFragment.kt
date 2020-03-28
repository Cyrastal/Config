package com.flj.mall.fragments.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.flj.latte.fragments.LatteFragment
import com.flj.mall.R

class GoodsInfoFragment : LatteFragment() {

    private lateinit var mData: JSONObject

    companion object {
        private const val ARG_GOODS_DATA = "ARG_GOODS_DATA"

        fun create(goodsInfo: String): GoodsInfoFragment {
            val args = Bundle()
            args.putString(ARG_GOODS_DATA, goodsInfo)
            val fragment = GoodsInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val goodsData = arguments?.getString(ARG_GOODS_DATA)
        mData = JSON.parseObject(goodsData)
    }

    override fun setLayout(): Any {
        return R.layout.fragment_goods_info
    }

    override fun onBindView(inflater: LayoutInflater, savedInstanceState: Bundle?, rootView: View) {
        val goodsInfoTitle = rootView.findViewById<AppCompatTextView>(R.id.tv_goods_info_title)
        val goodsInfoDesc = rootView.findViewById<AppCompatTextView>(R.id.tv_goods_info_desc)
        val goodsInfoPrice = rootView.findViewById<AppCompatTextView>(R.id.tv_goods_info_price)
        val name = mData.getString("name")
        val desc = mData.getString("description")
        val price = mData.getDouble("price")
        goodsInfoTitle.text = name
        goodsInfoPrice.text = price.toString()
        goodsInfoDesc.text = desc
    }
}