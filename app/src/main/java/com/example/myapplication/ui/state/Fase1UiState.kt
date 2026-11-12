package com.example.myapplication.ui.state

data class Fase1UiState(
    val tema: String = "COMANDOS KOTLIN",
    val numeroFase: String = "Fase 1 | 8X12",
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