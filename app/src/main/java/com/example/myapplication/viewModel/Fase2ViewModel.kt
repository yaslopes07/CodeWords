package com.example.cacapalavras.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.state.Fase2UiState
import com.example.myapplication.ui.state.PalavraFase
import com.example.myapplication.ui.state.Posicao
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Fase2ViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(Fase2UiState())
    val uiState: StateFlow<Fase2UiState> = _uiState.asStateFlow()
    private var cronometroJob: Job? = null
    private var segundosDecorridos = 0

    init {
        carregarFase2()
        iniciarCronometro()
    }

    private fun carregarFase2() {

        val matriz = listOf(
            listOf('C', 'O', 'D', 'I', 'G', 'O', 'X', 'X'),
            listOf('L', 'B', 'S', 'P', 'Q', 'K', 'V', 'X'),
            listOf('A', 'A', 'C', 'I', 'G', 'O', 'L', 'X'),
            listOf('S', 'M', 'F', 'U', 'N', 'C', 'A', 'O'),
            listOf('S', 'S', 'O', 'M', 'N', 'C', 'F', 'I'),
            listOf('E', 'L', 'I', 'B', 'I', 'H', 'U', 'D'),
            listOf('T', 'Y', 'E', 'V', 'H', 'D', 'P', 'E'),
            listOf('K', 'R', 'O', 'T', 'E', 'V', 'Z', 'B'),
            listOf('S', 'I', 'T', 'E', 'A', 'T', 'A', 'U'),
            listOf('F', 'O', 'B', 'J', 'E', 'T', 'O', 'G'),
            listOf('B', 'M', 'R', 'E', 'D', 'E', 'Q', 'R'),
            listOf('S', 'T', 'R', 'I', 'N', 'G', 'X', 'Y')
        )

        val listaPalavras = listOf(
            PalavraFase("CODIGO", posicoes = listOf(
                Posicao(0, 0), Posicao(0, 1), Posicao(0, 2), Posicao(0, 3), Posicao(0, 4), Posicao(0, 5)
            )),

            PalavraFase("CLASSE", posicoes = listOf(
                Posicao(0, 0), Posicao(1, 0), Posicao(2, 0), Posicao(3, 0), Posicao(4, 0), Posicao(5, 0)
            )),

            PalavraFase("FUNCAO", posicoes = listOf(
                Posicao(3, 2), Posicao(3, 3), Posicao(3, 4), Posicao(3, 5), Posicao(3, 6), Posicao(3, 7)
            )),

            PalavraFase("LOGICA", posicoes = listOf(
                Posicao(2, 6), Posicao(2, 5), Posicao(2, 4), Posicao(2, 3), Posicao(2, 2), Posicao(2, 1)
            )),

            PalavraFase("VETOR", posicoes = listOf(
                Posicao(7, 5), Posicao(7, 4), Posicao(7, 3), Posicao(7, 2), Posicao(7, 1)
            )),

            PalavraFase("OBJETO", posicoes = listOf(
                Posicao(9, 1), Posicao(9, 2), Posicao(9, 3), Posicao(9, 4), Posicao(9, 5), Posicao(9, 6)
            )),

            PalavraFase("STRING", posicoes = listOf(
                Posicao(11, 0), Posicao(11, 1), Posicao(11, 2), Posicao(11, 3), Posicao(11, 4), Posicao(11, 5)
            )),

            PalavraFase("DEBUG", posicoes = listOf(
                Posicao(5, 7), Posicao(6, 7), Posicao(7, 7), Posicao(8, 7), Posicao(9, 7)
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

    fun reiniciarFase(){
        segundosDecorridos = 0

        carregarFase2()

        _uiState.update {
            it.copy(
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

