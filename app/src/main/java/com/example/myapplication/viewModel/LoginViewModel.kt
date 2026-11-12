package com.example.myapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.ui.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsuarioRepository

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        val dao = AppDatabase.getDatabase(application).usuarioDAO()
        repository = UsuarioRepository(dao)
    }

    fun onEmailOuUsuarioChange(novoValor: String) {
        _uiState.update { it.copy(emailOuUsuario = novoValor, mensagemErro = null) }
    }

    fun onSenhaChange(novaSenha: String) {
        _uiState.update { it.copy(senha = novaSenha, mensagemErro = null) }
    }

    fun entrar() {
        val estado = _uiState.value
        if (estado.emailOuUsuario.isBlank() || estado.senha.isBlank()) {
            _uiState.update { it.copy(mensagemErro = "Preencha o email/usuário e a senha.") }
            return
        }

        viewModelScope.launch {
            val usuario = repository.fazerLogin(estado.emailOuUsuario, estado.senha)
            if (usuario != null) {
                _uiState.update { it.copy(usuarioLogado = usuario, mensagemErro = null) }
            } else {
                _uiState.update { it.copy(mensagemErro = "Usuário ou senha inválidos.") }
            }
        }
    }
}