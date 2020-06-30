package com.e.kotlinapp.network.flow

import androidx.annotation.MainThread
import com.e.kotlinapp.model.response.base.ApiBaseResponse
import com.e.kotlinapp.network.BaseRepository
import com.e.kotlinapp.network.coroutine.ResponseResultWithWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import retrofit2.Response

/**
 * A repository which provides resource from local database as well as remote end point.
 *
 * [ResultType] represents the type for database.
 * [RequestType] represents the type for network.
 */

//https://github.com/skydoves/GithubFollows/blob/master/app/src/main/java/com/skydoves/githubfollows/repository/NetworkBoundRepository.kt

@ExperimentalCoroutinesApi
abstract class NetworkBoundRepository<REQUEST: ApiBaseResponse> :BaseRepository(){

    fun asFlow() = flow {

        // Emit Loading State
        emit(ResponseResultWithWrapper.Loading)

        try {
            // Emit Database content first

            // Fetch latest posts from remote
            val apiResponse = fetchFromRemote()

            // Parse body
            val remotePosts = apiResponse.body()

            // Check for response validation
            if (apiResponse.isSuccessful && remotePosts != null) {
                emit(onResponse<REQUEST>(remotePosts))
            } else {
                // Something went wrong! Emit Error state.
                emit(error(apiResponse.errorBody()))
            }
        } catch (e: Throwable) {
            // Exception occurred! Emit error
            emit(error(e))
            e.printStackTrace()
        }

    }

    /**
     * Fetches [Response] from the remote end point.
     */
    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>
}