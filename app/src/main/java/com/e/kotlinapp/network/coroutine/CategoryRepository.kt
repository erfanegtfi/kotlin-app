package com.e.kotlinapp.network.coroutine

import androidx.lifecycle.liveData
import com.e.kotlinapp.local.dao.CategoryDao
import com.e.kotlinapp.model.response.base.ApiListResponse
import com.e.kotlinapp.model.Category
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val movieApiService: ApiInterfaceCoroutine) : BaseDataSource() {


//    fun getCategoryList(): LiveData<ResponseResult<ApiListResponse<Category>>> = resultLiveData() {
//        getResult {
//            movieApiService.getCategoryList()
//        }
//    }
//    fun <T> resultLiveData(call: suspend () -> ResponseResult<T>): LiveData<ResponseResult<T>> {
//        return liveData(Dispatchers.IO) {
//            emit(ResponseResult.Loading)
//            emit(call.invoke())
//        }
//    }

    /////////////////////////////////////////////////////
    //return type: LiveData<ResponseResult<ApiListResponse<Category>>>
    fun getCategoryListRemote(getNewData: Boolean) = liveData(Dispatchers.IO) {
        emit(ResponseResult.loading())
        //return type: ResponseResult<ApiListResponse<Category>>
        if (getNewData) {
            val result = getResult {
                movieApiService.getCategoryList()
            }
            if (result is ResponseResult.Success)
                result.response.data
            emit(result)
        }
    }


    /////////////////////////////////////////////////////////////////////

    suspend fun getCategoryList2(getNewData: Boolean): ResponseResult<ApiListResponse<Category>> {
        return getResult {
            movieApiService.getCategoryList()
        }
    }

}