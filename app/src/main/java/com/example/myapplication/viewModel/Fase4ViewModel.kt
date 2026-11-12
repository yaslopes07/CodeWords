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
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sin

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
        // Matriz 12 linhas x 8 colunas
        val matriz = listOf(
            "RMSCRUMY", // 0  -> SCRUM (horizontal, col 2-6) + início de REQUISITO (col 0)
            "ESNBIQPM", // 1  -> SPRINT começa em (1,1)
            "QZPJPLSG", // 2
            "UQERMVCO", // 3  -> MVC (horizontal, col 4-6)
            "IJEYIYDP", // 4
            "STZIONRI", // 5
            "IWZLTETT", // 6
            "TJPDXCVO", // 7
            "OETESTET", // 8  -> TESTE (horizontal, col 2-6)
            "DKLMUPRO", // 9  -> UML (de trás pra frente, col 4 -> 2)
            "GOLKCABR", // 10 -> BACKLOG (de trás pra frente, col 6 -> 0)
            "DEBUGDLP"  // 11 -> DEBUG (horizontal, col 0-4)
        ).map { it.toList() }

        val listaPalavras = listOf(
            // Vertical normal (cima -> baixo) - coluna 0, linhas 0 a 8
            PalavraFase("REQUISITO", posicoes = (0..8).map { Posicao(it, 0) }),

            // Vertical ao contrário (baixo -> cima) - coluna 7, linhas 11 a 3
            PalavraFase("PROTOTIPO", posicoes = (11 downTo 3).map { Posicao(it, 7) }),

            // Horizontal normal (esquerda -> direita) - linha 0
            PalavraFase("SCRUM", posicoes = (2..6).map { Posicao(0, it) }),

            // Horizontal ao contrário (direita -> esquerda) - linha 10
            PalavraFase("BACKLOG", posicoes = (6 downTo 0).map { Posicao(10, it) }),

            // Diagonal principal (\) - cima/esquerda -> baixo/direita
            PalavraFase("SPRINT", posicoes = (0..5).map { Posicao(1 + it, 1 + it) }),

            // Horizontal normal - linha 11
            PalavraFase("DEBUG", posicoes = (0..4).map { Posicao(11, it) }),

            // Diagonal secundária (/) - baixo/esquerda -> cima/direita
            PalavraFase("DEPLOY", posicoes = (0..5).map { Posicao(9 - it, it) }),

            // Horizontal normal - linha 8
            PalavraFase("TESTE", posicoes = (2..6).map { Posicao(8, it) }),

            // Horizontal normal - linha 3
            PalavraFase("MVC", posicoes = (4..6).map { Posicao(3, it) }),

            // Horizontal ao contrário - linha 9
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

    // ---------- Seleção por arrastar (marca-texto) ----------

    fun iniciarSelecao(posicao: Posicao) {
        if (_uiState.value.mensagemVitoria) return
        _uiState.update { it.copy(posicoesSelecionada = listOf(posicao)) }
    }

    fun arrastarSelecao(posicao: Posicao) {
        val inicio = _uiState.value.posicoesSelecionada.firstOrNull() ?: return
        _uiState.update { it.copy(posicoesSelecionada = calcularLinha(inicio, posicao)) }
    }

    fun finalizarSelecao() {
        val selecionadas = _uiState.value.posicoesSelecionada
        if (selecionadas.isNotEmpty()) verificarPalavraFormada(selecionadas)
        _uiState.update { it.copy(posicoesSelecionada = emptyList()) }
    }

    private fun calcularLinha(inicio: Posicao, fim: Posicao): List<Posicao> {
        val dl = fim.linha - inicio.linha
        val dc = fim.coluna - inicio.coluna
        if (dl == 0 && dc == 0) return listOf(inicio)

        val anguloGraus = Math.toDegrees(atan2(dl.toDouble(), dc.toDouble()))
        val anguloEncaixado = Math.toRadians((anguloGraus / 45.0).roundToInt() * 45.0)
        val passoLinha = sin(anguloEncaixado).roundToInt()
        val passoColuna = cos(anguloEncaixado).roundToInt()
        val comprimento = max(abs(dl), abs(dc))

        val estado = _uiState.value
        val totalLinhas = estado.matrizLetras.size
        val totalColunas = estado.matrizLetras.firstOrNull()?.size ?: 0

        val resultado = mutableListOf<Posicao>()
        for (i in 0..comprimento) {
            val l = inicio.linha + passoLinha * i
            val c = inicio.coluna + passoColuna * i
            if (l !in 0 until totalLinhas || c !in 0 until totalColunas) break
            resultado.add(Posicao(l, c))
        }
        return resultado
    }

    private fun verificarPalavraFormada(selecionadas: List<Posicao>) {
        val estadoAtual = _uiState.value
        val conjuntoSelecionado = selecionadas.toSet()

        val palavra = estadoAtual.palavras.firstOrNull {
            !it.encontrada && it.posicoes.toSet() == conjuntoSelecionado
        } ?: return

        val novasPalavras = estadoAtual.palavras.map { p ->
            if (p.texto == palavra.texto) p.copy(encontrada = true) else p
        }
        val novasEncontradas = estadoAtual.posicoesEncontradas + palavra.posicoes
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
                mensagemVitoria = vitoria
            )
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
