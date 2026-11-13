package com.example.myapplication.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.myapplication.data.local.dao.ProgressoFaseDao
import com.example.myapplication.data.local.dao.usuarioDAO
import com.example.myapplication.data.local.entity.ProgressoFaseEntity
import com.example.myapplication.data.local.entity.UsuarioEntity

@Database(
    entities = [UsuarioEntity::class, ProgressoFaseEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): usuarioDAO
    abstract fun progressoFaseDao(): ProgressoFaseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "codewords_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
