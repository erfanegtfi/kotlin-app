package com.e.kotlinapp.model.response.base;

import com.google.gson.annotations.SerializedName;
import com.e.kotlinapp.MessageShowType;

open class ApiBaseResponse(
    @SerializedName("message") var msg: String? = null,
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("show_type") var showType: MessageShowType? = null,
    @SerializedName("http_code") var httpCode: Int? = null,
    @SerializedName("total") var total: Int? = null
) {


}
