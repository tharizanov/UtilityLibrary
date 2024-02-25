package uk.co.accsoft.util.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

//////////////////// Context extensions ///////////////////////////

tailrec fun Context.getActivity(): Activity? =
    (this as? Activity) ?: (this as? ContextWrapper)?.baseContext?.getActivity()

@ColorInt
fun Context.getColorCompat(@ColorRes colorResId: Int): Int =
    ResourcesCompat.getColor(resources, colorResId, null)

fun Context.getDrawableCompat(@DrawableRes resId: Int): Drawable? =
    AppCompatResources.getDrawable(this, resId)

fun Context.getFloat(@DimenRes resId: Int): Float {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        return resources.getFloat(resId)
    }

    val value = TypedValue()
    resources.getValue(resId, value, true)
    if (value.type != TypedValue.TYPE_FLOAT) {
        throw Resources.NotFoundException("Resource is not of type Float")
    }

    return value.float
}

fun Context.showToast(@StringRes messageRes: Int, length: Int = Toast.LENGTH_SHORT) =
    showToast(getString(messageRes), length)

fun Context.showToast(message: CharSequence?, length: Int = Toast.LENGTH_SHORT) {
    message?.ifNotEmpty {
        Toast.makeText(this, it, length).show()
    }
}


///////////////////// View extensions //////////////////////////////

fun RecyclerView.addLinearDividerDecoration(@DrawableRes drawableId: Int) {
    (layoutManager as? LinearLayoutManager)?.let { lm ->
        AppCompatResources.getDrawable(context, drawableId)?.let { drawable ->
            DividerItemDecoration(context, lm.orientation).let { decoration ->
                decoration.setDrawable(drawable)
                addItemDecoration(decoration)
            }
        }
    }
}

fun View.getParentLayout(): ViewGroup? = parent as? ViewGroup

fun View.hideKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(windowToken, 0)
}

fun EditText.onTextChange(listener: (CharSequence) -> Unit): TextWatcher =
    object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener.invoke(s ?: "")
        }
        override fun afterTextChanged(s: Editable?) {}
    }.also {
        addTextChangedListener(it)
    }


///////////////////// Component extensions //////////////////////////////

fun Fragment.findNavControllerSafe(): NavController? =
    try { findNavController() } catch (_: IllegalStateException) { null }