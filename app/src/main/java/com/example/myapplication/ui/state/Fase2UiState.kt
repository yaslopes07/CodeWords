package com.example.myapplication.ui.state

data class Posicao(val linha: Int, val coluna: Int)

data class PalavraFase(
    val texto: String,
    val encontrada: Boolean = false,
    val posicoes: List<Posicao> = emptyList()
)

data class Fase2UiState(
    val tema: String = "LÓGICA EM CÓDIGO",
    val numeroFase: String = "Fase 2 | 8X12",
    val tempoDecorrido: String = "00:00",
    val palavras: List<PalavraFase> = emptyList(),
    val matrizLetras: List<List<Char>> = emptyList(),
    val posicoesSelecionada: List<Posicao> = emptyList(),
    val posicoesEncontradas: Set<Posicao> = emptySet(),
    val mensagemVitoria: Boolean = false
){
    val totalPalavras: Int get() = palavras.size
    val encontradasCount: Int get() = palavras.count { it.encontrada }
    val progresso: Float get() = if (totalPalavras > 0) encontradasCount.toFloat() / totalPalavras else 0f
}