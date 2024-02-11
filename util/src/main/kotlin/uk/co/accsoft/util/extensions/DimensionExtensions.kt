package uk.co.accsoft.util.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.util.TypedValue
import android.view.WindowManager

fun Context.getDisplaySize() = Point().also {
    (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getRealSize(it)
}

fun Int.toDp(): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    toFloat(),
    Resources.getSystem().displayMetrics
).toInt()

fun Float.toDp(): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    Resources.getSystem().displayMetrics
)

fun Int.toSp(): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    toFloat(),
    Resources.getSystem().displayMetrics
).toInt()

fun Float.toSp(): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    this,
    Resources.getSystem().displayMetrics
)