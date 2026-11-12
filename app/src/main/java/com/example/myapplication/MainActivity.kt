package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.entity.UsuarioEntity
import com.example.myapplication.ui.telas.TelaCadastroRoute
import com.example.myapplication.ui.telas.TelaFase2Route
import com.example.myapplication.ui.telas.TelaFase3Route
import com.example.myapplication.ui.telas.TelaFase4Route
import com.example.myapplication.ui.telas.TelaLoginRoute
import com.example.myapplication.ui.telas.TelaMenuFases
import com.example.myapplication.ui.telas.TelaRankingRoute
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val context = LocalContext.current
                val progressoDao = remember { AppDatabase.getDatabase(context).progressoFaseDao() }

                var telaAtual by remember { mutableStateOf("login") }
                var usuarioLogado by remember { mutableStateOf<UsuarioEntity?>(null) }
                var fasesConcluidas by remember { mutableStateOf<Set<Int>>(emptySet()) }

                LaunchedEffect(telaAtual, usuarioLogado) {
                    val userId = usuarioLogado?.id ?: return@LaunchedEffect
                    if (telaAtual == "menu_fases") {
                        fasesConcluidas = progressoDao.buscarFasesConcluidasDoUsuario(userId).toSet()
                    }
                }

                when (telaAtual) {
                    "login" -> TelaLoginRoute(
                        onLoginSucesso = { usuario ->
                            usuarioLogado = usuario
                            telaAtual = "menu_fases"
                        },
                        onCadastrarClick = { telaAtual = "cadastro" }
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
                    "perfil" -> {
                        // Aqui vamos conectar a TelaPerfilRoute assim que você mandar o código dela!
                    }
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