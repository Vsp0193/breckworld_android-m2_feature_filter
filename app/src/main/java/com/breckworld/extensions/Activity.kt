package com.breckworld.extensions

import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import com.breckworld.R

fun AppCompatActivity.replaceFragment(
        fragment: androidx.fragment.app.Fragment,
        isAddToBackStack: Boolean = false,
        containerResId: Int = R.id.container
) {
    val oldFragment = supportFragmentManager.findFragmentById(containerResId)
    val transaction = supportFragmentManager.beginTransaction()
    /*transaction.setCustomAnimations(
            R.anim.slide_in_from_right,
            R.anim.slide_out_from_right,
            R.anim.slide_in_from_left,
            R.anim.slide_out_from_left
    )*/

    if (oldFragment != null) {
        transaction.detach(oldFragment)
    }

    transaction.replace(containerResId, fragment, fragment::class.java.simpleName)
    if (isAddToBackStack) {
        transaction.addToBackStack(fragment::class.java.simpleName).commit()
        supportFragmentManager.executePendingTransactions()
    } else {
        transaction.commitNow()
    }
}

fun AppCompatActivity.popBackStack() {
    supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    view?.apply {
        val imm = getInputMethodManager()
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}

fun AppCompatActivity.showKeyboard(editText: EditText) {
    val imm = getInputMethodManager()
    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

fun FragmentActivity.hideKeyboard() {
    val view = this.currentFocus
    view?.apply {
        val imm = getInputMethodManager()
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}