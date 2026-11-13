package com.example.myapplication.ui.state

data class CadastroUiState(
    val emailOuUsuario: String = "",
    val senha: String = "",
    val confirmarSenha: String = "",
    val fotoUri: String? = null,
    val mensagemErro: String? = null,
    val cadastroConcluido: Boolean = false
)