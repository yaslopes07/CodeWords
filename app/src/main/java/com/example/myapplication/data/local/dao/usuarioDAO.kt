package com.example.myapplication.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.example.myapplication.data.local.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface usuarioDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun inserirUsuario(usuario: UsuarioEntity): Long

    @Update
    suspend fun atualizarUsuario(usuario: UsuarioEntity): Int

    @Delete
    suspend fun deletarUsuario(usuario: UsuarioEntity): Int

    @Query("DELETE FROM usuarios WHERE id = :id")
    suspend fun deletarPorId(id: Int): Int

    @Query("SELECT * FROM usuarios WHERE (emailUsuario = :login OR nome = :login) AND senha = :senha LIMIT 1")
    suspend fun autenticar(login: String, senha: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE emailUsuario = :login OR nome = :login LIMIT 1")
    suspend fun buscarPorLogin(login: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): UsuarioEntity?

    @Query("SELECT * FROM usuarios ORDER BY id DESC")
    fun listarTodosFlow(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios ORDER BY id DESC")
    suspend fun listarTodos(): List<UsuarioEntity>
}