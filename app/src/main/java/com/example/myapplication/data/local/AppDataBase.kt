package com.example.myapplication.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.myapplication.data.local.dao.UsuarioDAO
import com.example.myapplication.data.local.entity.UsuarioEntity

@Database(entities = [UsuarioEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDAO(): UsuarioDAO

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "codewords_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}