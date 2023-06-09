package sju.sejong.capstonedesign.util

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter

@BindingAdapter("layoutMarginBottom")
fun setLayoutMarginBottom(view: View, dimen: Float) {
    (view.layoutParams as ViewGroup.MarginLayoutParams).let {
        it.bottomMargin = dimen.toInt()
        view.layoutParams = it
    }
}