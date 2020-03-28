package com.flj.latte.ui.loader

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.util.*

/**
 * @author 傅令杰
 */
class LatteLoader private constructor() {

    private val mLoaders = Stack<LoadingDialogFragment>()

    object Holder {
        val instance: LatteLoader = LatteLoader()
    }

    companion object {
        val instance: LatteLoader
            get() = Holder.instance
    }

    fun showLoading(fragment: Fragment) {
        val manager = fragment.fragmentManager
        val transaction = manager?.beginTransaction()
        val preFragment = manager?.findFragmentByTag(LoadingDialogFragment::javaClass.name)
        if (preFragment !== null) {
            transaction?.remove(preFragment)
        }
        transaction?.addToBackStack(null)
        val dialogFragment = LoadingDialogFragment()
        if (transaction != null) {
            dialogFragment.show(transaction, LoadingDialogFragment::javaClass.name)
        }
        mLoaders.push(dialogFragment)
    }

    fun showLoading(activity: AppCompatActivity) {
        val manager = activity.supportFragmentManager
        val transaction = manager.beginTransaction()
        val preFragment = manager.findFragmentByTag(LoadingDialogFragment::javaClass.name)
        if (preFragment !== null) {
            transaction.remove(preFragment)
        }
        transaction.addToBackStack(null)
        val dialogFragment = LoadingDialogFragment()
        dialogFragment.show(transaction, LoadingDialogFragment::javaClass.name)
        mLoaders.push(dialogFragment)
    }

    fun stopLoading() {
        mLoaders.forEach {
            if (it !== null) {
                it.dismiss()
            }
        }
        mLoaders.clear()
    }
}
