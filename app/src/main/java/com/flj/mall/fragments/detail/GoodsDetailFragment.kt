package com.flj.mall.fragments.detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.daimajia.androidanimations.library.YoYo
import com.flj.latte.fragments.LatteFragment
import com.flj.latte.net.RestClient
import com.flj.latte.net.callback.IError
import com.flj.latte.net.callback.IFailure
import com.flj.latte.net.callback.ISuccess
import com.flj.latte.ui.banner.BannerCreator
import com.flj.latte.ui.widget.CircleTextView
import com.flj.latte.util.log.LogUtil
import com.flj.mall.R
import com.flj.mall.fragments.detail.animation.BezierAnimation
import com.flj.mall.fragments.detail.animation.BezierUtil
import com.flj.mall.fragments.detail.animation.ScaleUpAnimator
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import com.mikepenz.iconics.view.IconicsTextView
import com.youth.banner.Banner
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class GoodsDetailFragment : LatteFragment(),
    AppBarLayout.OnOffsetChangedListener,
    View.OnClickListener, BezierUtil.AnimationListener {

    private var mGoodsId = -1
    private lateinit var mBanner: Banner
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager
    private lateinit var mBtnAddShopCart: AppCompatButton
    private lateinit var mIconShopCart: IconicsTextView
    private lateinit var mGoodsThumbUrl: String
    private lateinit var mCircleTextView: CircleTextView
    private var mGoodsCount: Int = 0

    companion object {
        private const val ARG_GOODS_ID = "ARG_GOODS_ID"
        private val options = RequestOptions
            .diskCacheStrategyOf(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate()
            .override(100, 100)

        fun create(goodsId: Int): GoodsDetailFragment {
            val args = Bundle()
            args.putInt(ARG_GOODS_ID, goodsId)
            val fragment = GoodsDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {
            mGoodsId = args.getInt(ARG_GOODS_ID)
        }
    }

    private fun onClickAddShopCart() {
        val animImg = CircleImageView(context)
        Glide.with(this)
            .load(mGoodsThumbUrl)
            .apply(options)
            .into(animImg)
        BezierAnimation.addCart(
            this,
            mBtnAddShopCart,
            mIconShopCart,
            animImg,
            this
        )
    }

    override fun onAnimationEnd() {
        YoYo.with(ScaleUpAnimator())
            .duration(500)
            .playOn(mIconShopCart)
        //需要通知服务器，修改了购物车的数目
        RestClient
            .builder()
            .url("add_shop_cart_count.php")
            .loader(this)
            .success(object : ISuccess {
                override fun onSuccess(response: String) {
                    //根据不同的服务器逻辑和返回值去判断
                    //这里只是个参考
                    val isAdded = JSON.parseObject(response).getBoolean("data")
                    if (isAdded) {
                        mGoodsCount++
                        mCircleTextView.visibility = View.VISIBLE
                        mCircleTextView.text = mGoodsCount.toString()
                    }
                }
            })
            .params("count", mGoodsCount)
            .build()
            .post()

        RestClient
            .builder()
            .url("https://www.baidu.com")
            .params("","")
            .loader(this)
            .success(object :ISuccess{
                override fun onSuccess(response: String) {
                    LogUtil.d("HTML",response)
                }
            })
            .failure(object:IFailure{
                override fun onFailure() {
                }
            })
            .error(object :IError{
                override fun onError(code: Int, message: String) {
                }
            })
            .build()
            .get()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_shop_cart -> {
                onClickAddShopCart()
            }
        }
    }

    //服务器是可以返回当前购物数量的，具体看不同后台的逻辑和公司规范
    private fun setShopCartCount(data: JSONObject) {
        mGoodsThumbUrl = data.getString("thumb")
        //TODO:我们可以根据自己业务的服务器，解析data获取count
        if (mGoodsCount == 0) {
            mCircleTextView.visibility = View.GONE
        }
    }

    private fun initData() {
        RestClient
            .builder()
            .url("goods_detail.php")
            .params("goods_id", mGoodsId)
            .loader(this)
            .success(object : ISuccess {
                override fun onSuccess(response: String) {
                    val data = JSON.parseObject(response).getJSONObject("data")
                    initBanner(data)
                    initGoodsInfo(data)
                    initPager(data)
                    setShopCartCount(data)
                }
            })
            .build()
            .get()
    }

    private fun initTabLayout() {
        mTabLayout.tabMode = TabLayout.MODE_FIXED
        val context = context
        if (context != null) {
            mTabLayout.setSelectedTabIndicatorColor(
                ContextCompat.getColor(
                    context,
                    R.color.app_main
                )
            )
        }
        mTabLayout.tabTextColors = ColorStateList.valueOf(Color.BLACK)
        mTabLayout.setBackgroundColor(Color.WHITE)
        //关联TabLayout和ViewPager
        mTabLayout.setupWithViewPager(mViewPager)
    }

    private fun initPager(data: JSONObject) {
        val adapter = fragmentManager?.let { TabPagerAdapter(it, data) }
        mViewPager.adapter = adapter
    }

    private fun initGoodsInfo(data: JSONObject) {
        val goodsData = data.toJSONString()
        //关键
        supportDelegate.loadRootFragment(
            R.id.frame_goods_info,
            GoodsInfoFragment.create(goodsData)
        )
    }

    private fun initBanner(data: JSONObject) {
        val array = data.getJSONArray("banners")
        val size = array.size
        val images = ArrayList<String>()
        for (i in 0 until size) {
            images.add(array.getString(i))
        }
        BannerCreator.setDefault(mBanner, images)
    }

    override fun setLayout(): Any {
        return R.layout.fragment_goods_detail
    }

    override fun onBindView(inflater: LayoutInflater, savedInstanceState: Bundle?, rootView: View) {
        mBanner = rootView.findViewById(R.id.detail_banner)
        mTabLayout = rootView.findViewById(R.id.tab_layout)
        mViewPager = rootView.findViewById(R.id.view_pager)
        val collapsingToolbarLayout =
            rootView.findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_detail)
        val appBarLayout = rootView.findViewById<AppBarLayout>(R.id.app_bar_detail)
        collapsingToolbarLayout.setContentScrimColor(Color.WHITE)
        appBarLayout.addOnOffsetChangedListener(this)
        mBtnAddShopCart = rootView.findViewById(R.id.btn_add_shop_cart)
        mBtnAddShopCart.setOnClickListener(this)
        mCircleTextView = rootView.findViewById(R.id.tv_shopping_cart_amount)
        mCircleTextView.setCircleBackground(Color.RED)
        mIconShopCart = rootView.findViewById(R.id.icon_shop_cart)
        initData()
        initTabLayout()
    }

    override fun onOffsetChanged(p0: AppBarLayout?, p1: Int) {
    }
}