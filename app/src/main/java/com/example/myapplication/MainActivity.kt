package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.entity.UsuarioEntity
import com.example.myapplication.ui.telas.TelaCadastroRoute
import com.example.myapplication.ui.telas.TelaFase1Route
import com.example.myapplication.ui.telas.TelaFase2Route
import com.example.myapplication.ui.telas.TelaFase3Route
import com.example.myapplication.ui.telas.TelaFase4Route
import com.example.myapplication.ui.telas.TelaLoginRoute
import com.example.myapplication.ui.telas.TelaMenuFases
import com.example.myapplication.ui.telas.TelaPainelAdminRoute
import com.example.myapplication.ui.telas.TelaPerfilRoute
import com.example.myapplication.ui.telas.TelaRankingRoute
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewModel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val context = LocalContext.current
                val database = remember { AppDatabase.getDatabase(context) }
                val progressoDao = remember { database.progressoFaseDao() }
                val usuarioDao = remember { database.usuarioDao() }
                val prefs = remember { context.getSharedPreferences("sessao_app", Context.MODE_PRIVATE) }
                val loginViewModel: LoginViewModel = viewModel()

                var verificandoSessao by remember { mutableStateOf(true) }
                var telaAtual by remember { mutableStateOf("login") }
                var usuarioLogado by remember { mutableStateOf<UsuarioEntity?>(null) }
                var fasesConcluidas by remember { mutableStateOf<Set<Int>>(emptySet()) }

                LaunchedEffect(Unit) {
                    val idSalvo = prefs.getInt("usuario_id", 0)
                    if (idSalvo != 0) {
                        val usuarioEncontrado = usuarioDao.buscarPorId(idSalvo)
                        if (usuarioEncontrado != null) {
                            usuarioLogado = usuarioEncontrado
                            telaAtual = if (usuarioEncontrado.papel.equals("ADMIN", ignoreCase = true)) {
                                "admin"
                            } else {
                                "menu_fases"
                            }
                        } else {
                            prefs.edit { remove("usuario_id") }
                        }
                    }
                    verificandoSessao = false
                }

                LaunchedEffect(telaAtual, usuarioLogado) {
                    val userId = usuarioLogado?.id ?: return@LaunchedEffect
                    if (telaAtual == "menu_fases") {
                        fasesConcluidas = progressoDao.buscarFasesConcluidasDoUsuario(userId).toSet()
                    }
                }

                if (verificandoSessao) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF141E26))
                    )
                } else {
                    when (telaAtual) {
                        "login" -> TelaLoginRoute(
                            viewModel = loginViewModel,
                            onLoginSucesso = { usuario ->
                                prefs.edit { putInt("usuario_id", usuario.id) }
                                usuarioLogado = usuario
                                telaAtual = if (usuario.papel.equals("ADMIN", ignoreCase = true)) {
                                    "admin"
                                } else {
                                    "menu_fases"
                                }
                            },
                            onCadastrarClick = { telaAtual = "cadastro" }
                        )

                        "admin" -> TelaPainelAdminRoute(
                            nomeAdminLogado = usuarioLogado?.nome?.ifBlank { usuarioLogado?.emailUsuario } ?: "Administrador",
                            onLogoutClick = {
                                prefs.edit { remove("usuario_id") }
                                loginViewModel.resetarLogin()
                                usuarioLogado = null
                                fasesConcluidas = emptySet()
                                telaAtual = "login"
                            }
                        )

                        "cadastro" -> TelaCadastroRoute(
                            onCadastroSucesso = { telaAtual = "login" },
                            onVoltarLoginClick = { telaAtual = "login" }
                        )

                        "menu_fases" -> TelaMenuFases(
                            fasesConcluidas = fasesConcluidas,
                            onFaseClick = { numeroFase ->
                                telaAtual = "fase$numeroFase"
                            },
                            onRankingClick = { telaAtual = "ranking" },
                            onPerfilClick = { telaAtual = "perfil" }
                        )

                        "ranking" -> TelaRankingRoute(
                            usuarioId = usuarioLogado?.id ?: 0,
                            onFasesClick = { telaAtual = "menu_fases" },
                            onPerfilClick = { telaAtual = "perfil" }
                        )

                        "perfil" -> TelaPerfilRoute(
                            usuarioLogado = usuarioLogado,
                            onSairClick = {
                                prefs.edit { remove("usuario_id") }
                                loginViewModel.resetarLogin()
                                usuarioLogado = null
                                fasesConcluidas = emptySet()
                                telaAtual = "login"
                            },
                            onNavegarFases = { telaAtual = "menu_fases" },
                            onNavegarRanking = { telaAtual = "ranking" }
                        )

                        "fase1" -> TelaFase1Route(
                            usuarioId = usuarioLogado?.id ?: 0,
                            onVoltarClick = { telaAtual = "menu_fases" }
                        )

                        "fase2" -> TelaFase2Route(
                            usuarioId = usuarioLogado?.id ?: 0,
                            onVoltarClick = { telaAtual = "menu_fases" }
                        )

                        "fase3" -> TelaFase3Route(
                            usuarioId = usuarioLogado?.id ?: 0,
                            onVoltarClick = { telaAtual = "menu_fases" }
                        )

                        "fase4" -> TelaFase4Route(
                            usuarioId = usuarioLogado?.id ?: 0,
                            onVoltarClick = { telaAtual = "menu_fases" }
                        )
                    }
                }
            }
        }
    }
}
