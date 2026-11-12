package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.UsuarioDAO
import com.example.myapplication.data.local.entity.UsuarioEntity

class UsuarioRepository(private val usuarioDAO: UsuarioDAO){

    suspend fun fazerLogin(login: String, senha: String): UsuarioEntity? {
        return usuarioDAO.autenticar(login.trim(), senha)
    }

    suspend fun cadastrarUsuario(login: String, senha: String): Result<UsuarioEntity> {
        val loginLimpo = login.trim()
        val existente = usuarioDAO.buscarPorLogin(loginLimpo)
        if (existente != null) {
            return Result.failure(Exception("Este email ou usuário já está cadastrado."))
        }

        val novoUsuario = UsuarioEntity(emailUsuario = loginLimpo, senha = senha)
        val idGerado = usuarioDAO.inserirUsuario(novoUsuario)
        return Result.success(novoUsuario.copy(id = idGerado.toInt()))
    }
}