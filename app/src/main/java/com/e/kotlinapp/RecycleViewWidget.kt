package com.e.kotlinapp


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.e.kotlinapp.model.response.base.*
import kotlinx.android.synthetic.main.loading_progress.view.*
import kotlinx.android.synthetic.main.recycle_view.view.*


class RecycleViewWidget : FrameLayout {

//    companion object {
//        const val LOADING = 1
//        const val LOADED = 2
//        const val ERROR = 3
//    }

    private lateinit var onNetworkClickListener: OnNetworkClickListener

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
        View.inflate(context, R.layout.recycle_view, this)
        (recycle_view_widget.itemAnimator as SimpleItemAnimator?)?.supportsChangeAnimations = false

        if (attrs != null) {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.RecycleViewWidget)
            try {
                val n = a.indexCount
                for (i in 0 until n) {
                    val attr = a.getIndex(i)
                    when (attr) {
                        R.styleable.RecycleViewWidget_layout_manager -> {
                            val layout = a.getResourceId(attr, 0)
                            if (layout == 0) setLinearLayoutManager() else if (layout == 1) setGridLayoutManager(
                                2
                            )
                        }
//                        R.styleable.RecycleViewWidget_loading -> {
//                            val loading = a.getResourceId(attr, 0)
//                            setLoadingState(loading)
//                        }
                        R.styleable.RecycleViewWidget_message -> {
                            val message = a.getResourceId(attr, 0)
                            setMessage(message)
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

    fun setOnNetworkListener(onLoadingClickListener: OnNetworkClickListener) {
        onNetworkClickListener = onLoadingClickListener
    }

    fun getProgress(): LoadingProgressView {
        return loading_progress
    }

    //    @BindingAdapter("loadingg")
//    public static  void setState(RecycleViewWidget widget, int v) {
//        if (v == LOADING)
//            widget.getProgress().loading();
//        else if (v == LOADED)
//            widget.getProgress().hideProgress();
//        else if (v == ERROR)
//            widget.getProgress().error();
//    }
    fun setLoadingState(listLoadState: ListLoadState) {
        when (listLoadState) {
            is ListLoading -> loading_progress.loading()
            is ListLoaded -> loading_progress.hideProgress()
            is ListError -> {
                loading_progress.error()
                setMessage(listLoadState.message)
            }
        }
    }

    fun setMessage(message: Int) {
        if (message != 0) loading_progress.setErrorText(context.getString(message))
    }

    fun setMessage(message: String?) {
        if (!message.isNullOrEmpty()) loading_progress.setErrorText(message)
    }

    fun setLinearLayoutManager() {
        recycle_view_widget.layoutManager = LinearLayoutManager(context)
    }

    fun setLinearLayoutManager(reverse: Boolean) {
        recycle_view_widget.layoutManager = LinearLayoutManager(
            context, RecyclerView.VERTICAL, reverse
        )
    }

    fun setHorizontalLinearLayoutManager() {
        recycle_view_widget.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, true
        )
    }

//    fun setLinearLayoutManagerForNestedScroll() {
//        recycle_view_widget.layoutManager = LinearLayoutManagerForNestedScroll(
//            context,
//            LinearLayoutManager.VERTICAL,
//            false
//        )
//    }

    fun setGridLayoutManager(numberOfColumns: Int) {
        recycle_view_widget.layoutManager = GridLayoutManager(context, numberOfColumns)
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager?) {
        recycle_view_widget.layoutManager = layoutManager
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        recycle_view_widget.adapter = adapter
    }

}

