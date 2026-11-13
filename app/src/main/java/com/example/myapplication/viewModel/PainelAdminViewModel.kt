package com.example.myapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.entity.UsuarioEntity
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.ui.telas.PapelUsuario
import com.example.myapplication.ui.telas.UsuarioItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PainelAdminUiState(
    val usuarios: List<UsuarioItem> = emptyList(),
    val carregando: Boolean = true,
    val mensagemStatus: String? = null
)

class PainelAdminViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsuarioRepository

    private val _uiState = MutableStateFlow(PainelAdminUiState())
    val uiState: StateFlow<PainelAdminUiState> = _uiState.asStateFlow()

    init {
        val dao = AppDatabase.getDatabase(application).usuarioDao()
        repository = UsuarioRepository(dao)

        viewModelScope.launch {
            repository.garantirAdminInicial()
            repository.obterTodosUsuariosFlow().collectLatest { entities ->
                val items = entities.map { entity ->
                    UsuarioItem(
                        id = entity.id.toLong(),
                        nome = entity.nome.ifBlank { entity.emailUsuario.substringBefore("@") },
                        email = entity.emailUsuario,
                        papel = if (entity.papel.equals("ADMIN", ignoreCase = true)) PapelUsuario.ADMIN else PapelUsuario.JOGADOR,
                        ativo = entity.ativo,
                        dataCadastro = entity.dataCadastro,
                        fotoUri = entity.fotoUri
                    )
                }
                _uiState.update { it.copy(usuarios = items, carregando = false) }
            }
        }
    }

    fun criarUsuario(item: UsuarioItem, senhaProvisoria: String) {
        viewModelScope.launch {
            repository.cadastrarUsuario(
                login = item.email,
                senha = senhaProvisoria.ifBlank { "123456" },
                nome = item.nome,
                papel = item.papel.name,
                ativo = item.ativo,
                fotoUri = item.fotoUri
            )
        }
    }

    fun atualizarUsuario(item: UsuarioItem) {
        viewModelScope.launch {
            val existente = AppDatabase.getDatabase(getApplication()).usuarioDao().buscarPorId(item.id.toInt())
            val senhaManter = existente?.senha ?: "123456"
            val entity = UsuarioEntity(
                id = item.id.toInt(),
                emailUsuario = item.email,
                senha = senhaManter,
                nome = item.nome,
                papel = item.papel.name,
                ativo = item.ativo,
                dataCadastro = item.dataCadastro,
                fotoUri = item.fotoUri ?: existente?.fotoUri
            )
            repository.atualizarUsuario(entity)
        }
    }

    fun deletarUsuario(id: Long) {
        viewModelScope.launch {
            repository.deletarUsuario(id.toInt())
        }
    }
}
