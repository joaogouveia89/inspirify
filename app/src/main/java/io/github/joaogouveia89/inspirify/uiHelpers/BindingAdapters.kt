package io.github.joaogouveia89.inspirify.uiHelpers

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:showIfTrue")
fun showIfTrue(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("app:showIfFalse")
fun showIfFalse(view: View, show: Boolean) {
    view.visibility = if (show) View.GONE else View.VISIBLE
}
