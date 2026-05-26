package com.example.huertohogardefinitiveedition.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.huertohogardefinitiveedition.data.dao.ProductoDao
import com.example.huertohogardefinitiveedition.data.model.Producto

@Database(
    entities = [Producto::class],
    version = 1,
    exportSchema = false
)
abstract class ProductoDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao

    companion object {

        @Volatile
        private var INSTANCE: ProductoDatabase? = null

        fun getDatabase(context: Context): ProductoDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        ProductoDatabase::class.java,
                        "producto_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                INSTANCE = instance

                instance
            }
        }
    }
}