package com.e.kotlinapp.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.e.kotlinapp.local.dao.CategoryDao
import com.e.kotlinapp.model.Category

/**
 * Abstract Foodium database.
 * It provides DAO [PostsDao] by using method [getPostsDao].
 */
@Database(entities = [Category::class], version = DatabaseMigrations.DB_VERSION)
abstract class IkoponCategoryDatabase : RoomDatabase() {

    /**
     * @return [PostsDao] Foodium Posts Data Access Object.
     */
    abstract fun getPostsDao(): CategoryDao

    companion object {
        const val DB_NAME = "foodium_database"

        @Volatile
        private var INSTANCE: IkoponCategoryDatabase? = null

        fun getInstance(context: Context): IkoponCategoryDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, IkoponCategoryDatabase::class.java, DB_NAME
                ).addMigrations(*DatabaseMigrations.MIGRATIONS).build()

                INSTANCE = instance
                return instance
            }
        }

    }
}