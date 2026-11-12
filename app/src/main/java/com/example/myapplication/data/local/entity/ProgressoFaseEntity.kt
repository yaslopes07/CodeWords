package com.example.myapplication.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "progresso_fases")
data class ProgressoFaseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    val numeroFase: Int,
    val tempoSegundos: Int,
    val tempoFormatado: String
)
