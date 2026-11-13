package com.example.myapplication.ui.state

data class FasePerfilItem(
    val numero: Int,
    val titulo: String,
    val subtitulo: String,
    val tempoTexto: String,
    val concluida: Boolean
)

data class PerfilUiState(
    val nomeUsuario: String = "Jogador",
    val fotoUri: String? = null,
    val nivel: Int = 1,
    val progressoNivel: Float = 0f,
    val melhorTempoGeral: String = "--:--",
    val fasesConcluidasTexto: String = "0/4",
    val listaFases: List<FasePerfilItem> = listOf(
        FasePerfilItem(1, "Fase 1", "Primeiros Passos", "Não Concluída", false),
        FasePerfilItem(2, "Fase 2", "Conexões", "Não Concluída", false),
        FasePerfilItem(3, "Fase 3", "Precisão", "Não Concluída", false),
        FasePerfilItem(4, "Fase 4", "Desafio", "Não Concluída", false)
    )
)