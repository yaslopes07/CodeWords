package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.usuarioDAO
import com.example.myapplication.data.local.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val usuarioDAO: usuarioDAO) {

    suspend fun fazerLogin(login: String, senha: String): UsuarioEntity? {
        garantirAdminInicial()
        return usuarioDAO.autenticar(login.trim(), senha)
    }

    suspend fun cadastrarUsuario(
        login: String,
        senha: String,
        nome: String = "",
        papel: String = "JOGADOR",
        fotoUri: String? = null,
        ativo: Boolean = true
    ): Result<UsuarioEntity> {
        val loginLimpo = login.trim()
        val existente = usuarioDAO.buscarPorLogin(loginLimpo)
        if (existente != null) {
            return Result.failure(Exception("Este email ou usuário já está cadastrado."))
        }

        val nomeFinal = nome.ifBlank { loginLimpo.substringBefore("@") }

        val novoUsuario = UsuarioEntity(
            emailUsuario = loginLimpo,
            senha = senha,
            nome = nomeFinal,
            papel = papel,
            ativo = ativo,
            fotoUri = fotoUri
        )
        val idGerado = usuarioDAO.inserirUsuario(novoUsuario)
        return Result.success(novoUsuario.copy(id = idGerado.toInt()))
    }

    suspend fun atualizarUsuario(usuario: UsuarioEntity): Result<Unit> {
        return try {
            usuarioDAO.atualizarUsuario(usuario)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletarUsuario(id: Int): Result<Unit> {
        return try {
            usuarioDAO.deletarPorId(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun obterTodosUsuariosFlow(): Flow<List<UsuarioEntity>> {
        return usuarioDAO.listarTodosFlow()
    }

    suspend fun buscarTodosUsuarios(): List<UsuarioEntity> {
        garantirAdminInicial()
        return usuarioDAO.listarTodos()
    }

    suspend fun garantirAdminInicial() {
        val todos = usuarioDAO.listarTodos()
        if (todos.isEmpty()) {
            usuarioDAO.inserirUsuario(
                UsuarioEntity(
                    nome = "Admin Mestre",
                    emailUsuario = "admin@codewords.com",
                    senha = "admin",
                    papel = "ADMIN",
                    ativo = true,
                    dataCadastro = "Nov. 2026"
                )
            )
            usuarioDAO.inserirUsuario(
                UsuarioEntity(
                    nome = "PedroPlay",
                    emailUsuario = "pedro@codewords.com",
                    senha = "123",
                    papel = "JOGADOR",
                    ativo = true,
                    dataCadastro = "Nov. 2026"
                )
            )
        }
    }
}