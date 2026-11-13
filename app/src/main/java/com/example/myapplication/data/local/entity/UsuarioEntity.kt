package com.example.myapplication.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val emailUsuario: String,
    val senha: String,
    val nome: String = "",
    val papel: String = "JOGADOR",
    val ativo: Boolean = true,
    val dataCadastro: String = "Nov. 2026",
    val fotoUri: String? = null
)