package com.example.myapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.entity.UsuarioEntity
import com.example.myapplication.ui.state.FasePerfilItem
import com.example.myapplication.ui.state.PerfilUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val progressoDao = AppDatabase.getDatabase(application).progressoFaseDao()

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    fun carregarPerfil(usuario: UsuarioEntity?) {
        if (usuario == null) return

        val nomeExibicao = usuario.emailUsuario.substringBefore("@").ifBlank { "Jogador" }

        viewModelScope.launch {
            val recordes = progressoDao.buscarRankingPessoalDoUsuario(usuario.id)
            val mapaRecordes = recordes.associateBy { it.numeroFase }

            val menorTempoSegundos = recordes.minOfOrNull { it.menorTempoSegundos }
            val melhorTempoTexto = if (menorTempoSegundos != null) {
                formatarTempo(menorTempoSegundos)
            } else {
                "--:--"
            }

            val fasesAtualizadas = listOf(
                criarItemFase(1, "Fase 1", "Primeiros Passos", mapaRecordes[1]?.menorTempoSegundos),
                criarItemFase(2, "Fase 2", "Conexões", mapaRecordes[2]?.menorTempoSegundos),
                criarItemFase(3, "Fase 3", "Precisão", mapaRecordes[3]?.menorTempoSegundos),
                criarItemFase(4, "Fase 4", "Desafio", mapaRecordes[4]?.menorTempoSegundos)
            )

            val totalJogaveis = 4
            val qtdConcluidas = recordes.count { it.numeroFase in 1..4 }
            val progresso = (qtdConcluidas.toFloat() / totalJogaveis.toFloat()).coerceIn(0f, 1f)

            _uiState.update {
                it.copy(
                    nomeUsuario = nomeExibicao,
                    fotoUri = usuario.fotoUri,
                    nivel = 1 + qtdConcluidas,
                    progressoNivel = progresso,
                    melhorTempoGeral = melhorTempoTexto,
                    fasesConcluidasTexto = "$qtdConcluidas/$totalJogaveis",
                    listaFases = fasesAtualizadas
                )
            }
        }
    }

    private fun criarItemFase(
        numero: Int,
        titulo: String,
        subtitulo: String,
        tempoSegundos: Int?
    ): FasePerfilItem {
        return if (tempoSegundos != null) {
            FasePerfilItem(
                numero = numero,
                titulo = titulo,
                subtitulo = subtitulo,
                tempoTexto = formatarTempo(tempoSegundos),
                concluida = true
            )
        } else {
            FasePerfilItem(
                numero = numero,
                titulo = titulo,
                subtitulo = subtitulo,
                tempoTexto = "Não Concluída",
                concluida = false
            )
        }
    }

    private fun formatarTempo(segundosTotais: Int): String {
        val minutos = segundosTotais / 60
        val segs = segundosTotais % 60
        return "%02d:%02d".format(minutos, segs)
    }
}