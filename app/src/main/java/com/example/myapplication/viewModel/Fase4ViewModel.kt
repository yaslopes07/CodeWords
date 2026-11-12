package com.example.myapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.entity.ProgressoFaseEntity
import com.example.myapplication.ui.state.Fase4UiState
import com.example.myapplication.ui.state.PalavraFase
import com.example.myapplication.ui.state.Posicao
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Fase4ViewModel(application: Application) : AndroidViewModel(application) {

    private val progressoDao = AppDatabase.getDatabase(application).progressoFaseDao()
    private var usuarioIdAtual: Int = 0

    private val _uiState = MutableStateFlow(Fase4UiState())
    val uiState: StateFlow<Fase4UiState> = _uiState.asStateFlow()

    private var cronometroJob: Job? = null
    private var segundosDecorridos = 0

    init {
        carregarFase4()
        iniciarCronometro()
    }

    fun definirUsuario(usuarioId: Int) {
        usuarioIdAtual = usuarioId
    }

    private fun carregarFase4() {

        val matriz = listOf(
            "RMSCRUMY",
            "ESNBIQPM",
            "QZPJPLSG",
            "UQERMVCO",
            "IJEYIYDP",
            "STZIONRI",
            "IWZLTETT",
            "TJPDXCVO",
            "OETESTET",
            "DKLMUPRO",
            "GOLKCABR",
            "DEBUGDLP"
        ).map { it.toList() }

        val listaPalavras = listOf(
            PalavraFase("REQUISITO", posicoes = (0..8).map { Posicao(it, 0) }),

            PalavraFase("PROTOTIPO", posicoes = (11 downTo 3).map { Posicao(it, 7) }),

            PalavraFase("SCRUM", posicoes = (2..6).map { Posicao(0, it) }),

            PalavraFase("BACKLOG", posicoes = (6 downTo 0).map { Posicao(10, it) }),

            PalavraFase("SPRINT", posicoes = (0..5).map { Posicao(1 + it, 1 + it) }),

            PalavraFase("DEBUG", posicoes = (0..4).map { Posicao(11, it) }),

            PalavraFase("DEPLOY", posicoes = (0..5).map { Posicao(9 - it, it) }),

            PalavraFase("TESTE", posicoes = (2..6).map { Posicao(8, it) }),

            PalavraFase("MVC", posicoes = (4..6).map { Posicao(3, it) }),

            PalavraFase("UML", posicoes = (4 downTo 2).map { Posicao(9, it) })
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
                _uiState.update { it.copy(tempoDecorrido = "%02d:%02d".format(minutos, segs)) }
            }
        }
    }

    fun selecionarPosicao(posicao: Posicao) {
        if (_uiState.value.mensagemVitoria) return

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
                                        numeroFase = 4,
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
        carregarFase4()
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