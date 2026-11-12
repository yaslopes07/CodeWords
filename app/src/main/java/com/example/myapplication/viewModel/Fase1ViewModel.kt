package com.example.cacapalavras.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.state.Fase1UiState
import com.example.myapplication.ui.state.PalavraFase
import com.example.myapplication.ui.state.Posicao
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Fase1ViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(Fase1UiState())
    val uiState: StateFlow<Fase1UiState> = _uiState.asStateFlow()
    private var cronometroJob: Job? = null
    private var segundosDecorridos = 0

    init {
        carregarFase1()
        iniciarCronometro()
    }

    private fun carregarFase1() {

        // Matriz 12 linhas x 8 colunas ou 11 linhas x 7 colunas
        val matriz = listOf(
            listOf('C', 'O', 'L', 'U', 'M', 'N', 'F', 'I'),
            listOf('U', 'O', 'M', 'O', 'B', 'I', 'N', 'H'),
            listOf('C', 'X', 'T', 'Z', 'W', 'I', 'V', 'E'),
            listOf('O', 'S', 'N', 'L', 'T', 'S', 'I', 'L'),
            listOf('M', 'T', 'R', 'O', 'P', 'M', 'I', 'L'),
            listOf('P', 'A', 'H', 'A', 'A', 'N', 'W', 'O'),
            listOf('O', 'E', 'C', 'A', 'C', 'S', 'E', 'W'),
            listOf('S', 'E', 'T', 'E', 'K', 'L', 'I', 'O'),
            listOf('R', 'A', 'I', 'D', 'A', 'G', 'H', 'R'),
            listOf('Z', 'V', 'C', 'A', 'G', 'N', 'M', 'L'),
            listOf('F', 'L', 'R', 'T', 'E', 'E', 'R', 'D'),
            listOf('O', 'V', 'E', 'R', 'R', 'I', 'D', 'E')
        )

        val listaPalavras = listOf(
            // Vertical Principal Normal (|) - Cima -> Baixo
            PalavraFase("HELLOWORLD", posicoes = listOf(
                Posicao(1, 7), Posicao(2, 7),
                Posicao(3, 7), Posicao(4, 7),
                Posicao(5, 7), Posicao(6, 7),
                Posicao(7, 7), Posicao(8, 7),
                Posicao(9, 7), Posicao(10, 7)
            )),

            // Horizontal Principal Normal (—) - Esquerda -> Direita
            PalavraFase("OVERRIDE", posicoes = listOf(
                Posicao(11, 0), Posicao(11, 1),
                Posicao(11, 2), Posicao(11, 3),
                Posicao(11, 4), Posicao(11, 5),
                Posicao(11, 6), Posicao(11, 7),
            )),

            // Horizontal Inversa (—) - (Direita -> Esquera)
            PalavraFase("IMPORT", posicoes = listOf(
                Posicao(6, 6), Posicao(5, 6),
                Posicao(4, 6), Posicao(3, 6),
                Posicao(2, 6), Posicao(1, 6)
            )),

            // Vertical Normal (Cima -> Baixo)
            PalavraFase("PACKAGE", posicoes = listOf(
                Posicao(4, 4), Posicao(5, 4),
                Posicao(6, 4), Posicao(7, 4),
                Posicao(8, 4), Posicao(9, 4),
                Posicao(10, 4)
            )),

            // Diagonal Inversa (Esquerda/Cima -> Direita/Baixo)
            PalavraFase("INIT", posicoes = listOf(
                Posicao(0, 7), Posicao(1, 6),
                Posicao(2, 5), Posicao(3, 4)
            )),

            // Diagonal Inversa (Baixo -> Cima)
            PalavraFase("SPACER", posicoes = listOf(
                Posicao(3, 5), Posicao(4, 4),
                Posicao(5, 3), Posicao(6, 2),
                Posicao(7,1), Posicao(8,0)
            )),

            // Horizontal Ao Contrário (Direita -> Esquerda)
            PalavraFase("COLUMN", posicoes = listOf(
                Posicao(0, 0), Posicao(0, 1),
                Posicao(0, 2), Posicao(0, 3),
                Posicao(0, 4), Posicao(0, 5)
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

        carregarFase1()

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