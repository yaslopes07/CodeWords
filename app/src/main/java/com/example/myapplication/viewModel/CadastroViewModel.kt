package com.example.myapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.ui.state.CadastroUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CadastroViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsuarioRepository

    private val _uiState = MutableStateFlow(CadastroUiState())
    val uiState: StateFlow<CadastroUiState> = _uiState.asStateFlow()

    init {
        val dao = AppDatabase.getDatabase(application).usuarioDao()
        repository = UsuarioRepository(dao)
    }

    fun onEmailOuUsuarioChange(novoValor: String) {
        _uiState.update { it.copy(emailOuUsuario = novoValor, mensagemErro = null) }
    }

    fun onSenhaChange(novaSenha: String) {
        _uiState.update { it.copy(senha = novaSenha, mensagemErro = null) }
    }

    fun onConfirmarSenhaChange(novaConfirmacao: String) {
        _uiState.update { it.copy(confirmarSenha = novaConfirmacao, mensagemErro = null) }
    }

    fun onFotoUriChange(fotoUri: String?) {
        _uiState.update { it.copy(fotoUri = fotoUri) }
    }

    fun cadastrar() {
        val estado = _uiState.value

        if (estado.emailOuUsuario.isBlank() || estado.senha.isBlank() || estado.confirmarSenha.isBlank()) {
            _uiState.update { it.copy(mensagemErro = "Preencha todos os campos.") }
            return
        }

        if (estado.senha != estado.confirmarSenha) {
            _uiState.update { it.copy(mensagemErro = "As senhas não coincidem.") }
            return
        }

        viewModelScope.launch {
            val resultado = repository.cadastrarUsuario(
                login = estado.emailOuUsuario,
                senha = estado.senha,
                fotoUri = estado.fotoUri
            )

            resultado.onSuccess {
                _uiState.update { it.copy(cadastroConcluido = true, mensagemErro = null) }
            }.onFailure { erro ->
                _uiState.update {
                    it.copy(mensagemErro = erro.message ?: "Erro ao cadastrar usuário.")
                }
            }
        }
    }
}