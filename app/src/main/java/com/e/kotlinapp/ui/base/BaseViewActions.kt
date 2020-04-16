package com.e.kotlinapp.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.e.kotlinapp.R
import com.e.kotlinapp.model.response.base.ApiBaseResponse


class BaseViewActions private constructor() {

    private lateinit var progress: Dialog
    private lateinit var context: Context

    companion object {
        fun getInstance(context: Context) =  BaseViewActions().also {
            it.context = context
        }
    }

//    public static BaseViewActions getInstance() {
//        return new BaseViewActions();
//    }

    fun showLoading(message: String?) {
    }

    public fun showLoading() {
        progress = Dialog(context, R.style.CustomLoadingDialogTheme);
        progress.window?.setBackgroundDrawableResource(android.R.color.transparent);
        progress.window?.requestFeature(Window.FEATURE_NO_TITLE);

        progress.setContentView(ProgressBar(context));
        if (!progress.isShowing) progress.show();
    }

    public fun hideLoading() {
        if (progress.isShowing) progress.dismiss();
    }

    public fun unauthorizedUser(response: ApiBaseResponse?) {
//        if (response.httpCode == 401) {
//            UtilsMessage.showLovelyStandardDialog(
//                context,
//                R.string.auth_dialog_login_required_login,
//                R.string.auth_dialog_login_required_register,
//                R.string.auth_dialog_login_required_close,
//                R.string.auth_dialog_login_required_error,
//                R.string.auth_dialog_login_required_title,
//                R.drawable.ic_nav_user,
//                (dialogInterface,
//                i
//            ) -> AuthenticationActivity.start(context, AuthenticationActivity.OPEN_TYPE_LOGIN)
//            , (dialogInterface, i) -> AuthenticationActivity.start(context, AuthenticationActivity.OPEN_TYPE_REGISTER)
//            , (dialogInterface, i) -> {
//
//            });
//        } else { // on wrong verification code
//            onResponseMessage(response, context);
//        }
    }


    fun onTimeout(throwable: Throwable?) {

    }

    fun onNetworkError(throwable: Throwable?) {
        Toast.makeText(context, "خطا در اتصال به اینترنت!", Toast.LENGTH_SHORT).show();
    }

    fun onError(throwable: Throwable?, message: ApiBaseResponse?) {

    }

    fun onResponseMessage(message: ApiBaseResponse?) {
//        if (!message.msg.isNullOrEmpty()) //&& !message.isSuccess()
//            if (message.httpCode == Constants.CODE_SUCCESS) {
//                if (message.messageShowType == MessageShowType.DIALOG) UtilsMessage.showDialog(context, "تایید", "", message.msg);
//                else //if (message.getMessageShowType() == MessageShowType.TOAST)
//                    Toast.makeText(context, message.getMsg(), Toast.LENGTH_SHORT).show();
//            } else UtilsMessage.showDialog(context, "تایید", "", message.getMsg());
    }


}
