package com.example.myapplication.ui.state

import com.example.myapplication.data.local.entity.UsuarioEntity

data class LoginUiState(
    val emailOuUsuario: String = "",
    val senha: String = "",
    val mensagemErro: String? = null,
    val usuarioLogado: UsuarioEntity? = null
)