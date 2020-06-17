package com.e.kotlinapp.ui.base;


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.e.kotlinapp.BaseView
import com.e.kotlinapp.BaseViewModel
import com.e.kotlinapp.di.AppViewModelFactory
import com.e.kotlinapp.model.response.base.ApiBaseResponse
import com.e.kotlinapp.model.response.base.ApiCallState
import com.e.kotlinapp.model.response.base.ApiCallState.*
import com.e.kotlinapp.network.coroutine.ResponseResult
import com.e.kotlinapp.network.coroutine.ResponseResultErrors
import com.e.kotlinapp.network.coroutine.ResponseResultWithWrapper
import com.e.kotlinapp.network.coroutine.ResponseWrapper
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


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

    @Inject
    lateinit var  viewModelFactory: AppViewModelFactory

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

    private fun obtainViewModel() = ViewModelProvider(this, viewModelFactory).get(viewModelClass).apply {
        viewModel = this
        subscribeLoadingListener();
        subscribeLoadingListener2();
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
                is ResponseResult.Loading -> {
                    showLoading();
                }
                is ResponseResult.Success -> {
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

    fun parsError(callEvent: ResponseResult<ApiBaseResponse>) {
        when (callEvent) {
            is ResponseResult.ResponseError -> {
                onResponseMessage(callEvent.response);
            }
            is ResponseResult.UnAuthorizedError -> {
                unauthorizedUser(callEvent.response);
            }
            is ResponseResult.NetworkError -> {
                onNetworkError(callEvent.throwable);
            }
            is ResponseResult.TimeOutError -> {
                onTimeout(callEvent.throwable);
            }
        }
    }
    ///////////////////////

    private fun subscribeLoadingListener2() {
        viewModel.apiEvents2.observe(this, Observer {
            when (it) {
                is ResponseResultWithWrapper.Loading -> {
                    showLoading();
                }
                is ResponseResultWithWrapper.Success -> {
                    hideLoading();
                    onResponseMessage(it.responseWrapper.data);
                }
                is ResponseResultWithWrapper.ErrorResponse -> {
                    onResponseMessage(it.responseWrapper.responseError);
                }
                is ResponseResultWithWrapper.Error -> { // on errors
                    hideLoading();
                    parsError2(it.responseWrapper);
                }
            }
        })
    }
    fun parsError2(callEvent: ResponseWrapper<*>?) {
        when (callEvent?.throwable) {
            is ResponseResultErrors.UnAuthorizedError -> {
                unauthorizedUser(callEvent.responseError)
            }
            is ResponseResultErrors.NetworkError -> {
//                onNetworkError(callEvent.throwable.UnAuthorizedError);
            }
            is ResponseResultErrors.TimeOutError -> {
//                onTimeout(callEvent.throwable);
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
