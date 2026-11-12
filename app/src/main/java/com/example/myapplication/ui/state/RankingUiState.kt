package com.example.myapplication.ui.state

data class ItemRankingFase(
    val posicao: Int,
    val nomeFase: String,
    val tempoFormatado: String
)

data class RankingUiState(
    val itensRanking: List<ItemRankingFase> = emptyList()
)