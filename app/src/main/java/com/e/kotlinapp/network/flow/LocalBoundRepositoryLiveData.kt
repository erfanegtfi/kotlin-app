package com.e.kotlinapp.network.flow

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.e.kotlinapp.model.Category
import com.e.kotlinapp.model.response.base.ApiBaseResponse
import com.e.kotlinapp.model.response.base.ApiListResponse
import com.e.kotlinapp.network.BaseRepository
import com.e.kotlinapp.network.UtilsError
import com.e.kotlinapp.network.coroutine.ResponseResult
import com.e.kotlinapp.network.coroutine.ResponseResultErrors
import com.e.kotlinapp.network.coroutine.ResponseResultWithWrapper
import com.e.kotlinapp.network.coroutine.ResponseWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import retrofit2.Response

/**
 * A repository which provides resource from local database as well as remote end point.
 *
 * [ResultType] represents the type for database.
 * [RequestType] represents the type for network.
 */

//https://github.com/skydoves/GithubFollows/blob/master/app/src/main/java/com/skydoves/githubfollows/repository/NetworkBoundRepository.kt

/**
 * A repository which provides resource from local database as well as remote end point.
 *
 * [RESULT] represents the type for database.
 * [REQUEST] represents the type for network.
 */
@ExperimentalCoroutinesApi
abstract class LocalBoundRepositoryLiveData<RESULT, REQUEST> :BaseRepository(){

    fun asFlow() = liveData<ResponseResultWithWrapper<ResponseWrapper<RESULT>>>(Dispatchers.IO) {

        // Emit Loading State
        emit(ResponseResultWithWrapper.Loading)

        try {
            // Emit Database content first
            emit(ResponseResultWithWrapper.Success(ResponseWrapper(data = fetchFromLocal().value)))

            // Fetch latest posts from remote
            val apiResponse = fetchFromRemote()

            // Parse body
            val remotePosts = apiResponse.body()

            // Check for response validation
            if (apiResponse.isSuccessful && remotePosts != null) {

                // Save posts into the persistence storage
                saveRemoteData(remotePosts)
            } else {
                // Something went wrong! Emit Error state.
                emit(error(apiResponse.errorBody()))
            }
        } catch (e: Throwable) {
            // Exception occurred! Emit error
            emit(error(e))
            e.printStackTrace()
        }

        // Retrieve posts from persistence storage and emit
        emitSource(fetchFromLocal().map {
            ResponseResultWithWrapper.Success(ResponseWrapper(data = it))
        })
    }

    /**
     * Saves retrieved from remote into the persistence storage.
     */
    @WorkerThread
    protected abstract suspend fun saveRemoteData(response: REQUEST)

    /**
     * Retrieves all data from persistence storage.
     */
    @MainThread
    protected abstract fun fetchFromLocal(): LiveData<RESULT>

    /**
     * Fetches [Response] from the remote end point.
     */
    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>
}