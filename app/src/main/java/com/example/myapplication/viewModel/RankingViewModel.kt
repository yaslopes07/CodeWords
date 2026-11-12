package com.example.myapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.ui.state.ItemRankingFase
import com.example.myapplication.ui.state.RankingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RankingViewModel(application: Application) : AndroidViewModel(application) {

    private val progressoDao = AppDatabase.getDatabase(application).progressoFaseDao()

    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState: StateFlow<RankingUiState> = _uiState.asStateFlow()

    fun carregarRanking(usuarioId: Int) {
        if (usuarioId == 0) return

        viewModelScope.launch {
            val recordes = progressoDao.buscarRankingPessoalDoUsuario(usuarioId)

            val listaFormatada = recordes.mapIndexed { index, item ->
                val minutos = item.menorTempoSegundos / 60
                val segs = item.menorTempoSegundos % 60
                val tempoTexto = "%02d:%02d".format(minutos, segs)

                val tituloFase = when (item.numeroFase) {
                    1 -> "Fase 1 - Primeiros Passos"
                    2 -> "Fase 2 - Conexões"
                    3 -> "Fase 3 - Precisão"
                    4 -> "Fase 4 - Desafio"
                    else -> "Fase ${item.numeroFase}"
                }

                ItemRankingFase(
                    posicao = index + 1,
                    nomeFase = tituloFase,
                    tempoFormatado = tempoTexto
                )
            }

            _uiState.update { it.copy(itensRanking = listaFormatada) }
        }
    }
}