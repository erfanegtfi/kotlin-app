package com.e.kotlinapp

import android.content.Context
import android.content.ContextWrapper
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern
//https://github.com/SimpleBoilerplates/Android/tree/master/app/src/main/java/com/me/booksdemoandroid/shared/extension
fun View.invisible(): View {
    visibility = View.INVISIBLE
    return this
}

fun View.visible(): View {
    visibility = View.VISIBLE
    return this
}

fun View.gone(): View {
    visibility = View.GONE
    return this
}

fun View.setVisibility(expression:Boolean){
    if(expression) {
        visible()
    } else {
        gone()
    }
}

enum class Orientation { CENTER, LEFT, RIGHT, TOP, BOTTOM }

fun View.circularReveal(//https://github.com/emitchel/Sumbeat/blob/master/app/src/main/java/com/erm/artists/extensions/ViewExtentions.kt
    orientation: List<Orientation> = listOf(Orientation.CENTER),
    duration: Long = 5
) {

    val x = when {
        orientation.contains(Orientation.LEFT) -> 0
        orientation.contains(Orientation.CENTER) -> right / 2
        else -> right
    }

    val y = when {
        orientation.contains(Orientation.TOP) -> 0
        orientation.contains(Orientation.CENTER) -> bottom / 2
        else -> bottom
    }

    val startRadius = 0
    val endRadius = Math.hypot(
        width.toDouble(),
        height.toDouble()
    ).toInt()

    val anim = ViewAnimationUtils.createCircularReveal(
        this,
        x,
        y,
        startRadius.toFloat(),
        endRadius.toFloat()
    )
    anim.duration = duration
    anim.start()

}

fun <T : Any> List<T?>.whenAllNotNull(block: (List<T>) -> Unit) {
    if (this.all { it != null }) {
        block(this.filterNotNull())
    }
}

fun String.isAnEmailAddress() : Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun EditText.textString(): String = this.text.toString()

fun String.validPhoneNumber(): Boolean{
    return if (!Pattern.matches("[a-zA-Z]+", this)) {
        !(this.length < 6 || this.length > 13)
    } else {
        false
    }
}

fun ViewGroup.inflate(layout:Int): View {
    return LayoutInflater.from(context).inflate(layout,this, false)
}

fun Context.inflateView(layout: Int): View {
    val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    return inflater.inflate(layout, null)
}

fun Context.inflateCustomView(layout: Int, viewGroup: ViewGroup?) {
    val inflater= this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    inflater.inflate(layout, viewGroup, true)
}

fun ViewGroup.setLayoutChangeAnim(layoutChange: Int){
    this.layoutTransition.enableTransitionType(layoutChange)
}

fun BottomNavigationView.getSelectedItem() = menu.findItem(selectedItemId)

fun BottomNavigationView.getMenuItem(id: Int) = menu.findItem(id)

fun View.getParentActivity(): AppCompatActivity? = this.context.getParentActivity()

fun Context.getParentActivity(): AppCompatActivity? {
    return when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> this.baseContext.getParentActivity()
        else -> null
    }
}

fun Fragment.longToast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
}

fun Fragment.longToast(msg: Int) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
}

fun AppCompatActivity.longToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun View.snackbar(msg: Int) = Snackbar.make(this, msg, Snackbar.LENGTH_SHORT)
fun View.snackbar(msg: String) = Snackbar.make(this, msg, Snackbar.LENGTH_SHORT)
fun View.longSnackbar(msg: String) = Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
fun View.longSnackbar(msg: Int) = Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
fun View.indefiniteSnackbar(msg: String) = Snackbar.make(this, msg, Snackbar.LENGTH_INDEFINITE)
fun View.indefiniteSnackbar(msg: Int) = Snackbar.make(this, msg, Snackbar.LENGTH_INDEFINITE)

