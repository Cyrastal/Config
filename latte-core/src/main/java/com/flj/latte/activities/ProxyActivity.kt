package com.flj.latte.activities

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDelegate
import com.flj.latte.fragments.LatteFragment
import com.flj.latte.global.Latte
import me.yokeyword.fragmentation.SupportActivity

/**
 * @author 傅令杰
 */
abstract class ProxyActivity : SupportActivity() {

    abstract fun setRootFragment(): LatteFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContainer(savedInstanceState)
        Latte.instance.configurator.withActivity(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    private fun initContainer(savedInstanceState: Bundle?) {
        val container = FrameLayout(this)
        container.id = View.generateViewId()
        setContentView(container)
        if (savedInstanceState == null) {
            loadRootFragment(container.id, setRootFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        System.gc()
        System.runFinalization()
    }
}