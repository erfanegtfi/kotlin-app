package com.e.kotlinapp.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.e.kotlinapp.local.dao.CategoryDao
import com.e.kotlinapp.model.Category

/**
 * Abstract Foodium database.
 * It provides DAO [PostsDao] by using method [getCategoryDao].
 */
@Database(entities = [Category::class], version = DatabaseMigrations.DB_VERSION)
abstract class IkoponDatabase : RoomDatabase() {

    /**
     * @return [PostsDao] Foodium Posts Data Access Object.
     */
    abstract fun getCategoryDao(): CategoryDao

    companion object {
        const val DB_NAME = "foodium_database"

        @Volatile
        private var INSTANCE: IkoponDatabase? = null

        fun getInstance(context: Context): IkoponDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, IkoponDatabase::class.java, DB_NAME
                ).addMigrations(*DatabaseMigrations.MIGRATIONS).build()

                INSTANCE = instance
                return instance
            }
        }

    }
}