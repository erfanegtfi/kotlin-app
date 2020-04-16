package com.e.kotlinapp


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.loading_progress.view.*


class LoadingProgressView : LinearLayout {
    private var onNetworkClickListener: OnNetworkClickListener? = null

    interface OnNetworkClickListener {
        fun onNetworkClickListener()
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(
        context: Context, attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(
        context: Context, attrs: AttributeSet?
    ) {
        View.inflate(context, R.layout.loading_progress, this)
        internet_connection.setOnClickListener(OnClickListener {
            onNetworkClickListener?.onNetworkClickListener()
        })
        if (attrs != null) {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingProgressView)
            try {
                val n = a.indexCount
                for (i in 0 until n) {
                    val attr = a.getIndex(i)
                    when (attr) {
                        R.styleable.LoadingProgressView_prog_color -> {
                            val text = a.getResourceId(attr, 0)
                            setProgressColor(text)
                        }
                        else -> Log.d(
                            "TAG", "Unknown attribute for $javaClass: $attr"
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MyCheckBox", "There was an error loading attributes.")
            } finally {
                a.recycle()
            }
        }
    }

    fun setOnNetworkListener(onLoadingClickListener: OnNetworkClickListener?) {
        onNetworkClickListener = onLoadingClickListener
    }

    fun setProgressVisibility(visibility: Int) {
        progress_wheel!!.visibility = visibility
        setGonePanel()
    }

    fun setNoItemVisibility(visibility: Int) {
        no_item!!.visibility = visibility
        setGonePanel()
    }

    fun networkConnection(visibility: Int) {
        internet_connection!!.visibility = visibility
        setGonePanel()
    }

    fun setErrorText(text: String?) {
        no_item!!.text = text
    }

    fun setGonePanel() {
        if (no_item.visibility == View.GONE && progress_wheel.visibility == View.GONE && internet_connection.visibility == View.GONE) prgLoadMore.visibility =
            View.GONE else prgLoadMore.visibility = View.VISIBLE
    }

    fun error() {
        setNoItemVisibility(View.VISIBLE)
        setProgressVisibility(View.GONE)
    }

    fun success() {
        setNoItemVisibility(View.GONE)
        setProgressVisibility(View.GONE)
        networkConnection(View.GONE)
    }

    fun loading() {
        setProgressVisibility(View.VISIBLE)
        setNoItemVisibility(View.GONE)
        networkConnection(View.GONE)
    }

    fun hideProgress() {
        setNoItemVisibility(View.GONE)
        setProgressVisibility(View.GONE)
    }

    fun noNetwork() {
        networkConnection(View.VISIBLE)
        setErrorText("عدم اتصال به اینترنت!")
        error()
    }

    fun hasNetwork() {
        networkConnection(View.GONE)
    }

    fun setProgressColor(color: Int) { //        progress_wheel.setBarColor(color);
    }
}

