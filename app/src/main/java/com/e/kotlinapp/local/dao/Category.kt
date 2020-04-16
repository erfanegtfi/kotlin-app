package com.e.kotlinapp.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.e.kotlinapp.model.Category
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for [dev.shreyaspatil.foodium.data.local.FoodiumPostsDatabase]
 */
@Dao
interface CategoryDao {

    /**
     * Inserts [posts] into the [Post.TABLE_NAME] table.
     * Duplicate values are replaced in the table.
     * @param posts Posts
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(posts: List<Category>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(posts: Category)

    /**
     * Deletes all the posts from the [Post.TABLE_NAME] table.
     */
    @Query("DELETE FROM ${Category.TABLE_NAME}")
    fun deleteAllPosts()

    /**
     * Fetches the post from the [Post.TABLE_NAME] table whose id is [postId].
     * @param postId Unique ID of [Post]
     * @return [Flow] of [Post] from database table.
     */
    @Query("SELECT * FROM ${Category.TABLE_NAME} WHERE ID = :postId")
    fun getPostById(postId: Int): Flow<Category>

    /**
     * Fetches all the posts from the [Post.TABLE_NAME] table.
     * @return [Flow]
     */
    @Query("SELECT * FROM ${Category.TABLE_NAME}")
    fun getAllPosts(): Flow<List<Category>>

    /**
     * Fetches all the posts from the [Post.TABLE_NAME] table.
     * @return [Flow]
     */
    @Query("SELECT * FROM ${Category.TABLE_NAME}")
    fun getAllPosts2(): LiveData<List<Category>>
}