package com.example.myapplication.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.myapplication.data.local.entity.UsuarioEntity

@Dao
interface usuarioDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun inserirUsuario(usuario: UsuarioEntity): Long

    @Query("SELECT * FROM usuarios WHERE emailUsuario = :login AND senha = :senha LIMIT 1")
    suspend fun autenticar(login: String, senha: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE emailUsuario = :login LIMIT 1")
    suspend fun buscarPorLogin(login: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): UsuarioEntity?
}