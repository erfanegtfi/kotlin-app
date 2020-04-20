package com.e.kotlinapp.network.flow

import androidx.lifecycle.LiveData
import com.e.kotlinapp.model.Category
import com.e.kotlinapp.model.response.base.ApiListResponse
import com.e.kotlinapp.network.coroutine.ApiInterfaceCoroutine
import com.e.kotlinapp.network.coroutine.ResponseResultWithWrapper
import retrofit2.Response
import com.e.kotlinapp.local.dao.CategoryDao
import com.e.kotlinapp.network.api.ApiClient.getApiClient
import com.e.kotlinapp.network.BaseRepository
import com.e.kotlinapp.network.coroutine.ResponseWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class CategoryRepositoryFlow(private val movieApiService: ApiInterfaceCoroutine, private val postsDao: CategoryDao) : BaseRepository(){

    // load using Flow, database first
    fun loadCategory(): Flow<ResponseResultWithWrapper<ResponseWrapper<List<Category>>>> {
        return object : LocalBoundRepository<List<Category>, ApiListResponse<Category>>() {

            override suspend fun saveRemoteData(response: ApiListResponse<Category>) = postsDao.insertPosts(response.data)

            override fun fetchFromLocal(): Flow<List<Category>> = postsDao.getAllPosts()

            override suspend fun fetchFromRemote(): Response<ApiListResponse<Category>> = getApiClient(ApiInterfaceCoroutine::class.java).getCategoryList()

        }.asFlow().flowOn(Dispatchers.IO)
    }

    // load using liveData, database first
    fun loadCategory11(): LiveData<ResponseResultWithWrapper<ResponseWrapper<List<Category>>>> {
        return object : LocalBoundRepositoryLiveData<List<Category>, ApiListResponse<Category>>() {

            override suspend fun saveRemoteData(response: ApiListResponse<Category>) = postsDao.insertPosts(response.data)

            override fun fetchFromLocal(): LiveData<List<Category>> = postsDao.getAllPosts2()

            override suspend fun fetchFromRemote(): Response<ApiListResponse<Category>> = getApiClient(ApiInterfaceCoroutine::class.java).getCategoryList()

        }.asFlow()
    }

    // load remote using Flow
    fun loadCategory2(): Flow<ResponseResultWithWrapper<ResponseWrapper<ApiListResponse<Category>>>> {
        return object : NetworkBoundRepository<ApiListResponse<Category>>() {

            override suspend fun fetchFromRemote(): Response<ApiListResponse<Category>> = getApiClient(ApiInterfaceCoroutine::class.java).getCategoryList()

        }.asFlow().flowOn(Dispatchers.IO)
    }


    fun loadCategory3() = flow {
        // Emit Loading State
        emit(ResponseResultWithWrapper.Loading)
        try {
            // Fetch latest posts from remote
            val apiResponse = getApiClient(ApiInterfaceCoroutine::class.java).getCategoryList()

            // Parse body
            val remotePosts = apiResponse.body()

            // Check for response validation
            if (apiResponse.isSuccessful && remotePosts != null) {
                emit(ResponseResultWithWrapper.Success(ResponseWrapper(data = remotePosts)))
            } else {
                // Something went wrong! Emit Error state.
                emit(error(apiResponse.errorBody()))
            }
        } catch (e: Throwable) {
            // Exception occurred! Emit error
            emit(error(e))
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)
}