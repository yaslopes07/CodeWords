package com.example.myapplication.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.myapplication.data.local.entity.ProgressoFaseEntity

@Dao
interface ProgressoFaseDao {

    @Insert
    suspend fun salvarProgresso(progresso: ProgressoFaseEntity)

    @Query("SELECT DISTINCT numeroFase FROM progresso_fases WHERE usuarioId = :usuarioId")
    suspend fun buscarFasesConcluidasDoUsuario(usuarioId: Int): List<Int>

    @Query("SELECT * FROM progresso_fases WHERE usuarioId = :usuarioId AND numeroFase = :numeroFase ORDER BY tempoSegundos ASC LIMIT 1")
    suspend fun buscarMelhorTempo(usuarioId: Int, numeroFase: Int): ProgressoFaseEntity?
}

