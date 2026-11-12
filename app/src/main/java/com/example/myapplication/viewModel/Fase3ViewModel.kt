package com.example.myapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.entity.ProgressoFaseEntity
import com.example.myapplication.ui.state.Fase3UiState
import com.example.myapplication.ui.state.PalavraFase
import com.example.myapplication.ui.state.Posicao
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Fase3ViewModel(application: Application) : AndroidViewModel(application) {

    private val progressoDao = AppDatabase.getDatabase(application).progressoFaseDao()
    private var usuarioIdAtual: Int = 0

    private val _uiState = MutableStateFlow(Fase3UiState())
    val uiState: StateFlow<Fase3UiState> = _uiState.asStateFlow()

    private var cronometroJob: Job? = null
    private var segundosDecorridos = 0

    init {
        carregarFase3()
        iniciarCronometro()
    }

    fun definirUsuario(usuarioId: Int) {
        usuarioIdAtual = usuarioId
    }

    private fun carregarFase3() {
        val matriz = listOf(
            listOf('K', 'A', 'B', 'C', 'D', 'E', 'F', 'Z'),
            listOf('U', 'O', 'M', 'O', 'B', 'I', 'L', 'E'),
            listOf('C', 'X', 'T', 'J', 'W', 'Q', 'V', 'Z'),
            listOf('O', 'S', 'N', 'L', 'Z', 'X', 'I', 'C'),
            listOf('M', 'T', 'F', 'D', 'I', 'E', 'E', 'M'),
            listOf('P', 'A', 'H', 'J', 'W', 'N', 'W', 'P'),
            listOf('O', 'E', 'T', 'A', 'T', 'S', 'E', 'M'),
            listOf('S', 'E', 'T', 'E', 'U', 'L', 'I', 'O'),
            listOf('E', 'A', 'I', 'D', 'D', 'G', 'H', 'O'),
            listOf('Z', 'V', 'C', 'A', 'B', 'N', 'M', 'R'),
            listOf('F', 'L', 'R', 'T', 'T', 'E', 'R', 'Z'),
            listOf('P', 'G', 'L', 'D', 'A', 'R', 'G', 'X')
        )

        val listaPalavras = listOf(
            PalavraFase("KOTLIN", posicoes = listOf(
                Posicao(0, 0), Posicao(1, 1), Posicao(2, 2), Posicao(3, 3), Posicao(4, 4), Posicao(5, 5)
            )),
            PalavraFase("GRADLE", posicoes = listOf(
                Posicao(11, 1), Posicao(10, 2), Posicao(9, 3), Posicao(8, 4), Posicao(7, 5), Posicao(6, 6)
            )),
            PalavraFase("VIEW", posicoes = listOf(
                Posicao(2, 6), Posicao(3, 6), Posicao(4, 6), Posicao(5, 6)
            )),
            PalavraFase("COMPOSE", posicoes = listOf(
                Posicao(2, 0), Posicao(3, 0), Posicao(4, 0), Posicao(5, 0), Posicao(6, 0), Posicao(7, 0), Posicao(8, 0)
            )),
            PalavraFase("MOBILE", posicoes = listOf(
                Posicao(1, 2), Posicao(1, 3), Posicao(1, 4), Posicao(1, 5), Posicao(1, 6), Posicao(1, 7)
            )),
            PalavraFase("ROOM", posicoes = listOf(
                Posicao(9, 7), Posicao(8, 7), Posicao(7, 7), Posicao(6, 7)
            )),
            PalavraFase("STATE", posicoes = listOf(
                Posicao(6, 5), Posicao(6, 4), Posicao(6, 3), Posicao(6, 2), Posicao(6, 1)
            ))
        )

        _uiState.update {
            it.copy(
                matrizLetras = matriz,
                palavras = listaPalavras
            )
        }
    }

    private fun iniciarCronometro() {
        cronometroJob?.cancel()
        cronometroJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                segundosDecorridos++
                val minutos = segundosDecorridos / 60
                val segs = segundosDecorridos % 60
                val tempoFormatado = "%02d:%02d".format(minutos, segs)

                _uiState.update { it.copy(tempoDecorrido = tempoFormatado) }
            }
        }
    }

    fun selecionarPosicao(posicao: Posicao) {
        val selecionadasAtuais = _uiState.value.posicoesSelecionada.toMutableList()

        if (selecionadasAtuais.contains(posicao)) {
            selecionadasAtuais.remove(posicao)
        } else {
            selecionadasAtuais.add(posicao)
        }

        _uiState.update { it.copy(posicoesSelecionada = selecionadasAtuais) }

        verificarPalavraFormada(selecionadasAtuais)
    }

    private fun verificarPalavraFormada(selecionadas: List<Posicao>) {
        val estadoAtual = _uiState.value

        for (palavra in estadoAtual.palavras) {
            if (!palavra.encontrada && selecionadas.size == palavra.posicoes.size) {
                val bateuTodasPosicoes = palavra.posicoes.all { selecionadas.contains(it) }

                if (bateuTodasPosicoes) {
                    val novasPalavras = estadoAtual.palavras.map { p ->
                        if (p.texto == palavra.texto) p.copy(encontrada = true) else p
                    }

                    val novasEncontradas = estadoAtual.posicoesEncontradas.toMutableSet()
                    novasEncontradas.addAll(palavra.posicoes)

                    val vitoria = novasPalavras.all { it.encontrada }
                    if (vitoria) {
                        cronometroJob?.cancel()
                        if (usuarioIdAtual != 0) {
                            val tempoFinalTexto = estadoAtual.tempoDecorrido
                            val tempoFinalSegundos = segundosDecorridos
                            viewModelScope.launch {
                                progressoDao.salvarProgresso(
                                    ProgressoFaseEntity(
                                        usuarioId = usuarioIdAtual,
                                        numeroFase = 3,
                                        tempoSegundos = tempoFinalSegundos,
                                        tempoFormatado = tempoFinalTexto
                                    )
                                )
                            }
                        }
                    }

                    _uiState.update {
                        it.copy(
                            palavras = novasPalavras,
                            posicoesEncontradas = novasEncontradas,
                            posicoesSelecionada = emptyList(),
                            mensagemVitoria = vitoria
                        )
                    }
                    break
                }
            }
        }
    }

    fun reiniciarFase() {
        segundosDecorridos = 0
        carregarFase3()
        _uiState.update {
            it.copy(
                tempoDecorrido = "00:00",
                posicoesSelecionada = emptyList(),
                posicoesEncontradas = emptySet(),
                mensagemVitoria = false
            )
        }
        iniciarCronometro()
    }

    override fun onCleared() {
        super.onCleared()
        cronometroJob?.cancel()
    }
}
