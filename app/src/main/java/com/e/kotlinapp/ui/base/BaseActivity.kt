package com.e.kotlinapp.ui.base;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.LayoutRes


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.*
import com.e.kotlinapp.BaseView
import com.e.kotlinapp.BaseViewModel
import com.e.kotlinapp.model.response.base.*
import com.e.kotlinapp.model.response.base.ApiCallState.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.e.kotlinapp.BR
import com.e.kotlinapp.di.AppViewModelFactory
import com.e.kotlinapp.network.coroutine.ResponseResult
import com.e.kotlinapp.network.coroutine.ResponseResultErrors
import com.e.kotlinapp.network.coroutine.ResponseResultWithWrapper
import com.e.kotlinapp.network.coroutine.ResponseWrapper
import javax.inject.Inject

abstract class BaseActivity<VDB : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(), BaseView {

    var isFinished: Boolean? = null
    var activity: BaseActivity<*, *>? = null
    var isContentLoaded: Boolean? = null
    var progress: Dialog? = null
//    protected Boolean fragmented = false;
//    protected String parentTitle;

    //    public abstract void initView();
    lateinit var viewActions: BaseViewActions;

    protected lateinit var binding: VDB
    protected lateinit var viewModel: VM
    @get:LayoutRes
    protected abstract val layoutRes: Int
    abstract val viewModelClass: Class<VM>

    @Inject
    lateinit var  viewModelFactory: AppViewModelFactory

    private fun bind() = DataBindingUtil.setContentView<VDB>(this, layoutRes).apply {
        this.setVariable(BR.viewModel, viewModel)
        lifecycleOwner = this@BaseActivity
        binding = this
    }

    private fun obtainViewModel() = ViewModelProvider(this,viewModelFactory).get(viewModelClass).apply {
        viewModel = this
        subscribeLoadingListener()
        subscribeLoadingListener2()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        obtainViewModel()
        bind()
        viewActions = BaseViewActions.getInstance(this)


        EventBus.getDefault().register(this);
        activity = this;
    }


    private fun subscribeLoadingListener() {
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
//    protected fun <T> LiveData<ResponseResultWithWrapper<ResponseWrapper<T>>>.onResult(action: (ResponseResultWithWrapper<ResponseWrapper<T>>) -> Unit) {
//        observe(this@BaseActivity) { data ->
//            data.let(
//                when (data) {
//                    is ResponseResultWithWrapper.Loading -> {
//                        action
//                    }
//                    is ResponseResultWithWrapper.Error -> {
//                        when (data.responseWrapper.throwable) {
//                            is ResponseResultErrors.UnAuthorizedError -> {
//
//                            }
//                            is ResponseResultErrors.TimeOutError -> {
//
//                            }
//                            is ResponseResultErrors.NetworkError -> {
//
//                            }
//                            is ResponseResultErrors.UnknownError -> {
//
//                            }
//                        }
//                        action
//                    }
//                    is ResponseResultWithWrapper.ErrorResponse -> {
//                        action
//                    }
//                    else -> action
//                }
//
//            )
//        }
//    }

    //    protected fun <T: ApiBaseResponse> LiveData<ResponseResult<T>>.onResult(action: (ResponseResult<T>) -> Unit) {
//        observe(this@BaseActivity) { data ->
//            data.let(
//                when (data) {
//                    is ResponseResult.Loading -> {
//                        showLoading()
//                        action
//                    }
//                    is ResponseResult.Success -> {
//                        hideLoading();
//                        onResponseMessage(data.response);
//                        action
//                    }
//                    is ResponseResult.ResponseError -> {
//                        onResponseMessage(data.response);
//                        action
//                    }
//                    is ResponseResult.UnAuthorizedError -> {
////                        unauthorizedUser(data.throwable);
//                        action
//                    }
//                    is ResponseResult.NetworkError -> {
//                        internetConnection();
//                        action
//                    }
//                    is ResponseResult.TimeOutError -> {
//                        onTimeout(data.throwable);
//                        action
//                    }
//                    else ->  action
//                }
//
//            )
//        }
//    }

    fun internetConnection() {
        Toast.makeText(activity, "connection error!", Toast.LENGTH_SHORT).show();
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
        isFinished = true;
        EventBus.getDefault().unregister(this);
        viewModel.onDestroy();
        super.onDestroy();
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDefaultEvent(o: Object) {

    }

    override fun onBackPressed() {
        super.onBackPressed();
    }

}
