package com.e.kotlinapp.model.response.base;


import com.google.gson.annotations.SerializedName;


class ApiSingleResponse<T>(
    @SerializedName("data") var data: T
) : ApiBaseResponse() {


}
