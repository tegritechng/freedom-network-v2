package com.threelineng.freedomnetwork.common.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.threelineng.freedomnetwork.R
import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import kotlin.apply
import kotlin.collections.joinToString
import kotlin.text.isEmpty
import kotlin.text.isLowerCase
import kotlin.text.lowercase
import kotlin.text.replace
import kotlin.text.replaceFirstChar
import kotlin.text.split
import kotlin.text.titlecase
import kotlin.text.toByteArray
import kotlin.text.toRegex
import kotlin.text.trim

/**
 * Adds Naira before Integer
 * @return String
 */
fun Int.prependNairaSign(): String {
    return "₦$this"
}

/**
 * Adds Naira before Long
 * @return String
 */
fun Long.prependNairaSign(): String {
    return "₦$this"
}

/**
 * Adds Naira before Double
 * @return String
 */
fun Double.prependNairaSign(): String {
    return "₦$this"
}

/**
 * Adds Naira sign before String
 * @return String
 */
fun String.prependNairaSign(): String {
    return "₦$this"
}

fun String.prependNGN(): String {
    return "NGN$this"
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun <A> String.fromJson(type: Class<A>): A? {
    return Gson().fromJson(this, type)
}

fun <A> A.toJson(): String? {
    return Gson().toJson(this)
}

fun Long.toDateString(pattern: String = "yyyy-MM-dd") =
    SimpleDateFormat(pattern, Locale.ENGLISH).format(this)

fun String.convertLongDate(outputPattern: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ")
    val outputFormat = SimpleDateFormat(outputPattern)
    val date = try {
        inputFormat.parse(this)
    } catch (e: Exception) {
        this
    }
    return outputFormat.format(date)
}

val todayStartDateToLong = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.time.time

val yesterdayDateToLong = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    add(Calendar.DATE, -1)
}.time.time

val tomorrowDateToLong = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    add(Calendar.DAY_OF_YEAR, 1)
}.time.time

/**
 * Shows a short Toast with a String Parameter.
 */
fun Context.shortToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

/**
 * Shows a short Toast with an Int(Resource Value) Parameter.
 */
fun Context.shortToast(@StringRes msg: Int) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

/**
 * Shows a long Toast with a String Parameter.
 */
fun Context.longToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

/**
 * Shows a long Toast with an Int(Resource Value) Parameter.
 */
fun Context.longToast(@StringRes msg: Int) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun String.sha512(): String {
    val md: MessageDigest = MessageDigest.getInstance("SHA-512")
    val messageDigest = md.digest(this.toByteArray())

    // Convert byte array into signum representation
    val no = BigInteger(1, messageDigest)

    // Convert message digest into hex value
    var hashtext: String = no.toString(16)

    // Add preceding 0s to make it 128 chars long
    while (hashtext.length < 128) {
        hashtext = "0$hashtext"
    }

    // return the HashText
    return hashtext
}

fun verifyIfEditTextIsFilled(vararg editText: EditText, errorMessage: String): Boolean {
    var result = true
    for (text in editText) {
        if (text.text.toString().isEmpty()) {
            val focusView: View = text
            text.error = errorMessage
            focusView.requestFocus()
            result = false
        }
    }
    return result
}


/**
 * Shows indeterminate progress bar on this button in place of where icon would be.
 * By default the tint of progress bar is the same as iconTint.
 *
 * @param tintColor (@ColorInt Int) Sets custom progress bar tint color.
 */
fun MaterialButton.showProgress(
    @ColorInt tintColor: Int = ContextCompat.getColor(context, R.color.white),
) {
    val spec = CircularProgressIndicatorSpec(
        context,
        null,
        0,
        com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_ExtraSmall
    )
    spec.indicatorColors = intArrayOf(tintColor)
    val progressIndicatorDrawable = IndeterminateDrawable.createCircularDrawable(context, spec)
    this.icon = progressIndicatorDrawable
    this.iconGravity = MaterialButton.ICON_GRAVITY_TEXT_END
}

fun MaterialButton.cancelProgress() {
    this.icon = null
}

fun Context.color(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.drawable(@DrawableRes drawable: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawable)
}

fun generateUniqueSessionId(): String {
    return UUID.randomUUID().toString().replace("-", "")
}

fun Fragment.showSnack(
    message: String, duration: Int = Snackbar.LENGTH_SHORT, isFromTop: Boolean = true
) = Snackbar.make(view!!, message, duration).apply {
    if (isFromTop) {
        // Access the Snackbar's view and change its position
        val view = view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
    }
}.show()

fun String.capitalizeWord(): String {
    return this.trim() // remove leading/trailing spaces
        .lowercase(Locale.getDefault()).split("\\s+".toRegex()) // split by one or more spaces
        .joinToString(" ") { word ->
            word.replaceFirstChar { char ->
                if (char.isLowerCase()) char.titlecase(Locale.getDefault())
                else char.toString()
            }
        }
}

fun String.cleanAmount(): String {
    return replace(",", "")
}

fun String.toInitials(): String =
    trim()
        .split("\\s+".toRegex())
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
