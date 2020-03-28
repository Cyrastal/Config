package com.flj.mall.fragments.sort.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.flj.latte.fragments.LatteFragment
import com.flj.latte.net.RestClient
import com.flj.latte.net.callback.ISuccess
import com.flj.mall.R

class ContentFragment : LatteFragment() {

    private lateinit var mRecyclerView: RecyclerView
    private var mContentId = -1

    //简单工厂
    companion object {

        private const val ARGS_CONTENT_ID = "CONTENT_ID"

        fun newInstance(contentId: Int): ContentFragment {

            val args = Bundle()
            args.putInt(ARGS_CONTENT_ID, contentId)
            val fragment = ContentFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {
            mContentId = args.getInt(ARGS_CONTENT_ID)
        }
    }

    override fun setLayout(): Any {
        return R.layout.fragment_list_content
    }

    override fun onBindView(inflater: LayoutInflater, savedInstanceState: Bundle?, rootView: View) {
        mRecyclerView = rootView.findViewById(R.id.rv_list_content)
        val manager = StaggeredGridLayoutManager(
            2, StaggeredGridLayoutManager.VERTICAL
        )
        mRecyclerView.layoutManager = manager
        //初始化数据
        initData()
    }

    private fun initData() {
        RestClient.builder()
            .url("sort_content_list.php?contentId=$mContentId")
            .success(object : ISuccess {

                override fun onSuccess(response: String) {
                    val data =
                        SectionDataConverter().convert(response)
                    val sectionAdapter =
                        SectionAdapter(
                            R.layout.item_section_content,
                            R.layout.item_section_header,
                            data
                        )
                    mRecyclerView.adapter = sectionAdapter
                }
            })
            .build()
            .get()
    }

}











