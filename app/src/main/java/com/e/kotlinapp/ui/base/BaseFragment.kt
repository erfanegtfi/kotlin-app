package com.e.kotlinapp.ui.base;


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.e.kotlinapp.model.response.base.ApiCallState.*
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.e.kotlinapp.BR

import com.e.kotlinapp.BaseView
import com.e.kotlinapp.BaseViewModel
import com.e.kotlinapp.model.response.base.*

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * All Fragments must extends from this Base class
 */
abstract class BaseFragment<VDB : ViewDataBinding, VM : BaseViewModel> : Fragment(), BaseView {
    private lateinit var viewActions: BaseViewActions
    protected lateinit var baseActivity: BaseActivity<*, *>
    protected lateinit var contextFragment: Context;

    protected lateinit var binding: VDB
    protected lateinit var viewModel: VM
    @get:LayoutRes
    protected abstract val layoutRes: Int
    protected abstract val viewModelClass: Class<VM>
    abstract val bindingVariable: Int

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }

    private fun bind(inflater: LayoutInflater, container: ViewGroup?) = DataBindingUtil.inflate<VDB>(inflater, layoutRes, container, false).apply {
        binding = this
        binding.lifecycleOwner = this@BaseFragment
    }

    private fun obtainViewModel() = ViewModelProvider(this).get(viewModelClass).apply {
        viewModel = this
        subscribeLoadingListener();
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        viewActions = BaseViewActions.getInstance(contextFragment)

        obtainViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind(inflater, container)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(bindingVariable, viewModel)
        binding.executePendingBindings()
    }

    protected fun finishActivity() {
        baseActivity.finish()
    }

    fun subscribeLoadingListener() {
        viewModel.apiEvents.observe(this, Observer {
            when (it) {
                is Loading -> {
                    showLoading();
                }
                is Loaded -> {
                    hideLoading();
                    onResponseMessage(it.response);
                }
                else -> { // on errors
                    hideLoading();
                    parsError(it);
                }
            }
        })
    }

    fun parsError(callEvent: ApiCallState) {
        when (callEvent) {
            is ResponseError -> {
                onResponseMessage(callEvent.message);
            }
            is UnAuthorizedError -> {
                unauthorizedUser(callEvent.message);
            }
            is NetworkError -> {
                onNetworkError(callEvent.throwable);
            }
            is TimeOutError -> {
                onTimeout(callEvent.throwable);
            }
        }
    }

    override fun showLoading(message: String?) {
        viewActions.showLoading(message)
    }

    override fun showLoading() {
        viewActions.showLoading()
    }

    override fun hideLoading() {
        viewActions.hideLoading()
    }

    override fun unauthorizedUser(response: ApiBaseResponse?) {
        viewActions.unauthorizedUser(response);
    }

    override fun onTimeout(throwable: Throwable?) {
        viewActions.onTimeout(throwable);
    }

    override fun onNetworkError(throwable: Throwable?) {
        viewActions.onNetworkError(throwable);
    }

    override fun onError(throwable: Throwable?, message: ApiBaseResponse?) {
        viewActions.onError(throwable, message);
    }

    override fun onResponseMessage(message: ApiBaseResponse?) {
        viewActions.onResponseMessage(message);
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        viewModel?.onDestroy()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDefaultEvent(o: Any) {
    }

    fun onBackPressed(): Boolean {
        return true;
    }
}
