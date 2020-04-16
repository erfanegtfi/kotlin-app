package com.e.kotlinapp.model.response.base;

import com.google.gson.annotations.SerializedName;


class ApiListResponse<T>(
    @SerializedName("data") var data: MutableList<T>
) : ApiBaseResponse() {


}